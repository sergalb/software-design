package ru.akirakzov.sd.refactoring;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.akirakozov.sd.refactoring.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class E2EServerTests {

    static Thread serverThread;
    protected String serverUrl = "http://localhost:8081/";
    OkHttpClient client = new OkHttpClient();

    @BeforeAll
    static void initServer() throws InterruptedException {
        serverThread = new Thread(() -> {
            try {
                Main.main(new String[0]);
            } catch (Exception ignore) {
            }
        });
        serverThread.start();
        Thread.sleep(5000);
    }

    @AfterAll
    static void interruptServer() {
        serverThread.interrupt();
    }

    @BeforeEach
    private void deleteTableContent() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DELETE FROM PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    protected String addProductUrl(String name, int price) {
        return serverUrl + "add-product?" + "name=" + name + "&price=" + price;
    }

    protected String getProductUrl() {
        return serverUrl + "get-products";
    }

    protected String queryProductUrl(String command) {
        return serverUrl + "query?command=" + command;
    }


}
