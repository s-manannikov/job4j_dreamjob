package ru.job4j.servlet;

import ru.job4j.model.Candidate;
import ru.job4j.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DeleteCandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Candidate candidate = PsqlStore.instOf().findCandidateById(id);
        PsqlStore.instOf().deleteCandidate(candidate.getId());
        if (!candidate.getPhotoId().equals("default.png")) {
            File folder = new File("images");
            File file = new File(folder + File.separator + candidate.getPhotoId());
            file.delete();
        }
        resp.sendRedirect("candidates.do");
    }
}
