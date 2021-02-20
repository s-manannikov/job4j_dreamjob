package ru.job4j.servlet;

import ru.job4j.model.Candidate;
import ru.job4j.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Candidate candidate = PsqlStore.instOf().findCandidateById(id);
        PsqlStore.instOf().deleteCandidate(candidate.getId());
        PsqlStore.instOf().deletePhoto(candidate.getPhotoId());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        resp.sendRedirect("candidates.do");
    }
}
