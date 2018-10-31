package datadog.trace.agent.test.context;

import datadog.trace.bootstrap.InstrumentationContext;

/** A class which correctly uses the context api. */
public class ClassToRemap {

  /**
   * ClassToRemap has context class of type State.
   *
   * @return The value of State#anInt
   */
  public static int mapObject(final ClassToRemap classToRemap) {
    final State state = InstrumentationContext.get(classToRemap, ClassToRemap.class, State.class);
    return ++state.anInt;
  }

  /**
   * Runnable has context class of type State.
   *
   * @return The value of State#anInt
   */
  public static int mapOtherObject(final Runnable runnable) {
    State state = InstrumentationContext.get(runnable, Runnable.class, State.class);
    return ++state.anInt;
  }

  public static class State {
    public int anInt = 0;
    public Object anObject = new Object();
  }
}