import java.util.ArrayList;

import MainController;
import dom.gantt.TaskAbstract;
import service.MainController;

public class MainControllerTest {
    
    MainController mainobj = new MainController();
	
    public List<String> testlist = new Arrays.asList(
        "501\tHire Photographer\t500\t36\t38\t7\t3",
        "100\tPlan Event\t0",
        "301\tArrange Flowers\t300\t19\t22\t5\t2",
        "500\tOrganize Catering\t0",
        "101\tBook Venue\t100\t2\t4\t8\t3",
        "203\tBake Dessert\t200\t16\t18\t8\t3",
        "300\tSet Up Decorations\t0",
        "102\tSend Invitations\t100\t5\t6\t6\t2",
        "202\tCook Main Course\t200\t11\t15\t12\t5",
        "302\tInstall Lighting\t300\t23\t25\t7\t3",
        "601\tConfirm Timings\t600\t42\t44\t5\t2",
        "200\tPrepare Food\t0",
        "201\tPrepare Appetizers\t200\t8\t10\t10\t4",
        "502\tSet Up Photo Booth\t500\t39\t41\t6\t2",
        "400\tPlan Entertainment\t0",
        "303\tSet Up Audio Equipment\t300\t26\t28\t10\t4",
        "401\tBook Performers\t400\t32\t32\t9\t4",
        "600\tCoordinate Event Schedule\t0",
        "402\tOrganize Activities\t400\t31\t35\t6\t3",
        "602\tDistribute Agenda\t600\t45\t46\t4\t2");

    if(mainobj.setloadedTasks(testlist)){
        for (TaskAbstract testTask : mainobj.getAllTasks()) {
            testTask.toString();        
        }
    }

    if(mainobj.setloadedTasks(testlist)){
        for (TaskAbstract testTask : mainobj.getTasksInRange(100,300)) {
            testTask.toString();        
        }
    }

    if(mainobj.setloadedTasks(testlist)){
        for (TaskAbstract testTask : mainobj.getTopLevelTasksOnly()) {
            testTask.toString();        
        }
    }



}
