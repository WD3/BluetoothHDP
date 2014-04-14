/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>
Author: Jorge Fernández González <jfernandez@libresoft.es>

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
package ieee_11073.part_10101;

	/*************************************************************************************************
	 * This interface represents nomenclature codes
	 * This annex contains the nomenclature codes used in this standard. They are either copied from
	 * ISO/IEEE 11073-10101 or created for this standard and incorporated into ISO/IEEE 11073-10101.
	 * The format used here follows the one defined in ISO/IEEE 11073-10101.
	 *************************************************************************************************/
public interface Nomenclature {

	/* Partition codes																				 		 */
	public static final int	MDC_PART_UNSPEC						=	0;		/* Unspecified					 */
	public static final int MDC_PART_OBJ						=	1;		/* Object Infrastr.              */
	public static final int MDC_PART_SCADA						=	2;		/* SCADA (Physio IDs             */
	public static final int MDC_PART_EVT						=	3;		/* Alert/Events					 */
	public static final int MDC_PART_DIM						=	4;		/* Dimension                     */
	public static final int MDC_PART_VATTR						=	5;		/* Virtual/Attributes			 */
	public static final int MDC_PART_PGRP						=	6;		/* Param Group ID				 */
	public static final int MDC_PART_SITES						=	7;		/* Body Site Locations			 */
	public static final int MDC_PART_INFRA						=	8;		/* Infrastructure                */
	public static final int MDC_PART_FEF						=	9;		/* File Exchange Format			 */
	public static final int MDC_PART_ECG_EXTN					=	10;		/* ECG Extensions				 */
	public static final int MDC_PART_IDCO_EXTN					=	11;		/* IDCO Extensions				 */
	public static final int MDC_PART_PHD_DM						=	128;	/* Disease Mgmt                  */
	public static final int MDC_PART_PHD_HF						=	129;	/* Health and Fitness            */
	public static final int MDC_PART_PHD_AI						=	130;	/* Aging Independently           */
	public static final int MDC_PART_RET_CODE					=	255;	/* Return Codes                  */
	public static final int MDC_PART_EXT_NOM					=	256;	/* Ext. Nomenclature             */
	public static final int MDC_PART_PVT						=	1024;	/* Private						 */

