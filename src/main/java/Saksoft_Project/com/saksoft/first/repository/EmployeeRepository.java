package Saksoft_Project.com.saksoft.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Saksoft_Project.com.saksoft.first.model.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

}
