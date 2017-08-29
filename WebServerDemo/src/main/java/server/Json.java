package server;

import java.util.Map;

import net.sf.json.JSONObject;

public class Json {
	
	public static String toJson(Map<String, ? extends Object> map) {
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}

}
