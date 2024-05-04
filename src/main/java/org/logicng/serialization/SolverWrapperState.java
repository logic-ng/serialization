package org.logicng.serialization;

import static com.booleworks.logicng.solvers.ProtoBufSatSolver.PBWrapperState;
import static org.logicng.serialization.ReflectionHelper.getField;
import static org.logicng.serialization.ReflectionHelper.getSuperField;
import static org.logicng.serialization.ReflectionHelper.setField;
import static org.logicng.serialization.ReflectionHelper.setSuperField;

import com.booleworks.logicng.solvers.ProtoBufSatSolver.PBSolverStyle;
import org.logicng.collections.LNGIntVector;
import org.logicng.datastructures.Tristate;
import org.logicng.solvers.MiniSat;

/**
 * A class which captures some information from the {@link MiniSat} wrapper class,
 * required for serializing and deserializing SAT solvers.
 * @version 2.5.0
 * @since 2.5.0
 */
public class SolverWrapperState {
    public final Tristate result;
    public final LNGIntVector validStates;
    public final int nextStateId;
    public final boolean lastComputationWithAssumptions;
    public final MiniSat.SolverStyle solverStyle;

    /**
     * Constructs a new solver wrapper state.
     * @param solver the solver
     */
    public SolverWrapperState(final MiniSat solver) {
        this.result = getSuperField(solver, "result");
        this.validStates = getField(solver, "validStates");
        this.nextStateId = getField(solver, "nextStateId");
        this.lastComputationWithAssumptions = getField(solver, "lastComputationWithAssumptions");
        this.solverStyle = getField(solver, "style");
    }

    /**
     * Sets a solver wrapper state to a given solver.
     * @param miniSat the solver
     * @param wrapper the wrapper state
     */
    public static void setWrapperState(final MiniSat miniSat, final PBWrapperState wrapper) {
        setSuperField(miniSat, "result", SolverDatastructures.deserializeTristate(wrapper.getResult()));
        setField(miniSat, "validStates", Collections.deserializeIntVec(wrapper.getValidStates()));
        setField(miniSat, "nextStateId", wrapper.getNextStateId());
        setField(miniSat, "lastComputationWithAssumptions", wrapper.getLastComputationWithAssumptions());
        setField(miniSat, "style", deserializeSolverStyle(wrapper.getSolverStyle()));
    }

    /**
     * Serializes a solver style to a protocol buffer.
     * @param solverStyle the solver style
     * @return the protocol buffer
     */
    public static PBSolverStyle serializeSolverStyle(final MiniSat.SolverStyle solverStyle) {
        switch (solverStyle) {
            case MINISAT:
                return PBSolverStyle.MINISAT;
            case GLUCOSE:
                return PBSolverStyle.GLUCOSE;
            case MINICARD:
                return PBSolverStyle.MINICARD;
            default:
                throw new IllegalArgumentException("Unknwon solver style " + solverStyle);
        }
    }

    /**
     * Deserializes a solver style from a protocol buffer.
     * @param bin the protocol buffer
     * @return the solver style
     */
    private static MiniSat.SolverStyle deserializeSolverStyle(final PBSolverStyle bin) {
        switch (bin) {
            case MINISAT:
                return MiniSat.SolverStyle.MINISAT;
            case GLUCOSE:
                return MiniSat.SolverStyle.GLUCOSE;
            case MINICARD:
                return MiniSat.SolverStyle.MINICARD;
            default:
                throw new IllegalArgumentException("Unknwon solver style " + bin);
        }
    }
}
