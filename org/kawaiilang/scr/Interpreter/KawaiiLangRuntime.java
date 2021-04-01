package org.kawaiilang;

import org.kawaiilang.api.*;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;
import net.objecthunter.exp4j.*;

public final class KawaiiLangRuntime {

  // An object intended for use of communicating break statements
  public static final Object BREAK_OBJECT = new Object();

  // Variables for handling class declerations
  private OwObject classDeclaring = null;
  private ClassHelper classHelper;

  // Variables for handling collections
  OwOList<?> listPool = new OwOList<Object>();
  // The current list pool for list creation

  // Variables for handling functions
  private Function currentFunc = null;
  // The current function for function declaration
  private boolean overload = false;
  // Whether the function is an overload
  private HashMap<Map.Entry<String, String>, Function> overloadedFunctions = new HashMap<>();
  // A hash map of overloaded functions based on param varname and vartype

  // Variables for handling for loops
  private boolean declaringLoop = false;
  // Whether or not a loop is being declared right now
  private LoopInterface loop;
  // The loop (for or while) being declared
  private int loopIdx = -1;
  // Amount of nested loops, starting at index -1

  // Variables for handling switch statements
  private Stack<Switch> switches = new Stack<>();
  // A stack of switches being declared
  private ArrayList<Token[]> caseContents = new ArrayList<>();
  // A list of the contents of the current case of the current switch
  private int caseNumToPut;
  // The case number the interpreter is currently interpreting to put into the
  // switch statement
  private boolean putDefaults;
  // Whether or not the current case is a default case
  private int caseNumToExecute;
  // The case number to execute the switch statement

  // Variables for handling if statements
  private Boolean doInterpret = null;
  // Whether or not the code should be interpreted, null and true will be
  // interpreted and false will not.
  // Null means there is no preference, true is used in if statements that are
  // true, and false is used in if statements that are false
  private ArrayList<Boolean> prevDoInterpret = new ArrayList<>();
  // The previous values of doInterpret
  private boolean justHadElse = false;
  // Whether or not else was the last statement
  private boolean hasBeenTrue = false;
  // Whether or not the if-else cluster has had a true value
  private int activeElseStatements = 0;
  // The number of active else statements for nested if clusters

  // Base interpreter variables
  private Token[] tokens;
  // The current expression of tokens to interpret
  private Position pos;
  // The current token position
  private Token currentToken;
  // The current token
  private String fn;
  // The filename
  private HashMap<Token, Variable> heap = new HashMap<>();
  // A collection of all variables and tokens currently in use, built-in or custom
  // mapped by the varname

  public KawaiiLangRuntime(String fileName, Token[] tokens) {
    this.fn = fileName;
    setTokens(tokens);
    CoreAPI.addAPI(this);
  }

