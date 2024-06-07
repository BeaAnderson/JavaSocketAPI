package com.beacodeart.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beacodeart.api.Request.DeleteRequest;
import com.beacodeart.api.Request.GetRequest;
import com.beacodeart.api.Request.PostRequest;
import com.beacodeart.api.Request.PutRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

//impliment singleton for repositoriers?
public class Api {
	public static void main(String[] args) throws IOException {
		// start the server
		int port = 8080;

		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(port);

		System.out.println("Server started on port " + port);

		// keep the server running
		while (true) {

			// creates a new socket that will continually listen for incoming request
			Socket client = serverSocket.accept();
			client.setSoTimeout(0);

			new Thread(() -> {
				try {
					InputStream inputStream = client.getInputStream();
					OutputStream outputStream = client.getOutputStream();

					RequestProcessor requestProcessor = new RequestProcessor(
							new ObjectMapper());

					// httprequest stores the full request in one continuous string
					String httpRequest = read(inputStream);

					//because the parse request method takes an array of lines we need to create that array here
					//splits on a blank line because of http specification
					//as a result returns header and body
					List<String> lines = Arrays.asList(httpRequest.split("(?m)^\\s*$"));

					//returns an object that will implement the request interface
					Request parsedRequest = parseRequest(lines);

					//use visitor pattern to determine what should happen based on the request type
					outputStream.write(parsedRequest.accept(requestProcessor));

					//send our output
					outputStream.flush();

				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();

		}
	}

	// takes our input stream and returns the full input stream in string form
	static String read(InputStream input) throws IOException {
		StringBuilder result = new StringBuilder();

		do {
			result.append((char) input.read());
		} while (input.available() > 0);

		return result.toString();
	}

	//takes the individual lines of the http request and returns an object of type request
	static Request parseRequest(List<String> data) {
		String method = null;
		String url = null;
		Map<String, String> headers = new HashMap<String, String>();
		String body = null;

		//if the request array is 2 items long the 2nd item index pos 1 is the body
		//store this as one given rest specification/ we take in json
		if (data.size() > 1) {
			body = data.get(1);
		}

		//we need trto further process the headers of the request
		//we break the request into individual lines based on newline char regex
		String unparsedHeaders = data.get(0);
		List<String> headerLines = Arrays.asList(unparsedHeaders.split("\\r?\\n|\\r"));

		//line 1 tells us the type of request and the url of the rtequested resource
		String line1 = headerLines.get(0);
		method = line1.split("\\s+")[0].trim();
		url = line1.split("\\s+")[1].trim();

		//all other lines can be split and stored in a dictionary
		for (int i = 1; i < headerLines.size(); i++) {
			String line = headerLines.get(i);
			String key = line.split(":")[0];
			String value = line.split(":")[1];
			headers.put(key, value);
		}

		//the specific request to be returned is defined here
		switch (method) {
			case "GET":
				return new GetRequest(url, headers);
			case "POST":
				return new PostRequest(url, headers, body);
			case "PUT":
				return new PutRequest(url, headers, body);
			case "DELETE":
				return new DeleteRequest(url, headers, body);
			default:
				break;
		}

		return null;
	}

}
