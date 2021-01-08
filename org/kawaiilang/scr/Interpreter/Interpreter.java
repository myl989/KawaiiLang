package org.kawaiilang;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

import net.objecthunter.exp4j.*;

class Interpreter {

  private Token[] tokens;
  private Position pos;
  private Token currentToken;
  private String fn;
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

    //If and loop stuff goes here

    //NOT operation
    if (tokens[0].equals(new Token(Token.TT_KEYWORD, "nawt"))) {
      if (tokens.length > 1) {
        Object result = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, 1, tokens.length));
        if (result instanceof Double) {
          Double d = (Double) result;
          if (d > 0) {
            return 0.0;
          } else {
            return 1.0;
          }
        } else {
          //error
        }
      } else {
        //error
      }
    }

    //AND, OR, XOR operations
    if (Arrays.asList(tokens).contains(new Token(Token.TT_KEYWORD, "awnd")) || Arrays.asList(tokens).contains(new Token(Token.TT_KEYWORD, "orw")) || Arrays.asList(tokens).contains(new Token(Token.TT_KEYWORD, "xwr"))) {
      ArrayList<Token> exA = new ArrayList<>();
      ArrayList<Token> exB = new ArrayList<>();
      boolean secondPart = false;
      Token oper = null;
      for (int i = 0; i < tokens.length; i++) {
        if (tokens[i].value != null && (tokens[i].value.equals("awnd") || tokens[i].value.equals("orw") || tokens[i].value.equals("xwr"))) {
          if (!secondPart) {
            oper = tokens[i];
            secondPart = true;
          } else {
            exB.add(tokens[i]);
          }
        } else if (secondPart) {
          exB.add(tokens[i]);
        } else {
          exA.add(tokens[i]);
        }
      }
      return evalLogic(exA.toArray(new Token[0]), oper, exB.toArray(new Token[0]));
    }

    //==, >, <, >=, <= operations
    if (Arrays.asList(tokens).contains(new Token(Token.TT_EQUALS)) || Arrays.asList(tokens).contains(new Token(Token.TT_LT)) || Arrays.asList(tokens).contains(new Token(Token.TT_GT)) || Arrays.asList(tokens).contains(new Token(Token.TT_LTE)) || Arrays.asList(tokens).contains(new Token(Token.TT_GTE))) {
      ArrayList<Token> exA = new ArrayList<>();
      ArrayList<Token> exB = new ArrayList<>();
      boolean secondPart = false;
      Token oper = null;
      for (int i = 0; i < tokens.length; i++) {
        if (tokens[i].type == Token.TT_EQUALS || tokens[i].type == Token.TT_LT || tokens[i].type == Token.TT_GT || tokens[i].type == Token.TT_LTE || tokens[i].type == Token.TT_GTE) {
          if (!secondPart) {
            oper = tokens[i];
            secondPart = true;
          } else {
            exB.add(tokens[i]);
          }
        } else if (secondPart) {
          exB.add(tokens[i]);
        } else {
          exA.add(tokens[i]);
        }
      }
      return evalComparison(exA.toArray(new Token[0]), oper, exB.toArray(new Token[0]));
    }
    
    while (true) {
      if (currentToken == null) {
        break;
      } /*else if (???) { //new line?
        lastToken = null;
        advance();
      }*/ 
      //In future may add if statements here
      else if (currentToken instanceof org.kawaiilang.Error) {
        return currentToken;
      } else if (currentToken.type == Token.TT_INT || currentToken.type == Token.TT_FLOAT) {
        expr.append(currentToken.value);
        lastToken = currentToken;
        advance();
      } //Logical operations here
      else if (currentToken.type == Token.TT_VARNAME) {
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

  private Object evalComparison(Token[] exprA, Token oper, Token[] exprB) {
    Object resultA = new Runner(fn, this).interpret(exprA);
    Object resultB = new Runner(fn, this).interpret(exprB);
    if (oper.type == Token.TT_EQUALS) {
      //Custom equals implementations?
      if (resultA.equals(resultB)) {
        return 1.0;
      } else {
        return 0.0;
      }
    } else if (oper.type == Token.TT_LT) {
      //Custom comparison implementations?
      if (resultA instanceof Double && resultB instanceof Double) {
        Double a = (Double) resultA;
        Double b = (Double) resultB;
        if (a < b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    } else if (oper.type == Token.TT_GT) {
      //Custom comparison implementations?
      if (resultA instanceof Double && resultB instanceof Double) {
        Double a = (Double) resultA;
        Double b = (Double) resultB;
        if (a > b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    } else if (oper.type == Token.TT_LTE) {
      //Custom comparison implementations?
      if (resultA instanceof Double && resultB instanceof Double) {
        Double a = (Double) resultA;
        Double b = (Double) resultB;
        if (a <= b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    } else if (oper.type == Token.TT_GTE) {
      //Custom comparison implementations?
      if (resultA instanceof Double && resultB instanceof Double) {
        Double a = (Double) resultA;
        Double b = (Double) resultB;
        if (a >= b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    }
    return null;
  }

  private Object evalLogic(Token[] exprA, Token oper, Token[] exprB) {
    Object resultA = new Runner(fn, this).interpret(exprA);
    Object resultB = new Runner(fn, this).interpret(exprB);
    if (resultA instanceof Double && resultB instanceof Double ) {
      Double doubleA = (Double) resultA;
      Double doubleB = (Double) resultB;
      boolean a;
      boolean b;
      if (doubleA <= 0.0) {
        a = false;
      } else {
        a = true;
      }

      if (doubleB <= 0.0) {
        b = false;
      } else {
        b = true;
      }

      if (oper.value.equals("awnd")) {
        if (a && b) {
          return 1.0;
        } else {
          return 0.0;
        }
      } else if (oper.value.equals("orw")) {
        if (a || b) {
          return 1.0;
        } else {
          return 0.0;
        }
      } else if (oper.value.equals("xwr")) {
        if (a ^ b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    }
    
    Position start = pos.clone();
    return new BadOprandTypeError(start, pos, new StringBuilder("Bwad owpwand twypes fwr bwinwawy owpewawor \"").append(oper).append("\": first owpwand: ").append(resultA.toString()).append(", second owpwand: ").append(resultB.toString()).toString());
  }

}
