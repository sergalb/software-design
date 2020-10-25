package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Main;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private static String getAccumulateQueryBody(ResultSet rs) {
        try {
            if (rs.next()) {
                return Integer.toString(rs.getInt(1));
            }
            return "";
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }


    private void printResponse(HttpServletResponse response, String SQLQuery, String message,
                               Function<ResultSet, String> showHtml) {
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(SQLQuery);
                response.getWriter().println("<html><body>");
                response.getWriter().println(message);

                response.getWriter().println(showHtml.apply(rs));

                response.getWriter().println("</body></html>");

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            printResponse(response, "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                          "<h1>Product with max price: </h1>",
                          Main::formHtmlResponse);
        } else if ("min".equals(command)) {
            printResponse(response, "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                          "<h1>Product with min price: </h1>",
                          Main::formHtmlResponse);
        } else if ("sum".equals(command)) {
            printResponse(response,
                          "SELECT SUM(price) FROM PRODUCT", "Summary price: ",
                          QueryServlet::getAccumulateQueryBody);
        } else if ("count".equals(command)) {
            printResponse(response,
                          "SELECT COUNT(*) FROM PRODUCT", "Number of products: ",
                          QueryServlet::getAccumulateQueryBody);
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
