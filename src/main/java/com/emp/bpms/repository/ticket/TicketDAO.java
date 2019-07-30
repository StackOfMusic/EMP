package com.emp.bpms.repository.ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skt.bpms.model.TicketSearchModel;

@Repository
public class TicketDAO {
	
    @Autowired
    private SqlSessionTemplate sqlSession;

    public List<TicketDTO> selectAllList() {
    	return sqlSession.selectList("ticket.selectAllTicket");
    }

    public TicketDTO selectOneTicket(int ticket_id) {
    	return sqlSession.selectOne("ticket.selectOneTicket", ticket_id);
    }    
    
    public List<TicketDTO> selectReqList(int id) {
    	return sqlSession.selectList("ticket.selectReqTicket", id);
    }    

    public List<TicketDTO> selectRecvList(int id) {
    	return sqlSession.selectList("ticket.selectRecvTicket", id);
    }        
    
    public List<TicketDTO> selectList(TicketSearchModel tsm) {
    	return sqlSession.selectList("ticket.selectAllTicket");
    }
  
    public boolean insertTicket(TicketDTO ticketDTO) {
    	return (sqlSession.insert("ticket.insertTicket", ticketDTO) > 0);
    }
    
    public boolean updateTicket(TicketDTO ticketDTO) {
    	return (sqlSession.update("ticket.updateTicket", ticketDTO) > 0);
    }
    
    public boolean deleteTicket(int ticket_id) {
    	return (sqlSession.delete("ticket.deleteTicket", ticket_id) > 0);
    }

    public boolean acceptTicket(int ticket_id) {
    	return (sqlSession.delete("ticket.acceptTicket", ticket_id) > 0);
    }
    
    public boolean submitTicket(int ticket_id) {
    	return (sqlSession.delete("ticket.submitTicket", ticket_id) > 0);
    }

    public boolean finishTicket(int ticket_id) {
    	return (sqlSession.delete("ticket.finishTicket", ticket_id) > 0);
    }

    
    public List<TicketDTO> listCriteria(int pageStart, int perPageNum) {
    	Map<String, Object> page_data = new HashMap<String, Object>();
    	page_data.put("pageStart", pageStart);
    	page_data.put("perPageNum", perPageNum);
    	return sqlSession.selectList("ticket.listCriteria", page_data);
    }
    
    public int listCountCriteria() {
    	return sqlSession.selectOne("ticket.listCountCriteria");
    }
}
