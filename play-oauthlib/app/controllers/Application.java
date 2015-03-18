package controllers;

import java.util.List;

import models.User;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
    	User user = new User();
        return ok(index.render("Your new application is ready.", user));
    }
    
    public static Result db(){
    	List<User> users = User.findAll();
    	
    	return ok();
    }

}
