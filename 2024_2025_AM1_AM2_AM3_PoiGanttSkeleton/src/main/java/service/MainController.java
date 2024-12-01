package service;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;
import dom.gantt.SimpleTask;


public class MainController implements IMainController {

	private FileTypes fileType;
	private String sourcePath;
	private List<String> loadedTasks;
	private List<TaskAbstract> tasks;
	private Workbook sheet;
	
	@Override
	public List<String> load(String sourcePath, FileTypes filetype) {
		
		this.sourcePath = sourcePath;
        this.fileType = filetype;
		
        List<String> tasks = new ArrayList<>();

        switch (filetype) {
            case XLS:
                tasks = loadXls(sourcePath); 
                break;

            case XLSX:
                tasks = loadXlsx(sourcePath);
                break;

            case CSV:
            case CSV_EU:
                char delimiter = (filetype == FileTypes.CSV_EU) ? ';' : ','; 
                tasks = loadCsvOrTsv(sourcePath, delimiter); 
                break;

            case TSV:
                tasks = loadCsvOrTsv(sourcePath, '\t');
                break;

            default:
                throw new IllegalArgumentException("Unsupported file type: " + filetype); 
        }
        loadedTasks = tasks;
        return tasks; 
    }

	@Override
	public ProjectInfo prepareTargetWorkbook(FileTypes fileType, String targetPath) {
		 if (fileType == null || sourcePath == null) {
			 System.out.println("Return NULL #0");
			return null; // Return null if the file type or source path is not set
		}

		try (
			// Initialize the workbook in a try-with-resources block
			Workbook workbook = createWorkbook(fileType)
		) {
			if (workbook == null) {
				System.out.println("Return NULL #1");
				return null; // Return null if the workbook cannot be created
			}

			// Add a new sheet
			Sheet sheet = workbook.createSheet("Sheet1");

			// Load the tasks (read from the source file)
			List<String> tasks = loadedTasks;
			if (tasks == null || tasks.isEmpty()) {
				System.out.println("Return NULL #2");
				return null; // Return null if no tasks are loaded
			}

			// Write the tasks into the sheet row by row
			int rowIndex = 0;
			for (String task : tasks) {
				Row row = sheet.createRow(rowIndex++);
				String[] taskCells = task.split(","); // Assuming tasks are comma-separated
				for (int colIndex = 0; colIndex < taskCells.length; colIndex++) {
					Cell cell = row.createCell(colIndex);
					cell.setCellValue(taskCells[colIndex]);
				}
			}

			// Save the workbook to the target file
			System.out.println("HERE!!!");
			try (FileOutputStream fos = new FileOutputStream(targetPath)) {
				
				workbook.write(fos);
			}

			// Calculate metadata for ProjectInfo
			int totalNumTasks = tasks.size();
			int totalTopTasks = (int) tasks.stream().filter(t -> t.split(",").length == 1).count(); // Count single-column tasks

			// Construct and return ProjectInfo object
			String projectName = "GeneratedProject";
			String sourceFileName = new File(sourcePath).getName();
			String targetFileName = new File(targetPath).getName();
			return new ProjectInfo(projectName, sourceFileName, targetFileName, totalNumTasks, totalTopTasks);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Return NULL #3");
			return null; // Return null if something goes wrong
		}
	}

