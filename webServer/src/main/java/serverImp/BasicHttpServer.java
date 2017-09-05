package serverImp;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.HttpServer;

public class BasicHttpServer implements HttpServer{
	
	private static final String SERVER_NAME = "god";
	private static final String SERVER_VERSION = "1.0";
	private static final String SERVER_SIGNATURE = SERVER_NAME +"/"+ SERVER_VERSION;
	private static final int PORT = 9090;
	private static final int WORK_POOL_SIZE = 10;
	private static final String ROOT_PATH = "";
	private static final File root = new File(ROOT_PATH);
	
	private static ServerSocket server;
	private static volatile boolean running = true;
	
	private static ExecutorService workPool;
	private static ExecutorService servicePool;
	
	public BasicHttpServer(int port) throws IOException {
		workPool = Executors.newFixedThreadPool(WORK_POOL_SIZE);
		servicePool = Executors.newSingleThreadExecutor();
		server = new ServerSocket(port);
	}
	
	public BasicHttpServer() throws IOException {
		this(PORT);
	}

	public static void main(String[] args) {

	}

	@Override
	public void start() {
		servicePool.submit(new Runnable() {
			
			@Override
			public void run() {
				while(running) {
					try {
						Socket client = server.accept();
						dispatchRequest(client);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		System.err.println("Webserver started on port " + server.getLocalPort() + "...");
	}

	@Override
	public void stop() {
		try {
			running = false;
			server.close();
			workPool.shutdown();
			servicePool.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispatchRequest(Socket client) {
		workPool.submit(new BasicHttpWorker(client, this));
	}

	@Override
	public String getServerSignature() {
		return SERVER_SIGNATURE;
	}

	@Override
	public String getRootPath() {
		return ROOT_PATH;
	}

	@Override
	public String getRootDirectoryPath() {
		try {
			return root.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
