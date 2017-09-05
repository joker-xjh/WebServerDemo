package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;

import constant.HTTP;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatusCode;
import httpImp.BasicHttpResponse;

public abstract class HttpWorker implements Callable<Void>{
	
	protected Socket client;
	protected HttpServer server;
	
	protected boolean bad_request = false;
	protected boolean internal_server_error = false;
	
	public HttpWorker(Socket client, HttpServer server) {
		this.client = client;
		this.server = server;
	}
	
	public Void call() throws Exception {
		
		HttpRequest request = parseRequest(client.getInputStream());
		
		if(bad_request || internal_server_error) {
			HttpResponse response = tempResponse(request);
			if(bad_request) {
				response.setStatusCode(HttpStatusCode.BAD_REQUEST);
				response.setEntity("bad request".getBytes("UTF-8"));
			}
			else {
				response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
				response.setEntity("server error".getBytes("UTF-8"));
			}
			response.getHeaders().put(HTTP.CONNECTION, "close");
			sendResponse(response, client.getOutputStream());
			client.close();
			return null;
		}
		
		HttpResponse response = handleRequest(request);
		if(keepAlive(request, response)) {
			sendResponse(response, client.getOutputStream());
			server.dispatchRequest(client);
		}
		else {
			response.getHeaders().put(HTTP.CONNECTION, "close");
			sendResponse(response, client.getOutputStream());
			client.close();
		}
		
		return null;
	}
	
	protected HttpResponse tempResponse(HttpRequest request) {
		HttpResponse response = new BasicHttpResponse();
		response.getHeaders().put(HTTP.SERVER,server.getServerSignature());
		response.getHeaders().put(HTTP.DATE, new Date().toString());
		response.setVersion(request.getHttpVersion());
		return response;
	}
	
	
	protected String readLine(InputStream in) throws IOException{
		
		StringBuilder sb = new StringBuilder();
		boolean cr = false;
		int read = -1;
		
		while( (read = in.read()) != -1 ) {
			
			if(read == '\r') {
				cr = true;
				continue;
			}
			else if(read == '\n' && cr) {
				return sb.toString();
			}
			else {
				sb.append((char)read);
				cr = false;
			}
			
		}
		
		return sb.toString();
	}
	
	
	protected abstract HttpRequest parseRequest(InputStream in) throws IOException;
	
	protected abstract HttpResponse handleRequest(HttpRequest request) throws IOException;
	
	protected abstract void sendResponse(HttpResponse response, OutputStream out) throws IOException;
	
	protected abstract boolean keepAlive(HttpRequest request, HttpResponse response);
	
	
	
	
	
	
	

}
