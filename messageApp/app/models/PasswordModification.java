package models;

import java.util.*;

import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*; 
import javax.inject.Inject;


@Entity
public class PasswordModification extends Model {

    @Constraints.Required
    public String password;

    @Constraints.Required
    public String confirmationPassword;

    @Constraints.Required
    public String currentPassword;
}