package server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
	
	public static String content="";
	public static byte[] fileBytes;
	public static boolean isFile;
	public static List<String> header = new ArrayList<>();
	public static String STATUS = "HTTP/1.1 200 OK";
	public static List<String> cookies = new ArrayList<>();
	public static String ContentType = "text/html;charset=UTF-8;";
	
	private static final String ENTER = "\r\n";
	
	private static Map<String, String> ContentTypeCollection = new HashMap<>();
	static {
		ContentTypeCollection.put("html", "text/html;charset=UTF-8;");
		ContentTypeCollection.put("json", "application/json; charset=utf-8;");
		ContentTypeCollection.put("xml", "application/xml;charset=UTF-8;");
		ContentTypeCollection.put("zip", "application/x-zip-compressed");
		ContentTypeCollection.put("ico", "image/x-icon");
	}
	
	public static String Server = Options.ServerName;
	
	public static int ContentLength;
	
	public static void setStatus(int code) {
		
		String temp="";
		
		switch (code) {
		case 200:
			temp = "HTTP/1.1 200 OK";
			break;
		case 303:
			temp = "HTTP/1.1 303 See Other";
			break;
		case 304:
			temp = "HTTP/1.1 304 Not Modified";
			break;
		case 403:
			temp = "HTTP/1.1 403 Forbidden";
			break;
		case 404:
			temp = "HTTP/1.1 404 Not Found";
			break;
		case 500:
			temp = "HTTP/1.1 500 Internal Server Error";
		default:
			temp = "HTTP/1.1 200 OK";
			break;
		}
		
		STATUS = temp;
	}
	
	
	public static void setContentLength(int length) {
		ContentLength = length;
	}
	
	public static void setContentType(String type) {
		ContentType = type;
	}
	
	
	public static String findContentType(String type) {
		if(ContentTypeCollection.containsKey(type))
			return ContentTypeCollection.get(type);
		else
			return "application/x-zip-compressed";
	}
	
	public static void setContent(String content) {
		Response.content = content;
	}
	
	public static void addContent(String content) {
		Response.content += content;
	}
	
	public static boolean addHeader(String header) {
		return Response.header.add(header);
	}
	
	public static String getHeader() {
		String header= "";
		header += STATUS + ENTER;
		for(int i=0; i<cookies.size(); i++) {
			header += cookies.get(i) + ENTER;
 		}
		for(int i=0; i<Response.header.size(); i++)
			header += Response.header.get(i) + ENTER;
		
		header += "Content-type: " + Response.ContentType + ENTER;
		header += "Content-length: " + Response.ContentLength + ENTER;
		header += "Server: "+Server + ENTER+ENTER;
		return header;
	}
	
	public static byte[] getContentBytes() throws UnsupportedEncodingException {
		return content.getBytes("UTF-8");
	}
	
	public static void finish() {
		STATUS = "HTTP/1.1 200 OK";
		content = "";
		header = new ArrayList<>();
		cookies = new ArrayList<>();
		ContentLength = 0;
		ContentType = "text/html;charset=UTF-8;";
		fileBytes = new byte[0];
		isFile = false;
	}
	
	

}
