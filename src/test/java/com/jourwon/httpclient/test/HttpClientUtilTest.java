package com.jourwon.httpclient.test;

import com.jourwon.httpclient.util.HttpClientUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * description: httpClientUtil测试类
 * @author husky
 * @date 2019/9/5 11:55
 */
public class HttpClientUtilTest {
    /**
     * 测试get
     * */
    @Test
    public void testGet() throws Exception {
        String appId = "appId";
        String secret = "secret";
        Map<String,String> headers = new HashMap<String,String>(16);
        headers.put("Accept", "application/json");
        Map<String,String> params = new HashMap<String,String>(16);
        params.put("grant_type","client_credential");
        params.put("appid",appId);
        params.put("secret",secret);
        System.out.println(HttpClientUtil.doGet("https://api.weixin.qq.com/cgi-bin/token",headers,params));
    }
    /**
     * 测试formPost
     * */
    @Test
    public void testFormPost() throws Exception{
        Map<String,String> params = new HashMap<>(16);
        params.put("code","74740");
        params.put("message","formMessage");
        System.out.println(HttpClientUtil.doFormPost("http://127.0.0.1:8080/hello/postFormWithParam",null,params));
    }

    /**
     * 发送json post
     * */
    @Test
    public void testJsonPost() throws Exception{
        Map<String,String> params = new HashMap<>(16);
        params.put("userName","huskyui");
        params.put("password","123456");
        System.out.println(HttpClientUtil.doJsonPost("http://127.0.0.1:8080/hello/login",null,params));
    }

}
