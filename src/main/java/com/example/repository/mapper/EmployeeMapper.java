package com.example.repository.mapper;

import com.example.models.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface EmployeeMapper {

    @Select(value = "{CALL create_employee(" +
            "#{gender_id,    javaType=Integer, mode=IN, jdbcType=INTEGER}, " +
            "#{job_id,       javaType=Integer, mode=IN, jdbcType=INTEGER}, " +
            "#{name,         javaType=String, mode=IN, jdbcType=VARCHAR}, " +
            "#{last_name,    javaType=String, mode=IN, jdbcType=VARCHAR}, " +
            "#{birthdate,    javaType=java.util.Date, mode=IN, jdbcType=DATE} " +
            ")" +
            "}")
    @Options(statementType = StatementType.CALLABLE, keyProperty="id", keyColumn="id")
    Integer createEmployee(Map<String, Object> parameters);

    @Select(value = "{CALL get_employee_by_id(" +
            "#{id, javaType=Integer, mode=IN, jdbcType=INTEGER}" +
            ")" +
            "}")
    @Options(statementType = StatementType.CALLABLE)
    Employee getEmployeeId(Integer id);

    @Select(value = "{CALL get_employee_by_names" +
            " ( " +
            "#{name,      javaType=String, mode=IN, jdbcType=VARCHAR}," +
            "#{last_name, javaType=String, mode=IN, jdbcType=VARCHAR}" +
            " ) " +
            " }")
    @Options(statementType = StatementType.CALLABLE)
    List<Employee> getEmployeeByNames(String name, String last_name);

    @Select(value = "{CALL get_gender" +
            " ( " +
            "#{id, javaType=Integer, mode=IN, jdbcType=INTEGER}" +
            " ) " +
            " }")
    @Options(statementType = StatementType.CALLABLE)
    Gender getGender(Integer id);

    @Select(value = "{CALL get_job" +
            " ( " +
            "#{id, javaType=Integer, mode=IN, jdbcType=INTEGER}" +
            " ) " +
            " }")
    @Options(statementType = StatementType.CALLABLE)
    Job getJob(Integer id);

    @Select(value = "{CALL add_hour_employee(" +
            "#{employee_id,    javaType=Integer, mode=IN, jdbcType=INTEGER}, " +
            "#{worked_hours,   javaType=Integer, mode=IN, jdbcType=INTEGER}, " +
            "#{worked_date,    javaType=java.util.Date, mode=IN, jdbcType=DATE} " +
            ")" +
            "}")
    @Options(statementType = StatementType.CALLABLE, keyProperty="id", keyColumn="id")
    Integer addHourEmployee(Map<String, Object> parameters);

    @Select(value = "{CALL get_employee_by_job" +
            " ( " +
            "#{job_id javaType=Integer, mode=IN, jdbcType=INTEGER}" +
            " ) " +
            " }")
    @Options(statementType = StatementType.CALLABLE)
    List<Employee> getEmployeeByJob(Integer job_id);

    @Select(value = "{CALL get_total_hour(" +
            "#{start_date,  javaType=java.util.Date, mode=IN, jdbcType=DATE}, " +
            "#{end_date,    javaType=java.util.Date, mode=IN, jdbcType=DATE}, " +
            "#{employee_id, javaType=Integer, mode=IN, jdbcType=INTEGER}" +
            ")" +
            "}")
    @Options(statementType = StatementType.CALLABLE)
    Integer getTotalHour(Integer employee_id, Date start_date, Date end_date);

    @Select(value = "{CALL get_pay(" +
            "#{start_date,  javaType=java.util.Date, mode=IN, jdbcType=DATE}, " +
            "#{end_date,    javaType=java.util.Date, mode=IN, jdbcType=DATE}, " +
            "#{employee_id, javaType=Integer, mode=IN, jdbcType=INTEGER}" +
            ")" +
            "}")
    @Options(statementType = StatementType.CALLABLE)
    Double getPay(Integer employee_id, Date start_date, Date end_date);
}
