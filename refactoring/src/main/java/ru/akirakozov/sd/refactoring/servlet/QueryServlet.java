package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.Main;
import ru.akirakozov.sd.refactoring.repositories.DBRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private void printResponse(HttpServletResponse response, String message,
                               String responseBody) {
        try {
            response.getWriter().println("<html><body>");
            response.getWriter().println(message);
            response.getWriter().println(responseBody);
            response.getWriter().println("</body></html>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");
        try {
            try (DBRepository dbRepository = new DBRepository()) {
                if ("max".equals(command)) {
                    printResponse(response,
                                  "<h1>Product with max price: </h1>",
                                  Main.getProductsBody(dbRepository.getMaxProductsByPrice()));
                } else if ("min".equals(command)) {
                    printResponse(response,
                                  "<h1>Product with min price: </h1>",
                                  Main.getProductsBody(dbRepository.getMinProductsByPrice()));
                } else if ("sum".equals(command)) {
                    printResponse(response,
                                  "Summary price: ",
                                  Integer.toString(dbRepository.getSumOfProduct())
                    );
                } else if ("count".equals(command)) {
                    printResponse(response,
                                  "Number of products: ",
                                  Integer.toString(dbRepository.getCountOfProduct())
                    );
                } else {
                    response.getWriter().println("Unknown command: " + command);
                }

                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
