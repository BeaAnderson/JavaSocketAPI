package com.beacodeart.api.repositories;

import com.beacodeart.api.APIConnection;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReplyRepository {
    static String password = APIConnection.getPassword();
    static String url = APIConnection.getUrlStart() + "testdb" + APIConnection.getUrlEnd();
    static String username = APIConnection.getUsername();
    static ObjectMapper objectMapper = new ObjectMapper();
}
