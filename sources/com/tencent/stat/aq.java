package com.tencent.stat;

import android.content.Context;
import com.konka.kkinterface.tv.ChannelDesk;
import com.tencent.stat.a.e;
import com.tencent.stat.common.k;
import com.tencent.stat.common.p;

class aq {
    private static volatile long f = 0;
    /* access modifiers changed from: private */
    public e a;
    private StatReportStrategy b = null;
    /* access modifiers changed from: private */
    public boolean c = false;
    /* access modifiers changed from: private */
    public Context d = null;
    private long e = System.currentTimeMillis();

    public aq(e eVar) {
        this.a = eVar;
        this.b = StatConfig.getStatSendStrategy();
        this.c = eVar.f();
        this.d = eVar.e();
    }

    private void a(StatDispatchCallback statDispatchCallback) {
        g.b(StatServiceImpl.t).a(this.a, statDispatchCallback);
    }

    private void b() {
        if (this.a.d() != null && this.a.d().isSendImmediately()) {
            this.b = StatReportStrategy.INSTANT;
        }
        if (StatConfig.j && a.a(StatServiceImpl.t).e()) {
            this.b = StatReportStrategy.INSTANT;
        }
        if (StatConfig.isDebugEnable()) {
            StatServiceImpl.q.i("strategy=" + this.b.name());
        }
        switch (ag.a[this.b.ordinal()]) {
            case 1:
                c();
                return;
            case 2:
                au.a(this.d).a(this.a, (StatDispatchCallback) null, this.c, false);
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("PERIOD currTime=" + this.e + ",nextPeriodSendTs=" + StatServiceImpl.c + ",difftime=" + (StatServiceImpl.c - this.e));
                }
                if (StatServiceImpl.c == 0) {
                    StatServiceImpl.c = p.a(this.d, "last_period_ts", 0);
                    if (this.e > StatServiceImpl.c) {
                        StatServiceImpl.f(this.d);
                    }
                    long sendPeriodMinutes = this.e + ((long) (StatConfig.getSendPeriodMinutes() * 60 * ChannelDesk.max_dtv_count));
                    if (StatServiceImpl.c > sendPeriodMinutes) {
                        StatServiceImpl.c = sendPeriodMinutes;
                    }
                    d.a(this.d).a();
                }
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("PERIOD currTime=" + this.e + ",nextPeriodSendTs=" + StatServiceImpl.c + ",difftime=" + (StatServiceImpl.c - this.e));
                }
                if (this.e > StatServiceImpl.c) {
                    StatServiceImpl.f(this.d);
                    return;
                }
                return;
            case 3:
            case 4:
                au.a(this.d).a(this.a, (StatDispatchCallback) null, this.c, false);
                return;
            case 5:
                au.a(this.d).a(this.a, (StatDispatchCallback) new ar(this), this.c, true);
                return;
            case 6:
                if (a.a(StatServiceImpl.t).c() == 1) {
                    c();
                    return;
                } else {
                    au.a(this.d).a(this.a, (StatDispatchCallback) null, this.c, false);
                    return;
                }
            case 7:
                if (k.e(this.d)) {
                    a((StatDispatchCallback) new as(this));
                    return;
                }
                return;
            default:
                StatServiceImpl.q.error((Object) "Invalid stat strategy:" + StatConfig.getStatSendStrategy());
                return;
        }
    }

    private void c() {
        if (au.b().a <= 0 || !StatConfig.l) {
            a((StatDispatchCallback) new at(this));
            return;
        }
        au.b().a(this.a, (StatDispatchCallback) null, this.c, true);
        au.b().a(-1);
    }

    private boolean d() {
        if (StatConfig.h > 0) {
            if (this.e > StatServiceImpl.h) {
                StatServiceImpl.g.clear();
                StatServiceImpl.h = this.e + StatConfig.i;
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("clear methodsCalledLimitMap, nextLimitCallClearTime=" + StatServiceImpl.h);
                }
            }
            Integer valueOf = Integer.valueOf(this.a.a().a());
            Integer num = (Integer) StatServiceImpl.g.get(valueOf);
            if (num != null) {
                StatServiceImpl.g.put(valueOf, Integer.valueOf(num.intValue() + 1));
                if (num.intValue() > StatConfig.h) {
                    if (StatConfig.isDebugEnable()) {
                        StatServiceImpl.q.e((Object) "event " + this.a.g() + " was discard, cause of called limit, current:" + num + ", limit:" + StatConfig.h + ", period:" + StatConfig.i + " ms");
                    }
                    return true;
                }
            } else {
                StatServiceImpl.g.put(valueOf, Integer.valueOf(1));
            }
        }
        return false;
    }

    public void a() {
        if (!d()) {
            if (StatConfig.p != null) {
                String g = this.a.g();
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("transfer event data:" + g);
                }
                StatConfig.p.onTransfer(g);
                return;
            }
            if (StatConfig.m > 0 && this.e >= f) {
                StatServiceImpl.flushDataToDB(this.d);
                f = this.e + StatConfig.n;
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("nextFlushTime=" + f);
                }
            }
            if (a.a(this.d).f()) {
                if (StatConfig.isDebugEnable()) {
                    StatServiceImpl.q.i("sendFailedCount=" + StatServiceImpl.a);
                }
                if (!StatServiceImpl.a()) {
                    b();
                    return;
                }
                au.a(this.d).a(this.a, (StatDispatchCallback) null, this.c, false);
                if (this.e - StatServiceImpl.b > 1800000) {
                    StatServiceImpl.e(this.d);
                    return;
                }
                return;
            }
            au.a(this.d).a(this.a, (StatDispatchCallback) null, this.c, false);
        }
    }
}
