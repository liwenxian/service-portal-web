package com.ai.paas.ipaas.console.idps;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.paas.ipaas.system.constants.Constants;
import com.ai.paas.ipaas.system.util.UserUtil;
import com.ai.paas.ipaas.user.dubbo.interfaces.IDssConsoleDubboSv;
import com.ai.paas.ipaas.user.dubbo.interfaces.IIdpsConsoleDubboSv;
import com.ai.paas.ipaas.user.dubbo.interfaces.IProdProductDubboSv;
import com.ai.paas.ipaas.user.dubbo.interfaces.ISysParamDubbo;
import com.ai.paas.ipaas.user.dubbo.vo.ProdProductVo;
import com.ai.paas.ipaas.user.dubbo.vo.SelectWithNoPageRequest;
import com.ai.paas.ipaas.user.dubbo.vo.SelectWithNoPageResponse;
import com.ai.paas.ipaas.user.dubbo.vo.UserProdInstVo;
import com.ai.paas.ipaas.user.vo.UserInfoVo;
import com.alibaba.dubbo.config.annotation.Reference;

/**
 * DSS用户控制台
 * 
 * @author mapl
 * 
 */

@RequestMapping(value = "/idpsConsole")
@Controller
public class UserIdpsConsoleController {

	private static final Logger logger = LogManager
			.getLogger(UserIdpsConsoleController.class.getName());
	@Reference
	private ISysParamDubbo iSysParam;
 
	
	@Reference
	private IProdProductDubboSv prodProductDubboSv;
	
	@Reference
	private IIdpsConsoleDubboSv idpsConsoleDubboSv;
	
	@RequestMapping(value = "/consoleIndex")
	public String consoleIndex(HttpServletRequest req,
			HttpServletResponse resp) {

		return "console/dss/index";

	}

	@RequestMapping(value = "/toIdpsConsole")
	public String toManageConsole(HttpServletRequest req,
			HttpServletResponse resp) {
		String indexFlag=req.getParameter("indexFlag");
		req.setAttribute("indexFlag", indexFlag);
		Map<String, Object> result = new HashMap<String, Object>();		
		try {
			
			SelectWithNoPageResponse<ProdProductVo> prodProductVoresponse = null;
			SelectWithNoPageRequest<ProdProductVo> prodProductVoRequest = new SelectWithNoPageRequest<ProdProductVo>();
			ProdProductVo  prodProductVo = new ProdProductVo();
			short prodId = Constants.serviceType.IDPS_CENTER;
			prodProductVo.setProdId(prodId);
			prodProductVoRequest.setSelectRequestVo(prodProductVo);
			prodProductVoresponse = prodProductDubboSv.selectProduct(prodProductVoRequest);
			prodProductVo = prodProductVoresponse.getResultList().get(0);
			req.setAttribute("prodName", prodProductVo.getProdName());
			
		} catch (Exception e) {
			result.put("resultCode", Constants.OPERATE_CODE_FAIL);
			result.put("resultMessage", "查询出现异常！");
			logger.error(e);

		}
		return "console/idps/idpsConsole";
	}

	/**
	 * Idps列表查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryIdpsList")
	@ResponseBody
	public Map<String, Object> queryIdpsList(HttpServletRequest req,
			HttpServletResponse resp) {

		Map<String, Object> result = new HashMap<String, Object>();
		SelectWithNoPageResponse<UserProdInstVo> response = null;
		try {
			SelectWithNoPageRequest<UserProdInstVo> selectWithNoPageRequest = new SelectWithNoPageRequest<UserProdInstVo>();
			UserProdInstVo vo = new UserProdInstVo();
			UserInfoVo userVo = UserUtil.getUserSession(req.getSession());
			vo.setUserId(userVo.getUserId()); // 用户Id
			String prodId = String.valueOf(Constants.serviceType.IDPS_CENTER);
			vo.setUserServiceId(prodId);
			selectWithNoPageRequest.setSelectRequestVo(vo);
			response = idpsConsoleDubboSv.selectUserProdInsts(selectWithNoPageRequest);
						
			result.put("resultCode", response.getResponseHeader().getResultCode());
			result.put("resultMessage", response.getResponseHeader().getResultMessage());
			result.put("resultList", response.getResultList());			
			
			
		} catch (Exception e) {
			result.put("resultCode", Constants.OPERATE_CODE_FAIL);
			result.put("resultMessage", "查询出现异常！");
			logger.error(e);

		}

		return result;
	}

}
