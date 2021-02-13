package ru.job4j.servlet;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class GreetingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", name);
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println(jsonObject);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String data = req.getReader().lines().collect(Collectors.joining());
        String name = data.split(" : ")[1];
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", name);
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println(jsonObject);
        writer.flush();
    }
}