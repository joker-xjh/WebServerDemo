package http;

public interface HttpRequest extends HttpMessage{

	HttpMethod getMethod();
	String getRequestUri();
	void setRequestUri(String uri);
	void setMethod(HttpMethod extractMethod);
}
