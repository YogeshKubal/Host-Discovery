import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hostnames {
	public static void main(String[] args) {
		File file_hosts = new File ("/etc/hosts");
		StringBuilder line = new StringBuilder();
		BufferedReader reader_hosts = null;
		String text = null;
		Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
		String text_line;
		char ch;

		try {
			reader_hosts = new BufferedReader (new FileReader(file_hosts));	
			while ((text = reader_hosts.readLine()) !=null) {
				text_line = text.trim();
				text_line = text_line.replaceAll("\\s+", " ");
				if(text_line.startsWith("#")) {
					continue;
				}
				else if(text_line.isEmpty()) {
					continue;
				}
				else {
					String[] hostNames = text_line.split(" "); 
					if(ipPattern.matcher(hostNames[1]).matches()){
						continue;
					}
					else {
						line.append(hostNames[1].toString()).append(System.getProperty ("line.separator"));
					}
					if (text_line.lastIndexOf(" ")>text_line.lastIndexOf(hostNames[1]))
					{
						if(ipPattern.matcher(hostNames[2]).matches()){
							continue;
						}
						else {
							line.append(hostNames[2].toString()).append(System.getProperty ("line.separator"));
						}
					}
				}
			}
		}
		catch (IOException e) {}
		finally {
			try {
				if (reader_hosts !=null){
					reader_hosts.close();
				}
			}
			catch (IOException e) {}
		}

		/**************************************************************************/
		File file_ssh_config = new File("/etc/ssh/ssh_config");
		BufferedReader reader_ssh_config = null;

		try {
			reader_ssh_config = new BufferedReader (new FileReader(file_ssh_config));
			String text_ssh_config = null;

			while ((text_ssh_config = reader_ssh_config.readLine()) !=null) {
				text_line = text_ssh_config.trim();
				text_line = text_line.replaceAll("\\s+", " ");

				if (text_line.startsWith("#")) {
					continue;
				}
				else {
					if(text_line.contains("HostName")) {
						int i = text_line.indexOf("HostName");
						String[] hostNames = text_line.split(" ");
						if(ipPattern.matcher(hostNames[i+1]).matches()) {
							continue;
						}
						else
							line.append(hostNames[i+1]).append(System.getProperty ("line.separator"));
					}
					else if (text_line.contains("Host ")) {
						int i = text_line.indexOf("Host ");
						String[] hostNames = text_line.split(" ");
						//ch = hostNames[i+1].charAt(0);
						if(ipPattern.matcher(hostNames[i+1]).matches()) {
							continue;
						}
						else /*if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9'))*/
							line.append(hostNames[i+1]).append(System.getProperty ("line.separator"));
					}
					else {
						continue;
					}
				}
			}
		}
		catch (IOException e) {}
		finally {
			try {
				if (reader_ssh_config !=null) {
					reader_ssh_config.close();
				}
			}
			catch (IOException e) {}
		}

		/********************************************************************************/
		File file_known_host = new File ("/etc/ssh/ssh_known_hosts");
		BufferedReader reader_known_hosts = null;

		try {
			reader_known_hosts = new BufferedReader (new FileReader(file_known_host));
			String text_known_host = null;

			while ((text_known_host = reader_known_hosts.readLine()) !=null) {
				text_line = text_known_host.trim();
				text_line = text_line.replaceAll("\\s+", " ");
				if(text_line.isEmpty())
				{
					continue;
				}
				else if(text_line.startsWith("#"))
				{
					continue;
				}
				else{					
					String[] host_line = text_line.split(" "); 
					if(host_line[0].contains(","))
					{
						String[] hostName = host_line[0].split(",");
						for(int j=0;j<hostName.length;j++)
						{
							if(ipPattern.matcher(hostName[j]).matches()){
								continue;
							}
							else if(hostName[j].contains(":"))
								line.append((hostName[j].split(":")[0]).toString()).append(System.getProperty ("line.separator"));
							else
								line.append(hostName[j].toString()).append(System.getProperty ("line.separator"));
						}
					}
					else if(host_line[0].contains(":"))
						continue;
					else
					{
						if(ipPattern.matcher(host_line[0]).matches()){
							continue;
						}
						else
							line.append(host_line[0].toString()).append(System.getProperty ("line.separator"));
					}
				}
			}
		}
		catch (IOException e) {
		}
		finally 
		{
			try {
				if (reader_known_hosts !=null){
					reader_known_hosts.close();
				}
			}
			catch (IOException e) {}
		}

		/********************************************************************************/
		File file_passwd = new File("/etc/passwd");
		BufferedReader reader_passwd = null;

		try {
			reader_passwd = new BufferedReader (new FileReader(file_passwd));
			while ((text = reader_passwd.readLine()) != null) {
				text_line = text.trim();
				text_line = text_line.replaceAll("\\s+", " ");
				if((text_line.contains(":/home/"))&&(text_line.contains(":/bin/bash"))) {
					String[] userNames = text_line.split(":");

					File file_config = new File("/home/"+userNames[0]+"/.ssh/config");
					BufferedReader reader_config = null;

					try {
						reader_config = new BufferedReader (new FileReader(file_config));
						String text_config = null;

						while ((text_config = reader_config.readLine()) !=null) {
							text_line = text_config.trim();
							text_line = text_line.replaceAll("\\s+", " ");
							if (text_line.startsWith("#")) {
								continue;
							}
							else {
								if(text_line.contains("HostName")) {
									int i = text_line.indexOf("HostName");
									String[] hostNames = text_line.split(" ");
									if(ipPattern.matcher(hostNames[i+1]).matches()) {
										continue;
									}
									else
										line.append(hostNames[i+1]).append(System.getProperty ("line.separator"));
								}
								else if (text_line.contains("Host ")) {
									int i = text_line.indexOf("Host ");
									String[] hostNames = text_line.split(" ");
									ch = hostNames[i+1].charAt(0);
									if(ipPattern.matcher(hostNames[i+1]).matches()) {
										continue;
									}
									else if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9'))
										line.append(hostNames[i+1]).append(System.getProperty ("line.separator"));
									else
										continue;
								}
								else {
									continue;
								}
							}
						}
					}
					catch (IOException e) {}
					finally {
						try {
							if (reader_config !=null) {
								reader_config.close();
							}
						}
						catch (IOException e) {}
					}

					/********************************************************************************/
					////////////////////////////////////////////////////////////////////////////////////////////////////////////
					/*File file_id_rsa = new File("/home/"+userNames[0]+"/.ssh/authorized_keys");
					BufferedReader reader_id_rsa = null;

					try {
						reader_id_rsa = new BufferedReader (new FileReader(file_id_rsa));
						String text_id_rsa = null;

						while ((text_id_rsa = reader_id_rsa.readLine()) !=null) {
							if (text_id_rsa.isEmpty()) {
								continue;
							}
							else {
								String[] dataFields = text_id_rsa.split(" ");
								String[] hostNames = dataFields[2].split("@");
								if(ipPattern.matcher(hostNames[1]).matches()){
									continue;
								}
								else
									line.append(hostNames[1].toString()).append(System.getProperty ("line.separator"));
							}
						}
					}
					catch (IOException e) {
						//e.printStackTrace();
					}
					finally {
						try {
							if (reader_id_rsa !=null) {
								reader_id_rsa.close();
							}
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}*/

					File file_auth = new File("/home/"+userNames[0]+"/.ssh/authorized_keys");
					BufferedReader reader_auth = null;
					try {
						reader_auth = new BufferedReader (new FileReader(file_auth));
						String text_auth_key = null;
						FileWriter fw=new FileWriter("a.txt");

						while ((text_auth_key = reader_auth.readLine()) !=null) {
							text = text_auth_key.trim();
							text = text.replaceAll("\\s+", " ");

							if(text.contains("ssh-rsa"))
							{
								int i = text.indexOf("ssh-rsa");
								if(i>0) {
									fw.write(text.substring(0, i-1)+"\n"+text.substring(i)+"\n");
								}
								else {
									fw.write(text.substring(0)+"\n"); 
								}
							}
							else
							{
								fw.write(text.substring(0)+"\n");
							}				
						}
						fw.close();

						reader_auth = new BufferedReader (new FileReader("a.txt"));
						text_auth_key = null;
						while ((text_auth_key = reader_auth.readLine()) !=null) {
							text_line = text_auth_key.trim();
							text_line = text_line.replaceAll("\\s+", " ");

							if (text_line.isEmpty()) {
								continue;
							}
							else if(text_line.startsWith("ssh-rsa"))
							{
								String[] dataFields = text_line.split(" ");
								if(dataFields[2].contains("@"))
								{
									String[] hostNames = dataFields[2].split("@");
									if(ipPattern.matcher(hostNames[1]).matches()){
										continue;
									}
									else
										line.append(hostNames[1].toString()).append(System.getProperty ("line.separator"));
								}
								else
								{
									continue;
								}
							}
							else if(text_line.startsWith("from"))
							{
								Pattern p = Pattern.compile("\"([^\"]*)\"");
								Matcher m = p.matcher(text_line);
								while (m.find()) {
									String[] text_from = m.group(1).split(",");
									for(int j=0;j<text_from.length;j++)
									{
										if(ipPattern.matcher(text_from[j]).matches()){
											continue;
										}
										else if(text_from[j].contains(":"))
											line.append((text_from[j].split(":")[0]).toString()).append(System.getProperty ("line.separator"));
										else
											line.append(text_from[j].toString()).append(System.getProperty ("line.separator"));
									}						
								}
							}
							else if(text_line.contains("permitopen"))
							{
								Pattern p = Pattern.compile("\"([^\"]*)\"");
								Matcher m = p.matcher(text_line);
								while (m.find()) {
									String[] text_permitOpen = m.group(1).split(",");
									for(int j=0;j<text_permitOpen.length;j++)
									{
										if(ipPattern.matcher(text_permitOpen[j]).matches()){
											continue;
										}
										else if(text_permitOpen[j].contains(":"))
											line.append((text_permitOpen[j].split(":")[0]).toString()).append(System.getProperty ("line.separator"));
										else
											line.append(text_permitOpen[j].toString()).append(System.getProperty ("line.separator"));
									}
								}
							}
						}
					}
					catch (IOException e) {}
					finally {
						try {
							if (reader_auth !=null) {
								reader_auth.close();
							}
						}
						catch (IOException e) {}
					}
					//////////////////////////////////////////////////////////////////////////	
					File known_host = new File ("/home/"+userNames[0]+"/.ssh/known_hosts");
					BufferedReader known_hosts = null;

					try {
						known_hosts = new BufferedReader (new FileReader(known_host));
						String text_known_host = null;

						while ((text_known_host = known_hosts.readLine()) !=null) {
							text_line = text_known_host.trim();
							text_line = text_line.replaceAll("\\s+", " ");
							if(text_line.isEmpty())
							{
								continue;
							}
							else if(text_line.startsWith("#"))
							{
								continue;
							}
							else{					
								String[] host_line = text_line.split(" "); 
								if(host_line[0].contains(","))
								{
									String[] hostName = host_line[0].split(",");
									for(int j=0;j<hostName.length;j++)
									{
										if(ipPattern.matcher(hostName[j]).matches()){
											continue;
										}
										else if(hostName[j].contains(":"))
											line.append((hostName[j].split(":")[0]).toString()).append(System.getProperty ("line.separator"));
										else
											line.append(hostName[j].toString()).append(System.getProperty ("line.separator"));
									}
								}
								else if(host_line[0].contains(":"))
									continue;
								else
								{
									if(ipPattern.matcher(host_line[0]).matches()){
										continue;
									}
									else
										line.append(host_line[0].toString()).append(System.getProperty ("line.separator"));
								}
							}
						}
					}
					catch (IOException e) {
					}
					finally 
					{
						try {
							if (known_hosts !=null){
								known_hosts.close();
							}
						}
						catch (IOException e) {}
					}
					/////////////////////////////////////////////////////////////////////////
				}
				else {
					continue;
				}
			}
		}
		catch (IOException e) {}
		finally {
			try {
				if (reader_passwd != null) {
					reader_passwd.close();
				}
			}
			catch (IOException e) {}
		}
		System.out.println(line.toString());
	}
}
