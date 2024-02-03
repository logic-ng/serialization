// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.collections;

import com.booleworks.logicng.collections.ProtoBufCollections.PBBooleanVector;
import com.booleworks.logicng.collections.ProtoBufCollections.PBIntVector;
import com.booleworks.logicng.collections.ProtoBufCollections.PBIntVectorVector;
import com.booleworks.logicng.collections.ProtoBufCollections.PBLongVector;

/**
 * Serialization methods for LogicNG collections.
 * @version 2.5.0
 * @since 2.5.0
 */
public interface Collections {

    /**
     * Serializes a Boolean vector to a protocol buffer.
     * @param vec the Boolean vector
     * @return the protocol buffer
     */
    static PBBooleanVector serialize(final LNGBooleanVector vec) {
        final PBBooleanVector.Builder builder = PBBooleanVector.newBuilder().setSize(vec.size());
        for (int i = 0; i < vec.size(); i++) {
            builder.addElement(vec.get(i));
        }
        return builder.build();
    }

    /**
     * Deserializes a Boolean vector from a protocol buffer.
     * @param bin the protocol buffer
     * @return the Boolean vector
     */
    static LNGBooleanVector deserialize(final PBBooleanVector bin) {
        final boolean[] elements = new boolean[bin.getElementCount()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = bin.getElement(i);
        }
        return new LNGBooleanVector(elements, bin.getSize());
    }

    /**
     * Serializes an integer vector to a protocol buffer.
     * @param vec the integer vector
     * @return the protocol buffer
     */
    static PBIntVector serialize(final LNGIntVector vec) {
        final PBIntVector.Builder builder = PBIntVector.newBuilder().setSize(vec.size());
        for (int i = 0; i < vec.size(); i++) {
            builder.addElement(vec.get(i));
        }
        return builder.build();
    }

    /**
     * Serializes a vector of integer vector to a protocol buffer.
     * @param vec the vector of integer vectors
     * @return the protocol buffer
     */
    static PBIntVectorVector serialize(final LNGVector<LNGIntVector> vec) {
        final PBIntVectorVector.Builder builder = PBIntVectorVector.newBuilder().setSize(vec.size());
        for (int i = 0; i < vec.size(); i++) {
            builder.addElement(serialize(vec.get(i)));
        }
        return builder.build();
    }

    /**
     * Deserializes an integer vector from a protocol buffer.
     * @param bin the protocol buffer
     * @return the integer vector
     */
    static LNGIntVector deserialize(final PBIntVector bin) {
        final int[] elements = new int[bin.getElementCount()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = bin.getElement(i);
        }
        return new LNGIntVector(elements, bin.getSize());
    }

    /**
     * Deserializes a vector of integer vectors from a protocol buffer.
     * @param bin the protocol buffer
     * @return the vector of integer vectors
     */
    static LNGVector<LNGIntVector> deserialize(final PBIntVectorVector bin) {
        final LNGVector<LNGIntVector> vec = new LNGVector<>(bin.getSize());
        for (final PBIntVector i : bin.getElementList()) {
            vec.push(deserialize(i));
        }
        return vec;
    }

    /**
     * Serializes a long vector to a protocol buffer.
     * @param vec the long vector
     * @return the protocol buffer
     */
    static PBLongVector serialize(final LNGLongVector vec) {
        final PBLongVector.Builder builder = PBLongVector.newBuilder().setSize(vec.size());
        for (int i = 0; i < vec.size(); i++) {
            builder.addElement(vec.get(i));
        }
        return builder.build();
    }

    /**
     * Deserializes a long vector from a protocol buffer.
     * @param bin the protocol buffer
     * @return the long vector
     */
    static LNGLongVector deserialize(final PBLongVector bin) {
        final long[] elements = new long[bin.getElementCount()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = bin.getElement(i);
        }
        return new LNGLongVector(elements, bin.getSize());
    }
}
