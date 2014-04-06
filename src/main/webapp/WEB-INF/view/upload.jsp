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
        <h1 id="title">File Validator</h1>
        <form method="post" action="validation" enctype="multipart/form-data" accept-charset="utf-8">
            <table id="table-form">
                <tr>
                    <td>Upload file to validate:</td>
                    <td><input type="file" name="file"/></td>
                </tr>
                <tr> 
                	<td colspan="2">Store validation results to file:</td>
                </tr>
                <tr id="radio">
					<td><input type="radio" name="storeToDisk" value="true" checked="checked">YES</td>
					<td><input type="radio" name="storeToDisk" value="false"/>NO</td>
    			</tr> 
                <tr>
                    <td id="validate_button_row" colspan="2" align="center"><input id="validate_button" type="submit" value="VALIDATE"/></td>
                </tr>
            </table>
        </form>
        <c:if test="${fileInspectorResponse.status eq 'FAIL'}"><p id="error_msg">${fileInspectorResponse.message}</p></c:if>
    </center>
</body>
</html>