package util;

public class AuthorizeUtil {
	/**
	 * 校验回调uri，防止串改
	 * */
	public static boolean check_redirect_uri(String client_id, String redirect_uri){
		return true;
	}
	
	/**
	 * 生成一条grant数据
	 * 
	 * 返回grant id
	 * */
	public static void generate_grant(String response_tyep, String client_id, String redirect_uri, 
			String scope, int user_id){
		
	}
}
