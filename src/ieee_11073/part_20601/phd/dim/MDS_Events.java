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
package ieee_11073.part_20601.phd.dim;

import ieee_11073.part_20601.asn1.ConfigReport;
import ieee_11073.part_20601.asn1.ConfigReportRsp;
import ieee_11073.part_20601.asn1.ScanReportInfoFixed;
import ieee_11073.part_20601.asn1.ScanReportInfoMPFixed;
import ieee_11073.part_20601.asn1.ScanReportInfoMPVar;
import ieee_11073.part_20601.asn1.ScanReportInfoVar;

public interface MDS_Events {
	/**
	 * This event is sent by the agent during the configuring state of startup if the manager does not already
	 * know the agent’s configuration from past associations. The event provides static information about the
	 * supported measurement capabilities of the agent.
	 */
	public ConfigReportRsp MDS_Configuration_Event(ConfigReport config);

	/**
	 * This event provides dynamic data (typically measurements) from the agent for some or all of the
	 * objects that the agent supports. Data for reported objects are reported using a generic attribute list
	 * variable format (see 7.4.5 for details on event report formats). The event is triggered by an MDS-Data-
	 * Request from the manager system, or it is sent as an unsolicited message by the agent. For agents that
	 * support manager-initiated measurement data transmission, refer to 8.9.3.3.3 for information on
	 * controlling the activation and/or period of the data transmission. For agents that do not support
	 * manager-initiated measurement data transmission, refer to 8.9.3.3.2 for information on the limited
	 * control a manager can assert.
	 */
	public void MDS_Dynamic_Data_Update_Var(ScanReportInfoVar info);

	/**
	 * This event provides dynamic data (typically measurements) from the agent for some or all of the
	 * metric objects or the MDS object that the agent supports. Data are reported in the fixed format defined
	 * by the Attribute-Value-Map attribute for reported metric objects or the MDS object (see 7.4.5 for
	 * details on event report formats). The event is triggered by an MDS-Data-Request from the manager
	 * system (i.e., a manager-initiated measurement data transmission), or it is sent as an unsolicited
	 * message by the agent (i.e., an agent-initiated measurement data transmission). For agents that support
	 * manager-initiated measurement data transmission, refer to 8.9.3.3.3 for information on controlling the
	 * activation and/or period of the data transmission. For agents that do not support manager-initiated
	 * measurement data transmission, refer to 8.9.3.3.2 for information on the limited control a manager can
	 * assert.
	 */
	public void MDS_Dynamic_Data_Update_Fixed(ScanReportInfoFixed info);

	/**
	 * This is the same as MDS-Dynamic-Data-Update-Var, but allows inclusion of data from multiple persons.
	 */
	public void MDS_Dynamic_Data_Update_MP_Var(ScanReportInfoMPVar info);

	/**
	 * This is the same as MDS-Dynamic-Data-Update-Fixed, but allows inclusion of data from multiple persons.
	 */
	public void MDS_Dynamic_Data_Update_MP_Fixed(ScanReportInfoMPFixed info);
}
