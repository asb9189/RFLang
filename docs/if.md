---
layout: default
title: If, Else, and Elseif
nav_order: 4
---

# If statements

```
if true {
    print("true!")
}
```

```
Output:

true!
```

# If / Else statements

```
if false {
    print("true!")
} else {
    print("false :(")
}
```

```
Output:

false :(
```

# If / Else / Elseif statements

```
let x = "RF"

if x == "R" {
	print("R")
} elif x == "F" {
	print("F")
} else {
	print("RF")
}
```

```
Output:

RF
```

# *IF* the condition gets complex 
### { consider grouping expressions using parentheses }

```
let isRainingInRochester = true

if ((2 + 3) == 5) and isRainingInRochester {
	print("RF")
} else {
	print(":(")
}
```

```
Output:

RF
```