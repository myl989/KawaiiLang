package org.kawaiilang;
import java.util.Scanner;

public final class Shell {
  
  @SuppressWarnings("resource")
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    Runner r = new Runner();
    while (true) {
      System.out.print("KawaiiLang> ");
      String input = s.nextLine();
      Object result = r.eval(input);
          
      //The following code prints out any errors. Please disable if the "print return values" option is turned on.
      /*if (result instanceof Error) {
        	System.out.println(result);
      }*/
      
      //The following code prints out the return values of each statement only! (unlike the testing print code in Runner class which prints every evaluation)
        if (result == null) {
          System.out.println("nwthin");
        } else {
          System.out.println(result);
        }
    } //endInfiniteLoop
  }
}