/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\BluetoothHDPActivity\\src\\es\\libresoft\\openhealth\\android\\aidl\\IScannerService.aidl
 */
package es.libresoft.openhealth.android.aidl;
public interface IScannerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements es.libresoft.openhealth.android.aidl.IScannerService
{
private static final java.lang.String DESCRIPTOR = "es.libresoft.openhealth.android.aidl.IScannerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an es.libresoft.openhealth.android.aidl.IScannerService interface,
 * generating a proxy if needed.
 */
public static es.libresoft.openhealth.android.aidl.IScannerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof es.libresoft.openhealth.android.aidl.IScannerService))) {
return ((es.libresoft.openhealth.android.aidl.IScannerService)iin);
}
return new es.libresoft.openhealth.android.aidl.IScannerService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setScannerOperationalState:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.types.objects.IScanner _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.types.objects.IScanner.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.types.IOperationalState _arg1;
if ((0!=data.readInt())) {
_arg1 = es.libresoft.openhealth.android.aidl.types.IOperationalState.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
boolean _result = this.setScannerOperationalState(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements es.libresoft.openhealth.android.aidl.IScannerService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public boolean setScannerOperationalState(es.libresoft.openhealth.android.aidl.types.objects.IScanner scanner, es.libresoft.openhealth.android.aidl.types.IOperationalState opState, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((scanner!=null)) {
_data.writeInt(1);
scanner.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((opState!=null)) {
_data.writeInt(1);
opState.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_setScannerOperationalState, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
if ((0!=_reply.readInt())) {
err.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setScannerOperationalState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public boolean setScannerOperationalState(es.libresoft.openhealth.android.aidl.types.objects.IScanner scanner, es.libresoft.openhealth.android.aidl.types.IOperationalState opState, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
}
