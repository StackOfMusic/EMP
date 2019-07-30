package com.emp.bpms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emp.bpms.repository.statistics.StatisticsDAO;
import com.emp.bpms.repository.statistics.StatisticsDTO;
import com.emp.bpms.repository.user.UserService;
import com.skt.bpms.StaticFlag;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class StatisticsController {
	
	@Autowired
	private UserService 	userService;
	@Autowired
	private StatisticsDAO	statisticsDAO;
	
	//Î™®Îìà
	private static String barChartURL 	= StaticFlag.defaultModuleURI + "/skt/statistics/barChart";
	private static String lineChartURL 	= StaticFlag.defaultModuleURI + "/skt/statistics/lineChart";
	private static String equipTicketGridURL 	= StaticFlag.defaultModuleURI + "/skt/grid/equipTicketGrid";
	
	//?éò?ù¥Ïß?
	private static String workingTimeStatisticsPageURI		=	StaticFlag.defaultPageURI + "/skt/statistics/workingTimeStatistics";
	private static String dayNightStatisticsPageURI		=	StaticFlag.defaultPageURI + "/skt/statistics/dayNightStatistics";
	private static String supportTypeStatisticsPageURI		=	StaticFlag.defaultPageURI + "/skt/statistics/supportTypeStatistics";
	private static String teamStatisticsPageURI		=	StaticFlag.defaultPageURI + "/skt/statistics/teamStatistics";
	private static String equipmentStatisticsPageURI		=	StaticFlag.defaultPageURI + "/skt/statistics/equipmentStatistics";
	
	private static String defaultStatisticsPageURI		=	"redirect:"+ workingTimeStatisticsPageURI;
	private static String mainStatisticsPageURI			=	"redirect:"+ workingTimeStatisticsPageURI;
	
	
	
	public void log() {
		if(userService != null) {
			log.info(userService.loadLoginUserDTO().getEmail());
			log.info(userService.loadLoginUserDTO().getName());
		}
	}

	public void setUserBasicAttribute(Model model) {
		//?ö∞?Ñ†?? ?ù¥Î¶? ?Ñ£?ñ¥Ï£ºÍ∏∞(Í∞ÅÏ¢Ö ?éò?ù¥Ïß??óê?Ñú ?ôï?ù∏?ö©?èÑ Î∞? Ï∞®ÌõÑ Í±∞Î•¥Í∏? ?úÑ?ïú ?ö©?èÑ)
		model.addAttribute("curUserName", userService.loadLoginUserDTO().getName());
	}

	
	//default
	@RequestMapping("/statistics")
	public String defaultStatisticsPage() {
		return defaultStatisticsPageURI; 
	}

	//main
	@RequestMapping("/statistics/main")
	public String mainStatisticsPage() {
		return mainStatisticsPageURI; 
	}	
	
	@RequestMapping("/statistics/workingTimeStatistics")
	public String workingTimeStatisticsPage(Model model) {
		setUserBasicAttribute(model);
		return workingTimeStatisticsPageURI; 
	}
	
	@RequestMapping("/statistics/dayNightStatistics")
	public String dayNightStatisticsPage(Model model) {
		setUserBasicAttribute(model);
		return dayNightStatisticsPageURI; 
	}	
	
	@RequestMapping("/statistics/supportTypeStatistics")
	public String supportTypeStatisticsPage(Model model) {
		setUserBasicAttribute(model);
		return supportTypeStatisticsPageURI; 
	}
	
	@RequestMapping("/statistics/teamStatistics")
	public String teamStatisticsPage(Model model) {
		setUserBasicAttribute(model);
		return teamStatisticsPageURI; 
	}
	
	@RequestMapping("/statistics/equipmentStatistics")
	public String equipmentStatisticsPage(Model model) {
		setUserBasicAttribute(model);
		return equipmentStatisticsPageURI; 
	}	
	
	// BarÏ∞®Ìä∏ Î™®Îìà
	@RequestMapping("/statistics/barChart")
	public String barChart(HttpServletRequest request, Model model) {
		model.addAttribute("url",request.getParameter("url")); 
		model.addAttribute("location",request.getParameter("location"));
		model.addAttribute("title",request.getParameter("title"));
		model.addAttribute("series_names",request.getParameter("series_names"));
		return barChartURL;
	}
	
	// lineÏ∞®Ìä∏ Î™®Îìà
	@RequestMapping("/statistics/lineChart")
	public String lineChart(HttpServletRequest request, Model model) {
		model.addAttribute("url",request.getParameter("url")); 
		model.addAttribute("location",request.getParameter("location"));
		model.addAttribute("title",request.getParameter("title"));
		model.addAttribute("series_names",request.getParameter("series_names"));
		return lineChartURL;
	}

	@ResponseBody
    @RequestMapping("/statistics/spportTypeList") 
    public  Map<String,List<String>> spportTypeList() {
		Map<String, List<String>> chart = new LinkedHashMap<>();      	
       	List<StatisticsDTO> list = statisticsDAO.spportTypeList();
       	List<String> str_list = new ArrayList<>();
     	for(StatisticsDTO dto: list) {
       		str_list.add(dto.getItem());
    	}
       	chart.put("list", str_list);
       	return chart;  
    } 
	
    @ResponseBody
    @RequestMapping("/statistics/countYearlyWorkingTime") 
    public  Map<String,List<String>> countYearlyWorkingTime() {
 	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countYearlyWorkingTime();
      
       	if(list.size() == 0) {
       		return chart; 
       	}
      	
     	String start_date = "2018";
     	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
       	String end_date = sdf.format(date);
       	for(int i=0; i<1200; i++) {
       		List<String> temp= new ArrayList<>();
       		temp.add("0");
       		temp.add("0");
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int year = Integer.parseInt(start_date);
       		year += 1;
       		start_date = Integer.toString(year);
       	}
       	for(StatisticsDTO dto: list) {
       		List<String> temp= new ArrayList<>();	
       		String bp = Integer.toString(dto.getSubitem1());
       		String skt = Integer.toString(dto.getSubitem2());
       		temp.add(bp);
       		temp.add(skt);
       		chart.put(dto.getDate_cat(), temp);
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countMonthlyWorkingTime") 
    public  Map<String,List<String>> countMonthlyWorkingTime() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countMonthlyWorkingTime();
      
       	if(list.size() == 0) {
       		return chart; 
       	}
      	
     	String start_date = "2018-01";
     	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       	String end_date = sdf.format(date);
       	for(int i=0; i<1200; i++) {
       		List<String> temp= new ArrayList<>();
       		temp.add("0");
       		temp.add("0");
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		List<String> temp= new ArrayList<>();
       		String bp = Integer.toString(dto.getSubitem1());
       		String skt = Integer.toString(dto.getSubitem2());
       		temp.add(bp);
       		temp.add(skt);
       		chart.put(dto.getDate_cat(), temp);
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countRecentTwelveMonthlyWorkingTime") 
    public  Map<String,List<String>> countRecentTwelveMonthlyWorkingTime() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countRecentTwelveMonthlyWorkingTime();
       	if(list.size() == 0) {
       		return chart; 
       	}
      	
     	
     	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       	String end_date = sdf.format(date);

       	int temp_m =Integer.parseInt(end_date.split("-")[0])-1;
     	String start_date;
       	if(temp_m < 2018)
       		start_date = "2018-01";
       	else
       		start_date= Integer.toString(temp_m)+"-"+end_date.split("-")[1]; 
       	for(int i=0; i<13; i++) {
       		List<String> temp= new ArrayList<>();
       		temp.add("0");
       		temp.add("0");
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		List<String> temp= new ArrayList<>();
       		String bp = Integer.toString(dto.getSubitem1());
       		String skt = Integer.toString(dto.getSubitem2());
       		temp.add(bp);
       		temp.add(skt);
       		chart.put(dto.getDate_cat(), temp);
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countRecentTwentyfourMonthlyWorkingTime") 
    public  Map<String,List<String>> countRecentTwentyfourMonthlyWorkingTime() {
	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countRecentTwentyfourMonthlyWorkingTime();
       	if(list.size() == 0) {
       		return chart; 
       	}
      	
     	
     	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       	String end_date = sdf.format(date);

       	int temp_m =Integer.parseInt(end_date.split("-")[0])-2;
     	String start_date;
       	if(temp_m < 2018)
       		start_date = "2018-01";
       	else
       		start_date= Integer.toString(temp_m)+"-"+end_date.split("-")[1]; 
       	for(int i=0; i<26; i++) {
       		List<String> temp= new ArrayList<>();
       		temp.add("0");
       		temp.add("0");
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		List<String> temp= new ArrayList<>();
       		String bp = Integer.toString(dto.getSubitem1());
       		String skt = Integer.toString(dto.getSubitem2());
       		temp.add(bp);
       		temp.add(skt);
       		chart.put(dto.getDate_cat(), temp);
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countMonthlyDayNight") 
    public  Map<String,List<String>> countMonthlyDayNight() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countMonthlyDayNight();
       	if(list.size() == 0) {
       		return chart; 
       	}
       
       	String start_date = "2018-01";
       	String end_date = list.get(list.size()-1).getDate_cat();
       	for(int i=0; i<1200; i++) {
       		List<String> temp= new ArrayList<>();
       		temp.add("0");
       		temp.add("0");
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		List<String> temp= new ArrayList<>();
       		String day = new String(chart.get(dto.getDate_cat()).get(0));
       		String night = new String(chart.get(dto.getDate_cat()).get(1));
       		if(dto.getItem().equals("day")) 
       			day = Integer.toString(dto.getSubitem1());
       		else 
       			night = Integer.toString(dto.getSubitem1());
       		temp.add(day);
       		temp.add(night);
       		chart.put(dto.getDate_cat(), temp);
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countRecentTwelveMonthlyDayNight") 
    public  Map<String,List<String>> countRecentTwelveMonthlyDayNight() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countRecentTwelveMonthlyDayNight();
       	if(list.size() == 0) {
       		return chart; 
       	}
       
       	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       	String end_date = sdf.format(date);

       	int temp_m =Integer.parseInt(end_date.split("-")[0])-1;
     	String start_date;
       	if(temp_m < 2018)
       		start_date = "2018-01";
       	else
       		start_date= Integer.toString(temp_m)+"-"+end_date.split("-")[1]; 
       	for(int i=0; i<1200; i++) {
       		List<String> temp= new ArrayList<>();
       		temp.add("0");
       		temp.add("0");
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		List<String> temp= new ArrayList<>();
       		String day = new String(chart.get(dto.getDate_cat()).get(0));
       		String night = new String(chart.get(dto.getDate_cat()).get(1));
       		if(dto.getItem().equals("day")) 
       			day = Integer.toString(dto.getSubitem1());
       		else 
       			night = Integer.toString(dto.getSubitem1());
       		temp.add(day);
       		temp.add(night);
       		chart.put(dto.getDate_cat(), temp);
    	}
       	return chart;  
    } 
	
    @ResponseBody
    @RequestMapping("/statistics/countRecentTwentyfourMonthlyDayNight") 
    public  Map<String,List<String>> countRecentTwentyfourMonthlyDayNight() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countRecentTwentyfourMonthlyDayNight();
       	if(list.size() == 0) {
       		return chart; 
       	}
       
       	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       	String end_date = sdf.format(date);

       	int temp_m =Integer.parseInt(end_date.split("-")[0])-2;
     	String start_date;
       	if(temp_m < 2018)
       		start_date = "2018-01";
       	else
       		start_date= Integer.toString(temp_m)+"-"+end_date.split("-")[1]; 
       	for(int i=0; i<1200; i++) {
       		List<String> temp= new ArrayList<>();
       		temp.add("0");
       		temp.add("0");
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		List<String> temp= new ArrayList<>();
       		String day = new String(chart.get(dto.getDate_cat()).get(0));
       		String night = new String(chart.get(dto.getDate_cat()).get(1));
       		if(dto.getItem().equals("day")) 
       			day = Integer.toString(dto.getSubitem1());
       		else 
       			night = Integer.toString(dto.getSubitem1());
       		temp.add(day);
       		temp.add(night);
       		chart.put(dto.getDate_cat(), temp);
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countYearlyDayNight") 
    public  Map<String,List<String>> countYearlyDayNight() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countYearlyDayNight();
       	if(list.size() == 0) {
       		return chart; 
       	}
       
      	
     	String start_date = "2018";
     	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
       	String end_date = sdf.format(date);
       	for(int i=0; i<120; i++) {
       		List<String> temp= new ArrayList<>();
       		temp.add("0");
       		temp.add("0");
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		year += 1; 
       		start_date = Integer.toString(year);
       	}
       	for(StatisticsDTO dto: list) {
       		List<String> temp= new ArrayList<>();
       		String day = new String(chart.get(dto.getDate_cat()).get(0));
       		String night = new String(chart.get(dto.getDate_cat()).get(1));
       		if(dto.getItem().equals("day")) 
       			day = Integer.toString(dto.getSubitem1());
       		else 
       			night = Integer.toString(dto.getSubitem1());
       		temp.add(day);
       		temp.add(night);
       		chart.put(dto.getDate_cat(), temp);
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countMonthlySupportType") 
    public  Map<String,List<String>> countMonthlySupportType() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countMonthlySupportType();
       	List<StatisticsDTO> support_list = statisticsDAO.spportTypeList();
       	List<String> sup_list = new ArrayList<>();
       	for(StatisticsDTO dto: support_list) {
   			sup_list.add(dto.getItem());
   		}
       	if(list.size() == 0) {
       		return chart; 
       	}
       
       	String start_date = "2018-01";
       	String end_date = list.get(list.size()-1).getDate_cat();
       	for(int i=0; i<1200; i++) {
       		List<String> temp= new ArrayList<>();
       		for(int j=0; j<sup_list.size(); j++) {
       			temp.add("0");
       		}
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		int index = sup_list.indexOf(dto.getItem());
       		chart.get(dto.getDate_cat()).set(index,Integer.toString(dto.getSubitem1()));
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countRecentTwelveMonthlySupportType") 
    public  Map<String,List<String>> countRecentTwelveMonthlySupportType() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countRecentTwelveMonthlySupportType();
       	List<StatisticsDTO> support_list = statisticsDAO.spportTypeList();
       	List<String> sup_list = new ArrayList<>();
       	for(StatisticsDTO dto: support_list) {
   			sup_list.add(dto.getItem());
   		}
       	if(list.size() == 0) {
       		return chart; 
       	}
       
       	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       	String end_date = sdf.format(date);

       	int temp_m =Integer.parseInt(end_date.split("-")[0])-1;
     	String start_date;
       	if(temp_m < 2018)
       		start_date = "2018-01";
       	else
       		start_date= Integer.toString(temp_m)+"-"+end_date.split("-")[1];
       	for(int i=0; i<2400; i++) {
       		List<String> temp= new ArrayList<>();
       		for(int j=0; j<sup_list.size(); j++) {
       			temp.add("0");
       		}
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		int index = sup_list.indexOf(dto.getItem());
       		chart.get(dto.getDate_cat()).set(index,Integer.toString(dto.getSubitem1()));
    	}
       	return chart;  
    } 
	
    @ResponseBody
    @RequestMapping("/statistics/countRecentTwentyfourMonthlySupportType") 
    public  Map<String,List<String>> countRecentTwentyfourMonthlySupportType() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countRecentTwentyfourMonthlySupportType();
       	List<StatisticsDTO> support_list = statisticsDAO.spportTypeList();
       	List<String> sup_list = new ArrayList<>();
       	for(StatisticsDTO dto: support_list) {
   			sup_list.add(dto.getItem());
   		}
       	if(list.size() == 0) {
       		return chart; 
       	}
       
       	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
       	String end_date = sdf.format(date);

       	int temp_m =Integer.parseInt(end_date.split("-")[0])-2;
     	String start_date;
       	if(temp_m < 2018)
       		start_date = "2018-01";
       	else
       		start_date= Integer.toString(temp_m)+"-"+end_date.split("-")[1];
       	for(int i=0; i<2400; i++) {
       		List<String> temp= new ArrayList<>();
       		for(int j=0; j<sup_list.size(); j++) {
       			temp.add("0");
       		}
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int month =Integer.parseInt(start_date.split("-")[1]);
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		if(month == 12) {
       			year += 1; 
       			month = 1;
       		}
       		else 
       			month += 1;
       		String m = new String(Integer.toString(month));
       		if(month < 10) 
       			m = "0" + m;
       		start_date = Integer.toString(year)+"-"+m;
       	}
       	for(StatisticsDTO dto: list) {
       		int index = sup_list.indexOf(dto.getItem());
       		chart.get(dto.getDate_cat()).set(index,Integer.toString(dto.getSubitem1()));
    	}
       	return chart;  
    } 
    
    @ResponseBody
    @RequestMapping("/statistics/countYearlySupportType") 
    public  Map<String,List<String>> countYearlySupportType() {
       	Map<String, List<String>> chart = new LinkedHashMap<>();
       	
       	List<StatisticsDTO> list = statisticsDAO.countYearlySupportType();
       	if(list.size() == 0) {
       		return chart; 
       	}
     	List<StatisticsDTO> support_list = statisticsDAO.spportTypeList();
       	List<String> sup_list = new ArrayList<>();
       	for(StatisticsDTO dto: support_list) {
   			sup_list.add(dto.getItem());
   		}
      	
     	String start_date = "2018";
     	Date date = new Date(); 
     	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
       	String end_date = sdf.format(date);
       	for(int i=0; i<120; i++) {
       		List<String> temp= new ArrayList<>();
     		for(int j=0; j<sup_list.size(); j++) {
       			temp.add("0");
       		}
       		chart.put(start_date, temp);
       		if(start_date.equals(end_date))
       			break;
       		int year = Integer.parseInt(start_date.split("-")[0]);
       		year += 1; 
       		start_date = Integer.toString(year);
       	}
       	for(StatisticsDTO dto: list) {
       		int index = sup_list.indexOf(dto.getItem());
       		chart.get(dto.getDate_cat()).set(index,Integer.toString(dto.getSubitem1()));
    	}
       	
       	return chart;  
    } 
    

	// Í∑∏Î¶¨?ìú1?óê ???ïú Î™®Îìà
 	@RequestMapping("/grid/equipTicketGrid")
 	public String equipTicketGrid() {
 		return equipTicketGridURL; 
 	}
}