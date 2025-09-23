package rs.ac.bg.etf.pp1.code_generation;

import rs.etf.pp1.mj.runtime.Code;

/**
 * Predstavlja jedan unos u virtuelnoj tabeli metoda (VMT).
 * Svaki unos sadrži ime metode, njen offset u VMT i referencu na obj čvor.
 */
public class TVFEntry {
    /** Ime metode */
    String methodName;

    /** Offset metode u VMT (u rečima) */
    int methodAddress;

    int methodModuleIndex;

    public TVFEntry(String name, int address, int index) {
        methodName = name;
        methodAddress = address;
        methodModuleIndex = index;
    }

    public int putTVFEntryInMemory(int memAddress, int moduleIndex) {
        // add method name char by char
        for (char ch: methodName.toCharArray()) {
            Code.loadConst(ch);
            Code.put(Code.putstatic);
            Code.put2(memAddress++);
            Code.put(moduleIndex);
        }
        // add -1 terminator for end of string
        Code.loadConst(-1);
        Code.put(Code.putstatic);
        Code.put2(memAddress++);
        Code.put(moduleIndex);

        // add method address
        Code.loadConst(methodAddress);
        Code.put(Code.putstatic);
        Code.put2(memAddress++);
        Code.put(moduleIndex);

        // add method module index
        Code.loadConst(moduleIndex);
        Code.put(Code.putstatic);
        Code.put2(memAddress++);
        Code.put(moduleIndex);

        Code.dataSize += methodName.length() + 2; // 1 for the -1 terminator and 1 for the address
        return memAddress;
    }

}
