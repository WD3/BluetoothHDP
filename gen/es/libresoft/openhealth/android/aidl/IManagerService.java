/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\BluetoothHDPActivity\\src\\es\\libresoft\\openhealth\\android\\aidl\\IManagerService.aidl
 */
package es.libresoft.openhealth.android.aidl;
public interface IManagerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements es.libresoft.openhealth.android.aidl.IManagerService
{
private static final java.lang.String DESCRIPTOR = "es.libresoft.openhealth.android.aidl.IManagerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an es.libresoft.openhealth.android.aidl.IManagerService interface,
 * generating a proxy if needed.
 */
public static es.libresoft.openhealth.android.aidl.IManagerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof es.libresoft.openhealth.android.aidl.IManagerService))) {
return ((es.libresoft.openhealth.android.aidl.IManagerService)iin);
}
return new es.libresoft.openhealth.android.aidl.IManagerService.Stub.Proxy(obj);
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
case TRANSACTION_agents:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<es.libresoft.openhealth.android.aidl.IAgent> _arg0;
_arg0 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.IAgent>();
this.agents(_arg0);
reply.writeNoException();
reply.writeTypedList(_arg0);
return true;
}
case TRANSACTION_registerApplication:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IManagerClientCallback _arg0;
_arg0 = es.libresoft.openhealth.android.aidl.IManagerClientCallback.Stub.asInterface(data.readStrongBinder());
this.registerApplication(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterApplication:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IManagerClientCallback _arg0;
_arg0 = es.libresoft.openhealth.android.aidl.IManagerClientCallback.Stub.asInterface(data.readStrongBinder());
this.unregisterApplication(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements es.libresoft.openhealth.android.aidl.IManagerService
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
@Override public void agents(java.util.List<es.libresoft.openhealth.android.aidl.IAgent> agentList) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_agents, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(agentList, es.libresoft.openhealth.android.aidl.IAgent.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void registerApplication(es.libresoft.openhealth.android.aidl.IManagerClientCallback mc) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((mc!=null))?(mc.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerApplication, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterApplication(es.libresoft.openhealth.android.aidl.IManagerClientCallback mc) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((mc!=null))?(mc.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterApplication, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_agents = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_registerApplication = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_unregisterApplication = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void agents(java.util.List<es.libresoft.openhealth.android.aidl.IAgent> agentList) throws android.os.RemoteException;
public void registerApplication(es.libresoft.openhealth.android.aidl.IManagerClientCallback mc) throws android.os.RemoteException;
public void unregisterApplication(es.libresoft.openhealth.android.aidl.IManagerClientCallback mc) throws android.os.RemoteException;
}