	/*************************************************************************************************
	* From Object Infrastructure (MDC_PART_OBJ)
	**************************************************************************************************/
	public static final int MDC_MOC_VMO_METRIC					=	4;		/*                               */
	public static final int MDC_MOC_VMO_METRIC_ENUM				=	5;		/*                               */
	public static final int MDC_MOC_VMO_METRIC_NU				=	6;		/*                               */
	public static final int MDC_MOC_VMO_METRIC_SA_RT			=	9; 		/*                               */
	public static final int MDC_MOC_SCAN						=	16;		/*                               */
	public static final int MDC_MOC_SCAN_CFG					=	17;		/*                               */
	public static final int MDC_MOC_SCAN_CFG_EPI				=	18;		/*                               */
	public static final int MDC_MOC_SCAN_CFG_PERI				=	19;		/*                               */
	public static final int MDC_MOC_VMS_MDS_SIMP				=	37;		/*                               */
	public static final int MDC_MOC_VMO_PMSTORE					=	61;		/*                               */
	public static final int MDC_MOC_PM_SEGMENT					=	62;		/*                               */
	public static final int MDC_ATTR_AL_OP_STAT					=	2310;	/*                               */
	public static final int MDC_ATTR_CONFIRM_MODE				=	2323;	/*                               */
	public static final int MDC_ATTR_CONFIRM_TIMEOUT			=	2324;	/*                               */
	public static final int MDC_ATTR_ID_HANDLE					=	2337;	/*                               */
    public static final int MDC_ATTR_ID_INSTNO 					=	2338;	/*                               */
    public static final int MDC_ATTR_ID_LABEL_STRING			=	2343;	/*                               */
    public static final int MDC_ATTR_ID_MODEL					=	2344;	/*                               */
    public static final int MDC_ATTR_ID_PHYSIO					=	2347;	/*                               */
    public static final int MDC_ATTR_ID_PROD_SPECN				=	2349;	/*                               */
    public static final int MDC_ATTR_ID_TYPE					=	2351;	/*                               */
    public static final int MDC_ATTR_LIMIT_CURR					=	2356;	/*                               */
    public static final int MDC_ATTR_METRIC_STORE_CAPAC_CNT		=	2369;	/*                               */
    public static final int MDC_ATTR_METRIC_STORE_SAMPLE_ALG 	=	2371;	/*                               */
    public static final int MDC_ATTR_METRIC_STORE_USAGE_CNT 	=	2372;	/*                               */
    public static final int MDC_ATTR_MSMT_STAT					=	2375;	/*                               */
    public static final int MDC_ATTR_NU_ACCUR_MSMT				=	2378;	/*                               */
    public static final int MDC_ATTR_NU_CMPD_VAL_OBS			=	2379;	/*                               */
    public static final int MDC_ATTR_NU_VAL_OBS					=	2384;	/*                               */
    public static final int MDC_ATTR_NUM_SEG					=	2385;	/*                               */
    public static final int MDC_ATTR_OP_STAT					=	2387;	/*                               */
    public static final int MDC_ATTR_POWER_STAT					=	2389;	/*                               */
	public static final int MDC_ATTR_SA_SPECN					=	2413;	/*                               */
	public static final int MDC_ATTR_SCALE_SPECN_I16			=	2415;	/*                               */
	public static final int MDC_ATTR_SCALE_SPECN_I32			=	2416;	/*                               */
	public static final int MDC_ATTR_SCALE_SPECN_I8				=   2417;	/*                               */
	public static final int MDC_ATTR_SCAN_REP_PD				=   2421;	/*                               */
	public static final int MDC_ATTR_SEG_USAGE_CNT				=   2427;	/*                               */
	public static final int MDC_ATTR_SYS_ID						=   2436;	/*                               */
	public static final int MDC_ATTR_SYS_TYPE					=   2438;	/*                               */
	public static final int MDC_ATTR_TIME_ABS					=   2439;	/*                               */
	public static final int MDC_ATTR_TIME_BATT_REMAIN			=	2440;	/*                               */
	public static final int MDC_ATTR_TIME_END_SEG				=   2442;	/*                               */
	public static final int MDC_ATTR_TIME_PD_SAMP				=   2445;	/*                               */
	public static final int MDC_ATTR_TIME_REL					=   2447;	/*                               */
	public static final int MDC_ATTR_TIME_STAMP_ABS				=   2448;	/*                               */
	public static final int MDC_ATTR_TIME_STAMP_REL				=   2449;	/*                               */
	public static final int MDC_ATTR_TIME_START_SEG				=   2450;	/*                               */
	public static final int MDC_ATTR_TX_WIND					=   2453;	/*                               */
	public static final int MDC_ATTR_UNIT_CODE					=   2454;	/*                               */
	public static final int MDC_ATTR_UNIT_LABEL_STRING			=	2457;	/*                               */
	public static final int MDC_ATTR_VAL_BATT_CHARGE			=	2460;	/*                               */
	public static final int MDC_ATTR_VAL_ENUM_OBS				=   2462;	/*                               */
	public static final int MDC_ATTR_AL_COND					=	2476;	/*                               */
	public static final int MDC_ATTR_AL_OP_TEXT_STRING			=	2478;	/*                               */
	public static final int MDC_ATTR_TIME_REL_HI_RES			=   2536;	/*                               */
	public static final int MDC_ATTR_TIME_STAMP_REL_HI_RES		=	2537;	/*                               */
	public static final int MDC_ATTR_DEV_CONFIG_ID				=   2628;	/*                               */
	public static final int MDC_ATTR_MDS_TIME_INFO				=   2629;	/*                               */
	public static final int MDC_ATTR_METRIC_SPEC_SMALL			=	2630;	/*                               */
	public static final int MDC_ATTR_SOURCE_HANDLE_REF			=	2631;	/*                               */
	public static final int MDC_ATTR_SIMP_SA_OBS_VAL			=	2632;	/*                               */
	public static final int MDC_ATTR_ENUM_OBS_VAL_SIMP_OID		=	2633;	/*                               */
	public static final int MDC_ATTR_ENUM_OBS_VAL_SIMP_STR		=	2634;	/*                               */
	public static final int MDC_ATTR_REG_CERT_DATA_LIST			=	2635;	/*                               */
	public static final int MDC_ATTR_NU_VAL_OBS_BASIC			=	2636;	/*                               */
	public static final int MDC_ATTR_PM_STORE_CAPAB				=   2637;	/*                               */
	public static final int MDC_ATTR_PM_SEG_MAP					=   2638;	/*                               */
	public static final int MDC_ATTR_PM_SEG_PERSON_ID 			=	2639;	/*                               */
	public static final int MDC_ATTR_SEG_STATS					=   2640;	/*                               */
	public static final int MDC_ATTR_SEG_FIXED_DATA				=   2641;	/*                               */
	public static final int MDC_ATTR_PM_SEG_ELEM_STAT_ATTR		=	2642;	/*                               */ // Removed in the standard revision
	public static final int MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP	=	2643;	/*                               */
	public static final int MDC_ATTR_SCAN_REP_PD_MIN         	=	2644;	/*                               */
	public static final int MDC_ATTR_ATTRIBUTE_VAL_MAP       	=	2645;	/*                               */
	public static final int MDC_ATTR_NU_VAL_OBS_SIMP         	=	2646;	/*                               */
	public static final int MDC_ATTR_PM_STORE_LABEL_STRING 		=	2647;	/*                               */
	public static final int MDC_ATTR_PM_SEG_LABEL_STRING     	=	2648;	/*                               */
	public static final int MDC_ATTR_TIME_PD_MSMT_ACTIVE     	=	2649;	/*                               */
	public static final int MDC_ATTR_SYS_TYPE_SPEC_LIST      	=	2650;	/*                               */
	public static final int MDC_ATTR_METRIC_ID_PART				=   2655;	/*                               */
	public static final int MDC_ATTR_ENUM_OBS_VAL_PART       	=	2656;	/*                               */
	public static final int MDC_ATTR_SUPPLEMENTAL_TYPES      	=	2657;	/*                               */
	public static final int MDC_ATTR_TIME_ABS_ADJUST        	=	2658;	/*                               */
	public static final int MDC_ATTR_CLEAR_TIMEOUT				=   2659;	/*                               */
	public static final int MDC_ATTR_TRANSFER_TIMEOUT        	=	2660;	/*                               */
	public static final int MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR  =	2661;	/*                               */
	public static final int MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR =	2662;	/*                               */
	public static final int MDC_ATTR_METRIC_STRUCT_SMALL		=   2675;	/*                               */
	public static final int MDC_ATTR_NU_CMPD_VAL_OBS_SIMP  		=	2676;	/*                               */
	public static final int MDC_ATTR_NU_CMPD_VAL_OBS_BASIC 		=	2677;	/*                               */
	public static final int MDC_ATTR_ID_PHYSIO_LIST				=	2678;	/*                               */
	public static final int MDC_ATTR_SCAN_HANDLE_LIST 			=	2679;	/*                               */
	public static final int MDC_ATTR_TIME_BO					=	2689;	/*                               */
	public static final int MDC_ATTR_TIME_STAMP_BO				=	2690;	/*                               */
	public static final int MDC_ATTR_TIME_START_SEG_BO			=	2691;	/*                               */
	public static final int MDC_ATTR_TIME_END_SEG_BO			=	2692;	/*                               */
	/* Partition: ACT */
	public static final int MDC_ACT_SEG_CLR 					=	3084;	/*                               */
	public static final int MDC_ACT_SEG_GET_INFO 				=	3085;	/*                               */
	public static final int MDC_ACT_SET_TIME 					=	3095;	/*                               */
	public static final int MDC_ACT_DATA_REQUEST 				=	3099;	/*                               */
	public static final int MDC_ACT_SEG_TRIG_XFER 				=	3100;	/*                               */
	public static final int MDC_ACT_SET_BO_TIME					=	3101;	/*                               */
	/* Partition: NOTI */
	public static final int MDC_NOTI_CONFIG 					=	3356;	/*                               */
	public static final int MDC_NOTI_SCAN_REPORT_FIXED       	=	3357;	/*                               */
	public static final int MDC_NOTI_SCAN_REPORT_VAR         		=	3358;	/*                           */
	public static final int MDC_NOTI_SCAN_REPORT_MP_FIXED    		=	3359;	/*                           */
	public static final int MDC_NOTI_SCAN_REPORT_MP_VAR      		=	3360;	/*                           */
	public static final int MDC_NOTI_SEGMENT_DATA 					=	3361;	/*                           */
	public static final int MDC_NOTI_UNBUF_SCAN_REPORT_VAR 			=	3362;	/*                           */
	public static final int MDC_NOTI_UNBUF_SCAN_REPORT_FIXED		=	3363;	/*                           */
	public static final int MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED		=	3364;	/*                           */
	public static final int MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR		=	3365;	/*                           */
	public static final int MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED		=	3366;	/*                           */
	public static final int MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED 	=	3367;	/*                           */
	public static final int MDC_NOTI_BUF_SCAN_REPORT_VAR			=	3368;	/*                           */
	public static final int MDC_NOTI_BUF_SCAN_REPORT_FIXED			=	3369;	/*                           */
	public static final int MDC_NOTI_BUF_SCAN_REPORT_GROUPED   		=	3370;	/*                           */
	public static final int MDC_NOTI_BUF_SCAN_REPORT_MP_VAR			=	3371;	/*                           */
	public static final int MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED		=	3372;	/*                           */
	public static final int MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED		=	3373;	/*                           */

