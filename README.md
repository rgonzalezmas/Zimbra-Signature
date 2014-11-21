Zimbra Signature Generator
=======================


 Signature Generator for Zimbra 8. 
 Customized Signature Generation with Active Directory/LDAP data population

----------


Description
-------------

Signature Generator defines a custom email signature for every user on Zimbra Collaboration Server
It uses the Zimbra Command Line Interface for setting an HTML signature populated with information from the coporate Active Directory.

It uses standard ldap queries

> **Steps:**

> - Read the  configuration from settings files
	> config.properties file contains basic LDAP connection settings, the HTML template and the list of users to           create the signature. 
	Sample user definition file
> 
> > OU=Dep1,OU=Area1,OU=Usuaris,DC=domain,DC=local  	OU=Dep2,OU=Area1,OU=Usuaris,DC=domain,DC=local
> 	OU=Dep3,OU=Area2,OU=Usuaris,DC=domain,DC=local
> 
>   Template mapping between Active Directory Fields and    Template File:

    givenname                           <-> $nom
    sn                                  <-> $cognoms
    title                               <-> $carrec
    physicaldeliveryofficename          <-> $departament 
    streetAddress	                    <-> $ubicacio
    pager                               <-> $telefonDepartament

>   - Connect to LDAP server to retreive the attributes value
>   - Map the data using the given template
>   - Set the generated HTML signature sor each user in the OU list
	
	  The key CLI Zimbra command to do this:
		

                Remove the default signature from user: zmprov dsig $email_account $signature_name
    		Create default signature:  zmprov csig $email_account $signature_name zimbraPrefMailSignatureHTML \"$signature\"
    		Setting signature as default for new and reply mails
    		zmprov modifyIdentity $email_account DEFAULT zimbraPrefDefaultSignatureId \"$signature_id\" zimbraPrefForwardReplySignatureId \"$signature_id\"";


> **Execution:**
> Create a jar file from the project
> java -jar signature.jar config_properties_path
> 
> By default uses config/config.properties

**Contact: Ricardo González @rgonzalezmas or rgonzalez@valls.cat**
