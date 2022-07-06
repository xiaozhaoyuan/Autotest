package com.xzy.cases;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xzy.common.HttpUtil;
import com.xzy.config.BaseConfig;
import com.xzy.config.UserClient;
import com.xzy.model.GetUserListCase;
import com.xzy.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GetUserInfoTest
 * @Description TODO
 * @Author xzy
 * @Date 2022/7/2 21:16
 * Version 1.0
 **/
public class GetUserInfoListTest extends BaseTest{
	private static Logger logger = Logger.getLogger(GetUserInfoListTest.class);

	@DataProvider(name="data")
	public Object[][] providerData() throws IOException {
		logger.info("开始加载db数据>GetUserListCase表");
		SqlSession session = getSqlSession(BaseConfig.CASE_DB);
		List<GetUserListCase> userListCaseList = session.selectList(BaseConfig.selectAllUserListCase);
		if(userListCaseList!=null&&userListCaseList.size()>0){
			GetUserListCase[][] userListCases = new GetUserListCase[userListCaseList.size()][1];
			for(int i = 0;i<userListCases.length;i++){
				userListCases[i][0] = userListCaseList.get(i);
			}
			return userListCases;
		}else{
			logger.error("加载db数据异常：GetUserListCase表数据为空！");
			throw new RuntimeException("加载db数据异常：GetUserListCase表数据为空！");
		}
	}

	@Test(dependsOnGroups = "loginTrue",dataProvider = "data",description = "按条件获取用户列表")
	public void getUserListInfo(GetUserListCase[] userListCases) throws IOException {
		GetUserListCase userListCase = userListCases[0];
		HttpResponse response = HttpUtil.sentPost(UserClient.httpClient,getUrl(BaseConfig.GETUSERLIST_URI),getDefaultHeader(),getParams(userListCase));
		String result = EntityUtils.toString(response.getEntity(),BaseConfig.DEFAULT_CHARSET);
		//System.out.println(result);
		JSONArray resultJsonArray = JSONArray.parseArray(result);

		//去真实数据库查询后校验
		SqlSession session = getSqlSession(BaseConfig.REAL_DB);
		List<User> userList = session.selectList(BaseConfig.getUserList,userListCase);
		JSONArray userListJsonArray = JSONArray.parseArray(JSON.toJSONString(userList));

		Assert.assertEquals(userListJsonArray.size(),resultJsonArray.size());
		for(int i = 0 ;i<resultJsonArray.size();i++){
			JSONObject actual = (JSONObject) resultJsonArray.get(i);
			System.out.println("接口返回的user："+actual.toString());
			JSONObject expect = (JSONObject) userListJsonArray.get(i);
			System.out.println("自己查询的user："+expect.toString());
			Assert.assertEquals(actual.toString(),expect.toString());
			System.out.println("------------------------------------------------------------");
		}
	}

	private Map<String,Object> getParams(GetUserListCase userListCase){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("userName",userListCase.getUserName());
		params.put("sex",userListCase.getSex());
		params.put("age",userListCase.getAge());
		return params;
	}
}
