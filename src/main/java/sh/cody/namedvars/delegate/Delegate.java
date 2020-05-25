/*
 * Copyright (c) 2020 - Maxwell Cody <maxwell@cody.sh>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package sh.cody.namedvars.delegate;

import java.util.function.*;

/**
 * This interface is implemented by classes that are intended to facilitate access to a variable's actual storage
 * location.
 *
 * <b>Note:</b> only implementations with a constructor matching
 * {@code
 *     Delegate(Object instance, Field field)
 * }
 * can be instantiated by {@link sh.cody.namedvars.annotation.GenerateVariableResolver}; therefore, for a
 * {@link Delegate} to be specified inside a {@link sh.cody.namedvars.annotation.GenerateVariable} annotation, it must
 * implement this constructor.
 *
 * @param <T> the type of the stored value as its known by the Scope
 */
public interface Delegate<T> {
   T get();
   void set(T value);

   /**
    * Generates an implementation of {@link Delegate} from a functional getter and setter.
    *
    * @param getter Value getter as a {@link Supplier}
    * @param setter Value setter as a {@link Consumer}
    * @param <T> the type of the stored value as its known by the Scope
    * @return an implementation of Delegate
    */
   static <T> Delegate<T> fromGetterAndSetter(Supplier<T> getter, Consumer<T> setter) {
      return new Delegate<>() {
         @Override
         public T get() {
            return getter.get();
         }

         @Override
         public void set(T value) {
            setter.accept(value);
         }
      };
   }
}
