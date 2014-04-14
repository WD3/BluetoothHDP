/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\BluetoothHDPActivity\\src\\es\\libresoft\\openhealth\\android\\aidl\\IPMStoreService.aidl
 */
package es.libresoft.openhealth.android.aidl;
public interface IPMStoreService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements es.libresoft.openhealth.android.aidl.IPMStoreService
{
private static final java.lang.String DESCRIPTOR = "es.libresoft.openhealth.android.aidl.IPMStoreService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an es.libresoft.openhealth.android.aidl.IPMStoreService interface,
 * generating a proxy if needed.
 */
public static es.libresoft.openhealth.android.aidl.IPMStoreService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof es.libresoft.openhealth.android.aidl.IPMStoreService))) {
return ((es.libresoft.openhealth.android.aidl.IPMStoreService)iin);
}
return new es.libresoft.openhealth.android.aidl.IPMStoreService.Stub.Proxy(obj);
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
case TRANSACTION_updatePMStore:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.types.objects.IPM_Store _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.types.objects.IPM_Store.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.types.IError _arg1;
_arg1 = new es.libresoft.openhealth.android.aidl.types.IError();
boolean _result = this.updatePMStore(_arg0, _arg1);
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
case TRANSACTION_getAllPMSegments:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.types.objects.IPM_Store _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.types.objects.IPM_Store.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment> _arg1;
_arg1 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment>();
es.libresoft.openhealth.android.aidl.types.IError _arg2;
_arg2 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getAllPMSegments(_arg0, _arg1, _arg2);
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
case TRANSACTION_getPMSegmentsTimeRange:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.types.objects.IPM_Store _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.types.objects.IPM_Store.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
long _arg1;
_arg1 = data.readLong();
long _arg2;
_arg2 = data.readLong();
java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Store> _arg3;
_arg3 = new java.util.ArrayList<es.libresoft.openhealth.android.aidl.types.objects.IPM_Store>();
es.libresoft.openhealth.android.aidl.types.IError _arg4;
_arg4 = new es.libresoft.openhealth.android.aidl.types.IError();
this.getPMSegmentsTimeRange(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
reply.writeTypedList(_arg3);
if ((_arg4!=null)) {
reply.writeInt(1);
_arg4.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_startPMSegmentTransfer:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.types.IError _arg1;
_arg1 = new es.libresoft.openhealth.android.aidl.types.IError();
boolean _result = this.startPMSegmentTransfer(_arg0, _arg1);
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
private static class Proxy implements es.libresoft.openhealth.android.aidl.IPMStoreService
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
@Override public boolean updatePMStore(es.libresoft.openhealth.android.aidl.types.objects.IPM_Store store, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((store!=null)) {
_data.writeInt(1);
store.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updatePMStore, _data, _reply, 0);
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
@Override public void getAllPMSegments(es.libresoft.openhealth.android.aidl.types.objects.IPM_Store store, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment> segments, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((store!=null)) {
_data.writeInt(1);
store.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getAllPMSegments, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(segments, es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment.CREATOR);
if ((0!=_reply.readInt())) {
err.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getPMSegmentsTimeRange(es.libresoft.openhealth.android.aidl.types.objects.IPM_Store store, long startTime, long endTime, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Store> stores, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((store!=null)) {
_data.writeInt(1);
store.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeLong(startTime);
_data.writeLong(endTime);
mRemote.transact(Stub.TRANSACTION_getPMSegmentsTimeRange, _data, _reply, 0);
_reply.readException();
_reply.readTypedList(stores, es.libresoft.openhealth.android.aidl.types.objects.IPM_Store.CREATOR);
if ((0!=_reply.readInt())) {
err.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean startPMSegmentTransfer(es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment segment, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((segment!=null)) {
_data.writeInt(1);
segment.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_startPMSegmentTransfer, _data, _reply, 0);
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
static final int TRANSACTION_updatePMStore = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getAllPMSegments = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getPMSegmentsTimeRange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_startPMSegmentTransfer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public boolean updatePMStore(es.libresoft.openhealth.android.aidl.types.objects.IPM_Store store, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
public void getAllPMSegments(es.libresoft.openhealth.android.aidl.types.objects.IPM_Store store, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment> segments, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
public void getPMSegmentsTimeRange(es.libresoft.openhealth.android.aidl.types.objects.IPM_Store store, long startTime, long endTime, java.util.List<es.libresoft.openhealth.android.aidl.types.objects.IPM_Store> stores, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
public boolean startPMSegmentTransfer(es.libresoft.openhealth.android.aidl.types.objects.IPM_Segment segment, es.libresoft.openhealth.android.aidl.types.IError err) throws android.os.RemoteException;
}
