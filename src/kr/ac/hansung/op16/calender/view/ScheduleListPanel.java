package kr.ac.hansung.op16.calender.view;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

import javax.swing.*;

import kr.ac.hansung.op16.calender.logic.ScheduleService;
import kr.ac.hansung.op16.calender.model.ScheduleData;


public class ScheduleListPanel extends Panel {
	ScheduleService scheduleService;
	java.util.List<ScheduleData> dayScheduleList;
	
	JFrame mainFrame;
	Panel topPanel;
	Panel bottomPanel;
	
	Label selectDateLabel;
	List scheduleList = new List();
	Button addScheduleBtn = new Button("일정추가");

	public ScheduleListPanel(int year, int month, int day, JFrame mainFrame) {
		scheduleService = ScheduleService.getInstence();
		this.mainFrame = mainFrame;
		dayScheduleList = scheduleService.getDayScheduleList(year, month, day);
		int scheduleListSize = dayScheduleList == null ? 0: dayScheduleList.size();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		for(int i=0; i<scheduleListSize; i++){
			ScheduleData eachSchedule = dayScheduleList.get(i);
			String viewString = "";
			if(! eachSchedule.isDateOnly()){
				viewString += timeFormat.format(eachSchedule.getStartDate().getTime()) + " ~ " + timeFormat.format(eachSchedule.getEndDate().getTime()) + " ";
			}
			viewString += eachSchedule.getTitle();
			
			scheduleList.add(viewString, i);
		}
		
		selectDateLabel = new Label(year + "년 " + (month+1) + "월 " + day + "일");
		
		/* 이벤트 등록 */
		scheduleList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame detailScheduleFormFrame = new JFrame();
				detailScheduleFormFrame.add(new ScheduleDetailPanel(dayScheduleList.get(scheduleList.getSelectedIndex()), detailScheduleFormFrame, mainFrame));
				detailScheduleFormFrame.pack();
				detailScheduleFormFrame.setVisible(true);
				detailScheduleFormFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		addScheduleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame addScheduleFormFrame = new JFrame();
				addScheduleFormFrame.add(new AddSchedulePanel(year, month, day, addScheduleFormFrame, mainFrame));
				addScheduleFormFrame.pack();
				addScheduleFormFrame.setVisible(true);
				addScheduleFormFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		
		setLayout(new BorderLayout());
		
		add(selectDateLabel, BorderLayout.NORTH);
		add(scheduleList, BorderLayout.CENTER);
		add(addScheduleBtn, BorderLayout.SOUTH);
	}

}
