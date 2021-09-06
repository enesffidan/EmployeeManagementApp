package dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import model.Employee;
import model.PagingOptions;

import java.sql.Connection;

public class EmployeeDao implements IEmployeeDao{
	
	private String dbName = "EmployeeDB.db";
	private String jdbcURL = "jdbc:sqlite:C:\\Users\\Enes\\eclipse-workspace\\EmployeeManagementApp\\" + dbName;
	
	private final String INSERT_EMPLOYEE = "INSERT INTO employeeTable (name, email, position, salary) VALUES (?, ?, ?, ?);";
	private final String NUMBER_OF_ENTRY = "SELECT COUNT(*) AS rowcount FROM employeeTable;";
	private final String DELETE_EMPLOYEE = "DELETE FROM employeeTable WHERE id= ?;";
	private final String SELECT_EMPLOYEES_PAGING="SELECT * FROM employeeTable order by id limit ? offset ?;";

	
	public EmployeeDao() {
		
	}
	
	protected Connection getConnection() {
		Connection connection = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(jdbcURL);
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("getConnection error SQLException: " + e.getMessage());
		}
		
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("getConnection error ClassNotFoundException: " + e.getMessage());
		}

		
		return connection;
	}
	
	public void insertEmployee(Employee employee) {
		
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE)){
			
			preparedStatement.setString(1, employee.getName());
			preparedStatement.setString(2, employee.getEmail());
			preparedStatement.setString(3, employee.getPosition());
			preparedStatement.setString(4, employee.getSalary());
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("SQLException in insertEmployee: " + e.getMessage());
		}
	}
	
	public void deleteEmployee(int selectedEmployeeId) {
		
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE)){
			
			preparedStatement.setInt(1, selectedEmployeeId);
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("delete error: " + e.getMessage());
		}
	}
	
	
	public int numberOfElementsInDB() {
		int numberOfElementsInDB = -1;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(NUMBER_OF_ENTRY)){
			
			ResultSet resultSet = preparedStatement.executeQuery();
			numberOfElementsInDB = resultSet.getInt("rowcount");
			
		} catch (SQLException e) {
			System.out.println("aaa:" + e.getMessage());
		}
		
		return numberOfElementsInDB;
	}
	
	public List<Employee> listEmployee(PagingOptions pagingOptions){
		
		List<Employee> listEmployee = new ArrayList<Employee>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EMPLOYEES_PAGING)){
			
			int limit = pagingOptions.getPageSize();
			int offset = pagingOptions.getCurrentPage();

			preparedStatement.setInt(1, limit);
			preparedStatement.setInt(2, offset);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String position = resultSet.getString("position");
				String salary = resultSet.getString("salary");
				
				Employee employee = new Employee(id, name, email, position, salary);
				listEmployee.add(employee);
			}
			
		} catch (SQLException e) {
			System.out.println("listEmployee erorr: " + e.getMessage());
		}
		
		return listEmployee;
	}
	


	
	
	
	
}
