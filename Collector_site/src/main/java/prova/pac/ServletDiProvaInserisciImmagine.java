/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prova.pac;

import collector_site.data.DAO.Collector_siteDataLayer;
import collector_site.data.impl.ImmagineImpl;
import collector_site.data.model.Collezione;
import collector_site.data.model.Collezionista;
import collector_site.data.model.Disco;
import collector_site.data.model.Immagine;
import collector_site.framework.data.DataException;
import collector_site.framework.result.ProvaConfig;
import collector_site.framework.utils.ServletHelpers;
import freemarker.core.ParseException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author mauri
 */
@MultipartConfig
public class ServletDiProvaInserisciImmagine extends ServletDiProvaCollector_siteBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            ProvaConfig pcg= new ProvaConfig(getServletContext());
            HttpSession s = request.getSession(false);
            Template t = pcg.getTemplate("dispatcherDiProva.ftl.html");
            Map<String,Object> dataM = new HashMap();
            dataM.put("numero", 7);
            
            // per il completamento del side menu
            Collezionista collezionista =((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO().getCollezionistaById((int)s.getAttribute("id"));
            // TO CHECK
            ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezionistaDAO();
            List<Collezione> collezioni = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getCollezioneDAO().getCollezioneByCollezionista(collezionista);
            dataM.put("collezioni",collezioni);
            
            // CARICAMENTO DELLE IMMAGINI NELLA PAGINA WEB
            Disco disco =((Collector_siteDataLayer) request.getAttribute("datalayer")).getDiscoDAO().getDisco((int) s.getAttribute("discoID"));  
            List<Immagine> immagini = ((Collector_siteDataLayer) request.getAttribute("datalayer")).getImmagineDAO().getImmaginiByDisco(disco);
            
            if(immagini.isEmpty()){
                immagini=null;
            }
            
            dataM.put("immagini",immagini);
            t.process(dataM, response.getWriter());
            
            // UPLOAD DELL'IMMAGINE
            // controllo sul file che il client intende caricare nel server non sia nullo
            try {
            if (request.getPart("filetoupload") != null) {
                action_upload(request, response, disco);
            } else {
                ServletHelpers.handleError("Nessuna immagine da caricare!", request, response, getServletContext());
            }
            
        } catch (NamingException | SQLException | IOException | NoSuchAlgorithmException ex) {
            ServletHelpers.handleError(ex, request, response, getServletContext());
        }
        } catch (ParseException ex) {
            Logger.getLogger(ServletDiProvaInserisciImmagine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServletDiProvaInserisciImmagine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TemplateException ex) {
            Logger.getLogger(ServletDiProvaInserisciImmagine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataException ex) {
            Logger.getLogger(ServletDiProvaInserisciImmagine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      private void action_upload(HttpServletRequest request, HttpServletResponse response, Disco disco) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, ServletException, DataException {

        Part file_to_upload = request.getPart("filetoupload");

        // controlli sul tipo di file_to_upload
        if (!file_to_upload.getContentType().equals("image/jpeg") && 
            !file_to_upload.getContentType().equals("image/png")) {
            // caso in cui file_to_upload non è un immagine oppure lo è ma ha un formato non consenstito
            // dalla webapp
            return;
        }
        
        // controllo sulle dimensioni di file_to_upload
        if (file_to_upload.getSize() > 10240000) {
            // caso in cui file_to_upload supera le dimensioni massime consentite (10MB) 
            return;
        }
        
        //vogliamo creare il digest sha-1 del file
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        //creiamo un nuovo file con nome univoco 
        File uploaded_file = File.createTempFile("img_", "", new File(getServletContext().getInitParameter("uploads.directory")));
        // copiamo nel file "uploaded_file" il contenuto del file "file_to_upload"
        try (InputStream is = file_to_upload.getInputStream();
            OutputStream os = new FileOutputStream(uploaded_file)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                //durante la copia, aggreghiamo i byte del file nel digest sha-1
                md.update(buffer, 0, read);
                os.write(buffer, 0, read);
            }
        }

        //adesso inseriamo tutte le informazioni sul file nel database
        Immagine img = new ImmagineImpl();
        img.setNomeImmagine(file_to_upload.getSubmittedFileName());
        img.setDimensioneImmagine((long) file_to_upload.getSize());
        img.setFilename(uploaded_file.getName());
        img.setImgType(file_to_upload.getContentType());
        img.setDiscoImg(disco);
        //convertiamo il digest in una stringa
        String digest = bytesToHexString(md.digest());
        img.setDigest(digest);
        // il timestamp viene settato direttamente dal DB
        ((Collector_siteDataLayer) request.getAttribute("datalayer")).getImmagineDAO().storeImmagine(img);
    }

    private String humanReadableFileSize(long size) {
        final String[] units = new String[]{"bytes", "KB", "MB", "GB", "TB", "PB", "EB"};
        if (size <= 1) {
            return size + " byte";
        }
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    
    private String bytesToHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            char[] hexDigits = new char[2];
            hexDigits[0] = Character.forDigit((byteArray[i] >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((byteArray[i] & 0xF), 16);          
            hexStringBuffer.append(new String(hexDigits));
        }
        return hexStringBuffer.toString();
    }
}