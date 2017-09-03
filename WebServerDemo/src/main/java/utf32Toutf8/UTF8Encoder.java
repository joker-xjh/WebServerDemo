package utf32Toutf8;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class UTF8Encoder {
	
	private static Map<Charset, Class<? extends UTFInputStreamDecoder>> decoders;
	
	static {
		initDecoders();
	}
	
	private static void initDecoders(){
		decoders = new HashMap<>();
		decoders.put(Charset.forName("UTF-32"), UTF32InputStreamDecoder.class);
	}
	
	
	private InputStream in;
	private OutputStream out;
	private boolean isConvertDone;
	private Charset sourceCharset;
	
	public UTF8Encoder(InputStream in, OutputStream out, Charset sourceCharset) throws UnsupportedEncodingException {
		if(!decoders.containsKey(sourceCharset))
			throw new UnsupportedEncodingException();
		this.in = in;
		this.out = out;
		this.sourceCharset = sourceCharset;
	}
	
	
	public boolean isConvertDone() {
		return isConvertDone;
	}
	
	public void convert() {
		if (isConvertDone) {
			throw new IllegalStateException("Convertion is done");
		}
		
		try (BufferedOutputStream outputStream = new BufferedOutputStream(out)){
			
			Class<? extends UTFInputStreamDecoder> clazz = decoders.get(sourceCharset);
			UTFInputStreamDecoder decoder = clazz.getConstructor(InputStream.class).newInstance(in);
			
			int word = -1;
			while( (word = decoder.read()) != -1) {
				byte[] data = encode(word);
				outputStream.write(data);
			}
			
		} catch (IOException e) {
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		isConvertDone = true;
	}
	
	
	private byte[] encode(int word) {
		byte[] encodes = null;
		
		if(word >= 0x0 && word <= 0x7f) {
			encodes = new byte[1];
			int bits = word & 0b01111111;
			encodes[0] = (byte) (bits| 0b0_0000000);
		}
		else if(word >= 0x80 && word <= 0x7ff) {
			encodes = new byte[2];
			word = bit6Slice(encodes, word);
			int bits = word & 0b11111;
			encodes[0] = (byte) (bits | 0b110_00000);
		}
		else if(word >= 0x800 && word <= 0xffff) {
			encodes = new byte[3];
			word = bit6Slice(encodes, word);
			int bits = word & 0b1111;
			encodes[0] = (byte) (bits | 0b1110_0000);
		}
		else if(word >= 0x10000 && word <= 0x1fffff) {
			 encodes = new byte[4];
			 word = bit6Slice(encodes, word);
			 int bits = word & 0b111;
			 encodes[0] = (byte) (bits | 0b11110_000);
		}
		else if(word >= 20000 && word <= 0x3ffffff) {
			encodes = new byte[5];
			word = bit6Slice(encodes, word);
			int bits = word & 0b11;
			encodes[0] = (byte) (bits | 0b11111000);
		}
		else if(word >= 4000000 && word <= 0x7fffffff) {
			encodes = new byte[6];
			word = bit6Slice(encodes, word);
			int bis = word & 0b1;
			encodes[0] = (byte) (bis | 0b1111110_0);
		}
		else {
			 throw new IllegalArgumentException("invalid code point");
		}
		
		
		return encodes;
	}
	
	
	
	
	private int bit6Slice(byte[] encode, int word) {
		int i = encode.length;
		while(--i > 0) {
			int bits = word & 0b111111;
			encode[i] = (byte) (bits | 0b10000000);
			word >>= 6;
		}
		return word;
	}
	

}
