package calender.logic;

import java.util.TimerTask;

import javax.swing.JFrame;

import calender.model.ScheduleData;
import calender.view.ScheduleDetailPanel;

public class AlertTimer extends TimerTask {
	ScheduleData alertScheduleData;
	
	public AlertTimer(ScheduleData scheduleData) {
		super();
		this.alertScheduleData = scheduleData;
	}
	
	@Override
	public void run() {
		JFrame alertWindowFrame = new JFrame();
		ScheduleDetailPanel scheduleDetailPanel = new ScheduleDetailPanel(alertScheduleData, alertWindowFrame, null, true);
		
		alertWindowFrame.add(scheduleDetailPanel);
		alertWindowFrame.pack();
		alertWindowFrame.setVisible(true);
		alertWindowFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		alertWindowFrame.setTitle("알람");
	}

}
