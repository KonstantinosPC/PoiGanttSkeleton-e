package dom.gantt;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskAbstract implements Comparable<TaskAbstract>{
	private int taskId;
	private String taskText;
	private int containerTaskId;
	
	public abstract int getTaskStart();
	public abstract int getTaskEnd();
	public abstract double getCost();
	public abstract double getEffort();
	public abstract boolean isSimple();
	
	public abstract List<TaskAbstract> getSubtasks();
	
	public TaskAbstract(int taskId, String taskText, int containerTaskId) {
		super();
		this.taskId = taskId;
		this.taskText = taskText;
		this.containerTaskId = containerTaskId;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public String getTaskText() {
		return taskText;
	}
	
	public int getContainerTaskId() {
		return containerTaskId;
	}
	
	public int getDuration() {
		return getTaskEnd() - getTaskStart() + 1;
	}

	@Override
	public int compareTo(TaskAbstract t) {
		int startDiff = this.getTaskStart() - t.getTaskStart();
		if (startDiff != 0)
			return startDiff;
		return (this.taskId - t.getTaskId()); 
	}
	
	@Override
	public String toString() {
		String separator = "\t";
		return 	"TaskId: " + separator + taskId + separator +
				"Text: " + separator + taskText + separator +
				"Mama: " + separator + containerTaskId + separator +
				"Start: " + separator + getTaskStart() + separator +
				"End: " + separator + getTaskEnd() + separator +
				"Cost: " + separator + getCost() + separator +
				"Effort: " + separator + getEffort() 
				;
	}

	public String[] toStringArray() {
		List<String> elements = new ArrayList<String>();
		elements.add(Integer.toString(taskId));
		elements.add(taskText);
		elements.add(Integer.toString(containerTaskId));
		elements.add(Integer.toString(getTaskStart()));
		elements.add(Integer.toString(getTaskEnd()));
		elements.add(Double.toString(getCost()));
		elements.add(Double.toString(getEffort()));
		return elements.toArray(new String[elements.size()]);
	}
}//end class
