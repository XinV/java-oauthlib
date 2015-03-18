package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class User extends Model {
	
	@Id
	public int id;
	public String email;
	public String passwd;
	
	public static Finder<Integer, User> find = new Finder<Integer, User>(
			Integer.class, User.class);
	
	public List<User> findAll(){
		return find.all();
	}
	
	public User findByEmail(String email){
		return find.where().eq("email", email).findUnique();
	}
}
