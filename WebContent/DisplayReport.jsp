<%@ page import = "com.waveproject.upload.CreateReport" %>
<%@ page import = "com.waveproject.upload.ReportDataView" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.util.ArrayList" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Final Report</title>
</head>
<body>
<%
CreateReport cr = new CreateReport();
List<ReportDataView> fulldata = cr.createReportData();
if(fulldata.isEmpty()) {
	%><div align="center"><H1>No Data To Display</H1> <%
}
else {
	%><div align="center"><H1>Salary Report</H1>
	<BR />
	<BR />
	<BR />
	<table border="2" align="center">
	<TR align="center"><TD><B>Employee ID</B></TD><TD><B>Pay Period</B></TD><TD><B>Amount Paid</B></TD></TR>
	<%
	for(ReportDataView rdv:fulldata) {
	%>
	<TR align="center">
		<TD><%=rdv.getEmployeeId()%></TD>
		<TD><%=rdv.getStartData()%> - <%=rdv.getEndData()%></TD>
		<TD><%=rdv.getTotalSalary()%></TD>
	</TR>	
	<% }%>
	</table>
	</div>
	<%
}

%>

</body>
</html>