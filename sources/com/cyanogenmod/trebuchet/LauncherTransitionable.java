package com.cyanogenmod.trebuchet;

import android.animation.Animator;

/* compiled from: Launcher */
interface LauncherTransitionable {
    void onLauncherTransitionEnd(Launcher launcher, Animator animator, boolean z);

    boolean onLauncherTransitionStart(Launcher launcher, Animator animator, boolean z);
}
