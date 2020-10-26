package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.HTMLFormatter;
import ru.akirakozov.sd.refactoring.repositories.DBRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (HTMLFormatter htmlFormatter = new HTMLFormatter(response);
             DBRepository dbRepository = new DBRepository()) {
            htmlFormatter.printProducts(dbRepository.getProducts());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
