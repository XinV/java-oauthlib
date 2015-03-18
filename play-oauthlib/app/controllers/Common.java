package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Common extends Controller{
	
	/**
	 * 生成一个client
	 * */
	public static Result client(){
		return ok();
	}
}
