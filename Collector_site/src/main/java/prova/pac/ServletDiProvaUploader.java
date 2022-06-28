/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prova.pac;

import collector_site.framework.result.HTMLResult;
import static collector_site.framework.utils.ServletHelpers.handleError;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author mauri
 */
public class ServletDiProvaUploader extends HttpServlet {
    
    private static final String ALL_FILES_QUERY = "";
    
    @Resource (name = "jdbc/collector_site")
    private DataSource ds;
    
    private String humanReadableFileSize(long size){
        final String[] units = new String []{"bytes","KB","MB","GB","TB","PB","EB"};
        if (size <= 1){
            return size + " byte";
        }
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    private void action_default (HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        
         HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("File Repository");

        result.appendToBody("<h1>File Repository</h1>");
        try (Connection c = ds.getConnection();
                PreparedStatement s2 = c.prepareStatement(ALL_FILES_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE)) {
            try (ResultSet r = s2.executeQuery()) {
                result.appendToBody("<h2>Current repository contents</h2>");
                int count = 0;
                if (r.last()) { 
                    count = r.getRow(); 
                    r.beforeFirst(); 
                }
                result.appendToBody("<p>This repository holds " + count + " items.</p>");

                result.appendToBody("<table border=\"1\">");
                result.appendToBody("<thead><th>ID</th><th>Name</th><th>Size</th><th>Modified</th><th>Digest</th></thead>");
                while (r.next()) {
                    ZonedDateTime updated = r.getTimestamp("updated").toInstant().atZone(ZoneId.of("GMT"));
                    result.appendToBody("<tr><td>" + r.getInt("ID")
                            + "</td><td>" + HTMLResult.sanitizeHTMLOutput(r.getString("name"))
                            + "</td><td>" + humanReadableFileSize(r.getInt("size"))
                            //è possibile prendere il timestamp dal DB e formattarlo localmente (ma attenzione alla time zone!)                            
                            //you can get the timestamp from the DB and format it locally (but be aware of the time zone!)
                            + "</td><td>" + updated.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                            //...o farlo formattare dal DB tramite l'SQL!
                            //...or have it formatted by the DB using appropriate SQL!
                            //+ "</td><td>" + r.getString("formatted_updated")
                            + "</td><td>" + r.getString("digest")
                            + "</td></tr>");
                }
                result.appendToBody("</table>");
                }
        }
        
        result.appendToBody("<form method='get' action='download' >");
        result.appendToBody("<p>Write the file ID to download <input type='text' name='res'/>");
        result.appendToBody("<input type='submit' name='submit' value='download'/></p>");
        result.appendToBody("</form>");

        result.appendToBody("<h2>Upload new content</h2>");
        result.appendToBody("<form method='post' action='upload' enctype='multipart/form-data'>");
        result.appendToBody("<p>Select the file to upload <input type='file' name='filetoupload'/>");
        result.appendToBody("<input type='submit' name='submit' value='upload'/></p>");
        result.appendToBody("</form>");

        result.activate(request, response);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            action_default(request, response);
        } catch (Exception ex) {
            handleError(ex, request, response, getServletContext());
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Servlet per l'upload delle immagini all'interno di Collector_site";
    }
 }

