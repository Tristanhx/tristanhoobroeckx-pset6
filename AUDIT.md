#BetterCodeHub results

![Better Code Hub Results](https://github.com/Tristanhx/tristanhoobroeckx-pset6/blob/master/doc/BetterCodeHubDebate.png?raw=true "Better Code Hub results")



I forgot to make a task list, but the the guidelines that weren't quite right (and maybe still aren't) were according
to BCH: Write Short Units of Code, Write Simple Units of Code, Write Code Once, and Keep Architecture Components
Balanced. Most of the affected code was in my Activities. I then decided to split some methods into two and have the 
first call the second. This eventually resulted in code that was in compliance with the guideline 'Write Short Units of
Code', and 'Write Simple Units of Code'. 'Write Code Once' wasn't checked, because BCH complained about repeating 
Activity life-cycle components across multiple Activities. The components involved were onStart() and onStop().
'Keep Architecture Components Balanced' wasn't fulfilled, because all my code resides in one component called 'app'.
This guideline calls for at least two components that are of equal size. 