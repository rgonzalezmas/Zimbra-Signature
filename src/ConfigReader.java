


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class ConfigReader {

		private String fileName;
		private Hashtable<String,String> config;
		private LdapServer ls;
		private String templateFile;
		private String userFile;
		
		
		
		public ConfigReader()
		{
			config = new Hashtable<String,String>();
			ls = new LdapServer();
		}
		
		public ConfigReader(String file)
		{
			config = new Hashtable<String,String>();
			fileName = file;
			ls = new LdapServer();
		}
		
		public boolean ReadConfigFile()
		{
			return ReadConfigFile(fileName);
		}
		
		public boolean ReadConfigFile(String file) {
		
			boolean res = false;
			try
			{
				Properties prop = new Properties();
				InputStream input = null;
			 
			
			 
					input = new FileInputStream(file);
					// load a properties file
					prop.load(input);
					
					ls.setUrl(prop.getProperty("server.url"));
					ls.setUser(prop.getProperty("server.user"));
					ls.setPassword(prop.getProperty("server.password"));
					ls.setSecurity(prop.getProperty("server.security"));
					
					templateFile = prop.getProperty("config.tempate");
					userFile = prop.getProperty("config.users");
				 
				
				res = true;
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				res = false;
			}
			
			return res;
		}
		
		public LdapServer getLdapInfo()
		{
			return this.ls;
		}
		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getTemplateFile() {
			return templateFile;
		}

		public void setTemplateFile(String templateFile) {
			this.templateFile = templateFile;
		}

		public String getUserFile() {
			return userFile;
		}

		public void setUserFile(String userFile) {
			this.userFile = userFile;
		}
		
}
