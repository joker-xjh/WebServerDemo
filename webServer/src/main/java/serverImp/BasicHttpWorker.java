package serverImp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;

import constant.HTTP;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatusCode;
import http.HttpVersion;
import httpImp.BasicHttpRequest;
import httpImp.BasicHttpResponse;
import server.HttpServer;
import server.HttpWorker;

public class BasicHttpWorker extends HttpWorker{
	
	private static final String INDEX = "/index.html";
	
	

	public BasicHttpWorker(Socket client, HttpServer server) {
		super(client, server);
	}

	@Override
	protected HttpRequest parseRequest(InputStream in)  {
		HttpRequest request = new BasicHttpRequest();
		try {
			String firstLine = readLine(in);
			request.setVersion(HttpVersion.extractVersion(firstLine));
			request.setRequestUri(firstLine.split(" ", 3)[1]);
			request.setMethod(HttpMethod.extractMethod(firstLine));
			
			String nextLine = "";
			while((nextLine = readLine(in)) != null && !nextLine.equals("")) {
				String[] vals = nextLine.split(": ");
				request.getHeaders().put(vals[0].trim(), vals[1].trim());
			}
			
			if(request.getHeaders().containsKey(HTTP.CONTENT_LENGTH)) {
				int size = Integer.parseInt(request.getHeaders().get(HTTP.CONTENT_LENGTH));
				if(size > 0) {
					byte[] entity = new byte[size];
					in.read(entity);
					request.setEntity(entity);
				}
			}
		} catch (IOException e) {
			internal_server_error = true;
		}catch (Exception e) {
			bad_request = true;
		}
		
		
		
		return request;
	}

	@Override
	protected HttpResponse handleRequest(HttpRequest request){
		HttpResponse response = new BasicHttpResponse();
		response.getHeaders().put(HTTP.SERVER,server.getServerSignature());
		response.getHeaders().put(HTTP.DATE, new Date().toString());
		response.setVersion(request.getHttpVersion());
		String requestUri = request.getRequestUri();
		if(requestUri.equals("/")) {
			requestUri = INDEX;
		}
		
		File file = new File(server.getRootPath()+requestUri);
		try {
			if(!file.getCanonicalPath().startsWith(server.getRootDirectoryPath())) {
				response.setStatusCode(HttpStatusCode.FORBIDDEN);
				return response;
			}
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
			return response;
		}
		
		if(file.exists()) {
			try (InputStream in = new BufferedInputStream(client.getInputStream())){
				
				byte[] buf = new byte[(int)file.length()];
				in.read(buf);
				response.setEntity(buf);
				response.setStatusCode(HttpStatusCode.OK);
				response.getHeaders().put(HTTP.CONTENT_TYPE, URLConnection.guessContentTypeFromName(file.getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
				response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
				return response;
			}
			
		}
		else {
			response.setStatusCode(HttpStatusCode.NOT_FOUND);
			return response;
		}
		
		return response;
	}

	@Override
	protected void sendResponse(HttpResponse response, OutputStream out) throws IOException {
		BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(out, "UTF-8"));
		
		writer.write(response.getHttpVersion()+"");
		writer.write(' ');
		writer.write(response.getStatusCode()+"");
		writer.write(' ');
		writer.write(response.getStatusCode().getReasonPhrase());
		writer.write(HTTP.CRLF);
		
		
		
		
		if(response.getEntity() != null && response.getEntity().length > 0) {
			response.getHeaders().put(HTTP.CONTENT_LENGTH, response.getEntity().length+"");
		}
		else {
			response.getHeaders().put(HTTP.CONTENT_LENGTH, "0");
		}
		
		for(String key : response.getHeaders().keySet()) {
			writer.write(key +": "+ response.getHeaders().get(key) + HTTP.CRLF);
		}
	
		
		writer.write(HTTP.CRLF);
		writer.flush();
		
		if(response.getEntity() != null && response.getEntity().length > 0) {
			out.write(response.getEntity());
		}
		out.flush();
		
		
	}

	@Override
	protected boolean keepAlive(HttpRequest request, HttpResponse response) {
		
		if(response.getHeaders().containsKey(HTTP.CONNECTION) && response.getHeaders().get(HTTP.CONNECTION).equalsIgnoreCase("close")) {
			return false;
		} 
		
		if(request.getHttpVersion().equals(HttpVersion.VERSION_1_1)) {
			
			if(request.getHeaders().containsKey(HTTP.CONNECTION) && request.getHeaders().get(HTTP.CONNECTION).equalsIgnoreCase("close")) {
				return false;
			}
			else {
				return true;
			}
			
		}
		
		
		return false;
	}
	
	

}