  public KawaiiLangRuntime(String fileName) {
    this.fn = fileName;
    CoreAPI.addAPI(this);
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

  public HashMap<Token, Variable> getHeap() {
    return heap;
  }

  public void putHeapItem(Token t, Variable var) {
    heap.put(t, var);
  }

  public void advanceLine() {
    pos.advanceLine();
  }

  private void advance() {
    pos.advance();
    if (pos.getIdx() < tokens.length) {
      currentToken = tokens[pos.getIdx()];
    } else {
      currentToken = null;
    }
  }

  private void retreatWithoutUpdating() {
    pos.retreat();
  }

  void resetIf() { // Resets if statements for use in loops, etc.
    hasBeenTrue = false;
  }

  public Object interpret() {
    StringBuilder expr = new StringBuilder();
    StringBuilder stringPool = new StringBuilder();
    Token lastToken = null;

    // CODE FOR PRINTING STUFF OUT FOR DEBUGGING
    // The following code prints out the heap without any built-in functions.
    /*
    heap.forEach((K, V) -> { if (!(V.getValue() instanceof BuiltInFunction)) {
    System.out.println(K.toString() + ' ' + V.getValue() + '\t'); } });
    */

    if (classDeclaring != null) { // Declaring insides of class
      if (classHelper == null) {
        classHelper = new ClassHelper(classDeclaring, this);
      } else if (tokens.length == 1 && tokens[0].equals(TokenV1.C_ENDCLASSK)) {
        // Finalize class declaration
        classHelper.setupClassCode();
        classHelper = null;
        heap.put(new TokenV1(TokenV1.TT_CLASS, classDeclaring.className()),
            new Variable(TokenV1.C_CLASSVT, classDeclaring));
        classDeclaring = null;
      } else {
        classHelper.addCodeToClass(tokens);
      }
    } else if (currentFunc != null) { // Declaring insides of function
      if (tokens.length == 1 && tokens[0].equals(TokenV1.C_ENDFUNCK)) {
        currentFunc = null;
      } else {
        currentFunc.addAction(tokens);
      }
    } else if (declaringLoop) { // Declaring insides of loop
      if (tokens.length == 1 && tokens[0].equals(TokenV1.C_ENDLOOPK)) {
        loopIdx--;
        if (loopIdx == -1) {
          declaringLoop = false;
          // System.out.println(loop);
          loop.loop();
          loop = null;
        } else {
          loop.addAction(tokens);
        }
      } else {
        if (tokens.length > 0 && tokens[0].equals(TokenV1.C_FORK)
            && tokens[tokens.length - 1].equals(TokenV1.C_TIMESK)) {
          loopIdx++; // So that ending a nested loop doesn't end the whole thing
        } else if (tokens.length == 1 && tokens[0].equals(TokenV1.C_BREAKK)) {
          return tokens[0];
        }
        loop.addAction(tokens);
      }
    } else if (!switches.isEmpty()) { // Switch statement content declaration
      if (switches.peek().equals(Switch.BLANK_SWITCH)) {
        switches.pop();
        return null;
      } else if (tokens.length == 1 && tokens[0].equals(TokenV1.C_ENDIFK)) {
        // End switch declaration and execute
        switches.peek().addStatement(caseNumToPut, caseContents);
        // System.out.println(switches.peek().toString());
        switches.pop().doSwitch(caseNumToExecute);
        caseContents = new ArrayList<>();
        return null;
      } else if (tokens.length == 1 && tokens[0].equals(TokenV1.C_NEXTCASEK)) {
        if (putDefaults) {
          switches.peek().addDefaults(caseContents);
          switches.pop().doSwitch(caseNumToExecute);
          caseContents = new ArrayList<>();
          switches.add(Switch.BLANK_SWITCH);
        } else {
          switches.peek().addStatement(caseNumToPut, caseContents);
          caseContents = new ArrayList<>();
        }
        return null;
      } else {
        if (tokens.length > 2 && tokens[0].equals(TokenV1.C_TILDE) && tokens[1].equals(TokenV1.C_EQUALS)) {
          Object o = new Runner(fn, this.strippedCopy()).interpret(Arrays.copyOfRange(tokens, 2, tokens.length));
          if (o instanceof Double) {
            caseNumToPut = ((Double) o).intValue();
          } else {
            
            return new InvalidParameterError(pos, "Naooo uwu numwer tw switch mwst be awn intewer ._.");
          }
          return null;
        } else if (tokens.length == 1 && tokens[0].equals(TokenV1.C_OTHERWISEDOK)) {
          putDefaults = true;
          return null;
        } else {
          caseContents.add(tokens);
          return null;
        }
      }
    } else {
      // Else if statements: do not run if last if is true
      if (activeElseStatements >= 0) {
        if (tokens.length == 1 && tokens[0].equals(TokenV1.C_ELSEK)) {
          justHadElse = true;
          if (!hasBeenTrue) {
            doInterpret = (!doInterpret);
          } else {
            doInterpret = false;
          }
        } else if (activeElseStatements > 0) {
          doInterpret = prevDoInterpret.get(activeElseStatements - 1);
        } else {
          // No else
          if (!justHadElse) {
            doInterpret = null;
            hasBeenTrue = false;
          }
        }
      }

      // Include statement
      if (tokens.length == 2 && tokens[0].getType().equals(TokenV1.TT_STR) && tokens[1].equals(TokenV1.C_INCLUDE)) {
        // Built-in APIs
        String s = (String) tokens[0].getValue();
        if (s.equals("wandom")) {
          RandomAPI.addAPI(this);
          return null;
        } else if (s.equals("mwath")) {
          MathAPI.addAPI(this);
          return null;
        }

        // Custom files
        String thisFn = fn;
        fn = s;
        try {
          new Runner(fn, this).run();
        } catch (IOException ie) {
          
          return new RunTimeError(pos, ie.getMessage());
        }
        fn = thisFn;
        return null;
      } else if (tokens.length > 1 && tokens[0].equals(TokenV1.C_CLASSK)) {
        // Class declaration
        String className = tokens[1].getValue().toString();
        if (tokens.length > 3 && tokens[2].equals(TokenV1.C_EXTENDSK) && tokens[3].getType().equals(TokenV1.TT_CLASS)) {
          OwObject parent = (OwObject) heap.get(tokens[3]).getValue();
          classDeclaring = declareClass(className, parent);
        } else {
          classDeclaring = declareClass(className);
        }
        StringBuilder sb = new StringBuilder(
            fn.split("(\\/(?!.*\\/)|\\\\(?!.*\\\\))(?!.*(\\/(?!.*\\/)|\\\\(?!.*\\\\)))")[0]);
        sb.append('/');
        sb.append(className);
        Lexer.addClassName(sb.toString(), classDeclaring.shortName());
      } else if (tokens.length == 1 && tokens[0].equals(TokenV1.C_OVERLOADK)) {
        // Function overload declaration
        overload = true;
      } else if (tokens.length > 6 && tokens[0].equals(TokenV1.C_OWOK) && tokens[1].equals(TokenV1.C_FUNCK)
          && tokens[2].getType().equals(TokenV1.TT_VARNAME) && tokens[3].equals(TokenV1.C_PARAM)
          && tokens[tokens.length - 3].equals(TokenV1.C_PARAM)
          && tokens[tokens.length - 2].equals(new TokenV1(TokenV1.TT_KEYWORD, "canGibU"))
          && (tokens[tokens.length - 1].getType().equals(TokenV1.TT_VARTYPE)
              || tokens[tokens.length - 1].getType().equals(TokenV1.TT_NOTHING))) {
        // Function declaration
        Token canGibU = tokens[tokens.length - 1];
        LinkedHashMap<String, String> param = null;
        advance();
        advance();
        advance();
        advance(); // Advance to the 4th token
        while (true) {
          lastToken = currentToken;
          advance();
          if (currentToken.equals(TokenV1.C_PARAM) || lastToken.equals(TokenV1.C_PARAM)) {
            break;
          } else {
            if (param == null) {
              param = new LinkedHashMap<>();
            }
            /*
             * System.out.print(lastToken); System.out.print(" ");
             * System.out.println(currentToken);
             */
            if (lastToken.equals(TokenV1.C_COMMA)) {
              lastToken = currentToken;
              advance();
              /*
               * System.out.print(lastToken); System.out.print(" ");
               * System.out.println(currentToken);
               */
              if (lastToken.getType().equals(TokenV1.TT_VARTYPE) && currentToken.getType().equals(TokenV1.TT_VARNAME)) {
                String paramType = (String) lastToken.getValue();
                String paramName = (String) currentToken.getValue();
                param.put(paramName, paramType);
                advance();
              } else { // Wrong types
                
                return new InvalidParameterError(pos,
                    "Naooo uwu fwnctwion pawamweters mwst hab wariable nwame awnd twype ._.");
              }
            } else if (lastToken.getType().equals(TokenV1.TT_VARTYPE)
                && currentToken.getType().equals(TokenV1.TT_VARNAME)) {
              String paramType = (String) lastToken.getValue();
              String paramName = (String) currentToken.getValue();
              param.put(paramName, paramType);
              advance();
            } else { // Wrong types
              
              return new InvalidParameterError(pos,
                  "Naooo uwu fwnctwion pawamweters mwst hab wariable nwame awnd twype ._.");
            }
          }
        }
        // System.out.println(param);
        String funcNameStr = (String) tokens[2].getValue();
        if (param == null) {
          currentFunc = new Function(this, funcNameStr, canGibU);
        } else {
          currentFunc = new Function(this, funcNameStr, param, canGibU);
        }
        if (!overload) {
          Variable funcVar = new Variable(new TokenV1(TokenV1.TT_VARNAME, "Fwnctwion"), currentFunc);
          heap.put(tokens[2], funcVar);
        } else {
          Map.Entry<String, String> e;
          if (param == null) {
            e = new AbstractMap.SimpleEntry<>(funcNameStr, null);
            overloadedFunctions.put(e, currentFunc);
          } else {
            StringBuilder sb = new StringBuilder();
            for (String s : param.values()) {
              sb.append(s).append('_');
            }
            e = new AbstractMap.SimpleEntry<>(funcNameStr, sb.toString());
            overloadedFunctions.put(e, currentFunc);
          }
        }
        overload = false;
        // end function declaration
      } else if (tokens.length > 2 && tokens[0].equals(TokenV1.C_WHILEK) && tokens[1].equals(TokenV1.C_STARTIF)
          && tokens[tokens.length - 1].equals(TokenV1.C_ENDIF)) { // While loops
        Token[] condition = Arrays.copyOfRange(tokens, 2, tokens.length - 1);
        loop = new DoWhen(this, condition);
        declaringLoop = true;
        loopIdx++;
      } else if (tokens.length > 0 && tokens[0].equals(TokenV1.C_FORK)
          && tokens[tokens.length - 1].equals(new TokenV1(TokenV1.TT_KEYWORD, "twimes"))) { // For loops
        if (Arrays.asList(tokens).contains(TokenV1.C_TOK)) {
          Token[] exprs = Arrays.copyOfRange(tokens, 1, tokens.length - 1);
          ArrayList<Token> exA = new ArrayList<>();
          ArrayList<Token> exB = new ArrayList<>();
          boolean secondPart = false;
          Token delim = TokenV1.C_TOK;
          for (int i = 0; i < exprs.length; i++) {
            if (exprs[i].equals(delim)) {
              secondPart = true;
            } else if (secondPart) {
              exB.add(exprs[i]);
            } else {
              exA.add(exprs[i]);
            }
          }
          Token[] exAa = exA.toArray(new TokenV1[0]);
          Object resultA = new Runner(fn, this).interpret(Arrays.copyOfRange(exAa, 0, exAa.length));
          Token[] exAb = exB.toArray(new TokenV1[0]);
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
            
            return new IllegalTypeError(pos,
                new StringBuilder(
                    "Naooo uwu da start and ewnd lowp amwownt mwst bwe a numbwer, inpwtwed start lowp amwownt: ")
                        .append(resultA).append(", impwtwed ewnd lowp amwownt: ").append(" ._.").append(resultB)
                        .toString());
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
              
              return new IllegalTypeError(pos,
                  new StringBuilder("Naooo uwu da lowp amwownt mwst bwe a numbwer, inpwtwed lowp amwownt: ")
                      .append(result).append(" ._.").toString());
            }
          } else {
            
            return new InvalidSyntaxError(pos, "Naooo uwu expwecwed lowp amwownt hewe ._.");
          }
        }
      } else {
        // If & switch tatements
        if (tokens.length > 0 && tokens[0].equals(TokenV1.C_STARTIF)) {
          if (tokens[tokens.length - 1].equals(TokenV1.C_TILDE)) {
            // Switch statements
            switches.push(new Switch(this));
            Object o = new Runner(fn, strippedCopy()).interpret(Arrays.copyOfRange(tokens, 1, tokens.length - 2));
            if (o instanceof org.kawaiilang.Error) {
              return o;
            } else if (!((o instanceof Double) && (((Double) o) % 1 == 0))) {
              
              return new InvalidParameterError(pos, "Naooo uwu numwer tw switch mwst be awn intewer ._.");
            }
            caseNumToExecute = ((Double) o).intValue();
            return null;
          } else {
            // If statements
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
              // Signifies the if statement is inside a false if statement or it is an else
              // after a statement that is true
              activeElseStatements++;
              prevDoInterpret.add(false);
              // So that any ewndNotice statements don't mess anything up
            } else if (result instanceof org.kawaiilang.Error) {
              return result;
            } else {
              
              return new BadOprandTypeError(pos,
                  new StringBuilder("Naooo uwu bwad reswlt twypes fwr \"if\" statement: reswlt: ")
                      .append(result.toString()).append(" ._.").toString());
            }
          }
        }

        if (doInterpret == null || doInterpret == true) {
          // This goes down alllllll the way

          // Checks for end of if.
          if (doInterpret != null && tokens.length == 1 && tokens[0].equals(TokenV1.C_ENDIFK)) {
            justHadElse = false;
            activeElseStatements--;
            prevDoInterpret.remove(activeElseStatements);
            return null;
          }
          // Breaks in switch statements
          else if (tokens.length == 1 && tokens[0].equals(TokenV1.C_BREAKK)) {
            return BREAK_OBJECT;
          }
          // NOT operation
          else if (tokens.length > 0 && tokens[0].equals(TokenV1.C_NOTK)) {
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
                
                return new BadOprandTypeError(pos,
                    new StringBuilder("Naooo uwu u hab bwad owpwand twypes fwr \"nawt\" owpewawor: owpwand: ")
                        .append(result.toString()).append(" ._.").toString());
              }
            } else {
              
              return new InvalidSyntaxError(pos, "Naooo uwu opwand not found for \"nawt\"opewawor ._.");
            }
          }

