<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>File validation</title>
	<link rel="icon" href="/resources/icons/tabIcon.png">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css" />" media="screen">
</head>
<body>
	<center>
		<p id="result-title">Validation results: </p>  
		<table id="results" summary="Validation results">  
			<thead>  
				<tr>  
					<th width="10%">type</th><th width="30%">value</th><th width="10%">validation result</th>  
				</tr>  
			</thead>  
			<c:forEach var="entry" items="${file.entries}">
			<tbody>  
				<tr>     
    				<td>${entry.type}</td>  
    				<td>${entry.value}</td>  
    				<td>${entry.validationResult}</td>  
				</tr>  
			</tbody> 
			</c:forEach> 
		</table>  
		<p id="validation-info">${storageServiceResponse}</p>
		<p><a id="return-link" href="${pageContext.request.contextPath}/">Upload file page</a></p>
	</center>
</body>
</html>