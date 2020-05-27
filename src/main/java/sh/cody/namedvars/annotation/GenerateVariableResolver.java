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

package sh.cody.namedvars.annotation;

import sh.cody.namedvars.delegate.Delegate;
import java.lang.reflect.*;
import java.util.Objects;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class GenerateVariableResolver {
   private final Object instance;
   private final Field field;
   private final GenerateVariable generateAnnotation;

   public GenerateVariableResolver(Object instance, Field field) {
      this.instance = Objects.requireNonNull(instance);
      this.field = Objects.requireNonNull(field);
      this.generateAnnotation = Objects.requireNonNull(field.getAnnotation(GenerateVariable.class),
            "Field must be decorated with @GenerateVariable.");
   }

   public String getDescription() {
      return this.generateAnnotation.description();
   }

   public String getName() {
      return "".equals(this.generateAnnotation.value()) ? this.field.getName() : this.generateAnnotation.value();
   }

   public <T> Class<T> getType() {
      return this.generateAnnotation.type() == Auto.class ? (Class<T>) this.field.getType() :
              (Class<T>) this.generateAnnotation.type();
   }

   public <T> Delegate<T> getDelegate() {
      Class<?extends Delegate> delegateClass = generateAnnotation.delegate();

      try {
         return delegateClass.getConstructor(Object.class, Field.class).newInstance(this.instance, this.field);
      } catch(IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
         throw new RuntimeException("Failed to create delegate.", e);
      }
   }
}
