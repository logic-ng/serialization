package org.logicng.serialization;

import static com.booleworks.logicng.solvers.sat.ProtoBufSolverCommons.PBMiniSatStyleSolver;
import static org.logicng.serialization.Collections.serializeBoolVec;
import static org.logicng.serialization.Collections.serializeIntVec;
import static org.logicng.serialization.ReflectionHelper.getField;
import static org.logicng.serialization.ReflectionHelper.getSuperField;
import static org.logicng.serialization.ReflectionHelper.setField;
import static org.logicng.serialization.ReflectionHelper.setSuperField;
import static org.logicng.serialization.SatSolverConfigs.serializeMinMode;
import static org.logicng.serialization.SolverDatastructures.serializeIntQueue;
import static org.logicng.serialization.SolverDatastructures.serializeLongQueue;

import com.booleworks.logicng.collections.ProtoBufCollections;
import com.booleworks.logicng.propositions.ProtoBufPropositions;
import com.booleworks.logicng.solvers.ProtoBufSatSolver;
import com.booleworks.logicng.solvers.ProtoBufSatSolver.PBGlucose;
import com.booleworks.logicng.solvers.ProtoBufSatSolver.PBMiniSat2;
import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures;
import com.booleworks.logicng.solvers.datastructures.ProtoBufSolverDatastructures.PBProofInformation;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.logicng.collections.LNGIntVector;
import org.logicng.collections.LNGVector;
import org.logicng.datastructures.Tristate;
import org.logicng.formulas.FormulaFactory;
import org.logicng.propositions.Proposition;
import org.logicng.propositions.StandardProposition;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.datastructures.MSClause;
import org.logicng.solvers.datastructures.MSVariable;
import org.logicng.solvers.datastructures.MSWatcher;
import org.logicng.solvers.sat.GlucoseSyrup;
import org.logicng.solvers.sat.MiniCard;
import org.logicng.solvers.sat.MiniSat2Solver;
import org.logicng.solvers.sat.MiniSatStyleSolver;
import org.logicng.solvers.sat.MiniSatStyleSolver.ProofInformation;
import org.logicng.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A serializer/deserializer for LogicNG SAT solvers.
 * @version 2.5.0
 * @since 2.5.0
 */
public class SolverSerializer {
    private final Function<byte[], Proposition> deserializer;
    private final Function<Proposition, byte[]> serializer;
    private final FormulaFactory f;

    private SolverSerializer(final FormulaFactory f, final Function<Proposition, byte[]> serializer,
                             final Function<byte[], Proposition> deserializer) {
        this.deserializer = deserializer;
        this.serializer = serializer;
        this.f = f;
    }

    /**
     * Generates a new solver serializer for a SAT solver which does not serialize propositions
     * of the proof information.
     * @param f the formula factory
     * @return the solver serializer
     */
    public static SolverSerializer withoutPropositions(final FormulaFactory f) {
        return new SolverSerializer(f, null, null);
    }

    /**
     * Generates a new solver serializer for a SAT solver which does serialize proof information
     * with only standard propositions.
     * @param f the formula factory
     * @return the solver serializer
     */
    public static SolverSerializer withStandardPropositions(final FormulaFactory f) {
        final Function<Proposition, byte[]> serializer = (final Proposition p) -> {
            if (!(p instanceof StandardProposition)) {
                throw new IllegalArgumentException("Can only serialize Standard propositions");
            }
            return Propositions.serializePropositions((StandardProposition) p).toByteArray();
        };
        final Function<byte[], Proposition> deserializer = (final byte[] bs) -> {
            try {
                return Propositions.deserializePropositions(f, ProtoBufPropositions.PBStandardProposition.newBuilder().mergeFrom(bs).build());
            } catch (final InvalidProtocolBufferException e) {
                throw new IllegalArgumentException("Can only deserialize Standard propositions");
            }
        };
        return new SolverSerializer(f, serializer, deserializer);
    }

