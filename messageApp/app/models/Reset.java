package models;

import java.util.*;

import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*; 
import javax.inject.Inject;

@Entity
public class Reset extends Model {

    @Constraints.Email
    @Constraints.Required
    public String email;

}