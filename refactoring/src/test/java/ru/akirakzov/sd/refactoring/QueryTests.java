package ru.akirakzov.sd.refactoring;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryTests extends E2EServerTests {

    String getMinMaxQueryResult(String command) throws IOException {
         Element result = Jsoup.connect(queryProductUrl(command))
                .get().select("h1").first();
         if (result == null) {
             return "";
         } else {
             return result.nextSibling().toString().strip();
         }
    }

    private void checkQueriesByPrices(List<Integer> prices) throws IOException {
        for (Integer price : prices) {
            String addBody = Jsoup.connect(addProductUrl("product" + price, price)).get()
                    .body().text();
            assertEquals("OK", addBody);
        }
        IntSummaryStatistics stats = prices.stream()
                .collect(Collectors.summarizingInt(Integer::intValue));
        String minBody = getMinMaxQueryResult("min");
        String min = Integer.toString(stats.getMin());
        assertEquals("product" + min + " " + min, minBody);

        String maxBody = getMinMaxQueryResult("max");
        String max = Integer.toString(stats.getMax());
        assertEquals("product" + max + " " + max, maxBody);

        String sumBody = Jsoup.connect(queryProductUrl("sum")).get().body().text();
        String sum = Long.toString(stats.getSum());
        assertEquals("Summary price: " + sum, sumBody);

        String countBody = Jsoup.connect(queryProductUrl("count")).get().body().text();
        String count = Long.toString(stats.getCount());
        assertEquals("Number of products: " + count, countBody);
    }


    @Test
    public void oneProductQueriesTest() throws IOException {
        checkQueriesByPrices(List.of(1));
    }


    @Test
    public void twoDifferentProductTest() throws IOException {
        checkQueriesByPrices(List.of(1, 2));
    }

    @Test
    public void zeroProductTest() throws IOException {
        String minBody = getMinMaxQueryResult("min");
        assertEquals("", minBody);

        String maxBody = getMinMaxQueryResult("max");
        assertEquals("", maxBody);

        String sumBody = Jsoup.connect(queryProductUrl("sum")).get().body().text();
        assertEquals("Summary price: 0", sumBody);

        String countBody = Jsoup.connect(queryProductUrl("count")).get().body().text();
        assertEquals("Number of products: 0", countBody);
    }


    @Test
    public void twoSameProductTest() throws IOException {
        checkQueriesByPrices(List.of(2, 2));
    }

    @Test
    public void differentAndSameProductsTest() throws IOException {
        checkQueriesByPrices(List.of(1, 2, 3, 4, 4));
    }

}
