package service;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class MainController implements IMainController {

	private String sourcePath;
	private FileTypes fileType;

	@Override
	public List<String> load(String sourcePath, FileTypes filetype) {
		// TODO Auto-generated method stub
		this.sourcePath = sourcePath;
		this.fileType = filetype;
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


	public int[] taskNumbering(List<TaskAbstract> tasks){
		int list[] = {1,0};
		int max = 0;
		for(TaskAbstract x: tasks){
			if(tasks.getTaskEnd() > max){
				max = tasks.getTaskEnd();
			}
		}
		
		list[1] = max;
		return list;
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

		
				XSSFSheet newSheet = workbook.createSheet(sheetName);

				/* Creates the Header Row which Implements all the basic info it needs */
				XSSFRow headerRow = newSheet.createRow(0);
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


	/* To here /\ */
}
