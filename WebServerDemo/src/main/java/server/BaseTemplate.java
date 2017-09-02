package server;

import java.util.HashMap;
import java.util.Map;

public class BaseTemplate {
	
	private Map<String, Object> param;
	
	public BaseTemplate() {
		param = new HashMap<>();
	}
	
	public String display() {
		return param.toString();
	}
	
	public String get(String key) {
		Object val = param.get(key);
		if(val != null)
			return val.toString();
		return null;
	}
	
	
	public boolean getBoolean(String key) {
		Boolean boolean1 = (Boolean)param.get(key);
		return boolean1.booleanValue();
	}
	
	
	public int getInt(String key) {
		Integer integer = (Integer) param.get(key);
		return integer.intValue();
	}
	
	public Object[] getArray(String key) {
		return (Object[]) param.get(key);
	}
	
	
	public void assign(String key, String value) {
		param.put(key, value);
	}
	
	public void assign(String key, int value) {
        this.param.put(key, value);
    }
	
	public void assign(String key, boolean value) {
        this.param.put(key, value);
    }
	
	public void assign(String key, Object[] value) {
        this.param.put(key, value);
    }
	

}
