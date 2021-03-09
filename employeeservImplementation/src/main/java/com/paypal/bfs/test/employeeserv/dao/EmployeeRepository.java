package com.paypal.bfs.test.employeeserv.dao;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.model.EmployeeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeTable,Integer> {

    public EmployeeTable findById(int id);
}
