# PoiGanttSkeleton
This was an exercise for the class of "Software Development". In this class we developed an app the creates an **excel** file that's based on a file that you import. The given file type has to be:
* TSV
* CSV
* CSV_EU
* XLSX
* XLS

The inside of the file has to be in this perticular form so the program can work with no errors.
Example:

| **ID** | **TASK** | **MAMA-ID** | **START** |  **END** | **COST** | **EFFORT** |
| ----------- | ----------- | ------------ | ------------- | ---------- | ---------- | ----------- |
| 101 | Take the Trash out | 100 | 1 | 2 | 0 | 10 |
| 100 | House Chores | | | | |
| 102 | Clean the Dishes | 100 | 2 | 4 | 2 | 7 |

The task **100** is a Mama-Task

The final form of the excel is gonna look like this:
| **LEVEL** | **ID** | **DESCRIPTION** | **COST** | **EFFORT** | **1** | **2** | **3** | **4** |
| --------- | ------ | --------------- | -------- | ---------- | ----- | ----- | ----- | ------ |
| TOP | 100 | House Chore | ||||
| | 101 | Take the Trash out | 0 | 10 | X | X |
| | 102 | Clean the Dishes | 2 | 7 | | X | X | X |

There are gonna be two types of exports which are in the same file but different sheet. Depending on the user. 
    

* The one is the example above which is represented by the "X"

* The other one that instead of putting "X" it puts color to the perticular cell. The user can also select the style not only for the "X" but for the whole sheet through a method called **addFontedStyle**

For the user to create a new Sheet he can use the method **createNewSheet** which the user can select the filter that the user wants.

