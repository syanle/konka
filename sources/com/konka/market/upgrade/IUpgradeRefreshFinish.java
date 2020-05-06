package com.konka.market.upgrade;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IUpgradeRefreshFinish extends IInterface {

    public static abstract class Stub extends Binder implements IUpgradeRefreshFinish {
        private static final String DESCRIPTOR = "com.konka.market.upgrade.IUpgradeRefreshFinish";
        static final int TRANSACTION_refreshFinish = 1;

        private static class Proxy implements IUpgradeRefreshFinish {
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

            public void refreshFinish(List<UpgradeInfo> infos, int ret, String errDes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ret);
                    _data.writeString(errDes);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    _reply.readTypedList(infos, UpgradeInfo.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUpgradeRefreshFinish asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUpgradeRefreshFinish)) {
                return new Proxy(obj);
            }
            return (IUpgradeRefreshFinish) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    List<UpgradeInfo> _arg0 = new ArrayList<>();
                    refreshFinish(_arg0, data.readInt(), data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_arg0);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void refreshFinish(List<UpgradeInfo> list, int i, String str) throws RemoteException;
}
