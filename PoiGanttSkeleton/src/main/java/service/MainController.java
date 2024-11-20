package service;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	@Override
	public List<TaskAbstract> getAllTasks() {
		
		//Two lists to return all Task-items, one for sigle tasks and another for toplevel tasks
		List<TaskAbstract> templistforsimpletasks = new ArrayList<>();
		List<TaskAbstract> templistfortopleveltasks = new ArrayList<>();
		
		//A list for Tasks, each task reprisented by a string
		List<String> Tasks = this.load("",/FilleTypes.CSV_EU);
		
		//=====I have to know the structure of List<String> to write the code correctly, I will make it with split(given that each string is saparated by "\t")===== 
		//=====given that size of mamatasks is 3!!!*************************************************************************************************************=====
		
		//I'm reading each string to make 2 lists with completed TaskAbstract Objects, one for toplevel tasks(mamatasks), and one for sigle tasks(subtasks)
		for (String task : Tasks) {
			String separator = "\t";
			String[] temp = task.split(separator);
			if(temp.length==3){
				templistfortopleveltasks.add(UpdateTask(temp[0],temp[1],temp[2],"0","0","0","0"));
			}else{
				templistforsimpletasks.add(UpdateTask(temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],temp[6]));
			}
		}

		//Then I'm Sorting these 2 lists into 1(with mergesort), while updating each toplevel task(mamatask)_fields
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

//====================================================================================================new private methods====================================================================================================
//--------------------------------------------------1----------------------------------------------------------------------------------------------------

	//A helping method to Update task_fields (by Changing the Strings to ints), making a SimpleTask task(Object) 
	private TaskAbstract UpdateTask(String taskId, String taskText,String containerTaskId,String Start,String end, String cost,String effort){
		TaskAbstract newtask = new SimpleTask(Integer.parseInt(taskId),taskText,Integer.parseInt(containerTaskId),Integer.parseInt(Start),Integer.parseInt(end),Integer.parseInt(cost),Integer.parseInt(effort));
		return newtask;
	}	

//__________________________________________________1____________________________________________________________________________________________________

//--------------------------------------------------2.0----------------------------------------------------------------------------------------------------

	//A helping method to sort 2 Lists (of single tasks and topleveltasks) into one List
	private List<TaskAbstract> SortAllTasks(List<TaskAbstract> toplevel,List<TaskAbstract> singletask){
		
		//Making 2 sorted Lists(with TaskAbstract Objects), one with toplevel tasks, one with singletasks and the Final List 
		List<TaskAbstract> Final = new ArrayList<>();
		List<TaskAbstract> topTasks = SortList(toplevel);
		List<TaskAbstract> subtasks = SortList(singletask);

		//I will fill the Final List (by merging the /\ two sorted Lists)
		for (TaskAbstract task : topTasks) {

			//First I push the mamatask into Final 
			Final.add(task);

			//bellow are the mamaTask charasteristics:
			int Start = 0;
			int End = 0;
			int Cost = 0;
			int Efford = 0;	

			//Then I push the subtasks of each mamatask (by doing same comparations-operations)
			for (TaskAbstract subtask : subtasks) {

				//if (id of toplevel) == (singletask mamaid), then singletask is a subtask of the topleveltask
				if((task.getTaskId()==subtask.getContainerTaskId())&&(subtask!=null)){

					//put the subtasks 
					Final.add(subtask);

					//change the charasteristics
					if((Start>subtask.getTaskStart())||(Start == 0)){
						Start = subtask.getTaskStart();
					}
					if((End<subtask.getTaskEnd())||(End == 0)){
						End = subtask.getTaskEnd();
					}
					Cost += subtask.getCost();
					Efford += subtask.getEffort();
				}
				
				//maybe we can make it more efficient!
				continue;
			}

			//Finally I'm updating mamatask characteristics
			Final.set(Final.indexOf(task), UpdateTask(""+task.getTaskId(), task.getTaskText(), ""+task.getContainerTaskId(), ""+Start, ""+End, ""+Cost, ""+Efford));
		}
		return Final;
	}

//__________________________________________________2.0____________________________________________________________________________________________________

//--------------------------------------------------2.1----------------------------------------------------------------------------------------------------

	//Sorting a list using mergesort (Factorial method)
	private List<TaskAbstract> SortList(List<TaskAbstract> alist){

		if(alist.size() <= 1){
			return alist;
		}

		int middle = alist.size()/2;
		List<TaskAbstract> left = new ArrayList<>(alist.subList(0, middle));
		List<TaskAbstract> right = new ArrayList<>(alist.subList(middle,alist.size()));
		
		return merge(SortList(left),SortList(right));
	}

//__________________________________________________2.1____________________________________________________________________________________________________

//--------------------------------------------------2.2----------------------------------------------------------------------------------------------------

	//mergesort method (for TaskAbstract Objects)
	private List<TaskAbstract> merge(List<TaskAbstract> left,List<TaskAbstract> right){
		List<TaskAbstract> result = new ArrayList<>();
		int i = 0;
		int j = 0;

		while(i< left.size() && j< right.size()){
			TaskAbstract leftTask = left.get(i);
			TaskAbstract rightTask = right.get(j);

			if(compareTasks(leftTask,rightTask) <= 0){
				result.add(leftTask);
				i++;
			}else{
				result.add(rightTask);
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

//__________________________________________________2.2____________________________________________________________________________________________________

//--------------------------------------------------2.3----------------------------------------------------------------------------------------------------


	//a method like compareTo(in TaskAbstract class)..
	private int compareTasks(TaskAbstract task1,TaskAbstract task2){
		int containerDiff = task1.getContainerTaskId() - task2.getContainerTaskId();
		
		if(containerDiff != 0){
			return containerDiff;
		}
		return task1.compareTo(task2);
	}

//__________________________________________________2.3____________________________________________________________________________________________________

//____________________________________________________________________________________________________new private methods____________________________________________________________________________________________________

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
