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
package es.libresoft.openhealth.events;

public interface EventType {

	/* A condition asserted by a low layer of software though a well-defined API							*/
	public static final int IND_TRANS_CONN						=	1;		/* Transport Connection			*/
	public static final int IND_TRANS_DESC						=	2;		/* Transport Desconnection		*/
	public static final int IND_TIMEOUT							=	3;		/* Timeout						*/

	/* A Request from the application software interfacing with the state machine							*/
	public static final int REQ_ASSOC_REL						= 	4;		/* Association release request	*/
	public static final int REQ_ASSOC_ABORT						= 	5;		/* Association abort request	*/
	

	public static final int REC_CORRUPTED_APDU					= 	6;		/* Received corrupted APPDU */

	public static final int REC_APDU_OVERFLOW					= 	7;		/* Received an APDU that exceeds maximum size */
	/* By Hongqiao Gao*/
	public static final int REQ_ASSOC							=	8;		/*Association request			*/
	
	/* Application defined events*/
	public static final int REQ_GET_PM_STORE					=	500;
	public static final int REQ_SET								=	501;
	public static final int REQ_MDS								=	502;
	public static final int REQ_GET_SEGMENT_INFO				=	503;
	public static final int REQ_TRIG_SEGMENT_DATA_XFER			=	504;
	public static final int REQ_SET_TIME						=	505;
	
	/* By Hongqiao Gao*/
	public static final int NOTI_MEASURE						=	510;

}
