package service;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dom.gantt.TaskAbstract;
import service.MainController;
import util.FileTypes;
import util.ProjectInfo;
import static org.junit.Assert.*;

public class MainControllerTest {
	
	private MainController testObject;
	private String sourcepath;
	private FileTypes filetype;
	private String targetPath;
	private ProjectInfo projectObject;
	
	@Before
	public void setUp(){
		testObject = new MainController();
		sourcepath = "C:/Users/Spiros/Desktop/Gant-Final/2024_2025_AM1_AM2_AM3_PoiGanttSkeleton/src/test/resources/input/EggsScrambled.tsv";
		targetPath = "C:/Users/Spiros/Desktop/Gant-Final/2024_2025_AM1_AM2_AM3_PoiGanttSkeleton/src/test/resources/output/EggsScrambled.xlsx";
		filetype = FileTypes.TSV;
		testObject.load(sourcepath,filetype);
	}

	@Test
	public final void testLoad() {
		List<String> test1 = testObject.load(sourcepath,filetype);
		int expected = 14;
		int actual = test1.size();
		assertEquals("test the expected number of tasks", expected, actual);
		System.out.println("\n");
	}

	@Test
	public final void testPrepareTargetWorkbook() {
		projectObject = testObject.prepareTargetWorkbook(FileTypes.XLS, targetPath);
		int expected = 3;
		int actual = projectObject.getTotalTopTasks();
		assertEquals("test the expected number of toptasks",expected,actual);
		System.out.println("\n");
	
	}

	@Test
	public final void testGetAllTasks() {
		List<TaskAbstract> testlist = testObject.getAllTasks();
		int expected = 14;
		int actual = testlist.size();
		assertEquals("test the expected number of tasks",expected,actual);
		System.out.println("\n");
	
	}

	@Test
	public final void testGetTopLevelTasksOnly() {
		List<TaskAbstract> testlist = testObject.getTopLevelTasksOnly();
		int expected = 3;
		int actual = testlist.size();
		assertEquals("test the expected number of toptasks",expected,actual);
		System.out.println("\n");
	
	}

	@Test
	public final void testGetTasksInRange() {
		List<TaskAbstract> testlist = testObject.getTasksInRange(101, 300);
		int expected = 10;
		int actual = testlist.size();
		assertEquals("test the expected number of tasks in range 101 to 300",expected,actual);
		System.out.println("\n");
	
	}

	@Test
	public final void testRawWriteToExcelFile() {
		projectObject = testObject.prepareTargetWorkbook(FileTypes.XLS, targetPath);
		boolean expected_for_null = false;
		boolean actual_for_null = testObject.rawWriteToExcelFile(null);
		boolean expected_for_AllTasks = true;
		boolean actual_for_AllTasks = testObject.rawWriteToExcelFile(testObject.getAllTasks());
		boolean expected_for_TopTasks = true;
		boolean actual_for_TopTasks = testObject.rawWriteToExcelFile(testObject.getTopLevelTasksOnly());
		assertEquals("test if rawWriteToExcelFile is false when arguments are null",expected_for_null,actual_for_null);
		assertEquals("test if rawWriteToExcelFile works for all tasks",expected_for_AllTasks,actual_for_AllTasks);
		assertEquals("test if rawWriteToExcelFile works for top tasks",expected_for_TopTasks,actual_for_TopTasks);
		System.out.println("\n");
	}
	
	@Test
	public final void testAddFontedStyle() {
		projectObject = testObject.prepareTargetWorkbook(FileTypes.XLS, targetPath);
		testObject.rawWriteToExcelFile(testObject.getAllTasks());
		String barStyleName = testObject.addFontedStyle(
			    "TopTask_bar_style",
			    IndexedColors.WHITE.getIndex(),
			    (short) 10,
			    "Times New Roman",
			    false,
			    false,
			    false,
			    IndexedColors.BLUE.getIndex(),
			    "solid",
			    "left",
			    false
			);
		String expected = "TopTask_bar_style";
		String actual = barStyleName;
		assertEquals("test if AddFontedStyle returns the correct string",expected,actual);
		System.out.println("\n");
	}

	@After
	public final void testCreateNewSheet() {
		projectObject = testObject.prepareTargetWorkbook(FileTypes.XLS, targetPath);
		testObject.rawWriteToExcelFile(testObject.getAllTasks());
		boolean expected = true;
		boolean actual = testObject.createNewSheet("File too", testObject.getAllTasks(), "DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal");
		assertEquals("test if CreateNewSheet works for all tasks",expected,actual);
		System.out.println("\n");
	}
}
