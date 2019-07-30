package com.emp.bpms.repository.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.emp.bpms.repository.user.UserDTO;
import com.skt.bpms.StaticFlag;

@Repository
public class StatisticsDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<StatisticsDTO> countMonthlyEquipment() {
		return sqlSession.selectList("statistics.countMonthlyEquipment");
	}

	public List<StatisticsDTO> countRecentTwelveMonthlyEquipment() {
		return sqlSession.selectList("statistics.countRecentTwelveMonthlyEquipment");
	}

	public List<StatisticsDTO> countRecentTwentyfourMonthlyEquipment() {
		return sqlSession.selectList("statistics.countRecentTwentyfourMonthlySupportType");
	}

	public List<StatisticsDTO> countYearlyEquipment() {
		return sqlSession.selectList("statistics.countYearlyEquipment");
	}
	
    public List<StatisticsDTO> countTermEquipment(String term_start, String term_end) {
        Map<String, Object> term = new HashMap<String, Object>();
        term.put("term_start", term_start);
        term.put("term_end", term_end);
        return sqlSession.selectList("statistics.countTermEquipment", term);
	}

	//

	public List<StatisticsDTO> countMonthlyDayNight() {
		return sqlSession.selectList("statistics.countMonthlyDayNight");
	}

	public List<StatisticsDTO> countRecentTwelveMonthlyDayNight() {
		return sqlSession.selectList("statistics.countRecentTwelveMonthlyDayNight");
	}

	public List<StatisticsDTO> countRecentTwentyfourMonthlyDayNight() {
		return sqlSession.selectList("statistics.countRecentTwentyfourMonthlyDayNight");
	}

	public List<StatisticsDTO> countYearlyDayNight() {
		return sqlSession.selectList("statistics.countYearlyDayNight");
	}

	//

	public List<StatisticsDTO> countMonthlySupportType() {
		return sqlSession.selectList("statistics.countMonthlySupportType");
	}

	public List<StatisticsDTO> countRecentTwelveMonthlySupportType() {
		return sqlSession.selectList("statistics.countRecentTwelveMonthlySupportType");
	}

	public List<StatisticsDTO> countRecentTwentyfourMonthlySupportType() {
		return sqlSession.selectList("statistics.countRecentTwentyfourMonthlySupportType");
	}

	public List<StatisticsDTO> countYearlySupportType() {
		return sqlSession.selectList("statistics.countYearlySupportType");
	}

	//

	public List<StatisticsDTO> countMonthlyReqTeam() {
		return sqlSession.selectList("statistics.countMonthlyReqTeam");
	}

	public List<StatisticsDTO> countRecentTwelveMonthlyReqTeam() {
		return sqlSession.selectList("statistics.countRecentTwelveMonthlyReqTeam");
	}

	public List<StatisticsDTO> countRecentTwentyfourMonthlyReqTeam() {
		return sqlSession.selectList("statistics.countRecentTwentyfourMonthlyReqTeam");
	}

	public List<StatisticsDTO> countYearlyReqTeam() {
		return sqlSession.selectList("statistics.countYearlyReqTeam");
	}

	//

	public List<StatisticsDTO> countMonthlyExeTeam() {
		return sqlSession.selectList("statistics.countMonthlyExeTeam");
	}

	public List<StatisticsDTO> countRecentTwelveMonthlyExeTeam() {
		return sqlSession.selectList("statistics.countRecentTwelveMonthlyExeTeam");
	}

	public List<StatisticsDTO> countRecentTwentyfourMonthlyExeTeam() {
		return sqlSession.selectList("statistics.countRecentTwentyfourMonthlyExeTeam");
	}

	public List<StatisticsDTO> countYearlyExeTeam() {
		return sqlSession.selectList("statistics.countYearlyExeTeam");
	}

	//

	public List<StatisticsDTO> countMonthlyWorkingTime() {
		return sqlSession.selectList("statistics.countMonthlyWorkingTime");
	}

	public List<StatisticsDTO> countRecentTwelveMonthlyWorkingTime() {
		return sqlSession.selectList("statistics.countRecentTwelveMonthlyWorkingTime");
	}

	public List<StatisticsDTO> countRecentTwentyfourMonthlyWorkingTime() {
		return sqlSession.selectList("statistics.countRecentTwentyfourMonthlyWorkingTime");
	}

	public List<StatisticsDTO> countYearlyWorkingTime() {
		return sqlSession.selectList("statistics.countYearlyWorkingTime");
	}
    public List<StatisticsDTO> spportTypeList() {
    	return sqlSession.selectList("statistics.spportTypeList");
    }
}
