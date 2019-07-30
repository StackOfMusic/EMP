package com.emp.bpms.repository.supportType;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skt.bpms.StaticFlag;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SupportTypeDAO {

    @Autowired
    private SqlSession sqlSession;
    
    public List<SupportTypeDTO> selectAllList() {
    	if( StaticFlag.debug == true) {
    		List<SupportTypeDTO> SupportTypeList = new ArrayList<SupportTypeDTO>();

    		for( int i = 0 ; i < 10 ; i++ ) {
    			SupportTypeList.add(new SupportTypeDTO());
    			SupportTypeList.get(i).setSupport_id(i);
    			SupportTypeList.get(i).setSupport_name("ì§??›?œ ?˜•"+i);
    		}

    		
    		
    		return SupportTypeList;
    	}
    	return sqlSession.selectList("support_type.selectAllSupportType");
    }
    
    
    public boolean insertSupportType(SupportTypeDTO SupportTypeDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.insert("support_type.insertSupportType", SupportTypeDTO) > 0);
    }
    
    public boolean updateSupportType(SupportTypeDTO SupportTypeDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.update("support_type.updateSupportType", SupportTypeDTO) > 0);
    }
    
    public boolean deleteSupportType(int id) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.delete("support_type.deleteSupportType", id) > 0);
    }

}
