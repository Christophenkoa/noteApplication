package models;

import java.util.*;

import javax.persistence.*;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*; 
import javax.inject.Inject;


@Entity
public class Info extends Model {

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String password;
}