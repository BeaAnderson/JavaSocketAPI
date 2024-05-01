package com.beacodeart.api.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.beacodeart.api.APIConnection;
import com.beacodeart.api.dto.BlogDTO;
import com.beacodeart.api.dto.BlogReplyDTO;
import com.beacodeart.api.dto.BlogUserDTO;
import com.beacodeart.api.models.Blog;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BlogRepository {
    static String password = APIConnection.getPassword();
    static String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    static String username = APIConnection.getUsername();
    static ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> getResource(String givenUrl) {
        String[] spliturl = givenUrl.split("/");

        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            String query = getAllQuery();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<String> blogs = new ArrayList<>();
            BlogDTO blog1 = new BlogDTO();
            //check this may need reset at every new blog
            HashSet<Integer> blogusers = new HashSet<>();

            while (rs.next()){
                if(rs.getInt(1) == blog1.getBlog_id()){

                    if (rs.getInt(3) !=0 ){
                        addUserToBlog(blog1, rs, blogusers);
                    }
                    if(rs.getInt(5)!=0){
                        addReplyToBlog(blog1, rs);
                    }
                } else {
                    System.out.println(blog1.getBlog_id());
                    if (blog1!=null && blog1.getBlog_id()!=0){
                        String blogAsString = objectMapper.writeValueAsString(blog1);
                        blogs.add(blogAsString);
                        blog1 = new BlogDTO();
                    }

                    blog1.setBlog_id(rs.getInt(1));
                    blog1.setTitle(rs.getString(2));

                    if(rs.getInt(3)!=0){
                        addUserToBlog(blog1, rs, blogusers);
                    }
                    if(rs.getInt(5)!=0){
                        addReplyToBlog(blog1, rs);
                    }
                }
            }

            if (blog1!=null){
                String blogAsString = objectMapper.writeValueAsString(blog1);
                blogs.add(blogAsString);
            }

            rs.close();
            stmt.close();

            return blogs;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String postResource(BlogDTO blog){
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            //check user exists
            String check = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(check);
            stmt.setInt(1, blog.getUserDTO().getUser_id());
            ResultSet rs = stmt.executeQuery();
            if(!rs.isBeforeFirst()){
                throw new Exception("Not found");
            }
            rs.close();
            stmt.close();

            
            //add the blog in
            String query = "INSERT INTO blogs ( title, user_id ) VALUES ( ?, ? )";
            PreparedStatement stmt2 = conn.prepareStatement(query);
            
            stmt2.setString(1, blog.getTitle());
            stmt2.setInt(2, blog.getUserDTO().getUser_id());
            stmt2.executeUpdate();
            stmt2.close();
            
        } catch (Exception e){
            e.printStackTrace();
        }
        return "success";
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

    public static void addUserToBlog(BlogDTO blog, ResultSet rs, HashSet<Integer> set) throws SQLException{
        int userId = rs.getInt(3);
        set.add(userId);
        BlogUserDTO user1 = new BlogUserDTO();
        user1.setUser_id(userId);
        user1.setUsername(rs.getString(4));
        blog.setUserDTO(user1);
    }

    public static void addReplyToBlog(BlogDTO blog, ResultSet rs) throws SQLException{
        BlogReplyDTO reply1 = new BlogReplyDTO();
        reply1.setReply_id(rs.getInt(5));
        reply1.setTitle(rs.getString(6));
        BlogUserDTO user2 = new BlogUserDTO();
        user2.setUser_id(rs.getInt(7));
        user2.setUsername(rs.getString(8));
        reply1.setUser(user2);
        if(blog.getReplyDTOs()==null){
            blog.setReplyDTOs(new ArrayList<BlogReplyDTO>());
        }
        blog.addReply(reply1);
    }

}
