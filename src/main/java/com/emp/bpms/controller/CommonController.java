package com.emp.bpms.controller;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.emp.bpms.repository.equipment.EquipmentDAO;
import com.emp.bpms.repository.reply.ReplyDAO;
import com.emp.bpms.repository.reqform.ReqformDAO;
import com.emp.bpms.repository.requestform.RequestFormDAO;
import com.emp.bpms.repository.team.TeamDAO;
import com.emp.bpms.repository.ticket.TicketDAO;
import com.emp.bpms.repository.user.UserService;
import com.emp.bpms.util.FileManager;
import com.skt.bpms.StaticFlag;
import com.skt.bpms.StaticFlag.FileType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CommonController implements ErrorController {
 
	@Autowired
	private TicketDAO 		ticketDAO;	
	
	@Autowired
	private ReplyDAO 		replyDAO;
	
	@Autowired
	private TeamDAO 		teamDAO;
	
	@Autowired
	private EquipmentDAO 	equipmentDAO;
	@Autowired
	private ReqformDAO 		reqformDAO;

	@Autowired
	private RequestFormDAO 		requestformDAO;	
	
	@Autowired
	UserService userService;
	
	public void setUserBasicAttribute(Model model) {
		//?ö∞?Ñ†?? ?ù¥Î¶? ?Ñ£?ñ¥Ï£ºÍ∏∞(Í∞ÅÏ¢Ö ?éò?ù¥Ïß??óê?Ñú ?ôï?ù∏?ö©?èÑ Î∞? Ï∞®ÌõÑ Í±∞Î•¥Í∏? ?úÑ?ïú ?ö©?èÑ)
		model.addAttribute("curUserName", userService.loadLoginUserDTO().getName());
	}

	
	@RequestMapping("/action/download.do")
	@ResponseBody
	public FileSystemResource fileDownloadAction(
			@RequestParam(value = "contentType", defaultValue = "", required=true) String contentType,
			@RequestParam(value = "contentId", defaultValue = "0", required=true) String contentId, 
			@RequestParam(value = "filename", defaultValue = "", required=false) String filename
			) {
		System.out.println('?');
		try {
			
			FileManager fm = new FileManager();
			
			switch(StaticFlag.FileType.getType(contentType)) {
				case TICKET		:	return new FileSystemResource(fm.getFile(ticketDAO.selectOneTicket(Integer.parseInt(contentId)), filename));
				case REPLY		:	return new FileSystemResource(fm.getFile(replyDAO.selectOneReplyById(Integer.parseInt(contentId)), filename));
				case REQUESTFORM:	return new FileSystemResource(fm.getFile(requestformDAO.selectOneRequestForm(Integer.parseInt(contentId)), filename));
				default:
				//?ò§Î•òÏùº?ïå
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
    @RequestMapping("/")
    public String pageDefault() {
    	log.info(userService.loadLoginUserDTO().getEmail());
        return "redirect:ticket/summary";
    }
    
    @RequestMapping("/main")
    public String mainPage(Model model) {
    	setUserBasicAttribute(model);
        return "pages/main";
    }
    
    @RequestMapping("/help")
    public String helpPage(Model model) {
    	setUserBasicAttribute(model);
        return "pages/help";
    }
    
    @RequestMapping("/userinfo")
    public String userinfoPage(Model model) {
    	setUserBasicAttribute(model);
        return "pages/skt/user/userinfo";
    }
    
    @RequestMapping("/login")
    public String loginPage() {
        return "pages/login";
    }
    
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
