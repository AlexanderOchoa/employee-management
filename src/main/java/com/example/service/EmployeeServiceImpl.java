package com.example.service;

import com.example.EmployeesApiDelegate;
import com.example.model.*;
import com.example.models.Employee;
import com.example.models.Gender;
import com.example.models.Job;
import com.example.repository.impl.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeesApiDelegate {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<CreateEmployeeResponse> createEmployee(CreateEmployeeRequest request) {
        List<Employee> employees = employeeRepository.getEmployeeByNames(request.getName(), request.getLastName());
        Job job = employeeRepository.getJob(request.getJobId());
        Gender gender = employeeRepository.getGender(request.getGenderId());
        long differenceInYears = getDifferenceYears(request.getBirthdate());
        Integer id = null;
        boolean statusResponse = false;

        if (employees.isEmpty() && job != null && gender != null && differenceInYears >= 18) {
            Map<String, Object> parametro = getParamsToRegister(request);
            id = employeeRepository.createEmployee(parametro);
            statusResponse = true;
        }

        CreateEmployeeResponse createEmployeeResponse = new CreateEmployeeResponse();
        createEmployeeResponse.setId(id);
        createEmployeeResponse.setSuccess(statusResponse);

        return new ResponseEntity<>(createEmployeeResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AddHourEmployeeResponse> addHours(AddHourEmployeeRequest request) {
        Employee employee = employeeRepository.getEmployeeId(request.getEmployeeId());
        Integer id = null;
        boolean statusResponse = false;

        if (employee != null) {
            Map<String, Object> parametro = getParamsToAddHours(request);
            id = employeeRepository.addHourEmployee(parametro);
            statusResponse = true;
        }

        AddHourEmployeeResponse addHourEmployeeResponse = new AddHourEmployeeResponse();
        addHourEmployeeResponse.setId(id);
        addHourEmployeeResponse.setSuccess(statusResponse);

        return new ResponseEntity<>(addHourEmployeeResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetEmployeeResponse> getEmployee(GetEmployeeRequest request) {
        Job job = employeeRepository.getJob(request.getJobId());
        List<GetEmployeeDataResponse> employees = null;
        boolean statusResponse = false;

        if (job != null) {
           List<Employee> employeesDb = employeeRepository.getEmployeeByJob(request.getJobId());
           employees = getEmployeeDataResponse(employeesDb, job);

           statusResponse = true;
        }

        GetEmployeeResponse getEmployeeResponse = new GetEmployeeResponse();
        getEmployeeResponse.setEmployees(employees);
        getEmployeeResponse.setSuccess(statusResponse);

        return new ResponseEntity<>(getEmployeeResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetTotalHourEmployeeResponse> getTotalHours(GetTotalHourEmployeeRequest request) {
        Employee employee = employeeRepository.getEmployeeId(request.getEmployeeId());
        BigDecimal totalWorkedHours = null;
        boolean statusResponse = false;

        if (employee != null && validateDates(request.getStartDate(), request.getEndDate())) {
            Integer totalHour = employeeRepository.getTotalHour(
                    request.getEmployeeId(),
                    getDateFromString(request.getStartDate()),
                    getDateFromString(request.getEndDate())
            );
            totalWorkedHours = BigDecimal.valueOf(totalHour);
            statusResponse = true;
        }

        GetTotalHourEmployeeResponse getTotalHourEmployeeResponse = new GetTotalHourEmployeeResponse();
        getTotalHourEmployeeResponse.setTotalWorkedHours(totalWorkedHours);
        getTotalHourEmployeeResponse.setSuccess(statusResponse);

        return new ResponseEntity<>(getTotalHourEmployeeResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetSalaryEmployeeResponse> getSalary(GetSalaryEmployeeRequest request) {
        Employee employee = employeeRepository.getEmployeeId(request.getEmployeeId());
        BigDecimal payment = null;
        boolean statusResponse = false;

        if (employee != null && validateDates(request.getStartDate(), request.getEndDate())) {
            Double paymentDb = employeeRepository.getPay(
                    request.getEmployeeId(),
                    getDateFromString(request.getStartDate()),
                    getDateFromString(request.getEndDate())
            );
            payment = BigDecimal.valueOf(paymentDb);
            statusResponse = true;
        }

        GetSalaryEmployeeResponse getEmployeeResponse = new GetSalaryEmployeeResponse();
        getEmployeeResponse.setPayment(payment);
        getEmployeeResponse.setSuccess(statusResponse);

        return new ResponseEntity<>(getEmployeeResponse, HttpStatus.OK);
    }

    private Long getDifferenceYears(String birthdate) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = simpleDateFormat.parse(birthdate);

            long diffInMillisecons = Math.abs(new Date().getTime() - birthDate.getTime());
            return (diffInMillisecons / (1000L * 60 * 60 * 24 * 365));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return 0L;
        }
    }

    private Date getDateFromString(String dateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private String getStringFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    private Map<String, Object> getParamsToRegister(CreateEmployeeRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("gender_id", request.getGenderId());
        params.put("job_id", request.getJobId());
        params.put("name", request.getName());
        params.put("last_name", request.getLastName());
        params.put("birthdate", getDateFromString(request.getBirthdate()));
        return params;
    }

    private Map<String, Object> getParamsToAddHours(AddHourEmployeeRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("employee_id", request.getEmployeeId());
        params.put("worked_hours", request.getWorkedHours());
        params.put("worked_date", getDateFromString(request.getWorkedDate()));
        return params;
    }

    private List<GetEmployeeDataResponse> getEmployeeDataResponse(List<Employee> employeesDb, Job job) {
        return employeesDb.stream().map(employee -> {
            GetEmployeeDataResponse getEmployeeDataResponse = new GetEmployeeDataResponse();
            getEmployeeDataResponse.setId(employee.getId());
            getEmployeeDataResponse.setName(employee.getName());
            getEmployeeDataResponse.setLastName(employee.getLast_name());
            getEmployeeDataResponse.setBirthdate(getStringFromDate(employee.getBirthdate()));
            getEmployeeDataResponse.setId(employee.getId());

            GetJobDataResponse getJobDataResponse = new GetJobDataResponse();
            getJobDataResponse.setId(job.getId());
            getJobDataResponse.setName(job.getName());
            getJobDataResponse.setSalary(BigDecimal.valueOf(job.getSalary()));

            Gender gender = employeeRepository.getGender(employee.getGender_id());
            GetGenderDataResponse getGenderDataResponse = new GetGenderDataResponse();
            getGenderDataResponse.setId(gender.getId());
            getGenderDataResponse.setName(gender.getName());

            getEmployeeDataResponse.setGender(getGenderDataResponse);
            getEmployeeDataResponse.setJob(getJobDataResponse);

            return getEmployeeDataResponse;
        }).collect(Collectors.toList());
    }

    private Boolean validateDates(String startDateStr, String endDateStr) {
        Date startDate = getDateFromString(startDateStr);
        Date endDate = getDateFromString(endDateStr);
        return endDate.after(startDate);
    }

}
