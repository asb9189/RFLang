---
layout: default
title: Loops, Loops, and more Loops
nav_order: 5
---

# Repeat

It's about as simple as it gets but don't let simplicity infer futile.
Repeat statements are modern for loops without the verbosity.

For example

```
repeat 10 {
    print("RF")
}
```

```
Output:

RF
RF
RF
RF
RF
RF
RF
RF
RF
RF
```

is equivalent to

```
for (var i = 0; i < 10; i++) { ... }
```

```
Output:

RF
RF
RF
RF
RF
RF
RF
RF
RF
RF
```

when the local variable `i` is not referenced!

## Break
Almost forgot, we can terminate a loop at anytime by using the `break` keyword.

```
repeat 10 {
	print("RF")
	break
}
```

```
Output:

RF
```

# While

However, we don't *always* want to loop unconditionally. Instead, it would be
great if we could loop *WHILE* a condition is true.

```
let isWindyInRochester = true
while isWindyInRochester {
    ...
}

haveFun()
```

# For in

"For In" statements are used to iterate over iterable collections. Currently, 
RFLang only supports one iterable collection, `List`. Let's see it in action!

```
let list = List[1, 2, 3] @ 'List' constructor with arguments '1', '2', and '3'!
for number in list {
    print(number)
}
```

```
Output:

1
2
3
```


