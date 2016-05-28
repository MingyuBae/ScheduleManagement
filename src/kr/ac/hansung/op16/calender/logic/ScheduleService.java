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

import kr.ac.hansung.op16.calender.model.ScheduleData;

public class ScheduleService{
	private static ScheduleService instance = null;
	
	private int mappingTargetYear, mappingTargetMonth;
	private String fileName = "calender.dat";
	private List<ScheduleData> scheduleList = new LinkedList<>();
	private Map<Integer, List<ScheduleData>> calenderMappingScheduleList;
	
	public ScheduleService(){
		this("calender.dat");
	}
	public ScheduleService(String fileName){
		this.fileName = fileName;
	}
	
	/**
	 * 싱글톤 패턴으로 일정관리 서비스를 사용하기 위해
	 * 하나의 인스턴스를 생성/가져오는 메소드
	 * @return
	 */
	public static ScheduleService getInstence(){
		if(instance == null){
			instance = new ScheduleService();
		}
		return instance;
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
	@SuppressWarnings("unchecked")
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
		return true;
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
	public void addSchedule(int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute, String title, String content){
		addSchedule(new ScheduleData(year, month, day, startHour, startMinute, endHour, endMinute, title, content));
	}
	
	public void addSchedule(int startYear, int startMonth, int startDay, int startHour, int startMinute, 
			int endYear, int endMonth, int endDay, int endHour, int endMinute, String title, String content){
		addSchedule(new ScheduleData(startYear, startMonth, startDay, startHour, startMinute,
										endYear, endMonth, endDay, endHour, endMinute, title, content));
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
	
	
	List<ScheduleData> getScheduleList() {
		return scheduleList;
	}
	
}
