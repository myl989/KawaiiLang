package org.kawaiilang;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

import net.objecthunter.exp4j.*;

class Interpreter {
    //Variables for handeling for loops
    private boolean declaringLoop = false;
    private Loop loop;
    private int loopIdx = -1;

    //Variables for handeling if statements
    private Boolean doInterpret = null;
    private ArrayList < Boolean > prevDoInterpret = new ArrayList < > ();
    private boolean justHadElse = false;
    private boolean hasBeenTrue = false;
    private int activeElseStatements = 0;

    //Base interpreter variables
    private Token[] tokens;
    private Position pos;
    private Token currentToken;
    private String fn;
    private HashMap < Token, Variable > heap = new HashMap < > ();

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

    public String getFileLocation() {
        return fn;
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

        //System.out.println("loopIdx: " + loopIdx);
        //System.out.println("declaringLoop: " + declaringLoop);
        //System.out.println(loop);

        //System.out.println("activeElseStatements: " + activeElseStatements);
        //System.out.println(prevDoInterpret);
        //System.out.println("doInterpret: " + doInterpret);
        //System.out.println("justHadElse: " + justHadElse);

        //System.out.println(heap);
        //System.out.println(heap.size());

        if (declaringLoop) {
            if (tokens.length == 1 && tokens[0].equals(new Token(Token.TT_KEYWORD, "^_^wepeatDat"))) {
                loopIdx--;
                if (loopIdx == -1) {
                    declaringLoop = false;
                    loop.loop();
                    loop = null;
                } else {
                  loop.addAction(tokens);
                }
            } else {
                if (tokens.length > 0 && tokens[0].equals(new Token(Token.TT_KEYWORD, "do")) && tokens[tokens.length - 1].equals(new Token(Token.TT_KEYWORD, "twimes"))) {
                  loopIdx++; //So that ending a nested loop doesn't end the whole thing
                } else if (tokens.length == 1 && tokens[0].equals(new Token(Token.TT_KEYWORD, "stawp"))) {
                  return tokens[0];
                }
                loop.addAction(tokens);
            }
        } else {
            //Else if statements: do not run if last if is true
            if (activeElseStatements >= 0) {
                if (tokens.length > 0 && tokens[0].equals(new Token(Token.TT_KEYWORD, "ewlse"))) {
                    justHadElse = true;
                    if (!hasBeenTrue) {
                        doInterpret = (!doInterpret);
                    } else {
                        doInterpret = false;
                    }
                } else if (activeElseStatements > 0) {
                    doInterpret = prevDoInterpret.get(activeElseStatements - 1);
                } else {
                    //No else
                    if (!justHadElse) {
                        doInterpret = null;
                    }
                }
            }

            //For loops
            if (tokens.length > 0 && tokens[0].equals(new Token(Token.TT_KEYWORD, "do")) && tokens[tokens.length - 1].equals(new Token(Token.TT_KEYWORD, "twimes"))) {
                if (Arrays.asList(tokens).contains(new Token(Token.TT_KEYWORD, "tw"))) {
                    Token[] exprs = Arrays.copyOfRange(tokens, 1, tokens.length - 1);
                    ArrayList < Token > exA = new ArrayList < > ();
                    ArrayList < Token > exB = new ArrayList < > ();
                    boolean secondPart = false;
                    Token delim = new Token(Token.TT_KEYWORD, "tw");
                    for (int i = 0; i < exprs.length; i++) {
                        if (exprs[i].equals(delim)) {
                            secondPart = true;
                        } else if (secondPart) {
                            exB.add(exprs[i]);
                        } else {
                            exA.add(exprs[i]);
                        }
                    }
                    Token[] exAa = exA.toArray(new Token[0]);
                    Object resultA = new Runner(fn, this).interpret(Arrays.copyOfRange(exAa, 0, exAa.length));
                    Token[] exAb = exB.toArray(new Token[0]);
                    Object resultB = new Runner(fn, this).interpret(Arrays.copyOfRange(exAb, 0, exAb.length));
                    if (resultA instanceof Double && resultB instanceof Double) {
                        Double dA = (Double) resultA;
                        int a = dA.intValue();
                        Double dB = (Double) resultB;
                        int b = dB.intValue();
                        loop = new Loop(this, a, b);
                        declaringLoop = true;
                        loopIdx++;
                    } else {
                        Position start = pos.clone();
                        return new IllegalTypeError(start, pos, new StringBuilder("Naooo uwu da start and ewnd lowp amwownt mwst bwe a numbwer, inpwtwed start lowp amwownt: ").append(resultA).append(", impwtwed ewnd lowp amwownt: ").append(" ._.").append(resultB).toString());
                    }
                } else {
                    if (tokens.length > 1) {
                        Object result = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, 1, tokens.length - 1));
                        if (result instanceof Double) {
                            Double d = (Double) result;
                            int i = d.intValue();
                            loop = new Loop(this, i);
                            declaringLoop = true;
                            loopIdx++;
                        } else if (result instanceof org.kawaiilang.Error) {
                            return result;
                        } else {
                            Position start = pos.clone();
                            return new IllegalTypeError(start, pos, new StringBuilder("Naooo uwu da lowp amwownt mwst bwe a numbwer, inpwtwed lowp amwownt: ").append(result).append(" ._.").toString());
                        }
                    } else {
                        Position start = pos.clone();
                        return new InvalidSyntaxError(start, pos, "Naooo uwu expwecwed lowp amwownt hewe ._.");
                    }
                }
            } else {
                //If statements
                if (tokens.length > 0 && tokens[0].equals(new Token(Token.TT_STARTIF))) {
                    Object result = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, 1, tokens.length - 1));
                    if (result instanceof Double) {
                        if (doInterpret == null || doInterpret) {
                            Double d = (Double) result;
                            if (d > 0) {
                                activeElseStatements++;
                                doInterpret = true;
                                prevDoInterpret.add(true);
                                hasBeenTrue = true;
                            } else {
                                activeElseStatements++;
                                doInterpret = false;
                                prevDoInterpret.add(false);
                            }
                        }
                    } else if (result == null) {
                        //Signifies the if statement is inside a false if statement or it is an else after a statement that is true
                        activeElseStatements++;
                        prevDoInterpret.add(false);
                        //So that any ewndNotice statements don't mess anything up
                    } else if (result instanceof org.kawaiilang.Error) {
                        return result;
                    } else {
                        Position start = pos.clone();
                        return new BadOprandTypeError(start, pos, new StringBuilder("Bwad reswlt twypes fwr \"if\" statement: reswlt: ").append(result.toString()).append(" ._.").toString());
                    }
                }

                if (doInterpret == null || doInterpret == true) {
                    //This goes down alllllll the way

                    //Checks for end of if. Expressions directly after end of if will not be evaluated.
                    if (doInterpret != null && tokens.length == 1 && tokens[0].equals(new Token(Token.TT_KEYWORD, "^_^ewndNotice"))) {
                        justHadElse = false;
                        activeElseStatements--;
                        prevDoInterpret.remove(activeElseStatements);
                        return null;
                    }
                    //NOT operation
                    else if (tokens.length > 0 && tokens[0].equals(new Token(Token.TT_KEYWORD, "nawt"))) {
                        if (tokens.length > 1) {
                            Object result = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, 1, tokens.length));
                            if (result instanceof Double) {
                                Double d = (Double) result;
                                if (d > 0) {
                                    return 0.0;
                                } else {
                                    return 1.0;
                                }
                            } else if (result instanceof org.kawaiilang.Error) {
                                return result;
                            } else {
                                Position start = pos.clone();
                                return new BadOprandTypeError(start, pos, new StringBuilder("Naooo uwu u hab bwad owpwand twypes fwr \"nawt\" owpewawor: owpwand: ").append(result.toString()).append(" ._.").toString());
                            }
                        } else {
                            Position start = pos.clone();
                            return new InvalidSyntaxError(start, pos, "Naooo uwu opwand not found for \"nawt\"opewawor ._.");
                        }
                    }

                    //AND, OR, XOR operations
                    if (Arrays.asList(tokens).contains(new Token(Token.TT_KEYWORD, "awnd")) || Arrays.asList(tokens).contains(new Token(Token.TT_KEYWORD, "orw")) || Arrays.asList(tokens).contains(new Token(Token.TT_KEYWORD, "xwr"))) {
                        ArrayList < Token > exA = new ArrayList < > ();
                        ArrayList < Token > exB = new ArrayList < > ();
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
                        ArrayList < Token > exA = new ArrayList < > ();
                        ArrayList < Token > exB = new ArrayList < > ();
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
                        }
                        /*else if (???) { //new line?
                               lastToken = null;
                               advance();
                             }*/
                        else if (currentToken instanceof org.kawaiilang.Error) {
                            return currentToken;
                        } else if (currentToken.type == Token.TT_INT || currentToken.type == Token.TT_FLOAT) {
                            expr.append(currentToken.value);
                            lastToken = currentToken;
                            advance();
                        }
                        else if (currentToken.type == Token.TT_VARNAME) {
                          //Special variable for changing loop index
                          if (currentToken.value.equals("inwex")) {
                            if (loop != null) {
                              if (pos.getIdx() + 1 < tokens.length && tokens[pos.getIdx() + 1].type == Token.TT_ASSIGN) {
                                lastToken = currentToken;
                                advance();
                                Object value = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, pos.getIdx() + 1, tokens.length));
                                if (value instanceof org.kawaiilang.Error) {
                                    return value;
                                } else if (value instanceof Double) {
                                    Double d = (Double) value;
                                    loop.setIdx(d.intValue());
                                    return value;
                                } else {
                                  Position start = pos.clone();
                                  return new IllegalTypeError(start, pos, "Naooo uwu da twype of variable fwor uwpdwated inwex mwst bwe Numwer ._.");
                                }
                              }
                              //Otherwise it is recall of loop index
                              return loop.getIdx();
                            } else {
                              Position start = pos.clone();
                              return new InvalidSyntaxError(start, pos, "Naooo uwu u cwannot gwet orw updwate inwex witowt lowp ._.");
                            }
                          }

                          //Variable reassignment
                          if (pos.getIdx() + 1 < tokens.length && tokens[pos.getIdx() + 1].type == Token.TT_ASSIGN && heap.containsKey(currentToken)) {
                                lastToken = currentToken;
                                advance();
                                Object value = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, pos.getIdx() + 1, tokens.length));
                                if (value instanceof org.kawaiilang.Error) {
                                    return value;
                                } else {
                                    currentToken = lastToken;
                                    Variable var = new Variable(heap.get(currentToken).getType(), value);
                                    heap.put(currentToken, var);
                                    return value;
                                }
                          }

                          //Variable recall
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
                                        return new IllegalTypeError(start, pos, new StringBuilder("Naooo uwu da twype of variable, ").append(type.type).append(", is nawt as decwared ._.").toString());
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
                                return new UnassignedVariableError(start, pos, new StringBuilder("Naooo uwu da wariable ").append(varName).append(" newer asswigned ._.").toString());
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
                                    return new InvalidSyntaxError(start, pos, new StringBuilder("Naooo uwu ").append(currentToken.toString()).append(" cwannot cwome awfter awnowther owpewation ._.").toString());
                                }
                            } else if (currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV || currentToken.type == Token.TT_MOD) {
                                if (lastToken != null && (lastToken.type != Token.TT_INT && lastToken.type != Token.TT_FLOAT)) {
                                    expr.append("0").append(currentToken.type);
                                } else {
                                    if (currentToken.type == Token.TT_MUL) {
                                        expr.append('*');
                                    } else {
                                        expr.append(currentToken.type);
                                    }
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
                                        return new UnassignedVariableError(start, pos, new StringBuilder("Naooo uwu wariable ").append(varName).append(" decwared but nao walue was asswigned ._.").toString());
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
                                        return new InvalidSyntaxError(start, pos, new StringBuilder("Naooo uwu ").append(currentToken.toString()).append(" is nawt wariable and i cwannot dewete ._.").toString());
                                    }
                                } else if (currentToken.value.equals("ewlse")) {
                                    return null; //Else does nothing on its own
                                } //other keywords that appear at the beginning of statement goes here
                                else {
                                  Position start = pos.clone();
                                  return new InvalidSyntaxError(start, pos, new StringBuilder("Naooo uwu ").append(currentToken.toString()).append(" is iwegal here ._.").toString());
                                }
                            } else {
                                Position start = pos.clone();
                                return new InvalidSyntaxError(start, pos, new StringBuilder("Naooo uwu ").append(currentToken.toString()).append(" is iwegal here ._.").toString());
                            }
                        } else if (lastToken.type == Token.TT_INT) {
                            if (currentToken.type == Token.TT_ADD || currentToken.type == Token.TT_MINUS || currentToken.type == Token.TT_MUL || currentToken.type == Token.TT_DIV || currentToken.type == Token.TT_MOD) {
                                expr.append(currentToken.type);
                                lastToken = currentToken;
                                advance();
                            } //future operations go below here
                        } else {
                            Position start = pos.clone();
                            return new InvalidSyntaxError(start, pos, new StringBuilder("Naooo uwu ").append(currentToken.toString()).append(" is iwegal here ._.").toString());
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
                            return result;
                        } catch (Exception ex) {
                            Position start = pos.clone();
                            return new RunTimeError(start, pos, ex.getMessage());
                        }
                    } else {
                        return null;
                    }
                } else { //doInterpret is FALSE
                    //Only care about the endif and evaluate nothing
                    if (tokens.length == 1 && tokens[0].equals(new Token(Token.TT_KEYWORD, "^_^ewndNotice"))) {
                        justHadElse = false;
                        activeElseStatements--;
                        prevDoInterpret.remove(activeElseStatements);
                    }
                    return null;
                }
            } //end Loop if
        } //end record loop
        return null;
    } //endFunction

    private Object makeVar(Token type, Token name) {
        advance();
        Object value = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, pos.getIdx(), tokens.length));
        if (value instanceof org.kawaiilang.Error) {
            return value;
        } else if (name.value.equals("inwex")) {
          Position start = pos.clone();
          return new InvalidSyntaxError(start, pos, "Naooo uwu u cwannot set inwex witowt lowp ._.");
        } else {
            Variable
            var = new Variable(type, value);
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
        if (resultA instanceof Double && resultB instanceof Double) {
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
        return new BadOprandTypeError(start, pos, new StringBuilder("Naooo uwu u hab bwad owpwand twypes fwr bwinwawy owpewawor \"").append(oper).append("\": first owpwand: ").append(resultA.toString()).append(", second owpwand: ").append(resultB.toString()).append(" ._.").toString());
    }

}