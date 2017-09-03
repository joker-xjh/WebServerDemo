package utf32Toutf8;

import java.io.IOException;
import java.io.InputStream;

public class UTF8InputStreamDecoder extends UTFInputStreamDecoder{
	
	private int six = 0b1111110_0;
	private int five = 0b111110_00;
	private int four = 0b11110_000;
	private int three = 0b1110_0000;
	private int two = 0b110_00000;
	private int one = 0b10_000000;

	public UTF8InputStreamDecoder(InputStream inputStream) {
		super(inputStream);
	}
	
	public UTF8InputStreamDecoder(InputStream inputStream, int size) {
		super(inputStream, size);
	}

	@Override
	protected int read() throws IOException {
		int word = 0;
		word = in.read();
		if(word == -1)
			return word;
		if((word & six )==six) {
			word = word & 1;
			word = fetch(word, 5);
		}
		else if((word & five )== five) {
			word = word & 0b00000011;
			word = fetch(word, 4);
		}
		else if((word & four )== four) {
			word = word & 0b00000111;
			word = fetch(word, 3);
		}
		else if((word & three )== three) {
			word = word & 0b00001111;
			word = fetch(word, 2);
		}
		else if((word & two )== two) {
			word = word & 0b00011111;
			word = fetch(word, 1);
		}
		else if((word & one )==0) {
			
		}
		else {
			throw new IllegalArgumentException("Invalid UTF-8 input stream");
		}
		
		return word;
	}
	
	
	private int fetch(int word, int time) throws IOException {
		while(time > 0) {
			word <<= 6;
			int temp = in.read();
			if(temp == -1)
				throw new IllegalArgumentException("Invalid UTF-8 input stream");
			word |= temp & 0b00111111;
			time--;
		}
		return word;
	}
	

}
