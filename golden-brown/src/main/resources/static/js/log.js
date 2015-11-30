$(document).ready(
	function() {
		$("#log_out").on("click", function(event) {
			sessionStorage.removeItem('usuario');
			document.getElementById("log_in").style.visibility="block";
			document.getElementById("register").style.visibility="block";
			document.getElementById("user").style.visibility="none";
	    	document.getElementById("log_out").style.visibility="none";
		});
	});