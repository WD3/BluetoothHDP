/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

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
package org.bn.coders.mder;

import org.bn.annotations.ASN1Element;
import org.bn.annotations.ASN1Float;
import org.bn.annotations.ASN1SFloat;
import org.bn.annotations.ASN1String;
import org.bn.annotations.ASN1BitString;
import org.bn.annotations.constraints.ASN1SizeConstraint;
import org.bn.coders.DecodedObject;
import org.bn.coders.ElementInfo;
import org.bn.coders.UniversalTag;
import org.bn.metadata.ASN1ElementMetadata;
import org.bn.metadata.constraints.ASN1SizeConstraintMetadata;
import org.bn.metadata.constraints.IASN1ConstraintMetadata;
import org.bn.types.BitString;

public class MDERCoderUtils {

	/*
	 * Sized Constraints must be used for all INTEGER and BIT STRING data types.
	 * Short names for the supported constraint are defined as follows:
	 */
	enum RestrictedInteger{INT_U8,INT_I8,INT_U16,INT_I16,INT_U32,INT_I32};
	enum RestrictedBitString{BITS_8, BITS_16,BITS_32};

    public static DecodedObject<Integer> getTagValueForElement(ElementInfo info, int tagClass, int elemenType, int universalTag) {
        DecodedObject<Integer> result = new DecodedObject<Integer>();
        result.setSize(1);
        // result.setValue(tagClass | elemenType | universalTag);
	if(universalTag < UniversalTag.LastUniversal) {
        	result.setValue(tagClass | elemenType | universalTag);
	}
	else
		result = getTagValue ( tagClass , elemenType , universalTag , universalTag, tagClass ) ;

        if(info.hasPreparedInfo()) {
            ASN1ElementMetadata meta = info.getPreparedASN1ElementInfo();
            if(meta!=null && meta.hasTag()) {
                result = getTagValue(tagClass,elemenType,universalTag,
                    meta.getTag(),
                    meta.getTagClass()
                );
            }
        }
        else {
            ASN1Element elementInfo = null;
            if(info.getASN1ElementInfo()!=null) {
                elementInfo = info.getASN1ElementInfo();
            }
            else
            if(info.getAnnotatedClass().isAnnotationPresent(ASN1Element.class)) {
                elementInfo = info.getAnnotatedClass().getAnnotation(ASN1Element.class);
            }

            if(elementInfo!=null) {
                if(elementInfo.hasTag()) {
                    result = getTagValue(tagClass,elemenType,universalTag, elementInfo.tag(),elementInfo.tagClass());
                }
            }
        }
        return result;
    }

    public static DecodedObject<Integer> getTagValue(int tagClass, int elemenType, int universalTag, int userTag, int userTagClass) {
        DecodedObject<Integer> resultObj = new DecodedObject<Integer>();
        int result = tagClass | elemenType | universalTag;

        tagClass = userTagClass;
        if (userTag < 31) {
            result = tagClass | elemenType | userTag;
            resultObj.setSize(1);
        }
        else {
            result = tagClass | elemenType | 0x1F;
            if (userTag < 0x80) {
                result <<= 8;
                result |= userTag & 0x7F;
                resultObj.setSize(2);
            }
            else
            if (userTag < 0x3FFF)
            {
                result <<= 16;
                result |= (((userTag & 0x3fff) >> 7) | 0x80) << 8;
                result |= ((userTag & 0x3fff) & 0x7f);
                resultObj.setSize(3);
            }
            else
            if (userTag < 0x3FFFF)
            {
                result <<= 24;
                result |= (((userTag & 0x3FFFF) >> 15) | 0x80) << 16;
                result |= (((userTag & 0x3FFFF) >> 7) | 0x80) << 8;
                result |= ((userTag & 0x3FFFF) & 0x3f);
                resultObj.setSize(4);
            }
        }
        resultObj.setValue(result);
        return resultObj;
    }

    public static RestrictedInteger getIntegerSubtype (long minValueRange, long maxValueRange) throws Exception{
    	if (minValueRange==0L && maxValueRange==255L) return RestrictedInteger.INT_U8;
    	else if (minValueRange==-128L && maxValueRange==127L) return RestrictedInteger.INT_I8;
    	else if (minValueRange==0L && maxValueRange==65535L) return RestrictedInteger.INT_U16;
    	else if (minValueRange==-32768L && maxValueRange==32767L) return RestrictedInteger.INT_I16;
    	else if (minValueRange==0L && maxValueRange==4294967295L) return RestrictedInteger.INT_U32;
    	else if (minValueRange==-2147483648L && maxValueRange==2147483647L) return RestrictedInteger.INT_I32;
    	else throw new Exception("Invalid MDER INTEGER range.");
    }

    public static boolean valueInRange (RestrictedInteger intType, long value) throws Exception{
    	if (intType==MDERCoderUtils.RestrictedInteger.INT_U8)
    		return (0L<=value && value<=255L);
    	else if (intType==MDERCoderUtils.RestrictedInteger.INT_I8)
    		return (-128L<=value && value<=127L);
    	else if (intType==MDERCoderUtils.RestrictedInteger.INT_U16)
    		return (0L<=value && value<=65535L);
    	else if (intType==MDERCoderUtils.RestrictedInteger.INT_I16)
    		return (-32768L<=value && value<=32767L);
    	else if (intType==MDERCoderUtils.RestrictedInteger.INT_U32)
    		return (0L<=value && value<=4294967295L);
    	else if (intType==MDERCoderUtils.RestrictedInteger.INT_I32)
    		return (-2147483648L<=value && value<=2147483647L);
    	return false;
    }

