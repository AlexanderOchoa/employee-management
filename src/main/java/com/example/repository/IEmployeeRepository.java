package com.example.repository;

import com.example.models.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface IEmployeeRepository {

    Integer createEmployee(Map<String, Object> parameters);
    Employee getEmployeeId(Integer id);
    List<Employee> getEmployeeByNames(String name, String lastName);
    Gender getGender(Integer id);
    Job getJob(Integer id);
    Integer addHourEmployee(Map<String, Object> parameters);
    List<Employee> getEmployeeByJob(Integer job_id);
    Integer getTotalHour(Integer employee_id, Date start_date, Date end_date);
    Double getPay(Integer employee_id, Date start_date, Date end_date);
}
