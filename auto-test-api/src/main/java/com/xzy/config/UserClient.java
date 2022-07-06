package com.xzy.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @ClassName UserClient
 * @Description 用于存放用户模块的httpClient、cookie；
 * @Author xzy
 * @Date 2022/7/2 12:50
 * Version 1.0
 **/
public class UserClient {
	public static CloseableHttpClient httpClient;
	public static CookieStore cookieStore;
}
