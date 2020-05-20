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

package sh.cody.namedvars.value;

import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class ReflectedValue<T> implements Value<T> {
   private final Object instance;
   private final Field field;

   public ReflectedValue(Object instance, Field field) {
      this.instance = Objects.requireNonNull(instance);
      this.field = Objects.requireNonNull(field);
   }

   @Override
   public T get() {
      try {
         this.field.setAccessible(true);
         return (T) this.field.get(this.instance);
      } catch(IllegalAccessException exception) {
         throw new RuntimeException("Could not read from reflected field.", exception);
      }
   }

   @Override
   public void set(T value) {
      try {
         this.field.setAccessible(true);
         this.field.set(this.instance, value);
      } catch(IllegalAccessException exception) {
         throw new RuntimeException("Could not write to reflected field.", exception);
      }
   }
}
