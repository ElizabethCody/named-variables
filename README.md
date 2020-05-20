# named-variables

This library facilitates mutable access, with built-in parsing, to primitive types (e.g. `int`, `double`, `boolean`,
etc.), boxed types (e.g. `Integer`, `Double`, `Boolean`, etc.), and `String` by allowing names to be assigned to them at runtime. One may find this particularly useful when adding support for
custom values to an existing configuration system (e.g. when modding a video game).

## Example

```java
import sh.cody.namedvars.*;

public class Example {
   private Scope scope;
   
   // Create variable entirely at runtime, see constructor.
   private Variable<T> colorCode1;

   // Create variable with reflection, implicitly named.
   @GenerateVariable
   private int colorCode2 = 0xff00dd;

   // Create variable with reflection, explicitly named.
   @GenerateVariable("colorCode3")
   private int anotherColorCode = 0xffff00;

   @GenerateVariable
   private boolean debugMode = false;

   public Example() throws VariableScopeException {
      scope = new Scope();
      colorCode1 = scope.newVariable("colorCode1", int.class, 0x00ff00); // Create a variable at runtime whose storage
                                                                         // is managed by named-vars.
      scope.importAllVariables(this); // Adds all variables decorated with @GenerateVariable to the scope.
   }

   void namedVarsExample() {
      // Uniformly access variables.
      System.out.println(scope.get("colorCode1").get()); // Retrieve native value.
      System.out.println(scope.get("colorCode2"));       // Use smart toString() method.
      scope.get("colorCode3").set(0xffffff);             // Update value.
      scope.get("debugMode").parse("true");              // Update value by parsing from String.      
   }
}
```

## Build
<!-- TODO: Expand on this -->

```
$ ./gradlew build
```

## Copyright

Copyright &copy; 2020 - Maxwell Cody
[&lt;maxwell&commat;cody&period;sh&gt;](mailto&colon;maxwell&commat;cody&period;sh)

This project is licensed under The MIT License, see `LICENSE.txt` for more details.