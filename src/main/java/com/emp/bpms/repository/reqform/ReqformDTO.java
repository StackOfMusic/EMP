package com.emp.bpms.repository.reqform;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReqformDTO {
	private long req_data_id;
	private String req_data_cont;
}
