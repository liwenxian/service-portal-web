package com.ai.paas.ipaas.maintain.controller;

import com.ai.paas.ipaas.maintain.constants.CCSComponentOperationParam;
import com.ai.paas.ipaas.maintain.constants.PathType;
import com.ai.paas.ipaas.maintain.model.UserIdModel;
import com.ai.paas.ipaas.maintain.util.ConfigUtil;
import com.ai.paas.ipaas.maintain.util.HttpClientUtil;
import com.ai.paas.ipaas.maintain.util.HttpRequestUtil;
import com.ai.paas.ipaas.maintain.util.HttpResponseUtil;
import com.google.gson.Gson;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/config/maintain")
public class ConfigMaintain {
	
    private final String url = ConfigUtil.getProperty("ipaas.rest.url");

    @RequestMapping("/main")
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        String userName = request.getParameter("name");
        String result = "";
        try{
        	result =  HttpRequestUtil.sendPost(
        			ConfigUtil.getProperty("ipaas.user.getUserID.rest.url"),
					"userName="+userName);
        	System.out.println("***********************"+result);
        }catch(Exception e){
        	e.printStackTrace();
        	mav.setViewName("config/error");
        	return mav;
        }
        Gson gson = new Gson();
        UserIdModel userIdModel = gson.fromJson(result, UserIdModel.class);
        request.getSession().setAttribute("userId", userIdModel.getUserId());
        //session.setAttribute("userId", "953C42783ABB441A8568471B9FA19EA4");
        mav.setViewName("config/main");
        return mav;
    }

    @RequestMapping("/listSubPath")
    public void getChildren(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        HttpSession session = request.getSession();
    	String userId = String.valueOf(session.getAttribute("userId"));
        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setUserId(userId);
        param.setPath(request.getParameter("path"));
        param.setPathType(PathType.convertPathType(Integer.valueOf(request.getParameter("flags"))));
        String result = HttpClientUtil.sendPostRequest(url + "/ccs-component/maintain/listSubPathAndData", new Gson().toJson(param));
        HttpResponseUtil.sendResponse(result, response);
    }
    
    /**
     * 用户内部配置--获取，前台只传path数据
     *
     * @return
     */
    @RequestMapping(value = "/get")
    public void get(HttpServletRequest request, HttpServletResponse response) {
    	
    	HttpSession session = request.getSession();
    	String userId = String.valueOf(session.getAttribute("userId"));
        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setUserId(userId);
        param.setPath(request.getParameter("path"));
        param.setPathType(PathType.convertPathType(Integer.valueOf(request.getParameter("flags"))));
        String result;
		try {
			result = HttpClientUtil.sendPostRequest(url + "/ccs-component/maintain/get", new Gson().toJson(param));
			HttpResponseUtil.sendResponse(result, response);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
        
 
    }
    
    /**
     * 用户内部配置--修改，前台只传path、data数据
     *
     * @return
     */
    @RequestMapping(value = "/modify")
    public void modify(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	String userId = String.valueOf(session.getAttribute("userId"));
        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setUserId(userId);
        param.setPath(request.getParameter("path"));
        param.setPathType(PathType.convertPathType(Integer.valueOf(request.getParameter("flags"))));
        param.setData(request.getParameter("data"));
        String result;
		try {
			result = HttpClientUtil.sendPostRequest(url + "/ccs-component/maintain/modify", new Gson().toJson(param));
			HttpResponseUtil.sendResponse(result, response);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 用户内部配置--删除，前台只传path、data数据
     *
     * @return
     */
    @RequestMapping(value = "/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	String userId = String.valueOf(session.getAttribute("userId"));
        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setUserId(userId);
        param.setPath(request.getParameter("path"));
        param.setPathType(PathType.convertPathType(Integer.valueOf(request.getParameter("flags"))));
        String result;
		try {
			result = HttpClientUtil.sendPostRequest(url + "/ccs-component/maintain/delete", new Gson().toJson(param));
			HttpResponseUtil.sendResponse(result, response);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 用户内部配置--增加，前台只传path、data数据
     *
     * @return
     */
    @RequestMapping(value = "/add")
    public void add(HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();
    	String userId = String.valueOf(session.getAttribute("userId"));
        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setUserId(userId);
        param.setPath(request.getParameter("path"));
        param.setPathType(PathType.convertPathType(Integer.valueOf(request.getParameter("flags"))));
        param.setData(request.getParameter("data"));
        String result;
		try {
			result = HttpClientUtil.sendPostRequest(url + "/ccs-component/maintain/add", new Gson().toJson(param));
			HttpResponseUtil.sendResponse(result, response);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
    }
    
}
