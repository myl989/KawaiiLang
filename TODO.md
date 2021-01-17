Todo:
* Handle multi-line statements
* Using functions as variables (i.e. `def useFunc(Func f): return f()`) as well as having functions avaliable as return values.
* Type classes (get type of variable)

Bugs (high priority):
N/A

Bugs (low priority):
* The line number in errors is often not correct as the line incrementer of Position class is weird
* Null pointers hangs the program
* Auto-delete variables outside scope perhaps...?
* Incompatable variable type declaration and contents are not detected until variable is used
