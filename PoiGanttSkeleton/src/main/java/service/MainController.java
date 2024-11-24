package service;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;


import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

public class MainController implements IMainController {

	 private FileTypes fileType;
	 private String sourcePath;
	 
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
                // Process line based on the delimiter
                tasks.add(line);
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

    
 
      
	@Override
	public ProjectInfo prepareTargetWorkbook(FileTypes fileType, String targetPath) {
		 if (fileType == null || sourcePath == null) {
		        return null; // Return null if the file type or source path is not set
		    }

		    try (
		        // Initialize the workbook in a try-with-resources block
		        Workbook workbook = createWorkbook(fileType)
		    ) {
		        if (workbook == null) {
		            return null; // Return null if the workbook cannot be created
		        }

		        // Add a new sheet
		        Sheet sheet = workbook.createSheet("Sheet1");

		        // Load the tasks (read from the source file)
		        List<String> tasks = load(sourcePath, fileType);
		        if (tasks == null || tasks.isEmpty()) {
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
		        return null; // Return null if something goes wrong
		    }
		}

		// Helper method to create the workbook based on file type
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
