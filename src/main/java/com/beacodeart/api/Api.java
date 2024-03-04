package com.beacodeart.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Api {
	public static void main(String[] args) throws IOException {
		//start the server
		int port = 8080;
		ServerSocket serverSocket = new ServerSocket(port);
		
		System.out.println("Server started on port " + port);
		
		//keep the server running
		while (true) {
			
			Socket client = serverSocket.accept();
			client.setSoTimeout(0);
			
			new Thread(() -> {
				try {
				InputStream inputStream = client.getInputStream();
				OutputStream outputStream = client.getOutputStream();
				
				String httpRequest = read(inputStream);
				
				List<String> lines = Arrays.asList(httpRequest.split("(?m)^\\s*$"));
				
				for (String line: lines) {
					System.out.println("line " + line);
				}
				
				System.out.println(lines.size());
				
				String headers = lines.get(0);
				//System.out.println(headers);
				String body;
				
				if (lines.size()>1) {
					body = lines.get(1);
					System.out.println(body);
				}
				
				//hello world response
				OutputStream clientOutput = client.getOutputStream();
				clientOutput.write(("HTTP/1.1 200 OK\r\n").getBytes());
				clientOutput.write(("\r\n").getBytes());
				clientOutput.write(("Hello World").getBytes());
				clientOutput.flush();
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					try {
						client.close();
					} catch (IOException e){
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
		} while (input.available()>0);
		
		return result.toString();
	}
}
	
