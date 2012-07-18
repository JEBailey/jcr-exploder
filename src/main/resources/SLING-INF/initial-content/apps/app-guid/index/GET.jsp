<%@ page session="false" %>
<%@ page import="javax.jcr.*,
        org.apache.sling.api.resource.Resource" %>
<%@ taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Application ID</title>
<link href="/etc/designs/app-guid/css/common.css" rel="stylesheet" type="text/css">
<link href="/etc/designs/app-guid/themes/blue/style.css" rel="stylesheet" type="text/css">
</head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<body>
	<form class="cmxform" id="appNameSubmission" method="post" action="">
		<fieldset>
			<legend>Request GUID</legend>
			<p>
				<label for="cname">Application Name</label> <em>*</em><input
					id="cname" name="name" size="40" class="required" minlength="6" />
			</p>
			<p>
			<div class="comment">minimum 6 characters, will be converted to
				lower case</div>
			</p>
			<p>
				<input class="submit" type="submit" value="Create GUID" />
			</p>
		</fieldset>
	</form>

	<table id="dataView" class="tablesorter">
		<thead>
			<tr>
				<th>Application Name</th>
				<th>Unique ID</th>
			</tr>
		</thead>
		<tbody id="dataBody">
		</tbody>
	</table>

</body>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript"
	src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
<script type="text/javascript" src="/etc/designs/app-guid/scripts/jquery.simpletablefilter.js"></script>
<script src="/system/sling.js"></script>
<script type="text/javascript">
$(document)
		.ready(
				function() {

					function tableFilter() {
						var data = $("#dataView");
						var field = $("#cname");
						$.simpleTableFilter(data, field.val().toLowerCase());

					}

					$("#cname").keyup(tableFilter);

					function populateTable() {
						 var data = Sling.getContent("/srv/app/guid", 1);
						 var dataBody = $('#dataBody');
						 $('tr:gt(0)').remove();
						 
						for ( var i in data) {
							dataBody.append('<tr><td width="100%">'
									+ data[i]["title"]
									+ '</td><td class="keys"><pre>'
									+ data[i]["uuid"] + '</pre></td>');
						};
						tableFilter();
					}

					//populateTable();
					populateTable();

					$("#appNameSubmission").validate(
							{
								submitHandler : function(form) {
									var appName = "./index.guid?create="
											+ $("#cname").val();

									$.ajax({
										url : appName,
										dataType : 'text',
										type : 'post',
										contentType : "text/plain",
										data : "proxy",
										success : function() {
											populateTable();
										},
										error : function(xhr, status, err) {
											var d = err;
										}
									});
								}
							});
				});



</script>
</html>
