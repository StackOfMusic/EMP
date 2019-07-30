package com.emp.bpms.controller;


import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emp.bpms.repository.reply.ReplyDAO;
import com.emp.bpms.repository.reply.ReplyDTO;
import com.emp.bpms.repository.reqform.ReqformDAO;
import com.emp.bpms.repository.supportType.SupportTypeDAO;
import com.emp.bpms.repository.team.TeamDAO;
import com.emp.bpms.repository.ticket.TicketDAO;
import com.emp.bpms.repository.ticket.TicketDTO;
import com.emp.bpms.repository.user.UserDAO;
import com.emp.bpms.repository.user.UserDTO;
import com.emp.bpms.repository.user.UserService;
import com.emp.bpms.util.ExcelPOIHelper;
import com.emp.bpms.util.paging.Criteria;
import com.emp.bpms.util.paging.PageMaker;
import com.skt.bpms.StaticFlag;
import com.skt.bpms.repository.equipment.EquipmentDAO;
import com.skt.bpms.repository.equipment.EquipmentDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TicketController {

	//?ï°?Öò(View ?óÜ?ù¥ ActionÎß? Ï°¥Ïû¨)
	private int getAuthority() {
		//ENUM?úºÎ°? ?åêÎ≥?
		return userService.loadLoginUserDTO().getAuthority_id()-1;
	}
	
	private String uriReturn(String[] uriSet) {
		return uriSet[getAuthority()];
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
	private ReqformDAO 		reqformDAO;
	@Autowired
	private UserService 	userService;
	
    @Autowired
    public JavaMailSender 	emailSender;
    
    @Autowired
    public SupportTypeDAO	supportTypeDAO;
    
    /*
     * test?ö©
     */
	private static String[]	socketTestURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/ticket/socketTest",
			StaticFlag.defaultPageURI + "/bp/ticket/socketTest",
			StaticFlag.defaultPageURI + "/skt/ticket/socketTest",
			StaticFlag.defaultPageURI + "/skt/ticket/socketTest"
		};
	
	private static String[] testPageURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/test/test",	//SKT ?ö¥?ö©?ûê
			StaticFlag.defaultPageURI + "/bp/test/test",	//bp ?ö¥?ö©?ûê
			StaticFlag.defaultPageURI + "/skt/test/test",	//SKT ?ö¥?ö©?ûê
			StaticFlag.defaultPageURI + "/skt/test/test"	//SKT ?ö¥?ö©?ûê			
		};
	
	
	/*
	 * ?éò?ù¥Ïß? Îß§Ìïë Í≥µÍ∞Ñ
	 * URI Rule
	 * -String[] Î°? Í∂åÌïúÎ≥? Page Ïß??†ï
	 * -PageURI?äî ?õÑÎØ∏Ïóê Î∂ôÎäî PageURIÎ°? Íµ¨Î∂Ñ
	 */

	//ticket/retrieve
	private static String[] createTicketPageURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/ticket/create",
			StaticFlag.defaultPageURI + "/bp/ticket/create",
			StaticFlag.defaultPageURI + "/skt/ticket/create",
			StaticFlag.defaultPageURI + "/skt/ticket/create"
		};	
	
	//ticket/retrieve
	private static String[] retrievePageURI 		= 
		{
			StaticFlag.defaultPageURI + "/skt/ticket/retrieve",
			StaticFlag.defaultPageURI + "/bp/ticket/retrieve",
			StaticFlag.defaultPageURI + "/skt/ticket/retrieve",
			StaticFlag.defaultPageURI + "/skt/ticket/retrieve"
		};

	//ticket/summary
	private static String[] ticketSummaryPageURI 	= 
		{
			StaticFlag.defaultPageURI + "/skt/ticket/summary",
			StaticFlag.defaultPageURI + "/bp/ticket/summary",
			StaticFlag.defaultPageURI + "/skt/ticket/summary",
			StaticFlag.defaultPageURI + "/skt/ticket/summary"
		};

	//ticket/fileupload
	private static String[] fileuploadPageURI 	= 
		{
			StaticFlag.defaultPageURI + "/skt/ticket/fileupload",
			StaticFlag.defaultPageURI + "/bp/ticket/fileupload",
			StaticFlag.defaultPageURI + "/skt/ticket/fileupload",
			StaticFlag.defaultPageURI + "/skt/ticket/fileupload"
		};

	//ticket/main
	private static String[] mainPageURI 			= 
		{
			"redirect:"+ticketSummaryPageURI[0],
			"redirect:"+ticketSummaryPageURI[1],
			"redirect:"+ticketSummaryPageURI[2],
			"redirect:"+ticketSummaryPageURI[3]
		};
	//ticket
	private static String[] defaultPageURI 		= 
		{	
			"redirect:" + ticketSummaryPageURI[0],
			"redirect:" + ticketSummaryPageURI[1],
			"redirect:" + ticketSummaryPageURI[2],
			"redirect:" + ticketSummaryPageURI[3]
		};
	
	//Î™®Îìà(?éò?ù¥Ïß? ?Ç¥ ?èô?†Å ?ÇΩ?ûÖ?êò?äî Í∏∞Îä•?ì§)

	private static String createTicketModuleURI 		= StaticFlag.defaultModuleURI + "/skt/ticket/create";
	private static String quickCreateTicketModuleURI 	= StaticFlag.defaultModuleURI + "/skt/ticket/quickCreate";	
	private static String requestFormModuleURI			= StaticFlag.defaultModuleURI + "/skt/ticket/requestForm";
	private static String replyListModuleURI 			= StaticFlag.defaultModuleURI + "/skt/ticket/replyList";
	private static String ticketListModuleURI 			= StaticFlag.defaultModuleURI + "/skt/ticket/ticketList";
	private static String equipmentSelectModuleURI		= StaticFlag.defaultModuleURI + "/skt/ticket/equipmentSelect";
	private static String userSelectModuleURI			= StaticFlag.defaultModuleURI + "/skt/ticket/userSelect";
	private static String ticketDetailModuleURI			= StaticFlag.defaultModuleURI + "/skt/ticket/ticketDetail";
	
	

	
	public void setUserBasicAttribute(Model model) {
		//?ö∞?Ñ†?? ?ù¥Î¶? ?Ñ£?ñ¥Ï£ºÍ∏∞(Í∞ÅÏ¢Ö ?éò?ù¥Ïß??óê?Ñú ?ôï?ù∏?ö©?èÑ Î∞? Ï∞®ÌõÑ Í±∞Î•¥Í∏? ?úÑ?ïú ?ö©?èÑ)
		UserDTO user = userService.loadLoginUserDTO();
		model.addAttribute("curUserName", user.getName());
		model.addAttribute("loginUser", user);
	}

    public void sendEmail() {
      MimeMessage message = emailSender.createMimeMessage();
      MimeMultipart multipart = new MimeMultipart();
      MimeBodyPart messageBodyPart = new MimeBodyPart();
      String filename = "/home/file.txt";

      
      
      try {
          DataSource source = new FileDataSource(filename);
          messageBodyPart.setDataHandler(new DataHandler(source));
          messageBodyPart.setFileName(filename);
          multipart.addBodyPart(messageBodyPart);
    	  
    	  
        message.setSubject("Í∏∞Ïà†Ïß??õê?öîÏ≤??Ñú ?Öå?ä§?ä∏", "UTF-8");
        String htmlContent = "";//mailContent;
        htmlContent = htmlContent + "<div>?Öå?ä§?ä∏?ûÖ?ãà?ã§.</div>";
        message.setText(htmlContent, "UTF-8", "html");
        message.setFrom(new InternetAddress("bpms@gmail.com"));
        message.addRecipient(RecipientType.TO, new InternetAddress("wahaha_kim@naver.com"));
        emailSender.send(message);
      } catch (MessagingException e) {
        e.printStackTrace();
        return;
      } catch (MailException e) {
        e.printStackTrace();
        return;
      } // try - catch
    }

	//Î©îÏù∏(?ú†??ÎßàÎã§ ?ã§Î•? ?ôîÎ©??)
    @RequestMapping("/ticket/main")
    public String defaultPage() {
        return uriReturn(mainPageURI);
    }
  
    //Î©îÏù∏(?ú†??ÎßàÎã§ ?ã§Î•? ?ôîÎ©??)
    @RequestMapping("/ticket")
    public String defaultPage(Model model) {
    	setUserBasicAttribute(model);

    	return uriReturn(defaultPageURI);
    }
    
    
