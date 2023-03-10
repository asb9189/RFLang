---
layout: default
title: Functions
nav_order: 6
---

# Functions

Functions in RFLang look and act the same way in most imperative languages. Functions can be defined with parameters and optionally return values!

### Functions can be defined using the `fun` keyword

```
fun sayHello() {
    print("Hello!")
}
```

```
Output
```

Notice how we don't have any output? That's because we forgot to call our function! Functions can be called using parenthesis containing any arguments the function may require `(...)`.

```
sayHello()
fun sayHello() {
    print("Hello!")
}
```

```
Output

Hello!
```

### Functions can also receive and return values
Functions can be defined with parameters and return values using the `return` keyword.



```
fun add(x, y) {
    return x + y
}

print( add (2, 3) )
```

```
Output

5
```

# Recursive Functions

Believe it or not, recursive functions almost prevented me from
releasing this project. I had everything working except for recursive functions
and couldn't figure out what was causing the bug... After a little jog outside
with a GilbertRF we started talking about RFLang (somehow) and by the end of the run
I knew what needed to be done.

Later that day I went home and fixed the bug once and for all with a single line!
What's even crazier is that I originally had a branch for a "potential" solution that was
several hundred lines. Reluctant to continue working on it, I quit and took off months of working
on this project only to come back and "finish" it once and for all. Although, when is a programming
language really ever "finished".

I digress, let's explore the breathtaking fibonacci sequence in RFLang!


```
print(fib(20))
fun fib(n) {
    if (n == 0) or (n == 1) { return n }
    return fib(n - 1) + fib(n - 2)
}
```

```
Output

6765
```