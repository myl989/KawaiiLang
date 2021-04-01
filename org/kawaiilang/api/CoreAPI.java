package org.kawaiilang.api;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedHashMap;
import org.kawaiilang.*;

public class CoreAPI {

  // The core API is a set of essential functions built in every KawaiiLang file.

  private CoreAPI() {
  }

  public static void addAPI(KawaiiLangRuntime i) {
    i.addFunction(new SenpaiStawp(i));
    i.overloadFunction(new SenpaiStawpEndCode(i));
    i.addFunction(new WhatsThis(i));
    i.overloadFunction(new WhatsThisWithPrompt(i));
    i.addFunction(new Nuzzle(i));
    i.overloadFunction(new NuzzleEnd(i));
    i.overloadFunction(new NuzzleNothing(i));
    i.overloadFunction(new NuzzleEndNothing(i));
    i.addFunction(new GiveString(i));
    i.addFunction(new Pop(i));
    i.addFunction(new Append(i));
    i.addFunction(new Upper(i));
    i.addFunction(new Lower(i));
  }

  static class SenpaiStawp extends BuiltInFunction {
    private SenpaiStawp(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "senpaiStawp", TokenV1.C_NOTHING);
    }

    public Object call() {
      System.exit(0);
      return null;
    }
  }

  static class SenpaiStawpEndCode extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

    private SenpaiStawpEndCode(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "senpaiStawp", parameters, TokenV1.C_NOTHING);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        int i = ((Double) inputs.get(0)).intValue();
        System.exit(i);
        return null;
      } else {
        return ci;
      }
    }
  }

  static class WhatsThis extends BuiltInFunction {
    private WhatsThis(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "whatsThis", TokenV1.C_STRVT);
    }

    public Object call() {
      Scanner sc = new Scanner(System.in);
      return sc.nextLine();
    }
  }

  static class WhatsThisWithPrompt extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Stwing");
    }

    private WhatsThisWithPrompt(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "whatsThis", parameters, TokenV1.C_STRVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        System.out.print(inputs.get(0));
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
      } else {
        return ci;
      }
    }
  }

  static class Nuzzle extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Anwy");
    }

    private Nuzzle(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "nuzzle", parameters, TokenV1.C_NOTHING);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        System.out.println(inputs.get(0));
        return null;
      } else {
        return ci;
      }
    }
  }

  static class NuzzleEnd extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Stwing");
      parameters.put("a", "Stwing");
    }

    private NuzzleEnd(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "nuzzle", parameters, TokenV1.C_NOTHING);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        System.out.print(inputs.get(0));
        System.out.print(inputs.get(1));
        return null;
      } else {
        return ci;
      }
    }
  }

  static class NuzzleEndNothing extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Stwing");
      parameters.put("a", "nwthin");
    }

    private NuzzleEndNothing(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "nuzzle", parameters, TokenV1.C_NOTHING);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        System.out.print(inputs.get(0));
        return null;
      } else {
        return ci;
      }
    }
  }

  static class NuzzleNothing extends BuiltInFunction {
    private NuzzleNothing(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "nuzzle", TokenV1.C_NOTHING);
    }

    public Object call() {
      System.out.println();
      return null;
    }
  }

  static class GiveString extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Anwy");
    }

    private GiveString(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "gibStwing", parameters, TokenV1.C_STRVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        if (inputs.get(0) instanceof Double) {
          Double d = (Double) inputs.get(0);
          if (d % 1 == 0) {
            return Integer.toString(d.intValue());
          }
        }
        return inputs.get(0).toString();
      } else {
        return ci;
      }
    }
  }

  static class Pop extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Lwist");
      parameters.put("a", "Numwer");
    }

    private Pop(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "pwp", parameters, TokenV1.C_ANYVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        OwOList<?> l = (OwOList<?>) inputs.get(0);
        int i = ((Double) inputs.get(1)).intValue();
        return l.pop(i);
      } else {
        return ci;
      }
    }
  }

  static class Append extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Lwist");
      parameters.put("a", "Anwy");
    }

    private Append(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "appwnd", parameters, TokenV1.C_LISTVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        OwOList l = (OwOList) inputs.get(0); // Type check
        if (inputs.get(1) instanceof OwOList) {
          OwOList l2 = (OwOList) inputs.get(1);
          return l.add(l2.asArrayList()); // Type check
        } else {
          return l.add(inputs.get(1));
        }
      } else {
        return ci;
      }
    }
  }

  static class Upper extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Stwing");
    }

    private Upper(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "uwpper", parameters, TokenV1.C_STRVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        String str = (String) inputs.get(0);
        return str.toUpperCase();
      } else {
        return ci;
      }
    }
  }

  static class Lower extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Stwing");
    }

    private Lower(KawaiiLangRuntime runtime) {
      super(runtime, "CoreAPI", "lwer", parameters, TokenV1.C_STRVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        String str = (String) inputs.get(0);
        return str.toLowerCase();
      } else {
        return ci;
      }
    }
  }

}