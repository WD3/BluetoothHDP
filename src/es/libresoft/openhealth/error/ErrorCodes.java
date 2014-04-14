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

package es.libresoft.openhealth.error;

/**
 * This class defines the used error codes.
 * */
public final class ErrorCodes {

	/** Indicates no error */
	public static final int NO_ERROR 				=	0;

	/** Generic TIMEOUT */
	public static final int TIMEOUT					=	100;
	/** Timeout in association */
	public static final int TIMEOUT_ASSOC			=	101;
	/** Timeout in configuration */
	public static final int TIMEOUT_CONFIG			=	102;
	/** Timeout in association release */
	public static final int TIMEOUT_ASSOC_REL		=	103;
	/** Timeout in MDS confirmed action */
	public static final int TIMEOUT_MDS_CONF_ACION	=	104;
	/** Timeout in MDS confirmed event report */
	public static final int TIMEOUT_MDS_CONF_EV_REP	=	105;
	/** Timeout in MDS Get */
	public static final int TIMEOUT_MDS_GET			=	106;
	/** Timeout in MDS Set */
	public static final int TIMEOUT_MDS_CONF_SET	=	107;
	/** Timeout in MDS inter-service */
	public static final int TIMEOUT_MDS_SPECIAL		=	108;

	/** Timeout in PM-Store confirmed action */
	public static final int TIMEOUT_PM_CONF_ACION	=	109;
	/** Timeout in PM-Store confirmed event report */
	public static final int TIMEOUT_PM_CONF_EV_REP	=	110;
	/** Timeout in PM-Store Get */
	public static final int TIMEOUT_PM_GET			=	111;
	/** Timeout in PM-Store Set */
	public static final int TIMEOUT_PM_CONF_SET		=	112;
	/** Timeout in PM-Store (end of segment) */
	public static final int TIMEOUT_PM_SPECIAL		=	113;
	/** Timeout in Scanner Set */
	public static final int TIMEOUT_SCN_CONF_SET	=	114;
	/** Timeout in Scanner confirmed event report */
	public static final int TIMEOUT_SCN_CONF_EV_REP	=	115;

	/** Unexpected error*/
	public static final int UNEXPECTED_ERROR		=	200;
	/** Unknown agent error*/
	public static final int UNKNOWN_AGENT			=	201;
	/** Invalid attribute */
	public static final int INVALID_ATTRIBUTE		=	202;
	/** Unknown object */
	public static final int UNKNOWN_OBJECT			=	203;
	/** Invalid arguments */
	public static final int INVALID_ARGUMENTS		=	204;
	/** Unknown PM_Store */
	public static final int UNKNOWN_PMSTORE			=	205;
	/** Association error */
	public static final int ASSOCIATION_ERROR		=	206;
	/** Invalid action */
	public static final int INVALID_ACTION			=	207;
}
