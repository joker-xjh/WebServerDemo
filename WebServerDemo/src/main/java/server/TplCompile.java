package server;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class TplCompile {
	
	public String templatePath = Options.TemplatePath;
	
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
	public Object run(String fileName) {
		TemplateCompile templateCompile = new TemplateCompile();
		int r = templateCompile.start(fileName);
		if(r==1)
			return null;
		String javaFile = templatePath + templateCompile.getFilePath(fileName)+"/"+templateCompile.getfileName(fileName)+ "Template.java";
		String classPath = getClass().getResource("/").getPath();
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		int status = javac.run(null, null, null, "-d",classPath,javaFile);
		if(status != 0) {
			System.out.println("没有编译成功");
			return null;
		}
		
		
		try {
			@SuppressWarnings("rawtypes")
			
			Class clazz = Class.forName("Template."+templateCompile.getFilePath(fileName).replace("/", ".")+templateCompile.getfileName(fileName)+"Template");
			 BaseTemplate object = (BaseTemplate) clazz.newInstance();
			 return object;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
