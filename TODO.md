Todo:
* Allow updating variables (e.g. +=, ++. etc)

Bugs:
* Single lines of code with multiple `!`s do not work
* The line number in errors is often not correct as the line incrementer of Position class is weird
* Else if statements will always be the opposite of the last statement even if a statement in the if cluster has already been run
