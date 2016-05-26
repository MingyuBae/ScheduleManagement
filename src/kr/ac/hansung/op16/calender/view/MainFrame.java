package kr.ac.hansung.op16.calender.view;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.RepaintManager;

import kr.ac.hansung.op16.calender.logic.ScheduleService;
import kr.ac.hansung.op16.calender.model.ScheduleData;

import java.awt.Window.Type;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
	ScheduleService scheduleService = new ScheduleService();
	Map<Integer, List<ScheduleData>> calenderMappingScheduleList;
	
	int year, month, day;
	Panel calenderPanel;
	Panel scheludeListPanel;
	
	Button f = new Button("test");
	Button f2 = new Button("test2");
	
	Button f3 = new Button("test3");
	Button f4 = new Button("test4");
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		Calendar nowDate = Calendar.getInstance();
		year = nowDate.get(Calendar.YEAR);
		month = nowDate.get(Calendar.MONTH);
		day = nowDate.get(Calendar.DAY_OF_MONTH);
		
		setType(Type.POPUP);
		setTitle("일정 관리");
		setSize(800, 800);
		setBounds(100, 100, 450, 300);
		setLayout(new GridLayout(1, 2));
		
		calenderPanel = new CalenderPanel(year, month, this);
		scheduleService.addSchedule(year, month, day, 00, 05, 13, 30, "테스트 일정", "테스트 일정입니다.");
		calenderMappingScheduleList = scheduleService.calendarMappingScheduleList(year, month);
		
		scheludeListPanel = new ScheduleListPanel(calenderMappingScheduleList, this, year, month, day);
		add(calenderPanel);
		add(scheludeListPanel);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void calenderRepaint(){
		calenderPanel = new CalenderPanel(year, month, this);
		scheludeListPanel = new ScheduleListPanel(calenderMappingScheduleList, this, year, month, day);
		
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
