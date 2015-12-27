$(document).ready(
	function() {
		dataUser();
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
			                      },
			                      error : function(msg) {
				        			  bootbox.alert("Incorrect update. Try again");
			                      },
			                      crossDomain: true,
			                      beforeSend: function(xhr) {
			                    	  var email_encrypted = sessionStorage.getItem('email');
			                          var email = $.parseJSON(email_encrypted);
			                          var password_encrypted = sessionStorage.getItem('password');
			                          var password = $.parseJSON(password_encrypted);
			                      	xhr.setRequestHeader('Authorization', 'Basic ' + window.btoa(unescape(encodeURIComponent(email + ':' + password))))
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

function dataUser() {
	var email_encrypted = sessionStorage.getItem('email');
    var email = $.parseJSON(email_encrypted);
	$("#user").on("click", function(event) {
		$.ajax({
		  type : "GET",
            url : "/dataUser",
            data: { username: email },
            success : function(msg) {
            	$('#email-modification', '.bootbox').val(msg.username);
				$('#nick-modification', '.bootbox').val(msg.nick);
				$('#password-modification', '.bootbox').val(msg.password);
            }
	  	});
	});
}