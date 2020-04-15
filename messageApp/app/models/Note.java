package models;

import java.util.*;

import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*; 
import javax.inject.Inject;


@Entity
public class Note extends Model {
    @Id
    private Long noteID;

    @Constraints.Required
    private String noteName;

    @Constraints.Required
    private String noteContent;

    private String date;

    @ManyToOne
    private User user;


//constructors

    @Inject 
    public Note (Long id , String name , String content , User user) {
        this.noteName = name;
        this.noteID = id;
        this.noteContent = content;
        this.user = user;

    }


    //getteurs

    public String getNoteName() {
        return this.noteName;
    }


    public Long getNoteID() {
        return this.noteID;
    }

    public String getNoteContent() {
        return this.noteContent;
    }

    public String getDate() {
        return this.date;
    }

    public User getUser() {
        return this.user;
    }


//setters

    public void setNoteName(String name) {
        this.noteName = name;
    }

    public void setNoteContent(String content) {
        this.noteContent = content;
    }

    public void setNoteID(Long id) {
        this.noteID = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }


    // Retrieve all users from the database
    public static Finder <Long,Note> find = new Finder<>(Note.class);

}