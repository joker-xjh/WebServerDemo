package charsetstudy;

import java.io.File;
import java.io.IOException;

public class Utf8ValidateCmd {
	
	private static final String VERSION = "1.2";

	
	public static void main(String[] args) {
		if(args.length < 1) {
			 System.out.println("UTF8 Validator version: " + VERSION);
			 System.out.println("Useage: utf8validate [options] <file>");
	         System.out.println("");
	         System.out.println("\t-f | --fail-fast");
	         System.out.println("\t\tStops on the first validation error rather than reporting all errors");
	         System.out.println("");
	         System.exit(ExitCode.INVALID_ARGS.getCode());
		}
		
		
		final boolean failFast;
		File file;
		if(args[0].equals("-f") || args[0].equals("--fail-fast")) {
			failFast = true;
			file = new File(args[1]);
		}
		else {
			failFast = false;
			file = new File(args[0]);
		}
		
		if(!file.exists()) {
			System.out.println("File: " + file.getPath() + " does not exist!");
			System.exit(ExitCode.INVALID_ARGS.getCode());
		}
		
		final ValidationHandler handler = new PrintingValidationHandler(failFast, System.out);
		 ExitCode result = ExitCode.OK;
		 final long start = System.currentTimeMillis();
		 System.out.println("Validating: " + file.getPath());
		try {
			new Utf8Validator(handler).validate(file);
			if(!failFast && handler.isError()) {
				result = ExitCode.VALIDATION_ERROR;
			}
			else {
				System.out.println("Valid OK (took " + (System.currentTimeMillis() - start) + "ms)");
                result = ExitCode.OK;
			}
		} catch (IOException | ValidationException e) {
			result = ExitCode.VALIDATION_ERROR;
			System.err.println("[ERROR]" + e.getMessage());
		}
		
		
		 System.exit(result.getCode());
		

	}

}
