/*
 * Copyright (C) 2016 - 2018 ExoMedia Contributors
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

package com.devbrackets.android.exomedia.core.api;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;

import androidx.annotation.*;

import com.devbrackets.android.exomedia.ExoMedia;
import com.devbrackets.android.exomedia.core.ListenerMux;
import com.devbrackets.android.exomedia.core.exoplayer.WindowInfo;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;

import java.util.Map;

/**
 * The basic APIs expected in the backing media player
 * implementations to allow us to create an abstraction
 * between the Native (Android) MediaPlayer and the AudioPlayer
 * using the ExoPlayer.
 */
public interface AudioPlayerApi {
    void setDataSource(@Nullable Uri uri);

    void setDataSource(@Nullable Uri uri, @Nullable MediaSource mediaSource);

    /**
     * Sets the {@link MediaDrmCallback} to use when handling DRM for media.
     * This should be called before specifying the videos uri or path
     * <br>
     * <b>NOTE:</b> DRM is only supported on API 18 +
     *
     * @param drmCallback The callback to use when handling DRM media
     */
    void setDrmCallback(@Nullable MediaDrmCallback drmCallback);

    /**
     * Prepares the media specified with {@link #setDataSource(Uri)} or
     * {@link #setDataSource(Uri, MediaSource)} in an asynchronous manner
     */
    void prepareAsync();

    boolean isPlaying();

    void start();

    void pause();

    void stopPlayback();

    /**
     * Prepares the media previously specified for playback.  This should only be called after
     * the playback has completed to restart playback from the beginning.
     *
     * @return {@code true} if the media was successfully restarted
     */
    boolean restart();

    /**
     * Releases the resources associated with this media player
     */
    void release();

    void reset();

    @IntRange(from = 0)
    long getDuration();

    @IntRange(from = 0)
    long getCurrentPosition();

    @IntRange(from = 0, to = 100)
    int getBufferedPercent();

    @Nullable
    WindowInfo getWindowInfo();

    int getAudioSessionId();

    /**
     * Sets the playback speed for this MediaPlayer.
     *
     * @param speed The speed to play the media back at
     * @return True if the speed was set
     */
    boolean setPlaybackSpeed(float speed);

    float getPlaybackSpeed();

    /**
     * Sets the audio stream type for this MediaPlayer. See {@link AudioManager}
     * for a list of stream types. Must call this method before prepare() or
     * prepareAsync() in order for the target stream type to become effective
     * thereafter.
     *
     * @param streamType The audio stream type
     * @see android.media.AudioManager
     */
    void setAudioStreamType(int streamType);

    boolean trackSelectionAvailable();

    /**
     * @deprecated use {@link #setTrack(ExoMedia.RendererType, int, int)}
     */
    @Deprecated
    void setTrack(@NonNull ExoMedia.RendererType type, int trackIndex);

    void setTrack(@NonNull ExoMedia.RendererType type, int groupIndex, int trackIndex);

    int getSelectedTrackIndex(@NonNull ExoMedia.RendererType type, int groupIndex);

    /**
     * Retrieves a list of available tracks to select from.  Typically {@link #trackSelectionAvailable()}
     * should be called before this.
     *
     * @return A list of available tracks associated with each track type
     */
    @Nullable
    Map<ExoMedia.RendererType, TrackGroupArray> getAvailableTracks();

    @FloatRange(from = 0.0, to = 1.0)
    float getVolumeLeft();

    @FloatRange(from = 0.0, to = 1.0)
    float getVolumeRight();

    void setVolume(@FloatRange(from = 0.0, to = 1.0) float left, @FloatRange(from = 0.0, to = 1.0) float right);

    void seekTo(@IntRange(from = 0) long milliseconds);

    void setWakeMode(@NonNull Context context, int mode);

    void setListenerMux(ListenerMux listenerMux);

    void onMediaPrepared();

    void setRepeatMode(@Player.RepeatMode int repeatMode);
}