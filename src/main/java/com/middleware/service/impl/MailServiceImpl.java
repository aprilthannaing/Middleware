package com.middleware.service.impl;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.middleware.entity.MailEvent;
import com.middleware.service.MailService;

@Service("mailService")
@Transactional(readOnly = true)
public class MailServiceImpl implements MailService {

	@Value("${FILEPATH}")
	private String filePath;

	private static Logger logger = Logger.getLogger(MailServiceImpl.class);

	ConcurrentLinkedQueue<MailEvent> queue = new ConcurrentLinkedQueue<MailEvent>();
	ConcurrentLinkedQueue<MailEvent> taxReportQueue = new ConcurrentLinkedQueue<MailEvent>();

	public String sendMail(MailEvent mailEvent) {

		Properties properties = new Properties();
		// properties.put("mail.smtp.host", "smtp.gmail.com");
		// properties.put("mail.smtp.port", "465");
		// properties.put("mail.smtp.port", "587");
//		properties.put("mail.smtp.ssl.enable", "true");
//		properties.put("mail.smtp.auth", "true");
//		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//		properties.put("mail.smtp.starttls.enable", "true");
//		properties.put("mail.user", "myanmarportal2@gmail.com");
//		properties.put("mail.password", "portal12345#");

		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		logger.info("port !!!!!!!!!!           587");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("myanmarportal2@gmail.com", "portal12345#");
			}
		});

		session.setDebug(true);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("myanmarportal2@gmail.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailEvent.getTo()));
			message.setSubject(mailEvent.getSubject());
			message.setText(mailEvent.getContent());

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(
					"The Payment Transactions report allows sellers to see the transactions that have taken place since the previous settlement period. â€œTransactionsâ€� include orders, adjustments, refunds, and Amazon-initiated credits or charges.");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();

			String newFilePath = filePath + "Payment.xlsx";

			String fileName = new File(newFilePath).getName();
			logger.info("fileName!!!!!!!!!!!!!!!!!!!!" + fileName);

			DataSource source = new FileDataSource(newFilePath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

			System.out.println("sending...");
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			logger.info("error: " + e.getMessage());
			return e.getMessage();
		}

		return "success";

	}
}
