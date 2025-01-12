package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.symboltable.Tab;

public class MJParserTest {

	static {
		DOMConfigurator.configure("config/log4j.xml");
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}

	public static void main(String[] args) throws Exception {

		Logger log = Logger.getLogger(MJParserTest.class);

		Reader br = null;
		try {
			File sourceCode = new File("test/GenerisanjeKoda-TestPrimeri_24_25_jan_feb/test301.mj");
			// File sourceCode = new File("tests/official_microjava_programs/test301.mj");
			// File sourceCode = new File("tests/official_microjava_programs/test302.mj");
			// File sourceCode = new File("tests/official_microjava_programs/test303.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());

			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);

			MJParser p = new MJParser(lexer);
			log.info("Starting syntax analysis.");
			Symbol s = p.parse();
			log.info("Ending syntax analysis.");

			Program prog = (Program) (s.value);
			log.info("Start of Abstract Syntax Tree (AST).");
			log.info(prog.toString(""));
			log.info("End of Abstract Syntax Tree (AST).");

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
			}
		}

	}

}


