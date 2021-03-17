package org.kawaiilang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.HashMap;

//A class for objects in KawaiiLang
class OwObject implements Cloneable {
  
  private String name;
  private OwObject parent;
  //Constructor functions to call on object initialization mapped to vartype
  private HashMap<String, Function> constructors = new HashMap<>();
  private ArrayList<Token[]> code = new ArrayList<>();	//The code of the object
  private KawaiiLangRuntime internalInterpreter = new KawaiiLangRuntime(name);
  private Runner internalRunner = new Runner(name, internalInterpreter);
  private OwObject masterClass = this;	//The class the object belongs to
  
  public void addConstructor(String vartypes, Function proc) {
	constructors.put(vartypes, proc);
  }
  
  public void addConstructor(Function proc) {
	constructors.put(null, proc);
  }
  
  /*
   * Add a constructor to the class
   * 
   * @param paramTypes an ArrayList of the parameter types
   * @param paramNames an ArrayList of the parameter names
   * @param proc the list of procedures of the constructor
   */
  public void addConstructor(ArrayList<String> paramTypes, ArrayList<String> paramNames, ArrayList<Token[]> proc) {
	LinkedHashMap<String, String> params = new LinkedHashMap<>();
	assert (paramTypes.size() == paramNames.size());
	for (int i = 0; i < paramTypes.size(); i++) {
	  params.put(paramNames.get(i), paramTypes.get(i));
	}
	Function f = new Function(internalInterpreter, name, params, TokenV1.C_RETURNCONSTRUCTEDK);
	for (Token[] action : proc) {
	  f.addAction(action);
	}
	addConstructor(f);
  }
  
  /*
   * Add a constructor to the class without any parameters
   * 
   * @param proc the list of procedures of the constructor
   */
  public void addConstructor(ArrayList<Token[]> proc) {
	Function f = new Function(internalInterpreter, name, TokenV1.C_RETURNCONSTRUCTEDK);
	for (Token[] action : proc) {
	  f.addAction(action);
	}
	addConstructor(f);
  }
  
  public OwObject construct() {
	OwObject constructed = (OwObject) this.clone();
	constructed.initCode();
	return constructed;
  }
  
  public OwObject constructWithConstructor() {
	OwObject constructed = construct();
	Function constructor = constructors.get(null);
	constructor.call();
	constructor.resetActions();
	return constructed;
  }
  
  public OwObject constructWithConstructor(ArrayList<Object> inputs) {
	OwObject constructed = construct();
	Function constructor = constructors.get(Function.toParameterTypeList(inputs));
	constructor.call(inputs);
	constructor.resetActions();
	return constructed;
  }
  
  //Does specific actions to the class
  public void doActions(ArrayList<Token[]> actions) {
	for (Token[] action : actions) {
	  internalRunner.interpret(action);
	}
  }
  
  public Object doActions(Token[] action) {
	return internalRunner.interpret(action);
  }
  
  //Initializes the class on construction according to its code
  public void initCode() {
	for (Token[] s : getAllCode()) {
	  internalRunner.interpret(s);
	}
  }
  
  public void addCode(Token[] code) {
	this.code.add(code);
  }
  
  public ArrayList<Token[]> getCode() {
	return code;
  }
  
  //Returns all code, including inherited code
  public ArrayList<Token[]> getAllCode() {
	if (parent == null) {
	  return code;
	} else {
	  ArrayList<Token[]> inherited = parent.getAllCode();
	  ArrayList<Token[]> allCode = new ArrayList<>();
	  allCode.addAll(inherited);
	  allCode.addAll(code);
	  return allCode;
	}
  }
  
  //Checks if any class is the parent of this class
  public boolean hasParent(String className) {
	if (parent == null) {
	  return false;
	} else {
	  if (parent.name.equals(className)) {
		return true;
	  } else {
		return parent.hasParent(className);
	  }
	}
  }
  
  public boolean equals(Object o) {
	if (o instanceof OwObject) {
	  OwObject owoo = (OwObject) o;
	  return name.equals(owoo.name);
	} else {
	  return false;
	}
  }
  
  public int hashCode() {
	return name.hashCode();
  }
  
  //Returns the instance of the master class, for purposes of running static code
  public OwObject getMasterClass() {
	return masterClass;
  }
  
  public OwObject(String name) {
	this.name = name;
	this.parent = null;
  }

  public OwObject(String name, OwObject parent) {
	this.name = name;
	this.parent = parent;
  }
  
  public String className() {
	return name;
  }
  
  public String shortName() {
	//The following regular expression code splits the name off at the last '/' or '\' character
	String[] sa = name.split("(\\/(?!.*\\/)|\\\\(?!.*\\\\))(?!.*(\\/(?!.*\\/)|\\\\(?!.*\\\\)))");
	if (sa.length == 2) {
	  return sa[1];
	}
	return sa[0];
  }
  
  public Object clone() {
	OwObject clone = new OwObject(name, parent);
	clone.masterClass = this;
	return clone;
  }
  
}