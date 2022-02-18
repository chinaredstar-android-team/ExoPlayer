/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.media3.common;

import static java.lang.annotation.ElementType.TYPE_USE;

import android.os.Bundle;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.media3.common.util.BundleableUtil;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import com.google.common.base.Joiner;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a media format.
 *
 * <p>When building formats, populate all fields whose values are known and relevant to the type of
 * format being constructed. For information about different types of format, see ExoPlayer's <a
 * href="https://exoplayer.dev/supported-formats.html">Supported formats page</a>.
 *
 * <h2>Fields commonly relevant to all formats</h2>
 *
 * <ul>
 *   <li>{@link #id}
 *   <li>{@link #label}
 *   <li>{@link #language}
 *   <li>{@link #selectionFlags}
 *   <li>{@link #roleFlags}
 *   <li>{@link #averageBitrate}
 *   <li>{@link #peakBitrate}
 *   <li>{@link #codecs}
 *   <li>{@link #metadata}
 * </ul>
 *
 * <h2 id="container-formats">Fields relevant to container formats</h2>
 *
 * <ul>
 *   <li>{@link #containerMimeType}
 *   <li>If the container only contains a single media track, <a href="#sample-formats">fields
 *       relevant to sample formats</a> can are also be relevant and can be set to describe the
 *       sample format of that track.
 *   <li>If the container only contains one track of a given type (possibly alongside tracks of
 *       other types), then fields relevant to that track type can be set to describe the properties
 *       of the track. See the sections below for <a href="#video-formats">video</a>, <a
 *       href="#audio-formats">audio</a> and <a href="#text-formats">text</a> formats.
 * </ul>
 *
 * <h2 id="sample-formats">Fields relevant to sample formats</h2>
 *
 * <ul>
 *   <li>{@link #sampleMimeType}
 *   <li>{@link #maxInputSize}
 *   <li>{@link #initializationData}
 *   <li>{@link #drmInitData}
 *   <li>{@link #subsampleOffsetUs}
 *   <li>Fields relevant to the sample format's track type are also relevant. See the sections below
 *       for <a href="#video-formats">video</a>, <a href="#audio-formats">audio</a> and <a
 *       href="#text-formats">text</a> formats.
 * </ul>
 *
 * <h2 id="video-formats">Fields relevant to video formats</h2>
 *
 * <ul>
 *   <li>{@link #width}
 *   <li>{@link #height}
 *   <li>{@link #frameRate}
 *   <li>{@link #rotationDegrees}
 *   <li>{@link #pixelWidthHeightRatio}
 *   <li>{@link #projectionData}
 *   <li>{@link #stereoMode}
 *   <li>{@link #colorInfo}
 * </ul>
 *
 * <h2 id="audio-formats">Fields relevant to audio formats</h2>
 *
 * <ul>
 *   <li>{@link #channelCount}
 *   <li>{@link #sampleRate}
 *   <li>{@link #pcmEncoding}
 *   <li>{@link #encoderDelay}
 *   <li>{@link #encoderPadding}
 * </ul>
 *
 * <h2 id="text-formats">Fields relevant to text formats</h2>
 *
 * <ul>
 *   <li>{@link #accessibilityChannel}
 * </ul>
 */
public final class Format implements Bundleable {

  /**
   * Builds {@link Format} instances.
   *
   * <p>Use Format#buildUpon() to obtain a builder representing an existing {@link Format}.
   *
   * <p>When building formats, populate all fields whose values are known and relevant to the type
   * of format being constructed. See the {@link Format} Javadoc for information about which fields
   * should be set for different types of format.
   */
  @UnstableApi
  public static final class Builder {

    @Nullable private String id;
    @Nullable private String label;
    @Nullable private String language;
    private @C.SelectionFlags int selectionFlags;
    private @C.RoleFlags int roleFlags;
    private int averageBitrate;
    private int peakBitrate;
    @Nullable private String codecs;
    @Nullable private Metadata metadata;

    // Container specific.

    @Nullable private String containerMimeType;

    // Sample specific.

    @Nullable private String sampleMimeType;
    private int maxInputSize;
    @Nullable private List<byte[]> initializationData;
    @Nullable private DrmInitData drmInitData;
    private long subsampleOffsetUs;

    // Video specific.

    private int width;
    private int height;
    private float frameRate;
    private int rotationDegrees;
    private float pixelWidthHeightRatio;
    @Nullable private byte[] projectionData;
    private @C.StereoMode int stereoMode;
    @Nullable private ColorInfo colorInfo;

    // Audio specific.

    private int channelCount;
    private int sampleRate;
    private @C.PcmEncoding int pcmEncoding;
    private int encoderDelay;
    private int encoderPadding;

    // Text specific.

    private int accessibilityChannel;

    // Provided by the source.

    private @C.CryptoType int cryptoType;

    /** Creates a new instance with default values. */
    public Builder() {
      averageBitrate = NO_VALUE;
      peakBitrate = NO_VALUE;
      // Sample specific.
      maxInputSize = NO_VALUE;
      subsampleOffsetUs = OFFSET_SAMPLE_RELATIVE;
      // Video specific.
      width = NO_VALUE;
      height = NO_VALUE;
      frameRate = NO_VALUE;
      pixelWidthHeightRatio = 1.0f;
      stereoMode = NO_VALUE;
      // Audio specific.
      channelCount = NO_VALUE;
      sampleRate = NO_VALUE;
      pcmEncoding = NO_VALUE;
      // Text specific.
      accessibilityChannel = NO_VALUE;
      // Provided by the source.
      cryptoType = C.CRYPTO_TYPE_NONE;
    }

    /**
     * Creates a new instance to build upon the provided {@link Format}.
     *
     * @param format The {@link Format} to build upon.
     */
    private Builder(Format format) {
      this.id = format.id;
      this.label = format.label;
      this.language = format.language;
      this.selectionFlags = format.selectionFlags;
      this.roleFlags = format.roleFlags;
      this.averageBitrate = format.averageBitrate;
      this.peakBitrate = format.peakBitrate;
      this.codecs = format.codecs;
      this.metadata = format.metadata;
      // Container specific.
      this.containerMimeType = format.containerMimeType;
      // Sample specific.
      this.sampleMimeType = format.sampleMimeType;
      this.maxInputSize = format.maxInputSize;
      this.initializationData = format.initializationData;
      this.drmInitData = format.drmInitData;
      this.subsampleOffsetUs = format.subsampleOffsetUs;
      // Video specific.
      this.width = format.width;
      this.height = format.height;
      this.frameRate = format.frameRate;
      this.rotationDegrees = format.rotationDegrees;
      this.pixelWidthHeightRatio = format.pixelWidthHeightRatio;
      this.projectionData = format.projectionData;
      this.stereoMode = format.stereoMode;
      this.colorInfo = format.colorInfo;
      // Audio specific.
      this.channelCount = format.channelCount;
      this.sampleRate = format.sampleRate;
      this.pcmEncoding = format.pcmEncoding;
      this.encoderDelay = format.encoderDelay;
      this.encoderPadding = format.encoderPadding;
      // Text specific.
      this.accessibilityChannel = format.accessibilityChannel;
      // Provided by the source.
      this.cryptoType = format.cryptoType;
    }

    /**
     * Sets {@link Format#id}. The default value is {@code null}.
     *
     * @param id The {@link Format#id}.
     * @return The builder.
     */
    public Builder setId(@Nullable String id) {
      this.id = id;
      return this;
    }

    /**
     * Sets {@link Format#id} to {@link Integer#toString() Integer.toString(id)}. The default value
     * is {@code null}.
     *
     * @param id The {@link Format#id}.
     * @return The builder.
     */
    public Builder setId(int id) {
      this.id = Integer.toString(id);
      return this;
    }

    /**
     * Sets {@link Format#label}. The default value is {@code null}.
     *
     * @param label The {@link Format#label}.
     * @return The builder.
     */
    public Builder setLabel(@Nullable String label) {
      this.label = label;
      return this;
    }

    /**
     * Sets {@link Format#language}. The default value is {@code null}.
     *
     * @param language The {@link Format#language}.
     * @return The builder.
     */
    public Builder setLanguage(@Nullable String language) {
      this.language = language;
      return this;
    }

    /**
     * Sets {@link Format#selectionFlags}. The default value is 0.
     *
     * @param selectionFlags The {@link Format#selectionFlags}.
     * @return The builder.
     */
    public Builder setSelectionFlags(@C.SelectionFlags int selectionFlags) {
      this.selectionFlags = selectionFlags;
      return this;
    }

    /**
     * Sets {@link Format#roleFlags}. The default value is 0.
     *
     * @param roleFlags The {@link Format#roleFlags}.
     * @return The builder.
     */
    public Builder setRoleFlags(@C.RoleFlags int roleFlags) {
      this.roleFlags = roleFlags;
      return this;
    }

    /**
     * Sets {@link Format#averageBitrate}. The default value is {@link #NO_VALUE}.
     *
     * @param averageBitrate The {@link Format#averageBitrate}.
     * @return The builder.
     */
    public Builder setAverageBitrate(int averageBitrate) {
      this.averageBitrate = averageBitrate;
      return this;
    }

    /**
     * Sets {@link Format#peakBitrate}. The default value is {@link #NO_VALUE}.
     *
     * @param peakBitrate The {@link Format#peakBitrate}.
     * @return The builder.
     */
    public Builder setPeakBitrate(int peakBitrate) {
      this.peakBitrate = peakBitrate;
      return this;
    }

    /**
     * Sets {@link Format#codecs}. The default value is {@code null}.
     *
     * @param codecs The {@link Format#codecs}.
     * @return The builder.
     */
    public Builder setCodecs(@Nullable String codecs) {
      this.codecs = codecs;
      return this;
    }

    /**
     * Sets {@link Format#metadata}. The default value is {@code null}.
     *
     * @param metadata The {@link Format#metadata}.
     * @return The builder.
     */
    public Builder setMetadata(@Nullable Metadata metadata) {
      this.metadata = metadata;
      return this;
    }

    // Container specific.

    /**
     * Sets {@link Format#containerMimeType}. The default value is {@code null}.
     *
     * @param containerMimeType The {@link Format#containerMimeType}.
     * @return The builder.
     */
    public Builder setContainerMimeType(@Nullable String containerMimeType) {
      this.containerMimeType = containerMimeType;
      return this;
    }

    // Sample specific.

    /**
     * Sets {@link Format#sampleMimeType}. The default value is {@code null}.
     *
     * @param sampleMimeType {@link Format#sampleMimeType}.
     * @return The builder.
     */
    public Builder setSampleMimeType(@Nullable String sampleMimeType) {
      this.sampleMimeType = sampleMimeType;
      return this;
    }

    /**
     * Sets {@link Format#maxInputSize}. The default value is {@link #NO_VALUE}.
     *
     * @param maxInputSize The {@link Format#maxInputSize}.
     * @return The builder.
     */
    public Builder setMaxInputSize(int maxInputSize) {
      this.maxInputSize = maxInputSize;
      return this;
    }

    /**
     * Sets {@link Format#initializationData}. The default value is {@code null}.
     *
     * @param initializationData The {@link Format#initializationData}.
     * @return The builder.
     */
    public Builder setInitializationData(@Nullable List<byte[]> initializationData) {
      this.initializationData = initializationData;
      return this;
    }

    /**
     * Sets {@link Format#drmInitData}. The default value is {@code null}.
     *
     * @param drmInitData The {@link Format#drmInitData}.
     * @return The builder.
     */
    public Builder setDrmInitData(@Nullable DrmInitData drmInitData) {
      this.drmInitData = drmInitData;
      return this;
    }

    /**
     * Sets {@link Format#subsampleOffsetUs}. The default value is {@link #OFFSET_SAMPLE_RELATIVE}.
     *
     * @param subsampleOffsetUs The {@link Format#subsampleOffsetUs}.
     * @return The builder.
     */
    public Builder setSubsampleOffsetUs(long subsampleOffsetUs) {
      this.subsampleOffsetUs = subsampleOffsetUs;
      return this;
    }

    // Video specific.

    /**
     * Sets {@link Format#width}. The default value is {@link #NO_VALUE}.
     *
     * @param width The {@link Format#width}.
     * @return The builder.
     */
    public Builder setWidth(int width) {
      this.width = width;
      return this;
    }

    /**
     * Sets {@link Format#height}. The default value is {@link #NO_VALUE}.
     *
     * @param height The {@link Format#height}.
     * @return The builder.
     */
    public Builder setHeight(int height) {
      this.height = height;
      return this;
    }

    /**
     * Sets {@link Format#frameRate}. The default value is {@link #NO_VALUE}.
     *
     * @param frameRate The {@link Format#frameRate}.
     * @return The builder.
     */
    public Builder setFrameRate(float frameRate) {
      this.frameRate = frameRate;
      return this;
    }

    /**
     * Sets {@link Format#rotationDegrees}. The default value is 0.
     *
     * @param rotationDegrees The {@link Format#rotationDegrees}.
     * @return The builder.
     */
    public Builder setRotationDegrees(int rotationDegrees) {
      this.rotationDegrees = rotationDegrees;
      return this;
    }

    /**
     * Sets {@link Format#pixelWidthHeightRatio}. The default value is 1.0f.
     *
     * @param pixelWidthHeightRatio The {@link Format#pixelWidthHeightRatio}.
     * @return The builder.
     */
    public Builder setPixelWidthHeightRatio(float pixelWidthHeightRatio) {
      this.pixelWidthHeightRatio = pixelWidthHeightRatio;
      return this;
    }

    /**
     * Sets {@link Format#projectionData}. The default value is {@code null}.
     *
     * @param projectionData The {@link Format#projectionData}.
     * @return The builder.
     */
    public Builder setProjectionData(@Nullable byte[] projectionData) {
      this.projectionData = projectionData;
      return this;
    }

    /**
     * Sets {@link Format#stereoMode}. The default value is {@link #NO_VALUE}.
     *
     * @param stereoMode The {@link Format#stereoMode}.
     * @return The builder.
     */
    public Builder setStereoMode(@C.StereoMode int stereoMode) {
      this.stereoMode = stereoMode;
      return this;
    }

    /**
     * Sets {@link Format#colorInfo}. The default value is {@code null}.
     *
     * @param colorInfo The {@link Format#colorInfo}.
     * @return The builder.
     */
    public Builder setColorInfo(@Nullable ColorInfo colorInfo) {
      this.colorInfo = colorInfo;
      return this;
    }

    // Audio specific.

    /**
     * Sets {@link Format#channelCount}. The default value is {@link #NO_VALUE}.
     *
     * @param channelCount The {@link Format#channelCount}.
     * @return The builder.
     */
    public Builder setChannelCount(int channelCount) {
      this.channelCount = channelCount;
      return this;
    }

    /**
     * Sets {@link Format#sampleRate}. The default value is {@link #NO_VALUE}.
     *
     * @param sampleRate The {@link Format#sampleRate}.
     * @return The builder.
     */
    public Builder setSampleRate(int sampleRate) {
      this.sampleRate = sampleRate;
      return this;
    }

    /**
     * Sets {@link Format#pcmEncoding}. The default value is {@link #NO_VALUE}.
     *
     * @param pcmEncoding The {@link Format#pcmEncoding}.
     * @return The builder.
     */
    public Builder setPcmEncoding(@C.PcmEncoding int pcmEncoding) {
      this.pcmEncoding = pcmEncoding;
      return this;
    }

    /**
     * Sets {@link Format#encoderDelay}. The default value is 0.
     *
     * @param encoderDelay The {@link Format#encoderDelay}.
     * @return The builder.
     */
    public Builder setEncoderDelay(int encoderDelay) {
      this.encoderDelay = encoderDelay;
      return this;
    }

    /**
     * Sets {@link Format#encoderPadding}. The default value is 0.
     *
     * @param encoderPadding The {@link Format#encoderPadding}.
     * @return The builder.
     */
    public Builder setEncoderPadding(int encoderPadding) {
      this.encoderPadding = encoderPadding;
      return this;
    }

    // Text specific.

    /**
     * Sets {@link Format#accessibilityChannel}. The default value is {@link #NO_VALUE}.
     *
     * @param accessibilityChannel The {@link Format#accessibilityChannel}.
     * @return The builder.
     */
    public Builder setAccessibilityChannel(int accessibilityChannel) {
      this.accessibilityChannel = accessibilityChannel;
      return this;
    }

    // Provided by source.

    /**
     * Sets {@link Format#cryptoType}. The default value is {@link C#CRYPTO_TYPE_NONE}.
     *
     * @param cryptoType The {@link C.CryptoType}.
     * @return The builder.
     */
    public Builder setCryptoType(@C.CryptoType int cryptoType) {
      this.cryptoType = cryptoType;
      return this;
    }

    // Build.

    public Format build() {
      return new Format(/* builder= */ this);
    }
  }

  /** A value for various fields to indicate that the field's value is unknown or not applicable. */
  public static final int NO_VALUE = -1;

  /**
   * A value for {@link #subsampleOffsetUs} to indicate that subsample timestamps are relative to
   * the timestamps of their parent samples.
   */
  @UnstableApi public static final long OFFSET_SAMPLE_RELATIVE = Long.MAX_VALUE;

  private static final Format DEFAULT = new Builder().build();

  /** An identifier for the format, or null if unknown or not applicable. */
  @Nullable public final String id;
  /** The human readable label, or null if unknown or not applicable. */
  @Nullable public final String label;
  /** The language as an IETF BCP 47 conformant tag, or null if unknown or not applicable. */
  @Nullable public final String language;
  /** Track selection flags. */
  public final @C.SelectionFlags int selectionFlags;
  /** Track role flags. */
  public final @C.RoleFlags int roleFlags;
  /**
   * The average bitrate in bits per second, or {@link #NO_VALUE} if unknown or not applicable. The
   * way in which this field is populated depends on the type of media to which the format
   * corresponds:
   *
   * <ul>
   *   <li>DASH representations: Always {@link Format#NO_VALUE}.
   *   <li>HLS variants: The {@code AVERAGE-BANDWIDTH} attribute defined on the corresponding {@code
   *       EXT-X-STREAM-INF} tag in the multivariant playlist, or {@link Format#NO_VALUE} if not
   *       present.
   *   <li>SmoothStreaming track elements: The {@code Bitrate} attribute defined on the
   *       corresponding {@code TrackElement} in the manifest, or {@link Format#NO_VALUE} if not
   *       present.
   *   <li>Progressive container formats: Often {@link Format#NO_VALUE}, but may be populated with
   *       the average bitrate of the container if known.
   *   <li>Sample formats: Often {@link Format#NO_VALUE}, but may be populated with the average
   *       bitrate of the stream of samples with type {@link #sampleMimeType} if known. Note that if
   *       {@link #sampleMimeType} is a compressed format (e.g., {@link MimeTypes#AUDIO_AAC}), then
   *       this bitrate is for the stream of still compressed samples.
   * </ul>
   */
  @UnstableApi public final int averageBitrate;
  /**
   * The peak bitrate in bits per second, or {@link #NO_VALUE} if unknown or not applicable. The way
   * in which this field is populated depends on the type of media to which the format corresponds:
   *
   * <ul>
   *   <li>DASH representations: The {@code @bandwidth} attribute of the corresponding {@code
   *       Representation} element in the manifest.
   *   <li>HLS variants: The {@code BANDWIDTH} attribute defined on the corresponding {@code
   *       EXT-X-STREAM-INF} tag.
   *   <li>SmoothStreaming track elements: Always {@link Format#NO_VALUE}.
   *   <li>Progressive container formats: Often {@link Format#NO_VALUE}, but may be populated with
   *       the peak bitrate of the container if known.
   *   <li>Sample formats: Often {@link Format#NO_VALUE}, but may be populated with the peak bitrate
   *       of the stream of samples with type {@link #sampleMimeType} if known. Note that if {@link
   *       #sampleMimeType} is a compressed format (e.g., {@link MimeTypes#AUDIO_AAC}), then this
   *       bitrate is for the stream of still compressed samples.
   * </ul>
   */
  @UnstableApi public final int peakBitrate;
  /**
   * The bitrate in bits per second. This is the peak bitrate if known, or else the average bitrate
   * if known, or else {@link Format#NO_VALUE}. Equivalent to: {@code peakBitrate != NO_VALUE ?
   * peakBitrate : averageBitrate}.
   */
  @UnstableApi public final int bitrate;
  /** Codecs of the format as described in RFC 6381, or null if unknown or not applicable. */
  @Nullable public final String codecs;
  /** Metadata, or null if unknown or not applicable. */
  @UnstableApi @Nullable public final Metadata metadata;

  // Container specific.

  /** The mime type of the container, or null if unknown or not applicable. */
  @Nullable public final String containerMimeType;

  // Sample specific.

  /** The sample mime type, or null if unknown or not applicable. */
  @Nullable public final String sampleMimeType;
  /**
   * The maximum size of a buffer of data (typically one sample), or {@link #NO_VALUE} if unknown or
   * not applicable.
   */
  @UnstableApi public final int maxInputSize;
  /**
   * Initialization data that must be provided to the decoder. Will not be null, but may be empty if
   * initialization data is not required.
   */
  @UnstableApi public final List<byte[]> initializationData;
  /** DRM initialization data if the stream is protected, or null otherwise. */
  @UnstableApi @Nullable public final DrmInitData drmInitData;

  /**
   * For samples that contain subsamples, this is an offset that should be added to subsample
   * timestamps. A value of {@link #OFFSET_SAMPLE_RELATIVE} indicates that subsample timestamps are
   * relative to the timestamps of their parent samples.
   */
  @UnstableApi public final long subsampleOffsetUs;

  // Video specific.

  /** The width of the video in pixels, or {@link #NO_VALUE} if unknown or not applicable. */
  public final int width;
  /** The height of the video in pixels, or {@link #NO_VALUE} if unknown or not applicable. */
  public final int height;
  /** The frame rate in frames per second, or {@link #NO_VALUE} if unknown or not applicable. */
  public final float frameRate;
  /**
   * The clockwise rotation that should be applied to the video for it to be rendered in the correct
   * orientation, or 0 if unknown or not applicable. Only 0, 90, 180 and 270 are supported.
   */
  @UnstableApi public final int rotationDegrees;
  /** The width to height ratio of pixels in the video, or 1.0 if unknown or not applicable. */
  public final float pixelWidthHeightRatio;
  /** The projection data for 360/VR video, or null if not applicable. */
  @UnstableApi @Nullable public final byte[] projectionData;
  /**
   * The stereo layout for 360/3D/VR video, or {@link #NO_VALUE} if not applicable. Valid stereo
   * modes are {@link C#STEREO_MODE_MONO}, {@link C#STEREO_MODE_TOP_BOTTOM}, {@link
   * C#STEREO_MODE_LEFT_RIGHT}, {@link C#STEREO_MODE_STEREO_MESH}.
   */
  @UnstableApi public final @C.StereoMode int stereoMode;
  /** The color metadata associated with the video, or null if not applicable. */
  @UnstableApi @Nullable public final ColorInfo colorInfo;

  // Audio specific.

  /** The number of audio channels, or {@link #NO_VALUE} if unknown or not applicable. */
  public final int channelCount;
  /** The audio sampling rate in Hz, or {@link #NO_VALUE} if unknown or not applicable. */
  public final int sampleRate;
  /** The {@link C.PcmEncoding} for PCM audio. Set to {@link #NO_VALUE} for other media types. */
  @UnstableApi public final @C.PcmEncoding int pcmEncoding;
  /**
   * The number of frames to trim from the start of the decoded audio stream, or 0 if not
   * applicable.
   */
  @UnstableApi public final int encoderDelay;
  /**
   * The number of frames to trim from the end of the decoded audio stream, or 0 if not applicable.
   */
  @UnstableApi public final int encoderPadding;

  // Text specific.

  /** The Accessibility channel, or {@link #NO_VALUE} if not known or applicable. */
  @UnstableApi public final int accessibilityChannel;

  // Provided by source.

  /**
   * The type of crypto that must be used to decode samples associated with this format, or {@link
   * C#CRYPTO_TYPE_NONE} if the content is not encrypted. Cannot be {@link C#CRYPTO_TYPE_NONE} if
   * {@link #drmInitData} is non-null, but may be {@link C#CRYPTO_TYPE_UNSUPPORTED} to indicate that
   * the samples are encrypted using an unsupported crypto type.
   */
  @UnstableApi public final @C.CryptoType int cryptoType;

  // Lazily initialized hashcode.
  private int hashCode;

  // Video.

  /**
   * @deprecated Use {@link Format.Builder}.
   */
  @UnstableApi
  @Deprecated
  public static Format createVideoSampleFormat(
      @Nullable String id,
      @Nullable String sampleMimeType,
      @Nullable String codecs,
      int bitrate,
      int maxInputSize,
      int width,
      int height,
      float frameRate,
      @Nullable List<byte[]> initializationData,
      @Nullable DrmInitData drmInitData) {
    return new Builder()
        .setId(id)
        .setAverageBitrate(bitrate)
        .setPeakBitrate(bitrate)
        .setCodecs(codecs)
        .setSampleMimeType(sampleMimeType)
        .setMaxInputSize(maxInputSize)
        .setInitializationData(initializationData)
        .setDrmInitData(drmInitData)
        .setWidth(width)
        .setHeight(height)
        .setFrameRate(frameRate)
        .build();
  }

  /**
   * @deprecated Use {@link Format.Builder}.
   */
  @UnstableApi
  @Deprecated
  public static Format createVideoSampleFormat(
      @Nullable String id,
      @Nullable String sampleMimeType,
      @Nullable String codecs,
      int bitrate,
      int maxInputSize,
      int width,
      int height,
      float frameRate,
      @Nullable List<byte[]> initializationData,
      int rotationDegrees,
      float pixelWidthHeightRatio,
      @Nullable DrmInitData drmInitData) {
    return new Builder()
        .setId(id)
        .setAverageBitrate(bitrate)
        .setPeakBitrate(bitrate)
        .setCodecs(codecs)
        .setSampleMimeType(sampleMimeType)
        .setMaxInputSize(maxInputSize)
        .setInitializationData(initializationData)
        .setDrmInitData(drmInitData)
        .setWidth(width)
        .setHeight(height)
        .setFrameRate(frameRate)
        .setRotationDegrees(rotationDegrees)
        .setPixelWidthHeightRatio(pixelWidthHeightRatio)
        .build();
  }

  // Audio.

  /**
   * @deprecated Use {@link Format.Builder}.
   */
  @UnstableApi
  @Deprecated
  public static Format createAudioSampleFormat(
      @Nullable String id,
      @Nullable String sampleMimeType,
      @Nullable String codecs,
      int bitrate,
      int maxInputSize,
      int channelCount,
      int sampleRate,
      @Nullable List<byte[]> initializationData,
      @Nullable DrmInitData drmInitData,
      @C.SelectionFlags int selectionFlags,
      @Nullable String language) {
    return new Builder()
        .setId(id)
        .setLanguage(language)
        .setSelectionFlags(selectionFlags)
        .setAverageBitrate(bitrate)
        .setPeakBitrate(bitrate)
        .setCodecs(codecs)
        .setSampleMimeType(sampleMimeType)
        .setMaxInputSize(maxInputSize)
        .setInitializationData(initializationData)
        .setDrmInitData(drmInitData)
        .setChannelCount(channelCount)
        .setSampleRate(sampleRate)
        .build();
  }

  /**
   * @deprecated Use {@link Format.Builder}.
   */
  @UnstableApi
  @Deprecated
  public static Format createAudioSampleFormat(
      @Nullable String id,
      @Nullable String sampleMimeType,
      @Nullable String codecs,
      int bitrate,
      int maxInputSize,
      int channelCount,
      int sampleRate,
      @C.PcmEncoding int pcmEncoding,
      @Nullable List<byte[]> initializationData,
      @Nullable DrmInitData drmInitData,
      @C.SelectionFlags int selectionFlags,
      @Nullable String language) {
    return new Builder()
        .setId(id)
        .setLanguage(language)
        .setSelectionFlags(selectionFlags)
        .setAverageBitrate(bitrate)
        .setPeakBitrate(bitrate)
        .setCodecs(codecs)
        .setSampleMimeType(sampleMimeType)
        .setMaxInputSize(maxInputSize)
        .setInitializationData(initializationData)
        .setDrmInitData(drmInitData)
        .setChannelCount(channelCount)
        .setSampleRate(sampleRate)
        .setPcmEncoding(pcmEncoding)
        .build();
  }

  // Generic.

  /**
   * @deprecated Use {@link Format.Builder}.
   */
  @UnstableApi
  @Deprecated
  public static Format createContainerFormat(
      @Nullable String id,
      @Nullable String label,
      @Nullable String containerMimeType,
      @Nullable String sampleMimeType,
      @Nullable String codecs,
      int bitrate,
      @C.SelectionFlags int selectionFlags,
      @C.RoleFlags int roleFlags,
      @Nullable String language) {
    return new Builder()
        .setId(id)
        .setLabel(label)
        .setLanguage(language)
        .setSelectionFlags(selectionFlags)
        .setRoleFlags(roleFlags)
        .setAverageBitrate(bitrate)
        .setPeakBitrate(bitrate)
        .setCodecs(codecs)
        .setContainerMimeType(containerMimeType)
        .setSampleMimeType(sampleMimeType)
        .build();
  }

  /**
   * @deprecated Use {@link Format.Builder}.
   */
  @UnstableApi
  @Deprecated
  public static Format createSampleFormat(@Nullable String id, @Nullable String sampleMimeType) {
    return new Builder().setId(id).setSampleMimeType(sampleMimeType).build();
  }

  private Format(Builder builder) {
    id = builder.id;
    label = builder.label;
    language = Util.normalizeLanguageCode(builder.language);
    selectionFlags = builder.selectionFlags;
    roleFlags = builder.roleFlags;
    averageBitrate = builder.averageBitrate;
    peakBitrate = builder.peakBitrate;
    bitrate = peakBitrate != NO_VALUE ? peakBitrate : averageBitrate;
    codecs = builder.codecs;
    metadata = builder.metadata;
    // Container specific.
    containerMimeType = builder.containerMimeType;
    // Sample specific.
    sampleMimeType = builder.sampleMimeType;
    maxInputSize = builder.maxInputSize;
    initializationData =
        builder.initializationData == null ? Collections.emptyList() : builder.initializationData;
    drmInitData = builder.drmInitData;
    subsampleOffsetUs = builder.subsampleOffsetUs;
    // Video specific.
    width = builder.width;
    height = builder.height;
    frameRate = builder.frameRate;
    rotationDegrees = builder.rotationDegrees == NO_VALUE ? 0 : builder.rotationDegrees;
    pixelWidthHeightRatio =
        builder.pixelWidthHeightRatio == NO_VALUE ? 1 : builder.pixelWidthHeightRatio;
    projectionData = builder.projectionData;
    stereoMode = builder.stereoMode;
    colorInfo = builder.colorInfo;
    // Audio specific.
    channelCount = builder.channelCount;
    sampleRate = builder.sampleRate;
    pcmEncoding = builder.pcmEncoding;
    encoderDelay = builder.encoderDelay == NO_VALUE ? 0 : builder.encoderDelay;
    encoderPadding = builder.encoderPadding == NO_VALUE ? 0 : builder.encoderPadding;
    // Text specific.
    accessibilityChannel = builder.accessibilityChannel;
    // Provided by source.
    if (builder.cryptoType == C.CRYPTO_TYPE_NONE && drmInitData != null) {
      // Encrypted content cannot use CRYPTO_TYPE_NONE.
      cryptoType = C.CRYPTO_TYPE_UNSUPPORTED;
    } else {
      cryptoType = builder.cryptoType;
    }
  }

  /** Returns a {@link Format.Builder} initialized with the values of this instance. */
  @UnstableApi
  public Builder buildUpon() {
    return new Builder(this);
  }

  /**
   * @deprecated Use {@link #buildUpon()} and {@link Builder#setMaxInputSize(int)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithMaxInputSize(int maxInputSize) {
    return buildUpon().setMaxInputSize(maxInputSize).build();
  }

  /**
   * @deprecated Use {@link #buildUpon()} and {@link Builder#setSubsampleOffsetUs(long)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithSubsampleOffsetUs(long subsampleOffsetUs) {
    return buildUpon().setSubsampleOffsetUs(subsampleOffsetUs).build();
  }

  /**
   * @deprecated Use {@link #buildUpon()} and {@link Builder#setLabel(String)} .
   */
  @UnstableApi
  @Deprecated
  public Format copyWithLabel(@Nullable String label) {
    return buildUpon().setLabel(label).build();
  }

  /**
   * @deprecated Use {@link #withManifestFormatInfo(Format)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithManifestFormatInfo(Format manifestFormat) {
    return withManifestFormatInfo(manifestFormat);
  }

  @UnstableApi
  @SuppressWarnings("ReferenceEquality")
  public Format withManifestFormatInfo(Format manifestFormat) {
    if (this == manifestFormat) {
      // No need to copy from ourselves.
      return this;
    }

    @C.TrackType int trackType = MimeTypes.getTrackType(sampleMimeType);

    // Use manifest value only.
    @Nullable String id = manifestFormat.id;

    // Prefer manifest values, but fill in from sample format if missing.
    @Nullable String label = manifestFormat.label != null ? manifestFormat.label : this.label;
    @Nullable String language = this.language;
    if ((trackType == C.TRACK_TYPE_TEXT || trackType == C.TRACK_TYPE_AUDIO)
        && manifestFormat.language != null) {
      language = manifestFormat.language;
    }

    // Prefer sample format values, but fill in from manifest if missing.
    int averageBitrate =
        this.averageBitrate == NO_VALUE ? manifestFormat.averageBitrate : this.averageBitrate;
    int peakBitrate = this.peakBitrate == NO_VALUE ? manifestFormat.peakBitrate : this.peakBitrate;
    @Nullable String codecs = this.codecs;
    if (codecs == null) {
      // The manifest format may be muxed, so filter only codecs of this format's type. If we still
      // have more than one codec then we're unable to uniquely identify which codec to fill in.
      @Nullable String codecsOfType = Util.getCodecsOfType(manifestFormat.codecs, trackType);
      if (Util.splitCodecs(codecsOfType).length == 1) {
        codecs = codecsOfType;
      }
    }

    @Nullable
    Metadata metadata =
        this.metadata == null
            ? manifestFormat.metadata
            : this.metadata.copyWithAppendedEntriesFrom(manifestFormat.metadata);

    float frameRate = this.frameRate;
    if (frameRate == NO_VALUE && trackType == C.TRACK_TYPE_VIDEO) {
      frameRate = manifestFormat.frameRate;
    }

    // Merge manifest and sample format values.
    @C.SelectionFlags int selectionFlags = this.selectionFlags | manifestFormat.selectionFlags;
    @C.RoleFlags int roleFlags = this.roleFlags | manifestFormat.roleFlags;
    @Nullable
    DrmInitData drmInitData =
        DrmInitData.createSessionCreationData(manifestFormat.drmInitData, this.drmInitData);

    return buildUpon()
        .setId(id)
        .setLabel(label)
        .setLanguage(language)
        .setSelectionFlags(selectionFlags)
        .setRoleFlags(roleFlags)
        .setAverageBitrate(averageBitrate)
        .setPeakBitrate(peakBitrate)
        .setCodecs(codecs)
        .setMetadata(metadata)
        .setDrmInitData(drmInitData)
        .setFrameRate(frameRate)
        .build();
  }

  /**
   * @deprecated Use {@link #buildUpon()}, {@link Builder#setEncoderDelay(int)} and {@link
   *     Builder#setEncoderPadding(int)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithGaplessInfo(int encoderDelay, int encoderPadding) {
    return buildUpon().setEncoderDelay(encoderDelay).setEncoderPadding(encoderPadding).build();
  }

  /**
   * @deprecated Use {@link #buildUpon()} and {@link Builder#setFrameRate(float)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithFrameRate(float frameRate) {
    return buildUpon().setFrameRate(frameRate).build();
  }

  /**
   * @deprecated Use {@link #buildUpon()} and {@link Builder#setDrmInitData(DrmInitData)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithDrmInitData(@Nullable DrmInitData drmInitData) {
    return buildUpon().setDrmInitData(drmInitData).build();
  }

  /**
   * @deprecated Use {@link #buildUpon()} and {@link Builder#setMetadata(Metadata)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithMetadata(@Nullable Metadata metadata) {
    return buildUpon().setMetadata(metadata).build();
  }

  /**
   * @deprecated Use {@link #buildUpon()} and {@link Builder#setAverageBitrate(int)} and {@link
   *     Builder#setPeakBitrate(int)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithBitrate(int bitrate) {
    return buildUpon().setAverageBitrate(bitrate).setPeakBitrate(bitrate).build();
  }

  /**
   * @deprecated Use {@link #buildUpon()}, {@link Builder#setWidth(int)} and {@link
   *     Builder#setHeight(int)}.
   */
  @UnstableApi
  @Deprecated
  public Format copyWithVideoSize(int width, int height) {
    return buildUpon().setWidth(width).setHeight(height).build();
  }

  /** Returns a copy of this format with the specified {@link #cryptoType}. */
  @UnstableApi
  public Format copyWithCryptoType(@C.CryptoType int cryptoType) {
    return buildUpon().setCryptoType(cryptoType).build();
  }

  /**
   * Returns the number of pixels if this is a video format whose {@link #width} and {@link #height}
   * are known, or {@link #NO_VALUE} otherwise
   */
  @UnstableApi
  public int getPixelCount() {
    return width == NO_VALUE || height == NO_VALUE ? NO_VALUE : (width * height);
  }

  @Override
  public String toString() {
    return "Format("
        + id
        + ", "
        + label
        + ", "
        + containerMimeType
        + ", "
        + sampleMimeType
        + ", "
        + codecs
        + ", "
        + bitrate
        + ", "
        + language
        + ", ["
        + width
        + ", "
        + height
        + ", "
        + frameRate
        + "]"
        + ", ["
        + channelCount
        + ", "
        + sampleRate
        + "])";
  }

  @Override
  public int hashCode() {
    if (hashCode == 0) {
      // Some fields for which hashing is expensive are deliberately omitted.
      int result = 17;
      result = 31 * result + (id == null ? 0 : id.hashCode());
      result = 31 * result + (label != null ? label.hashCode() : 0);
      result = 31 * result + (language == null ? 0 : language.hashCode());
      result = 31 * result + selectionFlags;
      result = 31 * result + roleFlags;
      result = 31 * result + averageBitrate;
      result = 31 * result + peakBitrate;
      result = 31 * result + (codecs == null ? 0 : codecs.hashCode());
      result = 31 * result + (metadata == null ? 0 : metadata.hashCode());
      // Container specific.
      result = 31 * result + (containerMimeType == null ? 0 : containerMimeType.hashCode());
      // Sample specific.
      result = 31 * result + (sampleMimeType == null ? 0 : sampleMimeType.hashCode());
      result = 31 * result + maxInputSize;
      // [Omitted] initializationData.
      // [Omitted] drmInitData.
      result = 31 * result + (int) subsampleOffsetUs;
      // Video specific.
      result = 31 * result + width;
      result = 31 * result + height;
      result = 31 * result + Float.floatToIntBits(frameRate);
      result = 31 * result + rotationDegrees;
      result = 31 * result + Float.floatToIntBits(pixelWidthHeightRatio);
      // [Omitted] projectionData.
      result = 31 * result + stereoMode;
      // [Omitted] colorInfo.
      // Audio specific.
      result = 31 * result + channelCount;
      result = 31 * result + sampleRate;
      result = 31 * result + pcmEncoding;
      result = 31 * result + encoderDelay;
      result = 31 * result + encoderPadding;
      // Text specific.
      result = 31 * result + accessibilityChannel;
      // Provided by the source.
      result = 31 * result + cryptoType;
      hashCode = result;
    }
    return hashCode;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Format other = (Format) obj;
    if (hashCode != 0 && other.hashCode != 0 && hashCode != other.hashCode) {
      return false;
    }
    // Field equality checks ordered by type, with the cheapest checks first.
    return selectionFlags == other.selectionFlags
        && roleFlags == other.roleFlags
        && averageBitrate == other.averageBitrate
        && peakBitrate == other.peakBitrate
        && maxInputSize == other.maxInputSize
        && subsampleOffsetUs == other.subsampleOffsetUs
        && width == other.width
        && height == other.height
        && rotationDegrees == other.rotationDegrees
        && stereoMode == other.stereoMode
        && channelCount == other.channelCount
        && sampleRate == other.sampleRate
        && pcmEncoding == other.pcmEncoding
        && encoderDelay == other.encoderDelay
        && encoderPadding == other.encoderPadding
        && accessibilityChannel == other.accessibilityChannel
        && cryptoType == other.cryptoType
        && Float.compare(frameRate, other.frameRate) == 0
        && Float.compare(pixelWidthHeightRatio, other.pixelWidthHeightRatio) == 0
        && Util.areEqual(id, other.id)
        && Util.areEqual(label, other.label)
        && Util.areEqual(codecs, other.codecs)
        && Util.areEqual(containerMimeType, other.containerMimeType)
        && Util.areEqual(sampleMimeType, other.sampleMimeType)
        && Util.areEqual(language, other.language)
        && Arrays.equals(projectionData, other.projectionData)
        && Util.areEqual(metadata, other.metadata)
        && Util.areEqual(colorInfo, other.colorInfo)
        && Util.areEqual(drmInitData, other.drmInitData)
        && initializationDataEquals(other);
  }

  /**
   * Returns whether the {@link #initializationData}s belonging to this format and {@code other} are
   * equal.
   *
   * @param other The other format whose {@link #initializationData} is being compared.
   * @return Whether the {@link #initializationData}s belonging to this format and {@code other} are
   *     equal.
   */
  @UnstableApi
  public boolean initializationDataEquals(Format other) {
    if (initializationData.size() != other.initializationData.size()) {
      return false;
    }
    for (int i = 0; i < initializationData.size(); i++) {
      if (!Arrays.equals(initializationData.get(i), other.initializationData.get(i))) {
        return false;
      }
    }
    return true;
  }

  // Utility methods

  /** Returns a prettier {@link String} than {@link #toString()}, intended for logging. */
  @UnstableApi
  public static String toLogString(@Nullable Format format) {
    if (format == null) {
      return "null";
    }
    StringBuilder builder = new StringBuilder();
    builder.append("id=").append(format.id).append(", mimeType=").append(format.sampleMimeType);
    if (format.bitrate != NO_VALUE) {
      builder.append(", bitrate=").append(format.bitrate);
    }
    if (format.codecs != null) {
      builder.append(", codecs=").append(format.codecs);
    }
    if (format.drmInitData != null) {
      Set<String> schemes = new LinkedHashSet<>();
      for (int i = 0; i < format.drmInitData.schemeDataCount; i++) {
        UUID schemeUuid = format.drmInitData.get(i).uuid;
        if (schemeUuid.equals(C.COMMON_PSSH_UUID)) {
          schemes.add("cenc");
        } else if (schemeUuid.equals(C.CLEARKEY_UUID)) {
          schemes.add("clearkey");
        } else if (schemeUuid.equals(C.PLAYREADY_UUID)) {
          schemes.add("playready");
        } else if (schemeUuid.equals(C.WIDEVINE_UUID)) {
          schemes.add("widevine");
        } else if (schemeUuid.equals(C.UUID_NIL)) {
          schemes.add("universal");
        } else {
          schemes.add("unknown (" + schemeUuid + ")");
        }
      }
      builder.append(", drm=[");
      Joiner.on(',').appendTo(builder, schemes);
      builder.append(']');
    }
    if (format.width != NO_VALUE && format.height != NO_VALUE) {
      builder.append(", res=").append(format.width).append("x").append(format.height);
    }
    if (format.frameRate != NO_VALUE) {
      builder.append(", fps=").append(format.frameRate);
    }
    if (format.channelCount != NO_VALUE) {
      builder.append(", channels=").append(format.channelCount);
    }
    if (format.sampleRate != NO_VALUE) {
      builder.append(", sample_rate=").append(format.sampleRate);
    }
    if (format.language != null) {
      builder.append(", language=").append(format.language);
    }
    if (format.label != null) {
      builder.append(", label=").append(format.label);
    }
    if (format.selectionFlags != 0) {
      List<String> selectionFlags = new ArrayList<>();
      // LINT.IfChange(selection_flags)
      if ((format.selectionFlags & C.SELECTION_FLAG_AUTOSELECT) != 0) {
        selectionFlags.add("auto");
      }
      if ((format.selectionFlags & C.SELECTION_FLAG_DEFAULT) != 0) {
        selectionFlags.add("default");
      }
      if ((format.selectionFlags & C.SELECTION_FLAG_FORCED) != 0) {
        selectionFlags.add("forced");
      }
      builder.append(", selectionFlags=[");
      Joiner.on(',').appendTo(builder, selectionFlags);
      builder.append("]");
    }
    if (format.roleFlags != 0) {
      // LINT.IfChange(role_flags)
      List<String> roleFlags = new ArrayList<>();
      if ((format.roleFlags & C.ROLE_FLAG_MAIN) != 0) {
        roleFlags.add("main");
      }
      if ((format.roleFlags & C.ROLE_FLAG_ALTERNATE) != 0) {
        roleFlags.add("alt");
      }
      if ((format.roleFlags & C.ROLE_FLAG_SUPPLEMENTARY) != 0) {
        roleFlags.add("supplementary");
      }
      if ((format.roleFlags & C.ROLE_FLAG_COMMENTARY) != 0) {
        roleFlags.add("commentary");
      }
      if ((format.roleFlags & C.ROLE_FLAG_DUB) != 0) {
        roleFlags.add("dub");
      }
      if ((format.roleFlags & C.ROLE_FLAG_EMERGENCY) != 0) {
        roleFlags.add("emergency");
      }
      if ((format.roleFlags & C.ROLE_FLAG_CAPTION) != 0) {
        roleFlags.add("caption");
      }
      if ((format.roleFlags & C.ROLE_FLAG_SUBTITLE) != 0) {
        roleFlags.add("subtitle");
      }
      if ((format.roleFlags & C.ROLE_FLAG_SIGN) != 0) {
        roleFlags.add("sign");
      }
      if ((format.roleFlags & C.ROLE_FLAG_DESCRIBES_VIDEO) != 0) {
        roleFlags.add("describes-video");
      }
      if ((format.roleFlags & C.ROLE_FLAG_DESCRIBES_MUSIC_AND_SOUND) != 0) {
        roleFlags.add("describes-music");
      }
      if ((format.roleFlags & C.ROLE_FLAG_ENHANCED_DIALOG_INTELLIGIBILITY) != 0) {
        roleFlags.add("enhanced-intelligibility");
      }
      if ((format.roleFlags & C.ROLE_FLAG_TRANSCRIBES_DIALOG) != 0) {
        roleFlags.add("transcribes-dialog");
      }
      if ((format.roleFlags & C.ROLE_FLAG_EASY_TO_READ) != 0) {
        roleFlags.add("easy-read");
      }
      if ((format.roleFlags & C.ROLE_FLAG_TRICK_PLAY) != 0) {
        roleFlags.add("trick-play");
      }
      builder.append(", roleFlags=[");
      Joiner.on(',').appendTo(builder, roleFlags);
      builder.append("]");
    }
    return builder.toString();
  }

  // Bundleable implementation.
  @Documented
  @Retention(RetentionPolicy.SOURCE)
  @Target(TYPE_USE)
  @IntDef({
    FIELD_ID,
    FIELD_LABEL,
    FIELD_LANGUAGE,
    FIELD_SELECTION_FLAGS,
    FIELD_ROLE_FLAGS,
    FIELD_AVERAGE_BITRATE,
    FIELD_PEAK_BITRATE,
    FIELD_CODECS,
    FIELD_METADATA,
    FIELD_CONTAINER_MIME_TYPE,
    FIELD_SAMPLE_MIME_TYPE,
    FIELD_MAX_INPUT_SIZE,
    FIELD_INITIALIZATION_DATA,
    FIELD_DRM_INIT_DATA,
    FIELD_SUBSAMPLE_OFFSET_US,
    FIELD_WIDTH,
    FIELD_HEIGHT,
    FIELD_FRAME_RATE,
    FIELD_ROTATION_DEGREES,
    FIELD_PIXEL_WIDTH_HEIGHT_RATIO,
    FIELD_PROJECTION_DATA,
    FIELD_STEREO_MODE,
    FIELD_COLOR_INFO,
    FIELD_CHANNEL_COUNT,
    FIELD_SAMPLE_RATE,
    FIELD_PCM_ENCODING,
    FIELD_ENCODER_DELAY,
    FIELD_ENCODER_PADDING,
    FIELD_ACCESSIBILITY_CHANNEL,
    FIELD_CRYPTO_TYPE,
  })
  private @interface FieldNumber {}

  private static final int FIELD_ID = 0;
  private static final int FIELD_LABEL = 1;
  private static final int FIELD_LANGUAGE = 2;
  private static final int FIELD_SELECTION_FLAGS = 3;
  private static final int FIELD_ROLE_FLAGS = 4;
  private static final int FIELD_AVERAGE_BITRATE = 5;
  private static final int FIELD_PEAK_BITRATE = 6;
  private static final int FIELD_CODECS = 7;
  private static final int FIELD_METADATA = 8;
  private static final int FIELD_CONTAINER_MIME_TYPE = 9;
  private static final int FIELD_SAMPLE_MIME_TYPE = 10;
  private static final int FIELD_MAX_INPUT_SIZE = 11;
  private static final int FIELD_INITIALIZATION_DATA = 12;
  private static final int FIELD_DRM_INIT_DATA = 13;
  private static final int FIELD_SUBSAMPLE_OFFSET_US = 14;
  private static final int FIELD_WIDTH = 15;
  private static final int FIELD_HEIGHT = 16;
  private static final int FIELD_FRAME_RATE = 17;
  private static final int FIELD_ROTATION_DEGREES = 18;
  private static final int FIELD_PIXEL_WIDTH_HEIGHT_RATIO = 19;
  private static final int FIELD_PROJECTION_DATA = 20;
  private static final int FIELD_STEREO_MODE = 21;
  private static final int FIELD_COLOR_INFO = 22;
  private static final int FIELD_CHANNEL_COUNT = 23;
  private static final int FIELD_SAMPLE_RATE = 24;
  private static final int FIELD_PCM_ENCODING = 25;
  private static final int FIELD_ENCODER_DELAY = 26;
  private static final int FIELD_ENCODER_PADDING = 27;
  private static final int FIELD_ACCESSIBILITY_CHANNEL = 28;
  private static final int FIELD_CRYPTO_TYPE = 29;

  @UnstableApi
  @Override
  public Bundle toBundle() {
    Bundle bundle = new Bundle();
    bundle.putString(keyForField(FIELD_ID), id);
    bundle.putString(keyForField(FIELD_LABEL), label);
    bundle.putString(keyForField(FIELD_LANGUAGE), language);
    bundle.putInt(keyForField(FIELD_SELECTION_FLAGS), selectionFlags);
    bundle.putInt(keyForField(FIELD_ROLE_FLAGS), roleFlags);
    bundle.putInt(keyForField(FIELD_AVERAGE_BITRATE), averageBitrate);
    bundle.putInt(keyForField(FIELD_PEAK_BITRATE), peakBitrate);
    bundle.putString(keyForField(FIELD_CODECS), codecs);
    // Metadata is currently not Bundleable because Metadata.Entry is an Interface,
    // which would be difficult to unbundle in a backward compatible way.
    // The entries are additionally of limited usefulness to remote processes.
    bundle.putParcelable(keyForField(FIELD_METADATA), metadata);
    // Container specific.
    bundle.putString(keyForField(FIELD_CONTAINER_MIME_TYPE), containerMimeType);
    // Sample specific.
    bundle.putString(keyForField(FIELD_SAMPLE_MIME_TYPE), sampleMimeType);
    bundle.putInt(keyForField(FIELD_MAX_INPUT_SIZE), maxInputSize);
    for (int i = 0; i < initializationData.size(); i++) {
      bundle.putByteArray(keyForInitializationData(i), initializationData.get(i));
    }
    // DrmInitData doesn't need to be Bundleable as it's only used in the playing process to
    // initialize the decoder.
    bundle.putParcelable(keyForField(FIELD_DRM_INIT_DATA), drmInitData);
    bundle.putLong(keyForField(FIELD_SUBSAMPLE_OFFSET_US), subsampleOffsetUs);
    // Video specific.
    bundle.putInt(keyForField(FIELD_WIDTH), width);
    bundle.putInt(keyForField(FIELD_HEIGHT), height);
    bundle.putFloat(keyForField(FIELD_FRAME_RATE), frameRate);
    bundle.putInt(keyForField(FIELD_ROTATION_DEGREES), rotationDegrees);
    bundle.putFloat(keyForField(FIELD_PIXEL_WIDTH_HEIGHT_RATIO), pixelWidthHeightRatio);
    bundle.putByteArray(keyForField(FIELD_PROJECTION_DATA), projectionData);
    bundle.putInt(keyForField(FIELD_STEREO_MODE), stereoMode);
    bundle.putBundle(keyForField(FIELD_COLOR_INFO), BundleableUtil.toNullableBundle(colorInfo));
    // Audio specific.
    bundle.putInt(keyForField(FIELD_CHANNEL_COUNT), channelCount);
    bundle.putInt(keyForField(FIELD_SAMPLE_RATE), sampleRate);
    bundle.putInt(keyForField(FIELD_PCM_ENCODING), pcmEncoding);
    bundle.putInt(keyForField(FIELD_ENCODER_DELAY), encoderDelay);
    bundle.putInt(keyForField(FIELD_ENCODER_PADDING), encoderPadding);
    // Text specific.
    bundle.putInt(keyForField(FIELD_ACCESSIBILITY_CHANNEL), accessibilityChannel);
    // Source specific.
    bundle.putInt(keyForField(FIELD_CRYPTO_TYPE), cryptoType);
    return bundle;
  }

  /** Object that can restore {@code Format} from a {@link Bundle}. */
  @UnstableApi public static final Creator<Format> CREATOR = Format::fromBundle;

  private static Format fromBundle(Bundle bundle) {
    Builder builder = new Builder();
    BundleableUtil.ensureClassLoader(bundle);
    builder
        .setId(defaultIfNull(bundle.getString(keyForField(FIELD_ID)), DEFAULT.id))
        .setLabel(defaultIfNull(bundle.getString(keyForField(FIELD_LABEL)), DEFAULT.label))
        .setLanguage(defaultIfNull(bundle.getString(keyForField(FIELD_LANGUAGE)), DEFAULT.language))
        .setSelectionFlags(
            bundle.getInt(keyForField(FIELD_SELECTION_FLAGS), DEFAULT.selectionFlags))
        .setRoleFlags(bundle.getInt(keyForField(FIELD_ROLE_FLAGS), DEFAULT.roleFlags))
        .setAverageBitrate(
            bundle.getInt(keyForField(FIELD_AVERAGE_BITRATE), DEFAULT.averageBitrate))
        .setPeakBitrate(bundle.getInt(keyForField(FIELD_PEAK_BITRATE), DEFAULT.peakBitrate))
        .setCodecs(defaultIfNull(bundle.getString(keyForField(FIELD_CODECS)), DEFAULT.codecs))
        .setMetadata(
            defaultIfNull(bundle.getParcelable(keyForField(FIELD_METADATA)), DEFAULT.metadata))
        // Container specific.
        .setContainerMimeType(
            defaultIfNull(
                bundle.getString(keyForField(FIELD_CONTAINER_MIME_TYPE)),
                DEFAULT.containerMimeType))
        // Sample specific.
        .setSampleMimeType(
            defaultIfNull(
                bundle.getString(keyForField(FIELD_SAMPLE_MIME_TYPE)), DEFAULT.sampleMimeType))
        .setMaxInputSize(bundle.getInt(keyForField(FIELD_MAX_INPUT_SIZE), DEFAULT.maxInputSize));

    List<byte[]> initializationData = new ArrayList<>();
    for (int i = 0; ; i++) {
      @Nullable byte[] data = bundle.getByteArray(keyForInitializationData(i));
      if (data == null) {
        break;
      }
      initializationData.add(data);
    }
    builder
        .setInitializationData(initializationData)
        .setDrmInitData(bundle.getParcelable(keyForField(FIELD_DRM_INIT_DATA)))
        .setSubsampleOffsetUs(
            bundle.getLong(keyForField(FIELD_SUBSAMPLE_OFFSET_US), DEFAULT.subsampleOffsetUs))
        // Video specific.
        .setWidth(bundle.getInt(keyForField(FIELD_WIDTH), DEFAULT.width))
        .setHeight(bundle.getInt(keyForField(FIELD_HEIGHT), DEFAULT.height))
        .setFrameRate(bundle.getFloat(keyForField(FIELD_FRAME_RATE), DEFAULT.frameRate))
        .setRotationDegrees(
            bundle.getInt(keyForField(FIELD_ROTATION_DEGREES), DEFAULT.rotationDegrees))
        .setPixelWidthHeightRatio(
            bundle.getFloat(
                keyForField(FIELD_PIXEL_WIDTH_HEIGHT_RATIO), DEFAULT.pixelWidthHeightRatio))
        .setProjectionData(bundle.getByteArray(keyForField(FIELD_PROJECTION_DATA)))
        .setStereoMode(bundle.getInt(keyForField(FIELD_STEREO_MODE), DEFAULT.stereoMode))
        .setColorInfo(
            BundleableUtil.fromNullableBundle(
                ColorInfo.CREATOR, bundle.getBundle(keyForField(FIELD_COLOR_INFO))))
        // Audio specific.
        .setChannelCount(bundle.getInt(keyForField(FIELD_CHANNEL_COUNT), DEFAULT.channelCount))
        .setSampleRate(bundle.getInt(keyForField(FIELD_SAMPLE_RATE), DEFAULT.sampleRate))
        .setPcmEncoding(bundle.getInt(keyForField(FIELD_PCM_ENCODING), DEFAULT.pcmEncoding))
        .setEncoderDelay(bundle.getInt(keyForField(FIELD_ENCODER_DELAY), DEFAULT.encoderDelay))
        .setEncoderPadding(
            bundle.getInt(keyForField(FIELD_ENCODER_PADDING), DEFAULT.encoderPadding))
        // Text specific.
        .setAccessibilityChannel(
            bundle.getInt(keyForField(FIELD_ACCESSIBILITY_CHANNEL), DEFAULT.accessibilityChannel))
        // Source specific.
        .setCryptoType(bundle.getInt(keyForField(FIELD_CRYPTO_TYPE), DEFAULT.cryptoType));

    return builder.build();
  }

  private static String keyForField(@FieldNumber int field) {
    return Integer.toString(field, Character.MAX_RADIX);
  }

  private static String keyForInitializationData(int initialisationDataIndex) {
    return keyForField(FIELD_INITIALIZATION_DATA)
        + "_"
        + Integer.toString(initialisationDataIndex, Character.MAX_RADIX);
  }

  @Nullable
  private static <T> T defaultIfNull(@Nullable T value, @Nullable T defaultValue) {
    return value != null ? value : defaultValue;
  }
}
