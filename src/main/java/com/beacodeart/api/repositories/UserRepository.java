package com.beacodeart.api.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.beacodeart.api.APIConnection;
import com.beacodeart.api.dto.DeleteUserDTO;
import com.beacodeart.api.dto.UserBlogDTO;
import com.beacodeart.api.dto.UserDTO;
import com.beacodeart.api.dto.UserReplyDTO;
import com.beacodeart.api.dto.UserUpdateDTO;
import com.beacodeart.api.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRepository {
    static String password = APIConnection.getPassword();
    static String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    static String username = APIConnection.getUsername();
    static String query;
    static ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> getResource(String givenUrl) {

        String[] spliturl = givenUrl.split("/");

        // get the connection
        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            // if the split url array is size two the request we are dealing with is /users
            if (spliturl.length == 2) {
                query = getAllQuery();

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                List<String> users = new ArrayList<>();
                UserDTO user1 = new UserDTO();
                // check this may need reset at every new user
                HashSet<Integer> userBlogs = new HashSet<>();

                while (rs.next()) {
                    if (rs.getInt(1) == user1.getId()) {

                        int optBlogId = rs.getInt(4);

                        if (optBlogId != 0 && !userBlogs.contains(optBlogId)) {
                            addBlogToUser(user1, rs, userBlogs, optBlogId);
                        }
                        if (rs.getInt(6) != 0) {
                            addReplyToUser(user1, rs);
                        }

                    } else {

                        if (user1 != null && user1.getId() != 0) {
                            String userAsString = objectMapper.writeValueAsString(user1);
                            users.add(userAsString);
                            user1 = new UserDTO();
                        }

                        user1.setId(rs.getInt(1));
                        user1.setUsername(rs.getString(2));
                        user1.setPassword(rs.getString(3));

                        int optBlogId = rs.getInt(4);

                        if (optBlogId != 0) {
                            addBlogToUser(user1, rs, userBlogs, optBlogId);
                        }
                        if (rs.getInt(6) != 0) {
                            addReplyToUser(user1, rs);
                        }

                    }
                }

                if (user1 != null) {
                    String userAsString = objectMapper.writeValueAsString(user1);
                    users.add(userAsString);
                }

                rs.close();
                stmt.close();

                return users;

                // if the split url is 4 we are dealing with a request that looks like
                // /users/param/{value}

            } else if (spliturl.length == 4) {
                // TODO fix
                query = getSpecific();
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(spliturl[3]));
                ResultSet rs = stmt.executeQuery();
                List<String> users = new ArrayList<>();
                UserDTO user1 = new UserDTO();
                HashSet<Integer> userBlogs = new HashSet<>();

                while (rs.next()) {
                    if (rs.getInt(1) == user1.getId()) {

                        int optBlogId = rs.getInt(4);

                        if (optBlogId != 0 && !userBlogs.contains(optBlogId)) {
                            addBlogToUser(user1, rs, userBlogs, optBlogId);
                        }
                        if (rs.getInt(6) != 0) {
                            addReplyToUser(user1, rs);
                        }

                    } else {

                        if (user1 != null && user1.getId() != 0) {
                            String userAsString = objectMapper.writeValueAsString(user1);
                            users.add(userAsString);
                            user1 = new UserDTO();
                        }

                        user1.setId(rs.getInt(1));
                        user1.setUsername(rs.getString(2));
                        user1.setPassword(rs.getString(3));

                        int optBlogId = rs.getInt(4);

                        if (optBlogId != 0) {
                            addBlogToUser(user1, rs, userBlogs, optBlogId);
                        }
                        if (rs.getInt(6) != 0) {
                            addReplyToUser(user1, rs);
                        }

                    }
                }

                if (user1 != null) {
                    String userAsString = objectMapper.writeValueAsString(user1);
                    users.add(userAsString);
                }

                rs.close();
                stmt.close();

                return users;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, String> postResource(User user) {

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            HashMap<String, String> returnVal = new HashMap<>();
            String query = "INSERT INTO users ( username, password ) VALUES ( ?, ? )";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            String id = rs.getString(1);
            String uri = "/users/user_id/" + id;
            String object = getResource(uri).get(0);
            returnVal.put(id, object);
            rs.close();
            stmt.close();
            return returnVal;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getAllQuery() {
        return """
                 select u.user_id,
                 u.username,
                 u.password,
                 b.blog_id,
                 b.title,
                 r.reply_id,
                 r.title as 'reply_title',
                r.blog_id as 'reply_blog',
                 v.title as 'reply_blog_title'
                from users u
                left join blogs b
                 on u.user_id = b.user_id
                 left join replies r
                on u.user_id = r.user_id
                 left join (
                 select title, blog_id
                 from blogs
                 ) v
                 on r.blog_id = v.blog_id;
                """;
    }

    private static String getSpecific() {

        return """
                 select u.user_id,
                 u.username,
                 u.password,
                 b.blog_id,
                 b.title,
                 r.reply_id,
                 r.title as 'reply_title',
                r.blog_id as 'reply_blog',
                 v.title as 'reply_blog_title'
                from users u
                left join blogs b
                 on u.user_id = b.user_id
                 left join replies r
                on u.user_id = r.user_id
                 left join (
                 select title, blog_id
                 from blogs
                 ) v
                 on r.blog_id = v.blog_id
                 where u.user_id = ?
                """;
    }

    private static void addBlogToUser(UserDTO user, ResultSet rs, HashSet<Integer> set, int blogId)
            throws SQLException {
        set.add(blogId);
        UserBlogDTO blog1 = new UserBlogDTO();
        blog1.setId(blogId);
        blog1.setTitle(rs.getString(4));
        if (user.getBlogs() == null) {
            user.setBlogs(new ArrayList<UserBlogDTO>());
        }
        user.addBlog(blog1);
    }

    private static void addReplyToUser(UserDTO user1, ResultSet rs) throws SQLException {
        UserReplyDTO reply1 = new UserReplyDTO();
        reply1.setId(rs.getInt(6));
        reply1.setTitle(rs.getString(7));
        UserBlogDTO blog2 = new UserBlogDTO();
        blog2.setId(rs.getInt(8));
        blog2.setTitle(rs.getString(9));
        reply1.setBlogDTO(blog2);
        if (user1.getReplies() == null) {
            user1.setReplies(new ArrayList<UserReplyDTO>());
        }
        user1.addReply(reply1);
    }

    public static String deleteResource(String url2, DeleteUserDTO userDTO) {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            int userId = Integer.parseInt(url2.split("/")[3]);
            String query = "select * from users where username = ? and user_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, userDTO.getUsername());
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new Exception("user not found");
            }
            rs.close();
            stmt.close();
            String query2 = "delete from users where user_id = ?";
            PreparedStatement stmt2 = con.prepareStatement(query2);
            stmt2.setInt(1, userId);
            stmt2.execute();
            stmt2.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;

        // delete from users where username = 'testUserPass' and user_id = 6;
    }

    public static String putResource(String url2, UserUpdateDTO user1) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            int userId = Integer.parseInt(url2.split("/")[3]);
            if (user1.getUsername() == null && user1.getPassword() == null) {
                throw new Exception("user not null");
            }
            if (user1.getUsername() != null && user1.getPassword() != null) {
                String query = "update users set username = ?, password = ? where user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, user1.getUsername());
                stmt.setString(2, user1.getPassword());
                stmt.setInt(3, userId);
                System.out.println(stmt);
                stmt.executeUpdate();
                stmt.close();
                return "string";
            }
            if (user1.getUsername() != null) {
                System.out.println("updating username");
                String query2 = "update users set username = ? where user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query2);
                stmt.setString(1, user1.getUsername());
                stmt.setInt(2, userId);
                stmt.executeUpdate();
                stmt.close();
                return "string";
            }
            if (user1.getPassword() != null) {
                System.out.println("updating password");
                String query3 = "update users set password = ? where user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query3);
                stmt.setString(1, user1.getPassword());
                stmt.setInt(2, userId);
                stmt.executeUpdate();
                stmt.close();
                return "string";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