    /**
     * Generates a new solver serializer for a SAT solver which does serialize proof information
     * with custom propositions.  In this case you have to provide your own serializer and deserializer
     * for your propositions.
     * @param f            the formula factory
     * @param serializer   the serializer for the custom propositions
     * @param deserializer the deserializer for the custom propositions
     * @return the solver serializer
     */
    public static SolverSerializer withCustomPropositions(
            final FormulaFactory f,
            final Function<Proposition, byte[]> serializer,
            final Function<byte[], Proposition> deserializer
    ) {
        return new SolverSerializer(f, serializer, deserializer);
    }

    /**
     * Serializes a MiniSat solver to a file.
     * @param miniSat  the MiniSat solver
     * @param path     the file path
     * @param compress a flag whether the file should be compressed (zip)
     * @throws IOException if there is a problem writing the file
     */
    public void serializeSolverToFile(final MiniSat miniSat, final Path path, final boolean compress) throws IOException {
        try (final OutputStream outputStream = compress ? new GZIPOutputStream(Files.newOutputStream(path)) : Files.newOutputStream(path)) {
            serializeSolverToStream(miniSat, outputStream);
        }
    }

    /**
     * Serializes a MiniSat solver to a stream.
     * @param miniSat the MiniSat solver
     * @param stream  the stream
     * @throws IOException if there is a problem writing to the stream
     */
    public void serializeSolverToStream(final MiniSat miniSat, final OutputStream stream) throws IOException {
        if (miniSat.getStyle() != MiniSat.SolverStyle.GLUCOSE) {
            serializeMiniSat(miniSat).writeTo(stream);
        } else {
            serializeGlucose(miniSat).writeTo(stream);
        }
    }

    /**
     * Serializes a MiniSat solver to a protocol buffer.
     * @param miniSat the MiniSat solver
     * @return the protocol buffer
     */
    public PBMiniSat2 serializeMiniSat(final MiniSat miniSat) {
        if (miniSat.getStyle() == MiniSat.SolverStyle.GLUCOSE) {
            throw new IllegalArgumentException("Cannot serialize a " + miniSat.getStyle() + " solver as MiniSat");
        }
        if (miniSat.underlyingSolver() instanceof MiniSat2Solver) {
            return serialize((MiniSat2Solver) miniSat.underlyingSolver(), new SolverWrapperState(miniSat));
        } else if (miniSat.underlyingSolver() instanceof MiniCard) {
            return serialize((MiniCard) miniSat.underlyingSolver(), new SolverWrapperState(miniSat));
        }
        throw new IllegalArgumentException("Unknown solver type " + miniSat.underlyingSolver());
    }

    /**
     * Serializes a Glucose solver to a protocol buffer.
     * @param miniSat the Glucose solver
     * @return the protocol buffer
     */
    public PBGlucose serializeGlucose(final MiniSat miniSat) {
        if (miniSat.getStyle() != MiniSat.SolverStyle.GLUCOSE) {
            throw new IllegalArgumentException("Cannot serialize a " + miniSat.getStyle() + " solver as Glucose");
        }
        return serialize((GlucoseSyrup) miniSat.underlyingSolver(), new SolverWrapperState(miniSat));
    }

    /**
     * Deserializes a MiniSat solver from a file.
     * @param path     the file path
     * @param compress a flag whether the file should be compressed (zip)
     * @return the solver
     * @throws IOException if there is a problem reading the file
     */
    public MiniSat deserializeMiniSatFromFile(final Path path, final boolean compress) throws IOException {
        try (final InputStream inputStream = compress ? new GZIPInputStream(Files.newInputStream(path)) : Files.newInputStream(path)) {
            return deserializeMiniSatFromStream(inputStream);
        }
    }

    /**
     * Deserializes a MiniSat solver from a stream.
     * @param stream the stream
     * @return the solver
     * @throws IOException if there is a problem reading from the stream
     */
    public MiniSat deserializeMiniSatFromStream(final InputStream stream) throws IOException {
        return deserializeMiniSat(ProtoBufSatSolver.PBMiniSat2.newBuilder().mergeFrom(stream).build());
    }

