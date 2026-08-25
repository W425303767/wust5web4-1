/**
 * Shared setup for the server side DataTables used by the data pages.
 */
function initServerTable(selector, url, columns) {
	var tableColumns = [];
	for (var i = 0; i < columns.length; i++) {
		tableColumns.push({ "data": columns[i] });
	}

	return $(selector).DataTable({
		"processing": true,
		"serverSide": true,
		"ajax": url,
		"columns": tableColumns
	});
}
