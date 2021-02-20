package ru.job4j.servlet;

import ru.job4j.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        resp.setContentType("name=" + name);
        resp.setContentType("image/png; image/jpeg");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
        byte[] picture = PsqlStore.instOf().findPhotoById(Integer.parseInt(name)).getPicture();
        try (ByteArrayInputStream in = new ByteArrayInputStream(picture)) {
            resp.getOutputStream().write(in.readAllBytes());
        }
    }
}