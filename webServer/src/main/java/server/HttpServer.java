package server;

import java.net.Socket;

public interface HttpServer {
	
	void start();
	void stop();
	void dispatchRequest(Socket client);
	String getServerSignature();
	String getRootPath();
	String getRootDirectoryPath();
	

}
