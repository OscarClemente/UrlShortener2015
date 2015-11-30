$(document).ready(
    function() {
        $("#shortener").submit(
            function(event) {
                event.preventDefault();
                var usuario = sessionStorage.getItem('usuario');
                var view = $.parseJSON(usuario);
                if (view != null) {
	                if (view.rolAdmin == "no") {
		                $.ajax({
		                    type : "POST",
		                    url : "/link",
		                    data : $(this).serialize(),
		                    success : function(msg) {
		                        $("#result").html(
		                            "<div class='alert alert-success lead'><a target='_blank' href='"
		                            + msg.uri
		                            + "'>"
		                            + msg.uri
		                            + "</a></div>");
		                    },
		                    error : function() {
		                        $("#result").html(
		                                "<div class='alert alert-danger lead'>ERROR</div>");
		                    }
		                });
	                }
                }
                else {
                	$("#result").html(
                    "<div class='alert alert-danger lead'>REGISTER TO USE THIS SERVICE</div>");
                }
            });
    });