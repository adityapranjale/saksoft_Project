package Saksoft_Project.com.saksoft.first.service;

import java.util.List;
import java.util.Optional;

import Saksoft_Project.com.saksoft.first.model.Employee;

public interface Servicei {

	Employee savedata(Employee e);

	List<Employee> getdata();

	void deletedata(int empId);

	Employee updateEndDate(int empId,Employee e);

}
