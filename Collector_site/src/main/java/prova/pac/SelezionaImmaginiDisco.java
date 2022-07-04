
package prova.pac;

import collector_site.framework.result.HTMLResult;
import collector_site.framework.utils.ServletHelpers;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Giuseppe Della Penna
 */
@MultipartConfig
public class SelezionaImmaginiDisco extends HttpServlet {

    private static final String GET_IMMAGINI = "SELECT ID,nomeImmagine,dimensioneImmagine,digest,updated,date_format(updated,'%d/%m/%Y %H:%i:%s') AS formatted_updated FROM immagine;";

    @Resource(name = "jdbc/collector_site")
    private DataSource ds;

    private String humanReadableFileSize(long size) {
        final String[] units = new String[]{"bytes", "KB", "MB", "GB", "TB", "PB", "EB"};
        if (size <= 1) {
            return size + " byte";
        }
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Carica Immagine");

        try (Connection c = ds.getConnection();
                //Il tipo TYPE_SCROLL_INSENSITIVE (o SENSITIVE) è necessario per poter usare i metodi last() e getRow()
                //The TYPE_SCROLL_INSENSITIVE  (or SENSITIVE) is required to use the last() and getRow() methods
                PreparedStatement s2 = c.prepareStatement(GET_IMMAGINI, ResultSet.TYPE_SCROLL_INSENSITIVE)) {

            try (ResultSet r = s2.executeQuery()) {
                result.appendToBody("<h2>Selezionare l'immagine da caricare!</h2>");

                //un modo per conoscere il numero di record prelevati da una query
                // NON CI SERVE
                /*int count = 0;
                if (r.last()) { //move to the last record if present
                    count = r.getRow(); //get the row number
                    r.beforeFirst(); //move to the initial position
                }
                result.appendToBody("<p>This repository holds " + count + " items.</p>");
                */
                result.appendToBody("<table border=\"1\">");
                result.appendToBody("<thead><th>ID</th><th>Nome</th><th>Size</th><th>Modified</th><th>Digest</th></thead>");
                while (r.next()) {
                    //un modo contorto per leggere il timestamp con la corretta timezone
                    ZonedDateTime updated = r.getTimestamp("updated").toInstant().atZone(ZoneId.of("GMT"));
                    result.appendToBody("<tr><td>" + r.getInt("ID")
                            + "</td><td>" + HTMLResult.sanitizeHTMLOutput(r.getString("nomeImmagine"))
                            + "</td><td>" + humanReadableFileSize(r.getInt("dimensioneImmagine"))
                            /*
                            //è possibile prendere il timestamp dal DB e formattarlo localmente (ma attenzione alla time zone!)                            
                            + "</td><td>" + updated.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                            */
                            //...o farlo formattare dal DB tramite l'SQL!
                            + "</td><td>" + r.getString("formatted_updated")
                            + "</td><td>" + r.getString("digest")
                            + "</td></tr>");
                }
                result.appendToBody("</table>");

            }
        }
//
        
        
//
        result.appendToBody("<h2>Carica nuove Immagini</h2>");
        result.appendToBody("<form method='post' action='upload' enctype='multipart/form-data'>");
        result.appendToBody("<p>Seleziona l'immagine da caricare <input type='file' name='filetoupload'/>");
        result.appendToBody("<input type='submit' name='submit' value='upload'/></p>");
        result.appendToBody("</form>");

        result.activate(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            action_default(request, response);
        } catch (Exception ex) {
            ServletHelpers.handleError(ex, request, response, getServletContext());
        }

    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
