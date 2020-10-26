package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.HTMLFormatter;
import ru.akirakozov.sd.refactoring.repositories.DBRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");

        try (HTMLFormatter htmlFormatter = new HTMLFormatter(response);
             DBRepository dbRepository = new DBRepository()
        ) {
            if ("max".equals(command)) {
                htmlFormatter.printHeader(1, "Product with max price: ");
                htmlFormatter.printProducts(dbRepository.getMaxProductsByPrice());
            } else if ("min".equals(command)) {
                htmlFormatter.printHeader(1, "Product with min price: ");
                htmlFormatter.printProducts(dbRepository.getMinProductsByPrice());
            } else if ("sum".equals(command)) {
                htmlFormatter.printBody("Summary price: " + dbRepository.getSumOfProduct());
            } else if ("count".equals(command)) {
                htmlFormatter.printBody("Number of products: " + dbRepository.getCountOfProduct());
            } else {
                response.getWriter().println("Unknown command: " + command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
