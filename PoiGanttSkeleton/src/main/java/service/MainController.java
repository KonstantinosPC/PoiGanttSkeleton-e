package service;

import java.util.ArrayList;
import java.util.List;

import dom.gantt.SimpleTask;
import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

public class MainController implements IMainController {

	@Override
	public List<String> load(String sourcePath, FileTypes filetype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProjectInfo prepareTargetWorkbook(FileTypes fileType, String targetPath) {
		// TODO Auto-generated method stub
		return null;
	}

//__________________________________________________________________________________________________________________________________________________________________________________________________________________
	
	@Override
	public List<TaskAbstract> getAllTasks() {
		// TODO Auto-generated method stub
		//Two lists to return all Task-items, one for sigle tasks and another for toplevel tasks
		List<TaskAbstract> templistforsimpletasks = null;
		List<TaskAbstract> templistfortopleveltasks = null;
		//A list for Tasks, each task reprisented by a string
		List<String> Tasks = this.load("",/FilleTypes.CSV_EU);
		//we merge each string with a task while sorting it
		//I have to know the structure of List<String> to write the code correctly, I will make it with split(given that each string is saparated by "\t"), 
		//TODO check the code 
		for (String task : Tasks) {
			String separator = "\t";
			String[] temp = task.split(separator);
			if(temp.length==3){
				templistfortopleveltasks.add(UpdateTask(temp[0],temp[1],temp[2],"0","0","0","0"));
			}else{
				templistforsimpletasks.add(UpdateTask(temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],temp[6]));
			}
		}
		try {
			return SortAllTasks(templistfortopleveltasks, templistforsimpletasks);	
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<TaskAbstract> getTopLevelTasksOnly() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskAbstract> getTasksInRange(int firstIncluded, int lastIncluded) {
		// TODO Auto-generated method stub
		return null;
	}

//-----------------------------new private methods----------------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------1------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//A helping method to Update a null task (by Changing the Strings to ints)
	private TaskAbstract UpdateTask(String taskId, String taskText,String containerTaskId,String Start,String end, String cost,String effort){
		TaskAbstract newtask = new SimpleTask(Integer.parseInt(taskId),taskText,Integer.parseInt(containerTaskId),Integer.parseInt(Start),Integer.parseInt(end),Integer.parseInt(cost),Integer.parseInt(effort));
		return newtask;
	}	
//---------------------------------------1------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------2------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//A helping method to sort 2 Lists (of tasks and topleveltasks) into one List
	private List<TaskAbstract> SortAllTasks(List<TaskAbstract> toplevel,List<TaskAbstract> singletask){
		List<TaskAbstract> Final = new ArrayList<>();
		List<TaskAbstract> topTasks = SortList(toplevel);
		List<TaskAbstract> subtasks = SortList(topTasks);
		

		//I will fill the Final List (by the two sorted methods)
		for (TaskAbstract task : topTasks) {
			Final.add(task);
			for (TaskAbstract subtask : subtasks) {
				//if (id of toplevel) == (singletask mamaid), then singletask is a subtask of the topleveltask
				if(task.getTaskId()==subtask.getContainerTaskId()){
					//put the subtasks 
					Final.add(subtask);
					//here\/ we may hit an error!->ConcurrentModificationException
					subtasks.remove(subtask);
				}else{
					break;
				}
			}
		}

		return Final;
	}
//---------------------------------------2------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------2.1----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//Sorting a list using mergesort
	private List<TaskAbstract> SortList(List<TaskAbstract> alist){
		if(alist.size() <= 1){
			return alist;
		}

		int middle = alist.size()/2;
		List<TaskAbstract> left = new ArrayList<>(alist.subList(0, middle));
		List<TaskAbstract> right = new ArrayList<>(alist.subList(middle,alist.size()));
		return merge(SortList(left),SortList(right));
	}
//---------------------------------------2.1------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------2.2------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//mergesort method
	private List<TaskAbstract> merge(List<TaskAbstract> left,List<TaskAbstract> right){
		List<TaskAbstract> result = new ArrayList<>();
		int i = 0;
		int j = 0;
	
		while(i< left.size() && j< right.size()){
			TaskAbstract leftTask = left.get(i);
			TaskAbstract rightTask = right.get(j);
			
			if(compareTasks(leftTask,rightTask) <= 0){
				result.add(rightTask);
				i++;
			}else{
				result.add(leftTask);
				j++;
			}
		}

		while (i< left.size()){
			result.add(left.get(i));
			i++;
		}

		while (j< right.size()){
			result.add(right.get(j));
			j++;
		}

		return result;
	}
//---------------------------------------2.2------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------2.3------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	//a method like compareTo..
	private int compareTasks(TaskAbstract task1,TaskAbstract task2){
		int containerDiff = task1.getContainerTaskId() - task2.getContainerTaskId();
		if(containerDiff != 0){
			return containerDiff;
		}
		return task1.compareTo(task2);
	}

//---------------------------------------2.3------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------new private methods------------------------------------------------------------------------------------------------------------------------------------------------------------------

//__________________________________________________________________________________________________________________________________________________________________________________________________________________
	@Override
	public boolean rawWriteToExcelFile(List<TaskAbstract> tasks) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String addFontedStyle(String styleName, short styleFontColor, short styleFontHeightInPoints,
			String styleFontName, boolean styleFontBold, boolean styleFontItalic, boolean styleFontStrikeout,
			short styleFillForegroundColor, String styleFillPatternString, String HorizontalAlignmentString,
			boolean styleWrapText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createNewSheet(String sheetName, List<TaskAbstract> tasks, String headerStyleName,
			String topBarStyleName, String topDataStyleName, String nonTopBarStyleName, String nonTopDataStyleName,
			String normalStyleName) {
		// TODO Auto-generated method stub
		return false;
	}

}
