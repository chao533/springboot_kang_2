package com.chao.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.kang.common.constant.JobConstants;
import com.kang.model.job.XxlJobInfo;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

public class JobTest {
	
	/**
	 * <p>Title: testAddJob</p>
	 * <p>Description: 添加任务</p>
	 * @param
	 */
	@Test
	public void testAddJob() {
		Map<String,Object> formMap = MapUtil.builder(new HashMap<String,Object>())
			.put("jobGroup", 1)
			.put("jobDesc", "测试任务2")
			.put("executorRouteStrategy", "FIRST")
			.put("jobCron", "0 0 0 * * ? *")
			.put("glueType", "BEAN")
			.put("executorHandler", "demoJobHandler")
			.put("executorBlockStrategy", "SERIAL_EXECUTION")
			//.put("childJobId", 1)
			.put("executorTimeout", 0)
			.put("executorFailRetryCount", 0)
			.put("author", "ck")
			.put("alarmEmail", "")
			.put("executorParam", "{\"username\":\"king\",\"password\":\"123456\"}")
			.put("glueRemark", "GLUE代码初始化")
			.put("glueSource", "")
			.build();
		
		HttpResponse httpResponse = HttpRequest.post(JobConstants.JOB_ADD)
			.contentType("application/x-www-form-urlencoded; charset=UTF-8")
			.header("cache-control", "no-cache")
			.cookie(toLogin())
			.form(formMap)
			.execute();
		
		Console.log(httpResponse.getStatus());
		Console.log(httpResponse.isOk());
		Console.log(httpResponse.body());
	}
	
	/**
	 * <p>Title: testPageList</p>
	 * <p>Description: 查看任务列表</p>
	 * @param
	 */
	@Test
	public void testPageList() {
		Map<String,Object> paramMap = Dict.create().set("jobGroup", 1).set("start", 0).set("length", 10);
		HttpResponse httpResponse = HttpRequest.post(JobConstants.JOB_PAGE_LIST)
				.contentType("application/x-www-form-urlencoded; charset=UTF-8")
				.cookie(toLogin())
				.form(paramMap)
				.execute();
		Console.log("httpResponse:{}",httpResponse);
		Map<String,Object> bodyMap = JSONUtil.toBean(httpResponse.body(), new TypeReference<Map<String,Object>>() {}, true);
		List<XxlJobInfo> list = MapUtil.get(bodyMap, "data", new TypeReference<List<XxlJobInfo>>() {});
		Console.log("list:{}",list);
	}
	
	
	/**
	 * <p>Title: toLogin</p>
	 * <p>Description: xxl-job登录</p>
	 * @param @return
	 */
	public String toLogin() {
		Map<String,Object> paramMap = Dict.create().set("userName", JobConstants.USERNAME).set("password", JobConstants.PASSWORD);
		HttpResponse httpResponse = HttpRequest.post(JobConstants.JOB_LOGIN)
				.form(paramMap)
				.execute();
		if(httpResponse.getStatus() == 200) {
			Console.log(httpResponse.getCookieStr());
			return httpResponse.getCookieStr();
		}
		return null;
	}
	
	@Test
	public void testStart() {
		Map<String,Object> paramMap = Dict.create().set("id", 3).set("executorParam", "{\"username\":\"king\",\"password\":\"123456\"}");
		HttpResponse httpResponse = HttpRequest.post(JobConstants.JOB_TRIGGER)
				.contentType("application/x-www-form-urlencoded; charset=UTF-8")
				.cookie(toLogin())
				.form(paramMap)
				.execute();
		Console.log(httpResponse.isOk());
	}
	
}

