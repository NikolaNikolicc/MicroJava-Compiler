# 🧩 MicroJava Modular Compiler

A modernized **MicroJava compiler and virtual machine**, developed as part of the *Compiler Construction* course at the **School of Electrical Engineering, University of Belgrade**.

This project extends the traditional educational MicroJava compiler with **support for modular programming**, enabling compilation and execution of programs distributed across multiple source files.

---

## 📘 Overview

**MicroJava** is a simplified, statically typed, object-oriented language designed for educational purposes.  
This compiler translates `.mj` source files into bytecode executable by the **MicroJava Virtual Machine (MJVM)**.

### 🧠 Key Characteristics

- **Statically typed** and single-pass compiler  
- **Object-oriented model**: classes, inheritance, interfaces, method overriding  
- **Modular system** with `import` directive and multi-file compilation  
- **Runtime virtual machine** (`mj-runtime`) with modular context switching  
- **Backward compatible** with previous single-file MicroJava versions  

---

## 🧩 Language Features

| Category | Supported Features |
|-----------|--------------------|
| **Primitive types** | `int`, `char`, `bool` |
| **Reference types** | arrays, classes, interfaces, sets |
| **Control flow** | `if`, `else`, `do-while`, `break`, `return` |
| **Object-oriented** | inheritance, polymorphism, dynamic dispatch |
| **Functions & variables** | static fields/methods, constants |
| **Modularity** | explicit `import` directive (selective or wildcard imports) |
| **Built-in methods** | `print`, `read`, `len`, `ord`, `chr`, `add`, `addAll` |

---

## ⚙️ Architecture

The compiler follows the classical structure of a modern compiler:

