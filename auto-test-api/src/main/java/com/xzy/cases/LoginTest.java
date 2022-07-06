package com.xzy.cases;

import com.xzy.common.HttpUtil;
import com.xzy.config.BaseConfig;
import com.xzy.config.UserClient;
import com.xzy.model.LoginCase;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LoginTest
 * @Description TODO
 * @Author xzy
 * @Date 2022/7/2 11:57
 * Version 1.0
 **/
public class LoginTest extends BaseTest{
	private static Logger logger = Logger.getLogger(LoginTest.class);

	@BeforeTest(groups = "loginTrue",description = "测试准备工作，获取HttpClient对象")
	public void beforeTest(){
		UserClient.cookieStore = new BasicCookieStore();
		UserClient.httpClient = HttpClients.custom().setDefaultCookieStore(UserClient.cookieStore).build();
	}

	@AfterTest()
	public void afterTest(){
		try {
			UserClient.httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@DataProvider(name="data")
	public Object[][] providerData() throws IOException {
		logger.info("开始加载db数据>LoginCase表");
		SqlSession session = getSqlSession(BaseConfig.CASE_DB);
		List<LoginCase> loginCaseList = session.selectList(BaseConfig.selectAllLoginCase);
		if(loginCaseList!=null&&loginCaseList.size()>0){
			LoginCase[][] loginCases = new LoginCase[loginCaseList.size()][1];
			for(int i = 0;i<loginCases.length;i++){
				loginCases[i][0] = loginCaseList.get(i);
			}
			return loginCases;
		}else{
			logger.error("加载db数据异常：LoginCase表数据为空！");
			throw new RuntimeException("加载db数据异常：LoginCase表数据为空！");
		}
	}

	@Test(groups = "loginTrue",dataProvider = "data",description = "用户登陆成功接口测试")
	public void login(LoginCase[] loginCases) throws IOException {
		LoginCase loginCase = loginCases[0];
		HttpResponse response = HttpUtil.sentPost(UserClient.httpClient,getUrl(BaseConfig.LOGIN_URI),getDefaultHeader(),getParams(loginCase));
		String result = EntityUtils.toString(response.getEntity(),BaseConfig.DEFAULT_CHARSET);

		Assert.assertEquals(result,loginCase.getExpected());

		/*
		if(response.getStatusLine().getStatusCode()!=200){
			logger.error("接口请求异常："+"\n"+result);
			throw new RuntimeException("接口请求异常："+"\n"+result);
		}*/

		/*
		HttpPost httpPost = new HttpPost(getUrl(BaseConfig.LOGIN_URI));
		httpPost.setHeader("content-type","application/json");
		JSONObject param = new JSONObject();
		param.put("userName",loginCase.getUserName());
		param.put("password",loginCase.getPassword());
		StringEntity entity = new StringEntity(param.toString(),"utf-8");
		httpPost.setEntity(entity);
		HttpResponse response = UserClient.httpClient.execute(httpPost);
		String result = EntityUtils.toString(response.getEntity(),"utf-8");
		System.out.println(result);
		//{"timestamp":"2022-07-02T05:26:36.030+0000","status":400,"error":"Bad Request","message":"JSON parse error: Unrecognized token 'userName': was expecting ('true', 'false' or 'null');
		// nested exception is com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'userName': was expecting ('true', 'false' or 'null')\n at [Source: (PushbackInputStream); line: 1, column: 10]","path":"/v1/login"}
		List<Cookie> cookies = UserClient.cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			System.out.println(cookies.get(i).getName()+"===>"+cookies.get(i).getValue());
		}*/

	}

	private Map<String,Object> getParams(LoginCase loginCase){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("userName",loginCase.getUserName());
		params.put("password",loginCase.getPassword());
		return params;
	}

}