	/*************************************************************************************************
	* From Medical supervisory control and data acquisition (MDC_PART_SCADA)
	**************************************************************************************************/
	public static final int MDC_PULS_OXIM_PULS_RATE				=	18458;	/*                               */
	public static final int MDC_PULS_RATE_NON_INV				=	18474;	/*                               */
	public static final int MDC_PRESS_BD_NONINV					=	18948;	/*                               */
	public static final int MDC_PRESS_BD_NONINV_SYS				=	18949;	/*                               */
	public static final int MDC_PRESS_BD_NONINV_DIA				=	18950;	/*                               */
	public static final int MDC_PRESS_BD_NONINV_MEAN			=	18951;	/*                               */
	public static final int MDC_SAT_O2_QUAL 					= 	19248;	/*                               */
	public static final int MDC_TEMP_BODY						=	19292;	/*                               */
	public static final int MDC_PULS_OXIM_PERF_REL 				= 	19376;	/*                               */
	public static final int MDC_PULS_OXIM_PLETH 				= 	19380;	/*                               */
	public static final int MDC_PULS_OXIM_SAT_O2 				= 	19384;	/*                               */
	public static final int MDC_MODALITY_FAST 					= 	19508;	/*                               */
	public static final int MDC_PULS_OXIM_PULS_CHAR 			= 	19512;	/*                               */
	public static final int MDC_MODALITY_SLOW 					= 	19512;	/*                               */
	public static final int MDC_MODALITY_SPOT 					= 	19516;	/*                               */
	public static final int MDC_PULS_OXIM_DEV_STATUS 			= 	19532;	/*                               */
	public static final int MDC_CONC_GLU_GEN					=	28948;	/*                               */
	public static final int MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD	=	29112;	/*                               */
	public static final int MDC_CONC_GLU_CAPILLARY_PLASMA		=	29116;	/*                               */
	public static final int MDC_CONC_GLU_VENOUS_WHOLEBLOOD		=	29120;	/*                               */
	public static final int MDC_CONC_GLU_VENOUS_PLASMA			=	29124;	/*                               */
	public static final int MDC_CONC_GLU_ARTERIAL_WHOLEBLOOD	=	29128;	/*                               */
	public static final int MDC_CONC_GLU_ARTERIAL_PLASMA		=	29132;	/*                               */
	public static final int MDC_CONC_GLU_CONTROL				=	29136;	/*                               */
	public static final int MDC_CONC_GLU_ISF					=	29140;	/*                               */
	public static final int MDC_CONC_HBA1C						=	29148;	/*                               */
	public static final int MDC_TRIG 							= 	53250;	/*                               */
	public static final int MDC_TRIG_BEAT 						= 	53251;	/*                               */
	public static final int MDC_TRIG_BEAT_MAX_INRUSH 			= 	53259;	/*                               */
	public static final int MDC_MASS_BODY_ACTUAL 				=	57664;	/*                               */
	public static final int MDC_BODY_FAT						=	57676;	/*                               */
	public static final int MDC_METRIC_NOS 						= 	61439;	/*                               */

