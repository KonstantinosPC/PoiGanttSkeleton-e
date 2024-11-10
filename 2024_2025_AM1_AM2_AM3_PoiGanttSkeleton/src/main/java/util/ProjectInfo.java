package util;

public class ProjectInfo {
	private String projectName;
	private String sourceFileName;
	private String targetFileName;
	private int totalNumTasks;
	private int totalTopTasks;
	
	public ProjectInfo(String projectName, String sourceFileName, String targetFileName, int totalNumTasks,
			int totalTopTasks) {
		super();
		this.projectName = projectName;
		this.sourceFileName = sourceFileName;
		this.targetFileName = targetFileName;
		this.totalNumTasks = totalNumTasks;
		this.totalTopTasks = totalTopTasks;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public String getTargetFileName() {
		return targetFileName;
	}

	public int getTotalNumTasks() {
		return totalNumTasks;
	}

	public int getTotalTopTasks() {
		return totalTopTasks;
	}

	@Override
	public String toString() {
		return "ProjectInfo [projectName=" + projectName + "\n sourceFileName=" + sourceFileName + "\n targetFileName="
				+ targetFileName + "\n totalNumTasks=" + totalNumTasks + "\n totalTopTasks=" + totalTopTasks + "]";
	}
	
	
	
}
