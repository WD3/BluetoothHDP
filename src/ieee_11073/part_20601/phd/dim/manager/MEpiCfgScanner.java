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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import es.libresoft.openhealth.Agent;
import es.libresoft.openhealth.error.ErrorCodes;
import es.libresoft.openhealth.events.Event;
import es.libresoft.openhealth.events.InternalEventReporter;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.events.MeasureReporterFactory;
import es.libresoft.openhealth.events.MeasureReporterUtils;
import es.libresoft.openhealth.events.application.ExternalEvent;
import es.libresoft.openhealth.events.application.SetEventData;
import es.libresoft.openhealth.logging.Logging;
import es.libresoft.openhealth.messages.MessageFactory;
import es.libresoft.openhealth.utils.ASN1_Tools;
import es.libresoft.openhealth.utils.ASN1_Values;
import es.libresoft.openhealth.utils.RawDataExtractor;

import ieee_11073.part_10101.Nomenclature;
import ieee_11073.part_20601.asn1.ApduType;
import ieee_11073.part_20601.asn1.AttrValMap;
import ieee_11073.part_20601.asn1.AttrValMapEntry;
import ieee_11073.part_20601.asn1.DataApdu;
import ieee_11073.part_20601.asn1.EventReportArgumentSimple;
import ieee_11073.part_20601.asn1.HandleAttrValMap;
import ieee_11073.part_20601.asn1.HandleAttrValMapEntry;
import ieee_11073.part_20601.asn1.ObservationScanGrouped;
import ieee_11073.part_20601.asn1.ScanReportInfoGrouped;
import ieee_11073.part_20601.phd.dim.Attribute;
import ieee_11073.part_20601.phd.dim.DIM;
import ieee_11073.part_20601.phd.dim.EpiCfgScanner;
import ieee_11073.part_20601.phd.dim.InvalidAttributeException;

public class MEpiCfgScanner extends EpiCfgScanner {

	public MEpiCfgScanner(Hashtable<Integer, Attribute> attributeList)
			throws InvalidAttributeException {
		super(attributeList);
	}

	@Override
	protected void checkAttributes(
			Hashtable<Integer, Attribute> attributes)
			throws InvalidAttributeException {

	}

	@Override
	public void Unbuf_Scan_Report_Fixed(EventReportArgumentSimple event) {
		Logging.debug("TODO: implement Unbuf_Scan_Report_Fixed");
	}

	@Override
	public void Unbuf_Scan_Report_Grouped(EventReportArgumentSimple event) {
		try {
			ScanReportInfoGrouped srig = ASN1_Tools.decodeData(event.getEvent_info(),
									ScanReportInfoGrouped.class,
									getMDS().getDeviceConf().getEncondigRules());
			Logging.debug("Grouped scan report #" + srig.getScan_report_no());
			// TODO: use the report number to detect missing packets

			Iterator<ObservationScanGrouped> i= srig.getObs_scan_grouped().iterator();
			Attribute ValMapAtt = getAttribute(Nomenclature.MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP);
			if (ValMapAtt == null) {
				Logging.error("Warninig: received Grouped Scan Report but Scan-Handle-Attr-Val-Map is not found");
				return;
			}
			HandleAttrValMap havm = (HandleAttrValMap) ValMapAtt.getAttributeType();

			ObservationScanGrouped obs;
			while (i.hasNext()) {
				obs = i.next();

				Iterator<HandleAttrValMapEntry> it = havm.getValue().iterator();
				RawDataExtractor de = new RawDataExtractor(obs.getValue());

				while (it.hasNext()){
					HandleAttrValMapEntry hattr = it.next();
					AttrValMap avm = hattr.getAttr_val_map();
					DIM elem = getMDS().getObject(hattr.getObj_handle());
					Iterator<AttrValMapEntry> vmit = avm.getValue().iterator();

					MeasureReporter mr = MeasureReporterFactory.getDefaultMeasureReporter();
					MeasureReporterUtils.addAttributesToReport(mr,elem);

					while (vmit.hasNext()){
						AttrValMapEntry vme = vmit.next();
						int attrId = vme.getAttribute_id().getValue().getValue();
						int length = vme.getAttribute_len();
						try {
							mr.addMeasure(attrId, RawDataExtractor.decodeRawData(attrId,
									de.getData(length), getMDS().getDeviceConf().getEncondigRules()));
						}catch(Exception e){
							Logging.error("Error: Can not get attribute " + attrId);
							e.printStackTrace();
						}
					}

					InternalEventReporter.receivedMeasure((Agent) getMDS().getDevice(), mr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Unbuf_Scan_Report_MP_Fixed(EventReportArgumentSimple event) {
		Logging.debug("TODO: implement Unbuf_Scan_Report_MP_Fixed");
	}

	@Override
	public void Unbuf_Scan_Report_MP_Grouped(EventReportArgumentSimple event) {
		Logging.debug("TODO: implement Unbuf_Scan_Report_MP_Grouped");
	}

	@Override
	public void Unbuf_Scan_Report_MP_Var(EventReportArgumentSimple event) {
		Logging.debug("TODO: implement Unbuf_Scan_Report_Fixed");
	}

	@Override
	public void Unbuf_Scan_Report_Var(EventReportArgumentSimple event) {
		Logging.debug("TODO: implement Unbuf_Scan_Report_Var");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void SET(Event event, Attribute attr) {
		HashMap<Attribute, Integer> attrs = new HashMap<Attribute, Integer>();
		ExternalEvent<Boolean, SetEventData> setEvent = null;
		try {
			setEvent = (ExternalEvent<Boolean, SetEventData>) event;
		} catch (ClassCastException e) {

		}

		attrs.put(attr, ASN1_Values.MOD_OP_REPLACE);
		DataApdu data = MessageFactory.PrstRoivCmipSet(this, attrs);
		ApduType apdu = MessageFactory.composeApdu(data, getMDS().getDeviceConf());
		getMDS().getStateHandler().send(apdu);
		addAttribute(attr);
		setEvent.processed(true, ErrorCodes.NO_ERROR);
	}
}
