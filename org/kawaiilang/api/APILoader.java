package org.kawaiilang.api;

import org.kawaiilang.KawaiiLangRuntime;
import java.lang.reflect.*;

public class APILoader {

  public static void addAPI(KawaiiLangRuntime i, Class api) {
    try {

      var clzs = api.getDeclaredClasses();
      for (Class<?> funclass : clzs) {
        //System.out.print("loading ");
        //System.out.print(funclass.getName().substring(api.getName().length()+1));
        Object instance = funclass.getDeclaredConstructors()[0].newInstance(i);
        if (instance instanceof OverloadFunction) {
          //System.out.println(" (overload)");
          i.overloadFunction((BuiltInFunction) instance);
        } else {
          //System.out.println();
          i.addFunction((BuiltInFunction) instance);
        }
      }

    } catch (SecurityException sx) {
      System.out.print("APIEwwor: Pewmwission nawt granted to access API uwu~\ntarget: ");
      System.out.println(api.getName());
    } catch (InstantiationException ix) {
      ix.printStackTrace();
      System.out.print("APIEwwor: API cowld nawt be cweated uwu~\ntarget: ");
      System.out.println(api.getName());
    } catch (IllegalAccessException ix) {
      System.out.print("APIEwwor: API cowd nawr be accwessed uwu~\ntarget: ");
      System.out.println(api.getName());
    } catch (IllegalArgumentException ix) {
      System.out.print("APIEwwor: owo the API hwas a pwoblem uwu~\ntarget: ");
      System.out.println(api.getName());
    } catch (InvocationTargetException ix) {
      System.out.print("APIEwwor: owo API rwan intwo a pwoblem when lwoading uwu~\ntarget: ");
      System.out.println(api.getName());
    }
  }

}