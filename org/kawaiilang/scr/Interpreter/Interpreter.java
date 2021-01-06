package org.kawaiilang;
import java.util.Arrays;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.ScriptEngine;

public class Interpreter {

  private Token[] tokens;
  private Position pos;
  private Token currentToken;
  private String fn;
  private static final ScriptEngineManager SENGMAN = new ScriptEngineManager();
  private static final ScriptEngine SENG = SENGMAN.getEngineByName("js");

  public Interpreter(String fileName, Token[] tokens) {
    this.tokens = tokens;
    this.fn = fileName;
    pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
    advance();
  }

  public void advance() {
    pos.advance();
    if (pos.getIdx() < tokens.length) {
      currentToken = tokens[pos.getIdx()];
    } else {
      currentToken = null;
    }
  }

  public Object interpret() {
    StringBuilder expr = new StringBuilder();
    Token lastToken = null;
    
    while (true) {
      if (currentToken == null) {
        break;
      } else if (currentToken instanceof org.kawaiilang.Error) {
        return currentToken;
      } else if (currentToken.type == Token.TT_INT || currentToken.type == Token.TT_FLOAT) {
        expr.append(currentToken.value);
        lastToken = currentToken;
        advance();
      } else if (lastToken == null) {
        if (currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV) {
          expr.append(0).append(currentToken.type);
          lastToken = currentToken;
          advance();
        } else if (currentToken.type == Token.TT_LPAREN) {
          expr.append("(");
          lastToken = currentToken;
          advance();
        } else {
          //error
        }
      } else if (lastToken.type == Token.TT_INT) {
        if (currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV) {
          expr.append(currentToken.type);
          lastToken = currentToken;
          advance();
        } else {
          //sometimes error?
        }
      }
    }
    if (expr.length() > 0) {
      try {
        Object result = SENG.eval(expr.toString());
        return result;
      } catch (ScriptException ex) {
        Position start = pos.clone();
        return new InvalidSyntaxError(start, pos, ex.getMessage());
      }
    } else {
      return null;
    }
  }

}