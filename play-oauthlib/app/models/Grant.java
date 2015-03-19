package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.format.*;
import play.db.ebean.Model;
import play.data.validation.*;

@Entity
@Table(name="oauth_grant")
public class Grant extends Model {
	@Id
	public int id;
	
	@Constraints.Required
	public String responseType;
	
	@Constraints.Required
	public String clientId;
	
	@Constraints.Required
	public int userId;
	
	@Constraints.Required
	public String code;
	
	@Constraints.Required
	public String redirectUri;
	
	@Constraints.Required
	public String scope;
	
	@Constraints.Required
	@Formats.DateTime(pattern="yyyy-MM-ddThh:mm:ss")
	public Date expires;
	
	public static Finder<Integer, Grant> find = new Finder<Integer, Grant>(
			Integer.class, Grant.class);
	
	public static Grant findById(int id){
		return find.byId(id);
	}
	
	public static Grant findByCode(String code){
		return find.where().eq("code", code).findUnique();
	}
}
