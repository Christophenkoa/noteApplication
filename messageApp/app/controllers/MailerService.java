package controllers;

import play.mvc.*;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import javax.inject.Inject;
import java.io.File;
import org.apache.commons.mail.EmailAttachment;
import models.User;
import play.data.*;
import play.db.ebean.Transactional;
import java.util.*;
import javax.inject.*;
import play.i18n.*;

public class MailerService extends Controller{

  @Inject MailerClient mailerClient;

  public Result sendEmail(String emailReceive , String password , Http.Request request) {
    Email email = new Email()
      .setSubject("noteApp your second memory")
      .setFrom("aa@gmail.com")
      .addTo(emailReceive)
      .setBodyText("------")
      .setBodyHtml("<html><body><p>"+ 
                      "<strong>Hi !<strong><br> " + "your new password is : <br><h2>" + password + " "+ 
                      "</h2>You can change it after loging into your account" +  
                      "</p><section>Thank your for your fidelity.</section>" +
                    "</body></html>");
    mailerClient.send(email);
    return redirect(controllers.routes.HomeController.resetPage()).flashing("success" , "A new password has been sent on your email account");
  }
}