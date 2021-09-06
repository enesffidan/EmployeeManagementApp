package model;

public class Employee {
	
	private int id;
	private String name;
	private String email;
	private String position;
	private String salary;
	
	public Employee() {
		
	}

	
	
	public Employee(String name, String email, String position, String salary) {
		super();
		this.name = name;
		this.email = email;
		this.position = position;
		this.salary = salary;
	}



	public Employee(int id, String name, String email, String position, String salary) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.position = position;
		this.salary = salary;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}
	
	
	
	

}
