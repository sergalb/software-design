package ru.akirakozov.sd.refactoring.repositories;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBRepository implements AutoCloseable {
    Connection c;

    public DBRepository() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private List<Pair<String, Integer>> getProductsByQuery(String SQLQuery) {
        List<Pair<String, Integer>> result = new ArrayList<>();
        try (Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(SQLQuery)
        ) {
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                result.add(new Pair<>(name, price));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private int getCharacteristic(String characteristic) {
        try (Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT " + characteristic + " FROM PRODUCT")
        ) {
            if (rs.next()) {
                return (rs.getInt(1));
            }
            return 0;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public void addProduct(String product, long price) {
        try {
            String sql = "INSERT INTO PRODUCT " +
                         "(NAME, PRICE) VALUES (\"" + product + "\"," + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pair<String, Integer>> getMinProductsByPrice() {
        return getProductsByQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
    }

    public List<Pair<String, Integer>> getMaxProductsByPrice() {
        return getProductsByQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
    }

    public List<Pair<String, Integer>> getProducts() {
        return getProductsByQuery("SELECT * FROM PRODUCT");
    }

    public int getSumOfProduct() {
        return getCharacteristic("SUM(price)");
    }

    public int getCountOfProduct() {
        return getCharacteristic("COUNT(*)");
    }

    @Override
    public void close() throws Exception {
        c.close();
    }
}
