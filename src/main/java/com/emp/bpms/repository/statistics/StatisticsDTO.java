package com.emp.bpms.repository.statistics;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StatisticsDTO {
	private String item;
	private String date_cat;
	private int subitem1;
	private int subitem2;
}
