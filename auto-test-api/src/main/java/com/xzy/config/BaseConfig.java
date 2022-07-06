package com.xzy.config;

import com.xzy.common.DatabaseUtil;
import com.xzy.common.PropertUtil;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

/**
 * @ClassName BaseConfig
 * @Description TODO
 * @Author xzy
 * @Date 2022/7/2 17:12
 * Version 1.0
 **/
public class BaseConfig {
	public static final String DEFAULT_CHARSET = "utf-8";
	public static final String BASE_URL = "base_url";
	public static final String CASE_DB = "case_db";
	public static final String REAL_DB = "real_db";

	/** user模块相关接口名***************************/
	public static final String LOGIN_URI = "login.uri";
	public static final String ADDUSER_URI = "addUser.uri";
	public static final String GETUSERLIST_URI = "getUserList.uri";

	/** user模块相关sql的id ***************************/
	public static final String selectAllLoginCase = "selectAllLoginCase";
	public static final String selectAllAddUserCase = "selectAllAddUserCase";
	public static final String selectAllUserListCase = "selectAllUserListCase";
	public static final String getUserList = "getUserList";


}
