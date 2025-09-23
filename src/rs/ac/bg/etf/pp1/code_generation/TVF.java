package rs.ac.bg.etf.pp1.code_generation;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.ModuleHandler;
import rs.etf.pp1.symboltable.concepts.Obj;

import java.util.ArrayList;
import java.util.List;

public class TVF {

    private int memoryStartTVF;
    List<TVFEntry> entries; // list of all methods in one TVF
    int moduleIndex; // index of module where this TVF belongs

    TVF(int moduleIndex) {
        this.entries = new ArrayList<>();
        memoryStartTVF = -1; // -1 means that TVF is not yet in memory
        this.moduleIndex = moduleIndex;
    }

    public int getMemoryStartTVF() {
        return memoryStartTVF;
    }

    public void inheritMethodsFromParent(TVF parentTVF) {
        if (parentTVF != null) {
            for (TVFEntry entry : parentTVF.entries) {
                // Pravimo kopiju TVFEntry-ja za nasleđene metode
                addEntry(entry.methodName, entry.methodAddress, entry.methodModuleIndex);
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

    public void addEntry(String methodName, int address, int moduleIndex) {
        entries.add(new TVFEntry(methodName, address, moduleIndex));
    }

    public void addMethod(Obj node) {
        TVFEntry entry = findEntry(node.getName());
        if (entry != null) {
            entry.methodAddress = node.getAdr();
            entry.methodModuleIndex = moduleIndex;
        } else {
            addEntry(node.getName(), node.getAdr(), node.getModule().getIndex());
        }

    }

    public void putTVFInMemory() {

        if (memoryStartTVF != -1) {
            return;
        }
        memoryStartTVF = Code.dataSize;

        int memAddress = memoryStartTVF;
        for (TVFEntry entry : entries) {
            memAddress = entry.putTVFEntryInMemory(memAddress, moduleIndex);
//            // add method name char by char
//            for (char ch: entry.methodName.toCharArray()) {
//                Code.loadConst(ch);
//                Code.put(Code.putstatic);
//                Code.put2(memAddress++);
//                Code.put(currentModuleIndex);
//            }
//            // add -1 terminator for end of string
//            Code.loadConst(-1);
//            Code.put(Code.putstatic);
//            Code.put2(memAddress++);
//            Code.put(currentModuleIndex);
//
//            // add method address
//            Code.loadConst(entry.methodAddress);
//            Code.put(Code.putstatic);
//            Code.put2(memAddress++);
//            Code.put(currentModuleIndex);
//
//            Code.dataSize += entry.methodName.length() + 2; // 1 for the -1 terminator and 1 for the address
        }

        // add -2 terminator for end of TVF
        Code.loadConst(-2);
        Code.put(Code.putstatic);
        Code.put2(memAddress);
        Code.put(moduleIndex);

        Code.dataSize++; // 1 for the -2 terminator
    }
}
