package com.emp.bpms.repository.requestform;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RequestFormDTO {
	private int doc_id;
	private int id;
	private String meta_data;
	private String subject;
	private String file_name;
}
