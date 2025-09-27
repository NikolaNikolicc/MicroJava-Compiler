package rs.ac.bg.etf.pp1.code_generation;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.syntax_analysis.output.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.semantic_analysis.SemanticAnalyzer;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Module;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TVFHandler {

    private static final Map<Module, TVFHandler> moduleTVFMap = new HashMap<>(); // static map of all TVFHandlers, key is module
    private static final Map<Integer, Module> moduleIndexMap = new HashMap<>(); // static map of all modules, key is module index

    private final  Map<Struct, TVF> tvfMap = new LinkedHashMap<>(); // map of all TVFs in this module's TVFHandler, key is class type
    private final CodeGenerator myCodeGenerator;

    Logger log = Logger.getLogger(getClass());
    int parentModuleIndex = -1;

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append (" na liniji ").append(line);
        log.info(msg.toString());
    }

    public static void putTVFHandlerInModuleTVFMap(Module key, TVFHandler value) {
        moduleTVFMap.put(key, value);
        moduleIndexMap.put(key.getIndex(), key);
    }

    public static void putAllTVFHandlersInMemory() {
        for (TVFHandler tvfh: TVFHandler.moduleTVFMap.values()){
            tvfh.putAllTVFsInMemory();
        }
    }

    private Module resolveModuleByIndex(int index){
        return moduleIndexMap.get(index);
    }

    private TVFHandler resolveTVFHandlerByModuleIndex(int index){
        Module mod = resolveModuleByIndex(index);
        if (mod != null){
            return moduleTVFMap.get(mod);
        }
        return null;
    }

    public TVFHandler(CodeGenerator myCodeGenerator) {
        this.myCodeGenerator = myCodeGenerator;
    }

    public void createTVF(Struct classType) {
        TVF tvf = new TVF(parentModuleIndex);
        tvfMap.put(classType, tvf);
    }

    public void setParentModuleIndex(int parentModuleIndex) {
        this.parentModuleIndex = parentModuleIndex;
    }

    public void inheritMethods(Struct classType) {
        TVF currTvf = tvfMap.get(classType);

        // ingerit interface methods (implemented methods only)
        for(Struct iface: classType.getImplementedInterfaces()){
            TVFHandler parentIfaceTVFHandler = resolveTVFHandlerByModuleIndex(iface.getModuleIndex());
            TVF parentIfaceTVF = parentIfaceTVFHandler.tvfMap.get(iface);
            currTvf.inheritMethodsFromParent(parentIfaceTVF);
        }
        // inherit methods from parent class
        Struct parent = classType.getElemType();
        while (parent != null && parent != Tab.noType){
            TVFHandler parentClassTVFHandler = resolveTVFHandlerByModuleIndex(parent.getModuleIndex());
            TVF parentClassTVF = parentClassTVFHandler.tvfMap.get(parent);
            currTvf.inheritMethodsFromParent(parentClassTVF);
            parent = parent.getElemType();
        }
    }

    public void addClassMethods(Struct classType) {
        TVF myTVF = tvfMap.get(classType);
        for (Obj member: classType.getMembers()){
            if (member.getKind() == Obj.Meth && member.getFpPos() == SemanticAnalyzer.FP_POS_IMPLEMENTED_NONGLOBAL_METHOD) {
                myTVF.addMethod(member);
            }
        }
    }

    public void putAllTVFsInMemory() {
        for (Struct classType : tvfMap.keySet()) {
            TVF myTVF = tvfMap.get(classType);
            myTVF.putTVFInMemory(myCodeGenerator);
        }
    }

    public void putTVFInMemory(Struct classType){
        TVF myTVF = tvfMap.get(classType);
        myTVF.putTVFInMemory(myCodeGenerator);
    }

    public int getMyTVFaddressInMemory(Struct classType) {
        TVF myTVF = tvfMap.get(classType);
        return myTVF.getMemoryStartTVF();
    }

}
