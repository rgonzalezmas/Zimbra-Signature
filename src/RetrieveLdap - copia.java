import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
 
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringEscapeUtils;

 
public class RetrieveLdap {
 

	private static final String ZIMBRA_SET_SIGNATURE = "/opt/zimbra/bin/zmprov csig $email_account $signature_name zimbraPrefMailSignatureHTML \"$signature\"";
	private static final String ZIMBRA_DEL_SIGNATURE = "/opt/zimbra/bin/zmprov dsig $email_account $signature_name";
	private static final String ZIMBRA_GET_SIGNATURES = "/opt/zimbra/bin/zmprov getSignatures $email_account | grep -B 1 \"Default\" | grep \"Id\" | cut -f 2 -d\" \"";
	private static final String ZIMBRA_SET_DEFAULT_SIGNATURE = "/opt/zimbra/bin/zmprov ma $email_account zimbraPrefDefaultSignatureId \"$signature_id\"";
	
    public static void main(String[] args) {
    	try
    	{
    		

    	RetrieveLdap retrieveUserAttributes = new RetrieveLdap();
    	ConfigReader config = new ConfigReader("config/config.properties");
    	config.ReadConfigFile();
    	String user = "rgonzalez";
    	//if (args.length >0) 
    	if (args.length == 2) user = args[1];
    	LdapServer data = config.getLdapInfo();
        User u = retrieveUserAttributes.getUserBasicAttributes(user, retrieveUserAttributes.getLdapContext(data));
        String signature = retrieveUserAttributes.generateHTML(u);
        ShellExecutor bash  = new ShellExecutor();
       
        String output ="";
        String[] commandArray = {"bash", "-c", ZIMBRA_DEL_SIGNATURE};
        
        //Delete Signature
        String command = ZIMBRA_DEL_SIGNATURE;
        command = command.replace("$email_account",u.getEmail());
        command = command.replace("$signature_name","Default");
        System.out.println("Execute Command:"+command);
        
        commandArray[2] = command;
        output = bash.executeCommand(commandArray);
        System.out.println("Output "+output);
        
        //Set New Signazture
        command = ZIMBRA_SET_SIGNATURE;
        command = command.replace("$email_account",u.getEmail());
        command = command.replace("$signature_name","Default");
        command = command.replace("$signature",signature);
        System.out.println("Execute Command:"+command);
        
        commandArray[2] = command;
        output = bash.executeCommand(commandArray);
        System.out.println("Output "+output);
        
        
        //Set DEFAULT
        command = ZIMBRA_SET_DEFAULT_SIGNATURE;
        command = command.replace("$email_account",u.getEmail());
        command = command.replace("$signature_id",output);
        System.out.println("Execute Command:"+command);
        
        commandArray[2] = command;
        output = bash.executeCommand(commandArray);
        System.out.println("Output "+output);
        
    	}
    	catch (Exception ex) { System.out.println("Error "+ ex.toString());}
    }
 
    public LdapContext getLdapContext(LdapServer info){
        LdapContext ctx = null;
        try{
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");

            env.put(Context.SECURITY_AUTHENTICATION, info.getSecurity());
            env.put(Context.SECURITY_PRINCIPAL, info.getUser());
            env.put(Context.SECURITY_CREDENTIALS, info.getPassword());
            env.put(Context.PROVIDER_URL, info.getUrl());
            
            ctx = new InitialLdapContext(env, null);
            System.out.println("Connection Successful to "+ info.getUrl());
        }catch(NamingException nex){
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
        return ctx;
    }
 
    private User getUserBasicAttributes(String username, LdapContext ctx) {
       User user=new User();
       Vector<User> userList = new Vector<User>();
 
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String[] attrIDs = { "distinguishedName",
                    "sn",
                    "givenname",
                    "mail",
                    "telephonenumber",
                    "physicaldeliveryofficename",
                    "pager",
                    "title",
                    "streetAddress",
                    "sAMAccountName"};
            
            constraints.setReturningAttributes(attrIDs);
          
            BufferedReader br = new BufferedReader(new FileReader("config/ou.txt"));
            String line;
            // Query Line By Line for users
            while ((line = br.readLine()) != null) {
            	try {
            	//Get user info.
            	answer = ctx.search(line,"(&(sAMAccountName=*)(!(userAccountControl:1.2.840.113556.1.4.803:=2)))", constraints);
                 while(answer.hasMore()) {
                     Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                     System.out.println(attrs.toString());
                     user = new User();
                     try
                     {
                    	 user.setLogin(attrs.get("sAMAccountName").get().toString());
                    	 user.setNom(attrs.get("givenname").get().toString());
                    	 user.setCognoms(attrs.get("sn").get().toString());
                    	 user.setCarrec(attrs.get("title").get().toString());
                    	 user.setTelefonDepartament(attrs.get("pager").get().toString());
                    	 user.setTelefon(attrs.get("telephonenumber").get().toString());
                    	 user.setDepartament(attrs.get("physicaldeliveryofficename").get().toString());
                    	 user.setUbicacio(attrs.get("streetAddress").get().toString());
                    	 user.setEmail(attrs.get("mail").get().toString());
                    	 userList.add(user);	
                     }catch (Exception ex)
                     {
                    	 System.out.println("Error reading "+user.getLogin());
                     }
                     
                 }
            
            }
            catch (Exception e) {
            	System.out.println("Error. Unable to Query "+line);
            	
            }
            br.close();
           
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       return user;
    }
    
     public String generateHTML(User user) throws IOException
     {
    	 File htmlTemplateFile = new File("config/templateAjuntament.html");
    	 String htmlString = FileUtils.readFileToString(htmlTemplateFile);
    	 
    	 htmlString = htmlString.replace("$nom", user.getNom());
    	 htmlString = htmlString.replace("$cognoms", user.getCognoms());
    	 htmlString = htmlString.replace("$carrec", user.getCarrec());
    	 htmlString = htmlString.replace("$departament", user.getDepartament());
    	 htmlString = htmlString.replace("$ubicacio", user.getUbicacio());
    	 htmlString = htmlString.replace("$telefon", user.getTelefonDepartament());
    	 
    	 File newHtmlFile = new File("config/new.html");
    	 HtmlEncoder enc = new HtmlEncoder();
    	 //FileUtils.writeStringToFile(newHtmlFile, StringEscapeUtils.escapeHtml4(htmlString));
    	 FileUtils.writeStringToFile(newHtmlFile, enc.transform(htmlString));
    	 
    	 return htmlString.replaceAll("[\r\n]+", " ");
     }
}
