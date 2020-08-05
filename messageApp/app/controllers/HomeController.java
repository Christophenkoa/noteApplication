package controllers;

//import liraries
import play.mvc.*;
import models.*;
import java.lang.*;
import play.data.*;
import play.db.ebean.Transactional;
import java.util.*;
import java.text.*;
import javax.inject.*;
import play.i18n.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private Form<Info> formFactory;
    private Form<User_> formFactory2;
    private Form <Note> formFactory3;
    private Form <Reset> formFactory4;
    private Form <PasswordModification> formFactory5;
    private final MessagesApi messagesApi;

    // id of the note which will be modified
    public static Long idNote = -1L; 

    @Inject
    public HomeController(FormFactory f, FormFactory f2 , FormFactory f3 , FormFactory f4 ,FormFactory f5 , MessagesApi messagesApi) {
        this.formFactory = f.form(Info.class) ;
        this.formFactory2 = f2.form(User_.class);
        this.formFactory3 = f3.form(Note.class);
        this.formFactory4 = f4.form(Reset.class);
        this.formFactory5 = f5.form(PasswordModification.class);
        this.messagesApi = messagesApi;
    }


    //Home page controller
    public Result index() {
        return ok(views.html.index.render());
    } 


    //controller to aboutMe page
    public Result aboutMe(Http.Request request) {
        return ok(views.html.aboutMe.render(request));
    } 

    //controller to note presentation page
    public Result notePresentation(Http.Request request, Long id) {
        
        // if the user exists display selected note else go to the login page
        if(request.session().getOptional("id").isPresent()){

            try {
                Note note = Note.find.byId(id);
                return ok(views.html.notePresentation.render(note,formFactory3, request,messagesApi.preferred(request)));
            }catch(NullPointerException e) {
                return redirect(controllers.routes.HomeController.userSpace()).flashing("danger", "WARNING : That note doesn't exist");
            }
            
        }else {
            return redirect(controllers.routes.HomeController.loginPage());
        }
    }


    //controller to display my personnal information if i loged in
    public Result myData(Http.Request request) {

        //check if a session variale exists
        if(request.session().getOptional("id").isPresent()) {

            User_ user = findUser(request);

            if(user == null) {
              return redirect(controllers.routes.HomeController.loginPage());  
            }
            return ok(views.html.personal_inf.render(user , formFactory5 , request,messagesApi.preferred(request)));
        }else{ 
            return ok(views.html.login.render(formFactory, request,messagesApi.preferred(request)));
        }
    }


    // controller which directs you to the loggin page if you haven't already logged in 
    @Transactional
    public Result loginPage(Http.Request request) {

        //chech if any user has loged in
        if(request.session().getOptional("id").isPresent()) {
            return redirect(controllers.routes.HomeController.userSpace());
        }else{ 
            return ok(views.html.login.render(formFactory, request,messagesApi.preferred(request)));
        }
    }


    // controller which directs you to the registration page if you haven't already loged in
    @Transactional
    public Result registrationPage(Http.Request request) {

        if(request.session().getOptional("id").isPresent()) {
            return redirect(controllers.routes.HomeController.userSpace());
        } else{ 
            return ok(views.html.registration.render(formFactory2, request,messagesApi.preferred(request)));
         }
    }



    /*handle and proccess informations received from the registration page 
    if there is no errors go to the registration page else stay on the same page and print an error message*/
    @Transactional
    public Result registration(Http.Request request) {

        Form<User_> regForm =  formFactory2.bindFromRequest(request);   

        if(regForm.hasErrors()) {
            return redirect(controllers.routes.HomeController.registrationPage()).flashing("danger", "WARNING : some required informations are missing");
        }

        User_ user = regForm.get();

        //register the user if the username is not already used
        try{
            user.save();
        }catch(io.ebean.DuplicateKeyException e) {
            return redirect(controllers.routes.HomeController.registrationPage()).flashing("danger", "WARNING : This user name or/and email are already used");
        }
        

        return redirect(controllers.routes.HomeController.loginPage()).flashing("success", "successfully registration");
    }



    //check if information from login page are corrects and match with any user present in my database
    public Result authenticate(Http.Request request) {

        Form<Info> logForm =  formFactory.bindFromRequest(request);

        if(logForm.hasErrors()) {
           return redirect(controllers.routes.HomeController.loginPage()).flashing("danger", "WARNING : some required informations are missing"); 
        }

        Info info = logForm.get();
        User_ user = null;

        //check in my database if any user with those informations(from login form) exits.
        user = user.authenticates(info.name,info.password);


        //if it is the case go to the user page and create a session variable with its id
        if(user != null) {

            String id = String.valueOf(user.getUserID());
            return ok(views.html.user.render(user,formFactory3, request,messagesApi.preferred(request))).
                                            addingToSession(request, "id", id);

        //print an error if there is no user with those informations
        }else
            return redirect(controllers.routes.HomeController.loginPage()).flashing("danger", "WARNING : user name and password are not matching");
    }



    //add a note retrieves from the text area
    public Result addNote(Http.Request request) {

        Form<Note> noteForm =  formFactory3.bindFromRequest(request);

        if(noteForm.hasErrors()) {
            return redirect(controllers.routes.HomeController.userSpace()).flashing("danger", "WARNING : title and content note are required");
        }

        //retrieve a note from the form
        Note note = noteForm.get();

        //set a date from that note
        generateDate(note);

        //find the user who want to save a note
        User_ user = findUser(request);

        //add the note in the note list of that user 
        user.getNotes().add(note);

        //link the note to that user
        note.setUser(user);

        //save the note 
        try {
            note.save();
        }catch(io.ebean.DuplicateKeyException e) {
            return redirect(controllers.routes.HomeController.userSpace()).flashing("danger", "WARNING : This title is already use");
        }
        

        //stay on the user space to see the new note
        return redirect(controllers.routes.HomeController.userSpace());
   
    }



    // user space
    public Result userSpace(Http.Request request) {

        //if the user is still connected via a session name go to the user page else to the login page
        if(request.session().getOptional("id").isPresent()){

            User_ user = findUser(request);

            if(user == null) {
              return redirect(controllers.routes.HomeController.loginPage());  
            }

            return ok(views.html.user.render(user,formFactory3, request,messagesApi.preferred(request)));

        }else {
            return redirect(controllers.routes.HomeController.loginPage());
        }
    }



    // a user deletes a note
    public Result deleteNote(Http.Request request , Long id){

        // if the user exists delete the note else go to the login page
        if(request.session().getOptional("id").isPresent()){
            //User_ user = findUser(request);
            Note note = Note.find.byId(id);
            note.delete();
            return redirect(controllers.routes.HomeController.userSpace());
        }else {
            return redirect(controllers.routes.HomeController.loginPage());
        }
    }



    //an user edits a note
    public Result editNote(Http.Request request , Long id){

        //keep the note id in our global variable to use it in the update method or controller
        idNote = id;

        return redirect(controllers.routes.HomeController.notePresentation(id));

    }


    //process the note informations and update that note if there is no errors
    public Result updateNote(Http.Request request){

        Form<Note> noteForm =  formFactory3.bindFromRequest(request);

        if(noteForm.hasErrors()) {
            return redirect(controllers.routes.HomeController.userSpace()).flashing("danger", "WARNING : title and content note are required");
        }

        Note newNote = noteForm.get();

        //if the note id exists ,  erase the previous note content by the new one if not flash an error
        if(idNote != -1L) {
            Note oldNote = Note.find.byId(idNote);
            oldNote.setNoteName(newNote.getNoteName());
            oldNote.setNoteContent(newNote.getNoteContent());
            generateDate(oldNote);
            oldNote.update();
            return redirect(controllers.routes.HomeController.notePresentation(idNote)).flashing("success", "Note updated successfully");
        }else {
            return redirect(controllers.routes.HomeController.userSpace()).flashing("danger", "You can't edit from here");
        }
    }


    //retrieve the current user via the session vaiable(id)
    private User_ findUser(Http.Request request) { 

            String stringId = request.session().getOptional("id").orElse("id");
            Long id = Long.parseLong(stringId);
            User_ user = User_.find.byId(id);
            return user;
    }



    // eneable a user to logout by destroying the session variable
    public Result logout(Http.Request request) {
        return redirect(controllers.routes.HomeController.loginPage()).removingFromSession(request, "id");
    }


    //generate a date for a particular note
    private void generateDate(Note note) {
        String pattern = "dd MMMMM yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        note.setDate(date);
    }



    @Transactional
    public Result resetPage(Http.Request request) {

        //chech if any user has loged in
        if(request.session().getOptional("id").isPresent()) {
            return redirect(controllers.routes.HomeController.userSpace());
        }else{ 
            return ok(views.html.passwordReset.render(formFactory4, request,messagesApi.preferred(request)));
        }
    }



    //process a resetting password query
    public Result resettingProcess(Http.Request request) {

        Form<Reset> resetForm =  formFactory4.bindFromRequest(request);

        if(resetForm.hasErrors()) {
           return redirect(controllers.routes.HomeController.resetPage()).flashing("danger", "WARNING : enter a correct and valid email"); 
        }

        Reset userEmail = resetForm.get();
        User_ user = null;
        user = user.findingEmail(userEmail.email);

        //check in my database if any user with those informations(from login form) exits.
        if(user != null) {
            String newPassword = getAlphaNumericString(10);
            user.setUserPassword(newPassword);
            user.update();
            return redirect(controllers.routes.MailerService.sendEmail(user.getUserEmail(),user.getUserPassword()));
        }


        //if it is the case go to the user page and create a session variable with its id


        //print an error if there is no user with those informations
            return redirect(controllers.routes.HomeController.resetPage()).flashing("danger", "WARNING : this email doesn't match with any account");
    }




    //generate a random password
    private String getAlphaNumericString(int n) { 
    
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 




    //process password modification
    public Result passwordModification(Http.Request request) {

        Form<PasswordModification> passwordForm =  formFactory5.bindFromRequest(request);

        if(passwordForm.hasErrors()) {
           return redirect(controllers.routes.HomeController.myData()).flashing("danger", "WARNING : fill every field correctly"); 
        }

        PasswordModification passwordInfo = passwordForm.get();

        //check if password and confirm password are matching
        if(!passwordInfo.password.equals(passwordInfo.confirmationPassword)) {
            return redirect(controllers.routes.HomeController.myData()).flashing("danger", "WARNING : passwords are not matching");
        }

        //if they are matched , use the session variable to find the current user
        if(request.session().getOptional("id").isPresent()){

            User_ user = findUser(request);

            if(user == null) {
              return redirect(controllers.routes.HomeController.loginPage());  
            }

            //check if the password entered is equal to the user password and change the user password value if it is true
            if(passwordInfo.currentPassword.equals(user.getUserPassword())) {
                user.setUserPassword(passwordInfo.password);
                user.update();
                return redirect(controllers.routes.HomeController.myData()).flashing("success", "Your password has been modified");
            }else{
                return redirect(controllers.routes.HomeController.myData()).flashing("danger", "WARNING : This is not the current password");
            }

        }else {
            return redirect(controllers.routes.HomeController.loginPage());
        } 
    } 

}