    /**
     * Deserializes a MiniSat solver from a protocol buffer.
     * @param bin the protocol buffer
     * @return the solver
     */
    public MiniSat deserializeMiniSat(final ProtoBufSatSolver.PBMiniSat2 bin) {
        final MiniSat miniSat = new MiniSat(this.f, deserialize(bin));
        SolverWrapperState.setWrapperState(miniSat, bin.getWrapper());
        return miniSat;
    }

    /**
     * Deserializes a Glucose solver from a file.
     * @param path     the file path
     * @param compress a flag whether the file should be compressed (zip)
     * @return the solver
     * @throws IOException if there is a problem reading the file
     */
    public MiniSat deserializeGlucoseFromFile(final Path path, final boolean compress) throws IOException {
        try (final InputStream inputStream = compress ? new GZIPInputStream(Files.newInputStream(path)) : Files.newInputStream(path)) {
            return deserializeGlucoseFromStream(inputStream);
        }
    }

    /**
     * Deserializes a Glucose solver from a stream.
     * @param stream the stream
     * @return the solver
     * @throws IOException if there is a problem reading from the stream
     */
    public MiniSat deserializeGlucoseFromStream(final InputStream stream) throws IOException {
        return deserializeGlucose(ProtoBufSatSolver.PBGlucose.newBuilder().mergeFrom(stream).build());
    }

    /**
     * Deserializes a Glucose solver from a protocol buffer.
     * @param bin the protocol buffer
     * @return the solver
     */
    public MiniSat deserializeGlucose(final ProtoBufSatSolver.PBGlucose bin) {
        final MiniSat miniSat = new MiniSat(this.f, deserialize(bin));
        SolverWrapperState.setWrapperState(miniSat, bin.getWrapper());
        return miniSat;
    }

