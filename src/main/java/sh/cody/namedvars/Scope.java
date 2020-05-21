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

import sh.cody.namedvars.exception.*;
import sh.cody.namedvars.parse.*;
import sh.cody.namedvars.value.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@SuppressWarnings("unchecked")
public final class Scope implements Iterable<Variable<?>> {
   private final Map<String, Variable<?>> variableMap;
   private final ParserProvider parserProvider;

   public Scope() {
      this(new SimpleParserProvider(false));
   }

   public Scope(ParserProvider parserProvider) {
      this.variableMap = new HashMap<>();
      this.parserProvider = parserProvider;
   }

   private <T> Variable<T> addVariable(Variable<T> variable) throws VariableScopeException {
      if(this.variableMap.containsKey(variable.getName())) {
         throw new VariableScopeException("A Variable with this name is already defined in this scope.");
      } else {
         this.variableMap.put(variable.getName(), variable);
      }

      return variable;
   }

   private <T> Variable<T> addProxyVariable(String name, Class<T> type, Value<T> value) throws VariableScopeException {
      return this.addVariable(new ValueVariable<>(name, type, this, this.parserProvider.match(type), value));
   }

   public <T> Variable<T> addProxyVariable(String name, Class<T> type, Supplier<T> getter, Consumer<T> setter)
      throws VariableScopeException {
      return this.addProxyVariable(name, type, new FunctionalValue<>(getter, setter));
   }

   public <T> Variable<T> newVariable(String name, Class<T> type) throws VariableScopeException {
      return this.newVariable(name, type, null);
   }

   public <T> Variable<T> newVariable(String name, Class<T> type, T value) throws VariableScopeException {
      Variable<T> variable = new ValueVariable<>(name, type, this, this.parserProvider.match(type),
            new StoredValue<>(value));

      return this.addVariable(variable);
   }

   public <T> Variable<T> importVariableFromField(Object instance, Field field)
         throws GenerateVariableException, VariableScopeException {
      GenerateVariable annotation = field.getAnnotation(GenerateVariable.class);

      if(annotation == null) {
         throw new GenerateVariableException("This field is not properly annotated.");
      } else {
         String name = "".equals(annotation.value()) ? field.getName() : annotation.value();
         Class<T> type = (Class<T>) field.getType();
         ReflectedValue<T> value = new ReflectedValue<>(instance, field);
         return this.addVariable(new ValueVariable<>(name, type, this, this.parserProvider.match(type), value));
      }
   }

   public Variable<?>[] importAllVariables(Object instance) throws VariableScopeException {
      List<Variable<?>> variables = new ArrayList<>();

      for(Field field : instance.getClass().getDeclaredFields()) {
         try {
            Variable<?> variable = this.importVariableFromField(instance, field);
            variables.add(variable);
         } catch(GenerateVariableException ignored) { }
      }

      return variables.toArray(new Variable<?>[0]);
   }

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