	/*************************************************************************************************
	* From Dimensions (MDC_PART_DIM)
	**************************************************************************************************/
	public static final int MDC_DIM_PERCENT						=	544;	/* %                          	 */
	public static final int MDC_DIM_MILLI_L						=	1618;	/* mL							 */
	public static final int MDC_DIM_X_G							=	1728;	/* g							 */
	public static final int MDC_DIM_KILO_G						=	1731;	/* kg 	                         */
	public static final int MDC_DIM_MILLI_G						=	1746; 	/* mg							 */
	public static final int MDC_DIM_MILLI_G_PER_DL				=	2130;	/* mg dL-1						 */
	public static final int MDC_DIM_MIN							=	2208;	/* min    	                     */
	public static final int MDC_DIM_HR							=	2240;	/* h          	                 */
	public static final int MDC_DIM_DAY							=	2272;	/* d              	             */
	public static final int MDC_DIM_BEAT_PER_MIN				=	2720; 	/* bpm 737						 */
	public static final int MDC_DIM_KILO_PASCAL					=	3843;	/* kPa                           */
	public static final int MDC_DIM_MMHG						=	3872;	/* mmHg                          */
	public static final int MDC_DIM_MILLI_MOLE_PER_L			=	4722;	/* mmol L-1						 */
	public static final int MDC_DIM_DEGC						=	6048;	/* ºC                 	         */

