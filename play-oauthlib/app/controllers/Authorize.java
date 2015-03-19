package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Client;
import models.Grant;
import models.Token;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.AuthorizeUtil;
import views.html.*;

public class Authorize extends Controller{
	
	/**
	 * 如果用户不存在session中。提示登录
	 * 
	 * */
	public static Result index(){
		if(request().method() == "POST"){
			
			Map<String, String[]> form_values = request().body().asFormUrlEncoded();
			String email = form_values.get("email")[0];
			String passwd = form_values.get("passwd")[0];
			
			User user = User.findByEmail(email);
			
			if(user != null){
				session("user", String.valueOf(user.id));
			}
			
			//return ok(index.render("Home page ", user));
			return redirect("/authorize?response_type=code&client_id=abc&redirect=http://localhost:8000&scope=email");
		}
		
		User user = null;
		if(session().containsKey("user")){
			//session中存在用户，获取用户信息
			int userId = Integer.parseInt(session().get("user"));
			user = User.findById(userId);
		}
		
		return ok(index.render("Home page", user));
	}
	
	/**
	 * 确认认证页面
	 * 用户点击是否确认认证
	 * */
	public static Result authorize(){
		if(!session().containsKey("user")){
			//session中没有用户，跳转到登录页面
			return redirect("/");
		}
		
		if(request().method() == "POST"){
			//确认认证, 则跳转到应用的回调uri并附上code，以便应用凭code换取token
			
			Map<String, String[]> confirm_data = request().body().asFormUrlEncoded();
			
			String confirm = confirm_data.get("yes")[0];
			
			if(confirm.equals("yes")){
				String responseType = confirm_data.get("response_type")[0];
				String clientId = confirm_data.get("client_id")[0];
				String redirectUri = confirm_data.get("redirect_uri")[0];
				String scope = confirm_data.get("scope")[0];
				int userId = Integer.parseInt(session().get("user"));
				
				//生成一个grant并存入数据库，并返回code
				String code = AuthorizeUtil.generate_grant(responseType, clientId, redirectUri, scope, userId);
				
				//跳转到client的回调uri
				return redirect(redirectUri + "?code=" + code);
			}else{
				//不同意授权，跳转
				//return redirect("");
				return ok("not allowed");
			}
		}
		
		int user_id = Integer.parseInt(session().get("user"));
		
		User user = User.findById(user_id);
		
		//根据oauth2 协议，必须参数包括 
		//response_type
		//client_id
		//redirect_uri
		//scope
		String responseType = request().getQueryString("response_type");
		String clientId = request().getQueryString("client_id");
		String redirectUri = request().getQueryString("redirect_uri");
		String scope = request().getQueryString("scope");
		
		if(!AuthorizeUtil.check_redirect_uri(clientId, redirectUri)){
			//校验回调uri，不对则跳转
			return ok("redirect uri invalid");
		}
		
		return ok(authorize.render(user, clientId, responseType, redirectUri, scope));
	}
	
	/**
	 * 获取token
	 * 
	 * client根据返回的code换取token
	 * 直接返回token 包括 access_token 和 refresh_token
	 * */
	public static Result token(){
		//根据oauth2协议，必须参数
		//grant_type
		//client_id
		//client_srcret
		//code
		//redirect_uri
		String grantType = request().getQueryString("grant_type");
		String clientId = request().getQueryString("client_id");
		String clientSecret = request().getQueryString("client_secret");
		String code = request().getQueryString("code");
				
		//根据code去grant数据中查找，如果存在且未超时，则获取client_id 等信息，
		//生成一个token，并删除grant
		Grant grant = Grant.findByCode(code);
		
		Date now = new Date();
		
		//检查是否符合client
		if(!AuthorizeUtil.checkClientValid(code, clientId, clientSecret)){
			return ok("not valid client");
		}
		
		//检查是否已经超过expires		
		if(now.before(grant.expires)){
			//超过过期时间
			return ok("expires， re-ensure aggin");
		}
		
		//生成一个token
		Token token = new Token();
		token.cliendId = clientId;
		token.userId = Integer.parseInt(session().get("userId"));
		token.scope = grant.scope;
		token.tokenType = "Bearer";
		
		Date expires = new Date();
		expires.setTime(expires.getTime()/1000 + 60 * 60 * 24 * 30);
		
		token.expires = expires;
		
		String access_token = "";
		String refresh_token = "";
		
		//保存token
		token.save();		
		
		response().setContentType("appliction/json");
		ObjectNode token_return = Json.newObject();
		token_return.put("access_token", access_token);
		token_return.put("refresh_token", refresh_token);
		
		return ok(token_return);
	}
	
	/**
	 * 测试api接口
	 * */
	public static Result apiUser(){
		return ok();
	}
}
