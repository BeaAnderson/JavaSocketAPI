package com.beacodeart.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beacodeart.api.models.Blog;
import com.beacodeart.api.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRepository {
    String password = APIConnection.getPassword();
    String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    String username = APIConnection.getUsername();

    public List<String> getResource(String givenUrl) {
        String query = "select * from users";
        String[] spliturl = givenUrl.split("/");

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            
            if (spliturl.length == 2){
                query = "select * from users u inner join blogs b on u.user_id = b.user_id";
                ObjectMapper objectMapper = new ObjectMapper();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                List<String> users = new ArrayList<>();
                
                while (rs.next()){
                    User user1 = new User();
                    user1.setUser_id(rs.getInt(1));
                    user1.setUsername(rs.getString(2));
                    Blog blog1 = new Blog();
                    blog1.setBlog_id(rs.getInt(3));
                    blog1.setTitle(rs.getString(4));
                    user1.setBlogs(Arrays.asList(blog1));
                    String userAsString = objectMapper.writeValueAsString(user1);
                    users.add(userAsString);
                }
            
            return users;

            } else if (spliturl.length == 4){
                
                query = "select * from users where " + spliturl[2] + " = ?";
                ObjectMapper objectMapper = new ObjectMapper();
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, spliturl[3]);
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery();
                List<String> users = new ArrayList<>();
                
                while (rs.next()){
                    User user1 = new User();
                    user1.setUser_id(rs.getInt(1));
                    user1.setUsername(rs.getString(2));
                    String userAsString = objectMapper.writeValueAsString(user1);
                    users.add(userAsString);
                }
            
                return users;

            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String postResource(User user){
        
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            String query = "INSERT INTO users ( username ) VALUES ( ? )";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        };

        return "success";
    }

}
