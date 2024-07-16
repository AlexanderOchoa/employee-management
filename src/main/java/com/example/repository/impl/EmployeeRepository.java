package com.example.repository.impl;

import com.example.models.*;
import com.example.repository.IEmployeeRepository;
import com.example.repository.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeRepository implements IEmployeeRepository {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Integer createEmployee(Map<String, Object> parameters) {
        return employeeMapper.createEmployee(parameters);
    }

    @Override
    public Employee getEmployeeId(Integer id) {
        return employeeMapper.getEmployeeId(id);
    }

    @Override
    public List<Employee> getEmployeeByNames(String name, String lastName) {
        return employeeMapper.getEmployeeByNames(name, lastName);
    }

    @Override
    public Gender getGender(Integer id) {
        return employeeMapper.getGender(id);
    }

    @Override
    public Job getJob(Integer id) {
        return employeeMapper.getJob(id);
    }

    @Override
    public Integer addHourEmployee(Map<String, Object> parameters) {
        return employeeMapper.addHourEmployee(parameters);
    }

    @Override
    public List<Employee> getEmployeeByJob(Integer job_id) {
        return employeeMapper.getEmployeeByJob(job_id);
    }

    @Override
    public Integer getTotalHour(Integer employee_id, Date start_date, Date end_date) {
        return employeeMapper.getTotalHour(employee_id, start_date, end_date);
    }

    @Override
    public Double getPay(Integer employee_id, Date start_date, Date end_date) {
        return employeeMapper.getPay(employee_id, start_date, end_date);
    }

}
