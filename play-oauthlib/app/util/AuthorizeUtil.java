package util;

import java.util.Date;
import java.util.Random;

import models.Client;
import models.Grant;

public class AuthorizeUtil {
	/**
	 * 校验回调uri，防止串改
	 * */
	public static boolean check_redirect_uri(String clientId, String redirectUri){
		Client client = Client.findById(clientId);
		if(client.redirect_uri.equals(redirectUri)){
			return true;
		}
		return false;
	}
	
	public static boolean checkClientValid(String code, String clientId, String clientSecret){
		Grant grant = Grant.findByCode(code);
		String grant_clientId = grant.clientId;
		Client client = Client.findById(grant_clientId);
		
		if(client.clientId.equals(clientId) && client.clientSecret.equals(clientSecret)){
			return true;
		}
		return false;
	}
	
	/**
	 * 生成一条grant数据
	 * 
	 * 返回grant id
	 * */
	public static String generate_grant(String responseType, String clientId, String redirectUri, 
			String scope, int userId){
		Grant grant = new Grant();
		
		grant.responseType = responseType;
		grant.clientId = clientId;
		grant.redirectUri = redirectUri;
		grant.scope = scope;
		grant.userId = userId;
		
		//随机生成code
		String code = generateRandomString(40);
		
		grant.code = code;
		
		Date current = new Date();
		long time = current.getTime()/1000 + 60 * 5;
		current.setTime(time*1000);
		
		grant.expires = current;
		
		grant.save();
		
		return code;
	}
	
	/**
	 * 根据传入参数生成对应长度的随机字符串
	 * */
	public static String generateRandomString(int length){
		StringBuffer allChar = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = allChar.length();
		
		for(int i=0;i<length;i++){
			sb.append(allChar.charAt(r.nextInt(range)));
		}
		
		return sb.toString();
	}
}
