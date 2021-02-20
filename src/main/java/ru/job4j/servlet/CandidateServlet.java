package ru.job4j.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.model.Candidate;
import ru.job4j.model.Photo;
import ru.job4j.store.PsqlStore;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Candidate candidate = new Candidate();
        Photo photo = new Photo();
        req.setCharacterEncoding("UTF-8");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                if (!item.isFormField() && !item.getName().isEmpty()) {
                    byte[] picture = item.getInputStream().readAllBytes();
                    photo.setPicture(picture);
                } else {
                    if (item.getFieldName().equals("name")) {
                        candidate.setName(item.getString());
                    }
                    if (item.getFieldName().equals("city")) {
                        candidate.setCityId(Integer.parseInt(item.getString()));
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        candidate.setId(Integer.parseInt(req.getParameter("id")));
        candidate.setPhotoId(PsqlStore.instOf().savePhoto(photo).getId());
        PsqlStore.instOf().saveCandidate(candidate);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}