| Phase | Description | Tool |
|-------|--------------|------|
| **Lexical analysis** | Tokenization of source code | [JFlex](https://jflex.de) |
| **Syntax analysis** | Grammar parsing and AST construction | [CUP](http://www2.cs.tum.edu/projects/cup/) |
| **Semantic analysis** | Type checking, scope resolution, module linking | Custom logic |
| **Code generation** | Bytecode emission for MJVM | Custom backend |
| **Runtime execution** | Execution of `.obj` files | MicroJava Virtual Machine |

The modular extension introduces:
- **ModuleHandler** and **ContextHandler** classes for managing modules and execution contexts.  
- **Extended .obj file format** with module metadata.  
- **New bytecode instruction** `moduleswitch` for context switching between modules.

---

## 🗂️ Project Structure

```
MicroJava-Compiler/
├── src/
│   ├── lexical_analysis/               # JFlex scanner
│   ├── syntax_analysis/                # CUP parser
│   ├── semantic_analysis/              # Semantic analysis
│   ├── code_generation/                # Bytecode generation
│   ├── util/                           # Utility classes
│   ├── autorun/CompilerService.java    # Compiler entry point
│   └── autorun/CompilerAutorun.java    # Main launcher
└── scripts/
    ├── rebuild_microjava_compiler.ps1
    └── run_microjava_compiler.ps1

MicroJava-Libraries/
├── symboltable/             # Frontend symbol management
├── mj-runtime/              # Runtime + MJVM
└── scripts/
    └── buildJars.ps1
```

---

## 🧱 Build & Run

### 🪛 Prerequisites
- **Java Development Kit (JDK 8+)**
- **PowerShell (Windows or Linux via pwsh)**

### 🏗️ Building the Compiler

From the project root:

```bash
cd scripts
./rebuild_microjava_compiler.ps1
```

This script:
- Recompiles all `.java` files
- Regenerates the `.jar` compiler executable
- Cleans the `bin/` directory

---

## 🚀 Running from Release

If you do not wish to rebuild the compiler manually, you can use the **precompiled release version** available on GitHub:  
👉 [**MicroJava Compiler v2.0 – GitHub Release**](https://github.com/NikolaNikolicc/MicroJava-Compiler/releases/tag/v2.0)

### Steps

1. Download the file:  
   `mjcompiler-v2.0.jar` from the release page above.  

2. Place it anywhere on your system, for example:  
   `MicroJava-Compiler/bin/mjcompiler.jar`  

3. Open PowerShell (or `pwsh` on Linux/Mac) and navigate to the compiler directory:

   ```powershell
   Set-Location -Path (Join-Path $PSScriptRoot "..")
   ```

4. Run the compiler with the same arguments used by the PowerShell launcher script:

   ```powershell
   java -jar "bin/mjcompiler.jar" `
       --program "test/official_tests/test303.mj" `
       --input "src/rs/ac/bg/etf/pp1/code_generation/input/input.txt" `
       --output "src/rs/ac/bg/etf/pp1/code_generation/output" `
       --build --run
   ```

5. Optionally, you can replace arguments manually:  

   | Argument | Description |
   |-----------|-------------|
   | `--program <path>` | Path to `.mj` source file |
   | `--input <path>` | Optional path to `.txt` input file |
   | `--output <path>` | Output directory for `.obj` and logs |
   | `--build` | Compile program |
   | `--disasm` | Generate disassembly file |
   | `--debug` | Start runtime in debug mode |
   | `--run` | Execute compiled program |

6. Example run with all options:

   ```powershell
   java -jar "bin/mjcompiler.jar" --program "examples/main.mj" --output "out" --build --disasm --debug --run
   ```

After execution, the compiler will produce:
```
program.obj       # Compiled bytecode
logs/             # Compiler logs
```

---

### ▶️ Running the Compiler

To compile and optionally execute a MicroJava program:

```bash
cd scripts
./run_microjava_compiler.ps1
```

You will be prompted to choose whether to:
- Use **default paths and options** (`--build`, `--disasm`, `--debug`, `--run`), or  
- Enter custom source and output paths manually.

The compiler will:
- Translate one or more `.mj` files into `.obj` bytecode
- Disassemble and execute them via the MJVM runtime

Example output files:
```
program.obj       # Compiled bytecode
logs/             # Compilation logs
```

---

## 🧮 Example Usage

**Source file (`Main.mj`):**
```java
program Main

import utils.*;

{
   void main() {
      print("Hello from modular MicroJava!");
   }
}
```

**Utility file (`utils.mj`):**
```java
program utils 
{
   void helper() {
      print("Helper module loaded.");
   }
}
```

---

## 💡 Extended Example

Below is a more complex MicroJava example demonstrating **classes, inheritance, interfaces, sets, arrays**, and **modular imports**:

```java
program Example

import mathlib.*;
import utils.*;

const int SIZE = 5;
set s1;

interface Printable {
    void printAll();
}

class Table extends Printable {
    int data[];
    void init(int n) {
        data = new int[n];
        int i = 0;
        do {
            data[i] = i * i;
        } while (i < n, i++);
    }
    void printAll() int i;
    {
        i = 0;
        do {
            print(data[i]);
        } while (i < len(data), i++);
    }
}



class ExtendedTable extends Table {
    int factor;
    void setFactor(int f) { factor = f; }
    void multiply() int i;
    {
        i = 0;
        do {
            data[i] = data[i] * factor;
        } while (i < len(data), i++);
    }
}

{
   void main() {
      Table t;
      ExtendedTable et;
      t = new Table();
      t.init(SIZE);
      et = new ExtendedTable();
      et.init(SIZE);
      et.setFactor(3);
      add(s1, 10);
      add(s1, 20);
      print("Set contents:");
      print(s1);
      print("Base table:");
      t.printAll();
      print("Extended table:");
      et.multiply();
      et.printAll();
   }
}
```

---

## 🧰 Included Libraries

- **symboltable** – handles symbol resolution, scope management, and modular linking  
- **mj-runtime** – provides MJVM bytecode execution, disassembly, and debugging tools  

Each library can be rebuilt individually using:

```bash
cd MicroJava-Libraries/scripts
./buildJars.ps1
```

---

## 🧭 Modular Compilation Flow

1. **Lexical/Syntax analysis** — parses all `.mj` files.  
2. **Module resolution** — imports are processed recursively.  
3. **Semantic linking** — symbols are verified across modules.  
4. **Code generation** — bytecode emitted for each module.  
5. **Execution** — MJVM loads `.obj` files, switches contexts dynamically.

---

## ⚠️ Limitations

- Maximum 256 local variables per method  
- Source code size ≤ 8 KB per module  
- Maximem 255 user-defined modules per program (module with 0 index is reserved for Universe module)
- No exceptions, threads, or generics (educational simplification)  

---

## 👤 Author

**Nikola Nikolić**  
University of Belgrade, School of Electrical Engineering  
2026 — *Diploma Thesis: “Development of a Compiler and Extension of the Virtual Machine for the MicroJava Language with Support for Modularity”*

---

## 📄 License

This project is released for **educational and research purposes**.  
Feel free to modify and extend under appropriate attribution.

---