	/*************************************************************************************************
	* From Communication Infrastructure (MDC_PART_INFRA)
	**************************************************************************************************/
	public static final int MDC_DEV_SPEC_PROFILE_PULS_OXIM		=	4100;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_MIN_ECG		=	4102;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_BP				=	4103;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_TEMP			=	4104;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_RESP_RATE		=	4109;	/* Respiration rate              */
	public static final int MDC_DEV_SPEC_PROFILE_SCALE			=	4111;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_GLUCOSE		=	4113;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_INR			=	4114;	/* International normalized ratio */
	public static final int MDC_DEV_SPEC_PROFILE_INSULIN_PUMP	=	4115;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_BCA			=	4116;	/* Body composition analyzer     */
	public static final int MDC_DEV_SPEC_PROFILE_PEAK_FLOW		=	4117;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_HF_CARDIO		=	4137;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_HF_STRENGTH 	=	4138;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_AI_ACTIVITY_HUB = 4167;	/*                               */
	public static final int MDC_DEV_SPEC_PROFILE_AI_MED_MINDER	=	4168;	/*                               */
	/* The range from 4196 to 5119 is reserved for sub classifying devices within other specializations
	(profiles). For instance in the -10441 (Cardio) specialization defines step counters as a profile */

	/* 4196 through 4211 used for IEEE Std 11073-10441 (Cardio)                      */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_STEP_COUNTER	=	4200; /* Step counter 				*/

	/* 4212 through 4235 used for IEEE Std 11073-10471 (Activity hub)         */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_FALL_SENSOR			=	4213;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_PERS_SENSOR			=	4214;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_SMOKE_SENSOR			=	4215;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_CO_SENSOR				=	4216;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_WATER_SENSOR			=	4217;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_GAS_SENSOR				=	4218;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_MOTION_SENSOR			=	4219;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_PROPEXIT_SENSOR		=	4220;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_ENURESIS_SENSOR		=	4221;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_CONTACTCLOSURE_SENSOR	=	4222;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_USAGE_SENSOR			=	4223;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_SWITCH_SENSOR			=	4224;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_DOSAGE_SENSOR			=	4225;	/*                               */
	public static final int MDC_DEV_SUB_SPEC_PROFILE_TEMP_SENSOR			=	4226;	/*                               */

