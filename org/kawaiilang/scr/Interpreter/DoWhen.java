package org.kawaiilang;

import java.util.Arrays;
import java.util.ArrayList;

class DoWhen implements LoopInterface {
  
  private Interpreter interpreter;
  private ArrayList<Token[]> actions = new ArrayList<>();
  private ArrayList<Byte> needsToBeStopped = new ArrayList<>(); //These types of actions needs top be stopped with an ^_^ statement before breaking or errors may occur.
  //These actions are numbered as follows:
  //NOTICE (if): 0
  //DO or DOWHEN (for or while): 1
  private Token[] condition;

  public DoWhen(Interpreter interpreter, Token[] condition) {
    this.interpreter = interpreter;
    this.condition = condition;
  }

  public void addAction(Token[] action) {
    actions.add(action);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (Token[] action : actions) {
      sb.append(Arrays.toString(action));
    }
    return sb.append("]").toString();
  }

  public void loop() {
    Object theCond = new Runner(interpreter.getFileLocation(), interpreter).interpret(condition);
    if (theCond instanceof Double) {
      int i = ((Double) theCond).intValue();
      if (i > 0) {
        outer:
        while (i > 0) {
          for (int j = 0; j < actions.size(); j++) {
            Token[] action = actions.get(j);
            if ((action.length > 0 && action[0].equals(new Token(Token.TT_KEYWORD, "do")) && action[action.length - 1].equals(new Token(Token.TT_KEYWORD, "twimes"))) || (action.length > 2 && action[0].equals(new Token(Token.TT_KEYWORD, "doWen")) && action[1].equals(new Token(Token.TT_STARTIF)) && action[action.length - 1].equals(new Token(Token.TT_ENDIF)))) {
              needsToBeStopped.add((byte)1);
            } else if (action.length > 0 && action[0].equals(new Token(Token.TT_STARTIF))) {
              needsToBeStopped.add((byte)0);
            } else if (action.length == 1 && action[0].equals(new Token(Token.TT_KEYWORD, "^_^wepeatDat"))) {
              needsToBeStopped.remove(Byte.valueOf((byte)1));
            } else if (action.length == 1 && action[0].equals(new Token(Token.TT_KEYWORD, "^_^ewndNotice"))) {
              needsToBeStopped.remove(Byte.valueOf((byte)0));
            }
            Object o = new Runner(interpreter.getFileLocation(), interpreter).interpret(action);
            if (o instanceof Token) {
              Token t = (Token) o;
              if (t.equals(new Token(Token.TT_KEYWORD, "stawp"))) {
                for (Byte b : needsToBeStopped) { //Checks for any actions that needs top be stopped
                  Token[] end = new Token[1];
                  switch(b) {
                    case (byte)0:
                      end[0] = new Token(Token.TT_KEYWORD, "^_^ewndNotice");
                      new Runner(interpreter.getFileLocation(), interpreter).interpret(end);
                      break outer;
                    case (byte)1:
                      end[0] = new Token(Token.TT_KEYWORD, "^_^wepeatDat");
                      new Runner(interpreter.getFileLocation(), interpreter).interpret(end);
                      break outer;
                  }
                }
                break outer;
              }
            }
          }
          //Update condition
          theCond = new Runner(interpreter.getFileLocation(), interpreter).interpret(condition);
          if (theCond instanceof Double) {
            i = ((Double) theCond).intValue();
          } else {
            break outer;
          }
        } //End while loop
      }
    }
  }

}