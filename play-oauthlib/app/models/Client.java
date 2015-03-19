package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Client extends Model{
	
	@Id
	public int id;
	
	public String clientName;
	public String clientId;
	public String clientSecret;
	
	public String redirect_uri;
	public String scope;
	
	public static Finder<Integer, Client> find = new Finder<Integer, Client>(
			Integer.class, Client.class);
	
	public static Client findById(String clientId){
		return find.where().eq("clientId", clientId).findUnique();
	}
	
	public static Client findByName(String name){
		return find.where().eq("clientName", name).findUnique();
	}
}
