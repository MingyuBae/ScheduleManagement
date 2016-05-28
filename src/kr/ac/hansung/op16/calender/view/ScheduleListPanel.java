package kr.ac.hansung.op16.calender.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import kr.ac.hansung.op16.calender.logic.ScheduleService;
import kr.ac.hansung.op16.calender.model.ScheduleData;


public class ScheduleListPanel extends Panel {
	java.util.List<ScheduleData> dayScheduleList;
	
	MainFrame mainFrame;
	Panel topPanel;
	Panel bottomPanel;
	
	Label selectDateLabel;
	List scheduleList = new List();
	Button addScheduleBtn = new Button("일정추가");

	/**
	 * Create the panel.
	 */
	public ScheduleListPanel(Map<Integer, java.util.List<ScheduleData>> calenderMappingScheduleList, 
							ScheduleService scheduleService, MainFrame mainframe, int year, int month, int day) {
		this.mainFrame = mainframe;
		dayScheduleList = calenderMappingScheduleList.get(day);
		int scheduleListSize = dayScheduleList == null ? 0: dayScheduleList.size();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		for(int i=0; i<scheduleListSize; i++){
			ScheduleData eachSchedule = dayScheduleList.get(i);
			String viewString = "";
			viewString += timeFormat.format(eachSchedule.getStartDate().getTime()) + " ~ " + timeFormat.format(eachSchedule.getEndDate().getTime());
			viewString += " " + eachSchedule.getTitle();
			
			scheduleList.add(viewString, i);
		}
		
		selectDateLabel = new Label(year + "년 " + (month+1) + "월 " + day + "일");
		
		scheduleList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame detailScheduleFormFrame = new JFrame();
				detailScheduleFormFrame.add(new ScheduleDetailPanel(scheduleService, dayScheduleList.get(scheduleList.getSelectedIndex()), detailScheduleFormFrame));
				detailScheduleFormFrame.pack();
				detailScheduleFormFrame.setVisible(true);
				detailScheduleFormFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		
		addScheduleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame addScheduleFormFrame = new JFrame();
				addScheduleFormFrame.add(new AddSchedulePanel(year, month, day, scheduleService, addScheduleFormFrame));
				addScheduleFormFrame.pack();
				addScheduleFormFrame.setVisible(true);
				addScheduleFormFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		
		setLayout(new BorderLayout());
		
		add(selectDateLabel, BorderLayout.NORTH);
		add(scheduleList, BorderLayout.CENTER);
		add(addScheduleBtn, BorderLayout.SOUTH);
		
		
	}

}
