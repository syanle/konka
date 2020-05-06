package com.konka.appupgrade;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IUpgradeService extends IInterface {

    public static abstract class Stub extends Binder implements IUpgradeService {
        private static final String DESCRIPTOR = "com.konka.appupgrade.IUpgradeService";
        static final int TRANSACTION_cancelUpgrade = 2;
        static final int TRANSACTION_scheduleUpgrade = 1;

        private static class Proxy implements IUpgradeService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void scheduleUpgrade(String pkgName, IRpcCallback downloadCb, IRpcCallback upgradeCb) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongBinder(downloadCb != null ? downloadCb.asBinder() : null);
                    if (upgradeCb != null) {
                        iBinder = upgradeCb.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelUpgrade(String pkgName, IRpcCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUpgradeService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUpgradeService)) {
                return new Proxy(obj);
            }
            return (IUpgradeService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    scheduleUpgrade(data.readString(), com.konka.appupgrade.IRpcCallback.Stub.asInterface(data.readStrongBinder()), com.konka.appupgrade.IRpcCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    cancelUpgrade(data.readString(), com.konka.appupgrade.IRpcCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void cancelUpgrade(String str, IRpcCallback iRpcCallback) throws RemoteException;

    void scheduleUpgrade(String str, IRpcCallback iRpcCallback, IRpcCallback iRpcCallback2) throws RemoteException;
}
