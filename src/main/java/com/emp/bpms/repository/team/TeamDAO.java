package com.emp.bpms.repository.team;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skt.bpms.StaticFlag;

@Repository
public class TeamDAO {

    @Autowired
    private SqlSession sqlSession;
    
    public List<TeamDTO> selectAllList() {
    	if( StaticFlag.debug == true) {
    		List<TeamDTO> teamList = new ArrayList<TeamDTO>();

    		for( int i = 0 ; i < 3 ; i++ ) {
    			teamList.add(new TeamDTO());
    			teamList.get(i).setCategory1("Core?š´?š© ë³¸ë?");
    			teamList.get(i).setCategory2("? „?†¡?š´?š©"+i+"??");
    			teamList.get(i).setTeam_id(i);
    			teamList.get(i).setType("SKT");
    		}
    		
    		return teamList;
    	}
    	return sqlSession.selectList("team.selectAllTeam");
    }
    
    
    public boolean insertTeam(TeamDTO TeamDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.insert("team.insertTeam", TeamDTO) > 0);
    }
    
    public boolean updateTeam(TeamDTO teamDTO) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	return (sqlSession.update("team.updateTeam", teamDTO) > 0);
    }
    
    public boolean deleteTeam(int id) {
    	if( StaticFlag.debug == true) {
    		return true;
    	}
    	System.out.println(id);
    	return (sqlSession.delete("team.deleteTeam", id) > 0);
    }
    
    public int selectTeamIdByTeamName(String team_name) {
    	return sqlSession.selectOne("team.selectTeamIdByTeamName", team_name);
    }
    
    public TeamDTO selectTeamIdByTeamId(int team_id) {
    	return sqlSession.selectOne("team.selectTeamIdByTeamId", team_id);
    }
}
