package charsetstudy;

public class ValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6064194673259494685L;
	
	
	public ValidationException(String message, long byteOffset) {
		super(message+" @ byte position:"+byteOffset);
	}
	
	
	

}
