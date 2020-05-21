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

package sh.cody.namedvars;

import sh.cody.namedvars.parse.Parser;
import sh.cody.namedvars.value.Value;
import java.util.Objects;

public final class ValueVariable<T> implements Variable<T> {
   private final String name;
   private final Class<T> type;
   private final Scope scope;
   private final Parser<T> parser;
   private final Value<T> value;

   ValueVariable(String name, Class<T> type, Scope scope, Parser<T> parser, Value<T> value) {
      this.name = Objects.requireNonNull(name);
      this.type = Objects.requireNonNull(type);
      this.scope = Objects.requireNonNull(scope);
      this.parser = parser;
      this.value = Objects.requireNonNull(value);
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public Class<T> getType() {
      return this.type;
   }

   @Override
   public Scope getScope() {
      return this.scope;
   }

   @Override
   public T get() {
      return this.value.get();
   }

   @Override
   public void set(T value) {
      this.value.set(value);
   }

   @Override
   public void parse(String str) {
      if(this.parser == null) {
         throw new RuntimeException("This variable does not support parsing.");
      }

      this.set(this.parser.parse(str));
   }

   @Override
   public String toString() {
      return String.valueOf(this.get());
   }
}
