function ajaxDelete(url, description) {
	if (!confirm('Are you sure you want to delete "' + description + '"?')) {
		return;
	}
	$.ajax({
		url : url,
		type : 'DELETE',
		success : function(data, textStatus) {
			window.location.reload();
		}
	});
}