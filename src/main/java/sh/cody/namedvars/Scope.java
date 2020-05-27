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

   /**
    * Constructs a new {@link Scope} with a {@link SimpleParserProvider}.
    */
   public Scope() {
      this(new SimpleParserProvider());
   }

   /**
    * Constructs a new {@link Scope} with the specified {@link ParserProvider}.
    *
    * @param parserProvider a parser provider
    */
   public Scope(ParserProvider parserProvider) {
      this.variableMap = new HashMap<>();
      this.parserProvider = parserProvider;
   }

   /**
    * Adds an instance of {@link Variable} to the {@link Scope#variableMap}.
    *
    * @param variable variable to be added
    * @param <T> the type of the variable's value
    * @return the variable that was added
    * @throws ScopeException the scope already contains a variable with this name
    */
   private <T> Variable<T> add(Variable<T> variable) throws ScopeException {
      if(this.variableMap.containsKey(variable.getName())) {
         throw new ScopeException("A variable with this name is already defined in this scope.");
      } else {
         this.variableMap.put(variable.getName(), variable);
      }

      return variable;
   }

   /**
    * Adds a variable to the scope.
    *
    * @param name the variable's name
    * @param type the variable's type
    * @param delegate the variable's delegate
    * @param <T> the variable's type
    * @return the variable that was added
    * @throws ScopeException the scope already contains a variable with this name
    */
   private <T> Variable<T> add(String name, Class<T> type, String description, Delegate<T> delegate) throws ScopeException {
      return this.add(new Variable<>(name, type, this, this.parserProvider.match(type), delegate, description));
   }

   /**
    * Adds a variable to the scope using a functional getter and setter.
    *
    * @param name the variable's name
    * @param type the variable's type
    * @param getter the variable's getter
    * @param setter the variable's setter
    * @param <T> the variable's type
    * @return the variable that was added
    * @throws ScopeException the scope already contains a variable with this name
    */
   public <T> Variable<T> add(String name, Class<T> type, Supplier<T> getter, Consumer<T> setter)
         throws ScopeException {
      return this.add(name, type, null, getter, setter);
   }

   /**
    * Adds a variable to the scope using a functional getter and setter.
    *
    * @param name the variable's name
    * @param type the variable's type
    * @param description the variable's description
    * @param getter the variable's getter
    * @param setter the variable's setter
    * @param <T> the variable's type
    * @return the variable that was added
    * @throws ScopeException the scope already contains a variable with this name
    */
   public <T> Variable<T> add(String name, Class<T> type, String description, Supplier<T> getter, Consumer<T> setter)
      throws ScopeException {
      return this.add(name, type, description, Delegate.fromGetterAndSetter(getter, setter));
   }

   /**
    * Creates a new variable in the scope.
    *
    * @param name the variable's name
    * @param type the variable's type
    * @param <T> the variable's type
    * @return the variable that was created
    * @throws ScopeException the scope already contains a variable with this name
    */
   public <T> Variable<T> create(String name, Class<T> type) throws ScopeException {
      return this.create(name, type, null);
   }

   /**
    * Creates a new variable in the scope.
    *
    * @param name the variable's name
    * @param type the variable's type
    * @param value the variable's value
    * @param <T> the variable's type
    * @return the variable that was created
    * @throws ScopeException the scope already contains a variable with this name
    */
   public <T> Variable<T> create(String name, Class<T> type, T value) throws ScopeException {
      return this.create(name, type, null, value);
   }

   /**
    * Creates a new variable in the scope.
    *
    * @param name the variable's name
    * @param type the variable's type
    * @param value the variable's value
    * @param <T> the variable's type
    * @return the variable that was created
    * @throws ScopeException the scope already contains a variable with this name
    */
   public <T> Variable<T> create(String name, Class<T> type, String description, T value) throws ScopeException {
      return this.add(name, type, description, new StoredValueDelegate<>(value));
   }

   /**
    * Imports a field annotated with {@link GenerateVariable} into the scope as a variable.
    *
    * @param instance the field's parent instance
    * @param field the field
    * @param <T> the variable's type
    * @return the variable that was imported
    * @throws ScopeException the scope already contains a variable with this name
    */
   public <T> Variable<T> importField(Object instance, Field field) throws ScopeException {
      GenerateVariableResolver resolver = new GenerateVariableResolver(instance, field);
      return this.add(resolver.getName(), resolver.getType(), resolver.getDescription(), resolver.getDelegate());
   }

   /**
    * Imports all fields annotated with {@link GenerateVariable} within an instance into the scope as variables.
    *
    * @param instance an instance
    * @return an array containing all variables that were imported
    * @throws ScopeException one or more of the field's variable names already exist within the scope
    */
   public Variable<?>[] importAll(Object instance) throws ScopeException {
      List<Variable<?>> variables = new ArrayList<>();
      for(Field field : instance.getClass().getDeclaredFields()) {
         try {
            variables.add(this.importField(instance, field));
         } catch(NullPointerException ignored) {}
      }
      return variables.toArray(new Variable<?>[0]);
   }

   /**
    * Retrieves a variable from the scope.
    *
    * @param name the variable's name
    * @param <T> the variable's type
    * @return the variable or {@code null} if it doesn't exist
    */
   @SuppressWarnings("unchecked")
   public <T> Variable<T> get(String name) {
      return (Variable<T>) this.variableMap.get(name);
   }

   /**
    * Returns a stream containing all variables in the scope.
    *
    * @return a stream containing all the variables in the scope
    */
   public Stream<Variable<?>> stream() {
      return this.variableMap.values().stream();
   }

   /**
    * Returns an iterator to iterate over every variable in the scope.
    *
    * @return an iterator to iterate over every variable in the scope
    */
   @Override
   public Iterator<Variable<?>> iterator() {
      return this.variableMap.values().iterator();
   }
}
