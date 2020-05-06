package com.cyanogenmod.trebuchet;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import java.util.LinkedList;

public class DeferredHandler {
    private Impl mHandler = new Impl(this, null);
    private MessageQueue mMessageQueue = Looper.myQueue();
    /* access modifiers changed from: private */
    public final LinkedList<Runnable> mQueue = new LinkedList<>();

    private class IdleRunnable implements Runnable {
        Runnable mRunnable;

        IdleRunnable(Runnable r) {
            this.mRunnable = r;
        }

        public void run() {
            this.mRunnable.run();
        }
    }

    private class Impl extends Handler implements IdleHandler {
        private Impl() {
        }

        /* synthetic */ Impl(DeferredHandler deferredHandler, Impl impl) {
            this();
        }

        public void handleMessage(Message msg) {
            synchronized (DeferredHandler.this.mQueue) {
                if (DeferredHandler.this.mQueue.size() != 0) {
                    Runnable r = (Runnable) DeferredHandler.this.mQueue.removeFirst();
                    r.run();
                    synchronized (DeferredHandler.this.mQueue) {
                        DeferredHandler.this.scheduleNextLocked();
                    }
                }
            }
        }

        public boolean queueIdle() {
            handleMessage(null);
            return false;
        }
    }

    public void post(Runnable runnable) {
        synchronized (this.mQueue) {
            this.mQueue.add(runnable);
            if (this.mQueue.size() == 1) {
                scheduleNextLocked();
            }
        }
    }

    public void postIdle(Runnable runnable) {
        post(new IdleRunnable(runnable));
    }

    public void cancelRunnable(Runnable runnable) {
        synchronized (this.mQueue) {
            do {
            } while (this.mQueue.remove(runnable));
        }
    }

    public void cancel() {
        synchronized (this.mQueue) {
            this.mQueue.clear();
        }
    }

    /* access modifiers changed from: 0000 */
    public void scheduleNextLocked() {
        if (this.mQueue.size() <= 0) {
            return;
        }
        if (((Runnable) this.mQueue.getFirst()) instanceof IdleRunnable) {
            this.mMessageQueue.addIdleHandler(this.mHandler);
        } else {
            this.mHandler.sendEmptyMessage(1);
        }
    }
}
