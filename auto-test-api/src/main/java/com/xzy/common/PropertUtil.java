package com.xzy.common;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @ClassName PropertUtil
 * @Description TODO
 * @Author xzy
 * @Date 2022/7/2 16:37
 * Version 1.0
 **/
public class PropertUtil {
	private static ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);

	public static String getPropert(String key){
		return bundle.getString(key);
	}
}
