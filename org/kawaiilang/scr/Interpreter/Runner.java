package org.kawaiilang;
import java.util.HashMap;

public class Runner {

  private Runner() {

  }

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

  private static String fileName = "<stdin>";
  private static Interpreter interpreter = new Interpreter(fileName);

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
      strBuilder.append(line).append("\n");
    }
    //System.out.println(strBuilder.toString());
    return strBuilder.toString();
  }

  public static void setFileName(String fileName) {
    Runner.fileName = fileName;
    interpreter.setFileName(fileName);
  }

  public static Object run(String text) {
    Lexer lexer = new Lexer(fileName, unUwUfy(text));
    Token[] tokens = lexer.makeTokens();
    //System.out.println(java.util.Arrays.toString(tokens));
    return interpret(tokens);
  }

  public static Object interpret(Token[] tokens) {
    interpreter.setTokens(tokens);
    Object result = interpreter.interpret();
    return result;
  }

}