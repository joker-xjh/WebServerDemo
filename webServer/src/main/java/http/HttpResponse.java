package http;

public interface HttpResponse extends HttpMessage{
	
	HttpStatusCode getStatusCode();
	void setStatusCode(HttpStatusCode statusCode);
	void setEntity(byte[] entity);
	

}
