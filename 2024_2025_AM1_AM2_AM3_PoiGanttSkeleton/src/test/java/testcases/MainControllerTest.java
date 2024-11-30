package testcases;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
		sourcepath = "C:/Users/User/Desktop/tempor/2024_2025_AM1_AM2_AM3_PoiGanttSkeleton/src/test/resources/input/Eggs.tsv";
		targetPath = "C:/Users/User/Desktop/tempor/2024_2025_AM1_AM2_AM3_PoiGanttSkeleton/src/test/resources/output/Eggs.xlsx";
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
		fail("Not yet implemented");
	}

	@Test
	public final void testGetTopLevelTasksOnly() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetTasksInRange() {
		fail("Not yet implemented");
	}

	@Test
	public final void testRawWriteToExcelFile() {
		fail("Not yet implemented");
	}

	@Test
	public final void testAddFontedStyle() {
		fail("Not yet implemented");
	}

	@Test
	public final void testCreateNewSheet() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetFileType() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetFileType() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetSourcePath() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetSourcePath() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetloadedTasks() {
		fail("Not yet implemented");
	}

	@Test
	public final void testSetloadedTasks() {
		fail("Not yet implemented");
	}

	@Test
	public final void testTaskNumbering() {
		fail("Not yet implemented");
	}

}