/*
 * ?Öå?ä§?ä∏?ö© Îß§Ìïë ?òÅ?ó≠
 */
    
    @RequestMapping("/ticket/socketTest") 
    public  String socketTest() {
    	
    	return uriReturn(socketTestURI); 
    }       
    
    @RequestMapping("/ticket/test") 
    public  String test() {
    	
    	return uriReturn(testPageURI); 
    }       
    
    
/*
 * Page?ö© Îß§Ìïë ?òÅ?ó≠
 */

    @RequestMapping("/ticket/create")
    public String createTicketPage(Model model) {
    	setUserBasicAttribute(model);

    	model.addAttribute("supportTypeList", supportTypeDAO.selectAllList());
    	
        return uriReturn(createTicketPageURI);
    }    
    
    //?ã∞ÏºìÎ∞ú?Ü° Î™®Îìà ?ùÑ?ö∞Í∏?
    @RequestMapping("/ticket/create.view")
    public String createTicketModule(Model model) {
    	setUserBasicAttribute(model);

    	model.addAttribute("supportTypeList", supportTypeDAO.selectAllList());
    	
        return createTicketModuleURI;
    }
    
    @RequestMapping("/ticket/quickCreate.view")
    public String quickCreateTicketModule() {
        return quickCreateTicketModuleURI;
    }
    
    @RequestMapping("/ticket/retrieve")
    public String retrievePage(Criteria cri, Model model) {
    	setUserBasicAttribute(model);
        return uriReturn(retrievePageURI);
    }
    
    @RequestMapping("/searchWord")
    public String searchWord(@RequestParam String linkword, Criteria cri, Model model) {
    	System.out.println("~!~!");
    	System.out.println(linkword);
    	setUserBasicAttribute(model);
    	model.addAttribute("ticketList", ticketDAO.listCriteria(cri.getPage(), cri.getPerPageNum()));// Í≤åÏãú?åê?ùò Í∏? Î¶¨Ïä§?ä∏
    	PageMaker pageMaker = new PageMaker();
    	pageMaker.setCri(cri);
    	pageMaker.setTotalCount(ticketDAO.listCountCriteria());
    		
    	model.addAttribute("pageMaker", pageMaker);  // Í≤åÏãú?åê ?ïò?ã®?ùò ?éò?ù¥Ïß? Í¥??†®, ?ù¥?†Ñ?éò?ù¥Ïß?, ?éò?ù¥Ïß? ÎßÅÌÅ¨ , ?ã§?ùå ?éò?ù¥Ïß?
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	
    	model.addAttribute("linkword",linkword);
        return uriReturn(retrievePageURI);
    }
    
    
    @RequestMapping("/ticket/summary")
    public String summaryPage(Model model) {
    	setUserBasicAttribute(model);
        return uriReturn(ticketSummaryPageURI);
    }
    
    @RequestMapping("/ticket/reqTicketList")
    public String reqTicketList(Model model) {//Criteria cri, Model model) {

    	model.addAttribute("ticketList", ticketDAO.selectReqList((int)userService.loadLoginUserDTO().getId()));
//    	PageMaker pageMaker = new PageMaker();
 //   	pageMaker.setCri(cri);
  //  	pageMaker.setTotalCount(1);
    		
    	model.addAttribute("pageMaker", null);  // Í≤åÏãú?åê ?ïò?ã®?ùò ?éò?ù¥Ïß? Í¥??†®, ?ù¥?†Ñ?éò?ù¥Ïß?, ?éò?ù¥Ïß? ÎßÅÌÅ¨ , ?ã§?ùå ?éò?ù¥Ïß?
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return ticketListModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)
    
    @RequestMapping("/ticket/recvTicketList")
    public String recvTicketList(Model model) {//Criteria cri, Model model) {

    	model.addAttribute("ticketList", ticketDAO.selectRecvList((int)userService.loadLoginUserDTO().getId()));

//    	PageMaker pageMaker = new PageMaker();
 //   	pageMaker.setCri(cri);
  //  	pageMaker.setTotalCount(1);

    		
    	model.addAttribute("pageMaker", null);  // Í≤åÏãú?åê ?ïò?ã®?ùò ?éò?ù¥Ïß? Í¥??†®, ?ù¥?†Ñ?éò?ù¥Ïß?, ?éò?ù¥Ïß? ÎßÅÌÅ¨ , ?ã§?ùå ?éò?ù¥Ïß?
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return ticketListModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)
    
    @RequestMapping("/ticket/fileupload")
    public String fileuploadPage(Model model) {
    	setUserBasicAttribute(model);
    	//?Ñ∏?ÖòÏ≤¥ÌÅ¨
    	//?Ç¥ ?öîÏ≤? ?òÑ?ô©
    	//?Ç¥Í∞? Î∞õÏ? ?òÑ?ô©
    	//?? ?òÑ?ô©
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	model.addAttribute("reqformList", reqformDAO.selectAllList());
    	return uriReturn(fileuploadPageURI);
    }
    
    public class Fruit {
    	String name;
    	long price;
    	int quantity;
    	
    	public Fruit() {
    		
    	}	
    
    	public Fruit(String name, long price, int quantity) {
    		this.name = name;
    		this.price = price;
    		this.quantity = quantity;
    	}
    
    	public String getName() {
    		return name;
    	}
    
    	public long getPrice() {
    		return price;
    	}
    
    	public int getQuantity() {
    		return quantity;
    	}
    
    	public void setName(String name) {
    		this.name = name;
    	}
    
    	public void setPrice(long price) {
    		this.price = price;
    	}
    
    	public void setQuantity(int quantity) {
    		this.quantity = quantity;
    	}
    }
   
    /*
    @PostMapping("/uploadExcelFile")
    public String uploadFile(Model model, MultipartFile file) throws IOException {
        InputStream in = file.getInputStream();
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
        FileOutputStream f = new FileOutputStream(fileLocation);
        int ch = 0;
        while ((ch = in.read()) != -1) {
            f.write(ch);
        }
        f.flush();
        f.close();
        model.addAttribute("message", "File: " + file.getOriginalFilename() 
          + " has been uploaded successfully!");
        return "excel";
    }*/
    
    @RequestMapping("/ticket/issuingTicket")
    public String issuingTicket(Model model) {
    	model.addAttribute("ticketList", ticketDAO.selectAllList());
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return uriReturn(retrievePageURI);
    } //Ï°∞Ìöå ÏµúÏ¥à ?éò?ù¥Ïß? ?ùÑ?ö∞Í∏?
    
    @RequestMapping("/ticket/ticketList")
    public String ticketList(Criteria cri, Model model) {
    	setUserBasicAttribute(model);
    	model.addAttribute("ticketList", ticketDAO.listCriteria(cri.getPageStart(), cri.getPerPageNum()));// Í≤åÏãú?åê?ùò Í∏? Î¶¨Ïä§?ä∏
    	PageMaker pageMaker = new PageMaker();
    	pageMaker.setCri(cri);
    	pageMaker.setTotalCount(ticketDAO.listCountCriteria());
    		
    	model.addAttribute("pageMaker", pageMaker);  // Í≤åÏãú?åê ?ïò?ã®?ùò ?éò?ù¥Ïß? Í¥??†®, ?ù¥?†Ñ?éò?ù¥Ïß?, ?éò?ù¥Ïß? ÎßÅÌÅ¨ , ?ã§?ùå ?éò?ù¥Ïß?
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return ticketListModuleURI;
    } //Î¶¨Ïä§?ä∏ ?óÖ?ç∞?ù¥?ä∏?ö©(Í≤??Éâ ?àå???ùÑ ?ïå)
    


    //?äπ?†ï ?ã∞Ïº? ?Ñ†?Éù ?ãú Î≥¥Ïó¨Ï§? ?Ñ∏Î∂? ?Ç¥?ó≠
    @RequestMapping("/ticket/ticketDetail") 
    public String ticketDetail(@RequestParam int ticket_id, Model model) {
    	setUserBasicAttribute(model);
    	model.addAttribute("ticket_id", ticket_id);
    	model.addAttribute("replyList", replyDAO.selectReplyList(ticket_id));
    	model.addAttribute("ticketData", ticketDAO.selectOneTicket(ticket_id));
    	return ticketDetailModuleURI;
    }     
    
    //Î¶¨Ìîå Í∞±Ïã†(Î¶¨Ìîå ?ì∞Í±∞ÎÇò Ïß??ö∞Í±∞ÎÇò ?àò?†ï?ïú ?õÑ) ajax ?èô?ûë
    @RequestMapping("/ticket/replyList") 
    public String replyList(@RequestParam int ticket_id, Model model) {
    	setUserBasicAttribute(model);
    	model.addAttribute("replyList", replyDAO.selectReplyList(ticket_id));
    	return replyListModuleURI;
    }   
    
    //Reply ?†Å?óà?ùÑ ?ïå ajax ?èô?ûë
    @RequestMapping("/ticket/writeReply") 
    public @ResponseBody Map<String, Object> writeReply(@RequestParam String data, @RequestParam int ticket_id, Model model) {
    	UserDTO reqUserDTO = userService.loadLoginUserDTO();    	
    	
    	ReplyDTO replyDTO = new ReplyDTO();
    	replyDTO.setId(reqUserDTO.getId());
    	replyDTO.setName(reqUserDTO.getName());
    	replyDTO.setTicket_id(ticket_id);
    	replyDTO.setReply_content(data);
    	replyDAO.insertReply(replyDTO);
    	
    	return null;
    } 



    
    //action ?? doÎ•? Î∂ôÏó¨Î≥ºÍπå?
    @RequestMapping("/ticket/issuing.do")
    @ResponseBody
    public void issuingAction(Model model, HttpServletRequest request) {//@RequestParam String content, @RequestParam int equipment_id, Model model) {
System.out.println(request.getParameter("reqUserId")+":"+request.getParameter("recvUserId")+":"+request.getParameter("equipId"));
    	
    	int reqUser_id	= Integer.parseInt(request.getParameter("reqUserId"));
    	int recvUser_id	= Integer.parseInt(request.getParameter("recvUserId"));
    	int equipment_id 	= Integer.parseInt(request.getParameter("equipId"));
    	int progress = request.getParameter("progress") != "" ? Integer.parseInt(request.getParameter("progress")) : 1;
    	progress = StaticFlag.AuthorityType.getAuthority(userService.loadLoginUserDTO().getAuthority_id()) == StaticFlag.AuthorityType.BPUSER ? 2 : progress;
    	
    	String content 		= request.getParameter("content") 		!= null ? request.getParameter("content") 		: "";
    	String content_etc	= request.getParameter("content_etc")	!= null ? request.getParameter("content_etc") 	: "";
    	String location		= request.getParameter("location") 		!= null ? request.getParameter("location") 		: "";
    	String target		= request.getParameter("target")		!= null ? request.getParameter("target")		: "";
    	
    	TicketDTO ticketDTO = new TicketDTO();

    	EquipmentDTO equipmentDTO = equipmentDAO.selectEquipmentById(equipment_id);
    	UserDTO reqUserDTO = userDAO.selectUserById(reqUser_id);//userService.loadLoginUserDTO();
    	UserDTO recvUserDTO = userDAO.selectUserById(recvUser_id);
    	
    	//???ÉÅ?û•Îπ?
    	ticketDTO.setEquipment_loc(location);
    	ticketDTO.setEquipment_id(equipmentDTO.getEquipment_id());
    	//?Ç¥?ö©
    	ticketDTO.setTicket_content(content);
    	ticketDTO.setTicket_subject(target);
    	ticketDTO.setTicket_content_etc(content_etc);//?ïº?äî ?Ñ£?ñ¥?Ñú ?†Ñ?ã¨?ïò?èÑÎ°?
    	
    	//?†ë?àò?ûê
    	ticketDTO.setExe_user_id(recvUserDTO.getId());
    	ticketDTO.setExe_user_team_id(recvUserDTO.getTeam_id());

    	//?öîÏ≤??ûê
    	ticketDTO.setReq_user_id((int)reqUserDTO.getId());
    	ticketDTO.setReq_user_team_id(reqUserDTO.getTeam_id());
    	ticketDTO.setProgress(progress);
    	
    	ticketDTO.setWork_type_id(1);
    	ticketDTO.setSupport_id(1);
/*
            id,
            ticket_time,
            ticket_reply_cnt,
            name,
            time_start,
            time_end,
            system_name,
            system_loc,
            support_type_id,
            detail_activity,
            post_content,
            remote,
            paid,
            req_team,
            req_name,
            exe_type,
            exe_team,
            exe_name,
            etc,
            progress 
 */
    	
    	ticketDAO.insertTicket(ticketDTO);
    	sendEmail();
 
    }     
    
    @RequestMapping("/ticket/delete.do")
    @ResponseBody
    public void deleteAction(@RequestParam int ticket_id, HttpServletRequest request) {//@RequestParam String content, @RequestParam int equipment_id, Model model) {
    	System.out.println(ticket_id+"");
    	System.out.println(ticketDAO.deleteTicket(ticket_id));
    }         

    @RequestMapping("/ticket/acceptTicket.do")
    @ResponseBody
    public void acceptTicketAction(@RequestParam int ticket_id, HttpServletRequest request) {//@RequestParam String content, @RequestParam int equipment_id, Model model) {
    	ticketDAO.acceptTicket(ticket_id);
    }         

    @RequestMapping("/ticket/submitTicket.do")
    @ResponseBody
    public void submitTicketAction(@RequestParam int ticket_id, HttpServletRequest request) {//@RequestParam String content, @RequestParam int equipment_id, Model model) {
    	ticketDAO.submitTicket(ticket_id);
    }         

    @RequestMapping("/ticket/finishTicket.do")
    @ResponseBody
    public void finishTicketAction(@RequestParam int ticket_id, HttpServletRequest request) {//@RequestParam String content, @RequestParam int equipment_id, Model model) {
    	ticketDAO.finishTicket(ticket_id);
    }         

    
    //?û•ÎπÑÍµ∞ ?Ñ†?Éù Ï∞ΩÏùÑ ?úÑ?ïú ajax ?èô?ûë
    @RequestMapping("/ticket/equipmentSelect") 
    public String equipmentSelect(Model model, String equipmentID, String equipValue, String equipManagerValue) {
    	
    	model.addAttribute("equipmentID", equipmentID);
    	model.addAttribute("equipValue", equipValue);
    	model.addAttribute("equipManagerValue", equipManagerValue);
    	
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return equipmentSelectModuleURI;
    } 

    //?û•ÎπÑÍµ∞ ?Ñ†?Éù Ï∞ΩÏùÑ ?úÑ?ïú ajax ?èô?ûë
    @RequestMapping("/ticket/userSelect") 
    public String equipmentSelect(Model model) {
    	
    	
    	model.addAttribute("userList", userDAO.selectAllUser());
    	return userSelectModuleURI;
    } 

    
    //
    @RequestMapping("/ticket/requestForm") 
    public String requestForm(Model model) {
    	
    	
//    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return requestFormModuleURI;
    } 
    
