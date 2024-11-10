package service;

import java.util.List;

import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

/**
 * The main interface for the back-end of the system
 * 
 * @author pvassil
 * @since  2024/09/29
 */
public interface IMainController {

	/**
	 * The method loads an input file You expect that in the back end, the loading converts 
	 * the file contents to an internal representation of the Project.
	 * What is returned back to the caller, however, is a list of strings, one for each task of the input
	 *  
	 * @param sourcePath A string with the path to the input file
	 * @param filetype A util.FileTypes value, depending on whether the file is XLS, XLSX, TSV, CSV etc 
	 * @return a list of strings, one for each task of the input file 
	 */
	List<String> load(String sourcePath, FileTypes filetype);

	/**
	 * After loading, the method assigns a target along with its filetype for the output.
	 * Also it creates any intermediate objects in the back-end.
	 * For example, a POI workbook has to be created here for the target file.
	 * 
	 *  IDEALLY: load() would have been incorporated inside this call.
	 *  They are given to you here as separate, to drive you into implementing the code step by step
	 *  
	 * @param fileType A util.FileTypes value, XLS, XLSX
	 * @param targetPath A string with the path to the output file
	 * @return A ProjectInfo object with all the details of the input-output (see class util.ProjectInfo)  
	 */
	ProjectInfo prepareTargetWorkbook(FileTypes fileType, String targetPath);

	/**
	 * Returns all the loaded tasks of the project
	 * 
	 * @return All the tasks of the project, or null if sth goes wrong
	 */
	List<TaskAbstract> getAllTasks();

	/**
	 * Returns all the loaded top-level tasks of the project
	 * 
	 * @return All the top-level tasks of the project, or null if sth goes wrong
	 */
	List<TaskAbstract> getTopLevelTasksOnly();

	/**
	 * Returns all the tasks of the project whose id is within [first,last] (included)
	 * This is independent on whether they are top or not
	 * 
	 * @return All the tasks of the project that fit the filter, or null if sth goes wrong
	 * 
	 * @param firstIncluded lowest acceptable TaskId
	 * @param lastIncluded highest acceptable TaskId
	 * @return All the tasks that qualify, or null if sth goes wrong
	 */
	List<TaskAbstract> getTasksInRange(int firstIncluded, int lastIncluded);

	/**
	 * Adds to the target file (already specified by method prepareTargetWorkbook() )
	 * a sheet with the raw intermediate representation of (a subset of) the project.
	 * The tasks to be loaded are either all the tasks, or a subset.
	 * In any case, one is expected to populated the respective parameter with a call 
	 * to the above-defined filters
	 * The name of the sheet is the Date of the system, formatted as "dd-MM-yyyy HH_mm_ss"
	 *  
	 *  HINT: Implement this before trying the createNewSheet() method
	 *  
	 * @param tasks A List of TaskAstract objects with the selected tasks 
	 * @return true if all goes well, false otherwise
	 */
	boolean rawWriteToExcelFile(List<TaskAbstract> tasks);
	
	/**
	 * Adds a new style with a name to the gallery of styles the project retains.
	 * 
	 * @param styleName a String with the name of the new style
	 * @param styleFontColor
	 * @param styleFontHeightInPoints
	 * @param styleFontName
	 * @param styleFontBold
	 * @param styleFontItalic
	 * @param styleFontStrikeout
	 * @param styleFillForegroundColor
	 * @param styleFillPatternString
	 * @param HorizontalAlignmentString
	 * @param styleWrapText
	 * @return if everything goes well, the styleName, or, "Normal", if sth goes wrong
	 */
	String addFontedStyle(String styleName, short styleFontColor, short styleFontHeightInPoints, String styleFontName,
			boolean styleFontBold, boolean styleFontItalic, boolean styleFontStrikeout, short styleFillForegroundColor,
			String styleFillPatternString, String HorizontalAlignmentString, boolean styleWrapText);

	/**
	 * Adds to the target file (already specified by method prepareTargetWorkbook() )
	 * a sheet with the styled representation of (a subset of) the project.
	 * The tasks to be loaded are either all the tasks, or a subset.
	 * In any case, one is expected to populated the respective parameter with a call 
	 * to the above-defined filters.
	 * The styles to be employed for the styling are specified by the respective parameters.
	 * 
	 * @param sheetName A String with the name of the sheet
	 * @param tasks  A List of TaskAstract objects with the selected tasks 
	 * @param headerStyleName A String to pick up from the system's style gallery the respective CellStyle for headers
	 * @param topBarStyleName A String to pick up from the system's style gallery the respective CellStyle for the bars of top tasks
	 * @param topDataStyleName A String to pick up from the system's style gallery the respective CellStyle for the data of top tasks
	 * @param nonTopBarStyleName A String to pick up from the system's style gallery the respective CellStyle for the bars of nontop tasks
	 * @param nonTopDataStyleName A String to pick up from the system's style gallery the respective CellStyle for the data of nontop tasks
	 * @param normalStyleName A String to pick up from the system's style gallery the respective Normal CellStyle for all else 
	 * @return true if all goes well, false otherwise
	 */
	boolean createNewSheet(String sheetName, List<TaskAbstract> tasks, String headerStyleName, String topBarStyleName,
			String topDataStyleName, String nonTopBarStyleName, String nonTopDataStyleName, String normalStyleName);



}