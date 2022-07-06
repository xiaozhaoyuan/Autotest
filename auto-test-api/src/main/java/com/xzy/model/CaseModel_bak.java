package com.xzy.model;

/**
 * @ClassName InterfaceModel
 * @Description 暂时不用的类
 * @Author xzy
 * @Date 2022/7/1 14:49
 * Version 1.0
 **/
public class CaseModel_bak {
	/**
	 *用例名称
	 */
	private String caseName;
	/**
	 *用例描述
	 */
	private String description;
	/**
	 *请求方式：get post
	 */
	private String requestType;
	/**
	 *请求url
	 */
	private String requestUrl;
	/**
	 *请求uri
	 */
	private String requestUri;
	/**
	 *请求的参数
	 */
	private String parameters;
	/**
	 *预期结果
	 */
	private String expectResult;
	/**
	 *实际结果
	 */
	private String actualResult;

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getExpectResult() {
		return expectResult;
	}

	public void setExpectResult(String expectResult) {
		this.expectResult = expectResult;
	}

	public String getActualResult() {
		return actualResult;
	}

	public void setActualResult(String actualResult) {
		this.actualResult = actualResult;
	}
}
