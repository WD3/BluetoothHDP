/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
package ieee_11073.part_20601.phd.dim.manager;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.ActionResultSimple;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.AttrValMap;
import ieee_11073.part_20601.asn1.AttrValMapEntry;
import ieee_11073.part_20601.asn1.AttributeList;
import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.asn1.GetResultSimple;
import ieee_11073.part_20601.asn1.HANDLE;
import ieee_11073.part_20601.asn1.InstNumber;
import ieee_11073.part_20601.asn1.InvokeIDType;
import ieee_11073.part_20601.asn1.OID_Type;
import ieee_11073.part_20601.asn1.PmSegmentEntryMap;
import ieee_11073.part_20601.asn1.SegmDataEventDescr;
import ieee_11073.part_20601.asn1.SegmEntryElem;
import ieee_11073.part_20601.asn1.SegmEntryElemList;
import ieee_11073.part_20601.asn1.SegmEvtStatus;
import ieee_11073.part_20601.asn1.SegmSelection;
import ieee_11073.part_20601.asn1.SegmentDataEvent;
import ieee_11073.part_20601.asn1.SegmentDataResult;
import ieee_11073.part_20601.asn1.SegmentInfo;
import ieee_11073.part_20601.asn1.SegmentInfoList;
import ieee_11073.part_20601.asn1.TYPE;
import ieee_11073.part_20601.asn1.TrigSegmDataXferReq;
import ieee_11073.part_20601.asn1.TrigSegmDataXferRsp;
import ieee_11073.part_20601.asn1.TrigSegmXferRsp;
import ieee_11073.part_20601.phd.dim.Attribute;
import ieee_11073.part_20601.phd.dim.DimTimeOut;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;
import ieee_11073.part_20601.phd.dim.Numeric;
import ieee_11073.part_20601.phd.dim.PM_Segment;
import ieee_11073.part_20601.phd.dim.PM_Store;
import ieee_11073.part_20601.phd.dim.TimeOut;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.bn.types.BitString;

import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.application.ExternalEvent;
import es.libresoft.openhealth.events.application.GetPmSegmentEventData;
import es.libresoft.openhealth.events.application.GetPmStoreEventData;
import es.libresoft.openhealth.events.application.TrigPMSegmentXferEventData;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;
import es.libresoft.openhealth.utils.DIM_Tools;
import es.libresoft.openhealth.utils.RawDataExtractor;

public class MPM_Store extends PM_Store {

	public MPM_Store(Hashtable<Integer, Attribute> attributeList)
			throws InvalidAttributeException {
		super(attributeList);
	}

	@Override
	public void Clear_Segments(SegmSelection ss) {

	}

