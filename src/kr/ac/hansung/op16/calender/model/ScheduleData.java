package kr.ac.hansung.op16.calender.model;
import java.io.Serializable;
import java.util.Calendar;

public class ScheduleData implements Serializable{
	Calendar startDate;
	Calendar endDate;
	String title;
	String content;
	int alertTimeSec;
	boolean alertCheck;
	
	public ScheduleData(int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute, int alertTimeSec, String title, String content){
		this(year, month, day, startHour, startMinute, year, month, day, endHour, endMinute, alertTimeSec, title, content);
	}
	
	public ScheduleData(int startYear, int startMonth, int startDay, int startHour, int startMinute,
						int endYear, int endMonth, int endDay, int endHour, int endMinute, int alertTimeSec, String title, String content){
		this.startDate = Calendar.getInstance();
		this.startDate.set(startYear, startMonth, startDay, startHour, startMinute);
		
		this.endDate = Calendar.getInstance();
		this.endDate.set(endYear, endMonth, endDay, endHour, endMinute);
		
		this.title = title;
		this.content = content;
		this.alertTimeSec = alertTimeSec;
		this.alertCheck = false;
	}

	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	public Calendar getEndDate() {
		return endDate;
	}
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getAlertTimeSec() {
		return alertTimeSec;
	}
	public void setAlertTimeSec(int alertTimeSec) {
		this.alertTimeSec = alertTimeSec;
	}
}
