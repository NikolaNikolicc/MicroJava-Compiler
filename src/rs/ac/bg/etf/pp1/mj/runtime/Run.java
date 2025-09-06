package rs.ac.bg.etf.pp1.mj.runtime;

import rs.ac.bg.etf.pp1.mj.runtime.FormatException;
import rs.ac.bg.etf.pp1.mj.runtime.VMException;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Run {
    static boolean debug;
    static byte[] code;
    static int[] data;
    static int[] heap;
    static int[] stack;
    static int[] local;
    static int dataSize;
    static int startPC;
    static int pc;
    static int fp;
    static int sp;
    static int esp;
    static int free;
    static final int heapSize = 100000;
    static final int mStackSize = 400;
    static final int eStackSize = 30;
    static final int load = 1;
    static final int load_0 = 2;
    static final int load_1 = 3;
    static final int load_2 = 4;
    static final int load_3 = 5;
    static final int store = 6;
    static final int store_0 = 7;
    static final int store_1 = 8;
    static final int store_2 = 9;
    static final int store_3 = 10;
    static final int getstatic = 11;
    static final int putstatic = 12;
    static final int getfield = 13;
    static final int putfield = 14;
    static final int const_0 = 15;
    static final int const_1 = 16;
    static final int const_2 = 17;
    static final int const_3 = 18;
    static final int const_4 = 19;
    static final int const_5 = 20;
    static final int const_m1 = 21;
    static final int const_ = 22;
    static final int add = 23;
    static final int sub = 24;
    static final int mul = 25;
    static final int div = 26;
    static final int rem = 27;
    static final int neg = 28;
    static final int shl = 29;
    static final int shr = 30;
    static final int inc = 31;
    static final int new_ = 32;
    static final int newarray = 33;
    static final int aload = 34;
    static final int astore = 35;
    static final int baload = 36;
    static final int bastore = 37;
    static final int arraylength = 38;
    static final int pop = 39;
    static final int dup = 40;
    static final int dup2 = 41;
    static final int jmp = 42;
    static final int jcond = 43;
    static final int call = 49;
    static final int return_ = 50;
    static final int enter = 51;
    static final int exit = 52;
    static final int read = 53;
    static final int print = 54;
    static final int bread = 55;
    static final int bprint = 56;
    static final int trap = 57;
    static final int invokevirtual = 58;
    static final int dup_x1 = 59;
    static final int dup_x2 = 60;
    static final int eq = 0;
    static final int ne = 1;
    static final int lt = 2;
    static final int le = 3;
    static final int gt = 4;
    static final int ge = 5;
    static String[] opcode = new String[]{"???        ", "load       ", "load_0     ", "load_1     ", "load_2     ", "load_3     ", "store      ", "store_0    ", "store_1    ", "store_2    ", "store_3    ", "getstatic  ", "putstatic  ", "getfield   ", "putfield   ", "const_0    ", "const_1    ", "const_2    ", "const_3    ", "const_4    ", "const_5    ", "const_m1   ", "const      ", "add        ", "sub        ", "mul        ", "div        ", "rem        ", "neg        ", "shl        ", "shr        ", "inc        ", "new        ", "newarray   ", "aload      ", "astore     ", "baload     ", "bastore    ", "arraylength", "pop        ", "dup        ", "dup2       ", "jmp        ", "jeq        ", "jne        ", "jlt        ", "jle        ", "jgt        ", "jge        ", "call       ", "return     ", "enter      ", "exit       ", "read       ", "print      ", "bread      ", "bprint     ", "trap       ", "invokevirtual", "dup_x1     ", "dup_x2     "};

    public Run() {
    }

    static void push(int var0) throws VMException {
        if (esp == 30) {
            throw new VMException("expression stack overflow");
        } else {
            stack[esp++] = var0;
        }
    }

    static int pop() throws VMException {
        if (esp == 0) {
            throw new VMException("expression stack underflow");
        } else {
            return stack[--esp];
        }
    }

    static void PUSH(int var0) throws VMException {
        if (sp == 400) {
            throw new VMException("method stack overflow");
        } else {
            local[sp++] = var0;
        }
    }

    static int POP() throws VMException {
        if (sp == 0) {
            throw new VMException("method stack underflow");
        } else {
            return local[--sp];
        }
    }

    static byte next(boolean var0) {
        byte var1 = code[pc++];
        if (debug && var0) {
            System.out.print(var1 + " ");
        }

        return var1;
    }

    static short next2(boolean var0) {
        short var1 = (short)((next(false) << 8) + (next(false) & 255) << 16 >> 16);
        if (debug && var0) {
            System.out.print(var1 + " ");
        }

        return var1;
    }

    static int next4() {
        int var0 = (next2(false) << 16) + (next2(false) & '\uffff');
        if (debug) {
            System.out.print(var0 + " ");
        }

        return var0;
    }

    static void load(String var0) throws IOException, FormatException {
        byte[] var2 = new byte[2];
        DataInputStream var3 = new DataInputStream(new FileInputStream(var0));
        var3.read(var2, 0, 2);
        if (var2[0] == 77 && var2[1] == 74) {
            int var1 = var3.readInt();
            if (var1 <= 0) {
                throw new FormatException("codeSize <= 0");
            } else {
                dataSize = var3.readInt();
                if (dataSize < 0) {
                    throw new FormatException("dataSize < 0");
                } else {
                    startPC = var3.readInt();
                    if (startPC >= 0 && startPC < var1) {
                        code = new byte[var1];
                        var3.read(code, 0, var1);
                    } else {
                        throw new FormatException("startPC not in code area");
                    }
                }
            }
        } else {
            throw new FormatException("wrong marker");
        }
    }

    static int alloc(int var0) throws VMException {
        int var1 = free;
        free += var0 + 3 >> 2;
        if (free > 100000) {
            throw new VMException("heap overflow");
        } else {
            return var1;
        }
    }

    static byte getByte(int var0, int var1) {
        return (byte)(var0 << 8 * var1 >>> 24);
    }

    static int setByte(int var0, int var1, byte var2) {
        int var3 = (3 - var1) * 8;
        int var4 = ~(255 << var3);
        int var5 = (var2 & 255) << var3;
        return var0 & var4 ^ var5;
    }

    static int readInt() throws IOException {
        int var0 = 0;
        int var1 = 32;

        int var2;
        for(var2 = System.in.read(); var2 < 48 || var2 > 57; var2 = System.in.read()) {
            var1 = var2;
        }

        while(var2 >= 48 && var2 <= 57) {
            var0 = 10 * var0 + var2 - 48;
            var2 = System.in.read();
        }

        if (var1 == 45) {
            var0 = -var0;
        }

        return var0;
    }

    static void printNum(int var0, int var1) {
        String var2 = (new Integer(var0)).toString();

        for(int var3 = var2.length(); var3 < var1; ++var3) {
            System.out.print(" ");
        }

        System.out.print(var2);
    }

    static void printInstr() {
        byte var0 = code[pc - 1];
        String var1 = var0 > 0 && var0 <= 58 ? opcode[var0] : opcode[0];
        printNum(pc - 1, 5);
        System.out.print(": " + var1 + " ");
    }

    static void printStack() {
        for(int var0 = 0; var0 < esp; ++var0) {
            System.out.print(stack[var0] + " ");
        }

        System.out.println();
    }

    static void interpret() {
        pc = startPC;
        if (debug) {
            System.out.println();
            System.out.println("  pos: instruction operands");
            System.out.println("     | expressionstack");
            System.out.println("-----------------------------");
        }

        try {
            while(true) {
                int var0 = next(false);
                if (debug) {
                    printInstr();
                }

                switch (var0) {
                    case 1:
                        push(local[fp + next(true)]);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        var0 -= 2;
                        push(local[fp + var0]);
                        break;
                    case 6:
                        local[fp + next(true)] = pop();
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        var0 -= 7;
                        local[fp + var0] = pop();
                        break;
                    case 11:
                        push(data[next2(true)]);
                        break;
                    case 12:
                        data[next2(true)] = pop();
                        break;
                    case 13:
                        int var31 = pop();
                        if (var31 == 0) {
                            throw new VMException("null reference used");
                        }

                        push(heap[var31 + next2(true)]);
                        break;
                    case 14:
                        int var47 = pop();
                        int var30 = pop();
                        if (var30 == 0) {
                            throw new VMException("null reference used");
                        }

                        heap[var30 + next2(true)] = var47;
                        break;
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                        push(var0 - 15);
                        break;
                    case 21:
                        push(-1);
                        break;
                    case 22:
                        push(next4());
                        break;
                    case 23:
                        push(pop() + pop());
                        break;
                    case 24:
                        push(-pop() + pop());
                        break;
                    case 25:
                        push(pop() * pop());
                        break;
                    case 26:
                        int var46 = pop();
                        if (var46 == 0) {
                            throw new VMException("division by zero");
                        }

                        push(pop() / var46);
                        break;
                    case 27:
                        int var45 = pop();
                        if (var45 == 0) {
                            throw new VMException("division by zero");
                        }

                        push(pop() % var45);
                        break;
                    case 28:
                        push(-pop());
                        break;
                    case 29:
                        int var44 = pop();
                        push(pop() << var44);
                        break;
                    case 30:
                        int var43 = pop();
                        push(pop() >> var43);
                        break;
                    case 31:
                        int var53 = fp + next(true);
                        int[] var68 = local;
                        var68[var53] += next(true);
                        break;
                    case 32:
                        push(alloc(next2(true)));
                        break;
                    case 33:
                        byte var42 = next(true);
                        int var63 = pop();
                        int var29;
                        if (var42 == 0) {
                            var29 = alloc(var63 + 4);
                        } else {
                            var29 = alloc(var63 * 4 + 4);
                        }

                        heap[var29] = var63;
                        push(var29 + 1);
                        break;
                    case 34:
                        int var56 = pop();
                        int var28 = pop();
                        if (var28 == 0) {
                            throw new VMException("null reference used");
                        }

                        int var62 = heap[var28 - 1];
                        if (var56 < 0 || var56 >= var62) {
                            throw new VMException("index out of bounds");
                        }

                        push(heap[var28 + var56]);
                        break;
                    case 35:
                        int var41 = pop();
                        int var55 = pop();
                        int var27 = pop();
                        if (var27 == 0) {
                            throw new VMException("null reference used");
                        }

                        int var61 = heap[var27 - 1];
                        if (debug) {
                            System.out.println("\nArraylength = " + var61);
                            System.out.println("Address = " + var27);
                            System.out.println("Index = " + var55);
                            System.out.println("Value = " + var41);
                        }

                        if (var55 < 0 || var55 >= var61) {
                            throw new VMException("index out of bounds");
                        }

                        heap[var27 + var55] = var41;
                        break;
                    case 36:
                        int var54 = pop();
                        int var26 = pop();
                        if (var26 == 0) {
                            throw new VMException("null reference used");
                        }

                        int var60 = heap[var26 - 1];
                        if (var54 < 0 || var54 >= var60) {
                            throw new VMException("index out of bounds");
                        }

                        push(getByte(heap[var26 + var54 / 4], var54 % 4));
                        break;
                    case 37:
                        int var40 = pop();
                        int var5 = pop();
                        int var25 = pop();
                        if (var25 == 0) {
                            throw new VMException("null reference used");
                        }

                        int var59 = heap[var25 - 1];
                        if (var5 < 0 || var5 >= var59) {
                            throw new VMException("index out of bounds");
                        }

                        heap[var25 + var5 / 4] = setByte(heap[var25 + var5 / 4], var5 % 4, (byte)var40);
                        break;
                    case 38:
                        int var24 = pop();
                        if (var24 == 0) {
                            throw new VMException("null reference used");
                        }

                        push(heap[var24 - 1]);
                        break;
                    case 39:
                        pop();
                        break;
                    case 40:
                        int var39 = pop();
                        push(var39);
                        push(var39);
                        break;
                    case 41:
                        int var38 = pop();
                        int var50 = pop();
                        push(var50);
                        push(var38);
                        push(var50);
                        push(var38);
                        break;
                    case 42:
                        short var52 = next2(true);
                        pc += var52 - 3;
                        break;
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                        short var51 = next2(true);
                        int var49 = pop();
                        int var37 = pop();
                        boolean var9 = false;
                        switch (var0 - 43) {
                            case 0:
                                var9 = var37 == var49;
                                break;
                            case 1:
                                var9 = var37 != var49;
                                break;
                            case 2:
                                var9 = var37 < var49;
                                break;
                            case 3:
                                var9 = var37 <= var49;
                                break;
                            case 4:
                                var9 = var37 > var49;
                                break;
                            case 5:
                                var9 = var37 >= var49;
                        }

                        if (var9) {
                            pc += var51 - 3;
                        }
                        break;
                    case 49:
                        short var4 = next2(true);
                        PUSH(pc);
                        pc += var4 - 3;
                        break;
                    case 50:
                        if (sp == 0) {
                            return;
                        }

                        pc = POP();
                        break;
                    case 51:
                        byte var10 = next(true);
                        byte var11 = next(true);
                        PUSH(fp);
                        fp = sp;

                        for(int var65 = 0; var65 < var11; ++var65) {
                            PUSH(0);
                        }

                        for(int var66 = var10 - 1; var66 >= 0; --var66) {
                            local[fp + var66] = pop();
                        }
                        break;
                    case 52:
                        sp = fp;
                        fp = POP();
                        break;
                    case 53:
                        try {
                            int var36 = readInt();
                            push(var36);
                            break;
                        } catch (IOException var19) {
                            throw new VMException("unexpected end of input");
                        }
                    case 54:
                        int var57 = pop();
                        int var35 = pop();
                        String var16 = (new Integer(var35)).toString();
                        var57 -= var16.length();

                        for(int var64 = 0; var64 < var57; ++var64) {
                            System.out.print(' ');
                        }

                        System.out.print(var16);
                        break;
                    case 55:
                        try {
                            push(System.in.read());
                            break;
                        } catch (IOException var18) {
                            throw new VMException("end of input");
                        }
                    case 56:
                        int var6 = pop() - 1;
                        int var34 = pop();

                        for(int var7 = 0; var7 < var6; ++var7) {
                            System.out.print(' ');
                        }

                        System.out.print((char)var34);
                        break;
                    case 57:
                        throw new VMException("trap(" + next(true) + ")");
                    case 58:
                        int var12 = pc;
                        int var13 = 0;
                        boolean var14 = false;
                        int var15 = -1;
                        int var1 = pop();

                        for(int var33 = data[var1++]; var33 != -2; var33 = data[var1++]) {
                            var15 = next4();
                            if (var15 != var33 || var15 == -1) {
                                if (var15 == -1 && var33 == -1) {
                                    var13 = data[var1];
                                    var14 = true;
                                    break;
                                }

                                if (var15 == -1 && var33 != -1) {
                                    for(pc = var12; var33 != -1; var33 = data[var1++]) {
                                    }

                                    ++var1;
                                    int var67 = data[var1];
                                } else {
                                    for(pc = var12; var33 != -1; var33 = data[var1++]) {
                                    }

                                    ++var1;
                                    int var10000 = data[var1];
                                }
                            }
                        }

                        if (!var14) {
                            while(var15 != -1) {
                                var15 = next4();
                            }

                            throw new VMException("method address not found");
                        }

                        PUSH(pc);
                        pc = var13;
                        break;
                    case 59:
                        int var32 = pop();
                        int var48 = pop();
                        push(var32);
                        push(var48);
                        push(var32);
                        break;
                    case 60:
                        int var2 = pop();
                        int var3 = pop();
                        int var8 = pop();
                        push(var2);
                        push(var8);
                        push(var3);
                        push(var2);
                        break;
                    default:
                        throw new VMException("wrong opcode " + var0);
                }

                if (debug) {
                    System.out.println();
                    System.out.print("     | ");
                    printStack();
                }
            }
        } catch (VMException var20) {
            System.out.println("\n-- exception at address " + (pc - 1) + ": " + var20.getMessage());
        }
    }

    public static void main(String[] var0) {
        String var1 = null;
        debug = false;

        for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2].equals("-debug")) {
                debug = true;
            } else {
                var1 = var0[var2];
            }
        }

        if (var1 == null) {
            System.out.println("Syntax: java ssw.mj.Run filename [-debug]");
        } else {
            try {
                load(var1);
                heap = new int[100000];
                data = new int[dataSize];
                stack = new int[30];
                local = new int[400];
                fp = 0;
                sp = 0;
                esp = 0;
                free = 1;
                long var7 = System.currentTimeMillis();
                interpret();
                System.out.print("\nCompletion took " + (System.currentTimeMillis() - var7) + " ms");
            } catch (FileNotFoundException var4) {
                System.out.println("-- file " + var1 + " not found");
            } catch (IOException var5) {
                System.out.println("-- error reading file " + var1);
            } catch (FormatException var6) {
                System.out.println("-- corrupted object file " + var1 + ": " + var6.getMessage());
            }

        }
    }
}
