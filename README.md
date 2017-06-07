# Strictify
Because Protbuf is not your typesystem

# Problem
## Everything is optional
In Protobuff 3 all fields are optional, for example a proto file similar to:
```proto
message A {
  X x,
  Y y,
  Z y
}
```
using a tool as [ScalaPb](https://github.com/scalapb/ScalaPB) to generate idiomatic Scala classes would generate something similar to this:
```scala
case class A(
  x: Option[X],
  y: Option[Y],
  z: Option[Z]
)
```

Leaking such class into the application would be very annoying, as it required developers to handle Options even when value is always going to be there (at least in this specific version of the protocol).

## Refined types
At Cakesolutions we are fans of [Refined types](https://github.com/fthomas/refined), we use it in our model classes. 
While you can instruct ScalaPb to generate custom types (making use of Refined types?), this pollutes the proto file with Scala snippets, which defies the purpose of using proto files if you use multiple languages.

# Solution
Refined allow you to own you model classes, which can use "Stricter" types, then get's automatically to converted from/to Proto buff (generated via ScalaPb)

Example:
Given a protofile similar to
```proto
syntax = "proto3";

package com.example.protocol;

import "google/protobuf/wrappers.proto";

message EmployeeData {
    // logically required (beware, ScalaPb sets default value to Zero)
    int32 id = 1;

    // logically required, non-empty
    // beware: ScalaPb sets default value to empty string
    // However, our Refined types in the crossponding model should catch this.
    string name = 2;

    // logically optional, Postive (using Google wrapper to allow optional)
    google.protobuf.Int32Value age = 20;
}
```

and Scala class similar to
```scala
case class EmployeeData(
  id: Int,
  name: String Refined NonEmpty,
  age: Option[Int Refined Positive]
)
object EmployeeData {
  implicit val protocolBinding = Binding.instance[EmployeeData, com.example.protocol.employee.EmployeeData]
}
```

Strcitify would be able to automatically Serialize and deserialize your class from/to crossponding Protobuff message.
```scala
import net.cakesolutions.strictify.scalapb.ops.SerializationOps._
val obj1 = EmployeeData(1, "name", Some(99))
val bytes1 = toByteArray(obj1)
val parsedObj1 = parseFrom[EmployeeData](bytes1)
println(parsedObj1)
```
