package org.kawaiilang;
import java.util.Scanner;

public class Shell {
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    Runner r = new Runner();
    while (true) {
      System.out.print("KawaiiLang> ");
      String input = s.nextLine();
      if (input.equals("Senpai.stawpp UwU UwU!")) {
        s.close();
        break;
      } else {
        //long startTime = System.nanoTime();
        Object result = r.eval(input);
        /*long endTime = System.nanoTime();
        System.out.print("Time taken: ");
        System.out.println(endTime - startTime);*/

        //The following code prints out the final results only! (unlike the testing print code in Runner class which prints every evaluation)
        if (result == null) {
          System.out.println("nwthin");
        } else {
          System.out.println(result);
        }
      } //endCheckIfToEndShell
    } //endInfiniteLoop
  }
}