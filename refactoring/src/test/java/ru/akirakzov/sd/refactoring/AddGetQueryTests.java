package ru.akirakzov.sd.refactoring;


import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AddGetQueryTests extends E2EServerTests {


    @Test
    public void oneProductAddTest() throws IOException {
        String addBody = Jsoup.connect(addProductUrl("product1", 1)).get()
                .body().text();
        assertEquals("OK", addBody);

        String getBody = Jsoup.connect(getProductUrl()).get().body().text();
        assertEquals("product1 1", getBody);
    }

    @Test
    public void twoProductAddTest() throws IOException {
        String firstProduct = Jsoup.connect(addProductUrl("product1", 1)).get()
                .body().text();
        assertEquals("OK", firstProduct);
        String secondProduct = Jsoup.connect(addProductUrl("product2", 2)).get()
                .body().text();
        assertEquals("OK", secondProduct);
        String getBody = Jsoup.connect(getProductUrl()).get()
                .body().text();
        assertEquals("product1 1 product2 2", getBody);
    }

    @Test
    public void addDuplicateProductBothShouldShownTest() throws IOException {
        String request = addProductUrl("product", 1);
        String addBody = Jsoup.connect(request).get()
                .body().text();
        assertEquals("OK", addBody);
        String addBodySecondTime = Jsoup.connect(request).get()
                .body().text();
        assertEquals("OK", addBodySecondTime);
        String getBody = Jsoup.connect(getProductUrl()).get()
                .body().text();
        assertEquals("product 1 product 1", getBody);
    }
}