	/* Placed 256 back from the start of the last Partition: OptionalPackageIdentifiers (i.e., 8192-256). 				  */
	public static final int MDC_TIME_SYNC_NONE					=	7936;	/* no time synchronization protocol supported */
	public static final int MDC_TIME_SYNC_NTPV3					=	7937;	/* RFC 1305 1992 Mar obs: 1119,1059,958 	  */
	public static final int MDC_TIME_SYNC_NTPV4 				=	7938;	/* <under development at ntp.org>  			  */
	public static final int MDC_TIME_SYNC_SNTPV4				=	7939;	/* RFC 2030 1996 Oct          obs: 1769       */
	public static final int MDC_TIME_SYNC_SNTPV4330				=	7940;	/* RFC 4330 2006 Jan          obs: 2030,1769  */
	public static final int MDC_TIME_SYNC_BTV1					=	7941;	/* Bluetooth Medical Device Profile           */

	/*************************************************************************************************
	* From Return Codes (MDC_PART_RET_CODE)
	**************************************************************************************************/
	public static final int MDC_RET_CODE_UNKNOWN				=	1;		/* Generic error code						   */
	/* Partition MDC_PART_RET_CODE/OBJ Object errors                                          		               		   */
	public static final int MDC_RET_CODE_OBJ_BUSY         		=	1000;	/* Object is busy so cannot handle the request */
	/* Partition MDC_PART_RETURN_CODES/STORE Storage errors                                    					   		   */
	public static final int MDC_RET_CODE_STORE_EXH        		=	2000;	/* Storage such as disk is full                */
	public static final int MDC_RET_CODE_STORE_OFFLN 			=	2001;	/* Storage such as disk is offline             */


	/*********************************************************************************
	* From Personal Health Device Disease Management (MDC_PART_PHD_DM)
	**********************************************************************************/
	public static final int MDC_GLU_METER_DEV_STATUS			=	29144;	/*                               */
	public static final int MDC_CTXT_GLU_EXERCISE				=	29152;	/*                               */
	public static final int MDC_CTXT_GLU_CARB					=	29156;	/*                               */
	public static final int MDC_CTXT_GLU_CARB_BREAKFAST			=	29160;	/*                               */
	public static final int MDC_CTXT_GLU_CARB_LUNCH				=	29164;	/*                               */
	public static final int MDC_CTXT_GLU_CARB_DINNER			=	29168;	/*                               */
	public static final int MDC_CTXT_GLU_CARB_SNACK				=	29172;	/*                               */
	public static final int MDC_CTXT_GLU_CARB_DRINK				=	29176;	/*                               */
	public static final int MDC_CTXT_GLU_CARB_SUPPER			=	29180;	/*                               */
	public static final int MDC_CTXT_GLU_CARB_BRUNCH			=	29184;	/*                               */
	public static final int MDC_CTXT_MEDICATION					=	29188;	/*                               */
	public static final int MDC_CTXT_MEDICATION_RAPIDACTING		=	29192;	/*                               */
	public static final int MDC_CTXT_MEDICATION_SHORTACTING		=	29196;	/*                               */
	public static final int MDC_CTXT_MEDICATION_INTERMEDIATEACTING	=	29200;	/*                           */
	public static final int MDC_CTXT_MEDICATION_LONGACTING		=	29204;	/*                               */
	public static final int MDC_CTXT_MEDICATION_PREMIX			=	29208;	/*                               */
	public static final int MDC_CTXT_GLU_HEALTH					=	29212;	/*                               */
	public static final int MDC_CTXT_GLU_HEALTH_MINOR			=	29216;	/*                               */
	public static final int MDC_CTXT_GLU_HEALTH_MAJOR			=	29220;	/*                               */
	public static final int MDC_CTXT_GLU_HEALTH_MENSES			=	29224;	/*                               */
	public static final int MDC_CTXT_GLU_HEALTH_STRESS			=	29228;	/*                               */
	public static final int MDC_CTXT_GLU_HEALTH_NONE			=	29232;	/*                               */
	public static final int MDC_CTXT_GLU_SAMPLELOCATION			=	29236;	/*                               */
	public static final int MDC_CTXT_GLU_SAMPLELOCATION_FINGER	=	29240;	/*                               */
	public static final int MDC_CTXT_GLU_SAMPLELOCATION_AST		=	29244;	/*                               */
	public static final int MDC_CTXT_GLU_SAMPLELOCATION_EARLOBE =	29248;	/*                               */
	public static final int MDC_CTXT_GLU_SAMPLELOCATION_CTRLSOLUTION =	29252;	/*                           */
	public static final int MDC_CTXT_GLU_MEAL					=	29256;	/*                               */
	public static final int MDC_CTXT_GLU_MEAL_PREPRANDIAL		=	29260;	/*                               */
	public static final int MDC_CTXT_GLU_MEAL_POSTPRANDIAL		=	29264;	/*                               */
	public static final int MDC_CTXT_GLU_MEAL_FASTING			=	29268;	/*                               */
	public static final int MDC_CTXT_GLU_MEAL_CASUAL			=	29272;	/*                               */
	public static final int MDC_CTXT_GLU_TESTER					=	29276;	/*                               */
	public static final int MDC_CTXT_GLU_TESTER_SELF			=	29280;	/*                               */
	public static final int MDC_CTXT_GLU_TESTER_HCP				=	29284;	/*                               */
	public static final int MDC_CTXT_GLU_TESTER_LAB				=	29288;	/*                               */

