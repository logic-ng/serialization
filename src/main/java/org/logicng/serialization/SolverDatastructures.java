// SPDX-License-Identifier: Apache-2.0 and MIT
// Copyright 2023-20xx BooleWorks GmbH

package org.logicng.serialization;

import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures.PBLngBoundedIntQueue;
import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures.PBLngBoundedLongQueue;
import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures.PBLngHeap;
import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures.PBMsClause;
import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures.PBMsVariable;
import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures.PBMsWatcher;
import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures.PBTristate;
import org.logicng.collections.LNGIntVector;
import org.logicng.datastructures.Tristate;
import org.logicng.solvers.datastructures.LNGBoundedIntQueue;
import org.logicng.solvers.datastructures.LNGBoundedLongQueue;
import org.logicng.solvers.datastructures.LNGHeap;
import org.logicng.solvers.datastructures.MSClause;
import org.logicng.solvers.datastructures.MSVariable;
import org.logicng.solvers.datastructures.MSWatcher;
import org.logicng.solvers.sat.MiniSatStyleSolver;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Serialization methods for SAT solver datastructures.
 * @version 2.5.0
 * @since 2.5.0
 */
public interface SolverDatastructures {

    /**
     * Serializes a tristate to a protocol buffer.
     * @param tristate the tristate value
     * @return the protocol buffer
     */
    static PBTristate serializeTristate(final Tristate tristate) {
        switch (tristate) {
            case FALSE:
                return PBTristate.FALSE;
            case TRUE:
                return PBTristate.TRUE;
            case UNDEF:
                return PBTristate.UNDEF;
            default:
                throw new IllegalArgumentException("Unknown tristate: " + tristate);
        }
    }

    /**
     * Deserializes a tristate from a protocol buffer.
     * @param bin the protocol buffer
     * @return the tristate
     */
    static Tristate deserializeTristate(final PBTristate bin) {
        switch (bin) {
            case FALSE:
                return Tristate.FALSE;
            case TRUE:
                return Tristate.TRUE;
            case UNDEF:
                return Tristate.UNDEF;
            default:
                throw new IllegalArgumentException("Unknown tristate: " + bin);
        }
    }

    /**
     * Serializes a solver heap to a protocol buffer.
     * @param heap the heap
     * @return the protocol buffer
     */
    static PBLngHeap serializeHeap(final LNGHeap heap) {
        return PBLngHeap.newBuilder()
                .setHeap(Collections.serializeIntVec(heap.getHeap()))
                .setIndices(Collections.serializeIntVec(heap.getIndices()))
                .build();
    }

    /**
     * Deserializes a solver heap from a protocol buffer.
     * @param bin the protocol buffer
     * @return the heap
     */
    static LNGHeap deserializeHeap(final PBLngHeap bin, final MiniSatStyleSolver solver) {
        final LNGIntVector heap = Collections.deserializeIntVec(bin.getHeap());
        final LNGIntVector indices = Collections.deserializeIntVec(bin.getIndices());
        return new LNGHeap(solver, heap, indices);
    }

    /**
     * Serializes a MiniSat clause to a protocol buffer.
     * @param clause the clause
     * @param id     the clause ID
     * @return the protocol buffer
     */
    static PBMsClause serializeClause(final MSClause clause, final int id) {
        return PBMsClause.newBuilder()
                .setData(Collections.serializeIntVec(clause.getData()))
                .setLearnt(clause.learnt())
                .setIsAtMost(clause.isAtMost())
                .setActivity(clause.activity())
                .setSzWithoutSelectors(clause.sizeWithoutSelectors())
                .setSeen(clause.seen())
                .setLbd(clause.lbd())
                .setCanBeDel(clause.canBeDel())
                .setOneWatched(clause.oneWatched())
                .setAtMostWatchers(clause.isAtMost() ? clause.atMostWatchers() : -1)
                .setId(id)
                .build();
    }

    /**
     * Deserializes a MiniSat clause from a protocol buffer.
     * @param bin the protocol buffer
     * @return the clause
     */
    static MSClause deserializeClause(final PBMsClause bin) {
        return new MSClause(
                Collections.deserializeIntVec(bin.getData()),
                bin.getLearnt(),
                bin.getIsAtMost(),
                bin.getActivity(),
                bin.getSzWithoutSelectors(),
                bin.getSeen(),
                bin.getLbd(),
                bin.getCanBeDel(),
                bin.getOneWatched(),
                bin.getAtMostWatchers()
        );
    }

