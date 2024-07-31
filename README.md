# LogicNG Serialization Library

A small library which allows for the serialization of
[LogicNG](https://github.com/logic-ng/LogicNG) datastructures like formulas
or whole SAT solvers as Google Protocol Buffers.

## Usage

### Formula Serializiation

To serialize and deserialize a formula as Protocol Buffer use the following
code:

```java
final FormulaFactory f = new FormulaFactory();
Formula formula = ...
PBFormulas serialized = Formulas.serializeFormula(formula);
Formula deserialized = Formulas.deserializeFormula(f, serialized);
```

Alternatively you can serialize formulas directly from and to streams or files:

```java
final FormulaFactory f = new FormulaFactory();
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
Formulas.serializeFormulaToStream(formula, outputStream);
byte[] byteArray = outputStream.toByteArray();
outputStream.close();
Formula deserialized = Formulas.deserializeFormulaFromStream(f, new ByteArrayInputStream(byteArray));
```

### Solver Serialization

You can also serialize a whole constructed SAT solver to Protocol Buffer, 
stream, or file.  The following code constructs a solver, serializes it to 
a zipped file and deserializes it again.  The solver has then the exact same
state as when serialized.

```java
MiniSat solver = MiniSat.miniSat(f);
solver.add(formulas); 

SolverSerializer serializer = SolverSerializer.withoutPropositions(f); 
serializer.serializeSolverToFile(solver, tempFile, true);

MiniSat deserialized = SolverSerializer.withoutPropositions(new FormulaFactory())
        .deserializeMiniSatFromFile(tempFile, true);
```
