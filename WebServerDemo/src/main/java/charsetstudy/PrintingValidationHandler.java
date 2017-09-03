package charsetstudy;

import java.io.PrintStream;

public class PrintingValidationHandler implements ValidationHandler{
	
	private boolean failFast;
	private PrintStream out;
	
	
	private boolean error;
	
	public PrintingValidationHandler(boolean failFast, PrintStream out) {
		this.failFast = failFast;
		this.out = out;
	}

	@Override
	public void error(String message, long byteOffset) throws ValidationException {
		error = true;
		if(failFast)
			throw new ValidationException(message, byteOffset);
		else
			out.println("[ERROR] "+message+" @ byte position:"+byteOffset);
	}
	
     
	@Override
	public boolean isError() {
		return error;
	}

}
