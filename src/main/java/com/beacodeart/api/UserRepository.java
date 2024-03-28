package com.beacodeart.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beacodeart.api.DTOs.UserBlogDTO;
import com.beacodeart.api.DTOs.UserDTO;
import com.beacodeart.api.DTOs.UserReplyDTO;
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
                query = "select u.user_id, \n" + //
                        "\tu.username, \n" + //
                        "\tb.blog_id, \n" + //
                        "\tb.title, \n" + //
                        "\tr.reply_id, \n" + //
                        "\tr.title as 'reply_title', \n" + //
                        "\tr.blog_id as 'reply_blog',\n" + //
                        "    v.title as 'reply_blog_title'\n" + //
                        "from users u \n" + //
                        "\tleft join blogs b \n" + //
                        "\t\ton u.user_id = b.user_id \n" + //
                        "    left join replies r \n" + //
                        "\t\ton u.user_id = r.user_id\n" + //
                        "\tleft join (\n" + //
                        "    select title, blog_id\n" + //
                        "    from blogs\n" + //
                        "    ) v\n" + //
                        "\t\ton r.blog_id = v.blog_id";
                ObjectMapper objectMapper = new ObjectMapper();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                List<String> users = new ArrayList<>();
                
                UserDTO user1 = new UserDTO();

                while (rs.next()){
                    if (rs.getInt(1) == user1.getId()){
                        
                        if (rs.getInt(3)!=0){
                            UserBlogDTO blog1 = new UserBlogDTO();
                            blog1.setId(rs.getInt(3));
                            blog1.setTitle(rs.getString(4));
                            if (user1.getBlogs() == null){
                                user1.setBlogs(new ArrayList<UserBlogDTO>());
                            }
                            user1.addBlog(blog1);
                        }
                        
                        if (rs.getInt(5)!=0){
                            UserReplyDTO reply1 = new UserReplyDTO();
                            reply1.setId(rs.getInt(5));
                            reply1.setTitle(rs.getString(6));
                            if (user1.getReplies()==null){
                                user1.setReplies(new ArrayList<UserReplyDTO>());
                            }
                            user1.addReply(reply1);
                        }
                    
                    } else {
                        
                        if (user1 != null && user1.getId() != 0){
                            String userAsString = objectMapper.writeValueAsString(user1);
                            users.add(userAsString);
                            user1 = new UserDTO();
                        }
                        
                        user1.setId(rs.getInt(1));
                        user1.setUsername(rs.getString(2));
                        
                        if (rs.getInt(3)!=0){
                            UserBlogDTO blog1 = new UserBlogDTO();
                            blog1.setId(rs.getInt(3));
                            blog1.setTitle(rs.getString(4));
                            if (user1.getBlogs() == null){
                                user1.setBlogs(new ArrayList<UserBlogDTO>());
                            }
                            user1.addBlog(blog1);
                        }
                        if (rs.getInt(5)!=0){
                            UserReplyDTO reply1 = new UserReplyDTO();
                            reply1.setId(rs.getInt(5));
                            reply1.setTitle(rs.getString(6));
                            if (user1.getReplies()==null){
                                user1.setReplies(new ArrayList<UserReplyDTO>());
                            }
                            user1.addReply(reply1);
                        }
                        
                    }
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
