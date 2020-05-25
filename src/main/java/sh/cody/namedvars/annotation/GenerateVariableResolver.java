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

import sh.cody.namedvars.proxy.Proxy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Objects;

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

   public String getName() {
      return "".equals(this.generateAnnotation.value()) ? this.field.getName() : this.generateAnnotation.value();
   }

   public Class<?> getType() {
      return this.generateAnnotation.type() == Auto.class ? this.field.getType() : this.generateAnnotation.type();
   }

   public Proxy<?> getProxy() {
      Class<?extends Proxy> proxyClass = generateAnnotation.proxy();

      try {
         return proxyClass.getConstructor(Object.class, Field.class).newInstance(this.instance, this.field);
      } catch(NoSuchMethodException ignored) {
      } catch(IllegalAccessException | InvocationTargetException | InstantiationException exception) {
         throw new RuntimeException("Failed to create proxy.", exception);
      }

      if(!Modifier.isFinal(this.field.getModifiers())) {
         throw new RuntimeException("Field should be final to use a reference proxy.");
      }

      try {
         return proxyClass.getConstructor(field.getType()).newInstance(this.field.get(this.instance));
      } catch(Exception exception) {
         throw new RuntimeException("Failed to create proxy.", exception);
      }
   }
}
