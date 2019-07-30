package com.emp.bpms.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emp.bpms.repository.reply.ReplyDAO;
import com.emp.bpms.repository.reqform.ReqformDAO;
import com.emp.bpms.repository.requestform.RequestFormDAO;
import com.emp.bpms.repository.team.TeamDAO;
import com.emp.bpms.repository.team.TeamDTO;
import com.emp.bpms.repository.ticket.TicketDAO;
import com.emp.bpms.repository.ticket.TicketDTO;
import com.emp.bpms.repository.user.UserDAO;
import com.emp.bpms.repository.user.UserDTO;
import com.emp.bpms.repository.user.UserService;
import com.skt.bpms.StaticFlag;
import com.skt.bpms.repository.equipment.EquipmentDAO;
import com.skt.bpms.repository.equipment.EquipmentDTO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class ManagementController {

	public static String uriReturn(String[] uriSet) {
		return uriSet[0];
	}

	@Autowired
	private RequestFormDAO 		requestformDAO;	
	
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
	private ReqformDAO 		reqformDAO;
	
	@Autowired
	private UserService 	userService;
	
    @Autowired
    public JavaMailSender 	emailSender;
    
	private static String[] requestFormPageURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/management/requestForm",
			null,										//BP ?ö¥?ö©?ûê
			null										//Administrator
		};
	
	private static String[] teamPageURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/management/team",
			null,										//BP ?ö¥?ö©?ûê
			null										//Administrator
		};
	
	private static String[] equipmentPageURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/management/equipment",
			null,										//BP ?ö¥?ö©?ûê
			null										//Administrator
		};
	
	private static String[] categoryPageURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/management/category",
			null,										//BP ?ö¥?ö©?ûê
			null										//Administrator
		};
	

	private static String[] requestFormDetailModuleURI 		= 
		{
			StaticFlag.defaultModuleURI + "/skt/management/requestFormDetail",
			null,										//BP ?ö¥?ö©?ûê
			null										//Administrator
		};
		
	
	private static String quickIssuingModuleURI 	= StaticFlag.defaultModuleURI + "/skt/ticket/quickIssuing";	
	private static String requestFormModuleURI		= StaticFlag.defaultModuleURI + "/skt/ticket/requestForm";
	private static String replyListModuleURI 		= StaticFlag.defaultModuleURI + "/skt/ticket/replyList";
	private static String ticketListModuleURI 		= StaticFlag.defaultModuleURI + "/skt/ticket/ticketList";
	private static String equipmentSelectModuleURI	= StaticFlag.defaultModuleURI + "/skt/ticket/equipmentSelect";
	private static String ticketDetailModuleURI		= StaticFlag.defaultModuleURI + "/skt/ticket/ticketDetail";
	private static String requestFormListModuleURI	= StaticFlag.defaultModuleURI + "/skt/management/requestFormList";
	private static String fileuploadModuleURI	= StaticFlag.defaultModuleURI + "/skt/management/fileupload";
	private static String equipmentListModuleURI	= StaticFlag.defaultModuleURI + "/skt/management/equipmentList";
	private static String equipmentUpdateListModuleURI	= StaticFlag.defaultModuleURI + "/skt/management/equipmentUpdateList";
	private static String equipmentDetailModuleURI	= StaticFlag.defaultModuleURI + "/skt/management/equipmentDetail";
	private static String teamListModuleURI	= StaticFlag.defaultModuleURI + "/skt/management/teamList";
	private static String teamDetailModuleURI	= StaticFlag.defaultModuleURI + "/skt/management/teamDetail";
	private static String teamUpdateListModuleURI	= StaticFlag.defaultModuleURI + "/skt/management/teamUpdateList";

	
	public void setUserBasicAttribute(Model model) {
		//?ö∞?Ñ†?? ?ù¥Î¶? ?Ñ£?ñ¥Ï£ºÍ∏∞(Í∞ÅÏ¢Ö ?éò?ù¥Ïß??óê?Ñú ?ôï?ù∏?ö©?èÑ Î∞? Ï∞®ÌõÑ Í±∞Î•¥Í∏? ?úÑ?ïú ?ö©?èÑ)
		model.addAttribute("curUserName", userService.loadLoginUserDTO().getName());
	}
	

	@RequestMapping("/management/testRequestFormList")
    public String testRequesFormListPage(Model model) {
    	setUserBasicAttribute(model);
    	//?Ñ∏?ÖòÏ≤¥ÌÅ¨
    	//model.addAttribute("ticketList", ticketDAO.selectAllList());
    	//model.addAttribute("teamList", teamDAO.selectAllList());
    	//model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	model.addAttribute("reqformList", reqformDAO.selectAllList());
        return StaticFlag.defaultPageURI + "/management/testRequestFormList";
    }

	
	@RequestMapping("/management/requestForm")
    public String requesFormPage(Model model) {
    	setUserBasicAttribute(model);
    	//?Ñ∏?ÖòÏ≤¥ÌÅ¨
    	//model.addAttribute("ticketList", ticketDAO.selectAllList());
    	//model.addAttribute("teamList", teamDAO.selectAllList());
    	//model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return uriReturn(requestFormPageURI);
    }

	@RequestMapping("/management/requestFormDetail")
    public String requesFormDetailModule(Model model) {
    	setUserBasicAttribute(model);
    	//?Ñ∏?ÖòÏ≤¥ÌÅ¨
    	//model.addAttribute("ticketList", ticketDAO.selectAllList());
    	//model.addAttribute("teamList", teamDAO.selectAllList());
    	//model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	model.addAttribute("reqformList", reqformDAO.selectAllList());
        return uriReturn(requestFormDetailModuleURI);
    }
	
	
	@RequestMapping("/management/team")
    public String teamPage(Model model) {
    	setUserBasicAttribute(model);
    	//?Ñ∏?ÖòÏ≤¥ÌÅ¨
    	//model.addAttribute("ticketList", ticketDAO.selectAllList());
    	//model.addAttribute("teamList", teamDAO.selectAllList());
    	//model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return uriReturn(teamPageURI);
    }
	
	@RequestMapping("/management/equipment")
    public String equipmentPage(Model model) {
    	setUserBasicAttribute(model);
    	//?Ñ∏?ÖòÏ≤¥ÌÅ¨
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return uriReturn(equipmentPageURI);
    }
	

	@RequestMapping("/management/category")
    public String categoryPage(Model model) {
    	setUserBasicAttribute(model);
    	//?Ñ∏?ÖòÏ≤¥ÌÅ¨
    	//model.addAttribute("ticketList", ticketDAO.selectAllList());
    	//model.addAttribute("teamList", teamDAO.selectAllList());
    	//model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return uriReturn(categoryPageURI);
    }
	
    @RequestMapping("/management/requestFormList")
    public String ticketList(Model model) {
    	model.addAttribute("requestFormList", requestformDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return requestFormListModuleURI;

    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)}

    @RequestMapping("/management/teamList")
    public String teamList(Model model) {
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return teamListModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)}
    
    @RequestMapping("/management/teamDetail")
    public String teamDetail(@RequestParam int team_id,Model model) {
    	model.addAttribute("team_id", team_id);
    	model.addAttribute("teamData",teamDAO.selectTeamIdByTeamId(team_id));
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return teamDetailModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)}

    @RequestMapping("/management/teamUpdateList")
    public String teamUpdateList(Model model) {
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return teamUpdateListModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)}
    
    @RequestMapping("/management/equipmentList")
    public String equipmentList(Model model) {
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return equipmentListModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)}

    @RequestMapping("/management/equipmentUpdateList")
    public String equipmentUpdateList(Model model) {
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return equipmentUpdateListModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)}
    
    @RequestMapping("/management/equipmentDetail")
    public String equipmentDetail(@RequestParam int equipment_id,Model model) {
    	model.addAttribute("equipment_id", equipment_id);
    	model.addAttribute("equipmentData",equipmentDAO.selectEquipmentById(equipment_id));
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return equipmentDetailModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)}
    
    @RequestMapping("/management/fileupload")
    public String fileupload(Model model) {
    	model.addAttribute("requestFormList", requestformDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return fileuploadModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)}
    
    @RequestMapping("/management/issuing.do") 
    public @ResponseBody Map<String, Object> quickIssuing(@RequestParam int equipment_id, @RequestParam String category1, @RequestParam String category2, @RequestParam String category3, @RequestParam int bp_user_id, Model model) {
    	TicketDTO ticketDTO = new TicketDTO();
		EquipmentDTO equipmentDTO = new EquipmentDTO();
    	//EquipmentDTO equipmentDTO = equipmentDAO.selectEquipmentById(equipment_id);
    	UserDTO reqUserDTO = userService.loadLoginUserDTO();
    	UserDTO recvUserDTO = userDAO.selectUserById(equipmentDTO.getManager_id());
    	System.out.println(equipment_id);
    	System.out.println(category1);
    	System.out.println(category2);
    	System.out.println(category3);
    	System.out.println(bp_user_id);
    	if(equipment_id==0) {
        	equipmentDTO.setCategory1(category1);
        	equipmentDTO.setCategory2(category2);
        	equipmentDTO.setCategory3(category3);
        	equipmentDTO.setBp_user_id(bp_user_id);
    		equipmentDAO.insertEquipment(equipmentDTO);
        	System.out.println("insert Í≥†Í≥†");
       	}
    	else {
        	System.out.println("?óÖ?ç∞?ù¥?ä∏ Í≥†Í≥†");
    		equipmentDTO.setEquipment_id(equipment_id);
        	System.out.println("111");
        	equipmentDTO.setCategory1(category1);
        	equipmentDTO.setCategory2(category2);
        	equipmentDTO.setCategory3(category3);
        	equipmentDTO.setBp_user_id(bp_user_id);
    		equipmentDAO.updateEquipment(equipmentDTO);
    	}
    	return null;
    }     
    
    @RequestMapping("/management/delete_table.do") 
    public @ResponseBody Map<String, Object> equipmentDelete(@RequestParam int equipment_id, Model model) {
    	
    	equipmentDAO.deleteEquipment(equipment_id);
    	
    	return null;
    }
    
    @RequestMapping("/management/team_issuing.do") 
    public @ResponseBody Map<String, Object> quickIssuing(@RequestParam int team_id, @RequestParam String type, @RequestParam String category1, @RequestParam String category2, @RequestParam String mail, Model model) {
    	TicketDTO ticketDTO = new TicketDTO();
		EquipmentDTO equipmentDTO = new EquipmentDTO();
		TeamDTO teamDTO = new TeamDTO();
    	//EquipmentDTO equipmentDTO = equipmentDAO.selectEquipmentById(equipment_id);
    	UserDTO reqUserDTO = userService.loadLoginUserDTO();
    	UserDTO recvUserDTO = userDAO.selectUserById(equipmentDTO.getManager_id());

    	if(team_id==0) {
        	teamDTO.setType(type);
        	teamDTO.setCategory1(category1);
        	teamDTO.setCategory2(category2);
        	teamDTO.setMail(mail);
        	teamDAO.insertTeam(teamDTO);
        	System.out.println("insert Í≥†Í≥†");
       	}
    	else {
        	System.out.println("?óÖ?ç∞?ù¥?ä∏ Í≥†Í≥†");
        	teamDTO.setTeam_id(team_id);
        	teamDTO.setType(type);
        	teamDTO.setCategory1(category1);
        	teamDTO.setCategory2(category2);
        	teamDTO.setMail(mail);
        	teamDAO.updateTeam(teamDTO);
    	}
    	return null;
    }     
    
    
    @RequestMapping("/management/team_delete_table.do") 
    public @ResponseBody Map<String, Object> teamDelete(@RequestParam int team_id, Model model) {
    	System.out.println("delete!~!");
    	teamDAO.deleteTeam(team_id);
    	
    	return null;
    }
};