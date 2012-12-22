package search;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ForSearch
 */
@WebServlet("/Results")
public class IRServlet extends HttpServlet {

  	private static final long serialVersionUID = 1L;
	Processor processor;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IRServlet() {
		super();
		processor = new Processor();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		String querySentFromHTML = request.getParameter("query"); 
		ArrayList<WebResultData> results = processor.processResults(querySentFromHTML);
		PrintWriter out = response.getWriter();
		StringBuilder html = new StringBuilder();
		
		html = html.append("<html>")
		           .append("<head>")
		           .append("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://fonts.googleapis.com/css?family=Rancho|Inconsolata|Coming+Soon\">")
		           .append("<div id=\"wrapper\">")
		           .append("<h1 id= \"logo\">")
			   .append("Treasure Map")
		           .append("</h1>")
			   .append("</div>")
			   .append("<table>")
		           .append("<tr>")
		           .append("<td>")
			   .append("<div id=\"parent\">")
		           .append("<div id=\"child\">")
		           .append("<link href=\"Results.css\" rel=\"stylesheet\">")
			   .append("<form method= \"POST\" id=\"searchbox\" action=\"http://XXX.XXX.XXX.XXX:XXXX/Engine/Results\">")
			   .append("<input id=\"search\" type=\"text\" placeholder=\"Type here\" name=\"query\">")
		           .append("<input id=\"submit\" type=\"submit\" value=\"Search\">")
			   .append("</form>")
		           .append("</div>")
			   .append("</div>")
			   .append("</td>")
			   .append("</tr>")
		           .append("</table>");
		
		if(!results.isEmpty()) {
			for (WebResultData w : results) {
				String url = w.getUrl();
				String title = w.getTitle();
				StringBuilder snippet = new StringBuilder();
				
				if (title.length() > 60) { 
					title = title.substring(0,60) + " ..."; 
				}
				if(url.length() > 60) { 
					url = url.substring(0, 60) + " ..."; 
				}
				for (int i = 0; i < 25; i++) { 
					snippet = snippet.append(w.getSnippet()[i] + " "); 
				}
				snippet = snippet.append(" ...");
				
				html = html.append("<div id=\"cover\">")
				           .append("<h2>")
					   .append("<a href=\"" + url +"\">" + title + "</a>")
					   .append("</h2>")
					   .append("<h3>")
					   .append(url)
					   .append("</h3>")
					   .append("<h4>")
				           .append(snippet.toString())
					   .append("</h4>")
					   .append("</div>");
			}
		} else {
			html = html.append("<div id=\"cover\">")
			           .append("<h2>")
				   .append("<center>")
				   .append("Sorry, No Results Found.")
				   .append("<center>")
				   .append("</h2>")
				   .append("</div>");
		}
		
		html.append("<div id=\"footer\">");
		html.append("<hr>");
		String home = "http://XXX.XXX.XXX.XXX:XXXX/home.html";
		html.append("<a href=\"" + home +"\">" + "Home" + "</a>");
		html.append("</html>");
		out.write(html.toString());	
	}
}
