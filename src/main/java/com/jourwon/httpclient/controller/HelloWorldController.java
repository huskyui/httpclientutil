package com.jourwon.httpclient.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

/**
 * Description: get和post请求测试controller
 * 
 * @author JourWon
 * @date Created on 2018年4月19日
 */
@RestController
@RequestMapping("/hello")
public class HelloWorldController {

	@GetMapping("/getWithParam")
	public String getWithParam(@RequestParam String message) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message",message);
		jsonObject.put("response","hello,end");
		return jsonObject.toJSONString();
	}

	@PostMapping("/postFormWithParam")
	public String postFormWithParam(@RequestParam String code, @RequestParam String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",message);
        jsonObject.put("code",code);
        jsonObject.put("response","hello,end");
		return jsonObject.toJSONString();
	}

	@PostMapping("/login")
    public String postJsonWithParam(@RequestBody JSONObject request){
	    String userName = request.getString("userName");
	    String password = request.getString("password");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName",userName);
        jsonObject.put("password",password);
        jsonObject.put("response","hello,end");
        return jsonObject.toJSONString();
    }

}
