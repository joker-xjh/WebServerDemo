package utf32Toutf8;

import java.io.IOException;
import java.io.InputStream;

public class UTF32InputStreamDecoder extends UTFInputStreamDecoder{

	
	public UTF32InputStreamDecoder(InputStream inputStream) {
		super(inputStream);
	}
	
	public UTF32InputStreamDecoder(InputStream inputStream, int size) {
		super(inputStream, size);
	}

	@Override
	protected int read() throws IOException {
		int utf32 = 0;
		
		for(int i=0; i<Integer.BYTES; i++) {
			int read = in.read();
			if(i == 0 && read == -1)
				return read;
			if(i > 0 && read == -1)
				throw new IllegalArgumentException("Invalid UTF-32 input stream");
			utf32 |= (read & 0xff) << ((Integer.BYTES-1-i) * Byte.SIZE);
		}
		
		return utf32;
	}

}
