package com.emp.bpms.repository.reply;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skt.bpms.StaticFlag;

@Repository
public class ReplyDAO {

    @Autowired
    private SqlSession sqlSession;
    
    public List<ReplyDTO> selectReplyList(int ticket_id) {
    	if( StaticFlag.debug == true) {
    		List<ReplyDTO> replyList = new ArrayList<ReplyDTO>();

    		for( int i = 0 ; i < 3 ; i++ ) {
    			replyList.add(new ReplyDTO());
    			replyList.get(i).setId(i);
    			replyList.get(i).setTicket_id(i);
    			replyList.get(i).setReply_id(i);
    			replyList.get(i).setName("name1");
    			replyList.get(i).setReply_time("2019/06/24 18:00:00");
    			replyList.get(i).setReply_content("?‚´?š©?ž…?‹ˆ?‹¤. ?´?Ÿ°???Ÿ° ?‚´?š© ?…Œ?Š¤?Š¸?Š¸?Š¸?Š¸?Š¸");
    		}
    		
    		
    		return replyList;
    	}
    	return sqlSession.selectList("reply.selectReplyList", ticket_id);
    }
    
    public List<ReplyDTO> selectAllReply() {
    	if( StaticFlag.debug == true) {
    		List<ReplyDTO> replyList = new ArrayList<ReplyDTO>();

    		for( int i = 0 ; i < 3 ; i++ ) {
    			replyList.add(new ReplyDTO());
    			replyList.get(0).setId(i);
    			replyList.get(0).setTicket_id(i);
    			replyList.get(0).setReply_id(i);
    			replyList.get(0).setName("name1");
    			replyList.get(0).setReply_time("2019/06/24 18:00:00");
    			replyList.get(0).setReply_content("?‚´?š©?ž…?‹ˆ?‹¤. ?´?Ÿ°???Ÿ° ?‚´?š© ?…Œ?Š¤?Š¸?Š¸?Š¸?Š¸?Š¸");
    		}
    		
    		
    		return replyList;
    	}
    	return sqlSession.selectList("reply.selectAllReply");
    }

    public ReplyDTO selectOneReplyById(int reply_id) {

    	return sqlSession.selectOne("reply.selectOneReplyById", reply_id);
    }

    
    
    
    public boolean insertReply(ReplyDTO replyDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.insert("reply.insertReply", replyDTO) > 0);
    }
    
    public boolean updateReply(ReplyDTO replyDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.update("reply.updateReply", replyDTO) > 0);
    }
    
    public boolean deleteReply(int reply_id) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.delete("reply.deleteReply", reply_id) > 0);
    }
}
