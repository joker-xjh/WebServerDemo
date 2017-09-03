package charsetstudy;

public interface ValidationHandler {
	
	void error(String message,long byteOffset) throws ValidationException;
	boolean isError();
	
}
