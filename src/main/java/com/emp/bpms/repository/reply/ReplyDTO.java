package com.emp.bpms.repository.reply;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReplyDTO {

	private long reply_id;
	private long ticket_id;
	private long id;
	private String name;
	private String reply_time;
	private String reply_content;
	
	
}
