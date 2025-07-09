package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;

/**
 * Predstavlja jedan unos u virtuelnoj tabeli metoda (VMT).
 * Svaki unos sadrži ime metode, njen offset u VMT i referencu na obj čvor.
 */
public class VMTEntry {
    /** Ime metode */
    String methodName;

    /** Offset metode u VMT (u rečima) */
    int offset;

    /** Referenca na obj čvor metode */
    Obj methodObj;
}
