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

package es.libresoft.openhealth.android;

import ieee_11073.part_20601.asn1.AbsoluteTime;
import ieee_11073.part_20601.phd.dim.Attribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import es.libresoft.mdnf.FloatType;
import es.libresoft.mdnf.SFloatType;
import es.libresoft.openhealth.android.aidl.types.IAttribute;
import es.libresoft.openhealth.android.aidl.types.measures.IAgentMetric;
import es.libresoft.openhealth.android.aidl.types.measures.IDateMeasure;
import es.libresoft.openhealth.android.aidl.types.measures.IMeasure;
import es.libresoft.openhealth.android.aidl.types.measures.IMeasureArray;
import es.libresoft.openhealth.android.aidl.types.measures.IValueMeasure;
import es.libresoft.openhealth.events.MeasureReporter;
import es.libresoft.openhealth.logging.Logging;

public class AndroidMeasureReporter implements MeasureReporter{

	IAgentMetric metric = new IAgentMetric();

	private IMeasure getMeasure(int mType, Object data) {
		if (data instanceof SFloatType){
			SFloatType sf = (SFloatType)data;
			return new IValueMeasure(mType,sf.getExponent(),sf.getMagnitude(), sf.doubleValueRepresentation(), sf.toString());
		}else if (data instanceof FloatType){
			FloatType sf = (FloatType)data;
			return new IValueMeasure(mType,sf.getExponent(),sf.getMagnitude(), sf.doubleValueRepresentation(), sf.toString());
		}else if (data instanceof Date){
			Date timestamp = (Date)data;
			return new IDateMeasure(mType,timestamp.getTime());
		}else if (data instanceof List<?>) {
			ArrayList<IMeasure> values = new ArrayList<IMeasure>();
			List<?> list = (List<?>) data;
			Iterator<?> it = list.iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				IMeasure m = getMeasure(mType, obj);
				if (m != null)
					values.add(m);
			}
			return new IMeasureArray(mType, values);
		}else if (data instanceof AbsoluteTime){
			/*
			 * The absolute time data type specifies the time of day with a resolution of 1/100
			 * of a second. The hour field shall be reported in 24-hr time notion (i.e., from 0 to 23).
			 * The values in the structure shall be encoded using binary coded decimal (i.e., 4-bit
			 * nibbles). For example, the year 1996 shall be represented by the hexadecimal value 0x19
			 * in the century field and the hexadecimal value 0x96 in the year field. This format is
			 * easily converted to character- or integer-based representations. See AbsoluteTime
			 * structure for details.
			 */
			//final String rawDate = ASN1_Tools.getHexString(data);
			AbsoluteTime absTime = (AbsoluteTime)data;
			String century = Integer.toString(absTime.getCentury().getValue(), 16);
			String year = Integer.toString(absTime.getYear().getValue(), 16);
			String month = Integer.toString(absTime.getMonth().getValue(), 16);
			String day = Integer.toString(absTime.getDay().getValue(), 16);
			String hour = Integer.toString(absTime.getHour().getValue(), 16);
			String minute = Integer.toString(absTime.getMinute().getValue(), 16);
			String second = Integer.toString(absTime.getSecond().getValue(), 16);
			String secFractions = Integer.toString(absTime.getSec_fractions().getValue(), 16);
			if (year.length() == 1)
				year = "0" + year;
			String yyyy = century.concat(year);
			//final String source = absTime.getCentury().getValue() + absTime.getYear().getValue() + "/" + /*century + year(first 2Bytes)*/ 
			final String source = yyyy + "/" + /*century + year(first 2Bytes)*/
					month + "/" +   /* month next 2B*/
					day + " " +   /* day next 2B */
					hour + ":" +  /* hour next 2B */
					minute + ":" + /* minute next 2B */
					second + ":" + /* second next 2B */
					secFractions; /* frac-sec last 2B */
			SimpleDateFormat sdf =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS");
			Date timestamp;
			try {
				timestamp = sdf.parse(source);
				return new IDateMeasure(mType, timestamp.getTime());
			} catch (ParseException e) {
				Logging.error("An AbsoluteTime measure " + mType + " won't be reported to the manager.");
				e.printStackTrace();
				return null;
			}
		}

		Logging.error("The unknown data type " + mType + " won't be reported to the manager.");
		return null;
	}

	@Override
	public void addMeasure(int mType, Object data) {
		IMeasure m = getMeasure(mType, data);
		if (m != null)
			metric.addMeasure(m);
	}

	@Override
	public List getMeasures(){
		return metric.getMeasures();
	}

	public List getAttributes(){
		return metric.getAttributes();
	}

	@Override
	public void clearMeasures() {
		metric.clearMeasures();
	}
	public void clearAttributes() {
		metric.clearAttributes();
	}

	@Override
	public void set_attribute(Attribute att) {
		IAttribute iAtt = new IAttribute();
		if (IAttrFactory.getParcelableAttribute(att, iAtt)) {
			metric.addAttribute(iAtt);
		}
	}

	public IAgentMetric getMetric() {
		return metric;
	}

}
