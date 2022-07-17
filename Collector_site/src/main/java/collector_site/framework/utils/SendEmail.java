/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collector_site.framework.utils;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    

    //metodo per mandare l'email
    public static void sendEmail(String recepient) throws MessagingException {
        boolean test = false;

        //qui va inserita la nostra email (quella sulla quale vogliamo ricevere il messaggio)
        final String fromEmail = "37d5cb32ad4102";
        //qui va inserita la password dell'account sul quale vogliamo ricevere il messaggio
        final String password = "85b7b2da16278d";


            // dettagli del server stmp di gmail(dando per scontato di usare gmail,altrimenti basta cambiare i setting)
            Properties pr = new Properties();
            pr.setProperty("mail.smtp.host", "smtp.mailtrap.io");
            pr.setProperty("mail.smtp.port", "2525");
            pr.setProperty("mail.smtp.auth", "true");
            
 
            
            //metodo standard di autenticazione,a noi è da rivedere probabilmente
            Session session = Session.getInstance(pr, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });
            
            Message message = prepareMessage(session, fromEmail, recepient);
            Transport.send(message);
/*
            //parte la sessione
            Message mess = new MimeMessage(session);

    		//indirizzo email dal quale parte la mail
            mess.setFrom(new InternetAddress(fromEmail));
    		//indirizzo email al quale arriva la mail
            mess.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
    		
    		//setta l'oggetto della mail
            mess.setSubject("E-mail di prova");
            
    		//setta il messaggio della mail
            mess.setText("collezione condivisa ");
            //invio della mail
            Transport.send(mess);
            //settiamo true se tutto è andato a buon fine,altrimenti finiamo nel catch e la mail non viene inoltrata perchè test=false
            test=true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }

     return test;
    }*/
}
    private static Message prepareMessage(Session session,String fromEmail, String recepient){
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("E-mail di prova");
            message.setText("collezione condivisa ");
            return message;
        }catch(Exception ex){
            Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE,null,ex);
        }
        return null;
    }
}