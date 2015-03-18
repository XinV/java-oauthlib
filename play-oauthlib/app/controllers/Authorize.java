package controllers;

import java.util.Map;

import models.User;
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

			User u = new User();
			u.email = email;
			u.passwd = passwd;
			
			session("user", email);
			
			return ok(index.render("Home page ", u));
		}
		
		User user = null;
		if(session().containsKey("user")){
			//session中存在用户，获取用户信息
			int userId = Integer.parseInt(session().get("user"));
			user = new User();
		}
		
		return ok(index.render("Home page", user));
	}
	
	/**
	 * 确认认证页面
	 * 
	 * */
	public static Result authorize(){
		if(!session().containsKey("user")){
			//session中没有用户，跳转到登录页面
			return redirect("/");
		}
		
		if(request().method() == "POST"){
			//确认认证, 则跳转到应用的回调uri并附上code，以便应用凭code换取token
		}
		
		int user_id = Integer.parseInt(session().get("user"));
		//根据oauth2 协议，必须参数包括 
		//response_type
		//client_id
		//redirect_uri
		//scope
		String response_type = request().getQueryString("response_type");
		String client_id = request().getQueryString("client_id");
		String redirect_uri = request().getQueryString("redirect_uri");
		String scope = request().getQueryString("scope");
		
		if(!AuthorizeUtil.check_redirect_uri(client_id, redirect_uri)){
			//校验回调uri，不对则跳转
			return redirect("");
		}
		
		//生成一个grant并存入数据库
		AuthorizeUtil.generate_grant(response_type, client_id, redirect_uri, scope, user_id);
		return ok();
	}
	
	/**
	 * 获取token
	 * 
	 * client根据返回的code换取token
	 * 直接返回token 包括 access_token 和 refresh_token
	 * */
	public static Result token(){
		String code = request().getQueryString("code");
		
		//根据code去grant数据中查找，如果存在且未超时，则获取client_id 等信息，
		//生成一个token，并删除grant
		
		//根据oauth2协议，必须参数
		//grant_type
		//client_id
		//client_srcret
		//code
		//redirect_uri
		String grant_type = request().getQueryString("grant_type");
		String client_id = request().getQueryString("client_id");
		String client_secret = request().getQueryString("client_secret");
		
		return ok();
	}
	
	/**
	 * 测试api接口
	 * */
	public static Result apiUser(){
		return ok();
	}
}