    public static int getIntegerSubtypeLength (RestrictedInteger intType) throws Exception{
    	if ((intType==MDERCoderUtils.RestrictedInteger.INT_U8)||(intType==MDERCoderUtils.RestrictedInteger.INT_I8))
        	return 1;
        else if ((intType==MDERCoderUtils.RestrictedInteger.INT_U16)||(intType==MDERCoderUtils.RestrictedInteger.INT_I16))
        	return 2;
        else if ((intType==MDERCoderUtils.RestrictedInteger.INT_U32)||(intType==MDERCoderUtils.RestrictedInteger.INT_I32))
        	return 4;
        else throw new Exception("Invalid MDER INTEGER subtype.");
    }

    public static RestrictedBitString checkBitStringConstraints (ElementInfo elementInfo) throws Exception{
    	long size=0;

    	if(elementInfo.hasPreparedInfo()) {
            if(elementInfo.getPreparedInfo().hasConstraint()
                && elementInfo.getPreparedInfo().getConstraint() instanceof ASN1SizeConstraintMetadata) {
                IASN1ConstraintMetadata constraint = elementInfo.getPreparedInfo().getConstraint();
                size = ((ASN1SizeConstraintMetadata)constraint).getMax();
            }
        } else if(elementInfo.getAnnotatedClass().isAnnotationPresent(ASN1SizeConstraint.class)) {
        	ASN1SizeConstraint constraint = elementInfo.getAnnotatedClass().getAnnotation(ASN1SizeConstraint.class);
            size = constraint.max();
        } else throw new Exception("Size Constraint for BIT STRING is required in MDER.");

    	return getRestrictedBitStringSubtype(size);
    }

    private static RestrictedBitString getRestrictedBitStringSubtype (long size) throws Exception{
    	if (size==8)
    		return RestrictedBitString.BITS_8;
    	else if (size==16)
    		return RestrictedBitString.BITS_16;
    	else if (size==32)
    		return RestrictedBitString.BITS_32;
    	else throw new Exception("Invalid MDER BIT STRING size.");
    }

    public static int getBitStringSubtypeLength (RestrictedBitString bsType) throws Exception{
    	if (bsType==RestrictedBitString.BITS_8)
    		return 1;
    	else if (bsType==RestrictedBitString.BITS_16)
    		return 2;
    	else if (bsType==RestrictedBitString.BITS_32)
    		return 4;
    	else throw new Exception("Invalid MDER BIT STRING size.");
    }

    public static boolean isFixedOctetString (ElementInfo elementInfo) {
    	if(elementInfo.hasPreparedInfo())
            return (elementInfo.getPreparedInfo().hasConstraint()
            		&& elementInfo.getPreparedInfo().getConstraint() instanceof ASN1SizeConstraintMetadata);
        else return (elementInfo.getAnnotatedClass().isAnnotationPresent(ASN1SizeConstraint.class));
    }

    public static long getLengthFixedOctetString (ElementInfo elementInfo) throws Exception {
    	long size=0L;
    	if(elementInfo.hasPreparedInfo()) {
            if(elementInfo.getPreparedInfo().hasConstraint()
                && elementInfo.getPreparedInfo().getConstraint() instanceof ASN1SizeConstraintMetadata) {
                IASN1ConstraintMetadata constraint = elementInfo.getPreparedInfo().getConstraint();
                size = ((ASN1SizeConstraintMetadata)constraint).getMax();
            }
        } else if(elementInfo.getAnnotatedClass().isAnnotationPresent(ASN1SizeConstraint.class)) {
        	ASN1SizeConstraint constraint = elementInfo.getAnnotatedClass().getAnnotation(ASN1SizeConstraint.class);
            size = constraint.max();
        } else throw new Exception("Octet string is not constrained.");
    	return size;
    }

    public static boolean isFloatType(ElementInfo elementInfo) {

    	if(elementInfo.getAnnotatedClass().isAnnotationPresent(ASN1Float.class))
    		/*
        	The floating point type data type (FLOAT-Type) is defined to represent numeric values that are not integer
			in type. The FLOAT-Type is defined as a 32-bit value with 24-bit mantissa and 8-bit exponent. See E.7 for
			full definition of this data type. This data type is defined as follows:

			FLOAT-Type ::= INT-U32 --32-bit float type; the integer type is a placeholder only
        	 */
    		return true;
    	else if(elementInfo.getAnnotatedClass().isAnnotationPresent(ASN1SFloat.class))
    		/*
    		The short floating point type data type (SFLOAT-Type) is defined to represent numeric values that are not
    		integer in type and have limited resolution. The SFLOAT-Type is defined as a 16-bit value with 12-bit
    		mantissa and 4-bit exponent. See Annex E.7 for full definition of this data type. This data type is defined as
    		follows:

    		SFLOAT-Type ::= INTEGER (0..65535) --16-bit float type; the integer type is a placeholder only
    		*/
    		return true;
    	else return false;
    }
}