package charsetstudy;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteCountingInputStream extends FilterInputStream{
	
	private long byteCount;
	protected ByteCountingInputStream(InputStream in) {
		super(in);
	}
	
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int read = super.read(b, off, len);
		byteCount += read;
		return read;
	}
	
	@Override
	public int read() throws IOException {
		int read = super.read();
		byteCount++;
		
		return read;
	}
	
	public long getByteCount() {
		return byteCount;
	}
	
	

}
