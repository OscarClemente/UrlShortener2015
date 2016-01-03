function getDataUser() {
	$.ajax({
		type : "GET",
		url: '/dataSocial',
		success: function(msg) {
			sessionStorage.setItem('email', JSON.stringify(msg.username));
			sessionStorage.setItem('password', JSON.stringify(msg.password));
		},
		async: false
	});
}