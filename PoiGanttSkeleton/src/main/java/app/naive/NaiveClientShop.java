package app.naive;

import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;

import app.ApplicationController;
import util.FileTypes;
import util.ProjectInfo;

public class NaiveClientShop {

	/*
	 * add -Dlog4j2.loggerContextFactory=org.apache.logging.log4j.simple.SimpleLoggerContextFactory 
	 * at the vm arguments, to avoid the complaints about the logger
	 */
	public static void main(String args[]) {
		ApplicationController appController = new ApplicationController();
		
		//  ////////////// Load /////////////////
		//List<String> loadedStr = appController.load("./src/test/resources/input/Shop.tsv", FileTypes.CSV);
		List<String> loadedStr = appController.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		
		System.out.println();System.out.println();
		System.out.println("----------");
		for (String s: loadedStr)
			System.out.println(s);

		//  ///////////////////// create workbook ///////////////
		ProjectInfo prjInfo = appController.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/output/ShopOutput.xlsx");
		System.out.println("----------");
		System.out.println("\n" + prjInfo);
		System.out.println("----------");

		//  ///////////////////// create sheets ///////////////
		appController.rawWriteToExcelFile(appController.getAllTasks());
		
		appController.createNewSheet("ALL_Styled", appController.getAllTasks(), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 

		appController.createNewSheet("Τop_Level", appController.getTopLevelTasksOnly(), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 

		String brownStyleName = appController.addFontedStyle("myBrownThing", 
				IndexedColors.BROWN.getIndex(), (short)10, "Times New Roman", 
				false, false, false, 
				IndexedColors.WHITE.getIndex(), "Solid", "Left", false);
		
		appController.createNewSheet("Range", appController.getTasksInRange(103,402), 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", brownStyleName, "Normal"); 
		
		System.out.println("End of naive xlsx client");
	}

}