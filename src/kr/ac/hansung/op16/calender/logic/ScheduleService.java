package kr.ac.hansung.op16.calender.logic;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import kr.ac.hansung.op16.calender.model.CalendarSettingData;
import kr.ac.hansung.op16.calender.model.ScheduleData;

public class ScheduleService{
	private static String SETTING_FILE_NAME = "setting.dat";
	private static ScheduleService INSTANCE = null;
	
	private CalendarSettingData settingData;
	private int mappingTargetYear, mappingTargetMonth;
	private String fileName = "calender.dat";
	private List<ScheduleData> scheduleList = new LinkedList<>();
	private Map<Integer, List<ScheduleData>> calenderMappingScheduleList;
	private GoogleCalendarApiService googleCalendarApiService = new GoogleCalendarApiService();
	Timer scheduleAlertTimer = new Timer();
	
	public ScheduleService(){
		if(! readFileSettingData()){
			settingData = new CalendarSettingData();
		}
		if(settingData.isAutoReadScheduleListFile()){
			fileName = settingData.getLastScheduleListFilePath();
			readFileSchedulList();
		}
	}
	
	/**
	 * 싱글톤 패턴으로 일정관리 서비스를 사용하기 위해
	 * 하나의 인스턴스를 생성/가져오는 메소드
	 * @return
	 */
	public static ScheduleService getInstence(){
		if(INSTANCE == null){
			INSTANCE = new ScheduleService();
		}
		return INSTANCE;
	}
	
