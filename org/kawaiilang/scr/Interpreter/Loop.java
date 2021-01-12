package org.kawaiilang;
import java.util.ArrayList;
import java.util.Arrays;

class Loop {

  private int idx = 0;
  private int max = 0;
  private Interpreter interpreter;
  private ArrayList<Token[]> actions = new ArrayList<>();
  private ArrayList<Byte> needsToBeStopped = new ArrayList<>();    //These types of actions needs top be stopped with an ^_^ statement before breaking or errors may occur.
  //These actions are numbered as follows:
  //NOTICE (if): 0
  //DO (for): 1
  
  public Loop(Interpreter interpreter, int max) {
    this.interpreter = interpreter;
    if (max > 0) {
      this.max = max;
    }
  }

  public Loop(Interpreter interpreter, int idx, int max) {
    this.interpreter = interpreter;
    this.idx = idx;
    this.max = max;
  }
  
  public void addAction(Token[] action) {
    actions.add(action);
  }

  public void loop() {
    System.out.println(this);
    outer:
    for (int i = idx; i < max; i++) {
      for (int j = 0; j < actions.size(); j++) {
        Token[] action = actions.get(j);
        if (action.length > 0 && action[0].equals(new Token(Token.TT_KEYWORD, "do")) && action[action.length - 1].equals(new Token(Token.TT_KEYWORD, "twimes"))) {
          needsToBeStopped.add((byte)1);
        } else if (action.length > 0 && action[0].equals(new Token(Token.TT_STARTIF))) {
          needsToBeStopped.add((byte)0);
        } else if (action.length == 1 && action[0].equals(new Token(Token.TT_KEYWORD, "^_^wepeatDat"))) {
          needsToBeStopped.remove(Byte.valueOf((byte)1));
        } else if (action.length == 1 && action[0].equals(new Token(Token.TT_KEYWORD, "^_^ewndNotice"))) {
          needsToBeStopped.remove(Byte.valueOf((byte)0));
        } else if (action.length == 1 && action[0].equals(new Token(Token.TT_KEYWORD, "stawp"))) {
          //Todo: fix bug
          //Breaks loop regardless of if statements, etc.
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
        new Runner(interpreter.getFileLocation(), interpreter).interpret(action);
      }
      idx++;
    }
  }

  public int getIndex() {
    return idx;
  }

  public void setIndex(int newIdx) {
    idx = newIdx;
  }

  //I don't think toString() works
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (Token[] action : actions) {
      sb.append(Arrays.toString(action));
    }
    return sb.append("]").toString();
  }

}