	@Override
	public List<TaskAbstract> getAllTasks() {
		
		//Two lists to return all Task-items, one for single tasks and another for toplevel tasks
		List<TaskAbstract> templistforsimpletasks = new ArrayList<>();
		List<TaskAbstract> templistfortopleveltasks = new ArrayList<>();
		
		//A list for Tasks, each task reprisented by a string
		List<String> Tasks = loadedTasks;
		
		//=====given that size of mamatasks is 3!!!*************************************************************************************************************=====
		
		//seperator is defined by the case of each fileType(the field of the class), because in load(method) seperator is not the same in every case of filetype(the field of load(method))
		String separator = "";
		for (String task : Tasks) {
			switch(fileType) {
			case XLS :
				/*!!!!!!!!!!!!!!!!!!! ----- CHeck first */
			case XLSX :
				
			case CSV :
				separator = ",";
				break;
			case CSV_EU :
				separator = ";";
				break;
			case TSV :
				
			default:
				separator = "\t";
			}
			//I'm reading each string to make 2 lists with completed TaskAbstract Objects, one for toplevel tasks(mamatasks), and one for sigle tasks(subtasks)
			String[] temp = task.split(separator);
			if(temp.length==3){
				templistfortopleveltasks.add(UpdateTask(temp[0],temp[1],temp[2],"0","0","0","0"));
			}else{
				templistforsimpletasks.add(UpdateTask(temp[0],temp[1],temp[2],temp[3],temp[4],temp[5],temp[6]));
			}
		}

		//Then I'm Sorting these 2 lists into 1(with mergesort), while updating each toplevel task(mamatask)_fields
		try {
			tasks = SortAllTasks(templistfortopleveltasks, templistforsimpletasks); 
			return tasks;	
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<TaskAbstract> getTopLevelTasksOnly() {
		List<TaskAbstract> allTasks = this.getAllTasks();
		List<TaskAbstract> topTasks = new ArrayList<>();
		
		for (TaskAbstract taskAbstract : allTasks) {
			if(taskAbstract.isSimple()==false){
				topTasks.add(taskAbstract);
			}
		}
		tasks = topTasks; 
		return tasks;
	}

	@Override
	public List<TaskAbstract> getTasksInRange(int firstIncluded, int lastIncluded) {
		List<TaskAbstract> allTasks = this.getAllTasks();
		List<TaskAbstract> IncludedTasks = new ArrayList<>();
		
		for (TaskAbstract taskAbstract : allTasks) {
			if((taskAbstract.getTaskId()<=lastIncluded)&&(taskAbstract.getTaskId()>=firstIncluded)){
				IncludedTasks.add(taskAbstract);
			}
		}
		tasks = IncludedTasks; 
		return tasks;
	}

	@Override
	public boolean rawWriteToExcelFile(List<TaskAbstract> tasks) {
		if (tasks == null || tasks.isEmpty()) {
	        System.err.println("No tasks provided to write to the Excel file.");
	        return false;
	    }

	    String targetFileName = "TasksOutput.xlsx"; // Default file name for the output file
	    try (Workbook workbook = new XSSFWorkbook()) { // Create a new workbook (XLSX format)
	        Sheet sheet = workbook.createSheet("Tasks");

	        // Create the header row
	        Row headerRow = sheet.createRow(0);
	        String[] headers = { "Task ID", "Task Text", "Container Task ID", "Start", "End", "Cost", "Effort" };
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	        }

	        // Fill data rows
	        int rowIndex = 1; // Start after the header row
	        for (TaskAbstract task : tasks) {
	            Row row = sheet.createRow(rowIndex++);
	            row.createCell(0).setCellValue(task.getTaskId());
	            row.createCell(1).setCellValue(task.getTaskText());
	            row.createCell(2).setCellValue(task.getContainerTaskId());
	            row.createCell(3).setCellValue(task.getTaskStart());
	            row.createCell(4).setCellValue(task.getTaskEnd());
	            row.createCell(5).setCellValue(task.getCost());
	            row.createCell(6).setCellValue(task.getEffort());
	        }

	        // Auto-size columns for better readability
	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        // Write the workbook to a file
	        try (FileOutputStream fos = new FileOutputStream(targetFileName)) {
	            workbook.write(fos);
	            System.out.println("Tasks written successfully to " + targetFileName);
	        }
	        return true;
	    } catch (IOException e) {
	        System.err.println("Error writing tasks to Excel file: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public String addFontedStyle(String styleName, short styleFontColor, short styleFontHeightInPoints,
			String styleFontName, boolean styleFontBold, boolean styleFontItalic, boolean styleFontStrikeout,
			short styleFillForegroundColor, String styleFillPatternString, String HorizontalAlignmentString,
			boolean styleWrapText) {
		
		CellStyle cellStyle = sheet.createCellStyle();
		Font font = sheet.createFont();
		
		font.setColor(styleFontColor);
		font.setFontHeightInPoints(styleFontHeightInPoints);
		font.setFontName(styleFontName);
		font.setBold(styleFontBold);
		font.setItalic(styleFontItalic);
		font.setStrikeout(styleFontStrikeout);
		
		
		cellStyle.setFillForegroundColor(styleFillForegroundColor);
		cellStyle.setWrapText(styleWrapText);
		
		if (styleFillPatternString != null && !styleFillPatternString.isEmpty()) {
		    FillPatternType fillPatternType = FillPatternType.valueOf(styleFillPatternString.toUpperCase());
		    cellStyle.setFillPattern(fillPatternType);
		}
		
		if (HorizontalAlignmentString != null && !HorizontalAlignmentString.isEmpty()) {
		    HorizontalAlignment alignment = HorizontalAlignment.valueOf(HorizontalAlignmentString.toUpperCase());
		    cellStyle.setAlignment(alignment);
		}
		
		
		return styleName;
	}

	@Override
	public boolean createNewSheet(String sheetName, List<TaskAbstract> tasks, String headerStyleName,
			String topBarStyleName, String topDataStyleName, String nonTopBarStyleName, String nonTopDataStyleName,
			String normalStyleName) {

				ProjectInfo ProjectWorkspace =  prepareTargetWorkbook(fileType, sourcePath);

			try (FileInputStream inputStream = new FileInputStream(new File(ProjectWorkspace.getSourceFileName()));
				FileOutputStream outputStream = new FileOutputStream(new File(ProjectWorkspace.getTargetFileName()))) {

				XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

				if (workbook.getSheet(sheetName) != null) {
					System.err.println("Sheet with name " + sheetName + " already exists.");
					workbook.close();
					return false;
				}
				
				this.sheet = workbook;
				
				XSSFSheet newSheet = workbook.createSheet(sheetName);

				/* Creates the Header Row which Implements all the basic info it needs */
				XSSFRow headerRow = newSheet.createRow(0);
				/* need to add to every cellValue the addFontedStyle method */
				headerRow.createCell(0).setCellValue("");
				headerRow.createCell(1).setCellValue("Level");
				headerRow.createCell(2).setCellValue("Id");
				headerRow.createCell(3).setCellValue("Description");
				headerRow.createCell(4).setCellValue("Cost");
				headerRow.createCell(5).setCellValue("Effort");
				
				int[] numberHeader = taskNumbering(tasks);
				for(int i=numberHeader[0]; i<numberHeader[1]; i++){
					headerRow.createCell(i+5).setCellValue(i);
				}
				/* This is where the header stops */


				workbook.write(outputStream);


				workbook.close();
				return true;

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

	}




	/* Helping Methods */

	private List<String> loadXls(String sourcePath) {
        List<String> rows = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(sourcePath);
             Workbook workbook = new HSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                StringBuilder rowString = new StringBuilder();
                for (Cell cell : row) {
                    rowString.append(cell.toString()).append(",");
                }
                rows.add(rowString.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading XLS file: " + sourcePath, e);
        }
        return rows;
     }
    

    private List<String> loadXlsx(String sourcePath) {
        List<String> rows = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(sourcePath);
                Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                StringBuilder rowString = new StringBuilder();
                for (Cell cell : row) {
                    rowString.append(cell.toString()).append(",");
                }
                rows.add(rowString.toString()); // No replacement of trailing commas
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading XLSX file: " + sourcePath, e);
        }
        return rows;
    }

    private List<String> loadCsvOrTsv(String sourcePath, char delimiter) {
    	List<String> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	String[] values = line.split(Character.toString(delimiter));
                // Process line based on the delimiter
                tasks.add(String.join(",", values));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading CSV/TSV file: " + sourcePath, e);
        }
        return tasks;       
    }
    // Getter for fileType
    public FileTypes getFileType() {
        return fileType;
    }

    // Setter for fileType
    public void setFileType(FileTypes fileType) {
        this.fileType = fileType;
    }

    // Getter for sourcePath
    public String getSourcePath() {
        return sourcePath;
    }

    // Setter for sourcePath
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

	private Workbook createWorkbook(FileTypes fileType) {
		switch (fileType) {
			case XLS:
				return new HSSFWorkbook();
			case XLSX:
				return new XSSFWorkbook();
			default:
				throw new IllegalArgumentException("Unsupported file type for target: " + fileType);
		}
	}


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

//--------------------------------------------------3----------------------------------------------------------------------------------------------------
	public List<String> getloadedTasks(){
		return loadedTasks;
	}
//__________________________________________________3____________________________________________________________________________________________________

//--------------------------------------------------4----------------------------------------------------------------------------------------------------
	public boolean setloadedTasks(List<String> tasklist){
		loadedTasks = tasklist;
		return true;
	}
//__________________________________________________4____________________________________________________________________________________________________


	public int[] taskNumbering(List<TaskAbstract> tasks){
		int list[] = {1,0};
		int max = 0;
		for(TaskAbstract x: tasks){
			if(x.getTaskEnd() > max){
				max = x.getTaskEnd();
			}
		}
		
		list[1] = max;
		return list;
	}
}
