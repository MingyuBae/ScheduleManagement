package kr.ac.hansung.op16.calender.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import kr.ac.hansung.op16.calender.logic.ScheduleService;

public class AddSchedulePanel extends JPanel {
	Label selectedDateLable;
	Label titleLable = new Label("�젣紐�");
	TextField titleField = new TextField("title");
	
	Label dateLabel = new Label("湲곌컙");
	
	Choice startHourChoice = new Choice();
	Choice startMinuteChoice = new Choice();
	Label timeLabel = new Label("~");
	Choice endHourChoice = new Choice();
	Choice endMinuteChoice = new Choice();
	
	Label contentLable = new Label("�긽�꽭�궡�슜");
	TextArea contentArea = new TextArea();
	
	Button submitBtn = new Button("異붽�");
	Button cancelBtn = new Button("痍⑥냼");
	
	public AddSchedulePanel(int year, int month, int day, JFrame thisFrame, JFrame mainFrame) {
		ScheduleService scheduleService = ScheduleService.getInstence();
		
		selectedDateLable = new Label("" + year + "�뀈 " + (month+1) + "�썡 " + day + "�씪");
		
		/* �씠踰ㅽ듃 �벑濡� */
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �엯�젰媛� �솗�씤 �븘�슂
				String title = titleField.getText();
				int startHour = Integer.parseInt(startHourChoice.getSelectedItem());
				int startMinute = Integer.parseInt(startMinuteChoice.getSelectedItem());
				int endHour = Integer.parseInt(endHourChoice.getSelectedItem());
				int endMinute = Integer.parseInt(endMinuteChoice.getSelectedItem());
				String content = contentArea.getText();
				
				scheduleService.addSchedule(year, month, day, startHour, startMinute, endHour, endMinute, title, content);
				
				thisFrame.setVisible(false);
				thisFrame.dispose();
				
				mainFrame.revalidate();
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisFrame.setVisible(false);
				thisFrame.dispose();
			}
		});
		
		for(int i=0; i<24; i++){
			startHourChoice.add("" + i);
			endHourChoice.add("" + i);
		}
		
		for(int i=0; i<=60; i+=5){
			startMinuteChoice.add("" + i);
			endMinuteChoice.add("" + i);
		}
		
		add(selectedDateLable);
		add(titleLable);
		add(titleField);
		
		add(dateLabel);
		add(startHourChoice);
		add(startMinuteChoice);
		add(timeLabel);
		add(endHourChoice);
		add(endMinuteChoice);
		
		add(contentArea);
		
		add(submitBtn);
		add(cancelBtn);
	}

}
