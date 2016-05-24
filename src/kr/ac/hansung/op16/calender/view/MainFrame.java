package kr.ac.hansung.op16.calender.view;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.RepaintManager;

import java.awt.Window.Type;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
	int year, month, day;
	Panel calenderPanel;
	
	Button f = new Button("test");
	Button f2 = new Button("test2");
	
	Button f3 = new Button("test3");
	Button f4 = new Button("test4");
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setType(Type.POPUP);
		setTitle("일정 관리");
		setSize(800, 800);
		setBounds(100, 100, 450, 300);
		setLayout(new GridLayout(1, 2));
		f.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		year = 2016;
		month = 5;
		
		calenderPanel = new CalenderPanel(year, month, this);
		add(calenderPanel);
		add(f2);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void calenderRepaint(){
//		calenderPanel.removeAll();
//		calenderPanel.setVisible(false);
		calenderPanel = new CalenderPanel(year, month, this);
		
		setContentPane(calenderPanel);
		revalidate();
		
//		add(calenderPanel, 0);
//		setVisible(true);
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
