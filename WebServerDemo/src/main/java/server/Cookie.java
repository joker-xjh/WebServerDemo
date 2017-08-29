package server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Cookie {
	
	private static Map<String, String> requestCookie;
	
	
	public static void parse(String cookie) {
		requestCookie = new HashMap<>();
		String[] array = cookie.split(";");
		for(int i=0; i<array.length; i++) {
			String[] temp = array[i].trim().split("=");
			requestCookie.put(temp[0].trim(), temp[1].trim());
		}
	}
	
	
	public static void setCookie(String name, String value, int maxAge, String path, String domain) {
		String setCookie = "Set-Cookie: "+name +"="+encode(value)+";Max-Age: "+maxAge+";Path: "+path+";Domain: "+domain+";";               
		Response.cookies.add(setCookie);
	}
	
	
	
	public static void setCookie(String name, String value, int maxAge, String path) {
		String setCookie = "Set-Cookie: "+name +"="+encode(value)+";Max-Age: "+maxAge+";Path: "+path+";";
		Response.cookies.add(setCookie);
	}
	
	
	
	public static void setCookie(String name, String value, int maxAge) {
		String setCookie = "Set-Cookie: "+name +"="+encode(value)+";Max-Age="+maxAge+";";
		Response.cookies.add(setCookie);
	}
	
	
	public static void setCookie(String name, String value) {
		String setCookie = "Set-Cookie: "+name +"="+encode(value)+";";
		Response.cookies.add(setCookie);
	}
	
	
	
	public static String getCookie(String name) {
		if(requestCookie != null && requestCookie.containsKey(name)) {
			String value = requestCookie.get(name);
			return decode(value);
		}
		
		return null;
	}
	
	
	private static String encode(String encode) {
		try {
			encode = URLEncoder.encode(encode, "UTF-8");
			return encode;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String decode(String decode) {
		try {
			decode = URLDecoder.decode(decode, "UTF-8");
			return decode;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Map<String, String> getRequestCookie() {
		return requestCookie;
	}

}
