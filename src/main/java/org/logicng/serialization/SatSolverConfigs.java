package org.logicng.serialization;

import static com.booleworks.logicng.solvers.sat.ProtoBufSolverCommons.PBGlucoseConfig;
import static com.booleworks.logicng.solvers.sat.ProtoBufSolverCommons.PBMiniSatConfig;

import com.booleworks.logicng.solvers.sat.ProtoBufSolverCommons.PBClauseMinimization;
import com.booleworks.logicng.solvers.sat.ProtoBufSolverCommons.PBCnfMethod;
import org.logicng.solvers.sat.GlucoseConfig;
import org.logicng.solvers.sat.MiniSatConfig;
import org.logicng.solvers.sat.MiniSatConfig.CNFMethod;
import org.logicng.solvers.sat.MiniSatConfig.ClauseMinimization;

/**
 * Serialization methods for SAT solver configurations.
 * @version 2.5.0
 * @since 2.5.0
 */
public interface SatSolverConfigs {

    /**
     * Serializes a MiniSat configuration to a protocol buffer.
     * @param config the configuration
     * @return the protocol buffer
     */
    static PBMiniSatConfig serializeMiniSatConfig(final MiniSatConfig config) {
        return PBMiniSatConfig.newBuilder()
                .setVarDecay(config.getVarDecay())
                .setClauseMin(serializeMinMode(config.getClauseMin()))
                .setRestartFirst(config.getRestartFirst())
                .setRestartInc(config.getRestartInc())
                .setClauseDecay(config.getClauseDecay())
                .setRemoveSatisfied(config.isRemoveSatisfied())
                .setLearntsizeFactor(config.getLearntsizeFactor())
                .setLearntsizeInc(config.getLearntsizeInc())
                .setIncremental(config.isIncremental())
                .setInitialPhase(config.isInitialPhase())
                .setProofGeneration(config.proofGeneration())
                .setCnfMethod(serializeCnfMode(config.getCnfMethod()))
                .setBbInitialUBCheckForRotatableLiterals(config.isBbInitialUBCheckForRotatableLiterals())
                .setBbCheckForComplementModelLiterals(config.isBbCheckForComplementModelLiterals())
                .setBbCheckForRotatableLiterals(config.isBbCheckForRotatableLiterals())
                .build();
    }

    /**
     * Deserializes a MiniSat configuration from a protocol buffer.
     * @param bin the protocol buffer
     * @return the configuration
     */
    static MiniSatConfig deserializeMiniSatConfig(final PBMiniSatConfig bin) {
        return MiniSatConfig.builder()
                .varDecay(bin.getVarDecay())
                .clMinimization(deserializeMinMode(bin.getClauseMin()))
                .restartFirst(bin.getRestartFirst())
                .restartInc(bin.getRestartInc())
                .clauseDecay(bin.getClauseDecay())
                .removeSatisfied(bin.getRemoveSatisfied())
                .lsFactor(bin.getLearntsizeFactor())
                .lsInc(bin.getLearntsizeInc())
                .incremental(bin.getIncremental())
                .initialPhase(bin.getInitialPhase())
                .proofGeneration(bin.getProofGeneration())
                .cnfMethod(deserializeCnfMode(bin.getCnfMethod()))
                .bbInitialUBCheckForRotatableLiterals(bin.getBbInitialUBCheckForRotatableLiterals())
                .bbCheckForComplementModelLiterals(bin.getBbCheckForComplementModelLiterals())
                .bbCheckForRotatableLiterals(bin.getBbCheckForRotatableLiterals())
                .build();
    }

