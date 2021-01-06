package org.kawaiilang;
import java.util.Arrays;
import java.util.HashMap;
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
  private HashMap<Token, HashMap<Token, Object>> heap = new HashMap<>();

  public Interpreter(String fileName, Token[] tokens) {
    this.fn = fileName;
    setTokens(tokens);
  }

  public Interpreter(String fileName) {
    this.fn = fileName;
  }

  public void setFileName(String fileName) {
    this.fn = fileName;
  }

  public void setTokens(Token[] tokens) {
    this.tokens = tokens;
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

    System.out.println(heap);
    
    while (true) {
      if (currentToken == null) {
        break;
      } else if (false) { //new line?
        lastToken = null;
        advance();
      } else if (currentToken instanceof org.kawaiilang.Error) {
        return currentToken;
      } else if (currentToken.type == Token.TT_INT || currentToken.type == Token.TT_FLOAT) {
        expr.append(currentToken.value);
        lastToken = currentToken;
        advance();
      } else if (lastToken == null) {
        if (currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV || currentToken.type == Token.TT_MOD) {
          expr.append(0).append(currentToken.type);
          lastToken = currentToken;
          advance();
        } else if (currentToken.type == Token.TT_LPAREN) {
          expr.append("(");
          lastToken = currentToken;
          advance();
        } else if (currentToken.type == Token.TT_VARNAME) {
          if (heap.keySet().toArray(new Token[0])[0].equals(currentToken)) { //this part is broken, fix!
            int idx = pos.getIdx();
            HashMap<Token, Object> varData = heap.get(currentToken);
            Token type = varData.keySet().toArray(new Token[0])[0];
            Object stored = varData.get(type);
            Token varReplaced = null;
            if (type.type == Token.TT_INT) {
              if (stored instanceof Integer) {
                varReplaced = new Token(Token.TT_INT, stored);
              } else {
                Position start = pos.clone();
                return new IllegalTypeError(start, pos, new StringBuilder("Type of variable, ").append(type.type).append(", is not as declared").toString());
              }
            } else if (type.type == Token.TT_FLOAT) {
              if (stored instanceof Double) {
                varReplaced = new Token(Token.TT_FLOAT, stored);
              } else {
                Position start = pos.clone();
                return new IllegalTypeError(start, pos, new StringBuilder("Type of variable, ").append(type.type).append(", is not as declared").toString());
              }
            }
            tokens[pos.getIdx()] = varReplaced;
            pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
            advance();
            Object value = interpret();
            return value;
          } else {
            String varName = (String) currentToken.value;
            Position start = pos.clone();
            return new UnassignedVariableError(start, pos, new StringBuilder("Variable ").append(varName).append(" never assigned").toString());
          }
        } else if (currentToken.type == Token.TT_VARTYPE) {
          Token t = currentToken;
          advance();
          if (currentToken.type == Token.TT_VARNAME) {
            lastToken = currentToken;
            advance();
            if (currentToken.type == Token.TT_ASSIGN) {
              advance();
              int idx = pos.getIdx();
              Object value = Runner.interpret(Arrays.copyOfRange(tokens, idx, tokens.length));
              if (value instanceof org.kawaiilang.Error) {
                return value;
              } else {
                HashMap<Token, Object> miniMap = new HashMap<>();
                miniMap.put(t, value);
                heap.put(lastToken, miniMap);
                return value;
              }
            } else {
              Position start = pos.clone();
              String varName = (String) lastToken.value;
              return new UnassignedVariableError(start, pos, new StringBuilder("Variable").append(varName).append(" declared but no value was assigned").toString());
            }
          } else {
            Position start = pos.clone();
            return new InvalidSyntaxError(start, pos, new StringBuilder(currentToken.toString()).append(" is illegal here").toString());
          }
        } else {
          Position start = pos.clone();
          return new InvalidSyntaxError(start, pos, new StringBuilder(currentToken.toString()).append(" is illegal here").toString());
        }
      } else if (lastToken.type == Token.TT_INT) {
        if (currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV || currentToken.type == Token.TT_MOD) {
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