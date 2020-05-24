package com.kang.common.constant;

/**
 * <p>Title: JobConstants</p> 
 * <p>Description: 读取配置文件xxl-job.properties</p> 
 * @author CK 
 * @date 2020年4月26日
*/
public class JobConstants {
	
	public static final String USERNAME = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.USERNAME);
	
	public static final String PASSWORD = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.PASSWORD);

	public static final String JOB_LOGIN = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.ADDRESSES) + "/login";
	
	public static final String JOB_ADD = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.ADDRESSES) + "/jobinfo/add";
	
	public static final String JOB_PAGE_LIST = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.ADDRESSES) + "/jobinfo/pageList";

	public static final String JOB_TRIGGER = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.ADDRESSES) + "/jobinfo/trigger";
	
	public static final String JOB_START = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.ADDRESSES) + "/jobinfo/start";
	
	public static final String JOB_STOP = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.ADDRESSES) + "/jobinfo/stop";
	
	public static final String JOB_DELETE = PropConstants.getProp_2(PropConstants.XXL_JOB, PropConstants.jobKey.ADDRESSES) + "/jobinfo/remove";
}
