package com.ggukgguk.api;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ggukgguk.api.common.vo.BasicResp;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private Logger log = LogManager.getLogger("base");
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		log.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@GetMapping(value = "/mailTest", produces = "application/json; charset=UTF-8")
	public ResponseEntity<?> testMail() throws Exception {
		final String SMTP_USERNAME = "";
		final String SMTP_PW = "";
		final String CONFIGSET = "BasicConfigSet";
		final String HOST = "email-smtp.ap-southeast-2.amazonaws.com";
		final int PORT = 587;
		
		String from = "ggukgguk@ggukgguk.online";
		String fromName = "꾹꾹";
		String to = "limorbear@gmail.com";
		
		String subject = "꾹꾹 메일 전송 테스트";
		String body = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>Amazon SES SMTP Email Test</h1>",
    	    "<p>This email was sent with Amazon SES using the ", 
    	    "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
    	    " for <a href='https://www.java.com'>Java</a>."
    	);
		
		Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtp");
    	props.put("mail.smtp.port", PORT); 
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.auth", "true");
    	
    	Session session = Session.getDefaultInstance(props);
    	
    	MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from,fromName));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(body,"text/html");
        
        msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
        
        Transport transport = session.getTransport();
		
        try
        {
            System.out.println("Sending...");
            
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PW);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            log.debug("Email sent!");
            
            return ResponseEntity.ok(new BasicResp<Object>("success", null, null));
        }
        catch (Exception ex) {
        	log.debug("The email was not sent.");
        	log.debug("Error message: " + ex.getMessage());
        	return ResponseEntity.badRequest().body(new BasicResp<Object>("error", null, null));
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
	}
	
}
