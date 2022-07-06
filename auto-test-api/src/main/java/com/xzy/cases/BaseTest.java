package com.xzy.cases;

import com.xzy.common.DatabaseUtil;
import com.xzy.common.PropertUtil;
import com.xzy.config.BaseConfig;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BaseTest
 * @Description TODO
 * @Author xzy
 * @Date 2022/7/2 17:03
 * Version 1.0
 **/
public class BaseTest {
	public String getUrl(String uriName){
		return PropertUtil.getPropert(BaseConfig.BASE_URL) +PropertUtil.getPropert(uriName);
	}

	public SqlSession getSqlSession(String dbKey) throws IOException {
		return DatabaseUtil.getSqlSession(PropertUtil.getPropert(dbKey));
	}

	public Map<String,String> getDefaultHeader(){
		Map<String, String> header = new HashMap<String, String>();
		header.put("content-type","application/json");
		return header;
	}
}
