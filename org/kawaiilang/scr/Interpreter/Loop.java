package org.kawaiilang;
import java.util.ArrayList;

class Loop {

  private int idx = 0;
  private int max = 0;
  private Interpreter interpreter;
  private ArrayList<Token[]> actions = new ArrayList<>();
  
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
    for (int i = idx; i < max; i++) {
      for (Token[] action : actions) {
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

}