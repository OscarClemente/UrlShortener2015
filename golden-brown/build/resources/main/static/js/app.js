$(document).ready(
    function() {
        $("#shortener").submit(
            function(event) {
                event.preventDefault();
                var email_encrypted = sessionStorage.getItem('email');
                var email = $.parseJSON(email_encrypted);
                $.ajax({
                    type : "POST",
                    url : "/link",
                    data : $(this).serialize() + "&username=" + email,
                    success : function(msg) {
						$("#sugerencia").html("");
<<<<<<< HEAD
						if (msg.sponsor != null) {
=======
						if (msg.sponsor!=null) {
>>>>>>> upstream/master
							$("#result").html(
								"<div class='alert alert-success lead'><a target='_blank' href='"
								+ msg.uri
								+ "'>"
								+ msg.uri
<<<<<<< HEAD
								+ "</a><br>Esta página tiene publicidad, se redirigirá a "
=======
								+ "</a><br>Esta página tiene publicidad, se redigirá a "
>>>>>>> upstream/master
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
<<<<<<< HEAD
						if (xhr.status != 400) {
							if (xhr.responseText.contains("separa")) {
								var sep = xhr.responseText.split("separa");
								$("#sugerencia").html("</br></br>"+sep[0]+"</br>"+sep[1]);
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
=======
						//if (error=="Not Acceptable") {
							var sep = xhr.responseText.split("separa");
							$("#sugerencia").html("</br></br>"+sep[0]+"</br>"+sep[1]+"</br>"+error);
							$("#result").html(
								"<div class='alert alert-danger lead'>ERROR YA EXISTE ESE NOMBRE</div>");
						//}
						/*else {
							$("#result").html(
								"<div class='alert alert-danger lead'>ERROR BAD URL</div>");
						}*/
>>>>>>> upstream/master
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