    Pair<PBMiniSatStyleSolver, IdentityHashMap<MSClause, Integer>> serializeCommon(final MiniSatStyleSolver solver) {
        final LNGVector<MSClause> clauses = getSuperField(solver, "clauses");
        final LNGVector<MSClause> learnts = getSuperField(solver, "learnts");
        final IdentityHashMap<MSClause, Integer> clauseMap = generateClauseMap(clauses, learnts);
        final PBMiniSatStyleSolver.Builder builder = PBMiniSatStyleSolver.newBuilder();
        builder.setConfig(SatSolverConfigs.serializeMiniSatConfig(getSuperField(solver, "config")));
        builder.setOk(getSuperField(solver, "ok"));
        builder.setQhead(getSuperField(solver, "qhead"));
        builder.setClauses(serializeClauseVec(clauses, clauseMap));
        builder.setLearnts(serializeClauseVec(learnts, clauseMap));
        builder.setWatches(serializeWatches(getSuperField(solver, "watches"), clauseMap));
        builder.setVars(serializeVarVec(getSuperField(solver, "vars"), clauseMap));
        builder.setOrderHeap(SolverDatastructures.serializeHeap(getSuperField(solver, "orderHeap")));
        builder.setTrail(serializeIntVec(getSuperField(solver, "trail")));
        builder.setTrailLim(serializeIntVec(getSuperField(solver, "trailLim")));
        builder.setModel(serializeBoolVec(getSuperField(solver, "model")));
        builder.setConflict(serializeIntVec(getSuperField(solver, "conflict")));
        builder.setAssumptions(serializeIntVec(getSuperField(solver, "assumptions")));
        builder.setSeen(serializeBoolVec(getSuperField(solver, "seen")));
        builder.setAnalyzeBtLevel(getSuperField(solver, "analyzeBtLevel"));
        builder.setClaInc(getSuperField(solver, "claInc"));
        builder.setSimpDBAssigns(getSuperField(solver, "simpDBAssigns"));
        builder.setSimpDBProps(getSuperField(solver, "simpDBProps"));
        builder.setClausesLiterals(getSuperField(solver, "clausesLiterals"));
        builder.setLearntsLiterals(getSuperField(solver, "learntsLiterals"));

        builder.setVarDecay(getSuperField(solver, "varDecay"));
        builder.setVarInc(getSuperField(solver, "varInc"));
        builder.setCcminMode(serializeMinMode(getSuperField(solver, "ccminMode")));
        builder.setRestartFirst(getSuperField(solver, "restartFirst"));
        builder.setRestartInc(getSuperField(solver, "restartInc"));
        builder.setClauseDecay(getSuperField(solver, "clauseDecay"));
        builder.setShouldRemoveSatsisfied(getSuperField(solver, "shouldRemoveSatsisfied"));
        builder.setLearntsizeInc(getSuperField(solver, "learntsizeInc"));
        builder.setIncremental(getSuperField(solver, "incremental"));

        builder.putAllName2Idx(getSuperField(solver, "name2idx"));

        final LNGVector<LNGIntVector> pgProof = getSuperField(solver, "pgProof");
        if (pgProof != null) {
            builder.setPgProof(Collections.serializeVec(pgProof));
        }
        final LNGVector<ProofInformation> pgOriginalClauses = getSuperField(solver, "pgOriginalClauses");
        if (pgOriginalClauses != null) {
            for (final ProofInformation oc : pgOriginalClauses) {
                builder.addPgOriginalClauses(serialize(oc));
            }
        }

        final Stack<Integer> backboneCandidates = getSuperField(solver, "backboneCandidates");
        if (backboneCandidates != null) {
            builder.setBackboneCandidates(serializeStack(backboneCandidates));
        }
        final LNGIntVector backboneAssumptions = getSuperField(solver, "backboneAssumptions");
        if (backboneAssumptions != null) {
            builder.setBackboneAssumptions(serializeIntVec(backboneAssumptions));
        }
        final HashMap<Integer, Tristate> backboneMap = getSuperField(solver, "backboneMap");
        if (backboneMap != null) {
            builder.putAllBackboneMap(serializeBbMap(backboneMap));
        }
        builder.setComputingBackbone(getSuperField(solver, "computingBackbone"));

        builder.setSelectionOrder(serializeIntVec(getSuperField(solver, "selectionOrder")));
        builder.setSelectionOrderIdx(getSuperField(solver, "selectionOrderIdx"));

        builder.setLearntsizeAdjustConfl(getSuperField(solver, "learntsizeAdjustConfl"));
        builder.setLearntsizeAdjustCnt(getSuperField(solver, "learntsizeAdjustCnt"));
        builder.setLearntsizeAdjustStartConfl(getSuperField(solver, "learntsizeAdjustStartConfl"));
        builder.setLearntsizeAdjustInc(getSuperField(solver, "learntsizeAdjustInc"));
        builder.setMaxLearnts(getSuperField(solver, "maxLearnts"));

        return new Pair<>(builder.build(), clauseMap);
    }

