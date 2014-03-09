/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storybook.export;

import com.itextpdf.text.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import storybook.StorybookApp;
import storybook.toolkit.I18N;

/**
 *
 * @author favdb
 */
public class ExportHtml {

	String report;
	String fileName = "";
	List<ExportHeader> headers;
	Font fontHeader, fontBody;
	BufferedWriter outStream;
	String author;
	private final Export parent;
	private ParamExport param;

	ExportHtml(Export parent, String report, String fileName, List<ExportHeader> headers, String author) {
		this.parent = parent;
		this.report = report;
		this.fileName = fileName;
		this.headers = headers;
		this.author = author;
		this.param=parent.parent.paramExport;
	}

	public void open() {
		try {
			try {
				outStream = new BufferedWriter(new FileWriter(fileName));
				String str = "<html>" + getHtmlHead();
				if (headers != null)
					str += "<body>"+"<h1>"+parent.bookTitle+" - "+parent.exportData.getKey()+"</h1>"+
							"<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\">";
				else
					str += "<body><h1>"+parent.bookTitle+" - "+parent.exportData.getKey()+"</h1>";
				outStream.write(str, 0, str.length());
				outStream.flush();
			} catch (IOException ex) {
				StorybookApp.error("ExportHtml.open()", ex);
			}
			if (headers != null) {
				String str = "<tr>\n";
				for (ExportHeader header : headers) {
					str += parent.getColon(header.getName(), header.getSize());
				}
				str += "</tr>\n";
				outStream.write(str, 0, str.length());
				outStream.flush();
			}
		} catch (IOException ex) {
			StorybookApp.error("ExportHtml.open()", ex);
		}
	}

	public void writeRow(String[] body) {
		try {
			String str = "<tr>\n";
			int index = 0;
			for (String s : body) {
				str += "    <td width=\"" + headers.get(index).getSize() + "%\">";
				str += ("".equals(s) ? "&nbsp" : s);
				str += "</td>\n";
				index++;
			}
			str += "</tr>\n";
			outStream.write(str, 0, str.length());
			outStream.flush();
		} catch (IOException ex) {
			StorybookApp.error("ExportHtml.writeRow()", ex);
		}
	}

	public String getHtmlHead() {
		String buf = "<head>";
		String rep=parent.exportData.getKey();
		buf+="<title>oStorybook : "+parent.bookTitle+" - "+rep+"</title>";
		buf += "<style type='text/css'>";
		buf += "<!--\n";
		if (param.htmlUseCss)
			try {
				InputStream ips = new FileInputStream(param.htmlCssFile);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String ligne;
				while ((ligne = br.readLine()) != null) {
					buf += ligne + "\n";
				}
				br.close();
			} catch (IOException e) {

			}
		else {
			// body
			buf += "body {"
					+ "font-family:Arial,sans-serif;"
					//+ "font-size:" + parent.zoom + "px;"
					+ "padding-left:2px;"
					+ "padding-right:2px;"
					+ "}\n";
			//h1
			buf += "h1 {"
					+ "font-family:Arial,sans-serif;"
					+ "font-size:140%;"
					+ "text-align:center;"
					+ "margin-top:15px;"
					+ "margin-bottom:15px;"
					+ "}\n";
			//h2
			buf += "h2 {"
					+ "font-family:Arial,sans-serif;"
					+ "font-size:120%;"
					+ "margin-top:15px;"
					+ "}\n";
			//p
			buf += "p {"
					+ "margin-top:2px;"
					+ "div {"
					+ "padding-left:5px;"
					+ "padding-right:5px;"
					+ "}\n";
			//ul
			buf += "ul {"
					+ "margin-top:2px;"
					+ "margin-left:15px;"
					+ "margin-bottom:2px;"
					+ "}\n";
			// ordered list
			buf += "ol {"
					+ "margin-top:2px;"
					+ "margin-left:15px;"
					+ "margin-bottom:2px;"
					+ "}\n";
			// table
			buf += "table tr {"
					+ "margin:0px;"
					+ "padding:0px;"
					+ "}\n"
					+ "td {"
					+ "margin-right:1px;"
					+ "padding:1px;"
					+ "}\n";
		}
		buf += "-->";
		buf += "</style>";
		buf += "</head>\n";
		return (buf);
	}

	public void writeText(String str) {
		try {
			String s = "<p>" + str + "</p>";
			outStream.write(s, 0, s.length());
			outStream.flush();
		} catch (IOException ex) {
			StorybookApp.error("ExportHtml.writeText(" + str + ")", ex);
		}
	}

	public void close() {
		try {
			String str = "";
			if (headers != null)
				str += "</table></body></html>";
			else
				str += "</body></html>";;
			outStream.write(str, 0, str.length());
			outStream.flush();
			outStream.close();
		} catch (IOException ex) {
			StorybookApp.error("ExportHtml.close()", ex);
		}
	}

}