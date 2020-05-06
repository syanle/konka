package com.konka.kkinterface.tv;

import com.mstar.android.tvapi.common.PvrManager.OnPvrEventListener;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.CaptureThumbnailResult;
import com.mstar.android.tvapi.common.vo.EnumPvrStatus;
import com.mstar.android.tvapi.common.vo.PvrFileInfo;
import com.mstar.android.tvapi.common.vo.PvrPlaybackSpeed.EnumPvrPlaybackSpeed;
import com.mstar.android.tvapi.common.vo.PvrUsbDeviceLabel.EnumPvrUsbDeviceLabel;
import com.mstar.android.tvapi.common.vo.VideoWindowType;

public interface PvrDesk extends BaseDesk {
    boolean assignThumbnailFileInfoHandler(String str) throws TvCommonException;

    CaptureThumbnailResult captureThumbnail() throws TvCommonException;

    boolean changeDevice(short s) throws TvCommonException;

    int checkUsbSpeed() throws TvCommonException;

    void clearMetaData() throws TvCommonException;

    boolean createMetaData(String str) throws TvCommonException;

    void deletefile(int i, String str) throws TvCommonException;

    void doPlaybackFastBackward() throws TvCommonException;

    void doPlaybackFastForward() throws TvCommonException;

    void doPlaybackJumpBackward() throws TvCommonException;

    void doPlaybackJumpForward() throws TvCommonException;

    int getCurPlaybackTimeInSecond() throws TvCommonException;

    String getCurPlaybackingFileName() throws TvCommonException;

    int getCurRecordTimeInSecond() throws TvCommonException;

    String getCurRecordingFileName() throws TvCommonException;

    int getEstimateRecordRemainingTime() throws TvCommonException;

    String getFileEventName(String str) throws TvCommonException;

    int getFileLcn(int i) throws TvCommonException;

    String getFileServiceName(String str) throws TvCommonException;

    int getMetadataSortKey() throws TvCommonException;

    EnumPvrPlaybackSpeed getPlaybackSpeed() throws TvCommonException;

    PvrFileInfo getPvrFileInfo(int i, int i2) throws TvCommonException;

    int getPvrFileNumber() throws TvCommonException;

    String getPvrMountPath() throws TvCommonException;

    int getRecordedFileDurationTime(String str) throws TvCommonException;

    String getThumbnailDisplay(int i) throws TvCommonException;

    int getThumbnailNumber() throws TvCommonException;

    String getThumbnailPath(int i) throws TvCommonException;

    int[] getThumbnailTimeStamp(int i) throws TvCommonException;

    short getUsbDeviceIndex() throws TvCommonException;

    EnumPvrUsbDeviceLabel getUsbDeviceLabel(int i) throws TvCommonException;

    int getUsbDeviceNumber() throws TvCommonException;

    int getUsbPartitionNumber() throws TvCommonException;

    boolean isAlwaysTimeShiftPlaybackPaused() throws TvCommonException;

    boolean isAlwaysTimeShiftRecording() throws TvCommonException;

    boolean isMetadataSortAscending() throws TvCommonException;

    boolean isPlaybackParentalLock() throws TvCommonException;

    boolean isPlaybackPaused() throws TvCommonException;

    boolean isPlaybacking() throws TvCommonException;

    boolean isRecordPaused() throws TvCommonException;

    boolean isRecording() throws TvCommonException;

    boolean isTimeShiftRecording() throws TvCommonException;

    boolean jumpPlaybackTime(int i) throws TvCommonException;

    boolean jumpToThumbnail(int i) throws TvCommonException;

    EnumPvrStatus pauseAlwaysTimeShiftPlayback(boolean z) throws TvCommonException;

    short pauseAlwaysTimeShiftRecord() throws TvCommonException;

    void pausePlayback() throws TvCommonException;

    void pauseRecord() throws TvCommonException;

    void resumePlayback() throws TvCommonException;

    void resumeRecord() throws TvCommonException;

    void setMetadataSortAscending(boolean z) throws TvCommonException;

    void setMetadataSortKey(int i) throws TvCommonException;

    void setOnPvrEventListener(OnPvrEventListener onPvrEventListener);

    boolean setPVRParas(String str, short s) throws TvCommonException;

    void setPlaybackSpeed(EnumPvrPlaybackSpeed enumPvrPlaybackSpeed) throws TvCommonException;

    void setPlaybackWindow(VideoWindowType videoWindowType, int i, int i2) throws TvCommonException;

    void setRecordAll(boolean z) throws TvCommonException;

    void setTimeShiftFileSize(long j) throws TvCommonException;

    short startAlwaysTimeShiftPlayback() throws TvCommonException;

    short startAlwaysTimeShiftRecord() throws TvCommonException;

    EnumPvrStatus startPlayback(String str) throws TvCommonException;

    EnumPvrStatus startPlayback(String str, int i) throws TvCommonException;

    EnumPvrStatus startPlayback(String str, int i, int i2) throws TvCommonException;

    void startPlaybackLoop(int i, int i2) throws TvCommonException;

    EnumPvrStatus startRecord() throws TvCommonException;

    EnumPvrStatus startTimeShiftPlayback() throws TvCommonException;

    EnumPvrStatus startTimeShiftRecord() throws TvCommonException;

    void stepInPlayback() throws TvCommonException;

    void stopAlwaysTimeShiftPlayback() throws TvCommonException;

    short stopAlwaysTimeShiftRecord() throws TvCommonException;

    void stopPlayback() throws TvCommonException;

    void stopPlaybackLoop() throws TvCommonException;

    boolean stopPvr() throws TvCommonException;

    void stopRecord() throws TvCommonException;

    void stopTimeShift() throws TvCommonException;

    void stopTimeShiftPlayback() throws TvCommonException;

    void stopTimeShiftRecord() throws TvCommonException;
}
