package controllers;

import java.util.Map;

import models.Client;
import play.mvc.Controller;
import play.mvc.Result;
import util.AuthorizeUtil;
import views.html.client;

public class Common extends Controller{
	
	/**
	 * 生成一个client
	 * */
	public static Result client(){
		if(request().method() == "POST"){
			//新建一个client
			Map<String, String[]> clientInfoForm = request().body().asFormUrlEncoded();
			
			String clientName = clientInfoForm.get("client_name")[0];
			String clientUri = clientInfoForm.get("client_uri")[0];
			
			String clientId = AuthorizeUtil.generateRandomString(20);
			String clientSecret = AuthorizeUtil.generateRandomString(30);
			
			Client client = new Client();
			client.clientName = clientName;
			client.clientId = clientId;
			client.clientSecret = clientSecret;
			client.redirect_uri = clientUri;
			
			client.scope = "email";
			
			client.save();
			
			//创建client成功
			
			return ok("client create done...");
		}
		
		return ok(client.render());
	}
}
