$(document).ready(function() {
	var oTable = $('#table-links').DataTable();
    $('#table-links tbody').on('click', 'tr', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        }
        else {
        	oTable.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });
 
    $('#delete-row').click(function() {
        var email_encrypted = sessionStorage.getItem('email');
        var email = $.parseJSON(email_encrypted);
    	var row = oTable.row('.selected').data();
        $.ajax({
            type : "POST",
            url : "/deleteRow",
            data : "username=" + email + "&hash=" + row[0] + "&target=" + row[1],
            success : function(msg) {
            	oTable.row('.selected').remove().draw(false);
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