package app.naive;

import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;

import app.ApplicationController;
import util.FileTypes;
import util.ProjectInfo;

public class NaiveClient {

	/*
	 * add -Dlog4j2.loggerContextFactory=org.apache.logging.log4j.simple.SimpleLoggerContextFactory 
	 * at the vm arguments, to avoid the complaints about the logger
	 */
	public static void main(String args[]) {

		ApplicationController appController = new ApplicationController();
		
		//  ////////////// Load /////////////////
		//List<String> loadedStr = appController.load("./src/test/resources/input/EggsScrambled.tsv", FileTypes.CSV);
		List<String> loadedStr = appController.load("src/test/resources/input/EggsScrambled.xlsx", FileTypes.XLSX);	
		System.out.println();System.out.println();
		System.out.println("----------");
		for (String s: loadedStr)
			System.out.println(s);

		//  ///////////////////// create workbook ///////////////
		//create workbook: absolutely needed for all else.
		ProjectInfo prjInfo = appController.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/output/EggsScrambled_Output.xlsx");
		System.out.println("----------");
		System.out.println("\n" + prjInfo);
		System.out.println("----------");
		
		//  ///////////////////// create sheets ///////////////
		appController.rawWriteToExcelFile(appController.getAllTasks());

		//String styleName,	
		//short styleFontColor, short styleFontHeightInPoints, String styleFontName, 
		//boolean styleFontBold, boolean styleFontItalic,boolean styleFontStrikeout,
		//short styleFillForegroundColor,String styleFillPatternString,	String HorizontalAlignmentString, boolean styleWrapText
		String greenStyleName = appController.addFontedStyle("myTealThing", 
				IndexedColors.TEAL.getIndex(), (short)10, "Times New Roman", 
				false, false, false, 
				IndexedColors.WHITE.getIndex(), "Solid", "Left", false);
		
		String orangeStyleName = appController.addFontedStyle("myOrangeThing", 
				IndexedColors.RED.getIndex(), (short)10, "Times New Roman", 
				false, false, false, 
				IndexedColors.ORANGE.getIndex(), "Solid", "Left", false);
		
		appController.createNewSheet("ALL_Styled", appController.getAllTasks(), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 

		appController.createNewSheet("Τop_Level", appController.getTopLevelTasksOnly(), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 

		appController.createNewSheet("Range", appController.getTasksInRange(103,202), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", orangeStyleName, greenStyleName, "Normal"); 
		
		System.out.println("End of naive xlsx client");
	}

}
