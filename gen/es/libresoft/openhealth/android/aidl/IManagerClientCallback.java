/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\workspace\\BluetoothHDPActivity\\src\\es\\libresoft\\openhealth\\android\\aidl\\IManagerClientCallback.aidl
 */
package es.libresoft.openhealth.android.aidl;
public interface IManagerClientCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements es.libresoft.openhealth.android.aidl.IManagerClientCallback
{
private static final java.lang.String DESCRIPTOR = "es.libresoft.openhealth.android.aidl.IManagerClientCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an es.libresoft.openhealth.android.aidl.IManagerClientCallback interface,
 * generating a proxy if needed.
 */
public static es.libresoft.openhealth.android.aidl.IManagerClientCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof es.libresoft.openhealth.android.aidl.IManagerClientCallback))) {
return ((es.libresoft.openhealth.android.aidl.IManagerClientCallback)iin);
}
return new es.libresoft.openhealth.android.aidl.IManagerClientCallback.Stub.Proxy(obj);
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
case TRANSACTION_agentPlugged:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.agentPlugged(_arg0);
return true;
}
case TRANSACTION_agentUnplugged:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.agentUnplugged(_arg0);
return true;
}
case TRANSACTION_agentChangeState:
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
if ((0!=data.readInt())) {
_arg1 = es.libresoft.openhealth.android.aidl.IState.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.agentChangeState(_arg0, _arg1);
return true;
}
case TRANSACTION_error:
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
if ((0!=data.readInt())) {
_arg1 = es.libresoft.openhealth.android.aidl.types.IError.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.error(_arg0, _arg1);
return true;
}
case TRANSACTION_agentNewMeassure:
{
data.enforceInterface(DESCRIPTOR);
es.libresoft.openhealth.android.aidl.IAgent _arg0;
if ((0!=data.readInt())) {
_arg0 = es.libresoft.openhealth.android.aidl.IAgent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
es.libresoft.openhealth.android.aidl.types.measures.IAgentMetric _arg1;
if ((0!=data.readInt())) {
_arg1 = es.libresoft.openhealth.android.aidl.types.measures.IAgentMetric.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.agentNewMeassure(_arg0, _arg1);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements es.libresoft.openhealth.android.aidl.IManagerClientCallback
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
/**
	 * Called when agent is available for to connect with the manager.
	 */
@Override public void agentPlugged(es.libresoft.openhealth.android.aidl.IAgent agent) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_agentPlugged, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/**
	 * Called when agent releases the association with the manager.
	 */
@Override public void agentUnplugged(es.libresoft.openhealth.android.aidl.IAgent agent) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_agentUnplugged, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/**
	 * Called when the state of the agent changes.
	 */
@Override public void agentChangeState(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.IState state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((state!=null)) {
_data.writeInt(1);
state.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_agentChangeState, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/**
	 * Notifies asynchronous error in the agent
	 */
@Override public void error(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((error!=null)) {
_data.writeInt(1);
error.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_error, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/**
	 * Notifies new meassures that are receive in the agent
	 */
@Override public void agentNewMeassure(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.measures.IAgentMetric metric) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((agent!=null)) {
_data.writeInt(1);
agent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
if ((metric!=null)) {
_data.writeInt(1);
metric.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_agentNewMeassure, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_agentPlugged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_agentUnplugged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_agentChangeState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_error = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_agentNewMeassure = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
/**
	 * Called when agent is available for to connect with the manager.
	 */
public void agentPlugged(es.libresoft.openhealth.android.aidl.IAgent agent) throws android.os.RemoteException;
/**
	 * Called when agent releases the association with the manager.
	 */
public void agentUnplugged(es.libresoft.openhealth.android.aidl.IAgent agent) throws android.os.RemoteException;
/**
	 * Called when the state of the agent changes.
	 */
public void agentChangeState(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.IState state) throws android.os.RemoteException;
/**
	 * Notifies asynchronous error in the agent
	 */
public void error(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.IError error) throws android.os.RemoteException;
/**
	 * Notifies new meassures that are receive in the agent
	 */
public void agentNewMeassure(es.libresoft.openhealth.android.aidl.IAgent agent, es.libresoft.openhealth.android.aidl.types.measures.IAgentMetric metric) throws android.os.RemoteException;
}
