package server;

public class InternalServerError500 {
	
	public static void set500(String message) {
		Response.setContent("<h2>500  服务器内部发生错误</h2> "+message);
		Response.STATUS = "500";
	}
	
	public static void set500(Exception exception) {
		set500(exception.toString());
	}

}
