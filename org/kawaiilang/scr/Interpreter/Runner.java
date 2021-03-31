package org.kawaiilang;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

public final class Runner {

  public static final HashMap<String, String> UWU_KEY = new HashMap<>();
  
  private static Token anyErrorsFound = null;	//Any errors found by the lexer will go here

  static {
    UWU_KEY.put("awdd", "+");
    UWU_KEY.put("mwinws", "-");
    UWU_KEY.put("mwltipwy", "*");
    UWU_KEY.put("diwide", "/");
    UWU_KEY.put("mwd", "%");
    UWU_KEY.put("waifu", "cwass");
    UWU_KEY.put("husbando", "cwass");
    UWU_KEY.put("^_^ewndWaifu", "ewndCwass");
    UWU_KEY.put("^_^ewndHusbando", "^_^ewndCwass");
  }

  private String fileLocation = "<stdin>";
  private KawaiiLangRuntime runtime;

  public Runner() {
    runtime = new KawaiiLangRuntime(fileLocation);
  }

  public Runner(String fileLocation) {
    this.fileLocation = fileLocation;
    runtime = new KawaiiLangRuntime(fileLocation);
  }

  //Sets the runtime for objects
  Runner(String fileLocation, KawaiiLangRuntime runtime) {
    this.fileLocation = fileLocation;
    this.runtime = runtime;
  }

  public static String unUwUfy(String uwufiedText) {
    String[] lines = uwufiedText.split("\n");
    for (int i = 0; i < lines.length; i++) {
      for (String s : UWU_KEY.keySet()) {
        String beginning = new StringBuilder(s).append(" ").toString();
        if (lines[i].startsWith(beginning)) {
          String subLine = lines[i].substring(s.length());
          lines[i] = new StringBuilder(UWU_KEY.get(s)).append(subLine).toString();
        }
        String toReplace = new StringBuilder(" ").append(s).toString();
        String replaceWith = new StringBuilder(" ").append(UWU_KEY.get(s)).toString();
        lines[i] = lines[i].replaceAll(toReplace, replaceWith);
      }
    }
    StringBuilder strBuilder = new StringBuilder();
    for (String line : lines) {
      //Boolean types
      line = line.replaceAll(":<", " 0 ");
      line = line.replaceAll(":>", " 1 ");
      strBuilder.append(line).append("\n");
    }
    //System.out.println(strBuilder.toString());
    return strBuilder.toString();
  }

  public void setFileLocation(String fileLocation) {
    this.fileLocation = fileLocation;
    runtime.setFileLocation(fileLocation);
  }

  public void run() throws IOException {
    File file = new File(fileLocation);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line;
    int i = 1;
    while ((line = br.readLine()) != null) {
      Object result = null;
      long emc = line.codePoints().filter(ch -> ch == '!').count();
      long qmc = line.codePoints().filter(ch -> ch == '?').count();
      if (line.isBlank()) {
    	//System.out.println("Blank " + i);
    	i++;
      } else {
    	//System.out.println(i);
        i++;
    	if (emc + qmc > 1) {
          //Splits line if it ends with "!" or "?", but allows typing the two symbols with "\!" and "\?"
          String[] splitInto = line.split("((?<=!)(?<!\\\\!)|(?<=\\?)(?<!\\\\\\?))");
          for (String portionToEval : splitInto) {
            result = eval(portionToEval);
          }
        } else {
          result = eval(line);
        }
        runtime.advanceLine();
        if (result instanceof org.kawaiilang.Error) {
          	System.out.print("Error: line ");
          	System.out.print(i);
          	System.out.print(" of ");
      		System.out.println(result);
      		br.close();
      		return;
        }
      }
      
    }
    br.close();
  }

  public Object eval(String text) {
	  if (text.startsWith(".-.")) { //Comment
	    return null;
	  }
    Lexer lexer = new Lexer(fileLocation, unUwUfy(text));
    Token[] tokens = lexer.makeTokens();
    Arrays.asList(tokens).forEach(t -> {
      if (t instanceof org.kawaiilang.Error) {
    	  anyErrorsFound = t;
      }
    });
    if (anyErrorsFound != null) {
      Object tmp = anyErrorsFound;
      anyErrorsFound = null;
      return tmp;
    } else {
      return interpret(tokens);
    }
  }

  public Object interpret(Token[] tokens) {
    runtime.setTokens(tokens);
    Object result = runtime.interpret();

    //Prints the result so I can actually see what is going on. Every single one of them. Ah yes the pains of debugging.
    /*if (result == null) {
      System.out.println("nwthin");
    } else {
      System.out.println(result);
    }*/
    return result;
  }

}