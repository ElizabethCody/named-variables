package sh.cody.namedvars.value;

import java.util.function.*;

public final class FunctionalValue<T> implements Value<T> {
   private final Supplier<T> getter;
   private final Consumer<T> setter;

   public FunctionalValue(Supplier<T> getter, Consumer<T> setter) {
      this.getter = getter;
      this.setter = setter;
   }

   @Override
   public T get() {
      return this.getter.get();
   }

   @Override
   public void set(T value) {
      this.setter.accept(value);
   }
}