    private Map<Integer, MSClause> deserializeCommon(final PBMiniSatStyleSolver bin, final MiniSatStyleSolver solver) {
        final Map<Integer, MSClause> clauseMap = new TreeMap<>();
        setSuperField(solver, "config", SatSolverConfigs.deserializeMiniSatConfig(bin.getConfig()));
        setSuperField(solver, "ok", bin.getOk());
        setSuperField(solver, "qhead", bin.getQhead());
        setSuperField(solver, "clauses", deserializeClauseVec(bin.getClauses(), clauseMap));
        setSuperField(solver, "learnts", deserializeClauseVec(bin.getLearnts(), clauseMap));
        setSuperField(solver, "watches", deserializeWatches(bin.getWatches(), clauseMap));
        setSuperField(solver, "vars", deserializeVarVec(bin.getVars(), clauseMap));
        setSuperField(solver, "orderHeap", SolverDatastructures.deserializeHeap(bin.getOrderHeap(), solver));
        setSuperField(solver, "trail", Collections.deserializeIntVec(bin.getTrail()));
        setSuperField(solver, "trailLim", Collections.deserializeIntVec(bin.getTrailLim()));
        setSuperField(solver, "model", Collections.deserializeBooVec(bin.getModel()));
        setSuperField(solver, "conflict", Collections.deserializeIntVec(bin.getConflict()));
        setSuperField(solver, "assumptions", Collections.deserializeIntVec(bin.getAssumptions()));
        setSuperField(solver, "seen", Collections.deserializeBooVec(bin.getSeen()));
        setSuperField(solver, "analyzeBtLevel", bin.getAnalyzeBtLevel());
        setSuperField(solver, "claInc", bin.getClaInc());
        setSuperField(solver, "simpDBAssigns", bin.getSimpDBAssigns());
        setSuperField(solver, "simpDBProps", bin.getSimpDBProps());
        setSuperField(solver, "clausesLiterals", bin.getClausesLiterals());
        setSuperField(solver, "learntsLiterals", bin.getLearntsLiterals());

        setSuperField(solver, "varDecay", bin.getVarDecay());
        setSuperField(solver, "varInc", bin.getVarInc());
        setSuperField(solver, "ccminMode", SatSolverConfigs.deserializeMinMode(bin.getCcminMode()));
        setSuperField(solver, "restartFirst", bin.getRestartFirst());
        setSuperField(solver, "restartInc", bin.getRestartInc());
        setSuperField(solver, "clauseDecay", bin.getClauseDecay());
        setSuperField(solver, "shouldRemoveSatsisfied", bin.getShouldRemoveSatsisfied());
        setSuperField(solver, "learntsizeInc", bin.getLearntsizeInc());
        setSuperField(solver, "incremental", bin.getIncremental());

        setSuperField(solver, "name2idx", new TreeMap<>(bin.getName2IdxMap()));
        final Map<Integer, String> idx2name = new TreeMap<>();
        bin.getName2IdxMap().forEach((k, v) -> idx2name.put(v, k));
        setSuperField(solver, "idx2name", idx2name);

        if (bin.hasPgProof()) {
            setSuperField(solver, "pgProof", Collections.deserializeVec(bin.getPgProof()));
        }
        if (bin.getPgOriginalClausesCount() > 0) {
            final LNGVector<ProofInformation> originalClauses = new LNGVector<>(bin.getPgOriginalClausesCount());
            for (final PBProofInformation pi : bin.getPgOriginalClausesList()) {
                originalClauses.push(deserialize(pi));
            }
            setSuperField(solver, "pgOriginalClauses", originalClauses);
        }

        if (bin.hasBackboneCandidates()) {
            setSuperField(solver, "backboneCandidates", deserializeStack(bin.getBackboneCandidates()));
        }
        if (bin.hasBackboneAssumptions()) {
            setSuperField(solver, "backboneAssumptions", Collections.deserializeIntVec(bin.getBackboneAssumptions()));
        }
        setSuperField(solver, "backboneMap", deserializeBbMap(bin.getBackboneMapMap()));
        setSuperField(solver, "computingBackbone", bin.getComputingBackbone());

        setSuperField(solver, "selectionOrder", Collections.deserializeIntVec(bin.getSelectionOrder()));
        setSuperField(solver, "selectionOrderIdx", bin.getSelectionOrderIdx());

        setSuperField(solver, "learntsizeAdjustConfl", bin.getLearntsizeAdjustConfl());
        setSuperField(solver, "learntsizeAdjustCnt", bin.getLearntsizeAdjustCnt());
        setSuperField(solver, "learntsizeAdjustStartConfl", bin.getLearntsizeAdjustStartConfl());
        setSuperField(solver, "learntsizeAdjustInc", bin.getLearntsizeAdjustInc());
        setSuperField(solver, "maxLearnts", bin.getMaxLearnts());

        return clauseMap;
    }

