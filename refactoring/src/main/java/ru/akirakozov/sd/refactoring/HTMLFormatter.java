package ru.akirakozov.sd.refactoring;

import javafx.util.Pair;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HTMLFormatter implements AutoCloseable {
    HttpServletResponse response;

    public HTMLFormatter(HttpServletResponse response) {
        this.response = response;
        try {
            response.getWriter().println("<html><body>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printProducts(List<Pair<String, Integer>> products) {
        try {
            for (Pair<String, Integer> product : products) {
                response.getWriter().println(product.getKey() + "\t" + product.getValue() + "</br>");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printHeader(int headerLevel, String header) {
        printBody("<h" + headerLevel + ">" + header + "</h" + headerLevel + ">");
    }

    public void printBody(String body) {
        try {
            response.getWriter().println(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            response.getWriter().println("</body></html>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}