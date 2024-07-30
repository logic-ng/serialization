// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: propositions.proto
// Protobuf Java Version: 4.27.1

package com.booleworks.logicng.propositions;

public final class ProtoBufPropositions {
  private ProtoBufPropositions() {}
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 27,
      /* patch= */ 1,
      /* suffix= */ "",
      ProtoBufPropositions.class.getName());
  }
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface PBStandardPropositionOrBuilder extends
      // @@protoc_insertion_point(interface_extends:propositions.PBStandardProposition)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.formulas.PBFormulas formula = 1;</code>
     * @return Whether the formula field is set.
     */
    boolean hasFormula();
    /**
     * <code>.formulas.PBFormulas formula = 1;</code>
     * @return The formula.
     */
    com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas getFormula();
    /**
     * <code>.formulas.PBFormulas formula = 1;</code>
     */
    com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulasOrBuilder getFormulaOrBuilder();

    /**
     * <code>string description = 2;</code>
     * @return The description.
     */
    java.lang.String getDescription();
    /**
     * <code>string description = 2;</code>
     * @return The bytes for description.
     */
    com.google.protobuf.ByteString
        getDescriptionBytes();
  }
  /**
   * Protobuf type {@code propositions.PBStandardProposition}
   */
  public static final class PBStandardProposition extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:propositions.PBStandardProposition)
      PBStandardPropositionOrBuilder {
  private static final long serialVersionUID = 0L;
    static {
      com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
        com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
        /* major= */ 4,
        /* minor= */ 27,
        /* patch= */ 1,
        /* suffix= */ "",
        PBStandardProposition.class.getName());
    }
    // Use PBStandardProposition.newBuilder() to construct.
    private PBStandardProposition(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
    }
    private PBStandardProposition() {
      description_ = "";
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.booleworks.logicng.propositions.ProtoBufPropositions.internal_static_propositions_PBStandardProposition_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.booleworks.logicng.propositions.ProtoBufPropositions.internal_static_propositions_PBStandardProposition_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition.class, com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition.Builder.class);
    }

    private int bitField0_;
    public static final int FORMULA_FIELD_NUMBER = 1;
    private com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas formula_;
    /**
     * <code>.formulas.PBFormulas formula = 1;</code>
     * @return Whether the formula field is set.
     */
    @java.lang.Override
    public boolean hasFormula() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>.formulas.PBFormulas formula = 1;</code>
     * @return The formula.
     */
    @java.lang.Override
    public com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas getFormula() {
      return formula_ == null ? com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.getDefaultInstance() : formula_;
    }
    /**
     * <code>.formulas.PBFormulas formula = 1;</code>
     */
    @java.lang.Override
    public com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulasOrBuilder getFormulaOrBuilder() {
      return formula_ == null ? com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.getDefaultInstance() : formula_;
    }

    public static final int DESCRIPTION_FIELD_NUMBER = 2;
    @SuppressWarnings("serial")
    private volatile java.lang.Object description_ = "";
    /**
     * <code>string description = 2;</code>
     * @return The description.
     */
    @java.lang.Override
    public java.lang.String getDescription() {
      java.lang.Object ref = description_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        description_ = s;
        return s;
      }
    }
    /**
     * <code>string description = 2;</code>
     * @return The bytes for description.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString
        getDescriptionBytes() {
      java.lang.Object ref = description_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        description_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) != 0)) {
        output.writeMessage(1, getFormula());
      }
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(description_)) {
        com.google.protobuf.GeneratedMessage.writeString(output, 2, description_);
      }
      getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) != 0)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, getFormula());
      }
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(description_)) {
        size += com.google.protobuf.GeneratedMessage.computeStringSize(2, description_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition)) {
        return super.equals(obj);
      }
      com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition other = (com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition) obj;

      if (hasFormula() != other.hasFormula()) return false;
      if (hasFormula()) {
        if (!getFormula()
            .equals(other.getFormula())) return false;
      }
      if (!getDescription()
          .equals(other.getDescription())) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasFormula()) {
        hash = (37 * hash) + FORMULA_FIELD_NUMBER;
        hash = (53 * hash) + getFormula().hashCode();
      }
      hash = (37 * hash) + DESCRIPTION_FIELD_NUMBER;
      hash = (53 * hash) + getDescription().hashCode();
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input);
    }
    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code propositions.PBStandardProposition}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:propositions.PBStandardProposition)
        com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardPropositionOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.booleworks.logicng.propositions.ProtoBufPropositions.internal_static_propositions_PBStandardProposition_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.booleworks.logicng.propositions.ProtoBufPropositions.internal_static_propositions_PBStandardProposition_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition.class, com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition.Builder.class);
      }

      // Construct using com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage
                .alwaysUseFieldBuilders) {
          getFormulaFieldBuilder();
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        formula_ = null;
        if (formulaBuilder_ != null) {
          formulaBuilder_.dispose();
          formulaBuilder_ = null;
        }
        description_ = "";
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.booleworks.logicng.propositions.ProtoBufPropositions.internal_static_propositions_PBStandardProposition_descriptor;
      }

      @java.lang.Override
      public com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition getDefaultInstanceForType() {
        return com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition.getDefaultInstance();
      }

      @java.lang.Override
      public com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition build() {
        com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition buildPartial() {
        com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition result = new com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition result) {
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.formula_ = formulaBuilder_ == null
              ? formula_
              : formulaBuilder_.build();
          to_bitField0_ |= 0x00000001;
        }
        if (((from_bitField0_ & 0x00000002) != 0)) {
          result.description_ = description_;
        }
        result.bitField0_ |= to_bitField0_;
      }

      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition) {
          return mergeFrom((com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition other) {
        if (other == com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition.getDefaultInstance()) return this;
        if (other.hasFormula()) {
          mergeFormula(other.getFormula());
        }
        if (!other.getDescription().isEmpty()) {
          description_ = other.description_;
          bitField0_ |= 0x00000002;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new java.lang.NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10: {
                input.readMessage(
                    getFormulaFieldBuilder().getBuilder(),
                    extensionRegistry);
                bitField0_ |= 0x00000001;
                break;
              } // case 10
              case 18: {
                description_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000002;
                break;
              } // case 18
              default: {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }
      private int bitField0_;

      private com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas formula_;
      private com.google.protobuf.SingleFieldBuilder<
          com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas, com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.Builder, com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulasOrBuilder> formulaBuilder_;
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       * @return Whether the formula field is set.
       */
      public boolean hasFormula() {
        return ((bitField0_ & 0x00000001) != 0);
      }
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       * @return The formula.
       */
      public com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas getFormula() {
        if (formulaBuilder_ == null) {
          return formula_ == null ? com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.getDefaultInstance() : formula_;
        } else {
          return formulaBuilder_.getMessage();
        }
      }
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       */
      public Builder setFormula(com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas value) {
        if (formulaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          formula_ = value;
        } else {
          formulaBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       */
      public Builder setFormula(
          com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.Builder builderForValue) {
        if (formulaBuilder_ == null) {
          formula_ = builderForValue.build();
        } else {
          formulaBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       */
      public Builder mergeFormula(com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas value) {
        if (formulaBuilder_ == null) {
          if (((bitField0_ & 0x00000001) != 0) &&
            formula_ != null &&
            formula_ != com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.getDefaultInstance()) {
            getFormulaBuilder().mergeFrom(value);
          } else {
            formula_ = value;
          }
        } else {
          formulaBuilder_.mergeFrom(value);
        }
        if (formula_ != null) {
          bitField0_ |= 0x00000001;
          onChanged();
        }
        return this;
      }
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       */
      public Builder clearFormula() {
        bitField0_ = (bitField0_ & ~0x00000001);
        formula_ = null;
        if (formulaBuilder_ != null) {
          formulaBuilder_.dispose();
          formulaBuilder_ = null;
        }
        onChanged();
        return this;
      }
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       */
      public com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.Builder getFormulaBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getFormulaFieldBuilder().getBuilder();
      }
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       */
      public com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulasOrBuilder getFormulaOrBuilder() {
        if (formulaBuilder_ != null) {
          return formulaBuilder_.getMessageOrBuilder();
        } else {
          return formula_ == null ?
              com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.getDefaultInstance() : formula_;
        }
      }
      /**
       * <code>.formulas.PBFormulas formula = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilder<
          com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas, com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.Builder, com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulasOrBuilder> 
          getFormulaFieldBuilder() {
        if (formulaBuilder_ == null) {
          formulaBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas, com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulas.Builder, com.booleworks.logicng.formulas.ProtoBufFormulas.PBFormulasOrBuilder>(
                  getFormula(),
                  getParentForChildren(),
                  isClean());
          formula_ = null;
        }
        return formulaBuilder_;
      }

      private java.lang.Object description_ = "";
      /**
       * <code>string description = 2;</code>
       * @return The description.
       */
      public java.lang.String getDescription() {
        java.lang.Object ref = description_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          description_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>string description = 2;</code>
       * @return The bytes for description.
       */
      public com.google.protobuf.ByteString
          getDescriptionBytes() {
        java.lang.Object ref = description_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          description_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string description = 2;</code>
       * @param value The description to set.
       * @return This builder for chaining.
       */
      public Builder setDescription(
          java.lang.String value) {
        if (value == null) { throw new NullPointerException(); }
        description_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }
      /**
       * <code>string description = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearDescription() {
        description_ = getDefaultInstance().getDescription();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      /**
       * <code>string description = 2;</code>
       * @param value The bytes for description to set.
       * @return This builder for chaining.
       */
      public Builder setDescriptionBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        description_ = value;
        bitField0_ |= 0x00000002;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:propositions.PBStandardProposition)
    }

    // @@protoc_insertion_point(class_scope:propositions.PBStandardProposition)
    private static final com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition();
    }

    public static com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<PBStandardProposition>
        PARSER = new com.google.protobuf.AbstractParser<PBStandardProposition>() {
      @java.lang.Override
      public PBStandardProposition parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        Builder builder = newBuilder();
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (com.google.protobuf.UninitializedMessageException e) {
          throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(e)
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };

    public static com.google.protobuf.Parser<PBStandardProposition> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<PBStandardProposition> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public com.booleworks.logicng.propositions.ProtoBufPropositions.PBStandardProposition getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_propositions_PBStandardProposition_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_propositions_PBStandardProposition_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022propositions.proto\022\014propositions\032\016form" +
      "ulas.proto\"S\n\025PBStandardProposition\022%\n\007f" +
      "ormula\030\001 \001(\0132\024.formulas.PBFormulas\022\023\n\013de" +
      "scription\030\002 \001(\tB;\n#com.booleworks.logicn" +
      "g.propositionsB\024ProtoBufPropositionsb\006pr" +
      "oto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.booleworks.logicng.formulas.ProtoBufFormulas.getDescriptor(),
        });
    internal_static_propositions_PBStandardProposition_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_propositions_PBStandardProposition_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_propositions_PBStandardProposition_descriptor,
        new java.lang.String[] { "Formula", "Description", });
    descriptor.resolveAllFeaturesImmutable();
    com.booleworks.logicng.formulas.ProtoBufFormulas.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
