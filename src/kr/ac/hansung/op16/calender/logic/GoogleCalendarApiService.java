package kr.ac.hansung.op16.calender.logic;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import kr.ac.hansung.op16.calender.model.ScheduleData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GoogleCalendarApiService {
	private static final String CALENDAR_EVENT_SCHEDULE = "primary";
	private static final String CALENDAR_EVENT_HOLIDAY = "ko.south_korea#holiday@group.v.calendar.google.com";
	private static final String CALENDAR_EVENT_HANSUNGUNIV = "hansunginfoteam@gmail.com";
	
	/** Application name. */
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/calendar-java-quickstart.json");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/calendar-java-quickstart.json
	 */
	private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	private static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = GoogleCalendarApiService.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Calendar client service.
	 * 
	 * @return an authorized Calendar client service
	 * @throws IOException
	 */
	private static com.google.api.services.calendar.Calendar getCalendarService() throws IOException {
		Credential credential = authorize();
		return new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}
	
	/**
	 * 구글 켈린더에 사용자의 일정을 가져오는 메소드
	 * @param year
	 * @param month
	 * @return
	 */
	public List<ScheduleData> getSchedule(int year, int month){
		List<ScheduleData> scheduleList  = new LinkedList<>();
		List<Event> eventList = getGoogleCalendarSchedule(CALENDAR_EVENT_SCHEDULE, year, month);
		
		if(eventList != null){
			for(Event eventEach : eventList){
				scheduleList.add(eventToScheduleData(eventEach));
			}
			return scheduleList;
		} else {
			return null;
		}
	}
	
	/**
	 * 구글 캘린더에 대한민국 휴일 정보를 가져오는 메소드
	 * @param year
	 * @param month
	 * @return
	 */
	public List<ScheduleData> getHolidayEvent(int year, int month){
		List<ScheduleData> scheduleList  = new LinkedList<>();
		List<Event> eventList = getGoogleCalendarSchedule(CALENDAR_EVENT_HOLIDAY, year, month);
		
		if(eventList != null){
			for(Event eventEach : eventList){
				scheduleList.add(eventToScheduleData(eventEach));
			}
			return scheduleList;
		} else {
			return null;
		}
	}
	
	/**
	 * 구글 캘린더로 부터 해당 달의 일정을 가져오는 메소드
	 * @param year
	 * @param month
	 * @return
	 */
	private List<Event> getGoogleCalendarSchedule(String listName, int year, int month) {
		try {
			com.google.api.services.calendar.Calendar service = getCalendarService();
			
			Calendar monthStartCalendar =  Calendar.getInstance();
			monthStartCalendar.set(year, month, 1, 0, 0, 0);
			
			Calendar monthEndCalendar =  Calendar.getInstance();
			monthEndCalendar.set(year, month, monthEndCalendar.getActualMaximum(Calendar.DAY_OF_MONTH), 24, 59, 59);
			
			DateTime startTime = new DateTime(monthStartCalendar.getTime());
			DateTime endTime = new DateTime(monthEndCalendar.getTime());
			Events events = service.events().list(listName)
											.setTimeMin(startTime)
											.setTimeMax(endTime)
											.setOrderBy("startTime")
											.setSingleEvents(true)
											.execute();
			List<Event> items = events.getItems();
			
			return items;
		} catch (IOException e) {
			System.err.println("데이터를 가져오는 중 오류가 발생하였습니다");
		}
		return null;
	}
	
	/**
	 * google API에서 받은 이벤트를 프로그램 내부에 사용하는 일정 정보로 변환하는 메소드
	 * @param target
	 * @return
	 */
	public ScheduleData eventToScheduleData(Event target){
		Calendar startDate = eventDateTimeToCalendar(target.getStart(), true);
		Calendar endDate = eventDateTimeToCalendar(target.getEnd(), false);
		
		return new ScheduleData(target.getId(), startDate, endDate, target.getSummary(), target.getDescription(), null, true);
	}
	
	/**
	 * 구글 API의 DateTime을 java의 Calendar로 변환해주는 메소드
	 * @param target
	 * @return
	 */
	public Calendar eventDateTimeToCalendar(EventDateTime eventDateTimeTarget, boolean isStart){
		Calendar returnValue = Calendar.getInstance();
		if(eventDateTimeTarget.getDateTime() == null){
			returnValue.setTimeInMillis(eventDateTimeTarget.getDate().getValue());
			if(isStart){
				returnValue.set(Calendar.HOUR_OF_DAY, 0);
				returnValue.set(Calendar.MINUTE, 0);
				returnValue.set(Calendar.SECOND, 0);
			} else {
				returnValue.set(Calendar.HOUR_OF_DAY, 23);
				returnValue.set(Calendar.MINUTE, 59);
				returnValue.set(Calendar.SECOND, 59);
			}
		} else {
			returnValue.setTimeInMillis(eventDateTimeTarget.getDateTime().getValue());
		}
		return returnValue;
	}

}
