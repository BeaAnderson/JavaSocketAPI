package com.beacodeart.api;

import java.util.List;

public class BlogRepository {
    static String password = APIConnection.getPassword();
    static String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    static String username = APIConnection.getUsername();

    public List<String> getResource(String url) {
        throw new UnsupportedOperationException("Unimplemented method 'getResource'");
    }

}
