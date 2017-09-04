package httpImp;

import http.HttpResponse;
import http.HttpStatusCode;
import http.HttpVersion;

public class BasicHttpResponse extends BasicHttpMessage implements HttpResponse{
	
	private HttpStatusCode statusCode;
	private byte[] entity;

	public HttpStatusCode getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(HttpStatusCode statusCode) {
		this.statusCode = statusCode;
	}
	
	@Override
	public void setVersion(HttpVersion version) {
		super.setVersion(version);
	}

	@Override
	public void setEntity(byte[] entity) {
		this.entity = entity;
	}
	@Override
	public byte[] getEntity() {
		return entity;
	}

}
