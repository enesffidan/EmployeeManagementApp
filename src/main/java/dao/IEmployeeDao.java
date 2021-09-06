package dao;

import java.util.List;

import model.Employee;
import model.PagingOptions;

public interface IEmployeeDao {
	
	public void insertEmployee(Employee employee);
	
	public void deleteEmployee(int selectedEmployeeId);
	
	public int numberOfElementsInDB();
	
	public List<Employee> listEmployee(PagingOptions pagingOptions);

}
