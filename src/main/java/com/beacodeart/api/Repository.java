package com.beacodeart.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.beacodeart.api.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Repository {

    public List<String> getResource() {

        String result = "hello";
        String query = "select * from users";
        String password = APIConnection.getPassword();
        String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
        String username = APIConnection.getUsername();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<String> users = new ArrayList<>();
            while (rs.next()){
                User user1 = new User();
                user1.setUser_id(rs.getInt(1));
                user1.setUsername(rs.getString(2));
                String userAsString = objectMapper.writeValueAsString(user1);
                users.add(userAsString);
            }
            
            
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
