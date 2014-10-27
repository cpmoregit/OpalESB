package org.opalesb.engine.inbound;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import org.opalesb.common.Log;

public class HTTPDataReceive extends Thread {

	private Socket _httpConnection = null;
	private BufferedReader _httpReceiveReader = null;
	private DataOutputStream _httpResponseWriter = null;

	public HTTPDataReceive(Socket httpConnection) {
		_httpConnection = httpConnection;
	}

	public void run() {

		String requestString = null;
		String headerLine = null;
		StringTokenizer tokenizer = null;
		String httpMethod = null;
		String httpQueryString = null;

		StringBuffer responseBuffer = null;

		try {
			Log.log(Log.INFO, _httpConnection.getInetAddress() + ":"
					+ _httpConnection.getLocalPort());

			_httpReceiveReader = new BufferedReader(new InputStreamReader(
					_httpConnection.getInputStream()));
			_httpResponseWriter = new DataOutputStream(
					_httpConnection.getOutputStream());

			requestString = _httpReceiveReader.readLine();
			headerLine = requestString;

			tokenizer = new StringTokenizer(headerLine);
			httpMethod = tokenizer.nextToken();
			httpQueryString = tokenizer.nextToken();

			responseBuffer = new StringBuffer();
			responseBuffer.append("<b>Welcome to Opal ESB<b><BR>");
			responseBuffer
					.append("<b>Please check if we got your information right:<b><BR>");

			while (_httpReceiveReader.ready()) {
				// Read the HTTP complete HTTP Query
				responseBuffer.append(requestString + "<BR>");
				requestString = _httpReceiveReader.readLine();
			}

			if (httpMethod.equals("GET")) {
				processGetRequest(200, responseBuffer.toString());
				
			}else if (httpMethod.equals("POST")) {
				processPostRequest(200, responseBuffer.toString());
			}

		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}
	
	public void processGetRequest(int statusCode, String responseString) {
		sendGetResponse(statusCode, responseString);
	}
	
	public void processPostRequest(int statusCode, String responseString) {
		
	}

	public void sendGetResponse(int statusCode, String responseString) {

		String statusLine = null;
		String serverDetails = "OPAL ESB HTTP Service";
		String contentLengthLine = null;
		String contentTypeLine = "Content-Type: text/html\r\n";
		
		if(statusCode == 200){
			statusLine = "HTTP/1.1 200 OK\r\n";
		}

		responseString = "<HTML><TITLE>OPAL ESB HTTP Service</TITLE>"
				+ "<BODY>" + responseString + "</BODY></HTML>";
		contentLengthLine = "Content-Length: " + responseString.length()
				+ "\r\n";
		try {
			_httpResponseWriter.writeBytes(statusLine);
			_httpResponseWriter.writeBytes(serverDetails);
			_httpResponseWriter.writeBytes(contentTypeLine);
			_httpResponseWriter.writeBytes(contentLengthLine);
			_httpResponseWriter.writeBytes("Connection: close\r\n\r\n");
			_httpResponseWriter.writeBytes(responseString);

			_httpResponseWriter.close();
		} catch (Exception exp) {
			Log.log(Log.FATAL, exp.getMessage());
		}
	}
	
	public void sendPostResponse(int statusCode, String responseString) {
		
	}

	public static void main(String[] args) {
		try{
			ServerSocket server = new ServerSocket(5000,
					                               10,
					                               InetAddress.getByName("127.0.0.1"));
			Log.log(Log.INFO, "TCPServer waiting for client on port 5000");
			
			while(true){
				Socket connected = server.accept();
					(new HTTPDataReceive(connected)).start();
			}
		}catch(Exception exp){
			Log.log(Log.FATAL, exp.getMessage());
		}
			 
	}

}
