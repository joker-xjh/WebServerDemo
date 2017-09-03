package charsetstudy;

public enum ExitCode {

	OK(0),
    INVALID_ARGS(1),
    VALIDATION_ERROR(2),
    IO_ERROR(4);
	
    private final int code;

    ExitCode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
        
    }
	
}