    PBMiniSat2 serialize(final MiniSat2Solver solver, final SolverWrapperState wrapperState) {
        return PBMiniSat2.newBuilder()
                .setCommon(serializeCommon(solver).first())
                .setWrapper(serializeWrapperState(wrapperState))
                .setUnitClauses(serializeIntVec(getField(solver, "unitClauses")))
                .build();
    }

    PBMiniSat2 serialize(final MiniCard solver, final SolverWrapperState wrapperState) {
        return PBMiniSat2.newBuilder()
                .setCommon(serializeCommon(solver).first())
                .setWrapper(serializeWrapperState(wrapperState))
                .setUnitClauses(serializeIntVec(getField(solver, "unitClauses")))
                .build();
    }

    MiniSatStyleSolver deserialize(final PBMiniSat2 bin) {
        final MiniSatStyleSolver solver = bin.getWrapper().getSolverStyle() == ProtoBufSatSolver.PBSolverStyle.MINISAT
                ? new MiniSat2Solver(SatSolverConfigs.deserializeMiniSatConfig(bin.getCommon().getConfig()))
                : new MiniCard(SatSolverConfigs.deserializeMiniSatConfig(bin.getCommon().getConfig()));
        deserializeCommon(bin.getCommon(), solver);
        setField(solver, "unitClauses", Collections.deserializeIntVec(bin.getUnitClauses()));
        return solver;
    }

    PBGlucose serialize(final GlucoseSyrup solver, final SolverWrapperState wrapperState) {
        final Pair<PBMiniSatStyleSolver, IdentityHashMap<MSClause, Integer>> common = serializeCommon(solver);
        return PBGlucose.newBuilder()
                .setGlucoseConfig(SatSolverConfigs.serializeGlucoseConfig(getField(solver, "glucoseConfig")))
                .setCommon(common.first())
                .setWrapper(serializeWrapperState(wrapperState))
                .setWatchesBin(serializeWatches(getField(solver, "watchesBin"), common.second()))
                .setPermDiff(serializeIntVec(getField(solver, "permDiff")))
                .setLastDecisionLevel(serializeIntVec(getField(solver, "lastDecisionLevel")))
                .setLbdQueue(serializeLongQueue(getField(solver, "lbdQueue")))
                .setTrailQueue(serializeIntQueue(getField(solver, "trailQueue")))
                .setAssump(serializeBoolVec(getField(solver, "assump")))
                .setMyflag(getField(solver, "myflag"))
                .setAnalyzeLBD(getField(solver, "analyzeLBD"))
                .setAnalyzeSzWithoutSelectors(getField(solver, "analyzeSzWithoutSelectors"))
                .setNbclausesbeforereduce(getField(solver, "nbclausesbeforereduce"))
                .setConflicts(getField(solver, "conflicts"))
                .setConflictsRestarts(getField(solver, "conflictsRestarts"))
                .setSumLBD(getField(solver, "sumLBD"))
                .setCurRestart(getField(solver, "curRestart"))
                .build();
    }

    private GlucoseSyrup deserialize(final PBGlucose bin) {
        final GlucoseSyrup solver =
                new GlucoseSyrup(SatSolverConfigs.deserializeMiniSatConfig(bin.getCommon().getConfig()), SatSolverConfigs.deserializeGlucoseConfig(bin.getGlucoseConfig()));
        final Map<Integer, MSClause> clauseMap = deserializeCommon(bin.getCommon(), solver);
        setField(solver, "watchesBin", deserializeWatches(bin.getWatchesBin(), clauseMap));
        setField(solver, "permDiff", Collections.deserializeIntVec(bin.getPermDiff()));
        setField(solver, "lastDecisionLevel", Collections.deserializeIntVec(bin.getLastDecisionLevel()));
        setField(solver, "lbdQueue", SolverDatastructures.deserializeLongQueue(bin.getLbdQueue()));
        setField(solver, "trailQueue", SolverDatastructures.deserializeIntQueue(bin.getTrailQueue()));
        setField(solver, "assump", Collections.deserializeBooVec(bin.getAssump()));
        setField(solver, "myflag", bin.getMyflag());
        setField(solver, "analyzeLBD", bin.getAnalyzeLBD());
        setField(solver, "analyzeSzWithoutSelectors", bin.getAnalyzeSzWithoutSelectors());
        setField(solver, "nbclausesbeforereduce", bin.getNbclausesbeforereduce());
        setField(solver, "conflicts", bin.getConflicts());
        setField(solver, "conflictsRestarts", bin.getConflictsRestarts());
        setField(solver, "sumLBD", bin.getSumLBD());
        setField(solver, "curRestart", bin.getCurRestart());
        return solver;
    }

