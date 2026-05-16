package com.example.banking.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp)
            throws MessagingException {

        MimeMessage message =
                mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setFrom("shruti@studyverse.me");

        helper.setTo(toEmail);

        helper.setSubject("Bank Account Opening OTP");

        String htmlContent =
                """
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Email Verification - OTP</title>
</head>

<body style="margin:0;padding:0;font-family:Arial,sans-serif;background-color:#f8f7ff;color:#1e1b4b;">

<table role="presentation" style="width:100%;background-color:#f8f7ff;">
<tr>
<td align="center">

<table role="presentation"
style="max-width:580px;width:100%;background:#ffffff;
margin:30px 20px;border-radius:10px;overflow:hidden;">

<!-- HEADER -->
<tr>
<td style="background-color:#4c1d95;padding:40px 30px;text-align:center;">

<h1 style="color:#ffffff;font-size:24px;margin:0;">
NiceAdmin
</h1>

<p style="color:rgba(255,255,255,0.8);
font-size:13px;margin-top:8px;">

Email Verification

</p>

</td>
</tr>

<!-- CONTENT -->
<tr>
<td style="padding:45px 30px;">

<p style="font-size:14px;margin-bottom:10px;">
Hello,
</p>

<p style="color:#6b7280;font-size:14px;line-height:1.8;">
Please use the verification code below to confirm your email address.
</p>

<table role="presentation"
style="width:100%;margin:40px 0;">

<tr>
<td style="text-align:center;">

<div style="background-color:#f3f0ff;
border:1px solid #e9d5ff;
border-radius:8px;
padding:40px 30px;">

<p style="color:#6b7280;
font-size:11px;
text-transform:uppercase;
letter-spacing:1.5px;
font-weight:600;">

Verification Code

</p>

<div style="color:#4c1d95;
font-size:42px;
font-weight:700;
letter-spacing:8px;
margin-top:20px;">

""" + otp + """

</div>

<p style="color:#9ca3af;
font-size:12px;
margin-top:18px;">

Valid for 10 minutes

</p>

</div>

</td>
</tr>

</table>

<p style="color:#6b7280;
font-size:13px;
line-height:1.8;
text-align:center;">

If you didn't request this code,
please ignore this email.

</p>

</td>
</tr>

<!-- FOOTER -->
<tr>
<td style="padding:25px 30px;text-align:center;">

<p style="color:#9ca3af;font-size:12px;">
NiceAdmin | support@niceadmin.com
</p>

<p style="color:#d1d5db;font-size:11px;margin-top:10px;">
© 2026 NiceAdmin. All rights reserved.
</p>

</td>
</tr>

</table>

</td>
</tr>
</table>

</body>
</html>
""";

        // IMPORTANT
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}