package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.*;


import dao.EmployeeDao;
import dao.IEmployeeDao;
import model.Employee;
import model.PagingOptions;


@WebServlet("/")
public class EmployeeServlet extends HttpServlet{


	private static final long serialVersionUID = 1L;
	private IEmployeeDao employeeDao;
	
	public void init() {
		employeeDao = new EmployeeDao();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String action = request.getServletPath();
		try {
			switch (action) {

			case "/deleteEmployee":
				deleteEmployee(request, response);
				break;
			
			case "/getEmployees":
				getEmployees(request, response);
				break;
				
			case "/numberOfElements":
				numberOfElements(request, response);
				break;
				
			default:
				listEmployee(request, response);
				break;
			}
			
		} catch (SQLException e) {
			System.out.println("doGet method error:" + e.getMessage());
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String action = request.getServletPath();
		
		try {
			switch (action) {
			case "/insert":

				insertEmployee(request, response);
				break;
			}

		} catch (SQLException e) {
			System.out.println("doPost method error:" + e.getMessage());
		}

	}
	
	/**
	 * Get required data (page size and current page) from dataTable
	 * then according to data create response
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getEmployees(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, ServletException, IOException {
		

		String pageSize=request.getParameter("length");
		String currentPage = request.getParameter("start");

		PagingOptions pagingOptions = new PagingOptions();
		pagingOptions.setCurrentPage(Integer.parseInt(currentPage));
		pagingOptions.setPageSize(Integer.parseInt(pageSize));
		
		List<Employee> listEmployee = employeeDao.listEmployee(pagingOptions);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(listEmployee);

		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
	    
	}
	
	/**
	 * Send number of elements value in database to client.
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void numberOfElements(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, ServletException, IOException {
		
		int numberOfElements = employeeDao.numberOfElementsInDB();
		String json = new Gson().toJson(numberOfElements);
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
		
	}
	
	/**
	 * Display jsp page.
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void listEmployee(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, ServletException, IOException {

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("employee-list.jsp");
		requestDispatcher.forward(request, response);
				
	}
	
	/**
	 * Get request employee information and insert employee
	 * to database with dao class.
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void insertEmployee(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, ServletException, IOException {
		
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String position = request.getParameter("position");
		String salary = request.getParameter("salary");
		
		
		Employee employee = new Employee(name, email, position, salary);
		employeeDao.insertEmployee(employee);
		response.sendRedirect("/EmployeeManagementApp/");
		
	}
	
	/**
	 * Get selected employee id for delete operation
	 * and delete employee from database with dao class.
	 * @param request
	 * @param response
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, ServletException, IOException {
		
		int id = Integer.parseInt(request.getParameter("employeeId"));
		employeeDao.deleteEmployee(id);
		response.sendRedirect("/EmployeeManagementApp/");
		
	}
	
}
