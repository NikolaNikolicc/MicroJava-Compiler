package rs.ac.bg.etf.pp1;

import rs.etf.pp1.mj.runtime.Code;

import java.util.ArrayList;
import java.util.List;

public class TVF {

    private int memoryStartTVF;
    List<TVFEntry> entries; // list of all methods in one TVF

    TVF() {
        this.entries = new ArrayList<>();
        memoryStartTVF = -1; // -1 means that TVF is not yet in memory
    }

    public int getMemoryStartTVF() {
        return memoryStartTVF;
    }

    public void inheritMethodsFromParent(TVF parentTVF) {
        if (parentTVF != null) {
            for (TVFEntry entry : parentTVF.entries) {
                // Pravimo kopiju TVFEntry-ja za nasleđene metode
                addEntry(entry.methodName, entry.methodAddress);
            }
        }
    }

    public TVFEntry findEntry(String methodName) {
        for (TVFEntry entry : entries) {
            if (entry.methodName.equals(methodName)) {
                return entry;
            }
        }
        return null;
    }

    public void addEntry(String methodName, int address) {
        TVFEntry entry = new TVFEntry();
        entry.methodName = methodName;
        entry.methodAddress = address;
        entries.add(entry);
    }

    public void addMethod(String methodName, int address) {
        TVFEntry entry = findEntry(methodName);
        if (entry != null) {
            entry.methodAddress = address;
        } else {
            addEntry(methodName, address);
        }

    }

    public void putTVFInMemory() {

        if (memoryStartTVF != -1) {
            return;
        }
        memoryStartTVF = Code.dataSize;

        int memAddress = memoryStartTVF;
        for (TVFEntry entry : entries) {
            // add method name char by char
            for (char ch: entry.methodName.toCharArray()) {
                Code.loadConst(ch);
                Code.put(Code.putstatic);
                Code.put2(memAddress++);
            }
            // add -1 terminator for end of string
            Code.loadConst(-1);
            Code.put(Code.putstatic);
            Code.put2(memAddress++);

            // add method address
            Code.loadConst(entry.methodAddress);
            Code.put(Code.putstatic);
            Code.put2(memAddress++);

            Code.dataSize += entry.methodName.length() + 2; // 1 for the -1 terminator and 1 for the address
        }

        // add -2 terminator for end of TVF
        Code.loadConst(-2);
        Code.put(Code.putstatic);
        Code.put2(memAddress);

        Code.dataSize++; // 1 for the -2 terminator
    }
}
