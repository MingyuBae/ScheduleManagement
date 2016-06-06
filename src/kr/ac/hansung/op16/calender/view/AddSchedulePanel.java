package kr.ac.hansung.op16.calender.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import kr.ac.hansung.op16.calender.logic.ScheduleService;
import kr.ac.hansung.op16.calender.model.ScheduleData;

public class AddSchedulePanel extends JPanel {
	Label selectedDateLable;
	Label titleLable = new Label("제목");
	TextField titleField = new TextField();
	
	Label dateLabel = new Label("기간");
	
	Choice startHourChoice = new Choice();
	Choice startMinuteChoice = new Choice();
	Choice endHourChoice = new Choice();
	Choice endMinuteChoice = new Choice();
	
	Checkbox addGoogleCalender = new Checkbox("Google Calendar 등록");
	Checkbox alertEnableCheckbox = new Checkbox("알람");
	TextField alertTimeFied = new TextField("5");
	Choice alertUnitChoice = new Choice();
	
	Label contentLable = new Label("상세내용");
	TextArea contentArea = new TextArea();
	
	Button submitBtn = new Button("추가");
	Button cancelBtn = new Button("취소");
	
	public AddSchedulePanel(int year, int month, int day, JFrame thisFrame, JFrame mainFrame) {
		ScheduleService scheduleService = ScheduleService.getInstence();
		boolean googleApiEnable = scheduleService.getSettingData().isGoogleApiEnable();
		
		selectedDateLable = new Label("" + year + "년 " + month + "월 " + day + "일");
		
		
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
				int alertTime = -1;
				
				
				if(alertEnableCheckbox.getState()){
					String selectedUnit = alertUnitChoice.getSelectedItem();
					if("시간".equals(selectedUnit)){
						alertTime = Integer.parseInt(alertTimeFied.getText()) * 60 * 60;
					} else if("분".equals(selectedUnit)){
						alertTime = Integer.parseInt(alertTimeFied.getText()) * 60;
					} else {
						alertTime = Integer.parseInt(alertTimeFied.getText());
					}
				}
				
				if(addGoogleCalender.getState()){
					ScheduleData addScheduleData = new ScheduleData(year, month, day, startHour, startMinute, endHour, endMinute, alertTime, title, content);
					scheduleService.getGoogleCalendarApiService().addGoogleCalendarSchedule(addScheduleData);
					scheduleService.calendarMappingRefresh();
				} else {
					scheduleService.addSchedule(year, month, day, startHour, startMinute, endHour, endMinute, alertTime, title, content);
				}
				
				thisFrame.setVisible(false);
				thisFrame.dispose();
				
				scheduleService.scheduleAlertSet();
				((MainFrame)mainFrame).calenderRepaint();
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
		alertEnableCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					alertTimeFied.setEnabled(true);
					alertUnitChoice.setEnabled(true);
				} else {
					alertTimeFied.setEnabled(false);
					alertUnitChoice.setEnabled(false);
				}
				
			}
		});
		addGoogleCalender.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(addGoogleCalender.getState()){
					alertEnableCheckbox.setEnabled(false);
					alertTimeFied.setEnabled(false);
					alertUnitChoice.setEnabled(false);
				} else {
					alertEnableCheckbox.setEnabled(true);
					alertTimeFied.setEnabled(true);
					alertUnitChoice.setEnabled(true);
				}
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
		alertEnableCheckbox.setState(true);
		
		alertUnitChoice.add("분");
		alertUnitChoice.add("시간");
		alertUnitChoice.select(0);
		
		add(selectedDateLable);
		add(titleLable);
		add(titleField);
		
		add(dateLabel);
		add(startHourChoice);
		add(startMinuteChoice);
		add(endHourChoice);
		add(endMinuteChoice);
		
		add(addGoogleCalender);
		
		add(alertEnableCheckbox);
		add(alertTimeFied);
		add(alertUnitChoice);
		
		add(contentArea);
		
		add(submitBtn);
		add(cancelBtn);
	}

}
