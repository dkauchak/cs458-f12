package crawler;

import java.net.*;
import java.io.*;
import java.util.*;

public class SaveURL
{

	public static void saveURL(URL url, Writer writer)
		throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		for (int c = in.read(); c != -1; c = in.read()) {
			writer.write(c);
		}
	}

	public static void saveURL(URL url, OutputStream os)
		throws IOException {
		InputStream is = url.openStream();
		byte[] buf = new byte[1048576];
		int n = is.read(buf);
		while (n != -1) {
			os.write(buf, 0, n);
			n = is.read(buf);
		}
	}

	public static String getURL(URL url)
		throws IOException {
		StringWriter sw = new StringWriter();
		saveURL(url, sw);
		return sw.toString();
	}

	public static void writeURLtoFile(URL url, String filename)
		throws IOException {
		// FileWriter writer = new FileWriter(filename);
		// saveURL(url, writer);
		// writer.close();
		FileOutputStream os = new FileOutputStream(filename);
		saveURL(url, os);
		os.close();
	}

	public static Vector extractLinks(URL url)
		throws IOException {
		return extractLinks(getURL(url));
	}

	public static Map extractLinksWithText(URL url)
		throws IOException {
		return extractLinksWithText(getURL(url));
	}

    public static Vector extractLinks(String rawPage, String page) {
		int index = 0;
		Vector links = new Vector();
		while ((index = page.indexOf("<a ", index)) != -1)
		{
		    if ((index = page.indexOf("href", index)) == -1) break;
		    if ((index = page.indexOf("=", index)) == -1) break;
		    String remaining = rawPage.substring(++index);
		    StringTokenizer st 
				= new StringTokenizer(remaining, "\t\n\r\"'>#");
		    String strLink = st.nextToken();
			if (! links.contains(strLink)) 
				links.add(strLink);
		}
		return links;
    }


	public static Map extractLinksWithText(String rawPage, String page) {
		int index = 0;
		Map links = new HashMap();
		while ((index = page.indexOf("<a ", index)) != -1)
		{
			int tagEnd = page.indexOf(">", index);
		    if ((index = page.indexOf("href", index)) == -1) break;
		    if ((index = page.indexOf("=", index)) == -1) break;
			int endTag = page.indexOf("</a", index);
		    String remaining = rawPage.substring(++index);
		    StringTokenizer st 
				= new StringTokenizer(remaining, "\t\n\r\"'>#");
		    String strLink = st.nextToken();
			String strText = "";
			if (tagEnd != -1 && tagEnd + 1 <= endTag) {
				strText = rawPage.substring(tagEnd + 1, endTag);
			}
			strText = strText.replaceAll("\\s+", " ");
			links.put(strLink, strText);
		}
		return links;
		
	}
    
	public static Vector extractLinks(String rawPage) {
        return extractLinks(rawPage, rawPage.toLowerCase().replaceAll("\\s", " "));
	}

	public static Map extractLinksWithText(String rawPage) {
        return extractLinksWithText(rawPage, rawPage.toLowerCase().replaceAll("\\s", " "));
	}

	public static void main(String[] args) {
		try {
			if (args.length == 1) {
                URL url = new URL(args[0]);
                System.out.println("Content-Type: " +
                    url.openConnection().getContentType());
// 				Vector links = extractLinks(url);
// 				for (int n = 0; n < links.size(); n++) {
// 					System.out.println((String) links.elementAt(n));
// 				}
				Set links = extractLinksWithText(url).entrySet();
				Iterator it = links.iterator();
				while (it.hasNext()) {
					Map.Entry en = (Map.Entry) it.next();
					String strLink = (String) en.getKey();
					String strText = (String) en.getValue();
					System.out.println(strLink + " \"" + strText + "\" ");
				}
				return;
			} else if (args.length == 2) {
				writeURLtoFile(new URL(args[0]), args[1]);
				return;
			}
		} catch (Exception e) {
			System.err.println("An error occured: ");
			e.printStackTrace();
// 			System.err.println(e.toString());
		}
		System.err.println("Usage: java SaveURL <url> [<file>]");
		System.err.println("Saves a URL to a file.");
		System.err.println("If no file is given, extracts hyperlinks on url to console.");
	}
}
