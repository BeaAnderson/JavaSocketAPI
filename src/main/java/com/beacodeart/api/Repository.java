package com.beacodeart.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Repository {

    public String getResource() {

        String result = "hello";
        String query = "select username from users where user_id = 2";
        String password = APIConnection.getPassword();
        String url = APIConnection.getUrl();
        String username = APIConnection.getUsername();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getString("username");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