/*
 * Action ?òÅ?ó≠
 */
    //?óë?? ?ã§?ö¥
   /* @RequestMapping(value = "/excelDown.do")
    public String excelDown(HttpServletResponse response) throws Exception {
        List<TicketDTO> list = ticketDAO.selectAllList();

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Í∏∞Ïà† Ïß??õê ?öîÏ≤? ?òÑ?ô©");
        Row row = null;
        Cell cell = null;
        int rowNo = 0;

        CellStyle headStyle = wb.createCellStyle();
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        headStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        
        row = sheet.createRow(rowNo++);
        cell = row.createCell(0);
        cell.setCellValue("");
        cell = row.createCell(1);
        cell.setCellStyle(headStyle);
        cell.setCellValue("Î≤àÌò∏");
        cell = row.createCell(2);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?ûë?óÖ ?ãú?ûë ?ãúÍ∞?");
        cell = row.createCell(3);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?ûë?óÖ Ï¢ÖÎ£å ?ãúÍ∞?");
        cell = row.createCell(4);
        cell.setCellStyle(headStyle);
        cell.setCellValue("???ÉÅ ?ãú?ä§?Öú Î™?");
        cell = row.createCell(5);
        cell.setCellStyle(headStyle);
        cell.setCellValue("???ÉÅ ?ãú?ä§?Öú ?úÑÏπ?");
        cell = row.createCell(6);
        cell.setCellStyle(headStyle);
        cell.setCellValue("Ïß??õê ?öîÏ≤? ???ûÖ");
        cell = row.createCell(7);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?ûë?óÖ ?Ç¥?ó≠");
        cell = row.createCell(8);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?Ç¥?ö©");
        cell = row.createCell(9);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?õêÍ≤?/?òÑ?û•");
        cell = row.createCell(10);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?ú†?ÉÅ/Î¨¥ÏÉÅ");
        cell = row.createCell(11);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?öîÏ≤?Î∂??Ñú");
        cell = row.createCell(12);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?öîÏ≤??ûê");
        cell = row.createCell(13);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?ãú?ñâÏ°∞ÏßÅ");
        cell = row.createCell(14);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?ãú?ñâÎ∂??Ñú");
        cell = row.createCell(15);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?ãú?ñâ?ûê");
        cell = row.createCell(16);
        cell.setCellStyle(headStyle);
        cell.setCellValue("ÎπÑÍ≥†");
        cell = row.createCell(17);
        cell.setCellStyle(headStyle);
        cell.setCellValue("ÏßÑÌñâ?ÉÅ?ô©");

        for(TicketDTO vo : list) {
            row = sheet.createRow(rowNo++);
            cell = row.createCell(0);
            cell = row.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getTicket_id());
            cell = row.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getTime_start());
            cell = row.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getTime_end());
            cell = row.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getSystem_name());
            cell = row.createCell(5);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getSystem_loc());
            cell = row.createCell(6);
            cell.setCellStyle(bodyStyle);
            if (vo.getSupport_type_id() == 1)
            	cell.setCellValue("Í∏∞Ïà†Î¨∏Ïùò");
            else
            	cell.setCellValue("?†Ñ?ôîÎ¨∏Ïùò");
            cell.setCellValue(vo.getSupport_type_id());
            cell = row.createCell(7);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getDetail_activity());
            cell = row.createCell(8);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getPost_content());
            cell = row.createCell(9);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getRemote());
            cell = row.createCell(10);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getPaid());
            cell = row.createCell(11);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getReq_team());
            cell = row.createCell(12);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getReq_name());
            cell = row.createCell(13);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getExe_type());
            cell = row.createCell(14);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getExe_team());
            cell = row.createCell(15);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getExe_name());
            cell = row.createCell(16);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getEtc());
            cell = row.createCell(17);
            cell.setCellStyle(bodyStyle);
            if (vo.getProgress() == 1)
            	cell.setCellValue("Ïß??õê ?öîÏ≤?");
            else if (vo.getProgress() == 2)
            	cell.setCellValue("ÏßÑÌñâÏ§?");
            else
            	cell.setCellValue("Ï¢ÖÎ£å");
        }
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=bpms_test.xls");
        
        wb.write(response.getOutputStream());
        wb.close();
        return retrievePageURI;
    }
        
    */
}