package calender.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import calender.logic.ScheduleService;
import calender.model.ScheduleData;

public class AddSchedulePanel extends JPanel {
	Label selectedDateLable;
	Label titleLable = new Label("제목");
	TextField titleField = new TextField(30);

	Label dateLabel = new Label("시간");

	Choice startHourChoice = new Choice();
	Choice startMinuteChoice = new Choice();
	Label timeLabel = new Label("~");
	Choice endHourChoice = new Choice();
	Choice endMinuteChoice = new Choice();

	Checkbox addGoogleCalender = new Checkbox("Google Calendar 등록");
	Checkbox alertEnableCheckbox = new Checkbox("알람");
	TextField alertTimeFied = new TextField("5");
	Choice alertUnitChoice = new Choice();
	TextArea contentArea = new TextArea(3, 30);

	Button submitBtn = new Button("추가");
	Button cancelBtn = new Button("취소");

	public AddSchedulePanel(int year, int month, int day, JFrame thisFrame, JFrame mainFrame) {
		ScheduleService scheduleService = ScheduleService.getInstence();

		/* 이벤트 등록 */

		for (int i = 0; i < 24; i++) {
			startHourChoice.add("" + i);
			endHourChoice.add("" + i);
		}

		for (int i = 0; i < 60; i += 5) {
			startMinuteChoice.add("" + i);
			endMinuteChoice.add("" + i);
		}

		setLayout(new GridBagLayout());

		selectedDateLable = new Label("" + year + "년 " + (month + 1) + "월 " + day + "일");
		GridBagConstraints gbc_selectedDateLable = new GridBagConstraints();
		gbc_selectedDateLable.gridwidth = 2;
		gbc_selectedDateLable.insets = new Insets(0, 0, 5, 0);
		gbc_selectedDateLable.gridx = 1;
		gbc_selectedDateLable.gridy = 0;
		add(selectedDateLable, gbc_selectedDateLable);
		GridBagConstraints gbc_titleLable = new GridBagConstraints();
		gbc_titleLable.insets = new Insets(0, 0, 5, 5);
		gbc_titleLable.gridx = 1;
		gbc_titleLable.gridy = 2;
		add(titleLable, gbc_titleLable);

		JPanel fieldPanel = new JPanel(new GridLayout(0, 1, 1, 1));
		GridBagConstraints gbc_fieldPanel = new GridBagConstraints();
		gbc_fieldPanel.fill = GridBagConstraints.BOTH;
		gbc_fieldPanel.insets = new Insets(0, 0, 5, 0);
		gbc_fieldPanel.gridx = 2;
		gbc_fieldPanel.gridy = 2;
		add(fieldPanel, gbc_fieldPanel);
		fieldPanel.add(titleField);
		GridBagConstraints gbc_dateLabel = new GridBagConstraints();
		gbc_dateLabel.insets = new Insets(0, 0, 5, 5);
		gbc_dateLabel.gridx = 1;
		gbc_dateLabel.gridy = 3;
		add(dateLabel, gbc_dateLabel);

		JPanel timePanel = new JPanel(new FlowLayout());
		GridBagConstraints gbc_timePanel = new GridBagConstraints();
		gbc_timePanel.anchor = GridBagConstraints.WEST;
		gbc_timePanel.insets = new Insets(0, 0, 5, 0);
		gbc_timePanel.gridx = 2;
		gbc_timePanel.gridy = 3;
		add(timePanel, gbc_timePanel);
		timePanel.add(startHourChoice);
		timePanel.add(startMinuteChoice);
		timePanel.add(timeLabel);
		timePanel.add(endHourChoice);
		timePanel.add(endMinuteChoice);
		alertEnableCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					alertTimeFied.setEnabled(true);
					alertUnitChoice.setEnabled(true);
				} else {
					alertTimeFied.setEnabled(false);
					alertUnitChoice.setEnabled(false);
				}

			}
		});

		alertEnableCheckbox.setState(true);

		alertUnitChoice.add("분");
		alertUnitChoice.add("시간");
		alertUnitChoice.select(0);
		addGoogleCalender.setEnabled(scheduleService.getSettingData().isGoogleApiEnable());
		addGoogleCalender.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (addGoogleCalender.getState()) {
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
		GridBagConstraints gbc_addGoogleCalender = new GridBagConstraints();
		gbc_addGoogleCalender.gridwidth = 2;
		gbc_addGoogleCalender.insets = new Insets(0, 0, 5, 0);
		gbc_addGoogleCalender.gridx = 1;
		gbc_addGoogleCalender.gridy = 4;
		add(addGoogleCalender, gbc_addGoogleCalender);

		JPanel checkboxFildPanel = new JPanel(new FlowLayout());
		checkboxFildPanel.add(alertEnableCheckbox);
		checkboxFildPanel.add(alertTimeFied);
		checkboxFildPanel.add(alertUnitChoice);
		GridBagConstraints gbc_alertFildPanel = new GridBagConstraints();
		gbc_alertFildPanel.gridwidth = 2;
		gbc_alertFildPanel.insets = new Insets(0, 0, 5, 0);
		gbc_alertFildPanel.gridx = 1;
		gbc_alertFildPanel.gridy = 5;
		add(checkboxFildPanel, gbc_alertFildPanel);
		GridBagConstraints gbc_contentArea = new GridBagConstraints();
		gbc_contentArea.insets = new Insets(0, 0, 5, 0);
		gbc_contentArea.gridwidth = 2;
		gbc_contentArea.fill = GridBagConstraints.BOTH;
		gbc_contentArea.gridx = 1;
		gbc_contentArea.gridy = 6;
		add(contentArea, gbc_contentArea);
		submitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = titleField.getText();
				int startHour = Integer.parseInt(startHourChoice.getSelectedItem());
				int startMinute = Integer.parseInt(startMinuteChoice.getSelectedItem());
				int endHour = Integer.parseInt(endHourChoice.getSelectedItem());
				int endMinute = Integer.parseInt(endMinuteChoice.getSelectedItem());
				String content = contentArea.getText();
				int alertTime = -1;
				boolean save = true;

				if (startHour > endHour) {
					JOptionPane.showMessageDialog(new Frame(), "끝나는 시간이 시작 시간보다 빠릅니다.", "시간 입력 오류",
							JOptionPane.ERROR_MESSAGE);
					save = false;
				} else if (startHour == endHour) {
					if (startMinute > endMinute) {
						JOptionPane.showMessageDialog(new Frame(), "끝나는 시간이 시작 시간보다 빠릅니다.", "시간 입력 오류",
								JOptionPane.ERROR_MESSAGE);
						save = false;
					}
				}
				if (title.length() == 0) {
					JOptionPane.showMessageDialog(new Frame(), "제목이 비었습니다.", "제목 입력 오류", JOptionPane.ERROR_MESSAGE);
					save = false;
				}
				if (alertEnableCheckbox.getState()) {
					String selectedUnit = alertUnitChoice.getSelectedItem();
					if ("시간".equals(selectedUnit)) {
						alertTime = Integer.parseInt(alertTimeFied.getText()) * 60 * 60;
					} else if ("분".equals(selectedUnit)) {
						alertTime = Integer.parseInt(alertTimeFied.getText()) * 60;
					} else {
						alertTime = Integer.parseInt(alertTimeFied.getText());
					}
				}

				if (save == true) {
					if (addGoogleCalender.getState()) {
						ScheduleData addScheduleData = new ScheduleData(year, month, day, startHour, startMinute,
								endHour, endMinute, alertTime, title, content);
						scheduleService.getGoogleCalendarApiService().addGoogleCalendarSchedule(addScheduleData);
						scheduleService.calendarMappingRefresh();
					} else {
						scheduleService.addSchedule(year, month, day, startHour, startMinute, endHour, endMinute,
								alertTime, title, content);
					}

					thisFrame.setVisible(false);
					thisFrame.dispose();

					scheduleService.scheduleAlertSet();
					((MainFrame) mainFrame).calenderRepaint();
					mainFrame.revalidate();
				}
			}
		});

		JPanel btnPanel = new JPanel(new FlowLayout());
		btnPanel.add(submitBtn);
		btnPanel.add(cancelBtn);
		GridBagConstraints gbc_btnPanel = new GridBagConstraints();
		gbc_btnPanel.insets = new Insets(0, 0, 5, 0);
		gbc_btnPanel.gridwidth = 3;
		gbc_btnPanel.gridx = 1;
		gbc_btnPanel.gridy = 7;
		add(btnPanel, gbc_btnPanel);
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisFrame.setVisible(false);
				thisFrame.dispose();
			}
		});

		JPanel labelPanel = new JPanel(new GridLayout(0, 1, 1, 1));
		labelPanel.setSize(new Dimension(100, 200));
		GridBagConstraints gbc_labelPanel = new GridBagConstraints();
		gbc_labelPanel.insets = new Insets(0, 0, 5, 5);
		gbc_labelPanel.gridy = 8;
		gbc_labelPanel.gridx = 0;
		gbc_labelPanel.fill = GridBagConstraints.HORIZONTAL;
		add(labelPanel, gbc_labelPanel);
		// thisFrame.add(formPanel, BorderLayout.CENTER);
		// thisFrame.add(bottomPanel, BorderLayout.SOUTH);
		//
		// thisFrame.setTitle("일정 추가");

	}

}
