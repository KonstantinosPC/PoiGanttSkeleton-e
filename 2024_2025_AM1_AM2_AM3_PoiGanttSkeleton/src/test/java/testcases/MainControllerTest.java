package testcases;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dom.gantt.TaskAbstract;
import service.MainController;
import util.FileTypes;
import util.ProjectInfo;

public class MainControllerTest {
	
	private MainController testObject;
	private String sourcepath;
	private FileTypes filetype;
	private String targetPath;
	private ProjectInfo projectObject;
	
	@Before
	public void setUp(){
		testObject = new MainController();
		sourcepath = "C:/Users/User/Desktop/Gant-Final/2024_2025_AM1_AM2_AM3_PoiGanttSkeleton/src/test/resources/input/EggsScrambled.tsv";
		targetPath = "C:/Users/User/Desktop/Gant-Final/2024_2025_AM1_AM2_AM3_PoiGanttSkeleton/src/test/resources/output/EggsScrambled.xlsx";
		filetype = FileTypes.TSV;
	}

	@Test
	public final void testLoad() {
		List<String> test1 = testObject.load(sourcepath,filetype);
		for (String string : test1) {
			System.out.println("Stoixio listas : " + string);
		}
		System.out.println("\n");
	}

	@Test
	public final void testPrepareTargetWorkbook() {
		testObject.load(sourcepath,filetype);
		projectObject = testObject.prepareTargetWorkbook(FileTypes.XLS, targetPath);
		System.out.println(projectObject.toString());
		System.out.println("\n");
	}

	@Test
	public final void testGetAllTasks() {
		testObject.load(sourcepath,filetype);
		System.out.println("getAllTasks");
		for(TaskAbstract task:testObject.getAllTasks()) {
			System.out.println(task.toString());
		}
		System.out.println("\n");
	}

	@Test
	public final void testGetTopLevelTasksOnly() {
		testObject.load(sourcepath,filetype);
		System.out.println("getTopLevelTasksOnly");
		for(TaskAbstract task:testObject.getTopLevelTasksOnly()) {
			System.out.println(task.toString());
		}
		System.out.println("\n");
	}

	@Test
	public final void testGetTasksInRange() {
		testObject.load(sourcepath,filetype);
		System.out.println("getTasksInRange(100,300)");
		for(TaskAbstract task:testObject.getTasksInRange(100,300)) {
			System.out.println(task.toString());
		}
		System.out.println("\n");
	}

	@Test
	public final void testRawWriteToExcelFile() {
		testObject.load(sourcepath,filetype);
		System.out.println("test rawwrite for null");
		projectObject = testObject.prepareTargetWorkbook(FileTypes.XLS, targetPath);
		testObject.rawWriteToExcelFile(null);

		System.out.println("test rawwrite for AllTasks");
//		testObject.rawWriteToExcelFile(testObject.getAllTasks());

		System.out.println("test rawwrite for TopLevelTasksOnly");
//		testObject.rawWriteToExcelFile(testObject.getTopLevelTasksOnly());
	
		System.out.println("test rawwrite for TasksInRange(100,300)");
//		testObject.rawWriteToExcelFile(testObject.getTasksInRange(100,300));
	}
	
	@Test
	public final void testAddFontedStyle() {
		testObject.load(sourcepath, filetype);
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
	}


	@After
	public final void testCreateNewSheet() {
		testObject.load(sourcepath, filetype);
		projectObject = testObject.prepareTargetWorkbook(FileTypes.XLS, targetPath);
		testObject.rawWriteToExcelFile(testObject.getAllTasks());
		testObject.createNewSheet("File too", testObject.getAllTasks(), "Normal", "Normal", "Normal", "Normal", "Normal", "Normal");
	}
	

	
}
