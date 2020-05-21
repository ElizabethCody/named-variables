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

@SuppressWarnings("unchecked")
public final class SimpleParserProvider extends ParserProvider {
   @Override
   public <T> Parser<T> match(Class<T> type) {
      return matcher(type,
            parserFor(boolean.class, Boolean::parseBoolean),
            parserFor(byte.class, Byte::parseByte),
            parserFor(short.class, Short::parseShort),
            parserFor(char.class, str -> str.charAt(0)),
            parserFor(int.class, Integer::parseInt),
            parserFor(long.class, Long::parseLong),
            parserFor(float.class, Float::parseFloat),
            parserFor(double.class, Double::parseDouble),
            parserFor(Boolean.class, Parser.nullChecked(Boolean::parseBoolean)),
            parserFor(Byte.class, Parser.nullChecked(Byte::parseByte)),
            parserFor(Short.class, Parser.nullChecked(Short::parseShort)),
            parserFor(Character.class, str -> (str != null && str.length() > 0) ? str.charAt(0) : null),
            parserFor(Integer.class, Parser.nullChecked(Integer::parseInt)),
            parserFor(Long.class, Parser.nullChecked(Long::parseLong)),
            parserFor(Float.class, Parser.nullChecked(Float::parseFloat)),
            parserFor(Double.class, Parser.nullChecked(Double::parseDouble)),
            parserFor(String.class, String::valueOf)
      );
   }
}
