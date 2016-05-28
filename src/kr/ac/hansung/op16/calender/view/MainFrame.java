package kr.ac.hansung.op16.calender.view;
import java.awt.*;
import javax.swing.*;

import kr.ac.hansung.op16.calender.logic.ScheduleService;
import kr.ac.hansung.op16.calender.model.ScheduleData;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
	ScheduleService scheduleService;
	Map<Integer, List<ScheduleData>> calenderMappingScheduleList;
	
	int year, month, day;
	Panel calenderPanel;
	Panel scheludeListPanel;

	public MainFrame() {
		Calendar nowDate = Calendar.getInstance();
		scheduleService = ScheduleService.getInstence();
		
		year = nowDate.get(Calendar.YEAR);
		month = nowDate.get(Calendar.MONTH);
		day = nowDate.get(Calendar.DAY_OF_MONTH);
		calenderPanel = new CalenderPanel(year, month, this);
		scheludeListPanel = new ScheduleListPanel(year, month, day, this);
		
		setType(Type.POPUP);
		setTitle("일정 관리");
		setSize(800, 800);
		setBounds(100, 100, 450, 300);
		setLayout(new GridLayout(1, 2));
		
		
		add(calenderPanel);
		add(scheludeListPanel);
		
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void calenderRepaint(){
		calenderPanel = new CalenderPanel(year, month, this);
		scheludeListPanel = new ScheduleListPanel(year, month, day, this);
		
		getContentPane().removeAll();
		getContentPane().add(calenderPanel);
		getContentPane().add(scheludeListPanel);
		revalidate();
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setDay(int day) {
		this.day = day;
	}
}
