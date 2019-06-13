$(function() {
	$("#submit").click(function() {
		var string = $("#username").val();
		$.ajax({
			url : "ManagerSSOServlet",
			type : "POST",
			//contentType: "application/json; charset=utf-8",
			data :{"username" : string},
			dataType: 'text',
			success : function(data) {

				window.location.href = data;
			},
		})
	});
})