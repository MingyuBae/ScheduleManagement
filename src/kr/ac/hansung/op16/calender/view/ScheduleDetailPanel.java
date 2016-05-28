package kr.ac.hansung.op16.calender.view;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import kr.ac.hansung.op16.calender.logic.ScheduleService;
import kr.ac.hansung.op16.calender.model.ScheduleData;

public class ScheduleDetailPanel extends JPanel {
	ScheduleService scheduleService;
	ScheduleData scheduleData;
	JFrame superFrame;
	
	Label titleLable;
	Label startDateLabel;
	Label endDateLabel;
	Label contentLabel;
	
	Button closeBtn;
	Button deleteScheduleBtn;
	
	public ScheduleDetailPanel(ScheduleService scheduleService, ScheduleData scheduleData, JFrame superFrame) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("YY-MM-dd HH:mm");
		
		this.scheduleService = scheduleService;
		this.scheduleData = scheduleData;
		this.superFrame = superFrame;
		
		titleLable = new Label(scheduleData.getTitle());
		startDateLabel = new Label(timeFormat.format(scheduleData.getStartDate().getTime()));
		endDateLabel = new Label(timeFormat.format(scheduleData.getEndDate().getTime()));
		
		contentLabel = new Label(scheduleData.getContent());
				
		closeBtn = new Button("닫기");
		deleteScheduleBtn = new Button("일정 삭제");
		
		closeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				superFrame.setVisible(false);
				superFrame.dispose();
			}
		});
		deleteScheduleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scheduleService.deleteSchedule(scheduleData);
				
				superFrame.setVisible(false);
				superFrame.dispose();
			}
		});
		
		add(titleLable);
		add(startDateLabel);
		add(endDateLabel);
		add(contentLabel);
		add(closeBtn);
		add(deleteScheduleBtn);
	}

}
