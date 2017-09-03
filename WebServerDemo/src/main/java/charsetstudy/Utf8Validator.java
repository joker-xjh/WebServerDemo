package charsetstudy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utf8Validator {
	
	private ValidationHandler handler;
	
	public Utf8Validator(ValidationHandler handler) {
		this.handler = handler;
	}
	
	public void validate(File file) throws IOException, ValidationException{
		try (InputStream in = new BufferedInputStream(new FileInputStream(file))){
			validate(in);
		} finally {
			
		}
	}
	
	
	private void validate(InputStream in) throws IOException, ValidationException{
		ByteCountingInputStream bci = new ByteCountingInputStream(in);		
		int read = -1;
		while( (read = bci.read()) != -1 ) {
			int count = getLeadingOne(read);			
			if(count ==1 || count >6) {
				handler.error("Invalid single byte UTF-8 character ", bci.getByteCount());
			}
			else if(count >=2) {
				checkNextBytes(bci, count-1);
			}
			
		}
		
	}
	
	
	private void checkNextBytes(ByteCountingInputStream bci, int next) throws IOException, ValidationException {
		byte[] array = new byte[next];
		int bytes = bci.read(array);
		if(bytes != next)
			handler.error("Invalid UTF-8 Sequence, expecting: " + (next + 1) + "bytes, but got: " + (bytes + 1) + "bytes - reached end of stream.", -1);
		
		for(int i=0; i<next; i++) {
			byte b = array[i];
			int temp1 = (b >>> 7) & 1;
			int temp2 = (b >>> 6) & 1;
			if(!(temp1 == 1 && temp2 ==0) ) {
				 handler.error("Invalid UTF-8 sequence, byte " + (i+2) + " of " + (next+1) + " byte multibyte sequence.", (bci.getByteCount() - next + i + 1));
			}
		}
		
	}
	
	private int getLeadingOne(int read) {
		int count = 0;
		for(int i=7; i>=0; i++) {
			int bit = ((1<<i) & read);
			if(bit == 1)
				count++;
			else
				break;
		}
		return count;
	}
	

}
