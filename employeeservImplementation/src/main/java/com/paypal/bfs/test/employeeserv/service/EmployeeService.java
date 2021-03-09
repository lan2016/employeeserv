package com.paypal.bfs.test.employeeserv.service;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.dao.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.mapper.EmployeeMapper;
import com.paypal.bfs.test.employeeserv.model.EmployeeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;


    private final Map<Integer, Employee> employeeMap = new ConcurrentHashMap<>();

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public Optional<Employee> byId(String id){

        Employee employee = employeeMap.get(Integer.valueOf(id));
        if(Objects.nonNull(employee)){
            return Optional.of(employee);
        }

        Optional<EmployeeTable> et = employeeRepository.findById(Integer.valueOf(id));
        if(et.isPresent()){
            Employee e = employeeMapper.toEmployee(et.get());
            if(Objects.nonNull(e)){
                employeeMap.put(e.getId(),e);
                return Optional.of(e);
            }

        }
        return Optional.empty();
    }

    public boolean create(Employee employeeRequest){
        EmployeeTable employeeTable = null;
        try{
            employeeTable = employeeRepository.save(employeeMapper.toEmployeeTable(employeeRequest));
            if(Objects.nonNull(employeeTable)){
                return true;
            }
        }catch(Exception e){
            System.out.println("Error in table creating...");
        }

        return false;
    }

}
