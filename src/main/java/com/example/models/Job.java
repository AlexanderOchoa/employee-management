package com.example.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Job {

	private Integer id;
	private String name;
	private Double salary;
}
