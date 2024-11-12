package com.me_social.MeSocial.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

     @Autowired
     private JavaMailSender javaMailSender;

     public void sendOtpEmail(String email, String otp) throws MessagingException {
          MimeMessage mimeMessage = javaMailSender.createMimeMessage();
          MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

          mimeMessageHelper.setTo(email);
          mimeMessageHelper.setSubject("Verify OTP");

          String messageContent = """
                        <div style="font-family: Arial, sans-serif; color: #333;">
                            <div style="padding: 20px; border: 1px solid #ddd; border-radius: 8px; max-width: 400px; margin: auto; text-align: center;">
                                <h2 style="color: #4CAF50; font-size: 24px;">Your Verification OTP</h2>
                                <p style="font-size: 16px; color: #555; margin-top: 10px;">
                                    Use the OTP below to verify your email address:
                                </p>
                                <p style="font-size: 24px; color: #333; font-weight: bold; letter-spacing: 2px; margin: 20px 0;">
                                    %s
                                </p>
                                <p style="font-size: 14px; color: #777;">
                                    This OTP will expire in 5 minutes. Do not share it with anyone.
                                </p>
                            </div>
                        </div>
                    """
                    .formatted(otp);

          mimeMessageHelper.setText(messageContent, true); // Enable HTML

          javaMailSender.send(mimeMessage);
     }
}
