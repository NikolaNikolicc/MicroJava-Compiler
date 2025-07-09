package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.List;

/**
 * Predstavlja virtuelnu tabelu metoda (VMT) za jednu klasu.
 * Svaka klasa ima svoju VMT koja sadrži sve metode te klase,
 * uključujući i nasleđene metode od roditeljske klase.
 */
public class VMTable {
    /** Lista svih metoda u VMT */
    List<VMTEntry> entries;

    /** Tip klase kojoj pripada ova VMT */
    Struct classType;

    /** VMT roditeljske klase (null ako nema roditelja) */
    VMTable parent;

    public VMTable() {
        this.entries = new ArrayList<>();
    }

    /**
     * Pronalazi VMTEntry za datu metodu po imenu
     * @param methodName Ime metode
     * @return VMTEntry ako metoda postoji, null inače
     */
    public VMTEntry findEntry(String methodName) {
        for (VMTEntry entry : entries) {
            if (entry.methodName.equals(methodName)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Dodaje novu metodu u VMT
     * @param methodName Ime metode
     * @param offset Offset metode u VMT
     * @param methodObj Referenca na obj čvor metode
     */
    public void addEntry(String methodName, int offset, Obj methodObj) {
        VMTEntry entry = new VMTEntry();
        entry.methodName = methodName;
        entry.offset = offset;
        entry.methodObj = methodObj;
        entries.add(entry);
    }

    /**
     * Nasleđuje sve metode iz roditeljske VMT
     * @param parentVMT VMT roditeljske klase
     */
    public void inheritMethods(VMTable parentVMT) {
        this.parent = parentVMT;
        if (parentVMT != null) {
            for (VMTEntry entry : parentVMT.entries) {
                // Pravimo kopiju VMTEntry-ja za nasleđene metode
                VMTEntry inherited = new VMTEntry();
                inherited.methodName = entry.methodName;
                inherited.offset = entry.offset;
                inherited.methodObj = entry.methodObj;
                entries.add(inherited);
            }
        }
    }

    /**
     * Override-uje postojeću metodu ili dodaje novu
     * @param methodName Ime metode
     * @param offset Novi offset
     * @param methodObj Nova implementacija metode
     */
    public void overrideMethod(String methodName, int offset, Obj methodObj) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).methodName.equals(methodName)) {
                VMTEntry entry = entries.get(i);
                entry.methodObj = methodObj;
                // Zadržavamo postojeći offset za polimorfizam
                return;
            }
        }
        // Ako metoda ne postoji, dodaj je kao novu
        addEntry(methodName, offset, methodObj);
    }

    /**
     * Vraća broj metoda u VMT
     */
    public int size() {
        return entries.size();
    }
}