	@Override
	public void Get_Segment_Info(ExternalEvent<List<PM_Segment>, GetPmSegmentEventData> event, SegmSelection ss) {
		try {
			DataApdu data = MessageFactory.PrstRoivCmipAction(this, ss);
			ApduType apdu = MessageFactory.composeApdu(data, getMDS().getDeviceConf());
			InvokeIDType invokeId = data.getInvoke_id();
			getMDS().getStateHandler().send(apdu);

			DimTimeOut to = new DimTimeOut(TimeOut.PM_STORE_TO_CA, invokeId.getValue(), getMDS().getStateHandler()) {

				@SuppressWarnings("unchecked")
				@Override
				public void procResponse(DataApdu data) {

					ExternalEvent<List<PM_Segment>, GetPmSegmentEventData> event = (ExternalEvent<List<PM_Segment>, GetPmSegmentEventData>) getEvent();

					if (!data.getMessage().isRors_cmip_confirmed_actionSelected()) {
						Logging.debug("Error: Unexpected response format");
						event.processed(null, ErrorCodes.UNEXPECTED_ERROR);
						return;
					}

					ActionResultSimple ars = data.getMessage().getRors_cmip_confirmed_action();
					OID_Type oid = ars.getAction_type();
					if (Nomenclature.MDC_ACT_SEG_GET_INFO != oid.getValue().getValue().intValue()) {
						Logging.debug("Error: Unexpected response format");
						event.processed(null, ErrorCodes.UNEXPECTED_ERROR);
						return;
					}

					try {
						SegmentInfoList sil = ASN1_Tools.decodeData(ars.getAction_info_args(),
													SegmentInfoList.class,
													getMDS().getDeviceConf().getEncondigRules());
						Iterator<SegmentInfo> i = sil.getValue().iterator();
						while (i.hasNext()) {
							SegmentInfo si = i.next();
							InstNumber in = si.getSeg_inst_no();
							AttributeList al = si.getSeg_info();

							Hashtable<Integer, Attribute> attribs;
							attribs = getAttributes(al, getMDS().getDeviceConf().getEncondigRules());
							MPM_Segment pm_segment = new MPM_Segment(attribs);
							addPM_Segment(pm_segment);
							Logging.debug("Got PM_Segment " + in.getValue().intValue());
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					List<PM_Segment> segments = new ArrayList<PM_Segment>(MPM_Store.this.getSegments());
					event.processed(segments, ErrorCodes.NO_ERROR);
				}

			};

			to.setEvent(event);
			to.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Trig_Segment_Data_Xfer (ExternalEvent<Boolean, TrigPMSegmentXferEventData> event, TrigSegmDataXferReq tsdx) {
		DataApdu data = MessageFactory.PrstRoivCmipConfirmedAction(this, tsdx);
		ApduType apdu = MessageFactory.composeApdu(data, getMDS().getDeviceConf());
		InvokeIDType invokeId = data.getInvoke_id();
		getMDS().getStateHandler().send(apdu);

		DimTimeOut to = new DimTimeOut(TimeOut.PM_STORE_TO_CA, invokeId.getValue(), getMDS().getStateHandler()) {

			@SuppressWarnings("unchecked")
			@Override
			public void procResponse(DataApdu data) {

				ExternalEvent<Boolean, TrigPMSegmentXferEventData> event = (ExternalEvent<Boolean, TrigPMSegmentXferEventData>) getEvent();

				if (!data.getMessage().isRors_cmip_confirmed_actionSelected()) {
					Logging.error("Error: Unexpected response format");
					event.processed(false, ErrorCodes.UNEXPECTED_ERROR);
					return;
				}

				ActionResultSimple ars = data.getMessage().getRors_cmip_confirmed_action();
				OID_Type oid = ars.getAction_type();
				if (Nomenclature.MDC_ACT_SEG_TRIG_XFER != oid.getValue().getValue().intValue()) {
					Logging.error("Error: Unexpected response format");
					event.processed(false, ErrorCodes.UNEXPECTED_ERROR);
					return;
				}

				try {
					TrigSegmDataXferRsp rsp = ASN1_Tools.decodeData(ars.getAction_info_args(),
													TrigSegmDataXferRsp.class,
													getMDS().getDeviceConf().getEncondigRules());
					InstNumber in = rsp.getSeg_inst_no();
					TrigSegmXferRsp tsxr = rsp.getTrig_segm_xfer_rsp();
					int result = tsxr.getValue().intValue();
					if (result == 0) {
						event.processed(true, ErrorCodes.NO_ERROR);
						return;
					}

					// TODO: Create and set proper error using "result" variable
					event.processed(false, ErrorCodes.UNEXPECTED_ERROR);
					Logging.error("InstNumber " + in.getValue().intValue() + " error " + result);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		//TODO: Add a timeout for data transfer (see 8.9.5.6 and MDC_ATTR_TRANSFER_TIMEOUT att from PMSegment)

		to.setEvent(event);
		to.start();
	}

	public void GET(Event event) {
		try {
			HANDLE handle = (HANDLE) getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getAttributeType();
			DataApdu data = MessageFactory.PrstRoivCmpGet(getMDS(), handle);
			ApduType apdu = MessageFactory.composeApdu(data, getMDS().getDeviceConf()); 
			InvokeIDType invokeId = data.getInvoke_id();
			getMDS().getStateHandler().send(apdu);

			DimTimeOut to = new DimTimeOut(TimeOut.PM_STORE_TO_GET, invokeId.getValue(), getMDS().getStateHandler()) {

				@SuppressWarnings("unchecked")
				@Override
				public void procResponse(DataApdu data) {
					Logging.debug("GOT_PMSOTRE invoke_id " + data.getInvoke_id().getValue().intValue());
					ExternalEvent<Boolean, GetPmStoreEventData> event = null;
					try {
						event = (ExternalEvent<Boolean, GetPmStoreEventData>) this.getEvent();
					} catch (ClassCastException e) {

					}

					if (!data.getMessage().isRors_cmip_getSelected()) {
						Logging.debug("TODO: Unexpected response format");
						if (event != null)
							event.processed(false, ErrorCodes.UNEXPECTED_ERROR);
						return;
					}

					GetResultSimple grs = data.getMessage().getRors_cmip_get();
					HANDLE handle = (HANDLE) getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getAttributeType();
					if (handle == null) {
						Logging.debug("Error: Can't get HANDLE attribute in PM_STORE object");
						if (event != null)
							event.processed(false, ErrorCodes.UNEXPECTED_ERROR);
						return;
					}

					if (grs.getObj_handle().getValue().getValue().intValue() != handle.getValue().getValue().intValue()) {
						Logging.debug("TODO: Unexpected object handle, should be value " +
																				handle.getValue().getValue().intValue());
						if (event != null)
							event.processed(false, ErrorCodes.UNEXPECTED_ERROR);
						return;
					}

					try {
						Hashtable<Integer, Attribute> attribs;
						attribs = getAttributes(grs.getAttribute_list(), getMDS().getDeviceConf().getEncondigRules());
						Iterator<Integer> i = attribs.keySet().iterator();
						while (i.hasNext()) {
							Attribute attr = attribs.get(i.next());
							if (getAttribute(attr.getAttributeID()) != null) {
								addAttribute(attr);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (event != null)
						event.processed(true, ErrorCodes.NO_ERROR);
				}

			};

			to.setEvent(event);
			to.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SegmentDataResult createSegmentDataResult(byte[] val, SegmentDataEvent sde) {
		SegmentDataResult sdr = new SegmentDataResult();
		SegmDataEventDescr sded = new SegmDataEventDescr();
		SegmEvtStatus ses = new SegmEvtStatus();
		BitString bs = new BitString();

		sded.setSegm_instance(sde.getSegm_data_event_descr().getSegm_instance());
		sded.setSegm_evt_entry_index(sde.getSegm_data_event_descr().getSegm_evt_entry_index());
		sded.setSegm_evt_entry_count(sde.getSegm_data_event_descr().getSegm_evt_entry_count());

		bs.setValue(val);
		ses.setValue(bs);
		sded.setSegm_evt_status(ses);
		sdr.setSegm_data_event_descr(sded);

		return sdr;
	}

	@Override
	public SegmentDataResult Segment_Data_Event(SegmentDataEvent sde) {
		SegmDataEventDescr sded = sde.getSegm_data_event_descr();
		byte[] status = new byte[2];
		byte mask = 0;

		mask |= ASN1_Values.SEVTSTA_FIRST_ENTRY | ASN1_Values.SEVTSTA_LAST_ENTRY;
		status[0] = (byte) (mask | sde.getSegm_data_event_descr().getSegm_evt_status().getValue().getValue()[0]);
		status[1] = 0;

		//Logging.debug("Segment Number: " + sded.getSegm_instance().getValue().intValue());

		MPM_Segment pmseg = (MPM_Segment) getPM_Segment(sded.getSegm_instance());
		if (pmseg == null) {
			Logging.error("Error: Can't get PM_Segment " + sded.getSegm_instance().getValue());
			/* TODO :Send error reply */
			return null;
		}

		Attribute attr = pmseg.getAttribute(Nomenclature.MDC_ATTR_PM_SEG_MAP);
		if (attr == null) {
			Logging.error("Error: Attribute " +
						DIM_Tools.getAttributeName(Nomenclature.MDC_ATTR_PM_SEG_MAP) + " not defined");
			status[1] = 0 | ASN1_Values.SEVTSTA_MANAGER_ABORT;
			return createSegmentDataResult(status, sde);
		}

		int first = sded.getSegm_evt_entry_index().getValue().intValue();
		int count = sded.getSegm_evt_entry_count().getValue().intValue();
		//Logging.debug("Count of entries in this event: " + count);
		//Logging.debug("Index of the first entry in this event: " + first);

		try {
			SegmEvtStatus ses = sded.getSegm_evt_status();
			String bitstring = ASN1_Tools.getHexString(ses.getValue().getValue());
			//Logging.debug("Segment event status: " + bitstring);

			PmSegmentEntryMap psem = (PmSegmentEntryMap) attr.getAttributeType();

			int bytes = 0x000000FF & psem.getSegm_entry_header().getValue().getValue()[0];
			bytes = (bytes << 8) | psem.getSegm_entry_header().getValue().getValue()[1];
			boolean hasEntries = (bytes != 0);
			int attrId = 0;
			int len = 0;

			if (hasEntries) {
				if ((bytes & ASN1_Values.SEG_ELEM_HDR_ABSOLUTE_TIME) != 0) {
					attrId = Nomenclature.MDC_ATTR_TIME_ABS;
					len = 8; /* AbsoluteTime */
				} else if ((bytes & ASN1_Values.SEG_ELEM_HDR_RELATIVE_TIME) != 0) {
					attrId = Nomenclature.MDC_ATTR_TIME_REL;
					len = 4; /* INT-U32 */
				} else if ((bytes & ASN1_Values.SEG_ELEM_HDR_HIRES_RELATIVE_TIME) != 0) {
					attrId = Nomenclature.MDC_ATTR_TIME_REL_HI_RES;
					len = 8; /* HighResRelativeTime */
				} else {
					Logging.error("Bad entry value: " + bytes);
					status[1] = 0 | ASN1_Values.SEVTSTA_MANAGER_ABORT;
					return createSegmentDataResult(status, sde);
				}
			}

			String eRules = getMDS().getDeviceConf().getEncondigRules();
			RawDataExtractor rde = new RawDataExtractor(sde.getSegm_data_event_entries());
			int j = 0;

			/* Get raw data */
			while (rde.hasMoreData() && (j < count)) {
				SegmEntryElemList seel = psem.getSegm_entry_elem_list();
				Iterator<SegmEntryElem> i = seel.getValue().iterator();
				j++;

				if (hasEntries) {
						RawDataExtractor.decodeRawData(attrId, rde.getData(len), eRules);
				}

				while (i.hasNext()) {
					SegmEntryElem see = i.next();
					OID_Type oid = see.getClass_id();
					TYPE type = see.getMetric_type();
					HANDLE handle = see.getHandle();

					if (type.getCode().getValue().getValue().intValue() == Nomenclature.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD)
						Logging.debug("*Glucose Capillary Whole Blood*");

					/* We can check also type.Nompartition to see if it is set to 2 for metric */
					if (oid.getValue().getValue().intValue() != Nomenclature.MDC_MOC_VMO_METRIC_NU) {
						Logging.error("Error: No metric object received.");
						status[1] = 0 | ASN1_Values.SEVTSTA_MANAGER_ABORT;
						return createSegmentDataResult(status, sde);
					}

					Numeric num = getMDS().getNumeric(handle);
					if (num == null) {
						Logging.error("Error: Invalid numeric received.");
						status[1] = 0 | ASN1_Values.SEVTSTA_MANAGER_ABORT;
						return createSegmentDataResult(status, sde);
					}

					OID_Type unitCode = (OID_Type) num.getAttribute(Nomenclature.MDC_ATTR_UNIT_CODE).getAttributeType();
					if (unitCode == null) {
						Logging.error("Can't get unit code");
					} else {
						switch (unitCode.getValue().getValue().intValue()) {
							case Nomenclature.MDC_DIM_MILLI_G_PER_DL:
								Logging.debug("Units: mg/dl");
								break;
							case Nomenclature.MDC_DIM_MILLI_MOLE_PER_L:
								Logging.debug("Units: mmol/L");
								break;
							default:
								Logging.error("Unknown unit code for the measure " +
															unitCode.getValue().getValue().intValue());
						}
					}

					AttrValMap avmnum = (AttrValMap) num.getAttribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP).getAttributeType();
					Iterator<AttrValMapEntry> ii = avmnum.getValue().iterator();
					while (ii.hasNext()) {
						AttrValMapEntry avme = ii.next();
						int id = avme.getAttribute_id().getValue().getValue().intValue();
						int length = avme.getAttribute_len().intValue();
						RawDataExtractor.decodeRawData(id, rde.getData(length), eRules);
					}
				}
			}

			status[1] = 0 | ASN1_Values.SEVTSTA_MANAGER_CONFIRM;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return createSegmentDataResult(status, sde);
	}
}
