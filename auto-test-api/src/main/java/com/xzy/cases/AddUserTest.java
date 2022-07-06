package com.xzy.cases;
import com.xzy.common.HttpUtil;
import com.xzy.config.BaseConfig;
import com.xzy.config.UserClient;
import com.xzy.model.AddUserCase;
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

public class AddUserTest extends BaseTest{
    private static Logger logger = Logger.getLogger(AddUserTest.class);

    @DataProvider(name="data")
    public Object[][] providerData() throws IOException {
        logger.info("开始加载db数据>AddUserCase表");
        SqlSession session = getSqlSession(BaseConfig.CASE_DB);
        List<AddUserCase> addUserCaseList = session.selectList(BaseConfig.selectAllAddUserCase);
        if(addUserCaseList!=null&&addUserCaseList.size()>0){
            AddUserCase[][] addUserCases = new AddUserCase[addUserCaseList.size()][1];
            for(int i = 0;i<addUserCases.length;i++){
                addUserCases[i][0] = addUserCaseList.get(i);
            }
            return addUserCases;
        }else{
            logger.error("加载db数据异常：AddUserCase表数据为空！");
            throw new RuntimeException("加载db数据异常：AddUserCase表数据为空！");
        }
    }

    @Test(dependsOnGroups = "loginTrue",dataProvider = "data",description = "添加用户接口测试")
    public void addUser(AddUserCase[] addUserCases) throws IOException {
        AddUserCase addUserCase = addUserCases[0];
        HttpResponse response = HttpUtil.sentPost(UserClient.httpClient,getUrl(BaseConfig.ADDUSER_URI),getDefaultHeader(),getParams(addUserCase));
        String result = EntityUtils.toString(response.getEntity(),BaseConfig.DEFAULT_CHARSET);
        Assert.assertEquals(result,addUserCase.getExpected());
    }

    private Map<String,Object> getParams(AddUserCase addUserCase){
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("userName",addUserCase.getUserName());
        params.put("password",addUserCase.getPassword());
        params.put("sex",addUserCase.getSex());
        params.put("age",addUserCase.getAge());
        params.put("permission",addUserCase.getPermission());
        params.put("isDelete",addUserCase.getIsDelete());
        return params;
    }

}
