package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;
import com.paypal.bfs.test.employeeserv.util.ValidationUtils;
import org.everit.json.schema.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

    @Autowired
    private EmployeeService employeeService;

  @Autowired
  private ValidationUtils validationUtils;

    private EmployeeResourceImpl(EmployeeService employeeService, ValidationUtils validationUtils) {
        this.employeeService = employeeService;
        this.validationUtils = validationUtils;
    }

    @Override
    public ResponseEntity<Employee> employeeGetById(String id) {

        Optional<Employee> employee = employeeService.byId(id);
        return employee.isPresent() ? new ResponseEntity<>(employee.get(),HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @Override
    public ResponseEntity createEmployee(Employee employeeRequest) {
        List<String>errorsList;
        if(null != employeeRequest.getId() ){
            Optional<Employee> employee = employeeService.byId(employeeRequest.getId()+"");
            if(employee.isPresent()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Employee Exists!");
            }
        }


        errorsList=validationUtils.validateJson(employeeRequest);
            if(errorsList!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsList);
        }

        return employeeService.create(employeeRequest) ? new ResponseEntity<>(HttpStatus.CREATED) :
                new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);

    }



}
