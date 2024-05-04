// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.logicng.serialization.CollectionComperator.assertBoolVecEquals;
import static org.logicng.serialization.CollectionComperator.assertIntVecEquals;
import static org.logicng.serialization.ReflectionHelper.getSuperField;
import static org.logicng.serialization.SolverDatastructureComparator.assertClausesEquals;
import static org.logicng.serialization.SolverDatastructureComparator.assertHeapEquals;
import static org.logicng.serialization.SolverDatastructureComparator.assertVariablesEquals;
import static org.logicng.serialization.SolverDatastructureComparator.assertWatchListsEquals;

import org.logicng.collections.LNGIntVector;
import org.logicng.collections.LNGVector;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.sat.MiniSatStyleSolver;
import org.logicng.solvers.sat.MiniSatStyleSolver.ProofInformation;

public class SolverComperator {

    public static void compareSolverStates(final MiniSat solver1, final MiniSat solver2) {
        final MiniSatStyleSolver s1 = solver1.underlyingSolver();
        final MiniSatStyleSolver s2 = solver2.underlyingSolver();

        assertFieldEqual(s1, s2, "ok");
        assertFieldEqual(s1, s2, "qhead");
        assertClausesEquals(getSuperField(s1, "clauses"), getSuperField(s2, "clauses"));
        assertClausesEquals(getSuperField(s1, "learnts"), getSuperField(s2, "learnts"));
        assertWatchListsEquals(getSuperField(s1, "watches"), getSuperField(s2, "watches"));
        assertVariablesEquals(getSuperField(s1, "vars"), getSuperField(s2, "vars"));
        assertHeapEquals(getSuperField(s1, "orderHeap"), getSuperField(s2, "orderHeap"));
        assertIntVecEquals(getSuperField(s1, "trail"), getSuperField(s2, "trail"));
        assertIntVecEquals(getSuperField(s1, "trailLim"), getSuperField(s2, "trailLim"));
        assertBoolVecEquals(getSuperField(s1, "model"), getSuperField(s2, "model"));
        assertIntVecEquals(getSuperField(s1, "conflict"), getSuperField(s2, "conflict"));
        assertIntVecEquals(getSuperField(s1, "assumptions"), getSuperField(s2, "assumptions"));
        assertBoolVecEquals(getSuperField(s1, "seen"), getSuperField(s2, "seen"));
        assertFieldEqual(s1, s2, "analyzeBtLevel");
        assertFieldEqual(s1, s2, "claInc");
        assertFieldEqual(s1, s2, "simpDBAssigns");
        assertFieldEqual(s1, s2, "simpDBProps");
        assertFieldEqual(s1, s2, "clausesLiterals");
        assertFieldEqual(s1, s2, "learntsLiterals");

        assertFieldEqual(s1, s2, "varDecay");
        assertFieldEqual(s1, s2, "varInc");
        assertFieldEqual(s1, s2, "ccminMode");
        assertFieldEqual(s1, s2, "restartFirst");
        assertFieldEqual(s1, s2, "restartInc");
        assertFieldEqual(s1, s2, "clauseDecay");
        assertFieldEqual(s1, s2, "shouldRemoveSatsisfied");
        assertFieldEqual(s1, s2, "learntsizeFactor");
        assertFieldEqual(s1, s2, "learntsizeInc");
        assertFieldEqual(s1, s2, "incremental");

        assertFieldEqual(s1, s2, "name2idx");
        assertFieldEqual(s1, s2, "idx2name");

        assertProofEqual(s1, s2);

        assertFieldEqual(s1, s2, "backboneCandidates");
        assertIntVecEquals(getSuperField(s1, "backboneAssumptions"), getSuperField(s2, "backboneAssumptions"));
        assertFieldEqual(s1, s2, "backboneMap");
        assertFieldEqual(s1, s2, "computingBackbone");

        assertIntVecEquals(getSuperField(s1, "selectionOrder"), getSuperField(s2, "selectionOrder"));
        assertFieldEqual(s1, s2, "selectionOrderIdx");

        assertFieldEqual(s1, s2, "learntsizeAdjustConfl");
        assertFieldEqual(s1, s2, "learntsizeAdjustCnt");
        assertFieldEqual(s1, s2, "learntsizeAdjustStartConfl");
        assertFieldEqual(s1, s2, "learntsizeAdjustInc");
        assertFieldEqual(s1, s2, "maxLearnts");
    }

    private static void assertFieldEqual(final MiniSatStyleSolver s1, final MiniSatStyleSolver s2, final String field) {
        final Object f1 = getSuperField(s1, field);
        final Object f2 = getSuperField(s2, field);
        assertThat(f1).isEqualTo(f2);
    }

    private static void assertProofEqual(final MiniSatStyleSolver s1, final MiniSatStyleSolver s2) {
        final LNGVector<ProofInformation> pg1 = getSuperField(s1, "pgOriginalClauses");
        final LNGVector<ProofInformation> pg2 = getSuperField(s2, "pgOriginalClauses");
        if (pg1 == null) {
            assertThat(pg2).isNull();
        }
        if (pg2 == null) {
            assertThat(pg1).isNull();
        }
        if (pg1 == null) {
            return;
        }
        assertThat(pg1.size()).isEqualTo(pg2.size());
        for (int i = 0; i < pg1.size(); i++) {
            final ProofInformation pi1 = pg1.get(i);
            final ProofInformation pi2 = pg2.get(i);
            assertIntVecEquals(pi1.clause(), pi2.clause());
            assertThat(pi1.proposition()).isEqualTo(pi2.proposition());
        }

        final LNGVector<LNGIntVector> proof1 = getSuperField(s1, "pgProof");
        final LNGVector<LNGIntVector> proof2 = getSuperField(s2, "pgProof");
        if (proof1 == null) {
            assertThat(proof2).isNull();
        }
        if (proof2 == null) {
            assertThat(proof1).isNull();
        }
        if (proof1 == null) {
            return;
        }
        assertThat(proof1.size()).isEqualTo(proof2.size());
        for (int i = 0; i < proof1.size(); i++) {
            assertIntVecEquals(proof1.get(i), proof2.get(i));
        }
    }
}
