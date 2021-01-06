package org.kawaiilang;

public class Position implements Cloneable {

  private int idx;
  private int ln;
  private int col;
  private String fn;
  private String ftxt;

  public Position(int idx, int ln, int col, String fn, String ftxt) {
    this.idx = idx;
    this.ln = ln;
    this.col = col;
    this.fn = fn;
    this.ftxt = ftxt;
  }

  public Position advance(char currentChar) {
    idx++;
    col++;
    if (currentChar == '\n') {
      ln++;
      col = 0;
    }
    return this;
  }

  public Position advance() {
    idx++;
    col++;
    return this;
  }

  public Position clone() {
    return new Position(idx, ln, col, fn, ftxt);
  }

  public int getIdx() {
    return idx;
  }

  public int getLine() {
    return ln;
  }

  public String getFileName() {
    return fn;
  }

}