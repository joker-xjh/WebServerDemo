package httpImp;

import java.util.HashMap;
import java.util.Map;

import http.HttpMethod;
import http.HttpRequest;

public class BasicHttpRequest extends BasicHttpMessage implements HttpRequest{
	
	private HttpMethod method;
	private String uri;
	private Map<String, String> headers = new HashMap<>();

	public HttpMethod getMethod() {
		return method;
	}

	public String getRequestUri() {
		return uri;
	}
	
	public void setMethod(HttpMethod method) {
		this.method = method;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public void setRequestUri(String uri) {
		this.uri = uri;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

}
