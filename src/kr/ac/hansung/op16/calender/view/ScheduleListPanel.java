package kr.ac.hansung.op16.calender.view;

import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Panel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import kr.ac.hansung.op16.calender.model.ScheduleData;


public class ScheduleListPanel extends Panel {
	MainFrame mainFrame;
	Panel topPanel;
	Panel bottomPanel;
	
	List scheduleList = new List();

	/**
	 * Create the panel.
	 */
	public ScheduleListPanel(Map<Integer, java.util.List<ScheduleData>> calenderMappingScheduleList, MainFrame mainframe, int year, int month, int day) {
		this.mainFrame = mainframe;
		java.util.List<ScheduleData> dayScheduleList = calenderMappingScheduleList.get(day);
		int scheduleListSize = dayScheduleList == null ? 0: dayScheduleList.size();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		for(int i=0; i<scheduleListSize; i++){
			ScheduleData eachSchedule = dayScheduleList.get(i);
			String viewString = "";
			viewString += timeFormat.format(eachSchedule.getStartDate().getTime()) + " ~ " + timeFormat.format(eachSchedule.getEndDate().getTime());
			viewString += " " + eachSchedule.getTitle();
			
			scheduleList.add(viewString, i);
		}
		
		setLayout(new BorderLayout());
		add(scheduleList, BorderLayout.CENTER);
	}

}
