<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>

<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Employee Management Application</title>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.0/css/jquery.dataTables.css">

</head>
<body>

	<div class="container mb-3 mt-3">

	
		<!-- Buttons -->
		<div class="btn-group">
			<button class="btn btn-info" style="background-color:#3399ff" type="button" data-toggle="modal" data-target="#addModel">Add Employee </button>
	
			<form method="get" action="/deleteEmployee">
		    	<button type="button" class="btn btn-danger" name="deleteButton" id="delete" disabled="disabled">Delete Employee</button>
			</form>
		</div>
	
		<!-- Modal -->
		<div id="addModel" class="modal fade" role="dialog">
		  <div class="modal-dialog">
		
		    <!-- Modal content-->
		    <div class="modal-content">
		      <div class="modal-header">
		      <h4 class="modal-title">Add Employee</h4>
		        <button type="button" class="close" data-dismiss="modal">&times;</button>
		      </div>
				<div class="container col-md5">
					<div class="card">
						<div class="card-body">
						
								<form action="insert" method="post">
								
								
								<fieldset class="form-group">
									<label>Name</label> <input type="text"
										class="form-control"
										name="name" required="required">
								</fieldset>
			
								<fieldset class="form-group">
									<label>Email</label> <input type="text"
										class="form-control"
										name="email">
								</fieldset>
			
			
								<fieldset class="form-group">
									<label>Position</label> <input type="text"
										class="form-control"
										name="position">
								</fieldset>
				
								<fieldset class="form-group">
									<label>Salary</label> <input type="text"
										class="form-control"
										name="salary">
								</fieldset>
				
								<button type="submit" class="btn btn-success">Save</button>
								</form>
						</div>
					</div>
				</div>
		    </div>
		
		  </div>
		  
		</div>
		<!-- DataTable -->
		<table class="table table-striped table-bordered" id="employeeTable" style="width: 100%">
			<thead>
				
				<tr>
					<th colspan="6" style="background-color:#3399ff">Employees</th>
				</tr>
				<tr>
					<th>#</th>
					<th>ID</th>
					<th>Name</th>
					<th>Email</th>
					<th>Position</th>
					<th>Salary</th>
				</tr>
			</thead>
	
			<tbody>

			</tbody>
		</table>
	</div>
	
	
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

	

	<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
	<script src="https://cdn.datatables.net/1.10.19/js/dataTables.bootstrap4.min.js"></script>

	<script>
		$(document).ready(function(){

			//Datatable creation and configuration.
			var employeeTable = $('#employeeTable').DataTable({
				searching: false,
				ordering: false,
				select: true,
				serverSide: true,
				processing: true,
			    paging: true,
			    pagingType: "simple_numbers",
			    
			    lengthMenu: [[5, 10, 25, 50,-1], [5, 10, 25, 50, "All"]],
			    
			    //
				ajax: {
					url: "getEmployees",
					dataType: 'json',
					dataSrc: function(response){

						/*Number of elements in database use for
						* pagination in datatable dynamically.
						*/
						numberOfElements=getNumberOfElements()
					    response.recordsTotal =  numberOfElements
					    response.recordsFiltered = numberOfElements

					    return response;
					}
					
				},
				 columns: [
					 	//Column index on datatable.
	                   	{ data: '#', 
		                    render: function (data, type, row, meta) {
		                        return meta.row + meta.settings._iDisplayStart + 1;
		                    }},
		                {data: 'id'},
	                    { data: 'name' }, 
	                    { data: 'email' },
	                    { data: 'position' },
	                    { data: 'salary' },
	                ],
	                
	             buttons: [
	                    { extend: 'deleteButton', name: "deleteButton"}
	                ],


				});
			//Defination for pagination ellipses representation.
			$.fn.DataTable.ext.pager.numbers_length = 5;
		    
		    
		    
		    //Send selected employee id to servlet for delete operation.
			var selectedEmployeeId = -1
		    $('#employeeTable tbody').on( 'click', 'tr', function () {
		    	selectedEmployeeId = employeeTable.row( this ).data().id

		        if ( $(this).hasClass('selected') ) {
		            $(this).removeClass('selected');

		            //Enable delete button when there is a selected row.
		            document.getElementById("delete").disabled = true;
		            
		            
		        }
		        else {
		        	employeeTable.$('tr.selected').removeClass('selected');
		            $(this).addClass('selected');
		            
		            //Disable delete button whene there is no selected row.
		            document.getElementById("delete").disabled = false;
		        }
		        
		        
		    } );
		    
		    //Delete event.
		    $('#delete').click( function () {
		    	employeeTable.row('.selected').remove().draw( false );
		    	$.ajax({
					  url: "deleteEmployee",
					  type: "get",
					  data: {
						  employeeId: selectedEmployeeId
						  },

					  success: function() {
						  employeeTable.ajax.reload();
					  },
					  error: function(xhr) {
						  console.log("Error:", error);
					  }
			
					});
		    	
		    	//Disable delete button after delete operation.
		    	document.getElementById("delete").disabled = true;
		    	
		    } );

		})
		

	//Retrieve number of elements in database
	function getNumberOfElements(){
			var count = 0
			$.ajax({
				  url: "numberOfElements",
				  type: "get",
				  async: false,

				  success: function(elements) {
					  count = elements
		
				  },
				  error: function(xhr) {
					  console.log("Error:", error);
				  }
		
				});
			return count
			
			}		


	</script>

</body>
</html>