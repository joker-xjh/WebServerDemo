package httpImp;

import java.util.HashMap;
import java.util.Map;

import http.HttpMessage;
import http.HttpVersion;

public class BasicHttpMessage implements HttpMessage{
	
	private byte[] entity;
	private HttpVersion version;
	private Map<String, String> headers = new HashMap<String, String>();
	
	public void setVersion(HttpVersion version) {
		this.version = version;
	}
	
	
	
	public HttpVersion getVersion() {
		return version;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public byte[] getEntity() {
		return entity;
	}

	public HttpVersion getHttpVersion() {
		return version;
	}


	@Override
	public void setEntity(byte[] entity) {
		this.entity = entity;
	}

}
