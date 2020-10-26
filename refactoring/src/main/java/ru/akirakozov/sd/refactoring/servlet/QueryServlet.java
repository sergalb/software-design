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
        try (HTMLFormatter htmlFormatter = new HTMLFormatter(response);
             DBRepository dbRepository = new DBRepository()
        ) {
            try {
                QUERIES query = QUERIES.valueOf(request.getParameter("command").toUpperCase());
                query.process(response, htmlFormatter, dbRepository);
            } catch (IllegalArgumentException e) {
                htmlFormatter.printBody("Unknown command: " + request.getParameter("command"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public enum QUERIES {
        MIN {
            @Override
            void process(HttpServletResponse response, HTMLFormatter htmlFormatter, DBRepository dbRepository) {
                htmlFormatter.printHeader(1, "Product with min price: ");
                htmlFormatter.printProducts(dbRepository.getMinProductsByPrice());
            }
        },
        MAX {
            @Override
            void process(HttpServletResponse response, HTMLFormatter htmlFormatter, DBRepository dbRepository) {
                htmlFormatter.printHeader(1, "Product with max price: ");
                htmlFormatter.printProducts(dbRepository.getMaxProductsByPrice());
            }
        },
        SUM {
            @Override
            void process(HttpServletResponse response, HTMLFormatter htmlFormatter, DBRepository dbRepository) {
                htmlFormatter.printBody("Summary price: " + dbRepository.getSumOfProduct());
            }
        },
        COUNT {
            @Override
            void process(HttpServletResponse response, HTMLFormatter htmlFormatter, DBRepository dbRepository) {
                htmlFormatter.printBody("Number of products: " + dbRepository.getCountOfProduct());
            }
        };

        abstract void process(HttpServletResponse response, HTMLFormatter htmlFormatter,
                              DBRepository dbRepository);

    }
}
