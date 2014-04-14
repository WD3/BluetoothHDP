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

import ieee_11073.part_20601.asn1.SegmentDataEvent;
import ieee_11073.part_20601.asn1.SegmentDataResult;

public interface PM_Store_Events {
		/**
		 * This event sends data stored in the Fixed-Segment-Data of a PM-segment from the agent to
		 * the manager. The event is triggered by the manager by the Trig-Segment-Data-Xfer method.
		 * Once the data transfer is triggered, the agent sends Segment-Data-Event messages until
		 * the complete Fixed-Segment-Data is transferred or the transfer is aborted by the manager
		 * or agent. See Transfer PM-segment content in 8.9.3.4.2 for a full description.
		 * It is encouraged to place as many segment entries contained in a Segment-Data-Event as
		 * possible to reduce the number of messages required for the transfer of the segment.
		 * Support for the event by the agent is mandatory if the agent supports PM-store objects.
		 */
		public SegmentDataResult Segment_Data_Event (SegmentDataEvent sde);
}
