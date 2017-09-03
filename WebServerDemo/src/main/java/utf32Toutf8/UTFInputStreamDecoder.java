package utf32Toutf8;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public abstract class UTFInputStreamDecoder implements Closeable{

	protected BufferedInputStream in;
	
	public UTFInputStreamDecoder(InputStream inputStream, int size) {
		in = new BufferedInputStream(inputStream, size);
	}
	
	public UTFInputStreamDecoder(InputStream inputStream) {
		this(inputStream, 8192);
	}
	
	protected abstract int read() throws IOException;
	
	
	@Override
	public void close() throws IOException {
		if(in != null)
			in.close();
	}

}
