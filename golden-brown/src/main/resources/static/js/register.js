$(document).ready(
	function() {
		$("#register").on("click", function(event) {
			var modal = bootbox.dialog({
				message: $("#form-content-register").html(),
				title: "Register",
				buttons: [
		          {
		        	  label: "Save",
		        	  className: "btn btn-primary pull-left",
		        	  callback: function() {
		        		  var form = modal.find("form#my-registry");
		        		  var items = form.serialize();
		        		  if (items != null) {
			        		  $.ajax({
			        			  type : "POST",
			                      url : "/register",
			                      data : items,
			                      success : function(msg) {
					        		  modal.modal("hide");
				        			  bootbox.alert("User created. Please login");
			                      },
			                      error : function(msg) {
				        			  bootbox.alert("Incorrect data. Try again");
			                      }
			        		  });
		        		  }
		        		  return false;
		        		  }
		        	  }
		        	  ],
		        	  show: false,
		        	  onEscape: function() {
		        		  modal.modal("hide");
		        	  }
				});			    
			   	modal.modal("show");
			});
	});