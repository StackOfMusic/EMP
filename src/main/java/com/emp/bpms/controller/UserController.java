package com.emp.bpms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.emp.bpms.repository.reply.ReplyDAO;
import com.emp.bpms.repository.team.TeamDAO;
import com.emp.bpms.repository.ticket.TicketDAO;
import com.emp.bpms.repository.user.UserDAO;
import com.emp.bpms.repository.user.UserDTO;
import com.emp.bpms.repository.user.UserService;
import com.skt.bpms.StaticFlag;
import com.skt.bpms.repository.equipment.EquipmentDAO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {

	public static String uriReturn(String[] uriSet) {
		return uriSet[0];
	}
	
	@Autowired
	private UserDAO 		userDAO;	

	@Autowired
	private TicketDAO 		ticketDAO;	
	
	@Autowired
	private ReplyDAO 		replyDAO;
	
	@Autowired
	private TeamDAO 		teamDAO;
	
	@Autowired
	private EquipmentDAO 	equipmentDAO;

	@Autowired
	private UserService 	userService;
	
    @Autowired
    public JavaMailSender 	emailSender;
    
	private static String[] userinfoPageURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/user/userinfo",
			null,										//BP ?ö¥?ö©?ûê
			null										//Administrator
		};
	
	@RequestMapping("/user/userinfo")
    public String userinfoPage(Model model) {
    	setUserBasicAttribute(model);
    	//?Ñ∏?ÖòÏ≤¥ÌÅ¨
    	//model.addAttribute("ticketList", ticketDAO.selectAllList());
    	//model.addAttribute("teamList", teamDAO.selectAllList());
    	//model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return uriReturn(userinfoPageURI);
    }
    
    @RequestMapping("/user/info")
    public String userPage(Model model) {
       
    	List<UserDTO> userDTOList;
        userDTOList = userDAO.selectAllUser();
        log.info(userDTOList.toString());
        /*
        UserDTO userDTO = new UserDTO();
        userDTO.setName("ccc");
        userRepository.insertUser(userDTO);
        userDTOList = userRepository.selectAllUser();
        log.info(userDTOList.toString());
        
        userDTO.setId(3)
            .setName("ddd");
        userRepository.updateUser(userDTO);
        userDTOList = userRepository.selectAllUser();
        log.info(userDTOList.toString());
 
        userRepository.deleteUser(5);
        userDTOList = userRepository.selectAllUser();
        log.info(userDTOList.toString());
        */
        
        model.addAttribute("user", userDTOList.get(0));
        return "pages/user";
    }
    
	public void setUserBasicAttribute(Model model) {
		//?ö∞?Ñ†?? ?ù¥Î¶? ?Ñ£?ñ¥Ï£ºÍ∏∞(Í∞ÅÏ¢Ö ?éò?ù¥Ïß??óê?Ñú ?ôï?ù∏?ö©?èÑ Î∞? Ï∞®ÌõÑ Í±∞Î•¥Í∏? ?úÑ?ïú ?ö©?èÑ)
		model.addAttribute("curUserName", userService.loadLoginUserDTO().getName());
	}
}
