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

	//?��?��(View ?��?�� Action�? 존재)
	private int getAuthority() {
		//ENUM?���? ?���?
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
     * test?��
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
			StaticFlag.defaultPageURI + "/skt/test/test",	//SKT ?��?��?��
			StaticFlag.defaultPageURI + "/bp/test/test",	//bp ?��?��?��
			StaticFlag.defaultPageURI + "/skt/test/test",	//SKT ?��?��?��
			StaticFlag.defaultPageURI + "/skt/test/test"	//SKT ?��?��?��			
		};
	
	
	/*
	 * ?��?���? 매핑 공간
	 * URI Rule
	 * -String[] �? 권한�? Page �??��
	 * -PageURI?�� ?��미에 붙는 PageURI�? 구분
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
	
	//모듈(?��?���? ?�� ?��?�� ?��?��?��?�� 기능?��)

	private static String createTicketModuleURI 		= StaticFlag.defaultModuleURI + "/skt/ticket/create";
	private static String quickCreateTicketModuleURI 	= StaticFlag.defaultModuleURI + "/skt/ticket/quickCreate";	
	private static String requestFormModuleURI			= StaticFlag.defaultModuleURI + "/skt/ticket/requestForm";
	private static String replyListModuleURI 			= StaticFlag.defaultModuleURI + "/skt/ticket/replyList";
	private static String ticketListModuleURI 			= StaticFlag.defaultModuleURI + "/skt/ticket/ticketList";
	private static String equipmentSelectModuleURI		= StaticFlag.defaultModuleURI + "/skt/ticket/equipmentSelect";
	private static String userSelectModuleURI			= StaticFlag.defaultModuleURI + "/skt/ticket/userSelect";
	private static String ticketDetailModuleURI			= StaticFlag.defaultModuleURI + "/skt/ticket/ticketDetail";
	
	

	
	public void setUserBasicAttribute(Model model) {
		//?��?��?? ?���? ?��?��주기(각종 ?��?���??��?�� ?��?��?��?�� �? 차후 거르�? ?��?�� ?��?��)
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
    	  
    	  
        message.setSubject("기술�??��?���??�� ?��?��?��", "UTF-8");
        String htmlContent = "";//mailContent;
        htmlContent = htmlContent + "<div>?��?��?��?��?��?��.</div>";
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

	//메인(?��??마다 ?���? ?���??)
    @RequestMapping("/ticket/main")
    public String defaultPage() {
        return uriReturn(mainPageURI);
    }
  
    //메인(?��??마다 ?���? ?���??)
    @RequestMapping("/ticket")
    public String defaultPage(Model model) {
    	setUserBasicAttribute(model);

    	return uriReturn(defaultPageURI);
    }
    
    
/*
 * ?��?��?��?�� 매핑 ?��?��
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
 * Page?�� 매핑 ?��?��
 */

    @RequestMapping("/ticket/create")
    public String createTicketPage(Model model) {
    	setUserBasicAttribute(model);

    	model.addAttribute("supportTypeList", supportTypeDAO.selectAllList());
    	
        return uriReturn(createTicketPageURI);
    }    
    
    //?��켓발?�� 모듈 ?��?���?
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
    	model.addAttribute("ticketList", ticketDAO.listCriteria(cri.getPage(), cri.getPerPageNum()));// 게시?��?�� �? 리스?��
    	PageMaker pageMaker = new PageMaker();
    	pageMaker.setCri(cri);
    	pageMaker.setTotalCount(ticketDAO.listCountCriteria());
    		
    	model.addAttribute("pageMaker", pageMaker);  // 게시?�� ?��?��?�� ?��?���? �??��, ?��?��?��?���?, ?��?���? 링크 , ?��?�� ?��?���?
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
    		
    	model.addAttribute("pageMaker", null);  // 게시?�� ?��?��?�� ?��?���? �??��, ?��?��?��?���?, ?��?���? 링크 , ?��?�� ?��?���?
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return ticketListModuleURI;
    } //리스?�� ?��?��?��?��?��(�??�� ?��???�� ?��)
    
    @RequestMapping("/ticket/recvTicketList")
    public String recvTicketList(Model model) {//Criteria cri, Model model) {

    	model.addAttribute("ticketList", ticketDAO.selectRecvList((int)userService.loadLoginUserDTO().getId()));

//    	PageMaker pageMaker = new PageMaker();
 //   	pageMaker.setCri(cri);
  //  	pageMaker.setTotalCount(1);

    		
    	model.addAttribute("pageMaker", null);  // 게시?�� ?��?��?�� ?��?���? �??��, ?��?��?��?���?, ?��?���? 링크 , ?��?�� ?��?���?
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return ticketListModuleURI;
    } //리스?�� ?��?��?��?��?��(�??�� ?��???�� ?��)
    
    @RequestMapping("/ticket/fileupload")
    public String fileuploadPage(Model model) {
    	setUserBasicAttribute(model);
    	//?��?��체크
    	//?�� ?���? ?��?��
    	//?���? 받�? ?��?��
    	//?? ?��?��
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
    } //조회 최초 ?��?���? ?��?���?
    
    @RequestMapping("/ticket/ticketList")
    public String ticketList(Criteria cri, Model model) {
    	setUserBasicAttribute(model);
    	model.addAttribute("ticketList", ticketDAO.listCriteria(cri.getPageStart(), cri.getPerPageNum()));// 게시?��?�� �? 리스?��
    	PageMaker pageMaker = new PageMaker();
    	pageMaker.setCri(cri);
    	pageMaker.setTotalCount(ticketDAO.listCountCriteria());
    		
    	model.addAttribute("pageMaker", pageMaker);  // 게시?�� ?��?��?�� ?��?���? �??��, ?��?��?��?���?, ?��?���? 링크 , ?��?�� ?��?���?
    	model.addAttribute("teamList", teamDAO.selectAllList());
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
        return ticketListModuleURI;
    } //리스?�� ?��?��?��?��?��(�??�� ?��???�� ?��)
    


    //?��?�� ?���? ?��?�� ?�� 보여�? ?���? ?��?��
    @RequestMapping("/ticket/ticketDetail") 
    public String ticketDetail(@RequestParam int ticket_id, Model model) {
    	setUserBasicAttribute(model);
    	model.addAttribute("ticket_id", ticket_id);
    	model.addAttribute("replyList", replyDAO.selectReplyList(ticket_id));
    	model.addAttribute("ticketData", ticketDAO.selectOneTicket(ticket_id));
    	return ticketDetailModuleURI;
    }     
    
    //리플 갱신(리플 ?��거나 �??��거나 ?��?��?�� ?��) ajax ?��?��
    @RequestMapping("/ticket/replyList") 
    public String replyList(@RequestParam int ticket_id, Model model) {
    	setUserBasicAttribute(model);
    	model.addAttribute("replyList", replyDAO.selectReplyList(ticket_id));
    	return replyListModuleURI;
    }   
    
    //Reply ?��?��?�� ?�� ajax ?��?��
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



    
    //action ?? do�? 붙여볼까?
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
    	
    	//???��?���?
    	ticketDTO.setEquipment_loc(location);
    	ticketDTO.setEquipment_id(equipmentDTO.getEquipment_id());
    	//?��?��
    	ticketDTO.setTicket_content(content);
    	ticketDTO.setTicket_subject(target);
    	ticketDTO.setTicket_content_etc(content_etc);//?��?�� ?��?��?�� ?��?��?��?���?
    	
    	//?��?��?��
    	ticketDTO.setExe_user_id(recvUserDTO.getId());
    	ticketDTO.setExe_user_team_id(recvUserDTO.getTeam_id());

    	//?���??��
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

    
    //?��비군 ?��?�� 창을 ?��?�� ajax ?��?��
    @RequestMapping("/ticket/equipmentSelect") 
    public String equipmentSelect(Model model, String equipmentID, String equipValue, String equipManagerValue) {
    	
    	model.addAttribute("equipmentID", equipmentID);
    	model.addAttribute("equipValue", equipValue);
    	model.addAttribute("equipManagerValue", equipManagerValue);
    	
    	model.addAttribute("equipmentList", equipmentDAO.selectAllList());
    	return equipmentSelectModuleURI;
    } 

    //?��비군 ?��?�� 창을 ?��?�� ajax ?��?��
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
 * Action ?��?��
 */
    //?��?? ?��?��
   /* @RequestMapping(value = "/excelDown.do")
    public String excelDown(HttpServletResponse response) throws Exception {
        List<TicketDTO> list = ticketDAO.selectAllList();

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("기술 �??�� ?���? ?��?��");
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
        cell.setCellValue("번호");
        cell = row.createCell(2);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?��?�� ?��?�� ?���?");
        cell = row.createCell(3);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?��?�� 종료 ?���?");
        cell = row.createCell(4);
        cell.setCellStyle(headStyle);
        cell.setCellValue("???�� ?��?��?�� �?");
        cell = row.createCell(5);
        cell.setCellStyle(headStyle);
        cell.setCellValue("???�� ?��?��?�� ?���?");
        cell = row.createCell(6);
        cell.setCellStyle(headStyle);
        cell.setCellValue("�??�� ?���? ???��");
        cell = row.createCell(7);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?��?�� ?��?��");
        cell = row.createCell(8);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?��?��");
        cell = row.createCell(9);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?���?/?��?��");
        cell = row.createCell(10);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?��?��/무상");
        cell = row.createCell(11);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?���?�??��");
        cell = row.createCell(12);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?���??��");
        cell = row.createCell(13);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?��?��조직");
        cell = row.createCell(14);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?��?���??��");
        cell = row.createCell(15);
        cell.setCellStyle(headStyle);
        cell.setCellValue("?��?��?��");
        cell = row.createCell(16);
        cell.setCellStyle(headStyle);
        cell.setCellValue("비고");
        cell = row.createCell(17);
        cell.setCellStyle(headStyle);
        cell.setCellValue("진행?��?��");

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
            	cell.setCellValue("기술문의");
            else
            	cell.setCellValue("?��?��문의");
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
            	cell.setCellValue("�??�� ?���?");
            else if (vo.getProgress() == 2)
            	cell.setCellValue("진행�?");
            else
            	cell.setCellValue("종료");
        }
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=bpms_test.xls");
        
        wb.write(response.getOutputStream());
        wb.close();
        return retrievePageURI;
    }
        
    */
}