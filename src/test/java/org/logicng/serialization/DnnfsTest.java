package org.logicng.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.knowledgecompilation.dnnf.DnnfFactory;
import org.logicng.knowledgecompilation.dnnf.datastructures.Dnnf;
import org.logicng.util.FormulaRandomizer;
import org.logicng.util.FormulaRandomizerConfig;

public class DnnfsTest {

    @Test
    public void testRandomizedDnnfs() {
        final FormulaFactory f = new FormulaFactory();
        final FormulaRandomizer randomizer = new FormulaRandomizer(f, FormulaRandomizerConfig.builder().seed(42).build());
        for (int i = 0; i < 100; i++) {
            final Formula formula = randomizer.formula(4);
            final Dnnf dnnf = new DnnfFactory().compile(formula);
            final Dnnf deserialized = Dnnfs.deserializeDnnf(f, Dnnfs.serializeDnnf(dnnf));
            assertThat(deserialized).isEqualTo(dnnf);
        }
    }
}
