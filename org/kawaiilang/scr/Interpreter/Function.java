package org.kawaiilang;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashMap;

class Function {

  private ArrayList<Token[]> actions = new ArrayList<>();
  private LinkedHashMap<String, String> parameters; // Variable name, variable type
  private Interpreter interpreter;
  private Token canGibU;

  public Function(Interpreter interpreter, Token canGibU) {
    this.interpreter = interpreter;
    this.parameters = null;
    this.canGibU = canGibU;
  }

  public Function(Interpreter interpreter, LinkedHashMap<String, String> parameters, Token canGibU) {
    this.interpreter = interpreter;
    this.parameters = parameters;
    this.canGibU = canGibU;
  }

  public void addAction(Token[] action) {
    actions.add(action);
  }

  public int parameterAmount() {
    if (parameters == null) {
      return 0;
    } else {
      return parameters.size();
    }
  }

  //Can only be used with functions with parameters
  public Object run(ArrayList<Object> inputs) {
    if (inputs.size() != parameters.size()) { // Checks if number of arguments is the same as declared
      Position start = interpreter.getPosition().clone();
      return new InvalidParameterError(start, interpreter.getPosition(),
          "Naooo uwu numwer of arwgwmwents nawt aws swame aws decwared ._.");
    }
    // Checks if the inputs types and required types match
    String[] keySet = parameters.keySet().toArray(new String[0]);
    String[] valueSet = parameters.values().toArray(new String[0]);
    for (int i = 0; i < inputs.size(); i++) {
      String inputType = "";
      if (inputs.get(i) instanceof Token) {
        Token t = (Token) inputs.get(i);
        inputType = t.type;
        if (inputType.equals(Token.TT_INT) || inputType.equals(Token.TT_FLOAT)) {
          inputType = "Numwer";
        }
      } else if (inputs.get(i) instanceof Double || inputs.get(i) instanceof Integer) {
        inputType = "Numwer";
      } // More datatypes here
      if (!inputType.equals(valueSet[i])) {
        Position start = interpreter.getPosition().clone();
        return new InvalidParameterError(start, interpreter.getPosition(),
            new StringBuilder("Naooo uwu giben awgwmwent twype, \"").append(inputType)
                .append("\", is nawt swame aws expwectwed twype, \"").append(valueSet[i]).append("\" ._.").toString());
      }
    }
    for (int anum = 0; anum < actions.size(); anum++) {
      for (int j = 0; j < inputs.size(); j++) {
        while (true) {
          int k = Arrays.asList(actions.get(anum)).indexOf(keySet[j]); // Location of inputs within action
          if (k > -1) {
            Token[] action = actions.get(anum);
            Token t = null;
            if (inputs.get(j) instanceof Double) {
              t = new Token(Token.TT_FLOAT, inputs.get(j));
            } else if (inputs.get(j) instanceof Integer) {
              t = new Token(Token.TT_INT, inputs.get(j));
            } // More datatypes here
            action[k] = t;
            actions.set(anum, action);
          } else {
            break;
          }
        } // end search for specific variable
      } // end loop through variables

      // It is now okay to run the statement
      if (actions.get(anum).length > 1
          && Arrays.asList(actions.get(anum)).get(0).equals(new Token(Token.TT_KEYWORD, "gibU"))) {
            Token[] action = actions.get(anum);
            Object o = new Runner(interpreter.getFileLocation(), interpreter).interpret(Arrays.copyOfRange(action, 1, action.length));
        if (o == null && canGibU == null) {
          return null;
        } else if (o instanceof Double) {
          String s = (String) canGibU.value;
          if (s.equals("Numwer")) {
            return o;
          } else {
            Position start = interpreter.getPosition().clone();
            return new IllegalReturnTypeError(start, interpreter.getPosition(),
                new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                    .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
          }
        } else { // More datatypes here
          Position start = interpreter.getPosition().clone();
          return new IllegalReturnTypeError(start, interpreter.getPosition(),
              new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                  .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
        }
      } else {
        new Runner(interpreter.getFileLocation(), interpreter).interpret(actions.get(anum));
      }
    } // End loop through statements
    if (canGibU == null) {
      return null;
    }
    Position start = interpreter.getPosition().clone();
    return new MissingReturnStatementError(start, interpreter.getPosition(),
        " Naooo uwu u hab no weturn stwatemwent ._.");
  }

  //Can only be used with functions without parameters
  public Object run() {
    if (parameters != null) {
      Position start = interpreter.getPosition().clone();
      return new InvalidParameterError(start, interpreter.getPosition(),
          "Naooo uwu no awgwmwent iws giwen wen de fwnctwion hwas awgwments ._.");
    }
    for (Token[] action : actions) {
      if (action.length > 1 && Arrays.asList(action).get(0).equals(new Token(Token.TT_KEYWORD, "gibU"))) {
        Object o = new Runner(interpreter.getFileLocation(), interpreter).interpret(Arrays.copyOfRange(action, 1, action.length));
        if (o == null && canGibU == null) {
          return null;
        } else if (o instanceof Double) {
          String s = (String) canGibU.value;
          if (s.equals("Numwer")) {
            return o;
          } else {
            Position start = interpreter.getPosition().clone();
            return new IllegalReturnTypeError(start, interpreter.getPosition(),
                new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                    .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
          }
        } else { // More datatypes here
          Position start = interpreter.getPosition().clone();
          return new IllegalReturnTypeError(start, interpreter.getPosition(),
              new StringBuilder("Naooo uwu giben wetwrn walue, \"").append(o)
                  .append("\", is nawt swame aws expwectwed twype, \"").append(canGibU).append("\" ._.").toString());
        }
      } else {
        new Runner(interpreter.getFileLocation(), interpreter).interpret(action);
      }
    } // End loop through statements
    if (canGibU == null) {
      return null;
    }
    Position start = interpreter.getPosition().clone();
    return new MissingReturnStatementError(start, interpreter.getPosition(),
        " Naooo uwu u hab no weturn stwatemwent ._.");
  }

}