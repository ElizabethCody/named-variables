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

import sh.cody.namedvars.annotation.*;
import sh.cody.namedvars.exception.*;
import sh.cody.namedvars.parse.*;
import sh.cody.namedvars.delegate.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class Scope implements Iterable<Variable<?>> {
   private final Map<String, Variable<?>> variableMap;
   private final ParserProvider parserProvider;

   public Scope() {
      this(new SimpleParserProvider());
   }

   public Scope(ParserProvider parserProvider) {
      this.variableMap = new HashMap<>();
      this.parserProvider = parserProvider;
   }

   private <T> Variable<T> add(Variable<T> variable) throws ScopeException {
      if(this.variableMap.containsKey(variable.getName())) {
         throw new ScopeException("A variable with this name is already defined in this scope.");
      } else {
         this.variableMap.put(variable.getName(), variable);
      }

      return variable;
   }

   private <T> Variable<T> add(String name, Class<T> type, Delegate<T> delegate) throws ScopeException {
      return this.add(new Variable<>(name, type, this, this.parserProvider.match(type), delegate));
   }

   public <T> Variable<T> add(String name, Class<T> type, Supplier<T> getter, Consumer<T> setter)
      throws ScopeException {
      return this.add(name, type, Delegate.fromGetterAndSetter(getter, setter));
   }

   public <T> Variable<T> create(String name, Class<T> type) throws ScopeException {
      return this.create(name, type, null);
   }

   public <T> Variable<T> create(String name, Class<T> type, T value) throws ScopeException {
      return this.add(name, type, new StoredValueDelegate<>(value));
   }

   public <T> Variable<T> importField(Object instance, Field field) throws ScopeException {
      GenerateVariableResolver resolver = new GenerateVariableResolver(instance, field);
      return this.add(resolver.getName(), resolver.getType(), resolver.getDelegate());
   }

   public Variable<?>[] importAll(Object instance) throws ScopeException {
      List<Variable<?>> variables = new ArrayList<>();
      for(Field field : instance.getClass().getDeclaredFields()) {
         try {
            variables.add(this.importField(instance, field));
         } catch(NullPointerException ignored) {}
      }
      return variables.toArray(new Variable<?>[0]);
   }

   @SuppressWarnings("unchecked")
   public <T> Variable<T> get(String name) {
      return (Variable<T>) this.variableMap.get(name);
   }

   public Stream<Variable<?>> stream() {
      return this.variableMap.values().stream();
   }

   @Override
   public Iterator<Variable<?>> iterator() {
      return this.variableMap.values().iterator();
   }
}
