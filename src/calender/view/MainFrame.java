package calender.view;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import calender.logic.GoogleCalendarApiService;
import calender.logic.ScheduleService;
import calender.model.ScheduleData;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
	ScheduleService scheduleService;
	Map<Integer, List<ScheduleData>> calenderMappingScheduleList;
	
	int year, month, day;
	
	MenuBar menuBar = new MenuBar();
	Menu fileMenu = new Menu("파일");
	Menu settingMenu = new Menu("설정");
	MenuItem saveMenuItem = new MenuItem("저장");
	MenuItem openMenuItem = new MenuItem("열기");
	MenuItem eventGetMenuItem = new MenuItem("이번달 일정가져오기");
	MenuItem refreshMenuItem = new MenuItem("새로고침");
	MenuItem settingOpenMenuItem = new MenuItem("설정");
	
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
		
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("일정목록", "dat"));
				
				if (fileChooser.showSaveDialog(new Frame()) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
					  // load from file
					  String filePath = file.getPath();
					  if (!filePath .endsWith(".dat"))
						  filePath += ".dat";
					  scheduleService.setFileName(filePath);
					  
					  if(! scheduleService.saveFileSchedulList()){
						  JOptionPane.showMessageDialog(new Frame(), "파일을 저장할 수 없습니다!", "파일 저장 오류", JOptionPane.ERROR_MESSAGE);
					  }
				}
				
			}
		});
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("일정목록", "dat"));
				
				if (fileChooser.showOpenDialog(new Frame()) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
					  // load from file
					  scheduleService.setFileName(file.getPath());
					  if(! scheduleService.readFileSchedulList()){
						  JOptionPane.showMessageDialog(new Frame(), "파일을 읽을 수 없습니다!", "파일 읽기 오류", JOptionPane.ERROR_MESSAGE);
					  }
				}
				scheduleService.calendarMappingRefresh();
				calenderRepaint();
			}
		});
		eventGetMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GoogleCalendarApiService apiService = new GoogleCalendarApiService();
				List<ScheduleData> scheduleList =  apiService.getSchedule(year, month);
				if(scheduleList != null){
					int addScheduleCount = scheduleService.addScheduleList(scheduleList);
					JOptionPane.showMessageDialog(new Frame(), year + "년 " + (month+1) + "월 일정을 " + addScheduleCount + "개 가져왔습니다.", "데이터 가져오기 성공", JOptionPane.INFORMATION_MESSAGE);
					scheduleService.calendarMappingRefresh();
					calenderRepaint();
				} else {
					JOptionPane.showMessageDialog(new Frame(), "데이터를 가져오는 중 오류가 발생했습니다!", "데이터 가져오기 오류", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		refreshMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scheduleService.calendarMappingRefresh();
				calenderRepaint();
			}
		});
		settingOpenMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame settingFrame = new JFrame();
				Panel settingPanel = new SettingPanel(settingFrame);
				settingFrame.add(settingPanel);
				settingFrame.pack();
				settingFrame.setVisible(true);
				settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		
		fileMenu.add(saveMenuItem);
		fileMenu.add(openMenuItem);
		
		settingMenu.add(eventGetMenuItem);
		settingMenu.add(refreshMenuItem);
		settingMenu.addSeparator();
		settingMenu.add(settingOpenMenuItem);
		
		menuBar.add(fileMenu);
		menuBar.add(settingMenu);
		
		setType(Type.POPUP);
		setTitle("일정 관리");
		setSize(800, 800);
		setBounds(100, 100, 450, 300);
		setLayout(new GridLayout(1, 2));
		setMenuBar(menuBar);
		
		
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
