package com.beacodeart.api;

import java.sql.Connection;
import java.sql.DriverManager;

public class Repository {

    public static String getResource() {

        String result = "";
        String password = APIConnection.getPassword();
        String url = APIConnection.getUrl();
        String username = APIConnection.getUsername();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
