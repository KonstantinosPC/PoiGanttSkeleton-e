package service;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import java.util.Scanner;

import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

public class MainController implements IMainController {

	@Override
	public List<String> load(String sourcePath, FileTypes filetype) {
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

        return tasks; 
    }

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
                 rows.add(rowString.toString().replaceAll(",$", ""));
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
                // Process line based on the delimiter
                tasks.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading CSV/TSV file: " + sourcePath, e);
        }
        return tasks;
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
