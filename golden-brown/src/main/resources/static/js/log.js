$(document).ready(
	function() {
		$("#log_out").on("click", function(event) {
			sessionStorage.removeItem('usuario');
			document.getElementById("log_in").style.display="inline";
			document.getElementById("register").style.display="inline";
			document.getElementById("user").style.display="none";
	    	document.getElementById("log_out").style.display="none";
		});
	});