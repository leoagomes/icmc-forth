# icmc-forth

This project is a toyish compiler for a toyish language that generates
assembly for the ICMC Processor Architecture, the architecture students
create and use throughout some courses at ICMC-USP. More Info about the
architecture can be found [here](https://github.com/simoesusp/Processador-ICMC).

## Usage

Build it. (It is an Idea project at the moment and so I believe you need
Intellij Idea to build it, but it should generate a pretty jar.)

Pass it a source file, a destination file name and the directories from
which to fetch both `libif` and the `#import`s used. It should generate
an icmc-assembly file that can be assembled using the tools at its
github page and run in a simulator (also provided on that page).

If you have an Altera DE2-115 (or DE2-70) you can also generate a core
and use that.

In the `/testing/` directory you can find some example files used for
testing the compiler.

## License

All code in this repository (unless explicitly stated otherwise) is
provided under the terms of the license available at `LICENSE`.

## The Language

Even though the name of this repository includes the word 'forth',
the language implemented isn't really forth, but inspired by forth.

It is a concatenative language that operates on stacks, the data
stack and the return stack. All functions and operations happen
on these stacks.

### The Stacks

There are two stacks.

The data stack is the one most operations take their operands from
and push their results to. It is used for data.

The return stack can also hold arbitrary data, but it should be used
carefully. The compiler may also use the return stack for instruction
pointers used in loops and other control flow operations. This means
that a piece of data in the return stack at the wrong time may make
the program go haywire.

### Literals

Only integers, strings and chars are supported, mostly because the
icmc processor only supports 16-bit integers. Integer literals are
as expected: any number of digits or hex literals (like `0xFF`).
String literals are more like C instead of forth: `" some string "`
would include the space after the `"`, so if you want just the
text `some string`, you'd need the string `"some string"`. Character
literals also follow the C model, so the character `c` would be `'c'`.

## Comments

The compiler will ignore everything between a `\` token and a newline
character (`\n`). It will also ignore everything from a `(` token
until it finds a `)` token. The word "token" is important because it
means that comment characters must be separated from the comments.
Examples:

```forth
( valid comment )
\ also a valid comment

(not a valid comment)
\also not valid comment
```

### Function Definitions

Function definitions follow the pattern:

```forth
: function-name
	( function body ) ;
```

Since everything shares the same data and return stacks, there
is no "returning" from functions, to "return" data from functions
just leave it on the stack.

```forth
: returns-five
	5 ;
```

## Examples `// TODO`

```forth
```