	/**********************************************************************************
	* All of the following are from NomPartition (MDC_PART_PHD_AI)                    *
	**********************************************************************************/
	public static final int MDC_AI_TYPE_SENSOR_FALL				=	1;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_PERS				=	2;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_SMOKE			=	3;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_CO				=	4;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_WATER			=	5;		/*   								*/
	public static final int MDC_AI_TYPE_SENSOR_GAS				=	6;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_MOTION			=	7;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_PROPEXIT			=	8;		/*    								*/
	public static final int MDC_AI_TYPE_SENSOR_ENURESIS			=	9;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_CONTACTCLOSURE	=	10;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_USAGE			=	11;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_SWITCH			=	12;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_DOSAGE			=	13;		/*     								*/
	public static final int MDC_AI_TYPE_SENSOR_TEMP				=	14;		/*     								*/

	/* The range 15-1024 is reserved for future expansion                                                 		*/
	/**********************************************************************************
	* All of the following are from NomPartition – Partition: MDC_PART_PHD_AI
	*
	**********************************************************************************/
	/********************************************************************************************************
	* Locations are encoded within 16 bits as.....															*
	* upper 10 bits are the location type (for example bedroom - MDC_AI_LOCATION_BEDROOM) 					*
	* lower 6 bits are the location type instance identifier (for example bedroom 0, bedroom 1, etc.)       *
	* therefore, location viewed as a 16 bit entity would have the types assigned in blocks of 64          	*
	* The location type instance identifier allows for both a single dwelling with multiple rooms of that 	*
	* type, as well as a multiple family dwelling that may contain that type room in each subunit          	*
	* *******************************************************************************************************/
	/* general                                                                                             	*/
	public static final int MDC_AI_LOCATION_START				=	1024;		/*     								*/
	public static final int MDC_AI_LOCATION_UNKNOWN				=	1024;		/*     								*/
	public static final int MDC_AI_LOCATION_UNSPECIFIED			=	1088;		/*     								*/
	public static final int MDC_AI_LOCATION_RESIDENT			=	1152;		/*     								*/
	public static final int MDC_AI_LOCATION_LOCALUNIT			=	1216;		/*     								*/
	/* The range 1217-3071 is reserved for future expansion															*/
	/* rooms																										*/
	public static final int MDC_AI_LOCATION_BEDROOM				=	3072;		/*     								*/
	public static final int MDC_AI_LOCATION_BEDROOMMASTER		=	3136;		/*     								*/
	public static final int MDC_AI_LOCATION_TOILET				=	3200;		/*     								*/
	public static final int MDC_AI_LOCATION_TOILETMAIN			=	3264;		/*     								*/
	public static final int MDC_AI_LOCATION_OUTSIDETOILET		=	3328;		/*     								*/
	public static final int MDC_AI_LOCATION_SHOWERROOM			=	3392;		/*     								*/
	public static final int MDC_AI_LOCATION_KITCHEN				=	3456;		/*     								*/
	public static final int MDC_AI_LOCATION_KITCHENMAIN			=	3520;		/*     								*/
	public static final int MDC_AI_LOCATION_LIVINGAREA			=	3584;		/*     								*/
	public static final int MDC_AI_LOCATION_LIVINGROOM			=	3648;		/*     								*/
	public static final int MDC_AI_LOCATION_DININGROOM			=	3712;		/*     								*/
	public static final int MDC_AI_LOCATION_STUDY				=	3776;		/*     								*/
	public static final int MDC_AI_LOCATION_HALL				=	3840;		/*     								*/
	public static final int MDC_AI_LOCATION_LANDING				=	3904;		/*     								*/
	public static final int MDC_AI_LOCATION_STAIRS				=	3968;		/*     								*/
	public static final int MDC_AI_LOCATION_HALLLANDINGSTAIRS	=	4032;		/*     								*/
	public static final int MDC_AI_LOCATION_GARAGE				=	4096;		/*     								*/
	public static final int MDC_AI_LOCATION_GARDENGARAGE		=	4160;		/*     								*/
	public static final int MDC_AI_LOCATION_GARDENGARAGEAREA	=	4224;		/*     								*/
	public static final int MDC_AI_LOCATION_FRONTGARDEN			=	4288;		/*     								*/
	public static final int MDC_AI_LOCATION_BACKGARDEN			=	4352;		/*     								*/
	public static final int MDC_AI_LOCATION_SHED				=	4416;		/*     								*/
	/* The range 4417-7167 is reserved for future expansion															*/
	/* appliances																									*/
	public static final int MDC_AI_APPLIANCE_KETTLE				=	7168; 		/* Note: Self heating				*/
	public static final int MDC_AI_APPLIANCE_TELEVISION			=	7232;		/*     								*/
	public static final int MDC_AI_APPLIANCE_STOVE				=	7296;		/*     								*/
	public static final int MDC_AI_APPLIANCE_MICROWAVE			=	7360;		/*     								*/
	public static final int MDC_AI_APPLIANCE_TOASTER			=	7424;		/*     								*/
	public static final int MDC_AI_APPLIANCE_VACUUM				=	7488;		/*     								*/
	public static final int MDC_AI_APPLIANCE_APPLIANCE			=	7552;		/*     								*/
	public static final int MDC_AI_APPLIANCE_FAUCET				=	7616;		/*     								*/
	/* The range 7617-9215 is reserved for future expansion															*/
	/* doors																										*/
	public static final int MDC_AI_LOCATION_FRONTDOOR			=	9216;		/*     								*/
	public static final int MDC_AI_LOCATION_BACKDOOR			=	9280;		/*     								*/
	public static final int MDC_AI_LOCATION_FRIDGEDOOR			=	9344;		/*     								*/
	public static final int MDC_AI_LOCATION_MEDCABDOOR			=	9408;		/*     								*/
	public static final int MDC_AI_LOCATION_WARDROBEDOOR		=	9472;		/*     								*/
	public static final int MDC_AI_LOCATION_FRONTCUPBOARDDOOR	=	9536;		/*     								*/
	public static final int MDC_AI_LOCATION_OTHERDOOR			=	9600;		/*     								*/
	/* The range 9601-11263 is reserved for future expansion														*/
	/* usage																										*/
	public static final int MDC_AI_LOCATION_BED					=	11264;		/*     								*/
	public static final int MDC_AI_LOCATION_CHAIR				=	11328;		/*     								*/
	public static final int MDC_AI_LOCATION_SOFA				=	11392;		/*     								*/
	public static final int MDC_AI_LOCATION_TOILET_SEAT			=	11456;		/*     								*/
	public static final int MDC_AI_LOCATION_STOOL				=	11520;		/*     								*/
	/* The range 11521-13311 is reserved for future expansion														*/
	/* The range 13312-61439 (0x3400-0xEFFF) is reserved for future expansion										*/

}
