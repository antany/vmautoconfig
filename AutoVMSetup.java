
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class AutoVMSetup {

	public static void main(String[] args) throws Exception {
		URL url = new URL("https://raw.githubusercontent.com/antany/vmautoconfig/master/hostmap");
		URLConnection connection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		HashMap<String, HostMap> map = new HashMap<>();
		while ((inputLine = in.readLine()) != null) {
			String[] data = inputLine.split(" ");
			HostMap hm = new HostMap(data[0], data[2], data[1]);
			map.put(data[0], hm);
		}
		in.close();

		System.out.println(args[1]);

		switch (args[0]) {
		case "all":
			updateHostName(args[1]);
			updateHostsFile(map);
			updateDHCPFile(map.get(args[1]));
			break;

		}

	}

	private static void updateDHCPFile(HostMap hm) throws Exception {
		String filename = "/etc/dhcpcd.conf";
		String content = readContentFromFile(filename);
		boolean first = true;
		boolean s3print = false;
		boolean s8print = false;

		String[] contents = content.split("(\\n)|(\\r\\n)");
		

		for (int i = 0; i < contents.length; i++) {
			System.out.println(i);

			String contentLine = contents[i];
			
			if(contentLine.trim().equals("interface enp0s3")) {
				contentLine = "interface enp0s3\nstatic ip_address="+hm.getNatIp()+"/24\nstatic routers=10.0.2.1\nstatic domain_name_server=192.168.0.1\n";
				i=i+3;
				s3print = true;
			}
			
			if(contentLine.trim().equals("interface enp0s8")) {
				contentLine = "interface enp0s8\nstatic ip_address="+hm.getHostonlyIp()+"/24\n";
				i=i+1;
				s8print = true;
			}

			if (first) {
				writeContent(filename, contentLine, false);
				first = false;
			} else {
				writeContent(filename, contentLine, true);
			}
		}
		
		if(!s3print) {
			String contentLine = "interface enp0s3\nstatic ip_address="+hm.getNatIp()+"/24\nstatic routers=10.0.2.1\nstatic domain_name_server=192.168.0.1\n";
			writeContent(filename, contentLine, true);
		}
		if(!s8print) {
			String contentLine = "interface enp0s8\nstatic ip_address="+hm.getHostonlyIp()+"/24\n";
			writeContent(filename, contentLine, true);
		}

	}

	private static void updateHostName(String hostname) throws Exception {
		System.out.println("Updating hostname " + hostname);
		writeContent("/etc/hostname", hostname, false);
	}

	private static void updateHostsFile(HashMap<String, HostMap> map) throws Exception {

		String filename = "/etc/hosts";
		boolean first = true;

		String content = readContentFromFile(filename);

		for (HostMap hm : map.values()) {
			if (first) {
				writeContent(filename, hm.getHostonlyIp() + " " + hm.getHostname(), false);
				first = false;
			} else {
				writeContent(filename, hm.getHostonlyIp() + " " + hm.getHostname(), true);
			}
		}

		e: for (String contentLine : content.split("(\\n)|(\\r\\n)")) {
			for (HostMap hm : map.values()) {
				if (contentLine.contains(hm.getHostname())) {
					continue e;
				}
			}
			writeContent(filename, contentLine, true);
		}
	}

	public static void writeContent(String fileName, String src, boolean appendInd) throws Exception {
		PrintWriter pw = new PrintWriter(new FileWriter((new File(fileName)), appendInd));
		pw.println(src);
		pw.flush();
		pw.close();
	}

	public static String readContentFromFile(String fileName) throws Exception {
		File srcFile = new File(fileName);
		char[] charContent = new char[(int) srcFile.length()];
		BufferedReader bufferedReader = new BufferedReader(new FileReader(srcFile));
		bufferedReader.read(charContent);
		bufferedReader.close();
		return String.copyValueOf(charContent);
	}

}

class HostMap {
	private String hostname;
	private String natIp;
	private String hostonlyIp;

	protected String getHostname() {
		return hostname;
	}

	protected void setHostname(String hostname) {
		this.hostname = hostname;
	}

	protected String getNatIp() {
		return natIp;
	}

	protected void setNatIp(String natIp) {
		this.natIp = natIp;
	}

	protected String getHostonlyIp() {
		return hostonlyIp;
	}

	protected void setHostonlyIp(String hostonlyIp) {
		this.hostonlyIp = hostonlyIp;
	}

	public HostMap(String hostname, String natIp, String hostonlyIp) {
		super();
		this.hostname = hostname;
		this.natIp = natIp;
		this.hostonlyIp = hostonlyIp;
	}

}
