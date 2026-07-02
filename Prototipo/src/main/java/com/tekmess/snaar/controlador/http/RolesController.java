package com.tekmess.snaar.controlador.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekmess.snaar.modelo.entidad.Rol;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet(name = "RolesController", urlPatterns = { "/roles/manage" })
public class RolesController extends HttpServlet {

    private File configFile;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        String path = getServletContext().getRealPath("/WEB-INF/role_config.json");
        configFile = new File(path);
        if (!configFile.exists()) {
            try {
                writeDefaults();
            } catch (IOException e) {
                throw new ServletException(e);
            }
        }
    }

    private void writeDefaults() throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Rol r : Rol.values()) {
            Map<String, Object> m = new HashMap<>();
            m.put("name", r.name());
            m.put("description", "Descripción para " + r.name());
            m.put("active", true);
            list.add(m);
        }
        try (Writer w = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            gson.toJson(list, w);
        }
    }

    private List<Map<String, Object>> readConfig() throws IOException {
        try (Reader r = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
            Type t = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            List<Map<String, Object>> l = gson.fromJson(r, t);
            return l != null ? l : new ArrayList<>();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("configList", readConfig());
        req.getRequestDispatcher("/vistas/roles/manage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Expect params: desc_<ROLE>, active_<ROLE>
        List<Map<String, Object>> cfg = readConfig();
        for (Map<String, Object> m : cfg) {
            String name = (String) m.get("name");
            String desc = req.getParameter("desc_" + name);
            String active = req.getParameter("active_" + name);
            if (desc != null)
                m.put("description", desc);
            m.put("active", active != null && (active.equals("on") || active.equals("true")));
        }
        try (Writer w = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            gson.toJson(cfg, w);
        }
        req.setAttribute("configList", cfg);
        req.setAttribute("exito", "Cambios guardados");
        req.getRequestDispatcher("/vistas/roles/manage.jsp").forward(req, resp);
    }
}
