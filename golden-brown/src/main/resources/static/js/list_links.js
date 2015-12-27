function listLinks() {
	var email_encrypted = sessionStorage.getItem('email');
    var email = $.parseJSON(email_encrypted);
    $.ajax({
        type : "POST",
        url : "/listLinks",
        data : { username: email },
        success : function(msg) {
            var oTable = $('#table-links').dataTable();
            for(var i = 0; i < msg.length; i++) {
            	oTable.fnAddData([ msg[i].hash, msg[i].target, msg[i].sponsor, msg[i].mode, msg[i].safe,
            	                   msg[i].ip, msg[i].country ]);
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
}