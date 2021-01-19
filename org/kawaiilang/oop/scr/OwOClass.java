package org.kawaiilang.oop;
import java.util.ArrayList;
import java.util.HashMap;
import org.kawaiilang.*;

class OwOClass {

  //The base type of class in kawaiilang
  
  private ArrayList<Token> properties;
  private HashMap<String, Function> functions;
  private String className;

  public OwOClass(String className) {
    this.className = className;
  }

  public void addProperty(Token t) {
    if (t.getType().equals(Token.TT_VARTYPE)) {
      properties.add(t);
    } else {
      //error
    }
  }

  public void addFunction(String funcName, Function func) {
    functions.put(funcName, func);
  }

}