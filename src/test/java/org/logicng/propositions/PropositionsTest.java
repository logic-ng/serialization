// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.propositions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.logicng.propositions.Propositions.deserialize;
import static org.logicng.propositions.Propositions.serialize;

import org.junit.jupiter.api.Test;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;

public class PropositionsTest {

    final FormulaFactory f = new FormulaFactory();

    @Test
    public void testStandardProposition() throws ParserException {
        final StandardProposition p = new StandardProposition("description", this.f.parse("a & (b => c + d = 1) <=> ~x"));
        assertThat(deserialize(this.f, serialize(p))).isEqualTo(p);
    }
}
