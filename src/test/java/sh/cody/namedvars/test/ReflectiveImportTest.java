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

import sh.cody.namedvars.annotation.GenerateVariable;
import sh.cody.namedvars.Scope;
import sh.cody.namedvars.exception.VariableScopeException;

public class ReflectiveImportTest {
   // Inferred names
   @GenerateVariable
   public boolean inferredNameBoolean = false;
   @GenerateVariable
   public byte inferredNameByte = (byte) 0x01;
   @GenerateVariable
   public short inferredNameShort = (short) 0x0101;
   @GenerateVariable
   public char inferredNameChar = 'a';
   @GenerateVariable
   public int inferredNameInt = 600000;
   @GenerateVariable
   public long inferredNameLong = 80000000000L;
   @GenerateVariable
   public float inferredNameFloat = 1.0f;
   @GenerateVariable
   public double inferredNameDouble = 2.0;
   
   // Explicit names
   @GenerateVariable("boolean")
   public boolean explicitNameBoolean = false;
   @GenerateVariable("byte")
   public byte explicitNameByte = (byte) 0x01;
   @GenerateVariable("short")
   public short explicitNameShort = (short) 0x0101;
   @GenerateVariable("char")
   public char explicitNameChar = 'a';
   @GenerateVariable("int")
   public int explicitNameInt = 600000;
   @GenerateVariable("long")
   public long explicitNameLong = 80000000000L;
   @GenerateVariable("float")
   public float explicitNameFloat = 1.0f;
   @GenerateVariable("double")
   public double explicitNameDouble = 2.0;
   
   ReflectiveImportTest(Scope scope) throws VariableScopeException {
      scope.importAllVariables(this);
      fiddle(scope);
   }
   
   void fiddle(Scope scope) {
      // Fiddle with variables with inferred names
      scope.get("inferredNameBoolean").set(true);
      scope.get("inferredNameByte").set((byte) 0x10);
      scope.get("inferredNameShort").set((short) 0x1010);
      scope.get("inferredNameChar").set('A');
      scope.get("inferredNameInt").set(300000);
      scope.get("inferredNameLong").set(40000000000L);
      scope.get("inferredNameFloat").set(0.0f);
      scope.get("inferredNameDouble").set(1.0);

      // Fiddle with variables with explicit names
      scope.get("boolean").set(true);
      scope.get("byte").set((byte) 0x10);
      scope.get("short").set((short) 0x1010);
      scope.get("char").set('A');
      scope.get("int").set(300000);
      scope.get("long").set(40000000000L);
      scope.get("float").set(0.0f);
      scope.get("double").set(1.0);
   }
}
