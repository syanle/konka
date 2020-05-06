package com.konka.passport.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface UserInfo extends IInterface {

    public static abstract class Stub extends Binder implements UserInfo {
        private static final String DESCRIPTOR = "com.konka.passport.service.UserInfo";
        static final int TRANSACTION_CheckSignature = 6;
        static final int TRANSACTION_CheckSignatureAndroid = 15;
        static final int TRANSACTION_Decode = 8;
        static final int TRANSACTION_Encode = 7;
        static final int TRANSACTION_GetADOpenFlag = 4;
        static final int TRANSACTION_GetCrcCode = 14;
        static final int TRANSACTION_GetCtrlInfoInnerApp = 17;
        static final int TRANSACTION_GetCtrlInfoThirPartyApp = 18;
        static final int TRANSACTION_GetErrorMessage = 24;
        static final int TRANSACTION_GetPassportId = 13;
        static final int TRANSACTION_GetPassportInfo = 19;
        static final int TRANSACTION_GetSerial = 2;
        static final int TRANSACTION_GetSerialWithCRC = 3;
        static final int TRANSACTION_GetUserId = 16;
        static final int TRANSACTION_LoginPassport = 21;
        static final int TRANSACTION_LogoutPassport = 23;
        static final int TRANSACTION_RegisterPassport = 22;
        static final int TRANSACTION_ReplaceUrlDomain = 5;
        static final int TRANSACTION_SendNetRequest = 1;
        static final int TRANSACTION_SendNetRequestWithFile = 20;
        static final int TRANSACTION_SendNetRequestWithLogin = 11;
        static final int TRANSACTION_UpdateRequest = 12;
        static final int TRANSACTION_cDecode = 10;
        static final int TRANSACTION_cEncode = 9;

        private static class Proxy implements UserInfo {
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

            public int SendNetRequest(String strParams, int iSysTypeId, int iSubTypeId, xmlData stXmlData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strParams);
                    _data.writeInt(iSysTypeId);
                    _data.writeInt(iSubTypeId);
                    if (stXmlData != null) {
                        _data.writeInt(1);
                        stXmlData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        stXmlData.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetSerial() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetSerialWithCRC() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int GetADOpenFlag() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String ReplaceUrlDomain(String strUrl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strUrl);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int CheckSignature(String strSN, String strMainFileName, String strSignatureFile, boolean bCheckSha1) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strSN);
                    _data.writeString(strMainFileName);
                    _data.writeString(strSignatureFile);
                    if (bCheckSha1) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] Encode(byte[] baData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(baData);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    _reply.readByteArray(baData);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] Decode(byte[] baData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(baData);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    _reply.readByteArray(baData);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] cEncode(byte[] baData, byte[] baKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(baData);
                    _data.writeByteArray(baKey);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    _reply.readByteArray(baData);
                    _reply.readByteArray(baKey);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte[] cDecode(byte[] baData, byte[] baKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(baData);
                    _data.writeByteArray(baKey);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    _reply.readByteArray(baData);
                    _reply.readByteArray(baKey);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int SendNetRequestWithLogin(String strParams, int iSysTypeId, int iSubTypeId, xmlData stXmlData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strParams);
                    _data.writeInt(iSysTypeId);
                    _data.writeInt(iSubTypeId);
                    if (stXmlData != null) {
                        _data.writeInt(1);
                        stXmlData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        stXmlData.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int UpdateRequest(String strParams, xmlData stXmlData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strParams);
                    if (stXmlData != null) {
                        _data.writeInt(1);
                        stXmlData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        stXmlData.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetPassportId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_GetPassportId, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetCrcCode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_GetCrcCode, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int CheckSignatureAndroid(String strPackageName, String strPackageKeySha1, String strSignatureFile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strPackageName);
                    _data.writeString(strPackageKeySha1);
                    _data.writeString(strSignatureFile);
                    this.mRemote.transact(Stub.TRANSACTION_CheckSignatureAndroid, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetCtrlInfoInnerApp(int BussinessId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(BussinessId);
                    this.mRemote.transact(Stub.TRANSACTION_GetCtrlInfoInnerApp, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetCtrlInfoThirPartyApp(String AppPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(AppPackageName);
                    this.mRemote.transact(Stub.TRANSACTION_GetCtrlInfoThirPartyApp, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int GetPassportInfo(PassportInfo passportinfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (passportinfo != null) {
                        _data.writeInt(1);
                        passportinfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_GetPassportInfo, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        passportinfo.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int SendNetRequestWithFile(String strParams, int iSysTypeId, int iSubTypeId, xmlData stXmlData, String strFilename) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strParams);
                    _data.writeInt(iSysTypeId);
                    _data.writeInt(iSubTypeId);
                    if (stXmlData != null) {
                        _data.writeInt(1);
                        stXmlData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(strFilename);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        stXmlData.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int LoginPassport(String strname, String strpassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strname);
                    _data.writeString(strpassword);
                    this.mRemote.transact(Stub.TRANSACTION_LoginPassport, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int RegisterPassport(String strname, String strpassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(strname);
                    _data.writeString(strpassword);
                    this.mRemote.transact(Stub.TRANSACTION_RegisterPassport, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int LogoutPassport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_LogoutPassport, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String GetErrorMessage(int iError) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(iError);
                    this.mRemote.transact(Stub.TRANSACTION_GetErrorMessage, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static UserInfo asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof UserInfo)) {
                return new Proxy(obj);
            }
            return (UserInfo) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            xmlData _arg3;
            PassportInfo _arg0;
            xmlData _arg1;
            xmlData _arg32;
            boolean _arg33;
            xmlData _arg34;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg12 = data.readInt();
                    int _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg34 = (xmlData) xmlData.CREATOR.createFromParcel(data);
                    } else {
                        _arg34 = null;
                    }
                    int _result = SendNetRequest(_arg02, _arg12, _arg2, _arg34);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    if (_arg34 != null) {
                        reply.writeInt(1);
                        _arg34.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = GetSerial();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = GetSerialWithCRC();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _result4 = GetADOpenFlag();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _result5 = ReplaceUrlDomain(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg13 = data.readString();
                    String _arg22 = data.readString();
                    if (data.readInt() != 0) {
                        _arg33 = true;
                    } else {
                        _arg33 = false;
                    }
                    int _result6 = CheckSignature(_arg03, _arg13, _arg22, _arg33);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg04 = data.createByteArray();
                    byte[] _result7 = Encode(_arg04);
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    reply.writeByteArray(_arg04);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg05 = data.createByteArray();
                    byte[] _result8 = Decode(_arg05);
                    reply.writeNoException();
                    reply.writeByteArray(_result8);
                    reply.writeByteArray(_arg05);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg06 = data.createByteArray();
                    byte[] _arg14 = data.createByteArray();
                    byte[] _result9 = cEncode(_arg06, _arg14);
                    reply.writeNoException();
                    reply.writeByteArray(_result9);
                    reply.writeByteArray(_arg06);
                    reply.writeByteArray(_arg14);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg07 = data.createByteArray();
                    byte[] _arg15 = data.createByteArray();
                    byte[] _result10 = cDecode(_arg07, _arg15);
                    reply.writeNoException();
                    reply.writeByteArray(_result10);
                    reply.writeByteArray(_arg07);
                    reply.writeByteArray(_arg15);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    int _arg16 = data.readInt();
                    int _arg23 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg32 = (xmlData) xmlData.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    int _result11 = SendNetRequestWithLogin(_arg08, _arg16, _arg23, _arg32);
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    if (_arg32 != null) {
                        reply.writeInt(1);
                        _arg32.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = (xmlData) xmlData.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    int _result12 = UpdateRequest(_arg09, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case TRANSACTION_GetPassportId /*13*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result13 = GetPassportId();
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case TRANSACTION_GetCrcCode /*14*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result14 = GetCrcCode();
                    reply.writeNoException();
                    reply.writeString(_result14);
                    return true;
                case TRANSACTION_CheckSignatureAndroid /*15*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result15 = CheckSignatureAndroid(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _result16 = GetUserId();
                    reply.writeNoException();
                    reply.writeString(_result16);
                    return true;
                case TRANSACTION_GetCtrlInfoInnerApp /*17*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result17 = GetCtrlInfoInnerApp(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case TRANSACTION_GetCtrlInfoThirPartyApp /*18*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result18 = GetCtrlInfoThirPartyApp(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result18);
                    return true;
                case TRANSACTION_GetPassportInfo /*19*/:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (PassportInfo) PassportInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _result19 = GetPassportInfo(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    if (_arg0 != null) {
                        reply.writeInt(1);
                        _arg0.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    int _arg17 = data.readInt();
                    int _arg24 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (xmlData) xmlData.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    int _result20 = SendNetRequestWithFile(_arg010, _arg17, _arg24, _arg3, data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    if (_arg3 != null) {
                        reply.writeInt(1);
                        _arg3.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case TRANSACTION_LoginPassport /*21*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result21 = LoginPassport(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case TRANSACTION_RegisterPassport /*22*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result22 = RegisterPassport(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case TRANSACTION_LogoutPassport /*23*/:
                    data.enforceInterface(DESCRIPTOR);
                    int _result23 = LogoutPassport();
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    return true;
                case TRANSACTION_GetErrorMessage /*24*/:
                    data.enforceInterface(DESCRIPTOR);
                    String _result24 = GetErrorMessage(data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result24);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    int CheckSignature(String str, String str2, String str3, boolean z) throws RemoteException;

    int CheckSignatureAndroid(String str, String str2, String str3) throws RemoteException;

    byte[] Decode(byte[] bArr) throws RemoteException;

    byte[] Encode(byte[] bArr) throws RemoteException;

    int GetADOpenFlag() throws RemoteException;

    String GetCrcCode() throws RemoteException;

    String GetCtrlInfoInnerApp(int i) throws RemoteException;

    String GetCtrlInfoThirPartyApp(String str) throws RemoteException;

    String GetErrorMessage(int i) throws RemoteException;

    String GetPassportId() throws RemoteException;

    int GetPassportInfo(PassportInfo passportInfo) throws RemoteException;

    String GetSerial() throws RemoteException;

    String GetSerialWithCRC() throws RemoteException;

    String GetUserId() throws RemoteException;

    int LoginPassport(String str, String str2) throws RemoteException;

    int LogoutPassport() throws RemoteException;

    int RegisterPassport(String str, String str2) throws RemoteException;

    String ReplaceUrlDomain(String str) throws RemoteException;

    int SendNetRequest(String str, int i, int i2, xmlData xmldata) throws RemoteException;

    int SendNetRequestWithFile(String str, int i, int i2, xmlData xmldata, String str2) throws RemoteException;

    int SendNetRequestWithLogin(String str, int i, int i2, xmlData xmldata) throws RemoteException;

    int UpdateRequest(String str, xmlData xmldata) throws RemoteException;

    byte[] cDecode(byte[] bArr, byte[] bArr2) throws RemoteException;

    byte[] cEncode(byte[] bArr, byte[] bArr2) throws RemoteException;
}
