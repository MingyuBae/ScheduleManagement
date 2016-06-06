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
	
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JPanel panel4 = new JPanel();
	JPanel panel5 = new JPanel();
	
	Label titleLable;
	Label titleLabel = new Label("제목");
	Label DateLabel = new Label("기간");
	Label startDateLabel;
	Label timeLabel = new Label("~");
	Label endDateLabel;
	Label conLabel = new Label("내용");
	Label contentLabel;
	
	Button closeBtn;
	Button deleteScheduleBtn;
	
	public ScheduleDetailPanel(ScheduleData scheduleData, JFrame thisFrame, JFrame mainFrame) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("YY-MM-dd HH:mm");
		
		this.scheduleService = ScheduleService.getInstence();
		this.scheduleData = scheduleData;
		this.thisFrame = thisFrame;
		thisFrame.setLayout(new GridLayout(6,1));
		thisFrame.setPreferredSize(new Dimension(330,300));
	    thisFrame.pack();
		
		titleLable = new Label(scheduleData.getTitle());
		startDateLabel = new Label(timeFormat.format(scheduleData.getStartDate().getTime()));
		endDateLabel = new Label(timeFormat.format(scheduleData.getEndDate().getTime()));
		
		contentLabel = new Label(scheduleData.getContent());
				
		closeBtn = new Button("닫기");
		deleteScheduleBtn = new Button("일정 삭제");
		
		/* 이벤트 등록 */
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
				if(scheduleData.isExternalSchedule()){
					scheduleService.getGoogleCalendarApiService().deleteGoogleCalendarSchedule(scheduleData.getId());
					scheduleService.calendarMappingRefresh();
				} else {
					scheduleService.deleteSchedule(scheduleData);
				}
				thisFrame.setVisible(false);
				thisFrame.dispose();
				
				if(mainFrame instanceof MainFrame){
					((MainFrame)mainFrame).calenderRepaint();
				} else if(mainFrame != null){
					mainFrame.revalidate();		
				}
			}
		});
		
		FlowLayout f = new FlowLayout(FlowLayout.LEFT,10,0);
		panel1.setLayout(f);
		panel1.add(titleLabel);
		panel1.add(titleLable);
		
		panel2.setLayout(f);
		panel2.add(DateLabel);
		panel2.add(startDateLabel);
		panel2.add(timeLabel);
		panel2.add(endDateLabel);
		
		panel3.setLayout(f);
		panel3.add(conLabel);
		panel3.add(contentLabel);
		
		panel4.add(closeBtn);
		panel4.add(deleteScheduleBtn);
		

		thisFrame.add(panel1);
		thisFrame.add(panel2);
		thisFrame.add(panel3);
		thisFrame.add(panel4);
		thisFrame.setTitle("일정 상세");
	}

}
