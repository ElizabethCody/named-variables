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

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class ParserProvider {
   public abstract <T> Parser<T> match(Class<T> type);

   protected static <T> Parser<T> matcher(Class<T> type, Function<Class, Parser>... functions) {
      for(Function<Class, Parser> function : functions) {
         Parser<T> parser = function.apply(type);
         if(parser != null) {
            return parser;
         }
      }

      return null;
   }

   protected static <T> Function<Class, Parser> parserFor(Class<T> parserType, Parser<T> parser) {
      return type -> Optional.of(type).filter(parserType::isAssignableFrom).map(ignored -> parser).orElse(null);
   }
}
