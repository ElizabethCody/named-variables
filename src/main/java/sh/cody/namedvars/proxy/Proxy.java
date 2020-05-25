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

package sh.cody.namedvars.proxy;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Proxy<T> {
   /*
    * For a proxy to be used in an @GenerateVariable annotation it must implement one of two constructors depending on
    * where the true value is stored.
    *
    * Case 1, in a field:
    *    Proxy(Object instance, Field field)
    *
    * Case 2, in a mutable reference type:
    *    Proxy(YourType x)
    *
    */

   T get();
   void set(T value);

   static <T> Proxy<T> fromGetterAndSetter(Supplier<T> getter, Consumer<T> setter) {
      return new Proxy<T>() {
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
