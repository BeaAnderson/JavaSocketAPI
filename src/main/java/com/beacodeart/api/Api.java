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
		ServerSocket serverSocket = new ServerSocket(port);

		System.out.println("Server started on port " + port);

		// keep the server running
		while (true) {

			Socket client = serverSocket.accept();
			client.setSoTimeout(0);

			new Thread(() -> {
				try {
					InputStream inputStream = client.getInputStream();
					OutputStream outputStream = client.getOutputStream();

					RequestProcessor requestProcessor = new RequestProcessor(
							new ObjectMapper());

					String httpRequest = read(inputStream);

					List<String> lines = Arrays.asList(httpRequest.split("(?m)^\\s*$"));

					Request parsedRequest = parseRequest(lines);

					outputStream.write(parsedRequest.accept(requestProcessor));

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

	static String read(InputStream input) throws IOException {
		StringBuilder result = new StringBuilder();

		do {
			result.append((char) input.read());
		} while (input.available() > 0);

		return result.toString();
	}

	static Request parseRequest(List<String> data) {
		String method = null;
		String url = null;
		Map<String, String> headers = new HashMap<String, String>();
		String body = null;

		if (data.size() > 1) {
			body = data.get(1);
		}

		String unparsedHeaders = data.get(0);
		List<String> headerLines = Arrays.asList(unparsedHeaders.split("\\r?\\n|\\r"));

		String line1 = headerLines.get(0);
		method = line1.split("\\s+")[0].trim();
		url = line1.split("\\s+")[1].trim();

		for (int i = 1; i < headerLines.size(); i++) {
			String line = headerLines.get(i);
			String key = line.split(":")[0];
			String value = line.split(":")[1];
			headers.put(key, value);
		}

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
