$(document).ready(
	function() {
		$("#log_out").on("click", function(event) {
			sessionStorage.clear();
		});
	});