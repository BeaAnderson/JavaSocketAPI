package com.beacodeart.api;

import java.util.List;

public class BlogRepository {
    String password = APIConnection.getPassword();
    String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    String username = APIConnection.getUsername();
    
    public List<String> getResource(String url) {
        throw new UnsupportedOperationException("Unimplemented method 'getResource'");
    }
    
}
