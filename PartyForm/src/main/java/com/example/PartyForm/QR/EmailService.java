package com.example.PartyForm.QR;

import com.example.PartyForm.Model.Form;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    public void sendRegistrationEmail(Form formData) throws MessagingException, IOException {
        try {
            // Prepare QR code data
            String qrData = String.format("Name: %s\nEmail: %s\nYear: %s\nGender: %s",
                    formData.getName(), formData.getEmail(), formData.getYear(), formData.getGender());
            byte[] qrCodeImage = qrCodeGenerator.generateQRCode(qrData);

            // Create email message
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(formData.getEmail());
            helper.setTo(formData.getEmail());
            helper.setSubject("🎉 You're Invited to the Computer Science SCRIET 2024 Freshers Party! 🎉");

            // Create the email body
            String emailBody = String.format("<p>Dear %s,</p>"
                            + "<p>We are excited to invite you to the Computer Science SCRIET 2024 Freshers Party! "
                            + "Join us for a day of fun, laughter, and new connections as we welcome our new students into the community.</p>"
                            + "<p><strong>Details of the Event:</strong></p>"
                            + "<p>Date: 8th October 2024<br>"
                            + "Time: 11 A.M<br>"
                            + "Venue: Brihaspati Bhawan, CCSU</p>"
                            + "<p>As a special touch, we will be providing a customized unique QR code for you upon registration. "
                            + "This QR code will serve as your entry pass and make check-in a breeze!</p>"
                            + "<p>Best regards,2nd Year senior<br>Computer Science Department<br>SCRIET</p>",
                    formData.getName());

            // Set the email body
            helper.setText(emailBody, true);


            // Attach the QR code image
            helper.addAttachment("QRCode.png", new ByteArrayResource(qrCodeImage));

            // Send email
            mailSender.send(message);

        } catch (WriterException e) {
            // Handle WriterException here
            e.printStackTrace(); // Or handle it in a more user-friendly way, like logging
        }
    }
}
