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

package sh.cody.namedvars.parse;

import java.util.*;

public interface Parser<T> {
   T parse(String str);

   static <T> Parser<T> nullChecked(Parser<T> parser) {
      return str -> {
         if(str == null || "".equals(str) || "null".equalsIgnoreCase(str)) {
            return null;
         } else {
            return parser.parse(str);
         }
      };
   }

   /**
    * Generates a parser from an enum type. The resultant parser will match strings to the given enum's values based on
    * their toString() value. Java's default toString() method returns the value's name as its defined in code, but this
    * can be overridden.
    *
    * @param enumClass Class object for the enum
    * @param <T> enum type
    * @param caseSensitive whether the resultant parser will be case sensitive
    * @return a parser matching the enum's values
    */
   static <T> Parser<T> fromEnum(Class<T> enumClass, boolean caseSensitive) {
      if(enumClass.isEnum()) {
         Map<String, T> values = new HashMap<>();
         for(T value : enumClass.getEnumConstants()) {
            values.put(caseSensitive ? value.toString() : value.toString().toLowerCase(), value);
         }

         return str -> values.get(caseSensitive ? str : str.toLowerCase());
      } else {
         throw new RuntimeException("The specified type is not an enum.");
      }
   }
}
