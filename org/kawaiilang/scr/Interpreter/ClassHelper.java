package org.kawaiilang;

import java.util.ArrayList;

class ClassHelper {

  private OwObject theClass;
  private ArrayList<Token[]> classCode = new ArrayList<>();
  private KawaiiLangRuntime runtime;
  private Position pos;
  private Position start;
  private Token[] currentLine;
  
  /*
   * Creates an ClassHelper instance used for class declaration.
   * 
   * @param theClass the class being declared
   * @param runtime the runtime creating this ClassHelper instance
   */
  public ClassHelper(OwObject theClass, KawaiiLangRuntime runtime) {
	this.theClass = theClass;
	this.runtime = runtime;
	pos = runtime.getPosition().clone();
	start = pos.clone();
  }
  
  public void addCodeToClass(Token[] currentLine) {
	this.classCode.add(currentLine);
  }
  
  public void advanceLine() {
    pos.advanceLine();
  }

  private void advance() {
      pos.advance();
      if (pos.getIdx() - start.getIdx() < classCode.size()) {
          currentLine = classCode.get(pos.getIdx() - start.getIdx());
      } else {
          currentLine = null;
      }
  }

  public void setupClassCode() {
	advance();
	//Items for making constructors
	boolean makingConstructor = false;
	ArrayList<String> paramTypes = new ArrayList<>();
	ArrayList<String> paramNames = new ArrayList<>();
	ArrayList<Token[]> proc = new ArrayList<>();
	
	//Main interpretation loop
    while(currentLine != null) {
      //Start constructor
      if (currentLine.length >= 2 && currentLine[0].equals(TokenV1.C_CONSTRUCTOR) && currentLine[1].equals(TokenV1.C_PARAM) && currentLine[currentLine.length - 1].equals(TokenV1.C_PARAM)) {
    	for (int i = 2; i < currentLine.length - 1; i++) {
    	  if ((i - 1) % 3 == 1 && currentLine[i].getType().equals(TokenV1.TT_VARTYPE)) {
    		paramTypes.add((String) currentLine[i].getValue());
    	  } else if ((i - 1) % 3 == 2 && currentLine[i].getType().equals(TokenV1.TT_VARNAME)) {
    		paramNames.add((String) currentLine[i].getValue());
    	  } else if ((i - 1) % 3 == 0 && currentLine[i].equals(TokenV1.C_COMMA)) {
    		continue;
    	  } else {
    		throw new RuntimeException("Invalid syntax in constructor");
    	  }
    	}
    	makingConstructor = true;
    	//Parameterless constructor
  	  } else if (currentLine.length == 1 && currentLine[0].equals(TokenV1.C_CONSTRUCTOR)) {
  		makingConstructor = true;
  	  } else if (makingConstructor) {
  		//Currently making constructor
  		if (currentLine.length == 1 && currentLine[0] == TokenV1.C_ENDCONSTRUCTOR) {
  		  if (paramTypes.isEmpty() && paramNames.isEmpty()) {
  			theClass.addConstructor(proc);
  		  } else {
  			theClass.addConstructor(paramTypes, paramNames, proc);
  			//Resets parameters if there is any
  			paramTypes = new ArrayList<>();
    		paramNames = new ArrayList<>();
  		  }
  		  //Reset constructor procedures
  		  proc = new ArrayList<>();
  		  makingConstructor = false;
  		} else {
  		  proc.add(currentLine);
  		}
  	  } else {
  		//If no constructor just add the code to the class
  		theClass.addCode(currentLine);
  	  }
      advance();
    }
  }
  
}