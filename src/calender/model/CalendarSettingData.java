package calender.model;

import java.io.Serializable;

public class CalendarSettingData implements Serializable{
	boolean googleApiEnable = false;
	boolean autoReadScheduleListFile = false;
	boolean showHansungUnivSchedule = true;
	boolean showHolidaySchedule = true;
	String lastScheduleListFilePath;
	
	public boolean isGoogleApiEnable() {
		return googleApiEnable;
	}
	public void setGoogleApiEnable(boolean googleApiEnable) {
		this.googleApiEnable = googleApiEnable;
	}
	public boolean isAutoReadScheduleListFile() {
		return autoReadScheduleListFile;
	}
	public void setAutoReadScheduleListFile(boolean autoReadScheduleListFile) {
		this.autoReadScheduleListFile = autoReadScheduleListFile;
	}
	public boolean isShowHansungUnivSchedule() {
		return showHansungUnivSchedule;
	}
	public void setShowHansungUnivSchedule(boolean showHansungUnivSchedule) {
		this.showHansungUnivSchedule = showHansungUnivSchedule;
	}
	public String getLastScheduleListFilePath() {
		return lastScheduleListFilePath;
	}
	public void setLastScheduleListFilePath(String lastScheduleListFilePath) {
		this.lastScheduleListFilePath = lastScheduleListFilePath;
	}
	public boolean isShowHolidaySchedule() {
		return showHolidaySchedule;
	}
	public void setShowHolidaySchedule(boolean showHolidaySchedule) {
		this.showHolidaySchedule = showHolidaySchedule;
	}
}
