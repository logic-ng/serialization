// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import org.logicng.collections.LNGBooleanVector;
import org.logicng.collections.LNGIntVector;
import org.logicng.collections.LNGLongVector;

public class CollectionComperator {

    public static void assertBoolVecEquals(final LNGBooleanVector v1, final LNGBooleanVector v2) {
        if (v1 == null && v2 == null) {
            return;
        }
        assertThat(v1.size()).isEqualTo(v2.size());
        for (int i = 0; i < v1.size(); i++) {
            assertThat(v1.get(i)).isEqualTo(v2.get(i));
        }
    }

    public static void assertIntVecEquals(final LNGIntVector v1, final LNGIntVector v2) {
        if (v1 == null && v2 == null) {
            return;
        }
        assertThat(v1.size()).isEqualTo(v2.size());
        for (int i = 0; i < v1.size(); i++) {
            assertThat(v1.get(i)).isEqualTo(v2.get(i));
        }
    }

    public static void assertLongVecEquals(final LNGLongVector v1, final LNGLongVector v2) {
        if (v1 == null && v2 == null) {
            return;
        }
        assertThat(v1.size()).isEqualTo(v2.size());
        for (int i = 0; i < v1.size(); i++) {
            assertThat(v1.get(i)).isEqualTo(v2.get(i));
        }
    }
}
