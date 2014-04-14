/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\BluetoothHDPActivity\\src\\es\\libresoft\\openhealth\\android\\aidl\\IAgentService.aidl
 */
package es.libresoft.openhealth.android.aidl;
public interface IAgentService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements es.libresoft.openhealth.android.aidl.IAgentService
{
private static final java.lang.String DESCRIPTOR = "es.libresoft.openhealth.android.aidl.IAgentService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an es.libresoft.openhealth.android.aidl.IAgentService interface,
 * generating a proxy if needed.
 */
public static es.libresoft.openhealth.android.aidl.IAgentService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof es.libresoft.openhealth.android.aidl.IAgentService))) {
return ((es.libresoft.openhealth.android.aidl.IAgentService)iin);
}
return new es.libresoft.openhealth.android.aidl.IAgentService.Stub.Proxy(obj);
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
case TRANSACTION_updateMDS:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.types.IError _arg1;
_arg1 = new es.libresoft.openhealth.android.aidl.types.IError();
boolean _result = this.updateMDS(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
if ((_arg1!=null)) {
reply.writeInt(1);
_arg1.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getState:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.IState _arg1;
_arg1 = new es.libresoft.openhealth.android.aidl.IState();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getState(_arg0, _arg1, _arg2);
reply.writeNoException();
if ((_arg1!=null)) {
reply.writeInt(1);
_arg1.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getMDS:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.types.objects.IMDS _arg1;
_arg1 = new es.libresoft.openhealth.android.aidl.types.objects.IMDS();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getMDS(_arg0, _arg1, _arg2);
reply.writeNoException();
if ((_arg1!=null)) {
reply.writeInt(1);
_arg1.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getNumeric:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<es.libresoft.openhealth.android.aidl.types.objects.INumeric> _arg1;
_arg1 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.types.objects.INumeric>();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getNumeric(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeTypedList(_arg1);
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getScanner:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IScanner> _arg1;
_arg1 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.types.objects.IScanner>();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getScanner(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeTypedList(_arg1);
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getRT_SA:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IRT_SA> _arg1;
_arg1 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.types.objects.IRT_SA>();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getRT_SA(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeTypedList(_arg1);
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getEnumeration:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IEnumeration> _arg1;
_arg1 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.types.objects.IEnumeration>();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getEnumeration(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeTypedList(_arg1);
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getPM_Store:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Store> _arg1;
_arg1 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.types.objects.IPM_Store>();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getPM_Store(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeTypedList(_arg1);
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getObjectAttrs:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.types.objects.IDIMClass _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.types.objects.IDIMClass.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<es.libresoft.openhealth.android.aidl.types.IAttribute> _arg1;
_arg1 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.types.IAttribute>();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getObjectAttrs(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeTypedList(_arg1);
if ((_arg2!=null)) {
reply.writeInt(1);
_arg2.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_setTime:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.types.IError _arg1;
_arg1 = new es.libresoft.openhealth.android.aidl.types.IError();
boolean _result = this.setTime(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
if ((_arg1!=null)) {
reply.writeInt(1);
_arg1.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_connect:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.connect(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.types.IError _arg1;
_arg1 = new es.libresoft.openhealth.android.aidl.types.IError();
boolean _result = this.disconnect(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
if ((_arg1!=null)) {
reply.writeInt(1);
_arg1.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements es.libresoft.openhealth.android.aidl.IAgentService
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
@Override public boolean updateMDS(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updateMDS, _data, _reply, 0);
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
@Override public void getState(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.IState state, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getState, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
state.readFromParcel(_reply);
}
if ((0!=_reply.readInt())) {
err.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getMDS(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.objects.IMDS mds, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getMDS, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
mds.readFromParcel(_reply);
}
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getNumeric(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.INumeric> nums, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getNumeric, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(nums, es.libresoft.openhealth.android.aidl.types.objects.INumeric.CREATOR);
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getScanner(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IScanner> scanners, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getScanner, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(scanners, es.libresoft.openhealth.android.aidl.types.objects.IScanner.CREATOR);
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getRT_SA(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IRT_SA> rts, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getRT_SA, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(rts, es.libresoft.openhealth.android.aidl.types.objects.IRT_SA.CREATOR);
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getEnumeration(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IEnumeration> nums, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getEnumeration, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(nums, es.libresoft.openhealth.android.aidl.types.objects.IEnumeration.CREATOR);
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getPM_Store(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Store> nums, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getPM_Store, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(nums, es.libresoft.openhealth.android.aidl.types.objects.IPM_Store.CREATOR);
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getObjectAttrs(es.libresoft.openhealth.android.aidl.types.objects.IDIMClass obj, java.util.List<es.libresoft.openhealth.android.aidl.types.IAttribute> attrs, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((obj!=null)) {
_data.writeInt(1);
obj.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getObjectAttrs, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(attrs, es.libresoft.openhealth.android.aidl.types.IAttribute.CREATOR);
if ((0!=_reply.readInt())) {
error.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean setTime(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_setTime, _data, _reply, 0);
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
@Override public void connect(es.libresoft.openhealth.android.aidl.IAgent agent) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_connect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean disconnect(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
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
static final int TRANSACTION_updateMDS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getMDS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getNumeric = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getScanner = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getRT_SA = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getEnumeration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getPM_Store = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getObjectAttrs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_setTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_connect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
}
public boolean updateMDS(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
public void getState(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.IState state, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
public void getMDS(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.objects.IMDS mds, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException;
public void getNumeric(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.INumeric> nums, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException;
public void getScanner(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IScanner> scanners, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException;
public void getRT_SA(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IRT_SA> rts, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException;
public void getEnumeration(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IEnumeration> nums, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException;
public void getPM_Store(es.libresoft.openhealth.android.aidl.IAgent agent, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Store> nums, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException;
public void getObjectAttrs(es.libresoft.openhealth.android.aidl.types.objects.IDIMClass obj, java.util.List<es.libresoft.openhealth.android.aidl.types.IAttribute> attrs, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException;
public boolean setTime(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
public void connect(es.libresoft.openhealth.android.aidl.IAgent agent) throws android.os.RemoteException;
public boolean disconnect(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
}
