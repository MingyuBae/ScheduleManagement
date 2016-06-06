package kr.ac.hansung.op16.calender.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import kr.ac.hansung.op16.calender.logic.ScheduleService;

public class AddSchedulePanel extends JPanel {
	
	boolean save = true;
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JPanel panel4 = new JPanel();
	JPanel panel5 = new JPanel();
	JPanel panel6 = new JPanel();
	Label selectedDateLable;
	Label titleLable = new Label("제목");
	TextField titleField = new TextField(20);
	
	Label dateLabel = new Label("기간");
	
	Choice startHourChoice = new Choice();
	Choice startMinuteChoice = new Choice();
	Label timeLabel = new Label("~");
	Choice endHourChoice = new Choice();
	Choice endMinuteChoice = new Choice();
	
	Checkbox alertEnableCheckbox = new Checkbox("알람");
	TextField alertTimeFied = new TextField("5");
	Choice alertUnitChoice = new Choice();
	
	Label contentLable = new Label("상세내용");
	TextArea contentArea = new TextArea(3,30);
	
	Button submitBtn = new Button("추가");
	Button cancelBtn = new Button("취소");
	
	public AddSchedulePanel(int year, int month, int day, JFrame thisFrame, JFrame mainFrame) {
		ScheduleService scheduleService = ScheduleService.getInstence();
		
		selectedDateLable = new Label("" + year + "년 " + (month+1) + "월 " + day + "일");
		thisFrame.setLayout(new GridLayout(7,1,0,0));
		thisFrame.setPreferredSize(new Dimension(350,500));
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
				int alertTime = -1;
				
				if(startHour > endHour){
					JOptionPane.showMessageDialog(new Frame(), "끝나는 시간이 시작 시간보다 빠릅니다.", "시간 입력 오류", JOptionPane.ERROR_MESSAGE);
					save = false;
				}
				else if(startHour==endHour){
					if(startMinute>endMinute){
					JOptionPane.showMessageDialog(new Frame(), "끝나는 시간이 시작 시간보다 빠릅니다.", "시간 입력 오류", JOptionPane.ERROR_MESSAGE);
					save = false;
					}
				}
				
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
				
				if(save==true){
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
		
		for(int i=0; i<24; i++){
			startHourChoice.add("" + i);
			endHourChoice.add("" + i);
		}
		
		for(int i=0; i<60; i+=5){
			startMinuteChoice.add("" + i);
			endMinuteChoice.add("" + i);
		}

		alertEnableCheckbox.setState(true);
		
		alertUnitChoice.add("분");
		alertUnitChoice.add("시간");
		alertUnitChoice.select(0);
		
		panel1.add(selectedDateLable);
		
		panel2.add(titleLable);
		panel2.add(titleField);
		
		panel3.add(dateLabel);
		panel3.add(startHourChoice);
		panel3.add(startMinuteChoice);
		panel3.add(timeLabel);
		panel3.add(endHourChoice);
		panel3.add(endMinuteChoice);
		
		panel4.add(alertEnableCheckbox);
		panel4.add(alertTimeFied);
		panel4.add(alertUnitChoice);
		
		panel5.add(contentLable);
		panel5.add(contentArea);
		
		panel6.add(submitBtn);
		panel6.add(cancelBtn);
		
		thisFrame.add(panel1);
		thisFrame.add(panel2);
		thisFrame.add(panel3);
		thisFrame.add(panel4);
		thisFrame.add(panel5);
		thisFrame.add(panel6);
		thisFrame.setTitle("일정 추가");
		
	}

}
