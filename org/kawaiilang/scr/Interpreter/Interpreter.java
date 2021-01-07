package org.kawaiilang;
import java.util.Arrays;
import java.util.HashMap;

//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//import javax.script.ScriptEngine;
import net.objecthunter.exp4j.*;

public class Interpreter {

  private Token[] tokens;
  private Position pos;
  private Token currentToken;
  private String fn;
  //private static final ScriptEngineManager SENGMAN = new ScriptEngineManager();
  //private static final ScriptEngine SENG = SENGMAN.getEngineByName("js");
  private HashMap<Token, Variable> heap = new HashMap<>();

  public Interpreter(String fileName, Token[] tokens) {
    this.fn = fileName;
    setTokens(tokens);
  }

  public Interpreter(String fileName) {
    this.fn = fileName;
  }

  public void setFileLocation(String fileLocation) {
    this.fn = fileLocation;
  }

  public void setTokens(Token[] tokens) {
    this.tokens = tokens;
    pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
    advance();
  }

  private void advance() {
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

    //System.out.println(heap);
    //System.out.println(heap.size());
    
    while (true) {
      if (currentToken == null) {
        break;
      } /*else if (???) { //new line?
        lastToken = null;
        advance();
      }*/ else if (currentToken instanceof org.kawaiilang.Error) {
        return currentToken;
      } else if (currentToken.type == Token.TT_INT || currentToken.type == Token.TT_FLOAT) {
        expr.append(currentToken.value);
        lastToken = currentToken;
        advance();
      } else if (currentToken.type == Token.TT_VARNAME) {
          if (pos.getIdx() + 1 < tokens.length && tokens[pos.getIdx() + 1].type == Token.TT_ASSIGN && heap.containsKey(currentToken)) {
            lastToken = currentToken;
            advance();
            int idx = pos.getIdx();
            Object value = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, idx + 1, tokens.length));
            if (value instanceof org.kawaiilang.Error) {
              return value;
            } else {
              currentToken = lastToken;
              Variable var = new Variable(heap.get(currentToken).getType(), value);
              heap.put(currentToken, var);
              return value;
            }
          }
          int idx = pos.getIdx();
          Variable varData = heap.get(currentToken);
          if (varData != null) {
          Token type = varData.getType();
          Object stored = varData.getValue();
          Token varReplaced = null;
          if (type.value.equals("Numwer")) {
            if (stored instanceof Integer) {
              varReplaced = new Token(Token.TT_INT, stored);
            } else if (stored instanceof Double) {
              varReplaced = new Token(Token.TT_FLOAT, stored);
            } else {
              Position start = pos.clone();
              return new IllegalTypeError(start, pos, new StringBuilder("Twype of variable, ").append(type.type).append(", is nawt as decwared ._.").toString());
            }
          } //More variable types in the future
            tokens[pos.getIdx()] = varReplaced;
            pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
            advance();
            Object value = interpret();
            return value;
          } else {
            String varName = (String) currentToken.value;
            Position start = pos.clone();
            return new UnassignedVariableError(start, pos, new StringBuilder("Wariable ").append(varName).append(" newer asswigned ._.").toString());
          }
        } else if (currentToken.type == Token.TT_LPAREN) {
          expr.append("(");
          lastToken = currentToken;
          advance();
        } else if (currentToken.type == Token.TT_RPAREN) {
          expr.append(")");
          lastToken = currentToken;
          advance();
        } else if (lastToken == null || currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV || currentToken.type == Token.TT_MOD) {
          if (lastToken != null && (lastToken.type == Token.TT_ADD || lastToken.type == Token.TT_MINUS)) {
            if (currentToken.type == Token.TT_ADD) {
              //adding a plus before numbers doesn't change anything
              lastToken = currentToken;
              advance();
            } else if (currentToken.type == Token.TT_MINUS) {
              //adding a minus before numbers makes negative positive and positive negative
              if (lastToken.type == Token.TT_ADD) {
                expr.deleteCharAt(expr.length() - 1);
                expr.append("-");
                lastToken = new Token(Token.TT_MINUS);
                advance();
              } else {
                expr.deleteCharAt(expr.length() - 1);
                expr.append("+");
                lastToken = new Token(Token.TT_ADD);
                advance();
              }
            } else {
              Position start = pos.clone();
              return new InvalidSyntaxError(start, pos, new StringBuilder(currentToken.toString()).append(" cwannot cwome awfter awnowther owpewation ._.").toString());
            }
          } else if (currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV || currentToken.type == Token.TT_MOD) {
            if (lastToken != null && (lastToken.type != Token.TT_INT && lastToken.type != Token.TT_FLOAT)) {
              expr.append("0").append(currentToken.type);
            } else {
              expr.append(currentToken.type);
            }
          lastToken = currentToken;
          advance();
        } else if (currentToken.type == Token.TT_LPAREN) {
          expr.append("(");
          lastToken = currentToken;
          advance();
        } else if (currentToken.type == Token.TT_VARTYPE) {
          Token type = currentToken;
          advance();
          if (currentToken != null && currentToken.type == Token.TT_VARNAME) {
            lastToken = currentToken;
            advance();
            if (currentToken != null && currentToken.type == Token.TT_ASSIGN) {
              return makeVar(type, lastToken);
            } else {
              Position start = pos.clone();
              String varName = (String) lastToken.value;
              return new UnassignedVariableError(start, pos, new StringBuilder("Wariable").append(varName).append(" decwared but nao walue was asswigned ._.").toString());
            }
          } //todo else if only vartype is given return the type class
           else {
            Position start = pos.clone();
            return new InvalidSyntaxError(start, pos, new StringBuilder(currentToken.toString()).append(" is iwegal here ._.").toString());
          }
        } else if (currentToken.type == Token.TT_KEYWORD) {
          if (currentToken.value.equals("dewete")) {
            advance();
            if (currentToken.type == Token.TT_VARNAME) {
              heap.remove(currentToken);
              return null;
            } else {
              Position start = pos.clone();
              return new InvalidSyntaxError(start, pos, new StringBuilder(currentToken.toString()).append(" is nawt wariable and i cwannot dewete ._.").toString());
            }
          } //other keywords that appear at the beginning of statement goes here
        } else {
          Position start = pos.clone();
          return new InvalidSyntaxError(start, pos, new StringBuilder(currentToken.toString()).append(" is iwegal here ._.").toString());
        }
      } else if (lastToken.type == Token.TT_INT) {
        if (currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV || currentToken.type == Token.TT_MOD) {
          expr.append(currentToken.type);
          lastToken = currentToken;
          advance();
        } //future operations go below here
      } else {
        Position start = pos.clone();
        return new InvalidSyntaxError(start, pos, new StringBuilder(currentToken.toString()).append(" is iwegal here ._.").toString());
      }
    }
    if (expr.length() > 0) {
      try {
        String s = expr.toString();
        //System.out.println(s);
        if (s.contains("/0")) {
          Position start = pos.clone();
          return new DivisionByZeroError(start, pos);
        }
        Expression e = new ExpressionBuilder(s).build();
        Object result = e.evaluate();
        //Object result = SENG.eval(expr.toString());
        return result;
      } catch (Exception ex) {
        Position start = pos.clone();
        return new RunTimeError(start, pos, ex.getMessage());
      }
    } else {
      return null;
    }
  }

  private Object makeVar(Token type, Token name) {
    advance();
    int idx = pos.getIdx();
    Object value = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, idx, tokens.length));
    if (value instanceof org.kawaiilang.Error) {
      return value;
    } else {
      Variable var = new Variable(type, value);
      heap.put(name, var);
      return value;
    }
  }

}