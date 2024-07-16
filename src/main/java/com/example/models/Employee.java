package com.example.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Employee {

	private Integer id;
	private Integer gender_id;
	private Integer job_id;
	private String name;
	private String last_name;
	private Date birthdate;

}
