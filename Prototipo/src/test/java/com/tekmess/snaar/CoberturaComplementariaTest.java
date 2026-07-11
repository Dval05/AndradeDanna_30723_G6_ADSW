package com.tekmess.snaar;

import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;
import com.tekmess.snaar.modelo.dao.IEmpleadoDAO;
import com.tekmess.snaar.modelo.dao.IUsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Anotacion;
import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.modelo.entidad.EstadoCuenta;
import com.tekmess.snaar.modelo.entidad.Locacion;
import com.tekmess.snaar.modelo.entidad.Reporte;
import com.tekmess.snaar.modelo.entidad.Rol;
import com.tekmess.snaar.modelo.entidad.Sesion;
import com.tekmess.snaar.modelo.entidad.Usuario;
import com.tekmess.snaar.patron.command.CrearEmpleadoComando;
import com.tekmess.snaar.patron.command.EditarEmpleadoComando;
import com.tekmess.snaar.patron.command.EliminarEmpleadoComando;
import com.tekmess.snaar.patron.command.IComando;
import com.tekmess.snaar.patron.command.InvocadorOperaciones;
import com.tekmess.snaar.patron.command.ResultadoComando;
import com.tekmess.snaar.patron.observer.EventoSistema;
import com.tekmess.snaar.patron.observer.IObservador;
import com.tekmess.snaar.patron.observer.ObservadorAuditoria;
import com.tekmess.snaar.patron.observer.ObservadorNotificacion;
import com.tekmess.snaar.patron.observer.SujetoGestionEmpleado;
import com.tekmess.snaar.util.CifradorContrasena;
import com.tekmess.snaar.util.GeneradorCredenciales;
import com.tekmess.snaar.util.ValidadorDatos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CoberturaComplementariaTest {

    @Test
    @DisplayName("Utilidades - validador cubre entradas validas e invalidas")
    void validadorDatosCubreReglasPrincipales() {
        ValidadorDatos validador = new ValidadorDatos();

        assertNull(validador.validarCedula("1712345678"));
        assertNotNull(validador.validarCedula("171"));
        assertNotNull(validador.validarCedula(null));

        assertNull(validador.validarNombres("Danna Andrade"));
        assertNotNull(validador.validarNombres(""));
        assertNotNull(validador.validarNombres("Danna"));
        assertNotNull(validador.validarNombres("Danna 123"));

        assertNull(validador.validarRol("GUARDIA"));
        assertNotNull(validador.validarRol("ADMIN"));
        assertNotNull(validador.validarRol(null));

        assertNull(validador.validarCorreo("danna@tekmess.com"));
        assertNotNull(validador.validarCorreo("correo-invalido"));
        assertNotNull(validador.validarCorreo(null));

        assertNull(validador.validarUsuario("usuario_1"));
        assertNotNull(validador.validarUsuario("ab"));
        assertNotNull(validador.validarUsuario(""));

        assertNull(validador.validarContrasena("Nueva123!"));
        assertNotNull(validador.validarContrasena("simple"));
        assertEquals("Texto limpio", validador.normalizarEspacios("  Texto   limpio  "));
        assertNull(validador.normalizarEspacios(null));
    }

    @Test
    @DisplayName("Utilidades - generador y cifrador trabajan correctamente")
    void generadorCredencialesYCifrador() {
        GeneradorCredenciales generador = new GeneradorCredenciales();
        CifradorContrasena cifrador = new CifradorContrasena();
        ValidadorDatos validador = new ValidadorDatos();

        assertEquals("dandrade", generador.generarNombreUsuario(
                new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.GUARDIA)));
        assertEquals("maria", generador.generarNombreUsuario(
                new Empleado("1712345678", "Maria", "maria@tekmess.com", Rol.GUARDIA)));
        assertEquals("dandrade2", generador.aplicarSufijo("dandrade", 2));

        String temporal = generador.generarContrasenaInicial();
        assertEquals(10, temporal.length());
        assertNull(validador.validarContrasena(temporal));

        String hash = cifrador.cifrar("Nueva123!");
        assertTrue(cifrador.verificar("Nueva123!", hash));
        assertFalse(cifrador.verificar("Otra123!", hash));
        assertFalse(cifrador.verificar("Nueva123!", "hash-no-valido"));
    }

    @Test
    @DisplayName("Entidades - getters, setters y metodos de negocio")
    void entidadesBasicas() {
        Empleado empleado = new Empleado();
        empleado.setCedula("1712345678");
        empleado.setNombres("Danna Andrade");
        empleado.setCorreo("danna@tekmess.com");
        empleado.setRol(Rol.SUPERVISOR);
        empleado.setIdLocacion(3);
        empleado.setNombreLocacion("Quito");
        Date fecha = new Date(1000);
        empleado.setFechaCreacion(fecha);
        empleado.setFechaModificacion(fecha);

        assertEquals("1712345678", empleado.getCedula());
        assertEquals("Danna Andrade", empleado.getNombres());
        assertEquals("danna@tekmess.com", empleado.getCorreo());
        assertEquals(Rol.SUPERVISOR, empleado.getRol());
        assertEquals(3, empleado.getIdLocacion());
        assertEquals("Quito", empleado.getNombreLocacion());
        assertTrue(empleado.toString().contains("1712345678"));

        Usuario usuario = new Usuario("1712345678", "dandrade", "hash");
        usuario.setIdUsuario(7);
        usuario.setContrasenaTemporal("Temp123!");
        usuario.setUltimoAcceso(fecha);
        usuario.incrementarIntentos();
        usuario.incrementarIntentos();
        usuario.incrementarIntentos();
        assertEquals(EstadoCuenta.BLOQUEADO, usuario.getEstadoCuenta());
        usuario.reiniciarIntentos();
        usuario.setEstadoCuenta(EstadoCuenta.ACTIVO);
        usuario.setPrimerAcceso(false);
        assertEquals(0, usuario.getIntentosFallidos());
        assertFalse(usuario.isPrimerAcceso());
        assertEquals("dandrade", usuario.getUsuario());
        assertEquals("dandrade", usuario.getNombre());
        assertNull(usuario.getRol());
        assertTrue(usuario.toString().contains("dandrade"));

        Sesion sesion = new Sesion(7, "dandrade", Rol.SUPERVISOR);
        assertTrue(sesion.isActiva());
        sesion.renovar();
        sesion.invalidar();
        assertFalse(sesion.isActiva());
        sesion.setIdSesion("sesion-1");
        sesion.setIdUsuario(8);
        sesion.setNombreUsuario("otro");
        sesion.setRolUsuario(Rol.GUARDIA);
        sesion.setActiva(true);
        assertEquals("sesion-1", sesion.getIdSesion());
        assertEquals(8, sesion.getIdUsuario());
        assertEquals("otro", sesion.getNombreUsuario());
        assertEquals(Rol.GUARDIA, sesion.getRolUsuario());
    }

    @Test
    @DisplayName("Entidades - reporte, anotacion, locacion y enums")
    void entidadesReporteAnotacionLocacion() {
        Anotacion anotacion = new Anotacion(1, "Observacion", "Danna");
        anotacion.setIdAnotacion(5);
        anotacion.setIdReporte(2);
        anotacion.setContenido("Actualizada");
        anotacion.setAutor("Ariel");
        Date fecha = new Date(1000);
        anotacion.setFechaCreacion(fecha);
        assertEquals(5, anotacion.getIdAnotacion());
        assertEquals(2, anotacion.getIdReporte());
        assertEquals("Actualizada", anotacion.getContenido());
        assertEquals("Ariel", anotacion.getAutor());
        assertEquals(fecha, anotacion.getFechaCreacion());

        Reporte reporte = new Reporte(new Date(1000), new Date(2000), "David");
        assertFalse(reporte.tieneDatos());
        reporte.setIdReporte(9);
        reporte.setFechaGeneracion(fecha);
        reporte.setFechaInicio(new Date(500));
        reporte.setFechaFin(new Date(2500));
        reporte.setGeneradoPor("Danna");
        reporte.setTotalEmpleadosCreados(1);
        reporte.setTotalEmpleadosEditados(2);
        reporte.setTotalEmpleadosEliminados(3);
        reporte.setTotalAccesosFallidos(4);
        reporte.agregarAnotacion(anotacion);
        reporte.setAnotaciones(new ArrayList<>(reporte.getAnotaciones()));
        assertTrue(reporte.tieneDatos());
        assertEquals(9, reporte.getIdReporte());
        assertEquals(1, reporte.getAnotaciones().size());

        Locacion locacion = new Locacion();
        locacion.setIdLocacion(1);
        locacion.setNombre("Bodega Norte");
        locacion.setCiudad("Quito");
        locacion.setDireccion("Av. Siempre Viva");
        locacion.setResponsable("Danna");
        locacion.setCapacidad(50);
        locacion.setActiva(true);
        assertEquals(1, locacion.getIdLocacion());
        assertEquals("Bodega Norte", locacion.getNombre());
        assertEquals("Quito", locacion.getCiudad());
        assertEquals("Av. Siempre Viva", locacion.getDireccion());
        assertEquals("Danna", locacion.getResponsable());
        assertEquals(50, locacion.getCapacidad());
        assertTrue(locacion.isActiva());

        assertEquals("Guardia", Rol.GUARDIA.getDescripcion());
        assertEquals(Rol.SUPERVISOR, Rol.fromDescripcion("Supervisor"));
        assertThrows(IllegalArgumentException.class, () -> Rol.fromDescripcion("Administrador"));
        assertEquals("Activo", EstadoCuenta.ACTIVO.getDescripcion());
    }

    @Test
    @DisplayName("Command - invocador ejecuta, deshace y mantiene historial")
    void patronCommandInvocador() {
        InvocadorOperaciones invocador = new InvocadorOperaciones();
        FakeCommand comandoOk = new FakeCommand(true, "OK", "UNDO");
        FakeCommand comandoError = new FakeCommand(false, "ERROR", "UNDO");

        assertFalse(invocador.ejecutarComando(comandoError).isExitoso());
        assertEquals(0, invocador.getTamanioHistorial());

        ResultadoComando resultado = invocador.ejecutarComando(comandoOk);
        assertTrue(resultado.isExitoso());
        assertEquals(1, invocador.getTamanioHistorial());
        assertEquals(1, invocador.getHistorial().size());

        ResultadoComando undo = invocador.deshacerUltimo();
        assertTrue(undo.isExitoso());
        assertEquals(0, invocador.getTamanioHistorial());
        assertFalse(invocador.deshacerUltimo().isExitoso());

        ResultadoComando conDatos = new ResultadoComando(true, "mensaje", "datos");
        assertEquals("datos", conDatos.getDatos());
        assertEquals("[OK] mensaje", conDatos.toString());
        assertEquals("[ERROR] ERROR", new ResultadoComando(false, "ERROR").toString());

        invocador.ejecutarComando(comandoOk);
        invocador.limpiarHistorial();
        assertEquals(0, invocador.getTamanioHistorial());
    }

    @Test
    @DisplayName("Command - comandos de empleado ejecutan y deshacen")
    void comandosEmpleado() {
        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        EmpleadoServicio servicio = new EmpleadoServicio(empleadoDAO, usuarioDAO);
        Empleado empleado = new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.GUARDIA);

        CrearEmpleadoComando crear = new CrearEmpleadoComando(servicio, empleado);
        assertTrue(crear.ejecutar().isExitoso());
        assertNotNull(crear.getCredencialesGeneradas());
        assertTrue(crear.getDescripcion().contains("Danna Andrade"));
        assertTrue(crear.deshacer().isExitoso());

        empleadoDAO.crear(empleado);
        EditarEmpleadoComando editar = new EditarEmpleadoComando(servicio,
                new Empleado("1712345678", "Danna Andrade", "danna.editada@tekmess.com", Rol.SUPERVISOR));
        assertTrue(editar.ejecutar().isExitoso());
        assertTrue(editar.deshacer().isExitoso());
        assertTrue(editar.getDescripcion().contains("1712345678"));

        EditarEmpleadoComando editarInexistente = new EditarEmpleadoComando(servicio,
                new Empleado("0000000000", "No Existe", "no@tekmess.com", Rol.GUARDIA));
        assertFalse(editarInexistente.ejecutar().isExitoso());
        assertFalse(editarInexistente.deshacer().isExitoso());

        EliminarEmpleadoComando eliminar = new EliminarEmpleadoComando(servicio, "1712345678");
        assertTrue(eliminar.ejecutar().isExitoso());
        assertTrue(eliminar.deshacer().isExitoso());
        assertTrue(eliminar.getDescripcion().contains("1712345678"));

        EliminarEmpleadoComando eliminarInexistente = new EliminarEmpleadoComando(servicio, "0000000000");
        assertFalse(eliminarInexistente.ejecutar().isExitoso());
        assertFalse(eliminarInexistente.deshacer().isExitoso());
    }

    @Test
    @DisplayName("Observer - eventos, auditoria, notificacion y sujeto")
    void patronObserver() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("usuario", "dandrade");
        EventoSistema evento = new EventoSistema(EventoSistema.TipoEvento.LOGIN_FALLIDO, "Sistema", datos);
        evento.agregarDato("intentos", 1);
        assertEquals(EventoSistema.TipoEvento.LOGIN_FALLIDO, evento.getTipoEvento());
        assertEquals("Sistema", evento.getActor());
        assertTrue(evento.toString().contains("LOGIN_FALLIDO"));

        EventoSistema eventoSinDatos = new EventoSistema(EventoSistema.TipoEvento.REPORTE_GENERADO, "Danna", null);
        assertTrue(eventoSinDatos.getDatos().isEmpty());

        ObservadorAuditoria auditoria = new ObservadorAuditoria();
        auditoria.actualizar(evento);
        auditoria.actualizar(new EventoSistema(EventoSistema.TipoEvento.EMPLEADO_CREADO, "Danna"));
        assertEquals(2, auditoria.getRegistros().size());
        assertEquals(1, auditoria.getRegistrosPorTipo(EventoSistema.TipoEvento.LOGIN_FALLIDO).size());

        ObservadorNotificacion notificacion = new ObservadorNotificacion();
        notificacion.actualizar(new EventoSistema(EventoSistema.TipoEvento.CUENTA_BLOQUEADA, "Sistema", datos));
        notificacion.actualizar(new EventoSistema(EventoSistema.TipoEvento.EMPLEADO_ELIMINADO, "Danna", Map.of("cedula", "1712345678")));
        notificacion.actualizar(new EventoSistema(EventoSistema.TipoEvento.LOGIN_FALLIDO, "Sistema", datos));
        notificacion.actualizar(new EventoSistema(EventoSistema.TipoEvento.EMPLEADO_CREADO, "Danna", Map.of("nombres", "Danna Andrade")));
        notificacion.actualizar(new EventoSistema(EventoSistema.TipoEvento.REPORTE_GENERADO, "David"));

        FakeEmpleadoDAO empleadoDAO = new FakeEmpleadoDAO();
        FakeUsuarioDAO usuarioDAO = new FakeUsuarioDAO();
        SujetoGestionEmpleado sujeto = new SujetoGestionEmpleado(new EmpleadoServicio(empleadoDAO, usuarioDAO));
        CapturadorObservador capturador = new CapturadorObservador();
        sujeto.agregarObservador(capturador);
        sujeto.agregarObservador(capturador);

        String crear = sujeto.crearEmpleado(new Empleado("1712345678", "Danna Andrade", "danna@tekmess.com", Rol.GUARDIA), "Danna");
        assertTrue(crear.contains("Empleado registrado"));
        String editar = sujeto.editarEmpleado(new Empleado("1712345678", "Danna Andrade", "danna2@tekmess.com", Rol.SUPERVISOR), "Danna");
        assertTrue(editar.contains("actualizados"));
        String eliminar = sujeto.eliminarEmpleado("1712345678", "Danna");
        assertTrue(eliminar.contains("eliminado"));
        assertEquals(3, capturador.eventos.size());

        sujeto.eliminarObservador(capturador);
        sujeto.notificarObservadores(new EventoSistema(EventoSistema.TipoEvento.REPORTE_GENERADO, "Danna"));
        assertEquals(3, capturador.eventos.size());
    }

    private static class FakeCommand implements IComando {
        private final boolean exitoso;
        private final String mensaje;
        private final String mensajeDeshacer;

        FakeCommand(boolean exitoso, String mensaje, String mensajeDeshacer) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.mensajeDeshacer = mensajeDeshacer;
        }

        @Override
        public ResultadoComando ejecutar() {
            return new ResultadoComando(exitoso, mensaje);
        }

        @Override
        public ResultadoComando deshacer() {
            return new ResultadoComando(true, mensajeDeshacer);
        }

        @Override
        public String getDescripcion() {
            return "FakeCommand";
        }
    }

    private static class CapturadorObservador implements IObservador {
        private final List<EventoSistema> eventos = new ArrayList<>();

        @Override
        public void actualizar(EventoSistema evento) {
            eventos.add(evento);
        }
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
            if (usuario == null) return false;
            usuario.setContrasenaHash(hash);
            return true;
        }

        @Override
        public boolean actualizarContrasenaTemporal(int idUsuario, String contrasenaTemporal) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) return false;
            usuario.setContrasenaTemporal(contrasenaTemporal);
            return true;
        }

        @Override
        public boolean actualizarCredencialesTemporales(int idUsuario, String hash, String contrasenaTemporal) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) return false;
            usuario.setContrasenaHash(hash);
            usuario.setContrasenaTemporal(contrasenaTemporal);
            usuario.setPrimerAcceso(true);
            return true;
        }

        @Override
        public boolean actualizarEstado(int idUsuario, EstadoCuenta estado) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) return false;
            usuario.setEstadoCuenta(estado);
            return true;
        }

        @Override
        public boolean actualizarIntentos(int idUsuario, int intentos) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) return false;
            usuario.setIntentosFallidos(intentos);
            totalAccesosFallidos++;
            return true;
        }

        @Override
        public boolean actualizarPrimerAcceso(int idUsuario, boolean primerAcceso) {
            Usuario usuario = buscarPorId(idUsuario);
            if (usuario == null) return false;
            usuario.setPrimerAcceso(primerAcceso);
            return true;
        }

        @Override
        public boolean eliminarPorCedula(String cedula) {
            Usuario usuario = porCedula.remove(cedula);
            if (usuario == null) return false;
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
}
