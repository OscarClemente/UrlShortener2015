$(document).ready(
	function() {
		$("#user").on("click", function(event) {
			var modal = bootbox.dialog({
				message: $("#form-content-modify").html(),
				title: "Profile",
				buttons: [
		          {
		        	  label: "Save",
		        	  className: "btn btn-primary pull-left",
		        	  callback: function() {
		        		  var form = modal.find("form#my-modification");
		        		  var items = form.serialize();
		        		  if (items != null) {
			        		  $.ajax({
			        			  type : "POST",
			                      url : "/update",
			                      data : items,
			                      success : function(msg) {
					        		  modal.modal("hide");
				        			  bootbox.alert("Data updated");
				        		      $("#user").html(
				        		    		  "<a id='user' class='btn-info btn-md btn'>" 
				        		    		  + msg.nick + "</a>");
			                      },
			                      error : function(msg) {
				        			  bootbox.alert("Incorrect update. Try again");
			                      }
			        		  });
		        		  }
		        		  return false;
		        		  }
		        	  },
		        	  {
		        		  label: "Close",
		        		  className: "btn btn-default pull-left"
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