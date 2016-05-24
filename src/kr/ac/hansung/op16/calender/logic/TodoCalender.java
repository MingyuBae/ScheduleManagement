package kr.ac.hansung.op16.calender.logic;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.ac.hansung.op16.calender.model.TodoData;

public class TodoCalender {
	private String fileName;
	private List<TodoData> todoList = new LinkedList<>();
	
	public TodoCalender(String fileName){
		this.fileName = fileName;
	}
	
	public boolean saveFileTodoList(){
		try{
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(todoList);
			outStream.close();
			fileOut.close();
		} catch(IOException e){
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean readFileTodoList(){
		try{
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream inStream = new ObjectInputStream(fileIn);
			todoList = (List<TodoData>) inStream.readObject();
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
	
	public Map<Integer, List<TodoData>> getCalendarMappingTodoList(int year, int month){
		Map<Integer, List<TodoData>> calendarMappingData = new HashMap<>();
		
		for(int i=0; i<todoList.size(); i++){
			TodoData eachTodoData = todoList.get(i);
			Calendar todoDate = eachTodoData.getDate();
			
			if(todoDate.get(Calendar.YEAR) == year && (todoDate.get(Calendar.MONTH) + 1) == month){
				int day = todoDate.get(Calendar.DAY_OF_MONTH);
				List<TodoData> dayTodoList;
				if(calendarMappingData.get(day) == null){
					dayTodoList = new LinkedList<TodoData>();
				} else {
					dayTodoList = calendarMappingData.get(day);
				}
				dayTodoList.add(eachTodoData);
				
				calendarMappingData.put(day, dayTodoList);
			}
		}
		
		return calendarMappingData;
	}	
	
	public void addTodo(int year, int month, int day, String title, String content){
		addTodo(new TodoData(year, month, day, title, content));
	}
	
	private void addTodo(TodoData todoData){
		todoList.add(todoData);
	}
	
	public void deleteTodo(int listIndex){
		todoList.remove(listIndex);
	}
}
