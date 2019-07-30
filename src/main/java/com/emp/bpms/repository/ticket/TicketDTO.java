package com.emp.bpms.repository.ticket;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TicketDTO {
	private long ticket_id;
	private String ticket_time;
	private long req_user_id;
	private String req_user_name;
	private long req_user_team_id;
	private String req_user_team;
	private long exe_user_id;
	private String exe_user_name;
	private long exe_user_team_id;
	private String exe_user_team;
	private long equipment_id;
	private String category1;
	private String category2;
	private String category3;
	private String equipment_loc;
	private long work_type_id;
	private String work_type;
	private long support_id;
	private String support_name;
	private String ticket_subject;
	private String ticket_content;
	private String ticket_content_etc;
	private String time_start;
	private String time_end;
	private int progress;
}
