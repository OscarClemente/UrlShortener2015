$(document).ready(
	function() {
		$("#register").on("click", function(event) {
			var modal = bootbox.dialog({
				message: $(".form-content").html(),
				title: "Register",
				buttons: [
		          {
		        	  label: "Save",
		        	  className: "btn btn-primary pull-left",
		        	  callback: function() {
		        		  var form = modal.find("form#my_form");
		        		  var items = form.serialize();
		        		  if (items != null) {
			        		  $.ajax({
			        			  type : "POST",
			                      url : "/register",
			                      data : items,
			                      success : function(msg) {
					        		  modal.modal("hide");
				        			  bootbox.alert("User created. Welcome");
				        			  var usuario = new User(msg.nick, msg.correo, msg.rolAdmin);
				        			  sessionStorage.setItem('usuario', JSON.stringify(usuario));
			                      },
			                      error : function(msg) {
				        			  bootbox.alert("Incorrect data. Try again");
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