package server;

public class ExceptionalHandling {
	
	public static void set404(Exception exception) {
		NotFound404.set404(exception);
	}
	
	public static void set500(Exception exception) {
		InternalServerError500.set500(exception);
	}
	
	public static void handling(Exception exception) {
		
	}

}
