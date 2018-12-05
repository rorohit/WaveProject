package com.waveproject.upload;

public class ReportDataView {
	private String startData;
	private String endData;
	private String hoursWorked;
	private String employeeId;
	private String jobGroup;
	private String totalSalary;
	public String getTotalSalary() {
		return totalSalary;
	}
	public void setTotalSalary(String totalSalary) {
		this.totalSalary = totalSalary;
	}
	public String getStartData() {
		return startData;
	}
	public void setStartData(String startData) {
		this.startData = startData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	public String getHoursWorked() {
		return hoursWorked;
	}
	public void setHoursWorked(String hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
}
