package kr.ac.hansung.op16.calender.view;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JOptionPane;

import kr.ac.hansung.op16.calender.logic.GoogleCalendarApiService;
import kr.ac.hansung.op16.calender.logic.ScheduleService;
import kr.ac.hansung.op16.calender.model.CalendarSettingData;

public class SettingPanel extends Panel {
	Checkbox autoReadScheduleListFileBtn = new Checkbox("자동으로 마지막으로 저장한 일정 파일 읽어오기");
	Checkbox googleApiEnableBtn = new Checkbox("Google Calender 연동");
	Checkbox showHolidayScheduleBtn = new Checkbox("휴일 정보 가져오기");
	Checkbox showHansungUnivScheduleBtn = new Checkbox("한성대학교 학사정보 가져오기");
	
	Button deleteGoogleApiAuthBtn = new Button("Goole 계정 정보 삭제");
	
	Button okBtn = new Button("확인");
	Button cancelBtn = new Button("취소");
	
	public SettingPanel(Frame thisFrame) {
		GoogleCalendarApiService apiService = new GoogleCalendarApiService();
		ScheduleService scheduleService = ScheduleService.getInstence();
		CalendarSettingData settingData = scheduleService.getSettingData();
		
		autoReadScheduleListFileBtn.setState(settingData.isAutoReadScheduleListFile());
		googleApiEnableBtn.setState(settingData.isGoogleApiEnable());
		showHolidayScheduleBtn.setState(settingData.isShowHolidaySchedule());
		showHansungUnivScheduleBtn.setState(settingData.isShowHansungUnivSchedule());
		
		if(settingData.isGoogleApiEnable()){
			showHansungUnivScheduleBtn.setEnabled(true);
			showHolidayScheduleBtn.setEnabled(true);
		} else {
			showHansungUnivScheduleBtn.setEnabled(false);
			showHolidayScheduleBtn.setEnabled(false);
		}
		
		deleteGoogleApiAuthBtn.setEnabled(apiService.isAuthFileExist());
		
		googleApiEnableBtn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(googleApiEnableBtn.getState()){
					showHansungUnivScheduleBtn.setEnabled(true);
					showHolidayScheduleBtn.setEnabled(true);
				} else {
					showHansungUnivScheduleBtn.setEnabled(false);
					showHolidayScheduleBtn.setEnabled(false);
				}
			}
		});
		deleteGoogleApiAuthBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(apiService.deleteGoogleApiAuth()){
					JOptionPane.showMessageDialog(new Frame(), "구글 인증 파일이 성공적으로 삭제되었습니다.", "구글 인증정보 삭제 성공", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(new Frame(), "구글 인증파일 삭제에 실패하였습니다", "구글 인증정보 삭제 실패", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CalendarSettingData newSettingData = new CalendarSettingData();
				newSettingData.setAutoReadScheduleListFile(autoReadScheduleListFileBtn.getState());
				newSettingData.setGoogleApiEnable(googleApiEnableBtn.getState());
				newSettingData.setShowHansungUnivSchedule(showHansungUnivScheduleBtn.getState());
				newSettingData.setShowHolidaySchedule(showHolidayScheduleBtn.getState());
				newSettingData.setLastScheduleListFilePath(settingData.getLastScheduleListFilePath());
				
				scheduleService.setSettingData(newSettingData);
				
				thisFrame.setVisible(false);
				thisFrame.dispose();
			}
		});
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisFrame.setVisible(false);
				thisFrame.dispose();
			}
		});
		
		add(autoReadScheduleListFileBtn);
		add(googleApiEnableBtn);
		add(showHolidayScheduleBtn);
		add(showHansungUnivScheduleBtn);
		add(deleteGoogleApiAuthBtn);
		add(okBtn);
		add(cancelBtn);
	}

}