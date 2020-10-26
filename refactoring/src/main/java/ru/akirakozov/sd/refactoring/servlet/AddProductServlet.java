package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.HTMLFormatter;
import ru.akirakozov.sd.refactoring.repositories.DBRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try (HTMLFormatter htmlFormatter = new HTMLFormatter(response);
             DBRepository dbRepository = new DBRepository()) {
            dbRepository.addProduct(name, price);
            htmlFormatter.printBody("OK");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
