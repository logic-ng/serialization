// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import com.booleworks.logicng.formulas.ProtoBufFormulas;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Variable;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.io.parsers.readers.FormulaReader;
import org.logicng.propositions.ExtendedProposition;
import org.logicng.propositions.Proposition;
import org.logicng.propositions.PropositionBackpack;
import org.logicng.propositions.StandardProposition;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.sat.GlucoseConfig;
import org.logicng.solvers.sat.MiniSatConfig;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SatSolversTest {

    private static FormulaFactory f;
    private static SolverSerializer serializer;
    private static List<Formula> formula;
    private static Path tempFile;

    @BeforeAll
    public static void init() throws ParserException, IOException {
        f = new FormulaFactory();
        serializer = SolverSerializer.withoutPropositions(f);
        tempFile = Files.createTempFile("temp", "pb");
        final Formula whole = FormulaReader.readPseudoBooleanFormula(Paths.get("src/test/resources/large_formula.txt").toFile(), f);
        formula = whole.stream().collect(Collectors.toList());
    }

    @AfterAll
    public static void cleanUp() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testMiniSatSimple(final boolean compress) throws IOException {
        final MiniSat solverBefore = MiniSat.miniSat(f);
        solverBefore.add(formula);
        serializer.serializeSolverToFile(solverBefore, tempFile, compress);
        final MiniSat solverAfter = SolverSerializer.withoutPropositions(new FormulaFactory()).deserializeMiniSatFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testMiniCardSimple(final boolean compress) throws IOException {
        final MiniSat solverBefore = MiniSat.miniCard(f);
        solverBefore.add(formula);
        serializer.serializeSolverToFile(solverBefore, tempFile, compress);
        final MiniSat solverAfter = SolverSerializer.withoutPropositions(new FormulaFactory()).deserializeMiniSatFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testGlucoseSimple(final boolean compress) throws IOException {
        final MiniSat solverBefore = MiniSat.glucose(f);
        solverBefore.add(formula);
        serializer.serializeSolverToFile(solverBefore, tempFile, compress);
        final MiniSat solverAfter = SolverSerializer.withoutPropositions(new FormulaFactory()).deserializeGlucoseFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testMiniSatSolved(final boolean compress) throws IOException {
        final MiniSat solverBefore = MiniSat.miniSat(f);
        solverBefore.add(formula);
        solverBefore.sat();
        serializer.serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer.withoutPropositions(ff).deserializeMiniSatFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
        solverBefore.add(f.variable("v3025").negate());
        solverAfter.add(f.variable("v3025").negate());
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testMiniCardSolved(final boolean compress) throws IOException {
        final MiniSat solverBefore = MiniSat.miniCard(f);
        solverBefore.add(formula);
        solverBefore.sat();
        serializer.serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer.withoutPropositions(ff).deserializeMiniSatFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
        solverBefore.add(f.variable("v3025").negate());
        solverAfter.add(f.variable("v3025").negate());
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testGlucoseSolved(final boolean compress) throws IOException {
        final MiniSat solverBefore = MiniSat.glucose(f);
        solverBefore.add(formula);
        solverBefore.sat();
        serializer.serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer.withoutPropositions(ff).deserializeGlucoseFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
        solverBefore.add(f.variable("v3025").negate());
        solverAfter.add(f.variable("v3025").negate());
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        compareSolverModels(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testMiniSatWithProof(final boolean compress) throws IOException, ParserException {
        final MiniSat solverBefore = MiniSat.miniSat(f, MiniSatConfig.builder().proofGeneration(true).build());
        solverBefore.add(formula);
        serializer.serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer.withoutPropositions(ff).deserializeMiniSatFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        final PropositionalParser p = new PropositionalParser(f);
        final PropositionalParser pp = new PropositionalParser(ff);
        solverBefore.add(p.parse("v1668 & v1671"));
        solverAfter.add(pp.parse("v1668 & v1671"));
        assertThat(solverBefore.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverAfter.sat()).isEqualTo(Tristate.FALSE);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testGlucoseWithProof(final boolean compress) throws IOException, ParserException {
        final MiniSat solverBefore = MiniSat.glucose(f, MiniSatConfig.builder().proofGeneration(true).incremental(false).build(),
                GlucoseConfig.builder().build());
        solverBefore.add(formula);
        serializer.serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer.withoutPropositions(ff).deserializeGlucoseFromFile(tempFile, compress);
        final PropositionalParser p = new PropositionalParser(f);
        final PropositionalParser pp = new PropositionalParser(ff);
        solverBefore.add(p.parse("v1668 & v1671"));
        solverAfter.add(pp.parse("v1668 & v1671"));
        solverBefore.sat();
        solverAfter.sat();
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        solverBefore.add(p.parse("v1668 & v1671"));
        solverAfter.add(pp.parse("v1668 & v1671"));
        assertThat(solverBefore.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverAfter.sat()).isEqualTo(Tristate.FALSE);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testMiniSatWithStandardPropositions(final boolean compress) throws IOException, ParserException {
        final MiniSat solverBefore = MiniSat.miniSat(f, MiniSatConfig.builder().proofGeneration(true).build());
        for (int i = 0; i < formula.size(); i++) {
            solverBefore.add(new StandardProposition("Prop " + i, formula.get(i)));
        }
        SolverSerializer.withStandardPropositions(f).serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer.withStandardPropositions(ff).deserializeMiniSatFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        final PropositionalParser p = new PropositionalParser(f);
        final PropositionalParser pp = new PropositionalParser(ff);
        solverBefore.add(new StandardProposition("Test", p.parse("v1668 & v1671")));
        solverAfter.add(new StandardProposition("Test", pp.parse("v1668 & v1671")));
        assertThat(solverBefore.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverAfter.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverBefore.unsatCore()).isEqualTo(solverAfter.unsatCore());
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testGlucoseWithStandardPropositions(final boolean compress) throws IOException, ParserException {
        final MiniSat solverBefore = MiniSat.glucose(f, MiniSatConfig.builder().incremental(false).proofGeneration(true).build(), GlucoseConfig.builder().build());
        for (int i = 0; i < formula.size(); i++) {
            solverBefore.add(new StandardProposition("Prop " + i, formula.get(i)));
        }
        SolverSerializer.withStandardPropositions(f).serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer.withStandardPropositions(ff).deserializeGlucoseFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        final PropositionalParser p = new PropositionalParser(f);
        final PropositionalParser pp = new PropositionalParser(ff);
        solverBefore.add(new StandardProposition("Test", p.parse("v1668 & v1671")));
        solverAfter.add(new StandardProposition("Test", pp.parse("v1668 & v1671")));
        assertThat(solverBefore.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverAfter.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverBefore.unsatCore()).isEqualTo(solverAfter.unsatCore());
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testMiniSatWithCustomPropositions(final boolean compress) throws IOException, ParserException {
        final MiniSat solverBefore = MiniSat.miniSat(f, MiniSatConfig.builder().proofGeneration(true).build());
        for (int i = 0; i < formula.size(); i++) {
            solverBefore.add(new ExtendedProposition<>(new CustomBackpack(i), formula.get(i)));
        }
        SolverSerializer.withCustomPropositions(f, CustomBackpack.serializer, CustomBackpack.deserializer)
                .serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer
                .withCustomPropositions(ff, CustomBackpack.serializer, CustomBackpack.deserializer)
                .deserializeMiniSatFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        final PropositionalParser p = new PropositionalParser(f);
        final PropositionalParser pp = new PropositionalParser(ff);
        solverBefore.add(new ExtendedProposition<>(new CustomBackpack(42), p.parse("v1668 & v1671")));
        solverAfter.add(new ExtendedProposition<>(new CustomBackpack(42), pp.parse("v1668 & v1671")));
        assertThat(solverBefore.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverAfter.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverBefore.unsatCore()).isEqualTo(solverAfter.unsatCore());
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testGlucoseWithCustomPropositions(final boolean compress) throws IOException, ParserException {
        final MiniSat solverBefore = MiniSat.glucose(f, MiniSatConfig.builder().incremental(false).proofGeneration(true).build(), GlucoseConfig.builder().build());
        for (int i = 0; i < formula.size(); i++) {
            solverBefore.add(new ExtendedProposition<>(new CustomBackpack(i), formula.get(i)));
        }
        SolverSerializer.withCustomPropositions(f, CustomBackpack.serializer, CustomBackpack.deserializer)
                .serializeSolverToFile(solverBefore, tempFile, compress);
        final FormulaFactory ff = new FormulaFactory();
        final MiniSat solverAfter = SolverSerializer
                .withCustomPropositions(ff, CustomBackpack.serializer, CustomBackpack.deserializer)
                .deserializeGlucoseFromFile(tempFile, compress);
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
        final PropositionalParser p = new PropositionalParser(f);
        final PropositionalParser pp = new PropositionalParser(ff);
        solverBefore.add(new ExtendedProposition<>(new CustomBackpack(42), p.parse("v1668 & v1671")));
        solverAfter.add(new ExtendedProposition<>(new CustomBackpack(42), pp.parse("v1668 & v1671")));
        assertThat(solverBefore.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverAfter.sat()).isEqualTo(Tristate.FALSE);
        assertThat(solverBefore.unsatCore()).isEqualTo(solverAfter.unsatCore());
        SolverComperator.compareSolverStates(solverBefore, solverAfter);
    }

    private static void compareSolverModels(final MiniSat solver1, final MiniSat solver2) {
        solver1.sat();
        solver2.sat();
        final List<Variable> model1 = solver1.model(solver1.knownVariables()).positiveVariables();
        final List<Variable> model2 = solver2.model(solver1.knownVariables()).positiveVariables();
        assertThat(model2).isEqualTo(model1);
    }

    private static class CustomBackpack implements PropositionBackpack {
        private final int i;

        private CustomBackpack(final int i) {
            this.i = i;
        }

        static Function<Proposition, byte[]> serializer = proposition -> {
            int integer = ((CustomBackpack) ((ExtendedProposition) proposition).backpack()).i;
            byte[] formulaBytes = Formulas.serializeFormula(proposition.formula()).toByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(4 + formulaBytes.length);
            buffer.putInt(integer);
            buffer.put(formulaBytes);
            return buffer.array();
        };

        static Function<byte[], Proposition> deserializer = byteArray -> {
            ByteBuffer buffer = ByteBuffer.wrap(byteArray);
            int integer = buffer.getInt();
            byte[] formulaBytes = new byte[buffer.limit() - 4];
            buffer.get(formulaBytes);
            try {
                return new ExtendedProposition<>(new CustomBackpack(integer), Formulas.deserializeFormula(f, ProtoBufFormulas.PBFormulas.parseFrom(formulaBytes)));
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        };

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final CustomBackpack that = (CustomBackpack) o;
            return this.i == that.i;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.i);
        }

        @Override
        public String toString() {
            return "CustomBackpack{" +
                    "i=" + this.i +
                    '}';
        }
    }
}
