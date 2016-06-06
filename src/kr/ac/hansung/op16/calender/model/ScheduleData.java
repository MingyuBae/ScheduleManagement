package kr.ac.hansung.op16.calender.model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ScheduleData implements Serializable{
	String id;
	Calendar startDate;
	Calendar endDate;
	String title;
	String content;
	Date alertDate;
	boolean externalSchedule;
	boolean dateOnly;
	
	public ScheduleData(String id, Calendar startDate, Calendar endDate, String title, String content, Date alertDate, boolean externalSchedule, boolean dateOnly){
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.title = title;
		this.content = content;
		this.alertDate = alertDate;
		this.externalSchedule = externalSchedule;
		this.dateOnly = dateOnly;
	}
	
	public ScheduleData(int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute, int alertTimeSec, String title, String content){
		this(year, month, day, startHour, startMinute, year, month, day, endHour, endMinute, alertTimeSec, title, content);
	}
	
	public ScheduleData(int startYear, int startMonth, int startDay, int startHour, int startMinute,
						int endYear, int endMonth, int endDay, int endHour, int endMinute, int alertTimeSec, String title, String content){
		this.startDate = Calendar.getInstance();
		this.startDate.set(startYear, startMonth, startDay, startHour, startMinute, 00);
		
		this.endDate = Calendar.getInstance();
		this.endDate.set(endYear, endMonth, endDay, endHour, endMinute, 00);
		
		this.title = title;
		this.content = content;
		
		externalSchedule = false;
		
		if(alertTimeSec >= 0){
			this.alertDate = new Date(startDate.getTimeInMillis() - (alertTimeSec * 1000));
		}
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
	public Date getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isExternalSchedule() {
		return externalSchedule;
	}
	public void setExternalSchedule(boolean externalSchedule) {
		this.externalSchedule = externalSchedule;
	}
	public boolean isDateOnly() {
		return dateOnly;
	}
	public void setDateOnly(boolean dateOnly) {
		this.dateOnly = dateOnly;
	}
}
