package rs.ac.bg.etf.pp1.code_generation;

/**
 * Predstavlja jedan unos u virtuelnoj tabeli metoda (VMT).
 * Svaki unos sadrži ime metode, njen offset u VMT i referencu na obj čvor.
 */
public class TVFEntry {
    /** Ime metode */
    String methodName;

    /** Offset metode u VMT (u rečima) */
    int methodAddress;

}
