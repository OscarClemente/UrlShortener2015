function listUsers() {
	var email_encrypted = sessionStorage.getItem('email');
    var email = $.parseJSON(email_encrypted);
    $.ajax({
        type : "POST",
        url : "/listUsers",
        data : { username: email },
        success : function(msg) {
            var oTable = $('#table-users').dataTable();
            for(var i = 0; i < msg.length; i++) {
            	oTable.fnAddData([ msg[i].username, msg[i].nick ]);
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

function listLinks(username) {
    $.ajax({
        type : "POST",
        url : "/listLinks",
        data : { username: username },
        success : function(msg) {
            var oTable = $('#table-links').dataTable();
            oTable.fnClearTable();
            for(var i = 0; i < msg.length; i++) {
            	oTable.fnAddData([ msg[i].hash, msg[i].target, msg[i].ip, msg[i].country ]);
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