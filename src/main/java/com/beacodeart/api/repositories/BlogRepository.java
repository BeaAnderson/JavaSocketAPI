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
import com.beacodeart.api.dto.BlogDTO;
import com.beacodeart.api.dto.BlogReplyDTO;
import com.beacodeart.api.dto.BlogUpdateDTO;
import com.beacodeart.api.dto.BlogUserDTO;
import com.beacodeart.api.dto.DeleteBlogDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BlogRepository {
    static String password = APIConnection.getPassword();
    static String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    static String username = APIConnection.getUsername();
    static ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> getResource(String givenUrl) {
        String[] spliturl = givenUrl.split("/");

        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            if (spliturl.length == 2) {
                String query = getAllQuery();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                List<String> blogs = new ArrayList<>();
                BlogDTO blog1 = new BlogDTO();
                // check this may need reset at every new blog
                HashSet<Integer> blogusers = new HashSet<>();

                while (rs.next()) {
                    if (rs.getInt(1) == blog1.getBlog_id()) {

                        if (rs.getInt(3) != 0) {
                            addUserToBlog(blog1, rs, blogusers);
                        }
                        if (rs.getInt(5) != 0) {
                            addReplyToBlog(blog1, rs);
                        }
                    } else {
                        if (blog1 != null && blog1.getBlog_id() != 0) {
                            String blogAsString = objectMapper.writeValueAsString(blog1);
                            blogs.add(blogAsString);
                            blog1 = new BlogDTO();
                        }

                        blog1.setBlog_id(rs.getInt(1));
                        blog1.setTitle(rs.getString(2));

                        if (rs.getInt(3) != 0) {
                            addUserToBlog(blog1, rs, blogusers);
                        }
                        if (rs.getInt(5) != 0) {
                            addReplyToBlog(blog1, rs);
                        }
                    }
                }

                if (blog1 != null) {
                    String blogAsString = objectMapper.writeValueAsString(blog1);
                    blogs.add(blogAsString);
                }

                rs.close();
                stmt.close();

                return blogs;
            } else if (spliturl.length == 4) {

                String query = getSpecific();
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(spliturl[3]));
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery();
                List<String> blogs = new ArrayList<>();
                BlogDTO blog1 = new BlogDTO();
                // check this may need reset at every new blog
                HashSet<Integer> blogusers = new HashSet<>();

                while (rs.next()) {
                    if (rs.getInt(1) == blog1.getBlog_id()) {

                        if (rs.getInt(3) != 0) {
                            addUserToBlog(blog1, rs, blogusers);
                        }
                        if (rs.getInt(5) != 0) {
                            addReplyToBlog(blog1, rs);
                        }
                    } else {
                        if (blog1 != null && blog1.getBlog_id() != 0) {
                            String blogAsString = objectMapper.writeValueAsString(blog1);
                            blogs.add(blogAsString);
                            blog1 = new BlogDTO();
                        }

                        blog1.setBlog_id(rs.getInt(1));
                        blog1.setTitle(rs.getString(2));

                        if (rs.getInt(3) != 0) {
                            addUserToBlog(blog1, rs, blogusers);
                        }
                        if (rs.getInt(5) != 0) {
                            addReplyToBlog(blog1, rs);
                        }
                    }
                }

                if (blog1 != null) {
                    String blogAsString = objectMapper.writeValueAsString(blog1);
                    blogs.add(blogAsString);
                }

                rs.close();
                stmt.close();

                return blogs;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, String> postResource(BlogDTO blog) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // check user exists
            HashMap<String, String> returnVal = new HashMap<>();
            String check = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(check);
            stmt.setInt(1, blog.getUserDTO().getUser_id());
            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new Exception("Not found");
            }
            rs.close();
            stmt.close();

            // add the blog in
            String query = "INSERT INTO blogs ( title, user_id ) VALUES ( ?, ? )";
            PreparedStatement stmt2 = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt2.setString(1, blog.getTitle());
            stmt2.setInt(2, blog.getUserDTO().getUser_id());
            stmt2.executeUpdate();
            ResultSet rs2 = stmt2.getGeneratedKeys();
            rs2.next();
            String id = rs2.getString(1);
            String uri = "/blogs/blog_id/" + id;
            String object = getResource(uri).get(0);
            returnVal.put(id, object);
            rs2.close();
            stmt2.close();
            return returnVal;

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
                   v.user_id,
                   v.username
                FROM blogs b
                left join users u
                on b.user_id = u.user_id
                left join replies r
                on b.blog_id = r.blog_id
                left join (
                   select user_id,
                   username
                   from users
                ) v
                on r.user_id = v.user_id
                       """;
    }

    private static String getSpecific() {
        return """
                   SELECT b.blog_id,
                   b.title,
                   u.user_id,
                   u.username,
                   r.reply_id,
                   r.title,
                   v.user_id,
                   v.username
                FROM blogs b
                left join users u
                on b.user_id = u.user_id
                left join replies r
                on b.blog_id = r.blog_id
                left join (
                   select user_id,
                   username
                   from users
                ) v
                on r.user_id = v.user_id
                where b.blog_id = ?;
                """;
    }

    public static void addUserToBlog(BlogDTO blog, ResultSet rs, HashSet<Integer> set) throws SQLException {
        int userId = rs.getInt(3);
        set.add(userId);
        BlogUserDTO user1 = new BlogUserDTO();
        user1.setUser_id(userId);
        user1.setUsername(rs.getString(4));
        blog.setUserDTO(user1);
    }

    public static void addReplyToBlog(BlogDTO blog, ResultSet rs) throws SQLException {
        BlogReplyDTO reply1 = new BlogReplyDTO();
        reply1.setReply_id(rs.getInt(5));
        reply1.setTitle(rs.getString(6));
        BlogUserDTO user2 = new BlogUserDTO();
        user2.setUser_id(rs.getInt(7));
        user2.setUsername(rs.getString(8));
        reply1.setUser(user2);
        if (blog.getReplyDTOs() == null) {
            blog.setReplyDTOs(new ArrayList<BlogReplyDTO>());
        }
        blog.addReply(reply1);
    }

    public static String deleteResource(String url2, DeleteBlogDTO blogDTO) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            int blogId = Integer.parseInt(url2.split("/")[3]);
            String query = "select * from blogs where blog_id = ? and title like ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, blogId);
            stmt.setString(2, "%" + blogDTO.getTitle() + "%");
            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new Exception("blog not found");
            }
            rs.close();
            stmt.close();
            String query2 = "delete from blogs where blog_id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            stmt2.setInt(1, blogId);
            stmt2.execute();
            stmt2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    public static String putResource(String url2, BlogUpdateDTO blogDto) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            int blogId = Integer.parseInt(url2.split("/")[3]);
            String query = "update blogs set title = ? where blog_id = ?";
            if (blogDto.getTitle() == null) {
                throw new Exception("title can'tt be null");
            }
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, blogDto.getTitle());
            stmt.setInt(2, blogId);
            stmt.executeUpdate();
            stmt.close();
            return "string";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
