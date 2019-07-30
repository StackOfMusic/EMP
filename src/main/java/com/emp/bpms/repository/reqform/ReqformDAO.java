package com.emp.bpms.repository.reqform;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReqformDAO {
	
    @Autowired
    private SqlSessionTemplate sqlSession;

    public List<ReqformDTO> selectAllList() {
    	return sqlSession.selectList("reqdata.selectAllData");
    }
}
