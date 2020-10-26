package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Main;
import ru.akirakozov.sd.refactoring.repositories.DBRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private String formHtmlResponse(ResultSet rs) {
        StringBuilder result = new StringBuilder();
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                result.append(name).append("\t").append(price).append("</br>").append(System.lineSeparator());
            }
        } catch (SQLException throwables) {
            throw new RuntimeException();
        }
        result.append("</body></html>");
        return result.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (DBRepository dbRepository = new DBRepository()) {
            response.getWriter().println("<html><body>");

            String body = Main.getProductsBody(dbRepository.getProducts());
            response.getWriter().println(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
