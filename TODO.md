Todo:
* Allow updating variables (e.g. +=, ++. etc)

Bugs:
* Single lines of code with multiple `!`s do not work
* The line number in errors is often not correct as the line incrementer of Position class is weird
* Some funky stuff is going on with nested if statements
* Variable assignments within false if statements will assign the value as `null`