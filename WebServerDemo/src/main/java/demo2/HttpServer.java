package demo2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	
	private static final String directory = "";

	public static void main(String[] args) {
		try(ServerSocket serverSocket = new ServerSocket(8888)) {
			while(true) {
				Socket client = serverSocket.accept();
				new Request(client, directory);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}


class Request extends Thread{
	private Socket client;
	private String dir;
	private BufferedReader in;
	private OutputStream out;
	
	public Request(Socket client, String dir) {
		this.dir = dir;
		this.client = client;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
			out = client.getOutputStream();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		start();
	}
	
	
	public String getHeader(String path) {
		String fileName = "";
		int index = path.lastIndexOf('/');
		fileName = path.substring(index+1);
		String[] array = fileName.split("//.");
		String type = array[array.length-1];
		if(type.equals("html")) {
			return "HTTP/1.0 200 OK\r\n"+"Content-Type:text/html\r\n" + "Server:myserver\r\n";
		}
		else if(type.equals("jpg")||type.equals("gif")||type.equals("png")) {
			return "HTTP/1.0 200 OK\r\n"+"Content-Type:image/jpeg\r\n" + "Server:myserver\r\n";
		}
		
		return null;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				String file = "";
				String firstLine = in.readLine();
				if(firstLine == null || firstLine.length() == 0)
					return;
				file = firstLine.split(" ")[1];
				InputStream dataStream = new FileInputStream(dir+file);
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				byte[] buff = new byte[1024];
				int end = -1;
				while((end = dataStream.read(buff)) != -1) {
					bao.write(buff, 0, end);
				}
				dataStream.close();
				bao.flush();
				String header = getHeader(file);
				out.write(header.getBytes("UTF-8"));
				out.write(bao.toByteArray());
				out.flush();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if(out != null)
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if(in != null)
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if(client != null)
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
}