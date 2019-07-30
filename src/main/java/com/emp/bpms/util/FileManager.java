package com.emp.bpms.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.assertj.core.util.Arrays;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ArrayUtils;

import com.emp.bpms.repository.reply.ReplyDTO;
import com.emp.bpms.repository.requestform.RequestFormDTO;
import com.emp.bpms.repository.ticket.TicketDTO;
import com.skt.bpms.StaticFlag;
import com.skt.bpms.StaticFlag.FileType;

public class FileManager {


	
	public File getFile(TicketDTO ticketDTO) throws IOException {
		return _getFirstFile(StaticFlag.getFilePath((int)ticketDTO.getTicket_id(), FileType.TICKET));
	}

	public File getFile(TicketDTO ticketDTO, String filename) throws IOException {
		if(filename == null) {
			return _getFirstFile(StaticFlag.getFilePath((int)ticketDTO.getTicket_id(), FileType.TICKET));			
		} else {
			return _getFile(StaticFlag.getFilePath((int)ticketDTO.getTicket_id(), FileType.TICKET), filename);
		}
	}	
	
	public File getFile(ReplyDTO replyDTO) throws IOException {
		return _getFirstFile(StaticFlag.getFilePath((int)replyDTO.getReply_id(), FileType.REPLY));
	}

	public File getFile(ReplyDTO replyDTO, String filename) throws IOException {
		if(filename == null) {
			return _getFirstFile(StaticFlag.getFilePath((int)replyDTO.getReply_id(), FileType.REPLY));
		} else {
			return _getFile(StaticFlag.getFilePath((int)replyDTO.getReply_id(), FileType.REPLY), filename);
		}
	}
	
	public File getFile(RequestFormDTO requestformDTO) throws IOException {
		return _getFirstFile(StaticFlag.getFilePath((int)requestformDTO.getDoc_id(), FileType.REQUESTFORM));
	}

	public File getFile(RequestFormDTO requestformDTO, String filename) throws IOException {
		if(filename == null) {
			return _getFirstFile(StaticFlag.getFilePath((int)requestformDTO.getDoc_id(), FileType.REQUESTFORM));
		} else {
			return _getFile(StaticFlag.getFilePath((int)requestformDTO.getDoc_id(), FileType.REQUESTFORM), filename);
		}
	}

	public List<File> getFiles(TicketDTO ticketDTO) throws IOException {
		return _getFiles((int)ticketDTO.getTicket_id(), StaticFlag.FileType.TICKET);
	}

	////////////////////////
	
	private File _getFirstFile(String path) throws IOException {
		for(File file : new File(path).listFiles()) if(file.isFile()) return file;
		return null;
	}
	
	private File _getFile(String path, String filename) throws IOException {
		return new File(path, filename);
	}

	private List<File> _getFiles(int id, FileType fileType) throws IOException {
		return _getFiles(StaticFlag.getFilePath(id, fileType));
	}

	private List<File> _getFiles(String path) throws IOException {
		List<File> fileList = new ArrayList<File>();
		for(File file : new File(path).listFiles()) if(file.isFile()) fileList.add(file);
		return fileList;
	}
	
	public boolean uploadFiles(TicketDTO ticketDTO, List<MultipartFile> fileList) throws IOException {
		for(MultipartFile file : fileList) {
			if(!uploadFile(ticketDTO, file)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean uploadFile(TicketDTO ticketDTO, MultipartFile file) throws IOException {
		return _uploadFile(StaticFlag.getFilePath((int)ticketDTO.getTicket_id(), FileType.TICKET), file);
	}
	
	public boolean uploadFile(ReplyDTO replyDTO, MultipartFile file) throws IOException {
		return _uploadFile(StaticFlag.getFilePath((int)replyDTO.getReply_id(), FileType.REPLY), file);
	}
	
	public boolean uploadFile(RequestFormDTO requestformDTO, MultipartFile file) throws IOException {		
		return _uploadFile(StaticFlag.getFilePath(requestformDTO.getDoc_id(), FileType.REQUESTFORM), file);
	}
	
	private boolean _uploadFile(String path, MultipartFile file) throws IOException {
    	File dstFile = new File(path, file.getOriginalFilename());
    	System.out.println(dstFile.getAbsolutePath());
    	File dstFolder = new File(path);
    	dstFolder.mkdirs();
   		file.transferTo(dstFile);
		return true;
	}
	
	
}
