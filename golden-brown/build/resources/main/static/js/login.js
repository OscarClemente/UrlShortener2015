$(document).ready(
	function() {
		$("#login").on("click", function(event) {
			var modal = bootbox.dialog({
				message: $("#form-content-log-in").html(),
				title: "Log in",
				show: false,
		        onEscape: function() {
		        	modal.modal("hide");
		        }
			});
			modal.modal("show");
		});
	});