<h1>Have you gotten sick and tired of normal programming language syntax?</h1> 

Come on, looking at programming syntax for long periods of time is really dehumanizing. But programming is nice! Can there not be a way to release your inner anime protag while big-braining game development, website making, or maybe some neural networks? Well, no, because this sucks, but it's at least one way of not dying of too much `if`s and `else`s, `while`s and `for`s, and who knows? Maybe it will increase the number of weebs doing programming? 

So, this is my attempt at making a "Kawaii" esoteric programming language featuring... UwU and OwO faces. 
I know this is kind of silly, and it has been done (to varying degrees) before, and this project probably won't even be the very practical, but... I've got too much time to spare anyways. (I might look at this in the future with dissappointment)

**DISCLAIMER: This whole thing is meant as a joke.** But... you can use it for regular coding too if you really want to.

<h2>In case you want a feel of what it looks like, check out the demo folder! (MD code snippets is being weird so I'm not putting it here)</h2>

Currently status: Interpreter works for calculations with the following operations

Current features: Integers, floating-points, add, subtract, multiply, divide, parenthesis, and modular arithmatic operations. Variable assignment and use, as well as variable deletion. Powerful tools for functions, and a plethera (well, what soon will be a plethra) of built-in functions.

**Breif documentation**

Note: whitespace doesn't matter. Feel free to write everything in one line.

Might make a more beginner-friendly tutorial someday.

All statements have `!` at the end of them.

Comments starting with `.-.` can be put after any statement. They do nothing.

Numbers: ...just regular numbers

Arithmatic: `awdd`, `mwinws`, `mwltipwy`, and `diwide` are used. `mwd` is used for modular arithmatic. Alternatively, `+`, `-`, `*`, `/`, and `%` can be used but discouraged (they are not rly UwU eh)

Parenthesis: Circular parenthesis can be used as with regular math.

Variable declaration assignment: The following code shows a variable declaration and assignment operation:
`Numwer a iws 1!`

Variable names must start with a letter, and can include all capital or lowercase English letters, digits 0~9, and the special characters `^_?:>=<`. Snakecase is suggested, along with using UwU talk for variable names, obviously.

Variables are declared with the data type.

`Numwer`s are numbers, both integer and floating-point, which can be declared simply by typing the number, e.g. `53`.

`Stwing`s are Strings of text declared like this: `'str'`. `'\''`, `'\\'`, `'\n'`, and `'\t'` can be used to write apostrophes, backslashes, new lines, and tabs respectively.
`Stwing`s can be concatenated simply by putting them together, such as `'str' 'str'` resulting in `'strstr'`, and `'Stwing s iws 'hello '! s 'world'!` results in `'hello world!'`.

When functions are declared (see below), they are stored as `Fwnctwion` datatypes. They can be used similar to python, like below: 
`def useFunc(f):
  f()`
  
Translation (yes I do notice that it's a very verbose language, but when you're talking in UwU does the length still matter?): 
`OwO canDo useFunc UwU Fwnctwion f UwU canGibU nwthin!
  f UwU UwU!
  gibU nwthin!
 ^_^ewndCanDo`

`Lwist`s are mutable collections of objects. They are declared like this: `Lwist l iws [1, 'two', nwthin]!`
As you can see, there isn't any type restrictions, but I might add them in the future.

Cool operations can also be done with lists: for example...

Getting item from list: `Lwist l iws [1,2]! l[0]!` results in `1`

Slicing list: `l[n tw m]` returns the list from index n to index m. You can also not write one of them (e.g. `l[tw m]` will use 0 for n)

Appending lists and removing items to lists can be found in the API (see API documentation).

The assignment operator is `iws`.

Any expression that does not result in `nwthin` can be assgined to variables. (This may change in the future and `nwthin` will be allowed for variables)

`nwthin`: null type, returned by functions without return values.

Variables **cannot** be declared without assignment.

Variable assignmet returns the value, so things like `a iws b iws 1` result in `a` being 1 and `b` being 1.

There is no garbage collection, so use the `dewete` keyword to get rid of unused variables (or to reset them) yourself. `dewete` will also get rid of the variable type limitation, perfect for resetting variable types. 

*Note: **do not** delete functions; if so the interpreter will only remove the refrence to the function, but the function object will remain in the heap, and some weird bugs may arise. Plus, where have you seen a programming language that allows you to delete functions anyways*

Boolean: `:>` a happy face is used as TRUE, while `:<` is FALSE. Note that TRUE and FALSE values correspond to the `Numwer`s 1 and 0 respectively. Any other number greater than 0 will be considered TRUE, while any number lesser or equal to 0 will be considered FALSE.

AND: `a awnd b` returns `:>` if both a and b are TRUE.

OR: `a orw b` returns `:>` if one of a or b are TRUE.

XOR: `a xwr b` returns `:>` if a and b are different.

NOT: `nawt a` returns the opposite of a.

Comparisons: `eqwals`, `>_<`, `<_<`, `>=<`, and `<=<` are used for `==`, `>`, `<`, `>=`, and `<=` respectively. `!=` can be done with `nawt a eqwals b!`.

Operations or comparisons are evaluated kind of backwards. `:> xor :> xor :< xor :<` results in `:<` What does this mean? First, we remove the first few operation, and only look at the last operation: `:< xor :<` is `:<` as the two parameters are the samedifferent. Then we pass this operation to the operation to the right of it, `:> xor ...`, putting the result in the second position, so its `:> xor :<`, which results in `:>` as the two are different. Then we repeat the process by passing the result `:>` to the last operation, `:> xor ...`, resulting in `:> xor :>` which results in `:<`.

String concatenations are as easy as ever: simply put two Strings next to each other, and your favourite waifu/husbando will do everything for you!
Example: `'a' 'a'` (result `'aa'`) 
Example: `Stwing s iws 'a'! s 'a'!` (result `'aa'`)

If statements: `OwO *notices [expression]*?`
Will run the next lines of code if the expression returns true, or a number greater than zero, and will return null if the expression returns false, or a number lesser than zero.
If statements are ended by `^_^ewndNotice!`

Else if statements: `ewlse! OwO *notices [expression]*?` can be followed after `^_^ewndNotice` and will run if the previous if(s) are not run, and will return null if the previous if(s) are run.
There is no simple `ewlse!` statement (yet) so `ewlse! OwO *notices :>*?` should be used instead.

Switch statements: `OwO *notices [expression that results in integer]*~` can be used to start a switch statement. Cases are created with `~ eqwals [case number]!`, and then the expressions, followed by `^_^nwextCase!` to continue to the next case (to break the case, use `stawp!` before the `^_^nwextCase!`).

For loops: telling your waifu/husbando to do things over and over again is very easy! Simply use `do [x] times!`, followed by the code you want to run, followed by `^_^wepeatDat` to end the loop declaration! Note that `[x]` must evaluate to `<T extends Numwer>`. `stawp` can be used to ask your waifu/husbando to... stop the loop. `do [x] to [y] times!` can also be used to start at index `[x]` and loop until `[y]` is reached, for both `[x], [y]` evaluates to `<T extends Numwer>`. The index can be modified by using `inwex` as a variable within the loop.

While loops: more loops, more fun! Use `doWen OwO *notices [expression]*?` to loop when the expression evaluates to true. Similar to above `^_^wepeatDat` ends the loop declaration and `stawp` can be used to break the loop.

Functions: declare functions with `OwO canDo UwU [parameters] UwU canGibU [return value]!`! Parameters are formatted as followed: `[datatype1] [varname1], [datatype2] [varname2]...`, or there can be no parameters! The return value can give any datatype as well as `nwthin`.
Functions are followed by code, a `gibU` return statement (`gibU nwthin` functions), and `^_^ewndCanDo` to end the function.
Note: I haven't implemented variable scope, so have fun deleting variables declared inside functions.

Function overload is when the function has the same name but takes different parameters. Overloaded functions must be preceded by the line `owerlwoad!`.

Object-oriented programming: OOP is now here! Your classes can have any code previously described, and you can assign objects to variables like other data types. Classes are declared with `cwass [name]` and ended with `^_^ewndCwass`. You can tell an object instance to do anything with the command "`do`", such as calling a method, updating or getting a variable, etc.

Inheritance: Type `cwass [name] extwends [parent]` to extend another class! To override functions simply define the function normally.

Constructors: Constructors are necessary in every object (until I write the auto-add constructor feature, that is). Each constructor starts with `hewesHwwUMake`, with any parameters similar to those of functions, or no parameters if there are none.
The next lines contain code for construction, and it must end with `finishMaking!` to tell the interpreter to finish the construction of the object.
Constructors are invoked by the command `make [class]` or `make [class] UwU [param] UwU`.
