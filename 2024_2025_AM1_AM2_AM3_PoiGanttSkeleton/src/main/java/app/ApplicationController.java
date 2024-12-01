package app;

import java.util.List;
import java.util.Objects;

import dom.gantt.TaskAbstract;
import service.IMainController;
import service.MainControllerFactory;
import util.FileTypes;
import util.ProjectInfo;

public class ApplicationController {
	private MainControllerFactory factory;
	private IMainController mainController;
	
	public ApplicationController() {
		this.factory = new MainControllerFactory ();
		this.mainController = factory.createMainController();
		if (Objects.isNull(this.mainController)){
			System.err.println("MainController : null controller, exiting.");
			System.exit(-1);
		}
	}
	
	public List<String> load(String fileName, FileTypes filetype){
		return this.mainController.load(fileName, filetype);
	}

	public ProjectInfo prepareTargetWorkbook(FileTypes fileType, String targetPath) {
		return this.mainController.prepareTargetWorkbook(fileType, targetPath);
	}


	public List<TaskAbstract> getAllTasks(){
		return this.mainController.getAllTasks();
	}

	public List<TaskAbstract> getTopLevelTasksOnly(){
		return this.mainController.getTopLevelTasksOnly();
	}


	public List<TaskAbstract> getTasksInRange(int firstIncluded, int lastIncluded){
		return this.mainController.getTasksInRange(firstIncluded, lastIncluded);
	}

	public String addFontedStyle(String styleName, short styleFontColor, short styleFontHeightInPoints, String styleFontName,
			boolean styleFontBold, boolean styleFontItalic, boolean styleFontStrikeout, short styleFillForegroundColor,
			String styleFillPatternString, String HorizontalAlignmentString, boolean styleWrapText) {
		return this.mainController.addFontedStyle(styleName, 
				styleFontColor, styleFontHeightInPoints, styleFontName, 
				styleFontBold, styleFontItalic, styleFontStrikeout, 
				styleFillForegroundColor, styleFillPatternString, HorizontalAlignmentString, styleWrapText);
	}

	public boolean createNewSheet(String sheetName, List<TaskAbstract> tasks, String headerStyleName, String topBarStyleName,
			String topDataStyleName, String nonTopBarStyleName, String nonTopDataStyleName, String normalStyleName) {
		return this.mainController.createNewSheet(sheetName, tasks, 
				headerStyleName, topBarStyleName, topDataStyleName, nonTopBarStyleName, nonTopDataStyleName, normalStyleName);
	}

	public boolean rawWriteToExcelFile(List<TaskAbstract> tasks) {
		return this.mainController.rawWriteToExcelFile(tasks);
	}
	
}//end class
