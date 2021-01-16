<kbd><img src="https://i.imgur.com/4G8WuHB.png" /></kbd>

<h1>Have you gotten sick and tired of normal programming language syntax? Have you ever wanted to code like your favourite anime girl or guy?</h1> 

Come on, looking at programming syntax for long periods of time is really dehumanizing. But programming is nice! Can there not be a way to release your inner anime protag while big-braining game development, website making, or maybe some neural networks? Well, no, because KawaiiLang sucks, but it's at least one way of not dying of too much `if`s and `else`s, `while`s and `for`s, and who knows? Maybe it will increase the number of weebs doing programming? 

(KawaiiLang is probably gonna be useless, but screw it I'm making this anyways.) 

So, this is my attempt at making a "Kawaii" esoteric programming language featuring... UwU and OwO faces. 
I know this is kind of silly, and it has been done (to varying degrees) before, and this project probably won't even be the very practical, but... I've got too much time to spare anyways. (I might look at this in the future with dissappointment)

**DISCLAIMER: This whole thing is meant as a joke.** But... you can use it for regular coding too if you really want to.

Currently status: Interpreter works for calculations with the following operations

Current features: Integers, floating-points, add, subtract, multiply, divide, parenthesis, and modular arithmatic operations. Variable assignment and use, as well as variable deletion.

Java (for PC, Linux, and Android) and Swift/Objective-C (for iOS) implementations of KawaiiLang are both being developed.

**Breif documentation**

Might make a more beginner-friendly tutorial someday.

All statements have `!` at the end of them.

Numbers: ...just regular numbers

Arithmatic: `awdd`, `mwinws`, `mwltipwy`, and `diwide` are used. `mwd` is used for modular arithmatic. Alternatively, `+`, `-`, `*`, `/`, and `%` can be used but discouraged (they are not rly UwU eh)

Parenthesis: Circular parenthesis can be used as with regular math.

Variable declaration assignment: The following code shows a variable declaration and assignment operation:
`Numwer a iws 1!`

Variable names must start with a letter, and can include all capital or lowercase English letters, digits 0~9, and the special characters `&^_?:>=<`. Snakecase is suggested, along with using UwU talk for variable names.

Variables are declared with the data type, the current only one being `Numwer` (able to handle both integer and floating-point inputs).

The assignment operator is `iws`.

Any expression that does not result in `nwthin` can be assgined to variables. (This may change in the future and `nwthin` will be allowed for variables)

`nwthin`: null type, returned by functions without return values.

Variables **cannot** be declared without assignment.

Variable assignmet returns the value, so things like `a iws b iws 1` result in `a` being 1 and `b` being 1.

There is no garbage collection, so use the `dewete` keyword to get rid of unused variables (or to reset them) yourself. `dewete` will also get rid of the variable type limitation, perfect for resetting variable types.

Boolean: `:>` a happy face is used as TRUE, while `:<` is FALSE. Note that TRUE and FALSE values correspond to the `Numwer`s 1 and 0 respectively. Any other number greater than 0 will be considered TRUE, while any number lesser or equal to 0 will be considered FALSE.

AND: `a awnd b` returns `:>` if both a and b are TRUE.

OR: `a orw b` returns `:>` if one of a or b are TRUE.

XOR: `a xwr b` returns `:>` if a and b are different.

NOT: `nawt a` returns the opposite of a.

Comparisons: `eqwals`, `>_<`, `<_<`, `>=<`, and `<=<` are used for `==`, `>`, `<`, `>=`, and `<=` respectively. `!=` can be done with `nawt a eqwals b!`.

Operations or comparisons are evaluated kind of backwards. `:> xor :> xor :< xor :<` results in `:<` What does this mean? First, we remove the first few operation, and only look at the last operation: `:< xor :<` is `:<` as the two parameters are the samedifferent. Then we pass this operation to the operation to the right of it, `:> xor ...`, putting the result in the second position, so its `:> xor :<`, which results in `:>` as the two are different. Then we repeat the process by passing the result `:>` to the last operation, `:> xor ...`, resulting in `:> xor :>` which results in `:<`.

If statements: `OwO *notices [expression]*?`
Will run the next lines of code if the expression returns true, or a number greater than zero, and will return null if the expression returns false, or a number lesser than zero.
If statements are ended by `^_^ewndNotice!`

Else if statements: `ewlse! OwO *notices [expression]*?` can be followed after `^_^ewndNotice` and will run if the previous if(s) are not run, and will return null if the previous if(s) are run.
There is no simple `ewlse!` statement (yet) so `ewlse! OwO *notices :>*?` should be used instead.

For loops: telling your waifu/husbando to do things over and over again is very easy! Simply use `do [x] times!`, followed by the code you want to run, followed by `^_^wepeatDat` to end the loop declaration! Note that `[x]` must evaluate to `<T extends Numwer>`. `stawp` can be used to ask your waifu/husbando to... stop the loop. `do [x] to [y] times!` can also be used to start at index `[x]` and loop until `[y]` is reached, for both `[x], [y]` evaluates to `<T extends Numwer>`. The index can be modified by using `inwex` as a variable within the loop.

While loops: more loops, more fun! Use `doWen OwO *notices [expression]*?` to loop when the expression evaluates to true. Similar to above `^_^wepeatDat` ends the loop declaration and `stawp` can be used to break the loop.
