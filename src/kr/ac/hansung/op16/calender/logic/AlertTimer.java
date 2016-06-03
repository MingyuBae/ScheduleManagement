package kr.ac.hansung.op16.calender.logic;

import java.awt.Frame;
import java.util.TimerTask;

import javax.swing.JFrame;

import kr.ac.hansung.op16.calender.model.ScheduleData;
import kr.ac.hansung.op16.calender.view.ScheduleDetailPanel;

public class AlertTimer extends TimerTask {
	ScheduleData alertScheduleData;
	
	public AlertTimer(ScheduleData scheduleData) {
		super();
		this.alertScheduleData = scheduleData;
	}
	
	@Override
	public void run() {
		JFrame alertWindowFrame = new JFrame();
		ScheduleDetailPanel scheduleDetailPanel = new ScheduleDetailPanel(alertScheduleData, alertWindowFrame, null);
		
		alertWindowFrame.add(scheduleDetailPanel);
		alertWindowFrame.pack();
		alertWindowFrame.setVisible(true);
		alertWindowFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

}
