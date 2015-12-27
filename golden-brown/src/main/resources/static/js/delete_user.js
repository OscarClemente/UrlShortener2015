$(document).ready(function() {
	var oTable = $('#table-users').DataTable();
	var oTable2 = $('#table-links').DataTable();
	
    $('#table-users tbody').on('click', 'tr', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            sessionStorage.removeItem('username');
        }
        else {
        	oTable.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
            var row = oTable.row('.selected').data();
            sessionStorage.setItem('username', JSON.stringify(row[0]));
            listLinks(row[0]);
        }
    });
 
    $('#delete-user').click(function() {
    	var username_encrypted = sessionStorage.getItem('username');
        var username = $.parseJSON(username_encrypted)
        $.ajax({
            type : "POST",
            url : "/deleteLinksUser",
            data : "username=" + username,
            success : function(msg) {
            	listLinks(username);
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
    
    $('#table-links tbody').on('click', 'tr', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        }
        else {
        	oTable2.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });
 
    $('#delete-link').click(function() {
        var username_encrypted = sessionStorage.getItem('username');
        var username = $.parseJSON(username_encrypted);
    	var row2 = oTable2.row('.selected').data();
        $.ajax({
            type : "POST",
            url : "/deleteRow",
            data : "username=" + username + "&hash=" + row2[0] + "&target=" + row2[1],
            success : function(msg) {
            	oTable2.row('.selected').remove().draw(false);
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