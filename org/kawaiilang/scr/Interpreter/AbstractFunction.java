package org.kawaiilang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public abstract class AbstractFunction {

  protected String name;
  protected LinkedHashMap<String, String> parameters; // Variable name, variable type
  protected KawaiiLangRuntime runtime;
  protected Token canGibU;

  public AbstractFunction(KawaiiLangRuntime runtime, String name, Token canGibU) {
    this.runtime = runtime;
    this.name = name;
    this.parameters = null;
    this.canGibU = canGibU;
  }

  public AbstractFunction(KawaiiLangRuntime runtime, String name, LinkedHashMap<String, String> parameters, Token canGibU) {
    this.runtime = runtime;
    this.name = name;
    this.parameters = parameters; // Parameter linked hash map with data Varname, Vartype
    this.canGibU = canGibU;
  }

  public Object call(ArrayList<Object> inputs) {
    throw new UnsupportedOperationException("This function has not been defined.");
  }

  public Object call() {
    throw new UnsupportedOperationException("This function has not been defined.");
  }

  public Collection<String> getParameterTypes() {
    if (parameters == null) {
      return null;
    } else {
      return parameters.values();
    }
  }

  public int parameterAmount() {
    if (parameters == null) {
      return 0;
    } else {
      return parameters.size();
    }
  }

  public String getName() {
    return name;
  }
  
  protected KawaiiLangRuntime getInterpreter() {
	  return runtime;
  }

  //Check whether the input types match the parameter types
  //Returns null if no errors, or an error if there is a type mismatch
  public Object checkInputs(ArrayList<Object> inputs) {
	String[] valueSet = parameters.values().toArray(new String[0]);
	//System.out.println(Arrays.toString(valueSet));
    for (int i = 0; i < inputs.size(); i++) {
      String inputType = "";
      if (inputs.get(i) instanceof Double || inputs.get(i) instanceof Integer) {
        inputType = "Numwer";
      } else if (inputs.get(i) instanceof String) {
        inputType = "Stwing";
      } else if (inputs.get(i) instanceof Function) {
        inputType = "Fwnctwion";
      } else if (inputs.get(i) instanceof OwOList) {
    	inputType = "Lwist";
      } else if (inputs.get(i) == null) {
    	inputType = "nwthin";
      }
      //More datatypes here
      if (!inputType.equals(valueSet[i]) && !valueSet[i].equals("Anwy")) {	//Parameter type of Anwy accepts any input
    	Position start = runtime.getPosition().clone();
    	return new InvalidParameterError(runtime.getPosition(),
        new StringBuilder("Naooo uwu giben awgwmwent twype, \"").append(inputType)
                .append("\", is nawt swame aws expwectwed twype, \"").append(valueSet[i]).append("\" ._.").toString());
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return new StringBuilder("<Function ").append(name).append(" from ").append(runtime.getFileLocation()).append('>').toString();
  }

}
