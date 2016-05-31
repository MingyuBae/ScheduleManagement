package kr.ac.hansung.op16.calender.logic;

import java.util.TimerTask;

import kr.ac.hansung.op16.calender.model.ScheduleData;

public class AlertTimer extends TimerTask {
	ScheduleData alertScheduleData;
	
	public AlertTimer(ScheduleData scheduleData) {
		super();
		this.alertScheduleData = scheduleData;
	}
	
	@Override
	public void run() {
		System.out.println(alertScheduleData.getTitle() + "알람");
	}

}
