package server;

import java.util.HashMap;
import java.util.Map;

public class Handler {
	
	public Request request;
	
	public void init(Request request) {
		this.request = request;
	}
	
	public Object getArgument(String key) {
		return request.getArgument().get(key);
	}
	
	public void write(String s) {
		Response.setContent(s);
	}
	
	public void redirect(String url) {
		Response.STATUS = "303";
		if(url.toLowerCase().indexOf("http://") < 0) {
			url = "http://"+request.getHeader().get("Host")+url;
		}
		Response.addHeader("Location: "+url);
	}
	
	public BaseTemplate render(String fileName) {
		BaseTemplate baseTemplate = null;
		if(Options.DEBUG) {
			fileName = fileName.replace(".", "/");
			TplCompile tplCompile = new TplCompile();
			baseTemplate = (BaseTemplate)tplCompile.run(fileName+ ".html");
			if(baseTemplate == null) {
				InternalServerError500.set500(fileName + "模版不存在!");
			}
			
		}
		else {
			try {
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName("Template."+fileName+"Template");
				baseTemplate = (BaseTemplate) clazz.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return baseTemplate;
	}
	
	
	public void jump(String message, String url, int time, boolean status) {
		boolean isSetUrl = false;
		if(message == null || message.length() == 0) {
			message = "Message!";
		}
		if(time < 1)
			time = 3;
		if("XMLHttpRequest".equals(request.getHeader().get("X-Request-With"))) {
			Map<String, Object> map = new HashMap<>();
			map.put("status", status);
			map.put("data", message);
			Response.setContentType(Response.findContentType("json"));
			Response.setContent(Json.toJson(map));
		}
		else {
			BaseTemplate jump = this.render("jump");
			jump.assign("status", status);
			jump.assign("message", message);
			if(url != null) {
				isSetUrl = true;
			}
			jump.assign("isSetUrl", isSetUrl);
			jump.assign("url", url);
			jump.assign("time", time);
			Response.setContent(jump.display());
		}
		
	}
	
	public void success(String message, String url, int time) {
		if(message == null || message.length()==0) {
			message = "success";
		}
		this.jump(message, url, time, true);
	}
	
	
	 public void success(String message, String url) {
	        this.success(message, url, 0);
	 }
	 
	 
	 public void success(String message) {
	        this.success(message, null, 0);
	 }
	
	 
	 public void error(String message, String url, int time) {
		 if(message == null || message.length() == 0) {
			 message = "error";
		 }
		 this.jump(message, url, time, false);
	 }
	 
	 public void error(String message, String url) {
	        this.error(message, url, 0);
	 }
	 
	 
	 public void error(String message) {
	        this.error(message, null, 0);
	 }
	
	
	
	
	
	
	
	
	
	
	

}
