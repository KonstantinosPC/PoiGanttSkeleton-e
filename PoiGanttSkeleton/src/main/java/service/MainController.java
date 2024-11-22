package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class MainController implements IMainController {

	private String sourcePath;

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
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public boolean rawWriteToExcelFile(List<TaskAbstract> tasks) {
		// TODO Auto-generated method stub
		return false;
	}


	/* From here \/ */
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

			try (FileInputStream inputStream = new FileInputStream(new File(sourcePath));
				FileOutputStream outputStream = new FileOutputStream(new File(sourcePath))) {
					
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

				if (workbook.getSheet(sheetName) != null) {
					System.err.println("Sheet with name " + sheetName + " already exists.");
					return false;
				}

		
				XSSFSheet newSheet = workbook.createSheet(sheetName);

				XSSFRow headerRow = newSheet.createRow(0);
				headerRow.createCell(0).setCellValue("Task Name");
				headerRow.createCell(1).setCellValue("Task Description");

				workbook.write(outputStream);


				workbook.close();
				return true;

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			

	}


	/* To here /\ */
}
