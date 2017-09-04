package http;

import java.util.Map;

public interface HttpMessage {
	Map<String, String> getHeaders();
	byte[] getEntity();
	HttpVersion getHttpVersion();
	void setVersion(HttpVersion version);
	void setEntity(byte[] entity);
}
