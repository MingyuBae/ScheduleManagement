package kr.ac.hansung.op16.calender.view;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import kr.ac.hansung.op16.calender.logic.ScheduleService;
import kr.ac.hansung.op16.calender.model.ScheduleData;

public class ScheduleDetailPanel extends JPanel {
	ScheduleService scheduleService;
	ScheduleData scheduleData;
	JFrame thisFrame;
	
	Label titleLable;
	Label startDateLabel;
	Label timeLabel;
	Label endDateLabel;
	Label contentLabel;
	
	Button closeBtn;
	Button deleteScheduleBtn;
	
	public ScheduleDetailPanel(ScheduleData scheduleData, JFrame thisFrame, JFrame mainFrame) {
		SimpleDateFormat starttimeFormat = new SimpleDateFormat("YY-MM-dd HH:mm");
		SimpleDateFormat endtimeFormat = new SimpleDateFormat("HH:mm");
		
		this.scheduleService = ScheduleService.getInstence();
		this.scheduleData = scheduleData;
		this.thisFrame = thisFrame;
		
		titleLable = new Label(scheduleData.getTitle());
		startDateLabel = new Label(starttimeFormat.format(scheduleData.getStartDate().getTime()));
		timeLabel = new Label();
		endDateLabel = new Label(endtimeFormat.format(scheduleData.getEndDate().getTime()));
		
		contentLabel = new Label(scheduleData.getContent());
				
		closeBtn = new Button("닫기");
		deleteScheduleBtn = new Button("일정 삭제");
		
		/* 이벤트등록 */
		closeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisFrame.setVisible(false);
				thisFrame.dispose();
			}
		});
		deleteScheduleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scheduleService.deleteSchedule(scheduleData);
				
				thisFrame.setVisible(false);
				thisFrame.dispose();
				
				mainFrame.revalidate();
			}
		});
		
		add(titleLable);
		add(startDateLabel);
		add(timeLabel);
		add(endDateLabel);
		add(contentLabel);
		add(closeBtn);
		add(deleteScheduleBtn);
	}

}