	/**
	 * List에 저장되있는 스케줄을 파일로 저장하는 메소드
	 * @return 저장 성공 여부
	 */
	public boolean saveFileSchedulList(){
		try{
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(scheduleList);
			outStream.close();
			fileOut.close();
		} catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * 파일에 저장되있는 스케줄을 읽어오는 메소드
	 * @return 읽기 성공 여부
	 */
	public boolean readFileSchedulList(){
		try{
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			scheduleList = (List<ScheduleData>) inStream.readObject();
			inStream.close();
			fileIn.close();
		} catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		calenderMappingScheduleList = calendarMappingScheduleList(mappingTargetYear, mappingTargetMonth);
		
		return true;
	}
	
	/**
	 * 설정을 파일로 저장하는 메소드
	 * @return 저장 성공 여부
	 */
	public boolean saveFileSettingData(){
		try{
			FileOutputStream fileOut = new FileOutputStream(SETTING_FILE_NAME);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(settingData);
			outStream.close();
			fileOut.close();
		} catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 파일에 저장되있는 스케줄을 읽어오는 메소드
	 * @return 읽기 성공 여부
	 */
	public boolean readFileSettingData(){
		try{
			FileInputStream fileIn = new FileInputStream(SETTING_FILE_NAME);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			settingData = (CalendarSettingData) inStream.readObject();
			inStream.close();
			fileIn.close();
		} catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	
	public void scheduleAlertSet(){
		Calendar nowDate = Calendar.getInstance();
		scheduleAlertTimer.cancel();
		scheduleAlertTimer = new Timer();
		
		for(ScheduleData scheduleEach : scheduleList){
			if(scheduleEach.getAlertDate() != null){
				if(nowDate.getTimeInMillis() < scheduleEach.getAlertDate().getTime()){
					scheduleAlertTimer.schedule(new AlertTimer(scheduleEach), scheduleEach.getAlertDate());
				}
			}
		}
	}
	
	/**
	 * 해당일의 일정 목록을 반환하는 메소드
	 * @param year
	 * @param month
	 * @param day
	 * @return 해당일의 일정 목록 (null시 없음)
	 */
	public List<ScheduleData> getDayScheduleList(int year, int month, int day){
		if(mappingTargetYear != year || mappingTargetMonth != month){
			mappingTargetYear = year;
			mappingTargetMonth = month;
			calenderMappingScheduleList = calendarMappingScheduleList(year, month);
		}
		return calenderMappingScheduleList.get(day);
	}
	
	/**
	 * 스케줄 맵핑을 강제로 새로고침
	 */
	public void calendarMappingRefresh(){
		calenderMappingScheduleList = calendarMappingScheduleList(mappingTargetYear, mappingTargetMonth);
	}
	
	/**
	 * 스케줄 리스트와 달력의 각 날짜를 연결
	 * @param year 연결시킬 달력의 년도
	 * @param month 연결시킬 달력의 월
	 * @return 맵핑 결과
	 */
	private Map<Integer, List<ScheduleData>> calendarMappingScheduleList(int year, int month){
		Map<Integer, List<ScheduleData>> calendarMappingData = new HashMap<>();
		
		for(int i=0; i<scheduleList.size(); i++){
			calendarMappingSchedule(year, month, calendarMappingData, scheduleList.get(i));
		}
		
		if(settingData.isGoogleApiEnable()){
			List<ScheduleData> googleCalendarSchedule = googleCalendarApiService.getSchedule(year, month);
			if(googleCalendarSchedule == null)
				googleCalendarSchedule = new LinkedList<>();
			
			if(settingData.isShowHolidaySchedule())
				googleCalendarSchedule.addAll(googleCalendarApiService.getScheduleEvent(GoogleCalendarApiService.CALENDAR_EVENT_HOLIDAY, year, month));
			if(settingData.isShowHansungUnivSchedule())
				googleCalendarSchedule.addAll(googleCalendarApiService.getScheduleEvent(GoogleCalendarApiService.CALENDAR_EVENT_HANSUNGUNIV, year, month));
			
			for(int i=0; i<googleCalendarSchedule.size(); i++){
				calendarMappingSchedule(year, month, calendarMappingData, googleCalendarSchedule.get(i));
			}
		}
		return calendarMappingData;
	}
	
	/**
	 * 스케줄과 날짜를 연결
	 * @param calendarMappingData 맵핑된 결과를 담는 맵
	 * @param scheduleData 일정 정보
	 * @return 맵핑된 결과
	 */
	private Map<Integer, List<ScheduleData>> calendarMappingSchedule(int year, int month, Map<Integer, List<ScheduleData>> calendarMappingData, ScheduleData scheduleData){
		Calendar startDate = scheduleData.getStartDate();
		Calendar endDate = scheduleData.getEndDate();
		
		if(startDate.get(Calendar.YEAR) <= year && (startDate.get(Calendar.MONTH)) <= month
				&& endDate.get(Calendar.YEAR) >= year && (endDate.get(Calendar.MONTH)) >= month){
			int startDay, endDay;		//맵핑될 일정의 시작일, 종료일을 저장하는 변수
			
			if(startDate.get(Calendar.YEAR) == year && startDate.get(Calendar.MONTH) == month){
				/* 일정 시작일이 해당하는 달에 있다면 */
				startDay = startDate.get(Calendar.DAY_OF_MONTH);
			} else {
				/* 일정 시작일이 해당하는 달에 있지 않을 경우 1일부터  */
				startDay = 1;
			}
			
			if(endDate.get(Calendar.YEAR) == year && endDate.get(Calendar.MONTH) == month){
				/* 일정 종료일이 해당하는 달에 있다면 */
				endDay = endDate.get(Calendar.DAY_OF_MONTH);
			} else {
				/* 일정 종료일이 해당하는 달에 있지 않을 경우   */
				Calendar tempMonthCalender =  Calendar.getInstance();
				tempMonthCalender.set(year, month, 1);
				endDay = tempMonthCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
			}
			
			for(int day=startDay; day<=endDay; day++){
				/* 일정 시작일과 종료일 사이를 맵핑시킴 */
				List<ScheduleData> dayTodoList;
			
				if(calendarMappingData.get(day) == null){
					/* 해당일에 기존에 등록된 일정이 없다면 */
					dayTodoList = new LinkedList<ScheduleData>();		// 해당일에 기존에 등록된 일정이 없다면 새로운 리스트 객체 생성
					calendarMappingData.put(day, dayTodoList);
				} else {
					dayTodoList = calendarMappingData.get(day);			// 해당일에 기존에 등록된 일정이 있다면 기존 리스트 객체를 불러옴
				}
				dayTodoList.add(scheduleData);				
			}
		}
		
		for(List<ScheduleData> eachScheduleList : calendarMappingData.values()){
			/* 일정 리스트를 시작 시간 순서로 정렬 */
			Collections.sort(eachScheduleList, new Comparator<ScheduleData>() {
				@Override
				public int compare(ScheduleData arg0, ScheduleData arg1){
					return arg0.getStartDate().compareTo(arg1.getStartDate());
				}
			});
		}
		return calendarMappingData;
	}
	
	/**
	 * 스케줄 리스트를 추가하는 메소드
	 * api를 통해 가져온 정보가 중복 저장되지 않게 하기 위해 id가 중복된 값은 추가하지 않음
	 * @param addScheduleList
	 */
	public int addScheduleList(List<ScheduleData> addScheduleList){
		int addScheduleCount = 0;
		
		for(ScheduleData addScheduleEach : addScheduleList){
			boolean scheduleOverlap = false;
			
			if(addScheduleEach.isExternalSchedule()){
				for(int j=0; j<scheduleList.size(); j++){
					ScheduleData scheduleEach = scheduleList.get(j);
					if(scheduleEach.isExternalSchedule() 
							&& addScheduleEach.getId().equals(scheduleEach.getId())){
						scheduleOverlap = true;
						scheduleList.set(j, addScheduleEach);
						break;
					}
				}
			}
			
			if(! scheduleOverlap){
				scheduleList.add(addScheduleEach);
				addScheduleCount ++;
			}
		}
		return addScheduleCount;
	}
	
	/**
	 * 스케줄 리스트에 일정을 추가시키는 메소드
	 * @param year 일정 년도
	 * @param month 일정 월
	 * @param day 일정 날짜
	 * @param startHour 일정 시작 시
	 * @param startMinute 일성 시작 분
	 * @param endHour 일정 종료 시
	 * @param endMinute 일전 종료 분
	 * @param title 일정명
	 * @param content 일정 상세 정보
	 */
	public void addSchedule(int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute, int alertTime, String title, String content){
		addSchedule(new ScheduleData(year, month, day, startHour, startMinute, endHour, endMinute, alertTime, title, content));
	}
	
	public void addSchedule(int startYear, int startMonth, int startDay, int startHour, int startMinute, 
			int endYear, int endMonth, int endDay, int endHour, int endMinute, int alertTime, String title, String content){
		addSchedule(new ScheduleData(startYear, startMonth, startDay, startHour, startMinute,
										endYear, endMonth, endDay, endHour, endMinute, alertTime, title, content));
	}
	
	/**
	 * 스케줄 리스트에 일정을 추가시키는 메소드
	 * @param todoData
	 */
	private void addSchedule(ScheduleData todoData){
		scheduleList.add(todoData);
		calenderMappingScheduleList = calendarMappingScheduleList(mappingTargetYear, mappingTargetMonth);
	}
	
	/**
	 * 리스트에 저장되있는 일정중 하나를 삭제시키는 메소드
	 * @param listIndex 리스트에 들어있는 일정 객체의 인덱스 번호
	 */
	public void deleteSchedule(int listIndex){
		scheduleList.remove(listIndex);
		calenderMappingScheduleList = calendarMappingScheduleList(mappingTargetYear, mappingTargetMonth);
	}
	
	public boolean deleteSchedule(ScheduleData deleteScheduleData){
		boolean isRemove = scheduleList.remove(deleteScheduleData);
		if(isRemove)
			calenderMappingScheduleList = calendarMappingScheduleList(mappingTargetYear, mappingTargetMonth);
		return isRemove;
	}
	
	
	public List<ScheduleData> getScheduleList() {
		return scheduleList;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public CalendarSettingData getSettingData() {
		return settingData;
	}
	public void setSettingData(CalendarSettingData settingData) {
		this.settingData = settingData;
		saveFileSettingData();
	}
	public GoogleCalendarApiService getGoogleCalendarApiService() {
		return googleCalendarApiService;
	}
}
