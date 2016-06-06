package kr.ac.hansung.op16.calender.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import kr.ac.hansung.op16.calender.logic.ScheduleService;

public class AddSchedulePanel extends JPanel {
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	/*
	 * 알람추가시 활성화
	JPanel panel4 = new JPanel();
	*/
	JPanel panel5 = new JPanel();
	JPanel panel6 = new JPanel();
	
	
	Label selectedDateLable;
	Label titleLable = new Label("제목");
	TextField titleField = new TextField("title");
	
	Label dateLabel = new Label("기간");
	
	Choice startHourChoice = new Choice();
	Choice startMinuteChoice = new Choice();
	Label timeLabel = new Label("~");
	Choice endHourChoice = new Choice();
	Choice endMinuteChoice = new Choice();
	
	Label contentLable = new Label("상세내용");
	TextArea contentArea = new TextArea(5,50);
	
	Button submitBtn = new Button("추가");
	Button cancelBtn = new Button("취소");
	
		
	public AddSchedulePanel(int year, int month, int day, JFrame thisFrame, JFrame mainFrame) {
		ScheduleService scheduleService = ScheduleService.getInstence();
		
		selectedDateLable = new Label("" + year + "년 " + (month+1) + "월 " + day + "일");
		
		thisFrame.setLayout(new GridLayout(6,1));
        thisFrame.setPreferredSize(new Dimension(500,650));
        thisFrame.pack();

		
		/* 이벤트 등록 */
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 입력값 확인 필요
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

		
		panel1.add(selectedDateLable);
		
		panel2.add(titleLable);
		panel2.add(titleField);
		
		panel3.add(dateLabel);
		panel3.add(startHourChoice);
		panel3.add(startMinuteChoice);
		panel3.add(timeLabel);
		panel3.add(endHourChoice);
		panel3.add(endMinuteChoice);
		
		panel5.setLayout(new FlowLayout());
		panel5.add(contentLable);
		panel5.add(contentArea);
		
		panel6.setLayout(new FlowLayout());
		panel6.add(submitBtn);
		panel6.add(cancelBtn);
		
		thisFrame.add(panel1);
		thisFrame.add(panel2);
		thisFrame.add(panel3);
		thisFrame.add(panel5);
		thisFrame.add(panel6);
		
		thisFrame.setTitle("일정 추가");
	}

}
