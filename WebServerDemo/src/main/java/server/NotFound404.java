package server;

public class NotFound404 {
	
	public static void set404(String message) {
		Response.setContent("<h2>404 没有找到</h2>"+message);
		Response.STATUS = "404";
	}
	
	public static void set404(Exception e) {
		set404(e.toString());
	}

}
