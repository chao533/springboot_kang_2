package com.kang.service.impl;

import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.kang.common.msg.ErrorCode;
import com.kang.common.msg.Message;
import com.kang.config.fdfs.FdfsConfig;
import com.kang.service.UploadService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService{
	
	@Autowired
	private FastFileStorageClient storageClient;
	@Autowired
	private FdfsConfig fastConfig;

	@SneakyThrows
	@Override
	public Message<?> uploadFile(HttpServletRequest request, String fileKey) {
		List<MultipartFile> multipartFileList = this.getFileList(request, fileKey);
		List<String> resultList = CollUtil.newArrayList();
		for(MultipartFile multipartFile : multipartFileList) {
			@Cleanup InputStream inputStream = multipartFile.getInputStream();
			String extName = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			StorePath storePath = storageClient.uploadFile(inputStream,multipartFile.getSize(), extName,null);
			String fullPath = storePath.getFullPath();
			if(StringUtils.isNotBlank(fullPath)) {
				resultList.add(fastConfig.getFullPush(fullPath));
			}
		}
		log.info("result:" + resultList);
		return new Message<>(ErrorCode.SUCCESS,resultList);
	}
	
	private List<MultipartFile> getFileList(HttpServletRequest request,String key){
		//创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResovler = new CommonsMultipartResolver();
		//判断 request 是否有文件上传,即多部分请求
		if (!multipartResovler.isMultipart(request)) {
			//无附件上传
			return null;
		}
		// 转型为MultipartHttpRequest：
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得文件：
		List<MultipartFile> files = multipartRequest.getFiles(key);
		return files;
	}

	@SneakyThrows
	@Override
	public Message<?> uploadCutPic(HttpServletRequest request, String fileKey, int x, int y, int width,int height) {
		List<MultipartFile> multipartFileList = this.getFileList(request, fileKey);
		List<String> resultList = CollUtil.newArrayList();
		for(MultipartFile multipartFile : multipartFileList) {
			
			@Cleanup ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImgUtil.cut(multipartFile.getInputStream(), output, new Rectangle(x,y,width,height));
			
			@Cleanup ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
			String extName = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			StorePath storePath = storageClient.uploadFile(input,output.size(), extName,null);
			String fullPath = storePath.getFullPath();
			if(StringUtils.isNotBlank(fullPath)) {
				resultList.add(fastConfig.getFullPush(fullPath));
			}
		}
		log.info("result:" + resultList);
		return new Message<>(ErrorCode.SUCCESS,resultList);
	}

	@SneakyThrows
	@Override
	public Message<String> uploadCutScale(HttpServletRequest request, String fileKey, float scale) {
		List<MultipartFile> multipartFileList = this.getFileList(request, fileKey);
		String result = "";
		for(MultipartFile multipartFile : multipartFileList) {
			
			@Cleanup ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImgUtil.scale(multipartFile.getInputStream(), output, scale);
			
			@Cleanup ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
			String extName = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			StorePath storePath = storageClient.uploadFile(input,output.size(), extName,null);
			String fullPath = storePath.getFullPath();
			if(StringUtils.isNotBlank(fullPath)) {
				result += fastConfig.getFullPush(fullPath) + ",";
			}
		}
		if(StringUtils.isNotBlank(result)) {
			result = result.substring(0, result.length() - 1);
		}
		log.info("result:" + result);
		return new Message<>(ErrorCode.SUCCESS,result);
	}

	@SneakyThrows(value = IOException.class)
	@Override
	public void downloadFile_HuTool(HttpServletResponse response, String path) {
//		//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
		response.setContentType("multipart/form-data");
//		 //2.设置文件头：
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("测试文件." + FileUtil.extName(path), "UTF-8"));//此处需要设置下载文件的默认名称
		
		HttpUtil.download(path, response.getOutputStream(), true,  new StreamProgress(){

		    @Override
		    public void start() {
		        Console.log("开始下载。。。。");
		    }

		    @Override
		    public void progress(long progressSize) {
		        Console.log("已下载：{}", FileUtil.readableFileSize(progressSize));
		    }

		    @Override
		    public void finish() {
		        Console.log("下载完成！");
		    }
		});
	}
	
	@SneakyThrows(value = IOException.class)
	@Override
	public void downloadFile(HttpServletResponse response, String path) {
		StorePath storePath = StorePath.parseFromUrl(path);
		@Cleanup InputStream inputStream = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), (input) -> input);
		//1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
		response.setContentType("multipart/form-data");
		//2.设置文件头：
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("测试文件." + FileUtil.extName(path), "UTF-8"));//此处需要设置下载文件的默认名称
		@Cleanup ServletOutputStream outputStream = response.getOutputStream();
		IOUtils.copy(inputStream, outputStream);
	}
	

	@Override
	public Message<String> deleteFile(String path) {
		storageClient.deleteFile(path);
		return new Message<>(ErrorCode.SUCCESS);
	}

	
}
