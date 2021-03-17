package org.kawaiilang;
import java.util.ArrayList;
import java.util.Arrays;

class Loop implements LoopInterface {

  private int idx = 0;
  private int max = 0;
  private KawaiiLangRuntime runtime;
  private ArrayList<Token[]> actions = new ArrayList<>();
  private ArrayList<Byte> needsToBeStopped = new ArrayList<>();    //These types of actions needs top be stopped with an ^_^ statement before breaking or errors may occur.
  //These actions are numbered as follows:
  //NOTICE (if): 0
  //DO or DOWHEN (for or while): 1
  
  public Loop(KawaiiLangRuntime runtime, int max) {
    this.runtime = runtime;
    if (max > 0) {
      this.max = max;
    }
  }

  public Loop(KawaiiLangRuntime runtime, int idx, int max) {
    this.runtime = runtime;
    this.idx = idx;
    this.max = max;
  }
  
  public void addAction(Token[] action) {
    actions.add(action);
  }

  public void loop() {
    //System.out.println(this);
    outer:
    for (; idx < max; idx++) {
      for (int j = 0; j < actions.size(); j++) {
        Token[] action = actions.get(j);
        if ((action.length > 0 && action[0].equals(TokenV1.C_FORK) && action[action.length - 1].equals(TokenV1.C_TIMESK)) || (action.length > 2 && action[0].equals(TokenV1.C_WHILEK) && action[1].equals(TokenV1.C_IFK) && action[action.length - 1].equals(TokenV1.C_ENDIFK))) {
          needsToBeStopped.add((byte)1);
        } else if (action.length > 0 && action[0].equals(TokenV1.C_IFK)) {
          needsToBeStopped.add((byte)0);
        } else if (action.length == 1 && action[0].equals(TokenV1.C_ENDLOOPK)) {
          needsToBeStopped.remove(Byte.valueOf((byte)1));
        } else if (action.length == 1 && action[0].equals(TokenV1.C_ENDIFK)) {
          needsToBeStopped.remove(Byte.valueOf((byte)0));
        }
        Object o = new Runner(runtime.getFileLocation(), runtime).interpret(action);
        if (o instanceof Token) {
          Token t = (Token) o;
          if (t.equals(TokenV1.C_BREAKK)) {
            for (Byte b : needsToBeStopped) { //Checks for any actions that needs top be stopped
              Token[] end = new TokenV1[1];
              switch(b) {
                case (byte)0:
                  end[0] = new TokenV1(TokenV1.TT_KEYWORD, "^_^ewndNotice");
                  new Runner(runtime.getFileLocation(), runtime).interpret(end);
                  break outer;
                case (byte)1:
                  end[0] = new TokenV1(TokenV1.TT_KEYWORD, "^_^wepeatDat");
                  new Runner(runtime.getFileLocation(), runtime).interpret(end);
                  break outer;
              }
            }
            break outer;
          }
        }
      } //End runActions
    } //End loopActions
  }

  public int getIdx() {
    return idx;
  }

  public void setIdx(int newIdx) {
    idx = newIdx;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int newMax) {
    max = newMax;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    for (Token[] action : actions) {
      sb.append(Arrays.toString(action));
    }
    return sb.append("]").toString();
  }

}