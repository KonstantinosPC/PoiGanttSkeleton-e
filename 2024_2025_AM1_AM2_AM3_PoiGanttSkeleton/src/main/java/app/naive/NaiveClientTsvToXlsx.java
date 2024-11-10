package app.naive;

import java.util.List;

import app.ApplicationController;
import util.FileTypes;
import util.ProjectInfo;

public class NaiveClientTsvToXlsx {

	/*
	 * add -Dlog4j2.loggerContextFactory=org.apache.logging.log4j.simple.SimpleLoggerContextFactory 
	 * at the vm arguments, to avoid the complaints about the logger
	 */
	public static void main(String args[]) {
		ApplicationController appController = new ApplicationController();

		List<String> loadedStr = appController.load("./src/test/resources/input/EggsScrambled.tsv", FileTypes.TSV);
		//List<String> loadedStr = appController.load("src/test/resources/input/EggsScrambled.xlsx", FileTypes.XLSX);
		
		System.out.println();System.out.println();
		System.out.println("----------");
		for (String s: loadedStr)
			System.out.println(s);
		
		//create workbook
		ProjectInfo prjInfo = appController.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/output/EggsScrambled_Output_TSVToXlsx.xlsx");
		System.out.println("----------");
		System.out.println("\n" + prjInfo);
		System.out.println("----------");
		
		appController.rawWriteToExcelFile(appController.getAllTasks());

		appController.createNewSheet("ALL_Styled", appController.getAllTasks(), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 

		appController.createNewSheet("Τop_Level", appController.getTopLevelTasksOnly(), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 

		appController.createNewSheet("Range", appController.getTasksInRange(103,202), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 
	
		System.out.println("End of naive xlsx client");
	}

}
