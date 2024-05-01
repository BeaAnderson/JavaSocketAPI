package com.beacodeart.api.repositories;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.beacodeart.api.APIConnection;
import com.beacodeart.api.dto.BlogDTO;

public class BlogRepository {
    static String password = APIConnection.getPassword();
    static String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    static String username = APIConnection.getUsername();

    public static List<String> getResource(String givenUrl) {
        String[] spliturl = givenUrl.split("/");

        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            String query = getAllQuery();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<String> blogs = new ArrayList<>();
            BlogDTO blog1 = new BlogDTO();
            HashSet<Integer> blogReplies = new HashSet<>();

            while (rs.next()){
                if(rs.getInt(1) == blog1.getBlog_id()){

                    int optUserId = rs.getInt(3);

                    if (optUserId !=0 && !blogReplies.contains(optUserId)){
                        
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getAllQuery() {
        return """
            SELECT b.blog_id,
            b.title,
            u.user_id,
            u.username,
            r.reply_id,
            r.title,
            v.username
         FROM blogs b
         inner join users u
         on b.user_id = u.user_id
         inner join replies r
         on b.blog_id = r.blog_id
         inner join (
            select user_id,
            username
            from users
         ) v
         on r.user_id = v.user_id 
                """;
    }

}
