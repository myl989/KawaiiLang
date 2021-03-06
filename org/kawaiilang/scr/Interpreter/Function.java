package org.kawaiilang;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import static org.kawaiilang.KawaiiLangRuntime.verifyFunction;

public final class Function extends AbstractFunction {

  private ArrayList<Token[]> actions = new ArrayList<>();
  private ArrayList<Token[]> orig = new ArrayList<>();
  private int functionDepth = 0;  //Number of functions nested inside, used to calculate which return values to ignore.

  public Function(KawaiiLangRuntime runtime, String name, Token canGibU) {
    super(runtime, name, canGibU);
  }

  public Function(KawaiiLangRuntime runtime, String name, LinkedHashMap<String, String> parameters, Token canGibU) {
    super(runtime, name, parameters, canGibU);
  }

  public void addAction(Token[] action) {
    actions.add(action);
    orig.add(TokenV1.cloneTokenArray(action));
  }

  //Can only be used with functions with parameters
  @Override
  public Object call(ArrayList<Object> inputs) {
    if (inputs.size() != parameters.size()) { // Checks if number of arguments is the same as declared
      Position start = runtime.getPosition().clone();
      return new InvalidParameterError(runtime.getPosition(),
          "Naooo uwu numwer of arwgwmwents nawt aws swame aws decwared ._.");
    }
    // Checks if the inputs types and required types match
    String[] keySet = parameters.keySet().toArray(new String[0]);
    String[] valueSet = parameters.values().toArray(new String[0]);
    for (int i = 0; i < inputs.size(); i++) {
      String inputType = "";
      //The below code probably is unnecessary as action would probably not be in the inputs
      /*if (inputs.get(i) instanceof Token) {
        Token t = (Token) inputs.get(i);
        inputType = t.type;
        if (inputType.equals(TokenV1.TT_INT) || inputType.equals(TokenV1.TT_FLOAT)) {
          inputType = "Numwer";
        }
      } else*/ if (inputs.get(i) instanceof Double || inputs.get(i) instanceof Integer) {
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
    for (int anum = 0; anum < actions.size(); anum++) {
      for (int j = 0; j < inputs.size(); j++) {
        while (true) {
          int k = Arrays.asList(actions.get(anum)).indexOf(new TokenV1(TokenV1.TT_VARNAME, keySet[j])); // Location of inputs within action
          //System.out.println(k);
          if (k > -1) {
            Token[] action = actions.get(anum);
            Token t = null;
            if (inputs.get(j) instanceof Double) {
              t = new TokenV1(TokenV1.TT_FLOAT, inputs.get(j));
            } else if (inputs.get(j) instanceof Integer) {
              t = new TokenV1(TokenV1.TT_INT, inputs.get(j));
            } else if (inputs.get(j) instanceof String) {
              t = new TokenV1(TokenV1.TT_STR, inputs.get(j));
            } else if (inputs.get(j) instanceof Function) {
              Function retrievedFunc = (Function) inputs.get(j);
              t = new TokenV1(TokenV1.TT_VARNAME, retrievedFunc.getName());
            } else if (inputs.get(j) instanceof OwOList) {
              t = new TokenV1(TokenV1.TT_LIST, inputs.get(j));
            } else if (inputs.get(j) == null) {
              t = new TokenV1(TokenV1.TT_NOTHING);
            }
            // More datatypes here
            action[k] = t;
            actions.set(anum, action);
          } else {
            break;
          }
        } // end search for specific variable
      } // end loop through variables

      // It is now okay to run the statement
      Token[] action = actions.get(anum);
      if (action.length > 6 && action[0].equals(TokenV1.C_OWOK) && action[1].equals(TokenV1.C_FUNCK)
          && action[2].getType().equals(TokenV1.TT_VARNAME) && action[3].equals(TokenV1.C_PARAM)
          && action[action.length - 3].equals(TokenV1.C_PARAM)
          && action[action.length - 2].equals(new TokenV1(TokenV1.TT_KEYWORD, "canGibU"))
          && (action[action.length - 1].getType().equals(TokenV1.TT_VARTYPE)
              || action[action.length - 1].getType().equals(TokenV1.TT_NOTHING))) {
        if (verifyFunction(action)) {
          functionDepth++;
        }
      } else if (action.length == 1 && action[0].equals(TokenV1.C_ENDFUNCK)) {
        functionDepth = functionDepth == 0 ? 0 : functionDepth - 1;
      }

      if (actions.get(anum).length > 1 && Arrays.asList(actions.get(anum)).get(0).equals(TokenV1.C_RETURNK) && (functionDepth == 0)) {	//Return values
          Object o = new Runner(runtime.getFileLocation(), runtime).interpret(Arrays.copyOfRange(action, 1, action.length));
        if (o == null && canGibU.equals(TokenV1.C_NOTHING)) {
          return null;
        } else if (canGibU.equals(TokenV1.C_ANYVT)) {	//Any return value
          return o;
        } else if (o instanceof Double) {
          String s = (String) canGibU.getValue();
          if (s.equals("Numwer")) {
            return o;
          } else {
            Position start = runtime.getPosition().clone();
            return new IllegalReturnTypeError(runtime.getPosition(),
                new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                    .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
          }
        } else if (o instanceof String) {
          String s = (String) canGibU.getValue();
          if (s.equals("Stwing")) {
            return o;
          } else {
            Position start = runtime.getPosition().clone();
            return new IllegalReturnTypeError(runtime.getPosition(),
                new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                    .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
          }
        } else if (o instanceof Function) {
          String s = (String) canGibU.getValue();
          if (s.equals("Fwnctwion")) {
            return o;
          } else {
            Position start = runtime.getPosition().clone();
            return new IllegalReturnTypeError(runtime.getPosition(),
                new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                    .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
          }
        } else if (o instanceof OwOList) {
          	String s = (String) canGibU.getValue();
          if (s.equals("Lwist")) {
            return o;
          } else {
            Position start = runtime.getPosition().clone();
            return new IllegalReturnTypeError(runtime.getPosition(),
                    new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                        .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
              }
        } else { // More datatypes here
          Position start = runtime.getPosition().clone();
          return new IllegalReturnTypeError(runtime.getPosition(),
              new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                  .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
        }
      } else if (actions.get(anum).length > 1 && Arrays.asList(actions.get(anum)).get(0).equals(TokenV1.C_RETURNCONSTRUCTEDK) && (functionDepth == 0)) {
    	 if (canGibU.equals(TokenV1.C_RETURNCONSTRUCTEDK)) {
    	   return null;
    	 } else {
    	   Position start = runtime.getPosition().clone();
    	   return new IllegalReturnTypeError(runtime.getPosition(), "Naooo uwu onlwy constwctwrs can hab finishMaking");
    	 }
      } else {
        new Runner(runtime.getFileLocation(), runtime).interpret(actions.get(anum));
      }
    } // End loop through statements
    if (canGibU == null) {
      return null;
    }
    //The only reason you are still here is because there is no return value.
    Position start = runtime.getPosition().clone();
    return new MissingReturnStatementError(runtime.getPosition(),
        " Naooo uwu u hab no weturn stwatemwent ._.");
  }

  //Can only be used with functions without parameters
  @Override
  public Object call() {
    if (parameters != null) {
      Position start = runtime.getPosition().clone();
      return new InvalidParameterError(runtime.getPosition(),
          "Naooo uwu awgwmwents r giwen wen de fwnctwion does nawt hwas awgwments ._.");
    }
    for (Token[] action : actions) {
      if (action.length > 6 && action[0].equals(TokenV1.C_OWOK) && action[1].equals(TokenV1.C_FUNCK)
          && action[2].getType().equals(TokenV1.TT_VARNAME) && action[3].equals(TokenV1.C_PARAM)
          && action[action.length - 3].equals(TokenV1.C_PARAM)
          && action[action.length - 2].equals(new TokenV1(TokenV1.TT_KEYWORD, "canGibU"))
          && (action[action.length - 1].getType().equals(TokenV1.TT_VARTYPE)
              || action[action.length - 1].getType().equals(TokenV1.TT_NOTHING))) {
        if (verifyFunction(action)) {
          functionDepth++;
        }
      } else if (action.length == 1 && action[0].equals(TokenV1.C_ENDFUNCK)) {
        functionDepth = functionDepth == 0 ? 0 : functionDepth - 1;
      }

      if (action.length > 1 && Arrays.asList(action).get(0).equals(TokenV1.C_RETURNK) && (functionDepth == 0)) {	//Return values
        Object o = new Runner(runtime.getFileLocation(), runtime).interpret(Arrays.copyOfRange(action, 1, action.length));
        if (o == null && canGibU.equals(TokenV1.C_NOTHING)) {
          return null;
        } else if (o instanceof Double) {
          String s = (String) canGibU.getValue();
          if (s.equals("Numwer")) {
            return o;
          } else {
            Position start = runtime.getPosition().clone();
            return new IllegalReturnTypeError(runtime.getPosition(),
                new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                    .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
          }
        } else if (o instanceof String) {
          String s = (String) canGibU.getValue();
          if (s.equals("Stwing")) {
            return o;
          } else {
            Position start = runtime.getPosition().clone();
            return new IllegalReturnTypeError(runtime.getPosition(),
                new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                    .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
          }
        } else if (o instanceof Function) {
          String s = (String) canGibU.getValue();
          if (s.equals("Fwnctwion")) {
            return o;
          } else {
            Position start = runtime.getPosition().clone();
            return new IllegalReturnTypeError(runtime.getPosition(),
                new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                    .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
          }
        } else if (o instanceof OwOList) {
        	String s = (String) canGibU.getValue();
            if (s.equals("Lwist")) {
              return o;
            } else {
              Position start = runtime.getPosition().clone();
              return new IllegalReturnTypeError(runtime.getPosition(),
                  new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                      .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
            }
        } else { // More datatypes here
          Position start = runtime.getPosition().clone();
          return new IllegalReturnTypeError(runtime.getPosition(),
              new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                  .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
        }
      } else if (action.length > 1 && Arrays.asList(action).get(0).equals(TokenV1.C_RETURNCONSTRUCTEDK) && (functionDepth == 0)) {
    	if (canGibU.equals(TokenV1.C_RETURNCONSTRUCTEDK)) {
   	 		return null;
    	} else {
	   		Position start = runtime.getPosition().clone();
	   		return new IllegalReturnTypeError(runtime.getPosition(), "Naooo uwu onlwy constwctwrs can hab finishMaking");
    	}
     } else {
        new Runner(runtime.getFileLocation(), runtime).interpret(action);
      }
    } // End loop through statements
    if (canGibU == null) {
      return null;
    }
    Position start = runtime.getPosition().clone();
    return new MissingReturnStatementError(runtime.getPosition(),
        " Naooo uwu u hab no weturn stwatemwent ._.");
  }

  public void resetActions() {  //Resets the actions to remove the substituted parameters
    actions = (ArrayList<Token[]>) orig.clone();
  }
  
  /*
   * Takes a list of objects and converts their respective class to their respective KawaiiLang varnames,
   * with Double becoming Numwer, String becoming Stwing, etc.
   * Each parameter is seperated by an underscore, with a trailing one at the end, for example [0.1, "hi"] would become "Numwer_Stwing_"
   * 
   * @param alo the list of parameter inputs
   * @return the parameter type list converted to a String form
   * 
   */
  public static String toParameterTypeList(Collection<?> alo) {
	if (alo == null) {
	  return null;
	}
	StringBuilder sb = new StringBuilder();
	  for (Object input : alo) {
		  if (input instanceof Double) {
			sb.append("Numwer_");
		  } else if (input instanceof String) {
			sb.append("Stwing_");
		  } else if (input instanceof Function) {
			sb.append("Fwnctwion_");
		  } else if (input instanceof OwOList) {
			sb.append("Lwist_");
		  } else if (input == null) {
			sb.append("nwthin_");
		  }
		  //More datatypes here
	}
	return sb.toString();
  }
  
  /*
   * Returns the type of the object
   * 
   * @param parameter the object to find the type of
   * @return the name of the type of object
   */
  public static String toParameterName(Object parameter) {
	if (parameter instanceof Double || parameter instanceof Integer) {
      return "Numwer";
    } else if (parameter instanceof String) {
      return "Stwing";
    } else if (parameter instanceof Function) {
      return "Fwnctwion";
    } else if (parameter instanceof OwOList) {
      return "Lwist";
    } else if (parameter == null) {
      return "nwthin";
    } else {
      return "";	//Parameter not found
    }
  }

}