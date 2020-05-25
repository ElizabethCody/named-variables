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

package sh.cody.namedvars.test;

import static org.junit.Assert.*;
import org.junit.Test;
import sh.cody.namedvars.Scope;
import sh.cody.namedvars.exception.VariableScopeException;

public class FullTest {
   @Test
   public void testReflectiveImport() throws VariableScopeException {
      ReflectiveImportTest test = new ReflectiveImportTest(new Scope());

      assertTrue(test.inferredNameBoolean);
      assertEquals(test.inferredNameByte, (byte) 0x10);
      assertEquals(test.inferredNameShort, (short) 0x1010);
      assertEquals(test.inferredNameChar, 'A');
      assertEquals(test.inferredNameInt, 300000);
      assertEquals(test.inferredNameLong, 40000000000L);
      assertEquals(test.inferredNameFloat, 0.0f, 0.000001f);
      assertEquals(test.inferredNameDouble, 1.0, 0.000001);

      assertTrue(test.explicitNameBoolean);
      assertEquals(test.explicitNameByte, (byte) 0x10);
      assertEquals(test.explicitNameShort, (short) 0x1010);
      assertEquals(test.explicitNameChar, 'A');
      assertEquals(test.explicitNameInt, 300000);
      assertEquals(test.explicitNameLong, 40000000000L);
      assertEquals(test.explicitNameFloat, 0.0f, 0.000001f);
      assertEquals(test.explicitNameDouble, 1.0, 0.000001);
   }

   @Test(expected = VariableScopeException.class)
   public void testDoubleAdd() throws VariableScopeException {
      Scope scope = new Scope();

      try {
         scope.newVariable("test", String.class, "Hello, World!");
      } catch(VariableScopeException exception) {
         fail("Exception occurred after one call.");
         exception.printStackTrace();
      }

      scope.newVariable("test", String.class, "Foobar");
   }

   @Test
   public void testStringSet() throws VariableScopeException {
      StringSetTest stringSetTest = new StringSetTest();
      assertTrue(stringSetTest.set.contains("i"));
      assertTrue(stringSetTest.set.contains("like"));
      assertTrue(stringSetTest.set.contains("pie"));
   }
}
