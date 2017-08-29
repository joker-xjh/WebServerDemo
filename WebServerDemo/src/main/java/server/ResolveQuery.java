package server;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ResolveQuery {
	
	@SuppressWarnings("deprecation")
	public static Map<String, String> parse(String query){
		Map<String, String> map = new HashMap<>();
		String[] array = query.split("&");
		for(int i=0; i<array.length; i++) {
			String[] split = array[i].split("=");
			map.put(URLDecoder.decode(split[0]), URLDecoder.decode(split[1]));
		}
		
		return map;
	}
	

}
