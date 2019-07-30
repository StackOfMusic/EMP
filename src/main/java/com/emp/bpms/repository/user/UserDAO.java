package com.emp.bpms.repository.user;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skt.bpms.StaticFlag;

@Repository
public class UserDAO {
	
    @Autowired
    private SqlSession sqlSession;

    public List<UserDTO> selectAllUser() {
    	if( StaticFlag.debug == true) {
    		List<UserDTO> userList = new ArrayList<UserDTO>();
    		userList.add(new UserDTO());
    		userList.get(0).setId(1);
    		userList.get(0).setName("?´ë¦?");
    		userList.get(0).setMobile("010-0101-0101");
    		userList.get(0).setTeam_id(1);
    		userList.get(0).setEnable(true);
    		userList.get(0).setAuthority_id(1);
    		
    		return userList;
    	}
    	return sqlSession.selectList("user.selectAllUser");
    }
    
    public UserDTO selectLoginUser(String email) {
    	return sqlSession.selectOne("user.selectLoginUser", email);
    }

    public UserDTO selectUserById(int id) {
    	return sqlSession.selectOne("user.selectUserById", id);
    }    
   
    
    public boolean insertUser(UserDTO userDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.insert("user.insertUser", userDTO) > 0);
    }
    
    public boolean updateUser(UserDTO userDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
        return (sqlSession.update("user.updateUser", userDTO) > 0);
    }
    
    public boolean deleteUser(int id) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.delete("user.deleteUser", id) > 0);
    }
    public int selectIdByName(String name) {
    	return sqlSession.selectOne("user.selectIdByName", name);
    }
}