          // AND, OR, XOR operations
          if (Arrays.asList(tokens).contains(TokenV1.C_ANDK) || Arrays.asList(tokens).contains(TokenV1.C_ORK)
              || Arrays.asList(tokens).contains(TokenV1.C_XORK)) {
            ArrayList<Token> exA = new ArrayList<>();
            ArrayList<Token> exB = new ArrayList<>();
            boolean secondPart = false;
            Token oper = null;
            for (int i = 0; i < tokens.length; i++) {
              if (tokens[i].getValue() != null && (tokens[i].getValue().equals("awnd")
                  || tokens[i].getValue().equals("orw") || tokens[i].getValue().equals("xwr"))) {
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
            return evalLogic(exA.toArray(new TokenV1[0]), oper, exB.toArray(new TokenV1[0]));
          }

          // ==, >, <, >=, <= operations
          if (Arrays.asList(tokens).contains(new TokenV1(TokenV1.TT_EQUALS))
              || Arrays.asList(tokens).contains(TokenV1.C_LT) || Arrays.asList(tokens).contains(TokenV1.C_GT)
              || Arrays.asList(tokens).contains(TokenV1.C_LTE) || Arrays.asList(tokens).contains(TokenV1.C_GTE)) {
            ArrayList<Token> exA = new ArrayList<>();
            ArrayList<Token> exB = new ArrayList<>();
            boolean secondPart = false;
            Token oper = null;
            for (int i = 0; i < tokens.length; i++) {
              if (tokens[i].getType() == TokenV1.TT_EQUALS || tokens[i].getType() == TokenV1.TT_LT
                  || tokens[i].getType() == TokenV1.TT_GT || tokens[i].getType() == TokenV1.TT_LTE
                  || tokens[i].getType() == TokenV1.TT_GTE) {
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
            return evalComparison(exA.toArray(new TokenV1[0]), oper, exB.toArray(new TokenV1[0]));
          }

          OwOList<?> origListPool = listPool.clone();

          while (true) { // Main interpretation loop
            if (currentToken == null) {
              break;
            } else if (currentToken instanceof org.kawaiilang.Error) {
              return currentToken;
            } else if (currentToken.getType().equals(TokenV1.TT_INT)
                || currentToken.getType().equals(TokenV1.TT_FLOAT)) {
              expr.append(currentToken.getValue());
              lastToken = currentToken;
              advance();
            } else if (currentToken.getType().equals(TokenV1.TT_STR)) {
              String str = (String) currentToken.getValue();
              stringPool.append(str);
              advance();
            } else if (currentToken.getType().equals(TokenV1.TT_LIST)) {
              OwOList l = (OwOList) currentToken.getValue();
              listPool = l;
              advance();
            } else if (currentToken.getType().equals(TokenV1.TT_NOTHING)) {
              return null;
            } else if (currentToken.getType().equals(TokenV1.TT_LSQU)) { // List creation
              advance();
              if (currentToken.getType().equals(TokenV1.TT_RSQU)) {
                return new OwOList<Object>();
              } else {
                ArrayList<Token> at = new ArrayList<>();
                OwOList<?> l = listPool.clone();
                // System.out.println(l.getType());
                Position origP = pos.clone();
                Token[] origT = tokens.clone();
                while (true) {
                  if (currentToken.getType().equals(TokenV1.TT_COMMA)) {
                    l.appendObj(new Runner(fn, this).interpret(at.toArray(new TokenV1[0])));
                    at = new ArrayList<>();
                    pos = origP;
                    tokens = origT;
                  } else if (currentToken.getType().equals(TokenV1.TT_RSQU)) {
                    l.appendObj(new Runner(fn, this).interpret(at.toArray(new TokenV1[0])));
                    if (l.length() > 1)
                      l.pop(1);
                    return l;
                  } else {
                    at.add(currentToken);
                  }
                  advance();
                }
              }
            } else if (currentToken.getType().equals(TokenV1.TT_VARNAME)) {
              // Special variable for changing loop index
              if (currentToken.getValue().equals("inwex") && loop instanceof Loop) {
                Loop l = (Loop) loop;
                if (l != null) {
                  if (pos.getIdx() + 1 < tokens.length && tokens[pos.getIdx() + 1].getType() == TokenV1.TT_ASSIGN) {
                    lastToken = currentToken;
                    advance();
                    Object value = new Runner(fn, this)
                        .interpret(Arrays.copyOfRange(tokens, pos.getIdx() + 1, tokens.length));
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
                      
                      return new IllegalTypeError(pos,
                          "Naooo uwu da twype of variable fwor uwpdwated inwex mwst bwe Numwer ._.");
                    }
                  }
                  // Otherwise it is getting of loop index
                  Token varReplaced = new TokenV1(TokenV1.TT_INT, l.getIdx());
                  tokens[pos.getIdx()] = varReplaced;
                  pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
                  advance();
                  Object value = interpret();
                  return value;
                } else {
                  
                  return new IllegalAssignmentError(pos,
                      "Naooo uwu u cwannot gwet orw updwate inwex witowt lowp ._.");
                }
              }
              // Same as above, but for changing max index
              if (currentToken.getValue().equals("max") && loop instanceof Loop) {
                Loop l = (Loop) loop;
                if (l != null) {
                  if (pos.getIdx() + 1 < tokens.length && tokens[pos.getIdx() + 1].getType() == TokenV1.TT_ASSIGN) {
                    lastToken = currentToken;
                    advance();
                    Object value = new Runner(fn, this)
                        .interpret(Arrays.copyOfRange(tokens, pos.getIdx() + 1, tokens.length));
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
                      
                      return new IllegalTypeError(pos,
                          "Naooo uwu da twype of variable fwor uwpdwated ,max inwex mwst bwe Numwer ._.");
                    }
                  }
                  // Otherwise it is getting of loop max index
                  Token varReplaced = new TokenV1(TokenV1.TT_INT, l.getMax());
                  tokens[pos.getIdx()] = varReplaced;
                  pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
                  advance();
                  Object value = interpret();
                  return value;
                } else {
                  
                  return new IllegalAssignmentError(pos,
                      "Naooo uwu u cwannot gwet orw updwate max inwex witowt lowp ._.");
                }
              }

              // Variable reassignment
              if (pos.getIdx() + 1 < tokens.length && tokens[pos.getIdx() + 1].getType() == TokenV1.TT_ASSIGN
                  && heap.containsKey(currentToken)) {
                lastToken = currentToken;
                advance();
                Object value = new Runner(fn, this)
                    .interpret(Arrays.copyOfRange(tokens, pos.getIdx() + 1, tokens.length));
                if (value instanceof org.kawaiilang.Error) {
                  return value;
                } else if (verifyVar(heap.get(lastToken).getType(), value)) {
                  currentToken = lastToken;
                  Variable var = new Variable(heap.get(currentToken).getType(), value);
                  heap.put(currentToken, var);
                  return value;
                } else {
                  return new IllegalTypeError(pos, new StringBuilder("Naooo uwu da twype of variable, ").append(heap.get(lastToken).getType().getValue()).append(", is nawt as decwared ._.").toString());
                }
              }

              // Variable recall
              Variable varData = heap.get(currentToken);
              if (varData != null) {
                Token type = varData.getType();
                Object stored = varData.getValue();
                boolean listOper = false; // true when array get or slice operations are called
                Token varReplaced = null;
                if (type.getValue().equals("Numwer")) {
                  if (stored instanceof Integer) {
                    varReplaced = new TokenV1(TokenV1.TT_INT, stored);
                  } else if (stored instanceof Double) {
                    varReplaced = new TokenV1(TokenV1.TT_FLOAT, stored);
                  }
                } else if (type.getValue().equals("Stwing")) {
                    varReplaced = new TokenV1(TokenV1.TT_STR, stored);
                } else if (type.getValue().equals("Fwnctwion")) {
                  Function retrievedFunc = (Function) stored;
                  ArrayList<Object> inputs = null; // List of input parameters to pass into function
                  advance();
                  if (currentToken == null) { // Just return the function itself
                    return retrievedFunc;
                  } else if (!currentToken.equals(TokenV1.C_PARAM)) {
                    
                    return new InvalidSyntaxError(pos, "Naooo \"UwU\" expected ._.");
                  }
                  advance();
                  ArrayList<Token> param2eval = new ArrayList<>();
                  Token[] origT = tokens.clone();
                  int nestedParamCounter = 0;
                  while (true) {
                    Position origP = pos.clone();
                    // Turns out it wasn't a nested function, so just process it normally.
                    if (currentToken == null) {
                      if (!param2eval.isEmpty()) {
                        if (inputs == null) {
                          inputs = new ArrayList<>();
                        }
                        // System.out.println(param2eval);
                        inputs.add(new Runner(fn, this).interpret(param2eval.toArray(new TokenV1[0])));
                        tokens = origT;
                        pos = origP;
                      }
                      break;
                    } else if (currentToken.equals(TokenV1.C_PARAM)) {
                      // Checks if last variable is a function, if so, it would assume its a nested
                      // function.
                      if (lastToken != null && lastToken.getType().equals(TokenV1.TT_VARNAME)
                          && heap.get(lastToken).getType().getValue().equals("Fwnctwion")) {
                        nestedParamCounter++;
                        param2eval.add(currentToken);
                      } else {
                        nestedParamCounter--;
                        if (nestedParamCounter < 0) { // If the last UwU is detected
                          if (!param2eval.isEmpty()) {
                            if (inputs == null) {
                              inputs = new ArrayList<>();
                            }
                            // System.out.println(param2eval);
                            inputs.add(new Runner(fn, this).interpret(param2eval.toArray(new TokenV1[0])));
                            tokens = origT;
                            pos = origP;
                          }
                          break;
                        } else { // Otherwise add it to params2eval
                          param2eval.add(currentToken);
                        }
                      }

                    } else if (currentToken.equals(TokenV1.C_COMMA)) {
                      if (inputs == null) {
                        inputs = new ArrayList<>();
                      }
                      // System.out.println(param2eval);
                      inputs.add(new Runner(fn, this).interpret(param2eval.toArray(new TokenV1[0])));
                      tokens = origT;
                      pos = origP;
                      param2eval = new ArrayList<>();
                    } else {
                      param2eval.add(currentToken);
                    }
                    lastToken = currentToken;
                    advance();
                  }
                  if (retrievedFunc.getParameterTypes() == null) {
                    Object o;
                    if (inputs != null) {
                      // Checks for any overloaded functions
                      Function ovld = getOverloadedFunction(retrievedFunc.getName(), inputs);
                      if (ovld != null) {
                        o = ovld.call(inputs);
                        ovld.resetActions();
                        return o;
                      } else {
                        
                        return new InvalidParameterError(pos,
                            "Naooo uwu awgwmwents r giwen wen de fwnctwion does nawt hwas awgwments ._.");
                        // No overloaded function so just return error
                      }
                    } else {
                      o = retrievedFunc.call();
                      retrievedFunc.resetActions();
                    }
                    return o;
                  } else {
                    // System.out.println(inputs);
                    Object o = null;
                    if (inputs != null) {
                      try {
                        o = retrievedFunc.call(inputs);
                      } catch (ArrayIndexOutOfBoundsException e) { // If more parameters than expected
                        Function ovld = getOverloadedFunction(retrievedFunc.getName(), inputs);
                        if (ovld != null) {
                          o = ovld.call(inputs);
                          ovld.resetActions();
                          return o;
                        } else {
                          if (o == null) {
                            
                            return new InvalidParameterError(pos,
                                new StringBuilder("Naooo uwu nao too mwany awgwmwents r giwen fwr the fwnctwion ")
                                    .append(retrievedFunc.getName()).append(" ._.").toString());
                          }
                          return o; // No overloaded function so just return the error
                        }
                      } finally {
                        retrievedFunc.resetActions();
                      }
                    }
                    if (inputs == null || o instanceof InvalidParameterError) {
                      // Checks for any overloaded functions
                      Function ovld = getOverloadedFunction(retrievedFunc.getName(), inputs);
                      if (ovld != null) {
                        if (inputs == null) {
                          o = ovld.call();
                        } else {
                          o = ovld.call(inputs);
                        }
                        ovld.resetActions();
                        return o;
                      } else {
                        if (o == null) {
                          
                          return new InvalidParameterError(pos,
                              "Naooo uwu nao awgwmwents r giwen wen de fwnctwion hwas awgwments ._.");
                        }
                        return o; // No overloaded function so just return the error
                      }
                    }
                    return o;
                  }
                  // End function recall
                } else if (type.getValue().equals("Lwist")) {
                    advance();
                    if (currentToken != null && currentToken.getType().equals(TokenV1.TT_LSQU)) { // Get and slice
                      listOper = true;
                      OwOList l = (OwOList) stored;
                      Position origP = pos.clone();
                      Position origOrigP = pos.clone();
                      Token[] origT = tokens.clone();
                      boolean slice = false;
                      ArrayList<Token> at = new ArrayList<>();
                      ArrayList<Token> at2 = new ArrayList<>();
                      getNslice: while (true) {
                        advance();
                        origP.advance();
                        if (currentToken.getType().equals(TokenV1.TT_RSQU)) {
                          if (at.isEmpty()) {
                            if (!at2.isEmpty()) { // Slice at end
                              Object o = new Runner(fn, this).interpret(at2.toArray(new TokenV1[0]));
                              pos = origP;
                              tokens = origT;
                              int i = ((Double) o).intValue();
                              if (i >= l.length()) {
                                
                                return new IndexOutOfBoundsError(pos, "uwu naooo inwex iws owt ofw bwownds ._.");
                              }
                              OwOList slicedList = l.slice(0, i);
                              varReplaced = new TokenV1(TokenV1.TT_LIST, slicedList);
                              break getNslice;
                            } else {
                              
                              return new InvalidParameterError(pos,
                                  "uwu naooo u must spwecify an inwex between the sqware bwackets ._.");
                            }
                          } else if (at2.isEmpty()) {
                            if (slice) { // Slice at beginning
                              Object o = new Runner(fn, this).interpret(at.toArray(new TokenV1[0]));
                              pos = origP;
                              tokens = origT;
                              int i = ((Double) o).intValue();
                              if (i < 0) {
                                
                                return new IndexOutOfBoundsError(pos, "uwu naooo inwex iws owt ofw bwownds ._.");
                              }
                              OwOList slicedList = l.slice(i, l.length());
                              varReplaced = new TokenV1(TokenV1.TT_LIST, slicedList);
                              break getNslice;
                            } else { // Get
                              // System.out.println(at);
                              Object o = new Runner(fn, this).interpret(at.toArray(new TokenV1[0]));
                              // System.out.println(o);
                              pos = origP;
                              tokens = origT;
                              int i = ((Double) o).intValue();
                              if (i >= l.length()) {
                                
                                return new IndexOutOfBoundsError(pos, "uwu naooo inwex iws owt ofw bwownds ._.");
                              }
                              Object fromList = l.get(i);
                              if (fromList instanceof Double) {
                                varReplaced = new TokenV1(TokenV1.TT_FLOAT, fromList);
                              } else if (fromList instanceof String) {
                                varReplaced = new TokenV1(TokenV1.TT_STR, fromList);
                              } else if (fromList instanceof Function) {
                                Function retrievedFunc = (Function) fromList;
                                varReplaced = new TokenV1(TokenV1.TT_VARNAME, retrievedFunc.getName());
                              } // More datatypes here
                              break getNslice;
                            }
                          } else { // Slice at beginning and end
                            Object o = new Runner(fn, this).interpret(at.toArray(new TokenV1[0]));
                            pos = origP;
                            tokens = origT;
                            Object o2 = new Runner(fn, this).interpret(at2.toArray(new TokenV1[0]));
                            pos = origP;
                            tokens = origT;
                            int i = ((Double) o).intValue();
                            int j = ((Double) o2).intValue();
                            if ((i < 0 || i >= l.length()) || (j > l.length() || j < 0)) {
                              
                              return new IndexOutOfBoundsError(pos, "uwu naooo inwex iws owt ofw bwownds ._.");
                            }
                            OwOList slicedList = l.slice(i, j);
                            varReplaced = new TokenV1(TokenV1.TT_LIST, slicedList);
                            pos = origP;
                            break getNslice;
                          }
                        } else if (currentToken.equals(TokenV1.C_TOK)) {
                          slice = true;
                        } else if (!slice) {
                          at.add(currentToken);
                        } else {
                          at2.add(currentToken);
                        }
                      }
                      pos = origOrigP;
                      tokens = origT;
                      // End getNslice
                    } else {
                      listOper = false;
                      retreatWithoutUpdating();
                      varReplaced = new TokenV1(TokenV1.TT_LIST, stored);
                    }
                  // End list recall
                } else if (type.getType().equals(TokenV1.TT_CLASS)) {
                  // Recall of object
                  if (tokens.length > 1 && tokens[pos.getIdx() + 1].equals(TokenV1.C_DOK)) {
                    Object o = heap.get(currentToken).getValue();
                      OwObject owoo = (OwObject) o;
                        return owoo.doActions(Arrays.copyOfRange(tokens, pos.getIdx() + 2, tokens.length));
                  } else {
                    Object o = heap.get(currentToken).getValue();
                      OwObject owoo = (OwObject) o;
                        return owoo;
                  }
                }
                // More variable types in the future
                tokens[pos.getIdx()] = varReplaced;
                // System.out.println("varReplaced "+ varReplaced);
                if (listOper) {
                  ArrayList<Token> tmp = new ArrayList<>(Arrays.asList(tokens));
                  tmp.remove(0);
                  // System.out.println(tmp);
                  int i = pos.getIdx();
                  while (true) {
                    Token t = tmp.remove(i);
                    if (t.getType().equals(TokenV1.TT_RSQU)) {
                      tokens = tmp.toArray(new TokenV1[0]);
                      break;
                    }
                  }
                }
                // System.out.println(Arrays.toString(tokens));
                pos = new Position(-1, 0, -1, fn, Arrays.toString(tokens));
                advance();
                Object value = interpret();
                return value == null ? new OwOList<Object>() : value;
              } else {
                String varName = (String) currentToken.getValue();
                
                return new UnassignedVariableError(pos, new StringBuilder("Naooo uwu da wariable ")
                    .append(varName).append(" newer asswigned ._.").toString());
              }
            } else if (currentToken.getType() == TokenV1.TT_LPAREN) {
              expr.append("(");
              lastToken = currentToken;
              advance();
            } else if (currentToken.getType() == TokenV1.TT_RPAREN) {
              expr.append(")");
              lastToken = currentToken;
              advance();
            } else if (lastToken == null || currentToken.getType() == TokenV1.TT_ADD
                || currentToken.getType() == TokenV1.TT_MINUS || currentToken.getType() == TokenV1.TT_MUL
                || currentToken.getType() == TokenV1.TT_DIV || currentToken.getType() == TokenV1.TT_MOD) {
              if (lastToken != null
                  && (lastToken.getType() == TokenV1.TT_ADD || lastToken.getType() == TokenV1.TT_MINUS)) {
                if (currentToken.getType() == TokenV1.TT_ADD) {
                  // adding a plus before numbers doesn't change anything
                  lastToken = currentToken;
                  advance();
                } else if (currentToken.getType() == TokenV1.TT_MINUS) {
                  // adding a minus before numbers makes negative positive and positive negative
                  if (lastToken.getType() == TokenV1.TT_ADD) {
                    expr.deleteCharAt(expr.length() - 1);
                    expr.append("-");
                    lastToken = new TokenV1(TokenV1.TT_MINUS);
                    advance();
                  } else {
                    expr.deleteCharAt(expr.length() - 1);
                    expr.append("+");
                    lastToken = new TokenV1(TokenV1.TT_ADD);
                    advance();
                  }
                } else {
                  
                  return new InvalidSyntaxError(pos,
                      new StringBuilder("Naooo uwu ").append(currentToken.toString())
                          .append(" cwannot cwome awfter awnowther owpewation ._.").toString());
                }
              } else if (currentToken.getType() == TokenV1.TT_ADD || currentToken.getType() == TokenV1.TT_MINUS
                  || currentToken.getType() == TokenV1.TT_MUL || currentToken.getType() == TokenV1.TT_DIV
                  || currentToken.getType() == TokenV1.TT_MOD) {
                if (lastToken != null
                    && (lastToken.getType() != TokenV1.TT_INT && lastToken.getType() != TokenV1.TT_FLOAT)) {
                  expr.append("0").append(currentToken.getType());
                } else {
                  if (currentToken.getType().equals(TokenV1.TT_MUL)) {
                    expr.append('*');
                  } else {
                    expr.append(currentToken.getType());
                  }
                }
                lastToken = currentToken;
                advance();
              } else if (currentToken.getType().equals(TokenV1.TT_LPAREN)) {
                expr.append("(");
                lastToken = currentToken;
                advance();
              } else if (currentToken.getType().equals(TokenV1.TT_VARTYPE)
                  || currentToken.getType().equals(TokenV1.TT_CLASS)) {
                // Variable assignment
                Token type = currentToken;
                advance();
                if (type.getValue().equals("Fwnctwion")) { // Cannot declare function like this!
                  
                  return new IllegalAssignmentError(pos, "Naooo uwu u cwannot asswign fwnctwion lwike dat ._.");
                } else if (currentToken != null && currentToken.getType().equals(TokenV1.TT_VARNAME)) {
                  lastToken = currentToken;
                  advance();
                  if (currentToken != null && currentToken.getType().equals(TokenV1.TT_ASSIGN)) {
                    return makeVar(type, lastToken);
                  } else {
                    
                    String varName = (String) lastToken.getValue();
                    return new UnassignedVariableError(pos, new StringBuilder("Naooo uwu wariable ")
                        .append(varName).append(" decwared but nao walue was asswigned ._.").toString());
                  }
                } else {
                  
                  return new InvalidSyntaxError(pos, new StringBuilder("Naooo uwu ")
                      .append(currentToken.toString()).append(" iws iwegal here ._.").toString());
                }
              } else if (currentToken.getType().equals(TokenV1.TT_KEYWORD)) {
                if (currentToken.getValue().equals("make")) { // Class construction
                  advance();
                  lastToken = currentToken; // The class to make
                  if (currentToken.getType().equals(TokenV1.TT_CLASS)) {
                    advance();
                    if (currentToken != null && currentToken.equals(TokenV1.C_PARAM)) {
                      // With parameter
                      // Evaluates all the parameters first
                      ArrayList<Object> params = new ArrayList<>();
                      int totalParamIndicators = 0; // The total occurrence of the C_PARAM token
                      for (Token token : tokens) {
                        if (token.equals(TokenV1.C_PARAM)) {
                          totalParamIndicators++;
                        }
                      }
                      if (totalParamIndicators % 2 != 0) {
                        
                        return new InvalidSyntaxError(pos, "Unclosed parameter indicator");
                      }
                      totalParamIndicators--;
                      ArrayList<Token> param2eval = new ArrayList<>();
                      while (totalParamIndicators > 0) {
                        advance();
                        if (currentToken.equals(TokenV1.C_COMMA)) {
                          params.add(new Runner(fn, this).interpret(param2eval.toArray(new Token[0])));
                          param2eval = new ArrayList<>();
                        } else {
                          if (currentToken.equals(TokenV1.C_PARAM)) {
                            totalParamIndicators--;
                          }
                          param2eval.add(currentToken);
                        }
                      }
                      // After evaluating parameters, invoke the constructor
                      OwObject o = (OwObject) heap.get(lastToken).getValue();
                      return o.constructWithConstructor(params);
                    } else {
                      // Without parameter
                      OwObject o = (OwObject) heap.get(lastToken).getValue();
                      return o.construct();
                    }
                  } else {
                    
                    return new InvalidSyntaxError(pos, new StringBuilder("Naooo uwu \"")
                        .append(lastToken.toString()).append("\" is nawt a proper cwass ._.").toString());
                  }
                } else if (currentToken.getValue().equals("dewete")) { // Variable deletion
                  advance();
                  if (currentToken.getType().equals(TokenV1.TT_VARNAME)) {
                    heap.remove(currentToken);
                    return null;
                  } else {
                    
                    return new InvalidSyntaxError(pos,
                        new StringBuilder("Naooo uwu ").append(currentToken.toString())
                            .append(" is nawt wariable and i cwannot dewete ._.").toString());
                  }
                } else if (currentToken.getValue().equals("ewlse")) {
                  return null; // Else does nothing on its own
                } // other keywords that appear at the beginning of statement goes here
                else {
                  
                  return new InvalidSyntaxError(pos, new StringBuilder("Naooo uwu ")
                      .append(currentToken.toString()).append(" iws iwegal here ._.").toString());
                }
              } else {
                
                return new InvalidSyntaxError(pos, new StringBuilder("Naooo uwu ")
                    .append(currentToken.toString()).append(" is iwegal here ._.").toString());
              }
            } else if (lastToken.getType() == TokenV1.TT_INT) {
              if (currentToken.getType() == TokenV1.TT_ADD || currentToken.getType() == TokenV1.TT_MINUS
                  || currentToken.getType() == TokenV1.TT_MUL || currentToken.getType() == TokenV1.TT_DIV
                  || currentToken.getType() == TokenV1.TT_MOD) {
                expr.append(currentToken.getType());
                lastToken = currentToken;
                advance();
              } // future operations go below here
            } else {
              
              return new InvalidSyntaxError(pos, new StringBuilder("Naooo uwu ").append(currentToken.toString())
                  .append(" is iwegal here ._.").toString());
            }
          }
          if (stringPool.length() > 0) {
            return stringPool.toString();
          } else if (listPool.length() > 0) {
            OwOList<?> tmp = listPool;
            listPool = origListPool;
            return tmp;
          } else if (expr.length() > 0) {
            try {
              String s = expr.toString();
              // System.out.println(s);
              if (s.contains("/0")) {
                
                return new DivisionByZeroError(pos);
              }
              Expression e = new ExpressionBuilder(s).build();
              Object result = e.evaluate();
              return result;
            } catch (Exception ex) {
              
              return new RunTimeError(pos, ex.getMessage());
            }
          } else {
            return null;
          } // end main loop
        } else { // doInterpret is FALSE
          // Only care about the endif and evaluate nothing
          if (tokens.length == 1 && tokens[0].equals(TokenV1.C_ENDIFK)) {
            justHadElse = false;
            activeElseStatements--;
            prevDoInterpret.remove(activeElseStatements);
          }
          return null;
        }
      } // end Loop if
    } // end record loop
    return null;
  } // endFunction

  private Object makeVar(Token type, Token name) {
    advance();
    Object value = new Runner(fn, this).interpret(Arrays.copyOfRange(tokens, pos.getIdx(), tokens.length));
    if (value instanceof org.kawaiilang.Error) {
      return value;
    } else if (name.getValue().equals("inwex")) {
      
      return new IllegalAssignmentError(pos, "Naooo uwu u cwannot set inwex witowt lowp ._.");
    } else if (verifyVar(type, value)) {
      Variable var = new Variable(type, value);
      heap.put(name, var);
      return value;
    } else {
      return new IllegalTypeError(pos, new StringBuilder("Naooo uwu da twype of variable, ").append(type.getValue()).append(", is nawt as decwared ._.").toString());
    }
  }

  // Verifies if the token matches the supposed type
  private boolean verifyVar(Token supposedType, Object actualObj) {
    boolean classTest = false;
    if (actualObj instanceof OwObject && supposedType.getValue().equals(TokenV1.TT_CLASS)) {
      OwObject owoo = (OwObject) actualObj;
      return owoo.className().equals(supposedType.getValue()) || owoo.hasParent((String) supposedType.getValue());
    }
    return ((actualObj instanceof Integer || actualObj instanceof Double) && supposedType.getValue().equals("Numwer"))
        || (actualObj instanceof String && supposedType.getValue().equals("Stwing"))
        || (actualObj instanceof OwOList && supposedType.getValue().equals("Lwist")) || classTest;
  }

  private Object evalComparison(Token[] exprA, Token oper, Token[] exprB) {
    Object resultA = new Runner(fn, this).interpret(exprA);
    Object resultB = new Runner(fn, this).interpret(exprB);
    if (oper.getType() == TokenV1.TT_EQUALS) {
      if (resultA.equals(resultB)) {
        return 1.0;
      } else {
        return 0.0;
      }
    } else if (oper.getType() == TokenV1.TT_LT) {
      if (resultA instanceof Double && resultB instanceof Double) {
        Double a = (Double) resultA;
        Double b = (Double) resultB;
        if (a < b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    } else if (oper.getType() == TokenV1.TT_GT) {
      if (resultA instanceof Double && resultB instanceof Double) {
        Double a = (Double) resultA;
        Double b = (Double) resultB;
        if (a > b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    } else if (oper.getType() == TokenV1.TT_LTE) {
      if (resultA instanceof Double && resultB instanceof Double) {
        Double a = (Double) resultA;
        Double b = (Double) resultB;
        if (a <= b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    } else if (oper.getType() == TokenV1.TT_GTE) {
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

  // Creates a class based on the class name
  private OwObject declareClass(String name) {
    StringBuilder sb;
    if (fn.equals("<stdin>")) {
      return new OwObject(name);
    } else {
      // Get the current file directory
      sb = new StringBuilder(fn.split("(\\/(?!.*\\/)|\\\\(?!.*\\\\))(?!.*(\\/(?!.*\\/)|\\\\(?!.*\\\\)))")[0]);
      sb.append('/');
    }
    sb.append(name);
    return new OwObject(sb.toString());
  }

  // Same as above but with a parent class
  private OwObject declareClass(String name, OwObject parent) {
    StringBuilder sb;
    if (fn.equals("<stdin>")) {
      return new OwObject(name, parent);
    } else {
      // Get the current file directory
      sb = new StringBuilder(fn.split("(\\/(?!.*\\/)|\\\\(?!.*\\\\))(?!.*(\\/(?!.*\\/)|\\\\(?!.*\\\\)))")[0]);
      sb.append('/');
    }
    sb.append(name);
    return new OwObject(sb.toString(), parent);
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

      if (oper.getValue().equals("awnd")) {
        if (a && b) {
          return 1.0;
        } else {
          return 0.0;
        }
      } else if (oper.getValue().equals("orw")) {
        if (a || b) {
          return 1.0;
        } else {
          return 0.0;
        }
      } else if (oper.getValue().equals("xwr")) {
        if (a ^ b) {
          return 1.0;
        } else {
          return 0.0;
        }
      }
    }

    
    return new BadOprandTypeError(pos,
        new StringBuilder("Naooo uwu u hab bwad owpwand twypes fwr bwinwawy owpewawor \"").append(oper)
            .append("\": first owpwand: ").append(resultA.toString()).append(", second owpwand: ")
            .append(resultB.toString()).append(" ._.").toString());
  }

  // Gets the proper overloaded function given the name of the function and the
  // parameter types
  private Function getOverloadedFunction(String fName, Collection<?> c) {
    Map.Entry<String, String> e = new AbstractMap.SimpleEntry<>(fName, Function.toParameterTypeList(c));
    return overloadedFunctions.get(e);
  }

  public void addFunction(Function f) {
    heap.put(new TokenV1(TokenV1.TT_VARNAME, f.getName()),
        new Variable(new TokenV1(TokenV1.TT_VARNAME, "Fwnctwion"), f));
  }

  public void overloadFunction(Function f) {
    Map.Entry<String, String> e;
    if (f.getParameterTypes() == null) {
      e = new AbstractMap.SimpleEntry<>(f.getName(), null);
      overloadedFunctions.put(e, f);
    } else {
      StringBuilder sb = new StringBuilder();
      for (String s : f.getParameterTypes()) {
        sb.append(s).append('_');
      }
      e = new AbstractMap.SimpleEntry<>(f.getName(), sb.toString());
      overloadedFunctions.put(e, f);
    }
  }

  // Returns an interpreter instance with copied heap and functions,
  // but different variables for very specific contexts, like position and if
  // statement declarations
  public KawaiiLangRuntime strippedCopy() {
    KawaiiLangRuntime i = new KawaiiLangRuntime(fn, tokens.clone());
    i.heap = heap;
    i.overloadedFunctions = overloadedFunctions;
    return i;
  }

}