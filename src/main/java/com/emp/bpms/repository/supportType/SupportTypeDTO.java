package com.emp.bpms.repository.supportType;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SupportTypeDTO {


		
	private int support_id;
	private String support_name;

}
