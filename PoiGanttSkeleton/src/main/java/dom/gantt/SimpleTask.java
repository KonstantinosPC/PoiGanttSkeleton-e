package dom.gantt;

import java.util.List;

public class SimpleTask extends TaskAbstract {

    private int taskstart;
    private int taskend;
    private int cost;
    private int effort;

    public SimpleTask(int taskId, String taskText, int containerTaskId,int Start,int end, int cost,int effort) {
		super(taskId, taskText, containerTaskId);
        this.taskstart = Start;
		this.taskend = end;
		this.cost = cost;
        this.effort = effort;
	}
    
    @Override
    public int getTaskStart() {
        return taskstart;
    }

    @Override
    public int getTaskEnd() {
        return taskend;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public double getEffort() {
        return effort;
    }

    @Override
    public boolean isSimple() {
        if(this.getContainerTaskId()==0){
            return false;
        }else{
            return true;
        }
    }


    //TODO make getSubtasks method
    @Override
    public List<TaskAbstract> getSubtasks() {
        if(isSimple()){
            return null;
        }else{
            return null;
        }
    }

}
