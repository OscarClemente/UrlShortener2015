function handleClick(box) {
    var el = document.getElementById("time");    
    if (box.checked) {
    	el.style.display = 'inline';
    }
    else {
    	el.style.display = 'none';
    }
}

function redirection() {
	var hash = getUrlParameter('hash');
    $.ajax({
        type : "GET",
        url : "/hash",
        data : "hash=" + hash,
        success : function(msg) {
    		setTimeout(function() {
    			window.location = msg.target;
    		}, msg.seconds*1000);
        },
		error: function(xhr, status, error) { }
    });
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}

function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}