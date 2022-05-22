package ru.netcracker.bikepackerserver.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.service.OnRegistrationCompleteEvent;
import ru.netcracker.bikepackerserver.service.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    @Value("${spring.mail.username}")
    private String emailBikepacker;

    @Autowired
    public RegistrationListener(UserService service, MessageSource messages, JavaMailSender mailSender) {
        this.service = service;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
        UserEntity user = event.getUser();
        Optional<String> recipientAddress = Optional.ofNullable(user.getEmail());
        Optional<String> userFirstname = Optional.of(Optional.ofNullable(user.getFirstname()).orElse("dear user!"));

        if(recipientAddress.isPresent()){
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "utf-8");
            String token = UUID.randomUUID().toString();
            service.createVerificationToken(user, token);
            String urlMessage = serverUrl+"registrationConfirm/" + token;
            String message = getHTML(urlMessage);
            mail.setContent(message,"text/html;charset=UTF-8");
            mimeMessageHelper.setTo(recipientAddress.get());
            mimeMessageHelper.setFrom(emailBikepacker);
            mimeMessageHelper.setSubject("Confirm Bikepacker registration");
            mailSender.send(mail);
        }

    }


    private String getHTML(String urlMessage){
         return "<html><head><style>\n" +
                 "    @import url('https://fonts.googleapis.com/css2?family=Poppins&display=swap');\n" +
                 "</style>\n" +
                 "</head><body><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"\n" +
                 "        margin: 0 auto;\n" +
                 "        padding: 0;\n" +
                 "        max-width: 390px;\n" +
                 "        width: 100vw;\n" +
                 "        text-align: center;\n" +
                 "        font-family: 'Poppins', sans-serif;\n" +
                 "        background-color: #5D3EA8;\n" +
                 "        background-image: url(https://bikepacking.com/wp-content/uploads/2022/04/4-venados-eco-overnighter-12.jpg);\n" +
                 "        background-position: center;\n" +
                 "        background-size: cover;\n" +
                 "        background-repeat: no-repeat;\n" +
                 "    \">\n" +
                 "    <tbody>\n" +
                 "        <tr style=\"display:block;\">\n" +
                 "            <td style=\"display:block; background-color: #5D3EA8; padding: 20px;\">\n" +
                 "                <object data=\"https://svgshare.com/i/h3d.svg\" type=\"image/svg+xml\" style=\"\n" +
                 "                    display: block;\n" +
                 "                    width: 70%;\n" +
                 "                    height: auto;\n" +
                 "                    margin: auto;\n" +
                 "                    overflow: hidden;\n" +
                 "                    \">\n" +
                 "                    <img src=\"https://res.cloudinary.com/mcborrow228/image/upload/v1652348632/bikepacker_logo_sznejt.png\" alt=\"Bikepacker logo\" style=\"\n" +
                 "                                            background-color: #5D3EA8;\n" +
                 "                                            margin: 0;\n" +
                 "                                            padding: 0;\n" +
                 "                                            border: 0;\n" +
                 "                                            display: block;\n" +
                 "                                            width: 100%;\n" +
                 "                                            height: 100px;\n" +
                 "                                            object-fit: contain;\n" +
                 "                                        \" id=\"nvidia-logo_mr_css_attr\" constrain=\"true\" imagepreview=\"false\" width=\"720\" height=\"50\">\n" +
                 "                </object>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "        <tr style=\"display:block; margin-top: 15px; margin-bottom: 30px; padding: 10px; color: #ffffff\">\n" +
                 "            <td>\n" +
                 "                <h1 style=\"margin: 40px 0;\">Thank you for joining Bikepacker family!</h1>\n" +
                 "                <p>We’d like to confirm that your account was created successfully.</p>\n" +
                 "                <p>To access the app click the link below.</p>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "        <tr style=\"display: block; width: 100%; padding: 0;\">\n" +
                 "            <td align=\"center\" class=\"em_font_mr_css_attr\" valign=\"middle\" style=\"\n" +
                 "                    display: flex;\n" +
                 "                    align-items: center;\n" +
                 "                    width: 100%;\n" +
                 "                    max-width: 60%;\n" +
                 "                    height: 60px;\n" +
                 "                    margin: 0 auto 60px;\n" +
                 "                    text-align: center;\n" +
                 "                    padding: 0; \n" +
                 "                    text-transform: uppercase; \n" +
                 "                    letter-spacing: 1px;\n" +
                 "                \">\n" +
                 "                <a href=\"" + urlMessage +"\" style=\"\n" +
                 "                        display: block;\n" +
                 "                        font-family: 'Poppins', sans-serif;\n" +
                 "                        font-size: 14px; \n" +
                 "                        font-weight: bold;\n" +
                 "                        width: 100%; \n" +
                 "                        height: 60px; \n" +
                 "                        line-height: 60px;\n" +
                 "                        text-decoration: none; \n" +
                 "                        color: #ffffff; \n" +
                 "                        display: block;\n" +
                 "                        background-color: #5D3EA8; \n" +
                 "                        border: 1px solid #5D3EA8;\" target=\"_blank\" rel=\" noopener noreferrer\">Confirm your email</a>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "    </tbody>\n" +
                 "</table></body></html>";
    }
}