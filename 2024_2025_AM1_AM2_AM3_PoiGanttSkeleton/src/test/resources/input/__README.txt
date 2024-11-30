The following structure for input data files is assumed
--------------------------------------------------------------------------
Column A:	int taskId 
Column B:	String taskText
Column C:	int containerId 			//0, if top-level task; mamaId, if nonTopLevel

The rest of the columns are empty for top-level tasks and filled for non-top-level
Column D:	int start 
Column E:		int end 
Column F:		double cost 
Column G:	double effort 