    private static IdentityHashMap<MSClause, Integer> generateClauseMap(final LNGVector<MSClause> clauses, final LNGVector<MSClause> learnts) {
        final IdentityHashMap<MSClause, Integer> clauseMap = new IdentityHashMap<>();
        for (final MSClause clause : clauses) {
            clauseMap.put(clause, clauseMap.size());
        }
        for (final MSClause learnt : learnts) {
            clauseMap.put(learnt, clauseMap.size());
        }
        return clauseMap;
    }

    private static ProtoBufSolverDatastructures.PBMsClauseVector serializeClauseVec(final LNGVector<MSClause> vec,
                                                                                    final IdentityHashMap<MSClause, Integer> clauseMap) {
        final ProtoBufSolverDatastructures.PBMsClauseVector.Builder builder = ProtoBufSolverDatastructures.PBMsClauseVector.newBuilder();
        for (final MSClause clause : vec) {
            builder.addElement(SolverDatastructures.serializeClause(clause, clauseMap.get(clause)));
        }
        return builder.build();
    }

    private static LNGVector<MSClause> deserializeClauseVec(final ProtoBufSolverDatastructures.PBMsClauseVector bin, final Map<Integer, MSClause> clauseMap) {
        final LNGVector<MSClause> vec = new LNGVector<>(bin.getElementCount());
        for (int i = 0; i < bin.getElementCount(); i++) {
            final ProtoBufSolverDatastructures.PBMsClause binClause = bin.getElement(i);
            final MSClause clause = SolverDatastructures.deserializeClause(binClause);
            clauseMap.put(binClause.getId(), clause);
            vec.push(clause);
        }
        return vec;
    }

    private static ProtoBufSolverDatastructures.PBMsWatcherVectorVector serializeWatches(final LNGVector<LNGVector<MSWatcher>> vec,
                                                                                         final IdentityHashMap<MSClause, Integer> clauseMap) {
        final ProtoBufSolverDatastructures.PBMsWatcherVectorVector.Builder builder = ProtoBufSolverDatastructures.PBMsWatcherVectorVector.newBuilder();
        for (final LNGVector<MSWatcher> watchList : vec) {
            final ProtoBufSolverDatastructures.PBMsWatcherVector.Builder watchBuilder = ProtoBufSolverDatastructures.PBMsWatcherVector.newBuilder();
            for (final MSWatcher watch : watchList) {
                watchBuilder.addElement(SolverDatastructures.serializeWatcher(watch, clauseMap));
            }
            builder.addElement(watchBuilder.build());
        }
        return builder.build();
    }

    private static LNGVector<LNGVector<MSWatcher>> deserializeWatches(final ProtoBufSolverDatastructures.PBMsWatcherVectorVector bin,
                                                                      final Map<Integer, MSClause> clauseMap) {
        final LNGVector<LNGVector<MSWatcher>> vec = new LNGVector<>(bin.getElementCount());
        for (int i = 0; i < bin.getElementCount(); i++) {
            final ProtoBufSolverDatastructures.PBMsWatcherVector binWatch = bin.getElement(i);
            final LNGVector<MSWatcher> watch = new LNGVector<>(binWatch.getElementCount());
            for (int j = 0; j < binWatch.getElementCount(); j++) {
                watch.push(SolverDatastructures.deserializeWatcher(binWatch.getElement(j), clauseMap));
            }
            vec.push(watch);
        }
        return vec;
    }

