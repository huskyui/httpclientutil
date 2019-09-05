package com.jourwon.httpclient.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description:httpclient工具类
 * @author husky
 * @date 2019/9/5 11:35
 */
public class HttpClientUtil {
    /**
     * ENCODING 编码格式。发送编码格式统一用UTF-8
     * */
    private static final String ENCODING = "UTF-8";

    /**
     * CONNECT_TIMEOUT 设置连接超时时间，单位毫秒。
     * */
    private static final int CONNECT_TIMEOUT = 6000;

    /**
     * SOCKET_TIMEOUT 请求获取数据的超时时间(即响应时间)，单位毫秒。
     */
    private static final int SOCKET_TIMEOUT = 6000;

    /**
     * 发送get请求:带请求头和请求参数
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数集合
     * @return JSONObject
     * @throws Exception
     */
    public static JSONObject doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        //创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = uriBuilder.build();
        HttpGet httpGet = new HttpGet(uri);
        //设置参数
        httpGet.setConfig(RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build());
        //设置请求头
        packageHeader(headers,httpGet);
        CloseableHttpResponse httpResponse = null;
        try {
            return getResponse(httpResponse,httpClient,httpGet);
        }finally {
            release(httpResponse,httpClient);
        }
    }

    /**
     * description 表单方式的post
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @return JSONObject
     * @throws Exception
     */
    public static JSONObject doFormPost(String url,Map<String, String> headers, Map<String, String> params) throws Exception{
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build());
        //封装请求参数
        packageHeader(headers,httpPost);
        //封装请求头
        packageFormParam(params,httpPost);
        CloseableHttpResponse httpResponse = null;
        try{
            return getResponse(httpResponse,httpClient,httpPost);
        }finally {
            release(httpResponse,httpClient);
        }
    }

    /**
     * description 表单方式的post
     * @param url 请求头
     * @param headers 请求头
     * @param params 请求参数
     * @return JSONObject
     * @throws Exception
     */
    public static JSONObject doJsonPost(String url,Map<String, String> headers, Map<String, String> params) throws Exception{
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build());
        //封装请求参数
        packageHeader(headers,httpPost);
        //封装请求头
        packageJsonParam(params,httpPost);
        CloseableHttpResponse httpResponse = null;
        try{
            return getResponse(httpResponse,httpClient,httpPost);
        }finally {
            release(httpResponse,httpClient);
        }
    }





    /**
     * Description: 封装请求头
     * @param params
     * @param httpMethod
     */
    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 封装表单请求参数
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    public static void packageFormParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
        }
    }

    /**
     * Description: 封装json请求参数
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    public static void packageJsonParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            JSONObject jsonParam = new JSONObject();
            Set<Map.Entry<String,String>> entrySet = params.entrySet();
            for(Map.Entry<String,String>  entry : entrySet){
                jsonParam.put(entry.getKey(),entry.getValue());
            }
            StringEntity entity = new StringEntity(jsonParam.toString(),ENCODING);
            entity.setContentEncoding(ENCODING);
            entity.setContentType("application/json");
            // 设置到请求的http对象中
            httpMethod.setEntity(entity);
        }
    }


    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

    /**
     * Description: 获得响应结果
     *
     * @param httpResponse
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static JSONObject getResponse(CloseableHttpResponse httpResponse,
                                                       CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        // 执行请求
        httpResponse = httpClient.execute(httpMethod);

        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            StringBuffer content = new StringBuffer();
            if (httpResponse.getEntity() != null) {
                content.append(EntityUtils.toString(httpResponse.getEntity(), ENCODING));
            }
            return JSON.parseObject(content.toString());
        }
        JSONObject data = new JSONObject();
        data.put("code","999");
        data.put("content","网络异常");
        return data;
    }


}
