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
import sh.cody.namedvars.delegate.Delegate;
import java.util.Objects;
import java.util.Optional;

public final class Variable<T> {
   private final String name;
   private final Class<T> type;
   private final Scope scope;
   private final Parser<T> parser;
   private final Delegate<T> delegate;
   private final String description;

   Variable(String name, Class<T> type, Scope scope, Parser<T> parser, Delegate<T> delegate, String description) {
      this.name = Objects.requireNonNull(name);
      this.type = Objects.requireNonNull(type);
      this.scope = Objects.requireNonNull(scope);
      this.parser = parser;
      this.delegate = Objects.requireNonNull(delegate);
      this.description = Optional.ofNullable(description).orElse("");
   }

   /**
    * Returns the variable's value.
    *
    * @return the variable's value
    */
   public T get() {
      return this.delegate.get();
   }

   /**
    * Updates the variable's value.
    *
    * @param value the new value
    */
   public void set(T value) {
      this.delegate.set(value);
   }

   /**
    * Returns the name of the variable.
    *
    * @return the name of the variable
    */
   public String getName() {
      return this.name;
   }

   /**
    * Returns the type of the value encapsulated by this variable.
    *
    * @return the type of the value encapsulated by this variable
    */
   public Class<T> getType() {
      return this.type;
   }

   /**
    * Returns the scope that contains this variable.
    *
    * @return the scope that contains this variable
    */
   public Scope getScope() {
      return this.scope;
   }

   /**
    * Returns the description of this variable.
    *
    * @return the description of this variable
    */
   public String getDescription() {
      return this.description;
   }


   /**
    * Returns the delegate of this variable.
    *
    * @return the delegate of this variable
    */
   public Delegate<T> getDelegate() {
      return this.delegate;
   }

   /**
    * Updates the variable's value from a string.
    *
    * @param str unparsed value string
    */
   public void parse(String str) {
      if(this.parser == null) {
         throw new RuntimeException("This variable does not support parsing.");
      }

      this.set(this.parser.parse(str));
   }

   /**
    * Returns a string representation of the variable's value.
    *
    * @return a string representation of the variable's value
    */
   @Override
   public String toString() {
      return String.valueOf(this.get());
   }
}
