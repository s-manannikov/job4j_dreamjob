package ru.job4j.servlet;

import ru.job4j.model.City;
import ru.job4j.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("html");
        List<City> cities = (List<City>) PsqlStore.instOf().findAllCities();
        resp.getWriter().write("<option value=\"\" selected disabled hidden>select a city</option>");
        for (City city : cities) {
            resp.getWriter().write("<option value=\"" + city.getId() + "\">" + city.getName() + "</option>");
        }
    }
}
