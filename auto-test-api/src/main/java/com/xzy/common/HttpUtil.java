package com.xzy.common;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName HttpUtil
 * @Description http工具类
 * @Author xzy
 * @Date 2022/7/2 18:09
 * Version 1.0
 **/
public class HttpUtil {
	private static final String DEFAULT_CHARSET = "utf-8";

	public static HttpResponse sentPost(CloseableHttpClient httpClient, String url, Map<String,String> headers, Map<String,Object> params) throws IOException {
		return sentPost(httpClient, url,headers,params,DEFAULT_CHARSET);
	}

	public static HttpResponse sentPost(CloseableHttpClient httpClient, String url, Map<String,String> headers,Map<String,Object> params,String charset) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		if(headers!=null&&headers.size()>0){
			for (String key : headers.keySet()) {
				httpPost.setHeader(key,headers.get(key));
			}
		}
		if(params!=null&&params.size()>0){
			JSONObject json = new JSONObject(params);
			StringEntity entity = new StringEntity(json.toString(),charset);
			httpPost.setEntity(entity);
		}
		HttpResponse response = httpClient.execute(httpPost);
		return response;
	}
}
