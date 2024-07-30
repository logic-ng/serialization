// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.serialization;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.sat.GlucoseConfig;
import org.logicng.solvers.sat.MiniSatConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

    private static void compareSolverModels(final MiniSat solver1, final MiniSat solver2) {
        solver1.sat();
        solver2.sat();
        final List<Variable> model1 = solver1.model(solver1.knownVariables()).positiveVariables();
        final List<Variable> model2 = solver2.model(solver1.knownVariables()).positiveVariables();
        assertThat(model2).isEqualTo(model1);
    }
}
