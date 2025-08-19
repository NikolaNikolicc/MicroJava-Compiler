package rs.ac.bg.etf.pp1;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class SetHandler {

    Obj addMeth;
    Obj addAllMeth;
    Obj printSetMeth;
    Obj unionSetsMeth;
    Obj intersectSetsMeth;
    Obj differenceSetsMeth;

    // <editor-fold desc="Singleton Instance">

    private static SetHandler instance;

    private SetHandler(){
        generateAdd();
        generateAddAll();
        generatePrintSet();
        generateUnionSets();
        generateIntersectionSets();
        generateDifferenceSets();
    }

    public static SetHandler getInstance(){
        if (instance == null) {
            instance = new SetHandler();
        }
        return instance;
    }

    // </editor-fold>

    // <editor-fold desc="Set Embedded Methods">

    /*
    add method logic:

    if (set_size >= set_capacity)return;
    set: [set_size, set_elem_0, ...], set_size <= set_capacity
    set_capacity = arraylength

    for (int i = 0; i < set_size; i++){
        if (set[i + 1] == b)return;
    }
    set[set_size] = b;
    set_size++;
    */
    private void generateAdd(){
        addMeth = Tab.find("add");
        Obj a = null; // Local variable for set
        Obj b = null; // Local variable for element to add
        Obj i = null;
        for(Obj member: addMeth.getLocalSymbols()){
            if (member.getName().equals("a")) {
                a = member;
            } else if (member.getName().equals("b")) {
                b = member;
            } else if (member.getName().equals("i")) {
                i = member;
            }
        }

        assert a != null && b != null && i != null : "Local variables not found in add method";

        addMeth.setAdr(Code.pc);

        // add(set, b):
        // set[0] = set_size
        // set[1..set_size] = elementi
        // Ako je set_size >= set.length - 1, return
        // Ako je b već u skupu, return
        // Ako nije, dodaj na kraj i uvećaj set[0]
        Code.put(Code.enter);
        Code.put(2); // 2 formalna parametra: a, b
        Code.put(3); // 2 parametra + 1 lokalna promenljiva (i)

        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0]

        Code.load(a);
        Code.put(Code.arraylength); // set.length
        Code.loadConst(1);
        Code.put(Code.sub);

        Code.putFalseJump(Code.lt, 0); // if set[0] >= set.length - 1 then return
        int fullReturnJump = Code.pc - 2;

        Code.loadConst(0);
        Code.store(i); // i = 0

        // if (i >= set[0])break; // while (i < set[0])
        int loopStart = Code.pc;
        Code.load(i); // i
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0] - get the size of the set
        Code.putFalseJump(Code.lt, 0); // if i >= set[0] then break
        int breakJump = Code.pc - 2;

        // if (set[i +1] == b) return;
        Code.load(a);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.put(Code.aload); // set[i + 1]
        Code.load(b);
        Code.putFalseJump(Code.ne, 0); // if set[i + 1] == b then return
        int returnJump = Code.pc - 2;

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart); // jump to the start of the loop

        Code.fixup(breakJump);
        Code.load(a);
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0] - get the size of the set
        Code.loadConst(1);
        Code.put(Code.add); // set[0] + 1
        Code.load(b);
        Code.put(Code.astore); // set[set[0] + 1] = b

        Code.load(a);
        Code.loadConst(0);
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0] - get the size of the set
        Code.loadConst(1);
        Code.put(Code.add); // set[0] + 1
        Code.put(Code.astore); // set[0] = set[0] + 1

        Code.fixup(returnJump);
        Code.fixup(fullReturnJump);

        Code.put(Code.exit);
        Code.put(Code.return_);

    }

    /*
    addAll(a, b)

    for (int i = 0; i < b_size; i++){
        add(a, b[i])
    }
    */
    private void generateAddAll(){


        addAllMeth = Tab.find("addAll");

        Obj a = null; // Local variable for set
        Obj b = null; // Local variable for element to add
        Obj i = null;
        for(Obj member: addMeth.getLocalSymbols()){
            if (member.getName().equals("a")) {
                a = member;
            } else if (member.getName().equals("b")) {
                b = member;
            } else if (member.getName().equals("i")) {
                i = member;
            }
        }

        assert a != null && b != null && i != null : "Local variables not found in add method";

        addAllMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(2); // 2 formalna parametra: a, b
        Code.put(3); // 2 parametra + 1 lokalna promenljiva (i)

        Code.loadConst(0);
        Code.store(i); // i = 0

        int loopStart = Code.pc;

        // if (i >= b.length)break; // while (i < b.length)
        Code.load(i);
        Code.load(b);
        Code.put(Code.arraylength);
        Code.putFalseJump(Code.lt, 0);
        int loopEnd = Code.pc - 2;

        // add(a, b[i]);
        Code.load(a);
        Code.load(b);
        Code.load(i);
        Code.put(Code.aload); // b[i]
        int addOffset = addMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        Code.put(Code.call);
        Code.put2(addOffset); // Call the add method

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart); // jump to the start of the loop

        Code.fixup(loopEnd);

        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    private void generatePrintSet(){
        printSetMeth = Tab.find("$printSet");

        Obj a = null; // Local variable for set
        Obj offset = null;
        Obj i = null;
        for(Obj member: printSetMeth.getLocalSymbols()){
            if (member.getName().equals("a")) {
                a = member;
            } else if (member.getName().equals("offset")) {
                offset = member;
            } else if (member.getName().equals("i")) {
                i = member;
            }
        }

        assert a != null && offset != null && i != null : "Local variables not found in add method";

        printSetMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(2); // 2 formalna parametra: a, offset
        Code.put(3); // 2 parametra + 1 lokalna promenljiva (i)

        CodeGenerator.printChar('s', null);
        CodeGenerator.printChar('e', null);
        CodeGenerator.printChar('t', null);
        CodeGenerator.printChar(':', null);
        CodeGenerator.printChar(' ', null);
        CodeGenerator.printChar('(', null);

        int loopStart = Code.pc;

        Code.load(i);
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload); // set[0] - get the size of the set
        Code.loadConst(1);
        Code.put(Code.sub); // set[0] - 1 (we are using 1-based indexing for elements because we want to print comma after each element except the last one)
        Code.putFalseJump(Code.lt, 0); // if i >= set[0] - 1 then break
        int loopEnd = Code.pc - 2;

        // print set[i + 1]
        Code.load(a);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.put(Code.aload); // set[i + 1]
        Code.loadConst(0);
        Code.put(Code.print);
        CodeGenerator.printChar(',', null);

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart);

        Code.fixup(loopEnd);

        Code.load(a);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.put(Code.aload);
        Code.loadConst(0);
        Code.put(Code.print); // print last element without comma

        CodeGenerator.printChar(')', offset);

        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    /* intersection
    int i = 1;
        while (i <= s1[0]){
        int j = 1;
        while (j <= s2[0]){
            if (s1[i] == s2[j]){
                res.add(res, s1[i]);
                continue;
            }
            j++;
        }
        next: i++;
    }
    */
    private void generateIntersectionSets(){
        intersectSetsMeth = Tab.find("$intersect");

        Obj dst = null;
        Obj s1 = null;
        Obj s2 = null;
        Obj i = null;
        Obj j = null;
        for(Obj member: intersectSetsMeth.getLocalSymbols()){
            if (member.getName().equals("s1")) {
                s1 = member;
            } else if (member.getName().equals("s2")) {
                s2 = member;
            } else if (member.getName().equals("dst")) {
                dst = member;
            } else if (member.getName().equals("i")) {
                i = member;
            } else if (member.getName().equals("j")){
                j = member;
            }
        }

        assert dst != null && s1 != null && s2 != null && i != null && j != null: "Local variables not found in add method";

        intersectSetsMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(3);
        Code.put(5); // 3 formalna parametra: dst, s1, s2 + 2 lokalne promenljive (i, j)

        Code.loadConst(1);
        Code.store(i); // i = 1

        int loopStart = Code.pc;
        // if (i > s1[0])break; // while (i <= s1[0])
        Code.load(i);
        Code.load(s1);
        Code.loadConst(0);
        Code.put(Code.aload); // s1[0] - get the size of the set
        Code.putFalseJump(Code.le, 0); // if i > s1[0] then break
        int loopEnd = Code.pc - 2;

        Code.loadConst(1);
        Code.store(j); // j = 1

        int innerLoopStart = Code.pc;
        Code.load(j);
        Code.load(s2);
        Code.loadConst(0);
        Code.put(Code.aload); // s2[0] - get the size of the set
        Code.putFalseJump(Code.le, 0);
        int innerLoopEnd = Code.pc - 2;

        // if (s1[i] == s2[j])
        Code.load(s1);
        Code.load(i);
        Code.put(Code.aload);

        Code.load(s2);
        Code.load(j);
        Code.put(Code.aload);
        Code.putFalseJump(Code.eq, 0); // if s1[i] != s2[j] then continue
        int incj = Code.pc - 2;

        // set params
        Code.load(dst);
        Code.load(s1);
        Code.load(i);
        Code.put(Code.aload);

        // call add method
        int addOffset = addMeth.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(addOffset);

        Code.putJump(0);
        int inci = Code.pc - 2;

        Code.fixup(incj);
        // j++
        Code.load(j);
        Code.loadConst(1);
        Code.put(Code.add); // j + 1
        Code.store(j);

        Code.putJump(innerLoopStart); // jump to the start of the inner loop
        Code.fixup(innerLoopEnd);

        Code.fixup(inci);
        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(i);
        Code.putJump(loopStart); // jump to the start of the outer loop

        Code.fixup(loopEnd);

        Code.load(dst); // load the result set

        Code.put(Code.exit);
        Code.put(Code.return_);

    }

    /* difference
     size = s1[0];
     int i = 1;
     while (i <= s1[0]){
          int j = 1;
          while (j <= s2[0]){
             if (s1[i] == s2[j]){
                 goto next;
             }
             j++;
         }
         res.add(res, s1[i]);
         next: i++;
    }
    */
    private void generateDifferenceSets(){
        differenceSetsMeth = Tab.find("$difference");

        Obj dst = null;
        Obj s1 = null;
        Obj s2 = null;
        Obj i = null;
        Obj j = null;
        for(Obj member: differenceSetsMeth.getLocalSymbols()){
            if (member.getName().equals("s1")) {
                s1 = member;
            } else if (member.getName().equals("s2")) {
                s2 = member;
            } else if (member.getName().equals("dst")) {
                dst = member;
            } else if (member.getName().equals("i")) {
                i = member;
            } else if (member.getName().equals("j")){
                j = member;
            }
        }

        assert dst != null && s1 != null && s2 != null && i != null && j != null: "Local variables not found in add method";

        differenceSetsMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(3);
        Code.put(5); // 3 formalna parametra: dst, s1, s2 + 2 lokalne promenljive (i, j)

        Code.loadConst(1);
        Code.store(i); // i = 1

        int loopStart = Code.pc;
        // if (i > s1[0])break; // while (i <= s1[0])
        Code.load(i);
        Code.load(s1);
        Code.loadConst(0);
        Code.put(Code.aload); // s1[0] - get the size of the set
        Code.putFalseJump(Code.le, 0); // if i > s1[0] then break
        int loopEnd = Code.pc - 2;

        Code.loadConst(1);
        Code.store(j); // j = 1

        int innerLoopStart = Code.pc;
        Code.load(j);
        Code.load(s2);
        Code.loadConst(0);
        Code.put(Code.aload); // s2[0] - get the size of the set
        Code.putFalseJump(Code.le, 0);
        int innerLoopEnd = Code.pc - 2;

        // if (s1[i] == s2[j])
        Code.load(s1);
        Code.load(i);
        Code.put(Code.aload);

        Code.load(s2);
        Code.load(j);
        Code.put(Code.aload);
        Code.putFalseJump(Code.eq, 0); // if s1[i] != s2[j] then continue
        int incj = Code.pc - 2;

        Code.putJump(0);
        int inci = Code.pc - 2;

        Code.fixup(incj);
        // j++
        Code.load(j);
        Code.loadConst(1);
        Code.put(Code.add); // j + 1
        Code.store(j);

        Code.putJump(innerLoopStart); // jump to the start of the inner loop
        Code.fixup(innerLoopEnd);

        // set params
        Code.load(dst);
        Code.load(s1);
        Code.load(i);
        Code.put(Code.aload);

        // call add method
        int addOffset = addMeth.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(addOffset);

        Code.fixup(inci);
        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(i);
        Code.putJump(loopStart); // jump to the start of the outer loop

        Code.fixup(loopEnd);

        Code.load(dst); // load the result set

        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    // c = a union b
    private void generateUnionSets(){
        unionSetsMeth = Tab.find("$union");

        Obj a = null;
        Obj b = null;
        Obj c = null;
        Obj i = null;
        for(Obj member: unionSetsMeth.getLocalSymbols()){
            if (member.getName().equals("a")) {
                a = member;
            } else if (member.getName().equals("b")) {
                b = member;
            } else if (member.getName().equals("c")) {
                c = member;
            } else if (member.getName().equals("i")) {
                i = member;
            }
        }

        assert a != null && b != null && c != null && i != null : "Local variables not found in add method";

        unionSetsMeth.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(3); // 3 formalna parametra: a, b, c
        Code.put(4); // 3 parametra + 1 lokalna promenljiva (i)

        // c = a;
        Code.loadConst(0);
        Code.store(i); // i = 0

        int loopStart = Code.pc;

        Code.load(i);
        Code.load(a);
        Code.loadConst(0);
        Code.put(Code.aload);

        Code.putFalseJump(Code.lt, 0);
        int loopEnd = Code.pc - 2;

        // add(c, a[i + 1])
        Code.load(c);
        Code.load(a);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.put(Code.aload); // a[i + 1]
        int addOffset = addMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        Code.put(Code.call);
        Code.put2(addOffset); // Call the add method

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart);

        Code.fixup(loopEnd);

        // c = c union b
        Code.loadConst(0);
        Code.store(i); // i = 0

        int loopStart1 = Code.pc;

        Code.load(i);
        Code.load(b);
        Code.loadConst(0);
        Code.put(Code.aload);

        Code.putFalseJump(Code.lt, 0);
        int loopEnd1 = Code.pc - 2;

        // add(c, b[i + 1])
        Code.load(c);
        Code.load(b);
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.put(Code.aload); // b[i + 1]
        int addOffset1 = addMeth.getAdr() - Code.pc; // Calculate the offset to the add method
        Code.put(Code.call);
        Code.put2(addOffset1); // Call the add method

        // i++
        Code.load(i);
        Code.loadConst(1);
        Code.put(Code.add); // i + 1
        Code.store(i);

        Code.putJump(loopStart1);

        Code.fixup(loopEnd1);

        Code.load(c);

        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    // </editor-fold>

}
