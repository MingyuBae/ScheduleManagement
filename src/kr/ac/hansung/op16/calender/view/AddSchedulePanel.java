package kr.ac.hansung.op16.calender.view;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import kr.ac.hansung.op16.calender.logic.ScheduleService;

public class AddSchedulePanel extends JPanel {
	Label selectedDateLable;
	Label titleLable = new Label("제목");
	TextField titleField = new TextField("title");
	
	Label dateLabel = new Label("기간");
	
	Choice startHourChoice = new Choice();
	Choice startMinuteChoice = new Choice();
	Choice endHourChoice = new Choice();
	Choice endMinuteChoice = new Choice();
	
	Label contentLable = new Label("상세내용");
	TextArea contentArea = new TextArea();
	
	Button submitBtn = new Button("추가");
	Button cancelBtn = new Button("취소");
	
//	Label startTimeHouls
	/**
	 * Create the panel.
	 */
	public AddSchedulePanel(int year, int month, int day, ScheduleService scheduleService, JFrame superFrame) {
		selectedDateLable = new Label("" + year + "년 " + month + "월 " + day + "일");
		for(int i=0; i<24; i++){
			startHourChoice.add("" + i);
			endHourChoice.add("" + i);
		}
		
		for(int i=0; i<=60; i+=5){
			startMinuteChoice.add("" + i);
			endMinuteChoice.add("" + i);
		}
		
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
				
				superFrame.setVisible(false);
				superFrame.dispose();
			}
		});
		
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				superFrame.setVisible(false);
				superFrame.dispose();				
			}
		});
		
		add(selectedDateLable);
		add(titleLable);
		add(titleField);
		
		add(dateLabel);
		add(startHourChoice);
		add(startMinuteChoice);
		add(endHourChoice);
		add(endMinuteChoice);
		
		add(contentArea);
		
		add(submitBtn);
		add(cancelBtn);
	}

}
