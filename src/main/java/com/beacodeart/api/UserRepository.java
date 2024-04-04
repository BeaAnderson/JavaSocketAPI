package com.beacodeart.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.beacodeart.api.DTOs.UserBlogDTO;
import com.beacodeart.api.DTOs.UserDTO;
import com.beacodeart.api.DTOs.UserReplyDTO;
import com.beacodeart.api.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRepository {
    String password = APIConnection.getPassword();
    String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    String username = APIConnection.getUsername();
    String query;
    ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getResource(String givenUrl) {

        String[] spliturl = givenUrl.split("/");
        System.out.println(url);
        
        //get the connection
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            
            //if the split url array is size two the request we are dealing with is /users
            if (spliturl.length == 2) {
                query = getAllQuery();
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                List<String> users = new ArrayList<>();
                UserDTO user1 = new UserDTO();
                HashSet<Integer> userBlogs = new HashSet<>();

                while (rs.next()) {
                    if (rs.getInt(1) == user1.getId()) {

                        int optBlogId = rs.getInt(3);

                        if (optBlogId != 0 && !userBlogs.contains(optBlogId)) {
                            UserBlogDTO blog1 = new UserBlogDTO();
                            userBlogs.add(optBlogId);
                            blog1.setId(optBlogId);
                            blog1.setTitle(rs.getString(4));
                            if (user1.getBlogs() == null) {
                                user1.setBlogs(new ArrayList<UserBlogDTO>());
                            }
                            user1.addBlog(blog1);
                        }

                        if (rs.getInt(5) != 0) {
                            UserReplyDTO reply1 = new UserReplyDTO();
                            reply1.setId(rs.getInt(5));
                            reply1.setTitle(rs.getString(6));
                            UserBlogDTO blog2 = new UserBlogDTO();
                            blog2.setId(rs.getInt(7));
                            blog2.setTitle(rs.getString(8));
                            reply1.setBlogDTO(blog2);
                            if (user1.getReplies() == null) {
                                user1.setReplies(new ArrayList<UserReplyDTO>());
                            }
                            user1.addReply(reply1);
                        }

                    } else {

                        if (user1 != null && user1.getId() != 0) {
                            String userAsString = objectMapper.writeValueAsString(user1);
                            users.add(userAsString);
                            user1 = new UserDTO();
                        }

                        user1.setId(rs.getInt(1));
                        user1.setUsername(rs.getString(2));

                        int optBlogId = rs.getInt(3);

                        if (optBlogId != 0) {
                            UserBlogDTO blog1 = new UserBlogDTO();
                            blog1.setId(optBlogId);
                            userBlogs.add(optBlogId);
                            blog1.setTitle(rs.getString(4));
                            if (user1.getBlogs() == null) {
                                user1.setBlogs(new ArrayList<UserBlogDTO>());
                            }
                            user1.addBlog(blog1);
                        }
                        if (rs.getInt(5) != 0) {
                            UserReplyDTO reply1 = new UserReplyDTO();
                            reply1.setId(rs.getInt(5));
                            reply1.setTitle(rs.getString(6));
                            UserBlogDTO blog2 = new UserBlogDTO();
                            blog2.setId(rs.getInt(7));
                            blog2.setTitle(rs.getString(8));
                            reply1.setBlogDTO(blog2);
                            if (user1.getReplies() == null) {
                                user1.setReplies(new ArrayList<UserReplyDTO>());
                            }
                            user1.addReply(reply1);
                        }

                    }
                }

                return users;

            // if the split url is 4 we are dealing with a request that looks like /user/id/{x}
            } else if (spliturl.length == 4) {

                query = "select * from users where " + spliturl[2] + " = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, spliturl[3]);
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery();
                List<String> users = new ArrayList<>();

                while (rs.next()) {
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

    public String postResource(User user) {

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO users ( username ) VALUES ( ? )";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;

        return "success";
    }

    private String getAllQuery() {
        return "select u.user_id, \n" + //
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
    }

}
