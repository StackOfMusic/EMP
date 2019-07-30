package com.emp.bpms.repository.requestform;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skt.bpms.StaticFlag;

@Repository
public class RequestFormDAO {

    @Autowired
    private SqlSession sqlSession;
    
    public List<RequestFormDTO> selectAllList() {
    	if( StaticFlag.debug == true) {
    		List<RequestFormDTO> requestformList = new ArrayList<RequestFormDTO>();

    		for( int i = 0 ; i < 3 ; i++ ) {
    			requestformList.add(new RequestFormDTO());
    			requestformList.get(i).setMeta_data("Core?š´?š© ë³¸ë?");
    			requestformList.get(i).setDoc_id(i);
    			requestformList.get(i).setId(i);
    			requestformList.get(i).setFile_name("form.xlsx");
    		}

    		return requestformList;
    	}
    	return sqlSession.selectList("requestform.selectAllRequestForm");
    }
    
    
    public RequestFormDTO selectOneRequestForm(int doc_id) {
    	return sqlSession.selectOne("requestform.selectOneRequestForm", Long.valueOf(""+doc_id));
    }
    
    
    
    public boolean insertRequestForm(RequestFormDTO RequestFormDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.insert("requestform.insertRequestForm", RequestFormDTO) > 0);
    }
    
    public boolean updateRequestForm(RequestFormDTO RequestFormDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.update("requestform.updateRequestForm", RequestFormDTO) > 0);
    }
    
    public boolean deleteRequestFormDTO(int doc_id) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.delete("requestform.deleteRequesForm", doc_id) > 0);
    }//*/
}