    /**
     * Serializes a Glucose configuration to a protocol buffer.
     * @param config the configuration
     * @return the protocol buffer
     */
    static PBGlucoseConfig serializeGlucoseConfig(final GlucoseConfig config) {
        return PBGlucoseConfig.newBuilder()
                .setLbLBDMinimizingClause(config.getLbLBDMinimizingClause())
                .setLbLBDFrozenClause(config.getLbLBDFrozenClause())
                .setLbSizeMinimizingClause(config.getLbSizeMinimizingClause())
                .setFirstReduceDB(config.getFirstReduceDB())
                .setSpecialIncReduceDB(config.getSpecialIncReduceDB())
                .setIncReduceDB(config.getIncReduceDB())
                .setFactorK(config.getFactorK())
                .setFactorR(config.getFactorR())
                .setSizeLBDQueue(config.getSizeLBDQueue())
                .setSizeTrailQueue(config.getSizeTrailQueue())
                .setReduceOnSize(config.isReduceOnSize())
                .setReduceOnSizeSize(config.getReduceOnSizeSize())
                .setMaxVarDecay(config.getMaxVarDecay())
                .build();
    }

    /**
     * Deserializes a Glucose configuration from a protocol buffer.
     * @param bin the protocol buffer
     * @return the configuration
     */
    static GlucoseConfig deserializeGlucoseConfig(final PBGlucoseConfig bin) {
        return GlucoseConfig.builder()
                .lbLBDMinimizingClause(bin.getLbLBDMinimizingClause())
                .lbLBDFrozenClause(bin.getLbLBDFrozenClause())
                .lbSizeMinimizingClause(bin.getLbSizeMinimizingClause())
                .firstReduceDB(bin.getFirstReduceDB())
                .specialIncReduceDB(bin.getSpecialIncReduceDB())
                .incReduceDB(bin.getIncReduceDB())
                .factorK(bin.getFactorK())
                .factorR(bin.getFactorR())
                .sizeLBDQueue(bin.getSizeLBDQueue())
                .sizeTrailQueue(bin.getSizeTrailQueue())
                .reduceOnSize(bin.getReduceOnSize())
                .reduceOnSizeSize(bin.getReduceOnSizeSize())
                .maxVarDecay(bin.getMaxVarDecay())
                .build();
    }

    /**
     * Serializes the clause minimization algorithm to a protocol buffer.
     * @param minimization the algorithm
     * @return the protocol buffer
     */
    static PBClauseMinimization serializeMinMode(final ClauseMinimization minimization) {
        switch (minimization) {
            case NONE:
                return PBClauseMinimization.NONE;
            case BASIC:
                return PBClauseMinimization.BASIC;
            case DEEP:
                return PBClauseMinimization.DEEP;
            default:
                throw new IllegalArgumentException("Unknown clause minimization: " + minimization);
        }
    }

    /**
     * Deserializes the clause minimization algorithm from a protocol buffer.
     * @param bin the protocol buffer
     * @return the algorithm
     */
    static ClauseMinimization deserializeMinMode(final PBClauseMinimization bin) {
        switch (bin) {
            case NONE:
                return ClauseMinimization.NONE;
            case BASIC:
                return ClauseMinimization.BASIC;
            case DEEP:
                return ClauseMinimization.DEEP;
            default:
                throw new IllegalArgumentException("Unknown clause minimization: " + bin);
        }
    }

    /**
     * Serializes the CNF algorithm to a protocol buffer.
     * @param cnf the algorithm
     * @return the protocol buffer
     */
    static PBCnfMethod serializeCnfMode(final CNFMethod cnf) {
        switch (cnf) {
            case FACTORY_CNF:
                return PBCnfMethod.FACTORY_CNF;
            case PG_ON_SOLVER:
                return PBCnfMethod.PG_ON_SOLVER;
            case FULL_PG_ON_SOLVER:
                return PBCnfMethod.FULL_PG_ON_SOLVER;
            default:
                throw new IllegalArgumentException("Unknown CNF method: " + cnf);
        }
    }

    /**
     * Deserializes the CNF algorithm from a protocol buffer.
     * @param bin the protocol buffer
     * @return the algorithm
     */
    static CNFMethod deserializeCnfMode(final PBCnfMethod bin) {
        switch (bin) {
            case FACTORY_CNF:
                return CNFMethod.FACTORY_CNF;
            case PG_ON_SOLVER:
                return CNFMethod.PG_ON_SOLVER;
            case FULL_PG_ON_SOLVER:
                return CNFMethod.FULL_PG_ON_SOLVER;
            default:
                throw new IllegalArgumentException("Unknown CNF method: " + bin);
        }
    }
}
