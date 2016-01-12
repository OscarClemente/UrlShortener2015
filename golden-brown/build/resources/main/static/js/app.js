$(document).ready(
    function() {
        $("#shortener").submit(
            function(event) {
                event.preventDefault();
                getDataUser();
                var email_encrypted = sessionStorage.getItem('email');
                var email = $.parseJSON(email_encrypted);
                $.ajax({
                    type : "POST",
                    url : "/link",
                    data : $(this).serialize() + "&username=" + email,
                    success : function(msg) {
						$("#sugerencia").html("");
						if (msg.sponsor != null) {
							$("#result").html(
								"<div class='alert alert-success lead'><a target='_blank' href='"
								+ msg.uri
								+ "'>"
								+ msg.uri
								+ "</a><br>Esta página tiene publicidad, se redirigirá a "
								+ msg.sponsor
								+ " Durante 10 segundos</div>");
								$("#result").click(
									function(event) {
										event.preventDefault();
										var openedWindow;
										setTimeout(function(){
											openedWindow.location = msg.target;
										}, 5000);
										openedWindow = window.open(msg.sponsor);
									}
								);
						}
						else {
							$("#result").html(
								"<div class='alert alert-success lead'><a target='_blank' href='"
								+ msg.uri
								+ "'>"
								+ msg.uri
								+ "</a><br>Esta página no tiene publicidad </div>");
						}
                    },
					error: function(xhr, status, error) {
						if (xhr.status != 400) {
							if (xhr.responseText.contains("separa")) {
								var sep = xhr.responseText.split("separa");
								$("#sugerencia").html(sep[0]+"</br>"+sep[1]+"</br></br>");
							}
							else {
								$("#result").html(
									"<div class='alert alert-danger lead'>ERROR NAME IS ALREADY IN USE</div>");
							}
						}
						else {
							$("#result").html(
								"<div class='alert alert-danger lead'>ERROR BAD URL</div>");
						}
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
            });
	});