package com.beacodeart.api.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Statement;

import com.beacodeart.api.APIConnection;
import com.beacodeart.api.dto.ReplyBlogDTO;
import com.beacodeart.api.dto.ReplyDTO;
import com.beacodeart.api.dto.ReplyUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ReplyRepository {
    static String password = APIConnection.getPassword();
    static String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    static String username = APIConnection.getUsername();
    static ObjectMapper objectMapper = new ObjectMapper();

    //TODO get specific resource
    public static List<String> getResource(String url1){
        String[] spliturl = url1.split("/");

        try (Connection conn = DriverManager.getConnection(url, username, password)){
            String query = getAllQuery();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<String> replies = new ArrayList<>();
            ReplyDTO reply1 = new ReplyDTO();
            //HashSet<Integer> replyusers = new HashSet<>();

            // TODO simplify, doesn't need to handle many to one
            while (rs.next()){
                if (rs.getInt(1)== reply1.getReply_id()){
                    if (rs.getInt(3)!=0){
                        addUserToReply(reply1, rs);
                    }
                    if (rs.getInt(5)!=0){
                        addBlogToReply(reply1, rs);
                    }
                } else {
                    if (reply1!=null && reply1.getReply_id()!=0){
                        String replyAsString = objectMapper.writeValueAsString(reply1);
                        replies.add(replyAsString);
                        reply1 = new ReplyDTO();
                    }
                    reply1.setReply_id(rs.getInt(1));
                    reply1.setTitle(rs.getString(2));

                    if(rs.getInt(3)!=0){
                        addUserToReply(reply1, rs);
                    }
                    if(rs.getInt(5)!=0){
                        addBlogToReply(reply1, rs);
                    }
                }
            }
            if (reply1!=null){
                String replyAsString = objectMapper.writeValueAsString(reply1);
                replies.add(replyAsString);
            }
            rs.close();
            stmt.close();
            return replies;
        } catch (Exception e){
            e.printStackTrace();
        }
        
        return null;
    }

    public static String postResource(ReplyDTO reply){
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            //check user and blog exist
            String check1 = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(check1);
            stmt.setInt(1, reply.getUser().getUser_id());
            ResultSet rs = stmt.executeQuery();
            if(!rs.isBeforeFirst()){
                throw new Exception("Not Found");
            }
            rs.close();
            stmt.close();

            String check2 = "SELECT * FROM blogs WHERE blog_id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(check2);
            stmt2.setInt(1, reply.getBlog().getBlog_id());
            ResultSet rs2 = stmt2.executeQuery();
            if(!rs2.isBeforeFirst()){
                throw new Exception("Not found");
            }
            rs2.close();
            stmt2.close();

            String query = "INSERT INTO replies ( title, user_id, blog_id ) VALUES ( ?, ?, ? )";
            PreparedStatement stmt3 = conn.prepareStatement(query);
            stmt3.setString(1, reply.getTitle());
            stmt3.setInt(2, reply.getUser().getUser_id());
            stmt3.setInt(3, reply.getBlog().getBlog_id());
            stmt3.executeUpdate();
            stmt3.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    private static void addBlogToReply(ReplyDTO reply1, ResultSet rs) throws Exception{
        ReplyBlogDTO blog1 = new ReplyBlogDTO();
        blog1.setBlog_id(rs.getInt(5));
        blog1.setTitle(rs.getString(6));
        ReplyUserDTO user1 = new ReplyUserDTO();
        user1.setUser_id(rs.getInt(7));
        user1.setUsername(rs.getString(8));
        blog1.setUser(user1);
        reply1.setBlog(blog1);
    }

    private static void addUserToReply(ReplyDTO reply1, ResultSet rs) throws Exception{
        int userId = rs.getInt(3);
        ReplyUserDTO user = new ReplyUserDTO();
        user.setUser_id(userId);
        user.setUsername(rs.getString(4));
        reply1.setUser(user);
    }

    

    private static String getAllQuery() {
        return """
            SELECT r.reply_id,
            r.title,
            u.user_id,
            u.username,
            b.blog_id,
            b.title,
            v.user_id,
            v.username
            from replies r
            left join users u
            on r.user_id = u.user_id
            left join blogs b
            on r.blog_id = b.blog_id
            left join (select user_id,
            username
            from users) v
            on b.user_id = v.user_id
                """;
    }
}
