****
Review

App made by Tristan Hoobroeckx

Review done by Tobias Garritsen
****
Overal Grades based on code review
****

**names:** 4

**headers:** 1

**comments:** 3

**layout:**	4

**formatting:**	4

**flow:** 3

**idiom:** 4, but I don't know for sure what they 
mean, but I think only importing things you need and using 
them to the fullest. 

**expressions:** 4

**decomposition:** 4

**modularization:** 4

****

Extra comments about possible improvements

****

**(headers)** Add header comments that summarize the purpose of the entire class/activity.

**(comments)** Add comments to the methods and not in at the part where the method is called (*MainActivity* line 87-90). In that case you don't have to repeat your comments at line 144-147.

**(flow)** The method createUser and emailPassword are very similar, almost identical. A part of the code could be put in one method that can be called from the *createUser* and *emailPassword* methods.

**(comments)** Your comment in MainActivity on line 113 and 170 is *"Set Displayname"*, for a method that is literally called *setDisplayName()*. Kinda redundant.

**(names)** Your *FloatingActionButton* is called *fab*, pretty logical, however in line 60,61 and 64,65 you use *redfab* and *bluefab* as id names. I had to think for a second what it was again. This is very nitpicking, but I need ten points.

**(comments)** CreateFireListener has only 2 lines of quite obvious comments. I think it is exactly the code from Firebase itself, but a few more lines that either explain the whole thing or a few parts would be nice for someone who hasn't worked with Firebase yet.

**(flow)** The MainActivity has line 124 in which multiple tasks in a single line ".getText().toString().equals("")" These are three actions. It might be prettier to first create the string and afterwards check if it is equal to nothing in the for loop.
