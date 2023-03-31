package Saksoft_Project.com.saksoft.first.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Saksoft_Project.com.saksoft.first.model.Employee;
import Saksoft_Project.com.saksoft.first.repository.EmployeeRepository;
import Saksoft_Project.com.saksoft.first.service.Servicei;

@Service
public class HomeService implements Servicei {

	@Autowired
	private EmployeeRepository erp;

	@Override
	public Employee savedata(Employee e) {
		Employee emp = erp.save(e);
		return emp;
	}

	@Override
	public List<Employee> getdata() {
		List<Employee> list = erp.findAll();
		return list;
	}

	@Override
	public void deletedata(int empId) {

		erp.deleteById(empId);
		;
	}

	@Override
	public Employee updateEndDate(int empId,Employee e) {

		Optional<Employee> em = erp.findById(empId);

		if (em.isPresent()) {
			Employee emp1 = em.get();
			
			if (emp1.getFirstName()!= null) {
				emp1.setFirstName(e.getFirstName());
			}
			if (emp1.getLastName()!= null) {
				emp1.setLastName(e.getLastName());
			}
			if (emp1.getEmailId()!= null) {
				emp1.setEmailId(e.getEmailId());
			}
			if (emp1.getAge()!= 0) {
				emp1.setAge(e.getAge());
			}
			Employee saveEmp = erp.save(emp1);
			return saveEmp;
		}else
		return null;

	}
}
