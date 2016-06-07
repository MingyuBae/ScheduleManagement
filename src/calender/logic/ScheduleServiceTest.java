package calender.logic;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import calender.model.ScheduleData;

public class ScheduleServiceTest {

	/**
	 * 스케줄 추가기능 테스트 메소드
	 */
	@Test
	public void testOneAddSchedule(){
		ScheduleService scheduleService = new ScheduleService();
		
		scheduleService.addSchedule(2016, 4, 25, 00, 05, 13, 30, -1, "테스트 일정", "테스트 일정입니다.");
		
		List<ScheduleData> scheduleList = scheduleService.getScheduleList();
		assertEquals(scheduleList.size(), 1);
		
		ScheduleData scheduleData = scheduleList.get(0);		
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.set(2016, 4, 25, 00, 05);
		endDate.set(2016,  4, 25, 13, 30);
		
		assertEquals(scheduleData.getStartDate().getTimeInMillis(), startDate.getTimeInMillis());
		assertEquals(scheduleData.getEndDate().getTimeInMillis(), endDate.getTimeInMillis());
		assertEquals(scheduleData.getTitle(), "테스트 일정");
		assertEquals(scheduleData.getContent(), "테스트 일정입니다.");
	}
	
	/**
	 * 달력의 날짜와 일정간의 맵핑 결과 테스트
	 */
	@Test
	public void testCalenderMapping(){
		ScheduleService scheduleService = new ScheduleService();
		
		scheduleService.addSchedule(2016, 4, 29, 00, 05, 2016, 5, 1, 13, 30, -1, "테스트 일정", "테스트 일정입니다.");
		
		assertNull(scheduleService.getDayScheduleList(2016, 4, 1));
		assertNull(scheduleService.getDayScheduleList(2016, 4, 28));
		assertNull(scheduleService.getDayScheduleList(2016, 4, 32));
		
		assertEquals(scheduleService.getDayScheduleList(2016, 4, 29).size(), 1);
		assertEquals(scheduleService.getDayScheduleList(2016, 4, 30).size(), 1);
		assertEquals(scheduleService.getDayScheduleList(2016, 4, 31).size(), 1);
	}

}
