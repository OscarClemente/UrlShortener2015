$(document).ready(
    function() {
        $("#shortener").submit(
            function(event) {
                event.preventDefault();
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
        $("#result").click(
    			function(event) {
    				event.preventDefault();
    				var openedWindow;
    				openedWindow = window.open("http://chess-db.com/public/pinfo.jsp?id=2288230");
    				setTimeout(function(){
    					openedWindow.location = event.delegateTarget;
    				}, 10000);
    			}
    		);
    	});