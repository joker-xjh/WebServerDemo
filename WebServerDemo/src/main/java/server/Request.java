package server;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Request {
	
	private Map<String, String> header;
	private Map<String, String> argument;
	private Map<String, String> cookies;
	private String path;
	private String method;
	private String remoteIP;
	
	public Request(String url) {
		StringTokenizer tokenizer = new StringTokenizer(url, "\r\n");
		header = new HashMap<>();
		argument = new HashMap<>();
		cookies = new HashMap<>();
		String[] firstLine = tokenizer.nextToken().split(" ");
		method = firstLine[0];//请求方式
		String href = firstLine[1];//URL
		String[] hrefSplit = href.split("\\?");
		path = hrefSplit[0];
		if(hrefSplit.length > 1) {
			//this.argument = hrefSplit[1];解析参数
			argument = ResolveQuery.parse(hrefSplit[1]);
		}
		
		while(tokenizer.hasMoreTokens()) {
			
			String temp = tokenizer.nextToken().trim();
			if(temp.length() == 0)
				break;   //空行,说明header可以结束了
			
			String[] split = temp.split(":");
			if(split[0].toLowerCase().equals("cookie")) {
				 //解析Cookie，不记录到Header中
				
				continue;
			}
			
			header.put(split[0], split[1].trim());
		}
		
		//post请求
		while(tokenizer.hasMoreTokens()) {
			String temp = tokenizer.nextToken().trim();
			if(temp.length()==0)
				continue;
			
			this.argument.putAll(ResolveQuery.parse(temp));
		}
		
	}
	
	public Map<String, String> getHeader() {
		return header;
	}
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	public Map<String, String> getArgument() {
		return argument;
	}
	public void setArgument(Map<String, String> argument) {
		this.argument = argument;
	}
	public Map<String, String> getCookies() {
		return cookies;
	}
	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRemoteIP() {
		return remoteIP;
	}
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

}
