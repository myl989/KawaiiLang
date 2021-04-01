package org.kawaiilang.api;

import org.kawaiilang.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RandomAPI {

  // The random API is a set of tools useful for generating random numbers.

  private static java.util.Random rand = new java.util.Random();

  private RandomAPI() {
  }

  static class Random extends BuiltInFunction {
     Random(KawaiiLangRuntime runtime) {
      super(runtime, "Wandom", "wandom", TokenV1.C_NUMVT);
    }

    public Object call() {
      return rand.nextDouble();
    }
  }

  static class RandomGaussian extends BuiltInFunction {
     RandomGaussian(KawaiiLangRuntime runtime) {
      super(runtime, "Wandom", "wandomGaussian", TokenV1.C_NUMVT);
    }

    public Object call() {
      return rand.nextGaussian();
    }
  }

  static class RandomWithMinMax extends BuiltInFunction implements OverloadFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("min", "Numwer");
      parameters.put("max", "Numwer");
    }

     RandomWithMinMax(KawaiiLangRuntime runtime) {
      super(runtime, "Wandom", "wandom", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        Double min = (Double) inputs.get(0);
        Double max = (Double) inputs.get(1);
        Double delta = Math.abs(max - min);
        Double r = rand.nextDouble();
        return delta * r + min;
      } else {
        return ci;
      }
    }
  }

  static class RandomInt extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("min", "Numwer");
      parameters.put("max", "Numwer");
    }

     RandomInt(KawaiiLangRuntime runtime) {
      super(runtime, "Wandom", "wandomIntewer", parameters, TokenV1.C_NUMVT);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        long min = Math.round((Double) inputs.get(0));
        long max = Math.round((Double) inputs.get(1));
        long delta = Math.abs(max - min);
        Double r = rand.nextDouble();
        double d = Math.round(delta * r + min);
        return d;
      } else {
        return ci;
      }
    }
  }

  static class SetRandomSeed extends BuiltInFunction {
    static final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
    static {
      parameters.put("", "Numwer");
    }

     SetRandomSeed(KawaiiLangRuntime runtime) {
      super(runtime, "Wandom", "setWandomSeed", parameters, TokenV1.C_NOTHING);
    }

    public Object call(ArrayList<Object> inputs) {
      Object ci = checkInputs(inputs);
      if (ci == null) {
        Double seed = (Double) inputs.get(0);
        rand.setSeed(seed.longValue());
        return null;
      } else {
        return ci;
      }
    }
  }

  static class Randomize extends BuiltInFunction {
     Randomize(KawaiiLangRuntime runtime) {
      super(runtime, "Wandom", "genewateWandomSeed", TokenV1.C_NOTHING);
    }

    public Object call() {
      rand = new java.util.Random();
      return null;
    }
  }

}