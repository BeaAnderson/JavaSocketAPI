package com.beacodeart.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

					String httpRequest = read(inputStream);

					List<String> lines = Arrays.asList(httpRequest.split("(?m)^\\s*$"));

					Request parsedRequest = parseRequest(lines);

					System.out.println(parsedRequest.getUrl());

					// hello world response
					outputStream.write(("HTTP/1.1 200 OK\r\n").getBytes());
					outputStream.write(("\r\n").getBytes());
					outputStream.write(("Hello World").getBytes());
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
		;
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

		return new Request(method, url, headers, body);
	}

	static class Request {
		private String method;
		private String url;
		private Map<String, String> headers;
		private String body;

		public Request(String method, String url, Map<String, String> headers, String body) {
			this.method = method;
			this.url = url;
			this.headers = headers;
			this.body = body;
		}

		public String getBody() {
			return body;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public String getMethod() {
			return method;
		}

		public String getUrl() {
			return url;
		}
	}
}
