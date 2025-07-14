package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TVFHandler {

    public static Map<Struct, TVF> tvfMap; // map of all TVFs, key is class type

    Logger log = Logger.getLogger(getClass());

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append (" na liniji ").append(line);
        log.info(msg.toString());
    }


    // Singleton instance
    private static TVFHandler instance;

    // Private constructor to prevent instantiation
    private TVFHandler() {
        tvfMap = new LinkedHashMap<>();
    }

    // Public method to get the singleton instance
    public static TVFHandler getInstance() {
        if (instance == null) {
            instance = new TVFHandler();
        }
        return instance;
    }

    public void createTVF(Struct classType) {
        TVF tvf = new TVF();
        tvfMap.put(classType, tvf);
    }

    public void inheritMethods(Struct classType) {
        TVF currTvf = tvfMap.get(classType);

        // ingerit interface methods (implemented methods only)
        for(Struct iface: classType.getImplementedInterfaces()){
            currTvf.inheritMethodsFromParent(tvfMap.get(iface));
        }
        // inherit methods from parent class
        Struct parent = classType.getElemType();
        while (parent != null && parent != Tab.noType){
            currTvf.inheritMethodsFromParent(tvfMap.get(parent));
            parent = parent.getElemType();
        }
    }

    public void addClassMethods(Struct classType) {
        TVF myTVF = tvfMap.get(classType);
        for (Obj member: classType.getMembers()){
            if (member.getKind() == Obj.Meth && member.getFpPos() == SemanticAnalyzer.FP_POS_IMPLEMENTED_NONGLOBAL_METHOD) {
                myTVF.addMethod(member.getName(), member.getAdr());
            }
        }
    }

    public void putAllTVFsInMemory() {
        for (Struct classType : tvfMap.keySet()) {
            TVF myTVF = tvfMap.get(classType);
            myTVF.putTVFInMemory();
        }
    }

    public void putTVFInMemory(Struct classType){
        TVF myTVF = tvfMap.get(classType);
        myTVF.putTVFInMemory();
    }

    public int getMyTVFaddressInMemory(Struct classType) {
        TVF myTVF = tvfMap.get(classType);
        return myTVF.getMemoryStartTVF();
    }

}
