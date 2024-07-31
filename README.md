![License](https://img.shields.io/badge/license-Apache/MIT%202-ff69b4.svg) [![Maven Central](https://img.shields.io/maven-central/v/org.logicng/logicng-serialization.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.logicng%22%20AND%20a:%22logicng-serialization%22)

<a href="https://www.logicng.org"><img src="https://github.com/logic-ng/LogicNG/blob/master/doc/logo/logo_big.png" alt="logo" width="300"></a>

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
