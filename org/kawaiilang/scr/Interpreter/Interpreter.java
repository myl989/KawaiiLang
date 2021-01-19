package org.kawaiilang;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.HashMap;
import net.objecthunter.exp4j.*;

class Interpreter {
    //Variables for handeling functions
    private Function currentFunc = null;

    //Variables for handeling for loops
    private boolean declaringLoop = false;
    private LoopInterface loop;
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

    public Position getPosition() {
      return pos;
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

    private void retreat() {
      pos.retreat();
    }

    public Object interpret() {
        StringBuilder expr = new StringBuilder();
        StringBuilder stringPool = new StringBuilder();
        OwOList listPool = new OwOList();
        Token lastToken = null;

        //System.out.println(currentFunc);

        //System.out.println("loopIdx: " + loopIdx);
        //System.out.println("declaringLoop: " + declaringLoop);
        //System.out.println(loop);

        //System.out.println("activeElseStatements: " + activeElseStatements);
        //System.out.println(prevDoInterpret);
        //System.out.println("doInterpret: " + doInterpret);
        //System.out.println("justHadElse: " + justHadElse);

        //System.out.println(heap);
        //System.out.println(heap.size());

        if (currentFunc != null) {
          if (tokens.length == 1 && tokens[0].equals(new Token(Token.TT_KEYWORD, "^_^ewndCanDo"))) {
            currentFunc = null;
          } else {
            //Statement to prevent nested function declarations
            if (tokens.length > 6 && tokens[0].equals(new Token(Token.TT_KEYWORD, "OwO")) && tokens[1].equals(new Token(Token.TT_KEYWORD, "canDo")) && tokens[2].type.equals(Token.TT_VARNAME) && tokens[3].equals(new Token(Token.TT_PARAM)) && tokens[tokens.length - 3].equals(new Token(Token.TT_PARAM)) && tokens[tokens.length - 2].equals(new Token(Token.TT_KEYWORD, "canGibU")) && (tokens[tokens.length - 1].type.equals(Token.TT_VARTYPE) || tokens[tokens.length - 1].type.equals(Token.TT_NOTHING))) {
              Position start = pos.clone();
              return new InvalidSyntaxError(start, pos, "Naooo uwu u cwannot hab nwested fwnctwions ._.");
            } else {
              currentFunc.addAction(tokens);
            }
          }
        } else if (declaringLoop) {
            if (tokens.length == 1 && tokens[0].equals(new Token(Token.TT_KEYWORD, "^_^wepeatDat"))) {
                loopIdx--;
                if (loopIdx == -1) {
                    declaringLoop = false;
                    //System.out.println(loop);
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

            //Function declaration
            if (tokens.length > 6 && tokens[0].equals(new Token(Token.TT_KEYWORD, "OwO")) && tokens[1].equals(new Token(Token.TT_KEYWORD, "canDo")) && tokens[2].type.equals(Token.TT_VARNAME) && tokens[3].equals(new Token(Token.TT_PARAM)) && tokens[tokens.length - 3].equals(new Token(Token.TT_PARAM)) && tokens[tokens.length - 2].equals(new Token(Token.TT_KEYWORD, "canGibU")) && (tokens[tokens.length - 1].type.equals(Token.TT_VARTYPE) || tokens[tokens.length - 1].type.equals(Token.TT_NOTHING))) {
              Token funcName = tokens[2];
              Token canGibU = tokens[tokens.length - 1];
              LinkedHashMap<String, String> param = null;
              advance();
              advance();
              advance();
              advance();  //Advance to the 4th token
              while (true) {
                lastToken = currentToken;
                advance();
                if (currentToken.equals(new Token(Token.TT_PARAM)) || lastToken.equals(new Token(Token.TT_PARAM))) {
                  break;
                } else {
                  if (param == null) {
                    param = new LinkedHashMap<>();
                  }
                  /*System.out.print(lastToken);
                  System.out.print(" ");
                  System.out.println(currentToken);*/
                  if (lastToken.equals(new Token(Token.TT_COMMA))) {
                    lastToken = currentToken;
                    advance();
                    /*System.out.print(lastToken);
                    System.out.print(" ");
                    System.out.println(currentToken);*/
                    if (lastToken.type.equals(Token.TT_VARTYPE) && currentToken.type.equals(Token.TT_VARNAME)) {
                      String paramType = (String) lastToken.value;
                      String paramName = (String) currentToken.value;
                      param.put(paramName, paramType);
                      advance();
                    } else {  //Wrong types
                      //error
                    }
                  } else if (lastToken.type.equals(Token.TT_VARTYPE) && currentToken.type.equals(Token.TT_VARNAME)) {
                    String paramType = (String) lastToken.value;
                    String paramName = (String) currentToken.value;
                    param.put(paramName, paramType);
                    advance();
                  } else {  //Wrong types
                    Position start = pos.clone();
                    return new InvalidParameterError(start, pos, "Naooo uwu fwnctwion pawamweters mwst hab wariable nwame awnd twype ._.");
                  }
                }
              }
              //System.out.println(param);
              if (param == null) {
                String funcNameStr = (String) tokens[2].value;
                currentFunc = new Function(this, funcNameStr, canGibU);
              } else {
                String funcNameStr = (String) tokens[2].value;
                currentFunc = new Function(this, funcNameStr, param, canGibU);
              }
              Variable funcVar = new Variable(new Token(Token.TT_VARNAME, "Fwnctwion"), currentFunc);
              heap.put(tokens[2], funcVar);
            } else if (tokens.length > 2 && tokens[0].equals(new Token(Token.TT_KEYWORD, "doWen")) && tokens[1].equals(new Token(Token.TT_STARTIF)) && tokens[tokens.length - 1].equals(new Token(Token.TT_ENDIF))) {  //While loops
              Token[] condition = Arrays.copyOfRange(tokens, 2, tokens.length - 1);
              loop = new DoWhen(this, condition);
              declaringLoop = true;
              loopIdx++;
            } else if (tokens.length > 0 && tokens[0].equals(new Token(Token.TT_KEYWORD, "do")) && tokens[tokens.length - 1].equals(new Token(Token.TT_KEYWORD, "twimes"))) { //For loops
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
                        return new BadOprandTypeError(start, pos, new StringBuilder("Naooo uwu bwad reswlt twypes fwr \"if\" statement: reswlt: ").append(result.toString()).append(" ._.").toString());
                    }
                }

                if (doInterpret == null || doInterpret == true) {
                    //This goes down alllllll the way

                    //Checks for end of if.
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

                    while (true) {  //Main interpretation loop
                        if (currentToken == null) {
                            break;
                        } else if (currentToken instanceof org.kawaiilang.Error) {
                            return currentToken;
                        } else if (currentToken.type.equals(Token.TT_INT) || currentToken.type.equals(Token.TT_FLOAT)) {
                            expr.append(currentToken.value);
                            lastToken = currentToken;
                            advance();
                        } else if (currentToken.type.equals(Token.TT_STR)) {
                          String str = (String) currentToken.value;
                          stringPool.append(str);
                          advance();
                        } else if (currentToken.type.equals(Token.TT_LIST)) {
                          OwOList l = (OwOList) currentToken.value;
                          listPool.append(l.asArrayList());
                          advance();
                        } else if (currentToken.type.equals(Token.TT_NOTHING)) {
                          return null;
                        } else if (currentToken.type.equals(Token.TT_LSQU)) { //List creation
                          advance();
                          if (currentToken.type.equals(Token.TT_RSQU)) {
                            return new OwOList();
                          } else {
                            ArrayList<Token> at = new ArrayList<>();
                            OwOList l = new OwOList();
                            Position origP = pos.clone();
                            Token[] origT = tokens.clone();
                            while (true) {
                              if (currentToken.type.equals(Token.TT_COMMA)) {
                                l.append(new Runner(fn, this).interpret(at.toArray(new Token[0])));
                                at = new ArrayList<>();
                                pos = origP;
                                tokens = origT;
                              } else if (currentToken.type.equals(Token.TT_RSQU)) {
                                l.append(new Runner(fn, this).interpret(at.toArray(new Token[0])));
                                if (l.length() > 1) l.pop(1);
                                return l;
                              } else {
                                at.add(currentToken);
                              }
                              advance();
                            }
                          }
                        } else if (currentToken.type.equals(Token.TT_VARNAME)) {
                          //Special variable for changing loop index
                          if (currentToken.value.equals("inwex") && loop instanceof Loop) {
                            Loop l = (Loop) loop;
                            if (l != null) {
                              if (pos.getIdx() + 1 < tokens.length && tokens[pos.getIdx() + 1].type == Token.TT_ASSIGN) {
                                lastToken = currentToken;
                                advance();
                                Object value = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, pos.getIdx() + 1, tokens.length));
                                if (value instanceof org.kawaiilang.Error) {
                                    return value;
                                } else if (value instanceof Integer) {
                                    Integer i = (Integer) value;
                                    l.setIdx(i);
                                    return value;
                                } else if (value instanceof Double) {
                                    Double d = (Double) value;
                                    l.setIdx(d.intValue());
                                    return value;
                                } else {
                                  Position start = pos.clone();
                                  return new IllegalTypeError(start, pos, "Naooo uwu da twype of variable fwor uwpdwated inwex mwst bwe Numwer ._.");
                                }
                              }
                              //Otherwise it is recall of loop index
                              Token varReplaced = new Token(Token.TT_INT, l.getIdx());
                              tokens[pos.getIdx()] = varReplaced;
                              pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
                              advance();
                              Object value = interpret();
                              return value;
                            } else {
                              Position start = pos.clone();
                              return new IllegalAssignmentError(start, pos, "Naooo uwu u cwannot gwet orw updwate inwex witowt lowp ._.");
                            }
                          }
                          //Same as above, but for changing max index
                          if (currentToken.value.equals("max") && loop instanceof Loop) {
                            Loop l = (Loop) loop;
                            if (l != null) {
                              if (pos.getIdx() + 1 < tokens.length && tokens[pos.getIdx() + 1].type == Token.TT_ASSIGN) {
                                lastToken = currentToken;
                                advance();
                                Object value = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, pos.getIdx() + 1, tokens.length));
                                if (value instanceof org.kawaiilang.Error) {
                                    return value;
                                } else if (value instanceof Integer) {
                                    Integer i = (Integer) value;
                                    l.setMax(i);
                                    return value;
                                } else if (value instanceof Double) {
                                    Double d = (Double) value;
                                    l.setMax(d.intValue());
                                    return value;
                                } else {
                                  Position start = pos.clone();
                                  return new IllegalTypeError(start, pos, "Naooo uwu da twype of variable fwor uwpdwated ,max inwex mwst bwe Numwer ._.");
                                }
                              }
                              //Otherwise it is recall of loop max index
                              Token varReplaced = new Token(Token.TT_INT, l.getMax());
                              tokens[pos.getIdx()] = varReplaced;
                              pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
                              advance();
                              Object value = interpret();
                              return value;
                            } else {
                              Position start = pos.clone();
                              return new IllegalAssignmentError(start, pos, "Naooo uwu u cwannot gwet orw updwate max inwex witowt lowp ._.");
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
                                } else if (type.value.equals("Stwing")) {
                                  if (stored instanceof String) {
                                    varReplaced = new Token(Token.TT_STR, stored);
                                  } else {
                                    Position start = pos.clone();
                                    return new IllegalTypeError(start, pos, new StringBuilder("Naooo uwu da twype of variable, ").append(type.type).append(", is nawt as decwared ._.").toString());
                                  }
                                } else if (type.value.equals("Fwnctwion")) {
                                      Function retrievedFunc = (Function) stored;
                                      ArrayList<Object> inputs = null;  //List of input parameters to pass into function
                                      advance();
                                      if (currentToken == null) {  //Just return the function itself
                                        return retrievedFunc;
                                      } else if (!currentToken.equals(new Token(Token.TT_PARAM))) {
                                        Position start = pos.clone();
                                        return new InvalidSyntaxError(start, pos, "Naooo \"UwU\" expected ._.");
                                      }
                                      advance();
                                      ArrayList<Token> param2eval = new ArrayList<>();
                                      Token[] origT = tokens.clone();
                                      while (true) {
                                        Position origP = pos.clone();
                                        if (currentToken.equals(new Token(Token.TT_PARAM))) {
                                          if (!param2eval.isEmpty()) {
                                            if (inputs == null) {
                                              inputs = new ArrayList<>();
                                            }
                                            //System.out.println(param2eval);
                                            inputs.add(new Runner(fn, this).interpret(param2eval.toArray(new Token[0])));
                                            tokens = origT;
                                            pos = origP;
                                          }
                                          break;
                                        } else if (currentToken.equals(new Token(Token.TT_COMMA))) {
                                          if (inputs == null) {
                                            inputs = new ArrayList<>();
                                          }
                                          //System.out.println(param2eval);
                                          inputs.add(new Runner(fn, this).interpret(param2eval.toArray(new Token[0])));
                                          tokens = origT;
                                          pos = origP;
                                          param2eval = new ArrayList<>();
                                        } else {
                                          param2eval.add(currentToken);
                                        }
                                        advance();
                                      }
                                      if (inputs == null) {
                                        Object o = retrievedFunc.run();
                                        retrievedFunc.resetActions();
                                        return o;
                                      } else {
                                        //System.out.println(inputs);
                                        Object o = retrievedFunc.run(inputs);
                                        retrievedFunc.resetActions();
                                        return o;
                                      }
                                } else if (type.value.equals("Lwist")) {
                                  if (stored instanceof OwOList) {
                                    advance();
                                    if (currentToken != null && currentToken.type.equals(Token.TT_LSQU)) {  //Get and slice
                                      OwOList l = (OwOList) stored;
                                      Position origP = pos.clone();
                                      Position origOrigP = pos.clone();
                                      Token[] origT = tokens.clone();
                                      boolean slice = false;
                                      ArrayList<Token> at = new ArrayList<>();
                                      ArrayList<Token> at2 = new ArrayList<>();
                                      getNslice:
                                      while (true) {
                                        advance();
                                        origP.advance();
                                        if (currentToken.type.equals(Token.TT_RSQU)) {
                                          if (at.isEmpty()) {
                                            if (at2.isEmpty()) {
                                              Object o = new Runner(fn, this).interpret(at2.toArray(new Token[0]));
                                              pos = origP;
                                              tokens = origT;
                                              int i = ((Double) o).intValue();
                                              OwOList slicedList = l.slice(0, i);
                                              varReplaced = new Token(Token.TT_LIST, slicedList);
                                              break getNslice;
                                            } else {
                                              //error
                                            }
                                          } else if (at2.isEmpty()) {
                                            if (slice) {
                                              Object o = new Runner(fn, this).interpret(at.toArray(new Token[0]));
                                              pos = origP;
                                              tokens = origT;
                                              int i = ((Double) o).intValue();
                                              OwOList slicedList = l.slice(i, l.length());
                                              varReplaced = new Token(Token.TT_LIST, slicedList);
                                              break getNslice;
                                            } else {  //Get
                                              Object o = new Runner(fn, this).interpret(at.toArray(new Token[0]));
                                              pos = origP;
                                              tokens = origT;
                                              int i = ((Double) o).intValue();
                                              Object fromList = l.get(i);
                                              if (fromList instanceof Double) {
                                                varReplaced = new Token(Token.TT_FLOAT, o);
                                              } else if (fromList instanceof String) {
                                                varReplaced = new Token(Token.TT_STR, o);
                                              } else if (fromList instanceof Function) {
                                                Function retrievedFunc = (Function) o;
                                                varReplaced = new Token(Token.TT_VARNAME, retrievedFunc.getName());
                                              } //More datatypes here
                                              break getNslice;
                                            }
                                          } else {
                                            Object o = new Runner(fn, this).interpret(at.toArray(new Token[0]));
                                            Object o2 = new Runner(fn, this).interpret(at2.toArray(new Token[0]));
                                            pos = origP;
                                            tokens = origT;
                                            int i = ((Double) o).intValue();
                                            int j = ((Double) o2).intValue();
                                            OwOList slicedList = l.slice(i, j);
                                            varReplaced = new Token(Token.TT_LIST, slicedList);
                                            pos = origP;
                                          }
                                        } else if (currentToken.equals(new Token(Token.TT_KEYWORD, "tw"))) {
                                          slice = true;
                                        } else if (!slice) {
                                          at.add(currentToken);
                                        } else {
                                          at2.add(currentToken);
                                        }
                                      }
                                      pos = origOrigP;
                                      //End getNslice
                                    } else {
                                      retreat();
                                      varReplaced = new Token(Token.TT_LIST, stored);
                                    }
                                  } else {
                                    Position start = pos.clone();
                                    return new IllegalTypeError(start, pos, new StringBuilder("Naooo uwu da twype of variable, ").append(type.type).append(", is nawt as decwared ._.").toString());
                                  }
                                }
                                //More variable types in the future
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
                                    if (currentToken.type.equals(Token.TT_MUL)) {
                                        expr.append('*');
                                    } else {
                                        expr.append(currentToken.type);
                                    }
                                }
                                lastToken = currentToken;
                                advance();
                            } else if (currentToken.type.equals(Token.TT_LPAREN)) {
                                expr.append("(");
                                lastToken = currentToken;
                                advance();
                            } else if (currentToken.type.equals(Token.TT_VARTYPE)) { //Variable assignment
                                Token type = currentToken;
                                advance();
                                if (type.value.equals("Fwnctwion")) { //Cannot declare function like this!
                                  Position start = pos.clone();
                                  return new IllegalAssignmentError(start, pos, "Naooo uwu u cwannot asswign fwnctwion lwike dat ._.");
                                } //The following code might come in handy when I add list object restrictions
                                /*else if (type.value.equals("Lwist")) {
                                  advance();
                                  if (currentToken != null && currentToken.type.equals(Token.TT_LSQU)) {
                                    advance();
                                    if (currentToken != null && currentToken.type.equals(Token.TT_RSQU)) {
                                      advance();
                                      if (currentToken != null && currentToken.type.equals(Token.TT_VARNAME)) {
                                        lastToken = currentToken;
                                        advance();
                                        if (currentToken != null && currentToken.type.equals(Token.TT_ASSIGN)) {
                                          return makeVar(type, lastToken);
                                        } else {
                                          //e*rror
                                        }
                                      } else {
                                        //e*rror
                                      }
                                    }
                                    //List type restrictions maybe?
                                  } else {
                                    //e*rror
                                  }
                                }*/ else if (currentToken != null && currentToken.type.equals(Token.TT_VARNAME)) {
                                    lastToken = currentToken;
                                    advance();
                                    if (currentToken != null && currentToken.type.equals(Token.TT_ASSIGN)) {
                                        return makeVar(type, lastToken);
                                    } else {
                                        Position start = pos.clone();
                                        String varName = (String) lastToken.value;
                                        return new UnassignedVariableError(start, pos, new StringBuilder("Naooo uwu wariable ").append(varName).append(" decwared but nao walue was asswigned ._.").toString());
                                    }
                                } //todo else if only vartype is given return the type class
                                else {
                                    Position start = pos.clone();
                                    return new InvalidSyntaxError(start, pos, new StringBuilder("Naooo uwu ").append(currentToken.toString()).append(" iws iwegal here ._.").toString());
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
                                  return new InvalidSyntaxError(start, pos, new StringBuilder("Naooo uwu ").append(currentToken.toString()).append(" iws iwegal here ._.").toString());
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
                    if (stringPool.length() > 0) {
                      return stringPool.toString();
                    } else if (listPool.length() > 0) {
                      return listPool;
                    } else if (expr.length() > 0) {
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
          return new IllegalAssignmentError(start, pos, "Naooo uwu u cwannot set inwex witowt lowp ._.");
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