    private static ProtoBufSolverDatastructures.PBMsVariableVector serializeVarVec(final LNGVector<MSVariable> vec,
                                                                                   final IdentityHashMap<MSClause, Integer> clauseMap) {
        final ProtoBufSolverDatastructures.PBMsVariableVector.Builder builder = ProtoBufSolverDatastructures.PBMsVariableVector.newBuilder();
        for (final MSVariable var : vec) {
            builder.addElement(SolverDatastructures.serializeVariable(var, clauseMap));
        }
        return builder.build();
    }

    private static LNGVector<MSVariable> deserializeVarVec(final ProtoBufSolverDatastructures.PBMsVariableVector bin, final Map<Integer, MSClause> clauseMap) {
        final LNGVector<MSVariable> vec = new LNGVector<>(bin.getElementCount());
        for (int i = 0; i < bin.getElementCount(); i++) {
            vec.push(SolverDatastructures.deserializeVariable(bin.getElement(i), clauseMap));
        }
        return vec;
    }

    public static ProtoBufCollections.PBIntVector serializeStack(final Stack<Integer> stack) {
        if (stack == null) {
            return null;
        }
        final ProtoBufCollections.PBIntVector.Builder vec = ProtoBufCollections.PBIntVector.newBuilder();
        for (final Integer integer : stack) {
            vec.addElement(integer);
        }
        vec.setSize(stack.size());
        return vec.build();
    }

    public static Stack<Integer> deserializeStack(final ProtoBufCollections.PBIntVector vec) {
        final Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < vec.getSize(); i++) {
            stack.push(vec.getElement(i));
        }
        return stack;
    }

    private static HashMap<Integer, ProtoBufSolverDatastructures.PBTristate> serializeBbMap(final Map<Integer, Tristate> map) {
        final HashMap<Integer, ProtoBufSolverDatastructures.PBTristate> ser = new HashMap<>();
        map.forEach((k, v) -> ser.put(k, SolverDatastructures.serializeTristate(v)));
        return ser;
    }

    private static HashMap<Integer, Tristate> deserializeBbMap(final Map<Integer, ProtoBufSolverDatastructures.PBTristate> map) {
        if (map.isEmpty()) {
            return null;
        }
        final HashMap<Integer, Tristate> ser = new HashMap<>();
        map.forEach((k, v) -> ser.put(k, SolverDatastructures.deserializeTristate(v)));
        return ser;
    }

    private static ProtoBufSatSolver.PBWrapperState serializeWrapperState(final SolverWrapperState state) {
        return ProtoBufSatSolver.PBWrapperState.newBuilder()
                .setResult(SolverDatastructures.serializeTristate(state.result))
                .setValidStates(serializeIntVec(state.validStates))
                .setNextStateId(state.nextStateId)
                .setLastComputationWithAssumptions(state.lastComputationWithAssumptions)
                .setSolverStyle(SolverWrapperState.serializeSolverStyle(state.solverStyle))
                .build();
    }

    private PBProofInformation serialize(final ProofInformation pi) {
        final PBProofInformation.Builder builder = PBProofInformation.newBuilder().setClause(serializeIntVec(pi.clause()));
        if (pi.proposition() != null) {
            builder.setProposition(ByteString.copyFrom(this.serializer.apply(pi.proposition())));
        }
        return builder.build();
    }

    private ProofInformation deserialize(final PBProofInformation bin) {
        final Proposition prop = bin.hasProposition() ? this.deserializer.apply(bin.getProposition().toByteArray()) : null;
        return new ProofInformation(Collections.deserializeIntVec(bin.getClause()), prop);
    }
}
