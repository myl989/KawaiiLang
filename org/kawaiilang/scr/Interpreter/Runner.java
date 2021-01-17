package org.kawaiilang;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.HashMap;

public class Runner {

  /*
   * TO USE:
   * 
   * 1. Import package
   * `Import org.kawaiilang.Runner;`
   *
   * 2. Create Runner class
   * `Runner r = new Runner([file location name]);`
   *
   * 3. Run the file
   * `r.run();`
   */

  public static final HashMap<String, String> UWU_KEY;

  static {

    HashMap<String, String> uwuKi = new HashMap<>();
    uwuKi.put("awdd", "+");
    uwuKi.put("mwinws", "-");
    uwuKi.put("mwltipwy", "*");
    uwuKi.put("diwide", "/");
    uwuKi.put("mwd", "%");
    UWU_KEY = uwuKi;

  }

  private String fileLocation = "<stdin>";
  private Interpreter interpreter;

  public Runner() {
    interpreter = new Interpreter(fileLocation);
  }

  public Runner(String fileLocation) {
    this.fileLocation = fileLocation;
    interpreter = new Interpreter(fileLocation);
  }

  //internal use only
  Runner(String fileLocation, Interpreter interpreter) {
    this.fileLocation = fileLocation;
    this.interpreter = interpreter;
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
    interpreter.setFileLocation(fileLocation);
  }

  public void run() throws IOException {
    File file = new File(fileLocation);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line;
    while ((line = br.readLine()) != null) {
      long emc = line.codePoints().filter(ch -> ch == '!').count();
      long qmc = line.codePoints().filter(ch -> ch == '?').count();
      if (emc + qmc > 1) {
        //Splits line if it ends with "!" or "?"
        String[] splitInto = line.split("(?<=!)|(?<=\\?)");
        for (String portionToEval : splitInto) {
          eval(portionToEval);
        }
      } else {
        eval(line);
      }
    }
    //make it not line dependant in the future?
  }

  public Object eval(String text) {
    Lexer lexer = new Lexer(fileLocation, unUwUfy(text));
    Token[] tokens = lexer.makeTokens();
    //System.out.println(java.util.Arrays.toString(tokens));
    return interpret(tokens);
  }

  public Object interpret(Token[] tokens) {
    interpreter.setTokens(tokens);
    Object result = interpreter.interpret();

    //Prints the result so I can actually see what is going on. Every single one of them. Ah yes the pains of debugging.
    /*if (result == null) {
      System.out.println("nwthin");
    } else {
      System.out.println(result);
    }*/

    return result;
  }

}