package com.emp.bpms.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.emp.bpms.model.MyCell;
import com.emp.bpms.repository.requestform.RequestFormDAO;
import com.emp.bpms.util.ExcelPOIHelper;
import com.emp.bpms.util.FileManager;
import com.skt.bpms.StaticFlag;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller 
public class ExcelController { 
	
	@Autowired
	private RequestFormDAO 		requestformDAO;	
	
	
	
    private String fileLocation; 
    @Resource(name = "excelPOIHelper") 
    private ExcelPOIHelper excelPOIHelper;
    
    @RequestMapping(method = RequestMethod.GET, value = "/excelProcessing") 
    public String getExcelProcessingPage() { 
        return "redirect:skt/ticket/fileupload"; 
    } 


    
    @RequestMapping(method = RequestMethod.POST, value = "/readPOI") 
    public String readPOI(Model model, MultipartFile file) throws IOException, ParserConfigurationException { 
    	log.info("in readPOI");

    	FileManager fm = new FileManager();
    	fm.uploadFile(requestformDAO.selectOneRequestForm(3), file);
    	log.info("success!");
    	fileLocation = file.getOriginalFilename();
    	
    	if (fileLocation != null) { 
            if (fileLocation.endsWith(".xlsx") || fileLocation.endsWith(".xls")) {
        		//System.out.println(excelPOIHelper.toHtml(file.getInputStream()));
                Map<Integer, List<MyCell>> data = excelPOIHelper.readExcel(file.getInputStream(), fileLocation);
        		int[] columnWidthList = excelPOIHelper.getColumnWidthList();
        		
                model.addAttribute("data", data);
                model.addAttribute("columnWidths", columnWidthList);
                //
                
            } else { 
            	model.addAttribute("message", "Not a valid excel file!"); 
            } 
        } else {
        	model.addAttribute("message", "File missing! Please upload an excel file."); 
        } 
        return StaticFlag.defaultModuleURI + "/skt/management/excelForm"; 
    } 
} 
