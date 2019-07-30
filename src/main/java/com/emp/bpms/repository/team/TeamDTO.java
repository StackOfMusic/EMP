package com.emp.bpms.repository.team;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TeamDTO {
	private int team_id;
	private String type;
	private String category1;
	private String category2;
	private String mail;
}
