package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Token extends Model {
	@Id
	public int id;
	
	@Constraints.Required
	public String clientId;
	
	@Constraints.Required
	public int userId;
	
	@Constraints.Required
	public String tokenType;
	
	@Constraints.Required
	public String accessToken;
	
	@Constraints.Required
	public String refreshToken;
	
	@Constraints.Required
	public String scope;
	
	@Constraints.Required
	@Formats.DateTime(pattern="yyyy-MM-ddThh:mm:ss")
	public Date expires;
	
	public static Finder<Integer, Token> find = new Finder<Integer, Token>(
			Integer.class, Token.class);
	
	public static Token findByUserId(int userId){
		return find.where().eq("userId", userId).findUnique();
	}
	
	public static List<Token> findByClientAndUser(String clientId, int userId){
		return find.where().eq("clientId", clientId).eq("userId", userId).findList();
	}
}
