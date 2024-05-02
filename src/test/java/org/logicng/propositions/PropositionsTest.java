// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.propositions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.logicng.propositions.Propositions.deserialize;
import static org.logicng.propositions.Propositions.serialize;

import org.junit.jupiter.api.Test;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PseudoBooleanParser;

public class PropositionsTest {

    final FormulaFactory f = new FormulaFactory();

    @Test
    public void testStandardProposition() throws ParserException {
        final PseudoBooleanParser parser = new PseudoBooleanParser(this.f);
        final StandardProposition p = new StandardProposition("description", parser.parse("a & (b => c + d = 1) <=> ~x"));
        assertThat(deserialize(this.f, serialize(p))).isEqualTo(p);
    }
}
