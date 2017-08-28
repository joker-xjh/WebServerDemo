package server;

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
	
	
	
	
	
	
	
	
	
	
	public Map<String, String> getRequestCookie() {
		return requestCookie;
	}

}
