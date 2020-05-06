package com.konka.kkimplements.tv.mstar;

import android.content.Context;
import com.konka.kkinterface.tv.CommonDesk;
import com.konka.kkinterface.tv.PvrDesk;
import com.mstar.android.tvapi.common.PvrManager.OnPvrEventListener;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.CaptureThumbnailResult;
import com.mstar.android.tvapi.common.vo.EnumPvrStatus;
import com.mstar.android.tvapi.common.vo.PvrFileInfo;
import com.mstar.android.tvapi.common.vo.PvrPlaybackSpeed.EnumPvrPlaybackSpeed;
import com.mstar.android.tvapi.common.vo.PvrUsbDeviceLabel.EnumPvrUsbDeviceLabel;
import com.mstar.android.tvapi.common.vo.VideoWindowType;

public class PvrDeskImpl extends BaseDeskImpl implements PvrDesk {
    private static PvrDeskImpl pvrMgrImpl = null;

    /* renamed from: com reason: collision with root package name */
    private CommonDesk f3com = null;
    private Context context;

    private PvrDeskImpl(Context context2) {
        this.context = context2;
        this.f3com = CommonDeskImpl.getInstance(context2);
        this.f3com.printfI("TvService", "PvrManagerImpl constructor!!");
    }

    public static PvrDeskImpl getPvrMgrInstance(Context context2) {
        if (pvrMgrImpl == null) {
            pvrMgrImpl = new PvrDeskImpl(context2);
        }
        return pvrMgrImpl;
    }

    public short startAlwaysTimeShiftRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().startAlwaysTimeShiftRecord();
        }
        return 0;
    }

    public short stopAlwaysTimeShiftRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().stopAlwaysTimeShiftRecord();
        }
        return 0;
    }

    public short pauseAlwaysTimeShiftRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().pauseAlwaysTimeShiftRecord();
        }
        return 0;
    }

    public short startAlwaysTimeShiftPlayback() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().startAlwaysTimeShiftPlayback();
        }
        return 0;
    }

    public void stopAlwaysTimeShiftPlayback() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().stopAlwaysTimeShiftPlayback();
        }
    }

    public boolean isAlwaysTimeShiftPlaybackPaused() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isAlwaysTimeShiftPlaybackPaused();
        }
        return false;
    }

    public boolean isAlwaysTimeShiftRecording() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isAlwaysTimeShiftRecording();
        }
        return false;
    }

    public EnumPvrStatus startRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().startRecord();
        }
        return null;
    }

    public void pauseRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().pauseRecord();
        }
    }

    public void resumeRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().resumeRecord();
        }
    }

    public void stopRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().stopRecord();
        }
    }

    public int getEstimateRecordRemainingTime() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getEstimateRecordRemainingTime();
        }
        return 0;
    }

    public EnumPvrStatus startPlayback(String fileName) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().startPlayback(fileName);
        }
        return null;
    }

    public EnumPvrStatus startPlayback(String fileName, int playbackTimeInSecond) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().startPlayback(fileName, playbackTimeInSecond);
        }
        return null;
    }

    public EnumPvrStatus startPlayback(String fileName, int playbackTimeInSecond, int thumbnailPts) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().startPlayback(fileName, playbackTimeInSecond);
        }
        return null;
    }

    public void pausePlayback() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().pausePlayback();
        }
    }

    public void resumePlayback() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().resumePlayback();
        }
    }

    public void stopPlayback() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().stopPlayback();
        }
    }

    public void doPlaybackFastForward() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().doPlaybackFastForward();
        }
    }

    public void doPlaybackFastBackward() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().doPlaybackFastBackward();
        }
    }

    public void doPlaybackJumpForward() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().doPlaybackJumpForward();
        }
    }

    public void doPlaybackJumpBackward() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().doPlaybackJumpBackward();
        }
    }

    public void stepInPlayback() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().stepInPlayback();
        }
    }

    public void startPlaybackLoop(int abLoopBeginTime, int abLoopEndTime) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().startPlaybackLoop(abLoopBeginTime, abLoopEndTime);
        }
    }

    public void stopPlaybackLoop() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().stopPlaybackLoop();
        }
    }

    public void setPlaybackSpeed(EnumPvrPlaybackSpeed playbackSpeed) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().setPlaybackSpeed(playbackSpeed);
        }
    }

    public EnumPvrPlaybackSpeed getPlaybackSpeed() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getPlaybackSpeed();
        }
        return null;
    }

    public boolean jumpPlaybackTime(int jumpToTimeInSeconds) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().jumpPlaybackTime(jumpToTimeInSeconds);
        }
        return false;
    }

    public EnumPvrStatus startTimeShiftRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().startTimeShiftRecord();
        }
        return null;
    }

    public void stopTimeShiftRecord() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().stopTimeShiftRecord();
        }
    }

    public void stopTimeShiftPlayback() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().stopTimeShiftPlayback();
        }
    }

    public void stopTimeShift() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().stopTimeShift();
        }
    }

    public boolean stopPvr() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().stopPvr();
        }
        return false;
    }

    public boolean isRecording() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isRecording();
        }
        return false;
    }

    public boolean isPlaybacking() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isPlaybacking();
        }
        return false;
    }

    public boolean isTimeShiftRecording() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isTimeShiftRecording();
        }
        return false;
    }

    public boolean isPlaybackParentalLock() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isPlaybackParentalLock();
        }
        return false;
    }

    public boolean isPlaybackPaused() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isPlaybackPaused();
        }
        return false;
    }

    public boolean isRecordPaused() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isRecordPaused();
        }
        return false;
    }

    public void setPlaybackWindow(VideoWindowType videoWindowType, int containerWidth, int containerHeight) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().setPlaybackWindow(videoWindowType, containerWidth, containerHeight);
        }
    }

    public String getCurRecordingFileName() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getCurRecordingFileName();
        }
        return null;
    }

    public int getCurPlaybackTimeInSecond() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getCurPlaybackTimeInSecond();
        }
        return 0;
    }

    public int getCurRecordTimeInSecond() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getCurRecordTimeInSecond();
        }
        return 0;
    }

    public boolean jumpToThumbnail(int thumbnailIndex) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().jumpToThumbnail(thumbnailIndex);
        }
        return false;
    }

    public void setTimeShiftFileSize(long timeShiftFileSizeInKb) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().setTimeShiftFileSize(timeShiftFileSizeInKb);
        }
    }

    public CaptureThumbnailResult captureThumbnail() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().captureThumbnail();
        }
        return null;
    }

    public String getCurPlaybackingFileName() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getCurPlaybackingFileName();
        }
        return null;
    }

    public EnumPvrStatus pauseAlwaysTimeShiftPlayback(boolean ready) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().pauseAlwaysTimeShiftPlayback(ready);
        }
        return null;
    }

    public EnumPvrStatus startTimeShiftPlayback() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().startTimeShiftPlayback();
        }
        return null;
    }

    public void startPvrFormat() throws TvCommonException {
    }

    public int checkUsbSpeed() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().checkUsbSpeed();
        }
        return 0;
    }

    public int getUsbPartitionNumber() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getUsbPartitionNumber();
        }
        return 0;
    }

    public int getUsbDeviceNumber() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getUsbDeviceNumber();
        }
        return 0;
    }

    public short getUsbDeviceIndex() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getUsbDeviceIndex();
        }
        return 0;
    }

    public EnumPvrUsbDeviceLabel getUsbDeviceLabel(int deviceIndex) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getUsbDeviceLabel(deviceIndex);
        }
        return null;
    }

    public int getPvrFileNumber() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getPvrFileNumber();
        }
        return 0;
    }

    public PvrFileInfo getPvrFileInfo(int index, int nSortKey) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getPvrFileInfo(index, nSortKey);
        }
        return null;
    }

    public int getFileLcn(int index) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getFileLcn(index);
        }
        return index;
    }

    public String getFileServiceName(String fileName) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getFileServiceName(fileName);
        }
        return fileName;
    }

    public String getFileEventName(String fileName) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getFileEventName(fileName);
        }
        return fileName;
    }

    public boolean assignThumbnailFileInfoHandler(String fileName) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().assignThumbnailFileInfoHandler(fileName);
        }
        return false;
    }

    public int getThumbnailNumber() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getThumbnailNumber();
        }
        return 0;
    }

    public String getThumbnailPath(int index) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getThumbnailPath(index);
        }
        return null;
    }

    public String getThumbnailDisplay(int index) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getThumbnailDisplay(index);
        }
        return null;
    }

    public int[] getThumbnailTimeStamp(int index) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getThumbnailTimeStamp(index);
        }
        return null;
    }

    public boolean changeDevice(short deviceIndex) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().changeDevice(deviceIndex);
        }
        return false;
    }

    public boolean setPVRParas(String path, short fileType) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().setPvrParams(path, fileType);
        }
        return false;
    }

    public void setOnPvrEventListener(OnPvrEventListener listener) {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().setOnPvrEventListener(listener);
        }
    }

    public int getRecordedFileDurationTime(String filename) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getRecordedFileDurationTime(filename);
        }
        return 0;
    }

    public void deletefile(int index, String filename) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().deletefile(index, filename);
        }
    }

    public void setRecordAll(boolean bRecordAll) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().setRecordAll(bRecordAll);
        }
    }

    public int getMetadataSortKey() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getMetadataSortKey();
        }
        return 0;
    }

    public void setMetadataSortKey(int nSortKey) throws TvCommonException {
        if (nSortKey < 0 || nSortKey > 5) {
            throw new TvCommonException("nSortKey out of range exception!");
        } else if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().setMetadataSortKey(nSortKey);
        }
    }

    public void setMetadataSortAscending(boolean bIsAscend) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().setMetadataSortAscending(bIsAscend);
        }
    }

    public boolean isMetadataSortAscending() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().isMetadataSortAscending();
        }
        return false;
    }

    public boolean createMetaData(String strMountPath) throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().createMetadata(strMountPath);
        }
        return false;
    }

    public void clearMetaData() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            TvManager.getInstance().getPvrManager().clearMetadata();
        }
    }

    public String getPvrMountPath() throws TvCommonException {
        if (TvManager.getInstance() != null) {
            return TvManager.getInstance().getPvrManager().getPvrMountPath();
        }
        return null;
    }
}
