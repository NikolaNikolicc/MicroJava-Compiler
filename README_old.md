# MicroJava Compiler

A compiler for the **MicroJava** programming language, developed as part of the *Compiler Construction* course at the **School of Electrical Engineering, University of Belgrade**.

---

## 📌 Project Overview

This compiler translates MicroJava source code into bytecode for the **MicroJava Virtual Machine (MJVM)**.\
MicroJava is a simplified, Java-like language with the following features:

- **Basic types**: `int`, `char`, `bool`
- **Reference types**: arrays, classes, interfaces, sets
- **Object-oriented**: inheritance, polymorphism, method overriding
- **Control structures**: `if-else`, `do-while`
- **Standard methods**: `print`, `read`, `ord`, `chr`, `len`, `add`, `addAll`

---

## ✨ Key Features

### Language Features

- Static fields and methods
- Class inheritance and interface implementation
- Method overloading and overriding
- Arrays and sets of integers
- Virtual Method Tables (VMT) for polymorphism
- Basic input/output operations

### Compiler Components

- Lexical analyzer (JFlex)
- Parser (CUP)
- Semantic analyzer
- Code generator
- Symbol table management

### Implementation Details

- Handles context-sensitive rules
- Type checking and compatibility
- Scope and lifetime management
- Virtual method table generation
- Bytecode optimization

---

## 🔧 Build Instructions

### Prerequisites

- Java Development Kit (JDK)
- Apache Ant

### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/your-repo/microjava-compiler.git
   cd microjava-compiler
   ```

2. Compile the project:

   ```bash
   ant compile
   ```

This will:

- Generate the lexer and parser
- Compile all Java sources
- Create the compiler executable

---

## ▶️ Usage

To compile a MicroJava program, open the `Compiler.java` file and set the desired `.mj` file path directly in the source code (e.g. inside the `main` method):

```java
File sourceCode = new File(<"src/test/microjava/Example.mj">);
```

Then, run the compiler:
```
java rs.ac.bg.etf.pp1.Compiler
```

After compiling the source file, it will generate:

- `program.obj` — The compiled bytecode
- Log files with compilation details

To execute the compiled program, run:

```bash
ant run
```

This will execute the `runObj` target defined in `build.xml`, which in turn:

- Disassembles the `.obj` file (bytecode)
- Optionally runs it in debug mode (via `debugObj`)
- Executes the final program using the MicroJava runtime

---

## 📄 Example Program

```java
program p
        
const int tableSize = 10;
set s1;
class Table {
   int pos[], neg[], factor;
   {
      void setfactor(int factor) {this.factor = factor;}
      void putp (int a, int idx) { this.pos[idx] = a; }
      void putn (int a, int idx) { this.neg[idx] = a; }
      int getp (int idx) { return pos[idx]; }
      int getn (int idx) { return neg[idx]; }
   }
}
Table val;
int rows, columns;
{
    void f(char ch, int a, int arg) int x;
    {
        x = arg;
    }
   void main() int x, i; char c; int arr[];
   {
      val = new Table();
      val.setfactor(2);
      s1 = new set[5];
      add(s1, 5); add(s1, 10);
      print(s1);
      arr = new int[3];
      i = 0;
      do
        arr[i] = i;
      while ( i<3, i++);
      i = 0;
      do
        print(arr[i]);
      while ( i<3, i++);
      val.pos = new int [tableSize];
      val.neg = new int [tableSize];
      read(x);
      i = 0;
      do{
        val.putp(0, i);
        val.putn(0, i);
      } while (i < tableSize, i++);
      f(c, x, i);
      read(rows);
      x=rows;
      do{
        if(x <= 0) break;
        if (0 <= x && x < tableSize)
        {
        val.putp(val.getp(x)+1);
        }
        else if (‐tableSize < x && x < 0)
        {
        val.putn(val.getn(‐x)+1);
        }
        read(x);
      }while();
   }
}
```

---

## ⚙️ Technical Details

### Virtual Machine Architecture

- Code area (bytecode instructions)
- Static data area (global variables)
- Heap (dynamic memory allocations)
- Procedure stack (activation records)
- Expression stack (operand storage)

### Bytecode Features

- 58 instruction types
- Object creation and manipulation
- Array operations
- Static and virtual method calls
- Arithmetic and logical operations

---

## ⚠️ Limitations

- Max 256 local variables per method
- Max 65536 global variables
- Max 65536 class fields
- Source code size limited to 8KB

---

## 🔍 Testing

The compiler includes tests for:

- Lexical analysis
- Syntax parsing
- Semantic analysis
- Code generation

To run tests:

```bash
ant runObj
```

---

## 📚 License

This project is developed for educational purposes as part of the *Compiler Construction* course at the **School of Electrical Engineering, University of Belgrade**.

---

## 👤 Contributors

- Nikola Nikolić

