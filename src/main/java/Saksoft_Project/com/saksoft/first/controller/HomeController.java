package Saksoft_Project.com.saksoft.first.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Saksoft_Project.com.saksoft.first.model.Employee;
import Saksoft_Project.com.saksoft.first.repository.EmployeeRepository;
import Saksoft_Project.com.saksoft.first.service.Servicei;

@RestController
@RequestMapping("/saksoft")
public class HomeController {
	@Autowired
	private Servicei sr;
	
	@Autowired
	private EmployeeRepository eri;
	
	@PostMapping("/savedata")
	public ResponseEntity<Employee> savedata(@RequestBody Employee e){
		
							Employee emp=sr.savedata(e);
		
		return new ResponseEntity<Employee>(emp,HttpStatus.CREATED);
	}
	
	@GetMapping("/getdata")
	public ResponseEntity <List<Employee>> fetchData(){
		
						List<Employee> list=sr.getdata();
		
		return new ResponseEntity <List<Employee>>(list, HttpStatus.OK);
	}
	@PutMapping("/updateData/{empId}")
	public ResponseEntity<Employee> updateData(@PathVariable int empId,@RequestBody Employee e){
	
						
		return new ResponseEntity<Employee>(sr.updateEndDate(empId,e), HttpStatus.OK);
						            
						            
	
	
}
	@DeleteMapping("/deleteData/{empId}")
	public ResponseEntity<Employee> deleteData(@PathVariable int empId){
		
								sr.deletedata(empId);
		
		return new ResponseEntity<Employee>(HttpStatus.OK);
	} 
}
