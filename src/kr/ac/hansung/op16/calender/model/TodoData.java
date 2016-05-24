package kr.ac.hansung.op16.calender.model;
import java.io.Serializable;
import java.util.Calendar;

public class TodoData implements Serializable{
	Calendar Date;
	String title;
	String content;
	
	public TodoData(int year, int month, int day, String title, String content){
		this.Date = Calendar.getInstance();
		this.Date.set(Calendar.YEAR, year);
		this.Date.set(Calendar.MONTH, month - 1);
		this.Date.set(Calendar.DAY_OF_MONTH, day);
		
		this.title = title;
		this.content = content;
	}
	
	public Calendar getDate() {
		return Date;
	}
	public void setDate(Calendar date) {
		Date = date;
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
	
	@Override
	public String toString() {
		return "TodoData [Date=" + Date.get(Calendar.YEAR) + "년 " + Date.get(Calendar.MONTH) + "월 " + Date.get(Calendar.DAY_OF_MONTH) + "일, "
				+ "title=" + title + ", content=" + content + "]";
	}
}
