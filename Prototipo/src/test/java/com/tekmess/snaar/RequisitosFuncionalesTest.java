package com.tekmess.snaar;

import com.tekmess.snaar.controlador.servicio.AuthServicio;
import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;
import com.tekmess.snaar.controlador.servicio.ReporteServicio;
import com.tekmess.snaar.modelo.dao.IEmpleadoDAO;
import com.tekmess.snaar.modelo.dao.IReporteDAO;
import com.tekmess.snaar.modelo.dao.IUsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Anotacion;
import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.modelo.entidad.EstadoCuenta;
import com.tekmess.snaar.modelo.entidad.Reporte;
import com.tekmess.snaar.modelo.entidad.Rol;
import com.tekmess.snaar.modelo.entidad.Sesion;
import com.tekmess.snaar.modelo.entidad.Usuario;
import com.tekmess.snaar.util.CifradorContrasena;
import com.tekmess.snaar.util.ValidadorDatos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequisitosFuncionalesTest {

    private final CifradorContrasena cifrador = new CifradorContrasena();

    @Test
    @DisplayName("REQ001 - registra empleado valido y genera credenciales con primer acceso obligatorio")
    void registraEmpleadoGeneraCredenciales() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        EmpleadoServicio servicio = new EmpleadoServicio(empleadoDAO, usuarioDAO);

        String resultado = servicio.crearEmpleado(
                new Empleado("1712345678", "Danna Andrade", "DANNA.ANDRADE@TEKMESS.COM", Rol.JEFE_LOGISTICA)
        );

        assertTrue(resultado.contains("Empleado registrado exitosamente"));
        assertNotNull(empleadoDAO.buscarPorCedula("1712345678"));
        Usuario usuario = usuarioDAO.buscarPorCedula("1712345678");
        assertNotNull(usuario);
        assertEquals("dandrade", usuario.getNombreUsuario());
        assertTrue(usuario.isPrimerAcceso());
        assertNull(new ValidadorDatos().validarContrasena(usuario.getContrasenaTemporal()));
        assertTrue(cifrador.verificar(usuario.getContrasenaTemporal(), usuario.getContrasenaHash()));
    }

    @Test
    @DisplayName("REQ001 - rechaza registro con cedula duplicada")
    void rechazaCedulaDuplicada() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        empleadoDAO.crear(new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.GUARDIA));

        String resultado = new EmpleadoServicio(empleadoDAO, new FakeUsuarioDAO())
                .crearEmpleado(new Empleado("1712345678", "Ariel Llumiquinga", "ariel@tekmess.com", Rol.SUPERVISOR));

        assertTrue(resultado.contains("ya se encuentra registrada"));
    }

    @Test
    @DisplayName("REQ002 - lista el personal registrado con datos principales")
    void listaPersonalRegistrado() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        empleadoDAO.crear(new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.JEFE_LOGISTICA));
        empleadoDAO.crear(new Empleado("1723456789", "Ariel Llumiquinga", "ariel@tekmess.com", Rol.SUPERVISOR));

        List<Empleado> empleados = new EmpleadoServicio(empleadoDAO, new FakeUsuarioDAO()).consultarEmpleados();

        assertEquals(2, empleados.size());
        assertTrue(empleados.stream().anyMatch(e -> e.getCedula().equals("1723456789")
                && e.getRol() == Rol.SUPERVISOR
                && e.getCorreo().equals("ariel@tekmess.com")));
    }

    @Test
    @DisplayName("REQ003 - edita empleado existente despues de validar los nuevos datos")
    void editaEmpleadoExistente() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        empleadoDAO.crear(new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.GUARDIA));

        String resultado = new EmpleadoServicio(empleadoDAO, new FakeUsuarioDAO())
                .editarEmpleado(new Empleado("1712345678", "Danna Andrade", "danna.andrade@tekmess.com", Rol.SUPERVISOR));

        assertTrue(resultado.contains("actualizados exitosamente"));
        assertEquals(Rol.SUPERVISOR, empleadoDAO.buscarPorCedula("1712345678").getRol());
        assertEquals(1, empleadoDAO.totalEditados);
    }

    @Test
    @DisplayName("REQ004 - elimina empleado e invalida su usuario asociado")
    void eliminaEmpleadoYUsuario() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        empleadoDAO.crear(new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.GUARDIA));
        usuarioDAO.crear(new Usuario("1712345678", "dandrade", cifrador.cifrar("Temp123!")));

        String resultado = new EmpleadoServicio(empleadoDAO, usuarioDAO).eliminarEmpleado("1712345678");

        assertTrue(resultado.contains("eliminado del sistema exitosamente"));
        assertNull(empleadoDAO.buscarPorCedula("1712345678"));
        assertNull(usuarioDAO.buscarPorCedula("1712345678"));
        assertEquals(1, empleadoDAO.totalEliminados);
    }

    @Test
    @DisplayName("REQ005 - inicia sesion, redirige primer acceso y bloquea por intentos fallidos")
    void inicioSesionPorRolYBloqueo() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        empleadoDAO.crear(new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.SUPERVISOR));
        Usuario usuario = new Usuario("1712345678", "dandrade", cifrador.cifrar("Temp123!"));
        usuarioDAO.crear(usuario);
        AuthServicio servicio = new AuthServicio(usuarioDAO, empleadoDAO);

        Object[] login = servicio.iniciarSesion("dandrade", "Temp123!");
        assertInstanceOf(Sesion.class, login[0]);
        assertEquals("PRIMER_ACCESO", login[1]);
        assertEquals(Rol.SUPERVISOR, ((Sesion) login[0]).getRolUsuario());

        servicio.iniciarSesion("dandrade", "Mala123!");
        servicio.iniciarSesion("dandrade", "Mala123!");
        Object[] bloqueado = servicio.iniciarSesion("dandrade", "Mala123!");

        assertNull(bloqueado[0]);
        assertTrue(((String) bloqueado[1]).contains("Cuenta bloqueada"));
        assertEquals(EstadoCuenta.BLOQUEADO, usuarioDAO.buscarPorNombreUsuario("dandrade").getEstadoCuenta());
    }

    @Test
    @DisplayName("REQ006 - cambia contrasena validando politica, hash y primer acceso")
    void cambiaContrasenaConPolitica() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        Usuario usuario = new Usuario("1712345678", "dandrade", cifrador.cifrar("Temp123!"));
        usuario.setPrimerAcceso(true);
        usuarioDAO.crear(usuario);

        String resultado = new AuthServicio(usuarioDAO, empleadoDAO)
                .cambiarContrasena("dandrade", "Temp123!", "Nueva123!", "Nueva123!");

        Usuario actualizado = usuarioDAO.buscarPorNombreUsuario("dandrade");
        assertTrue(resultado.contains("actualizada exitosamente"));
        assertTrue(cifrador.verificar("Nueva123!", actualizado.getContrasenaHash()));
        assertFalse(actualizado.isPrimerAcceso());
    }

    @Test
    @DisplayName("REQ007 - recupera contrasena con usuario y correo registrados")
    void recuperaContrasenaConCorreoYUsuario() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        empleadoDAO.crear(new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.GUARDIA));
        usuarioDAO.crear(new Usuario("1712345678", "dandrade", cifrador.cifrar("Temp123!")));

        String resultado = new AuthServicio(usuarioDAO, empleadoDAO)
                .recuperarContrasena("danna@tekmess.com", "dandrade", "Recupera123!", "Recupera123!");

        assertTrue(resultado.contains("restablecida exitosamente"));
        assertTrue(cifrador.verificar("Recupera123!", usuarioDAO.buscarPorNombreUsuario("dandrade").getContrasenaHash()));
    }

    @Test
    @DisplayName("REQ008 - genera reporte analitico consolidando empleados y accesos fallidos")
    void generaReporteAnalitico() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        FakeReporteDAO reporteDAO = new FakeReporteDAO();
        empleadoDAO.totalCreados = 2;
        empleadoDAO.totalEditados = 1;
        empleadoDAO.totalEliminados = 1;
        usuarioDAO.totalAccesosFallidos = 3;

        Object[] respuesta = new ReporteServicio(reporteDAO, empleadoDAO, usuarioDAO)
                .generarReporte(new Date(1_000), new Date(2_000), "Danna Andrade");

        assertInstanceOf(Reporte.class, respuesta[0]);
        Reporte reporte = (Reporte) respuesta[0];
        assertEquals(2, reporte.getTotalEmpleadosCreados());
        assertEquals(1, reporte.getTotalEmpleadosEditados());
        assertEquals(1, reporte.getTotalEmpleadosEliminados());
        assertEquals(3, reporte.getTotalAccesosFallidos());
        assertEquals("Reporte generado exitosamente.", respuesta[1]);
    }

    @Test
    @DisplayName("REQ009 - consulta historial, filtra por fechas y agrega anotaciones")
    void consultaHistorialYAnotaciones() {
        FakeReporteDAO reporteDAO = new FakeReporteDAO();
        Reporte reporte = new Reporte(new Date(1_000), new Date(2_000), "David Pilaguano");
        reporteDAO.guardar(reporte);
        ReporteServicio servicio = new ReporteServicio(reporteDAO, new FakeEmpleadoDAO(), new FakeUsuarioDAO());

        Object[] historial = servicio.consultarHistorial(new Date(500), new Date(3_000));
        boolean anotado = servicio.agregarAnotacion(reporte.getIdReporte(), "Revision aprobada", "Ariel Llumiquinga");

        assertEquals("OK", historial[1]);
        assertEquals(1, ((List<?>) historial[0]).size());
        assertTrue(anotado);
        assertEquals(1, reporteDAO.obtenerAnotaciones(reporte.getIdReporte()).size());
    }

    private static class FakeEmpleadoDAO implements IEmpleadoDAO {
        private final Map<String, Empleado> empleados = new HashMap<>();
        int totalCreados;
        int totalEditados;
        int totalEliminados;

        @Override
        public boolean crear(Empleado empleado) {
            empleados.put(empleado.getCedula(), empleado);
            totalCreados++;
            return true;
        }

        @Override
        public boolean editar(Empleado empleado) {
            if (!empleados.containsKey(empleado.getCedula())) {
                return false;
            }
            empleados.put(empleado.getCedula(), empleado);
            totalEditados++;
            return true;
        }

        @Override
        public boolean eliminar(String cedula) {
            Empleado eliminado = empleados.remove(cedula);
            if (eliminado != null) {
                totalEliminados++;
                return true;
            }
            return false;
        }

        @Override
        public Empleado buscarPorCedula(String cedula) {
            return empleados.get(cedula);
        }

        @Override
        public List<Empleado> listarTodos() {
            return new ArrayList<>(empleados.values());
        }

        @Override
        public boolean existeCedula(String cedula) {
            return empleados.containsKey(cedula);
        }

        @Override
        public int contarCreadosEnPeriodo(Date inicio, Date fin) {
            return totalCreados;
        }

        @Override
        public int contarEditadosEnPeriodo(Date inicio, Date fin) {
            return totalEditados;
        }

        @Override
        public int contarEliminadosEnPeriodo(Date inicio, Date fin) {
            return totalEliminados;
        }
    }

    private static class FakeUsuarioDAO implements IUsuarioDAO {
        private final Map<String, Usuario> porUsuario = new HashMap<>();
        private final Map<String, Usuario> porCedula = new HashMap<>();
        int siguienteId = 1;
        int totalAccesosFallidos;

        @Override
        public boolean crear(Usuario usuario) {
            if (usuario.getIdUsuario() == 0) {
                usuario.setIdUsuario(siguienteId++);
            }
            porUsuario.put(usuario.getNombreUsuario(), usuario);
            porCedula.put(usuario.getCedula(), usuario);
            return true;
        }

        @Override
        public Usuario buscarPorNombreUsuario(String nombreUsuario) {
            return porUsuario.get(nombreUsuario);
        }

        @Override
        public Usuario buscarPorCedula(String cedula) {
            return porCedula.get(cedula);
        }

        @Override
        public boolean actualizarContrasena(int idUsuario, String hash) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) {
                return false;
            }
            usuario.setContrasenaHash(hash);
            return true;
        }

        @Override
        public boolean actualizarContrasenaTemporal(int idUsuario, String contrasenaTemporal) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) {
                return false;
            }
            usuario.setContrasenaTemporal(contrasenaTemporal);
            return true;
        }

        @Override
        public boolean actualizarCredencialesTemporales(int idUsuario, String hash, String contrasenaTemporal) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) {
                return false;
            }
            usuario.setContrasenaHash(hash);
            usuario.setContrasenaTemporal(contrasenaTemporal);
            usuario.setPrimerAcceso(true);
            return true;
        }

        @Override
        public boolean actualizarEstado(int idUsuario, EstadoCuenta estado) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) {
                return false;
            }
            usuario.setEstadoCuenta(estado);
            return true;
        }

        @Override
        public boolean actualizarIntentos(int idUsuario, int intentos) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) {
                return false;
            }
            usuario.setIntentosFallidos(intentos);
            totalAccesosFallidos++;
            return true;
        }

        @Override
        public boolean actualizarPrimerAcceso(int idUsuario, boolean primerAcceso) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) {
                return false;
            }
            usuario.setPrimerAcceso(primerAcceso);
            return true;
        }

        @Override
        public boolean eliminarPorCedula(String cedula) {
            Usuario usuario = porCedula.remove(cedula);
            if (usuario == null) {
                return false;
            }
            porUsuario.remove(usuario.getNombreUsuario());
            return true;
        }

        @Override
        public boolean existeNombreUsuario(String nombreUsuario) {
            return porUsuario.containsKey(nombreUsuario);
        }

        @Override
        public int contarAccesosFallidosEnPeriodo(Date inicio, Date fin) {
            return totalAccesosFallidos;
        }

        @Override
        public List<Usuario> listarTodos() {
            return new ArrayList<>(porUsuario.values());
        }

        private Usuario buscarPorId(int idUsuario) {
            return porUsuario.values().stream()
                    .filter(usuario -> usuario.getIdUsuario() == idUsuario)
                    .findFirst()
                    .orElse(null);
        }
    }

    private static class FakeReporteDAO implements IReporteDAO {
        private final Map<Integer, Reporte> reportes = new HashMap<>();
        private final Map<Integer, List<Anotacion>> anotaciones = new HashMap<>();
        int siguienteId = 1;

        @Override
        public boolean guardar(Reporte reporte) {
            if (reporte.getIdReporte() == 0) {
                reporte.setIdReporte(siguienteId++);
            }
            reportes.put(reporte.getIdReporte(), reporte);
            return true;
        }

        @Override
        public Reporte buscarPorId(int id) {
            return reportes.get(id);
        }

        @Override
        public List<Reporte> listarPorFechas(Date inicio, Date fin) {
            return reportes.values().stream()
                    .filter(reporte -> !reporte.getFechaInicio().before(inicio)
                            && !reporte.getFechaFin().after(fin))
                    .toList();
        }

        @Override
        public List<Reporte> listarTodos() {
            return new ArrayList<>(reportes.values());
        }

        @Override
        public boolean agregarAnotacion(int idReporte, Anotacion anotacion) {
            Reporte reporte = reportes.get(idReporte);
            if (reporte == null) {
                return false;
            }
            anotaciones.computeIfAbsent(idReporte, id -> new ArrayList<>()).add(anotacion);
            reporte.agregarAnotacion(anotacion);
            return true;
        }

        @Override
        public List<Anotacion> obtenerAnotaciones(int idReporte) {
            return anotaciones.getOrDefault(idReporte, List.of());
        }
    }
}
