package models;

import java.util.*;

import javax.persistence.*;

import io.ebean.*;
//import com.avaje.ebean.Model;
//import com.avaje.ebean.Ebean;
import play.data.format.*;
import play.data.validation.*;  
import javax.inject.Inject;


@Entity
public class User_ extends Model {
    @Column(name="user_")
    @Id
    private Long userID;

    @Column(unique=true)
    @Constraints.Required
    private String userName;

    @Constraints.Required
    private String userPassword;

    @Constraints.Email
    @Constraints.Required
    @Column(unique=true)
    private String userEmail;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Note> notes;


//constructors

    @Inject 
    public User_(Long id , String name , String email , String password , List<Note> notes) {
        this.userName = name;
        this.userID = id;
        this.userEmail = email ;
        this.userPassword = password;
        this.notes = notes;

    }


    //getteurs

    public String getUserName() {
        return this.userName;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public Long getUserID() {
        return this.userID;
    }

    public List<Note> getNotes() {
        return this.notes;
    }


//setters

    public void setUserName(String name) {
        this.userName = name;
    }

    public void setUserPassword(String password) {
        this.userPassword = password;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public void setUserID(Long id) {
        this.userID = id;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }


    // Retrieve all users from the database
    public static Finder <Long,User_> find = new Finder<Long, User_>(User_.class);



    public static User_ findByUserName(String userName) {
        return find.query().where()
                .eq("userName", userName)
                .findOne();
    }



    //check there is an user in our db with taht name and password
    public static User_ authenticates(String name, String password) {

        //find a user with that name
        User_ user = find
                    .query()
                    .where()
                    .eq("userName", name)
                    .findOne();

        // in this case a user with that name exits
        if (user != null) {

            /*check if the password of that user equal to the password of my function with is the one
            enter in the login page*/
            if (password.equals(user.getUserPassword())) {
                return user;
            }
        }

        //if such user doesn't exit return null 
        return null;
    }

    //Searching a particular user via a given email
        public static User_ findingEmail(String email) {

        User_ user = find
                    .query()
                    .where()
                    .eq("userEmail", email)
                    .findOne();

        return user;
    }



}