    /**
     * Serializes a MiniSat variable to a protocol buffer.
     * @param variable  the variable
     * @param clauseMap a mapping from clause to clause ID
     * @return the protocol buffer
     */
    static PBMsVariable serializeVariable(final MSVariable variable, final IdentityHashMap<MSClause, Integer> clauseMap) {
        return PBMsVariable.newBuilder()
                .setAssignment(serializeTristate(variable.assignment()))
                .setLevel(variable.level())
                .setActivity(variable.activity())
                .setPolarity(variable.polarity())
                .setDecision(variable.decision())
                .setReason(variable.reason() == null ? -1 : clauseMap.get(variable.reason())).build();
    }

    /**
     * Deserializes a MiniSat variable from a protocol buffer.
     * @param bin       the protocol buffer
     * @param clauseMap a mapping from clause ID to clause
     * @return the variable
     */
    static MSVariable deserializeVariable(final PBMsVariable bin, final Map<Integer, MSClause> clauseMap) {
        final MSClause reason = bin.getReason() == -1 ? null : clauseMap.get(bin.getReason());
        return new MSVariable(deserializeTristate(bin.getAssignment()), bin.getLevel(), reason, bin.getActivity(), bin.getPolarity(), bin.getDecision());
    }

    /**
     * Serializes a MiniSat watcher to a protocol buffer.
     * @param watcher   the watcher
     * @param clauseMap a mapping from clause to clause ID
     * @return the protocol buffer
     */
    static PBMsWatcher serializeWatcher(final MSWatcher watcher, final IdentityHashMap<MSClause, Integer> clauseMap) {
        return PBMsWatcher.newBuilder()
                .setClause(clauseMap.get(watcher.clause()))
                .setBlocker(watcher.blocker())
                .build();
    }

    /**
     * Deserializes a MiniSat watcher from a protocol buffer.
     * @param bin       the protocol buffer
     * @param clauseMap a mapping from clause ID to clause
     * @return the watcher
     */
    static MSWatcher deserializeWatcher(final PBMsWatcher bin, final Map<Integer, MSClause> clauseMap) {
        return new MSWatcher(clauseMap.get(bin.getClause()), bin.getBlocker());
    }

    /**
     * Serializes a bounded integer queue to a protocol buffer.
     * @param queue the queue
     * @return the protocol buffer
     */
    static PBLngBoundedIntQueue serializeIntQueue(final LNGBoundedIntQueue queue) {
        return PBLngBoundedIntQueue.newBuilder()
                .setElems(Collections.serializeIntVec(queue.getElems()))
                .setFirst(queue.getFirst())
                .setLast(queue.getLast())
                .setSumOfQueue(queue.getSumOfQueue())
                .setMaxSize(queue.getMaxSize())
                .setQueueSize(queue.getQueueSize())
                .build();
    }

    /**
     * Deserializes a bounded integer queue from a protocol buffer.
     * @param bin the protocol buffer
     * @return the queue
     */
    static LNGBoundedIntQueue deserializeIntQueue(final PBLngBoundedIntQueue bin) {
        return new LNGBoundedIntQueue(Collections.deserializeIntVec(bin.getElems()), bin.getFirst(), bin.getLast(),
                bin.getSumOfQueue(), bin.getMaxSize(), bin.getQueueSize());
    }

    /**
     * Serializes a bounded long queue to a protocol buffer.
     * @param queue the queue
     * @return the protocol buffer
     */
    static PBLngBoundedLongQueue serializeLongQueue(final LNGBoundedLongQueue queue) {
        return PBLngBoundedLongQueue.newBuilder()
                .setElems(Collections.serializeLongVec(queue.getElems()))
                .setFirst(queue.getFirst())
                .setLast(queue.getLast())
                .setSumOfQueue(queue.getSumOfQueue())
                .setMaxSize(queue.getMaxSize())
                .setQueueSize(queue.getQueueSize())
                .build();
    }

    /**
     * Deserializes a bounded long queue from a protocol buffer.
     * @param bin the protocol buffer
     * @return the queue
     */
    static LNGBoundedLongQueue deserializeLongQueue(final PBLngBoundedLongQueue bin) {
        return new LNGBoundedLongQueue(Collections.deserializeLongVec(bin.getElems()), bin.getFirst(), bin.getLast(),
                bin.getSumOfQueue(), bin.getMaxSize(), bin.getQueueSize());
    }
}
