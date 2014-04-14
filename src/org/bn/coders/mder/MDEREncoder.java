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
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import org.bn.annotations.*;
import org.bn.annotations.constraints.ASN1SizeConstraint;
import org.bn.annotations.constraints.ASN1ValueRangeConstraint;
import org.bn.coders.CoderUtils;
import org.bn.coders.DecodedObject;
import org.bn.coders.ElementInfo;
import org.bn.coders.ElementType;
import org.bn.coders.Encoder;
import org.bn.coders.TagClass;
import org.bn.coders.UniversalTag;
import org.bn.metadata.ASN1SequenceOfMetadata;
import org.bn.metadata.constraints.ASN1SizeConstraintMetadata;
import org.bn.metadata.constraints.ASN1ValueRangeConstraintMetadata;
import org.bn.metadata.constraints.IASN1ConstraintMetadata;
import org.bn.types.BitString;
import org.bn.types.ObjectIdentifier;
import org.bn.utils.ReverseByteArrayOutputStream;
import java.lang.annotation.Annotation;

public class MDEREncoder <T> extends Encoder<T> {

    public MDEREncoder() {
    }

    public void encode(T object, OutputStream stream) throws Exception {
        ReverseByteArrayOutputStream reverseStream = new ReverseByteArrayOutputStream();
        super.encode(object, reverseStream);
        reverseStream.writeTo(stream);
    }

    public int encodeSequence(Object object, OutputStream stream,
                                 ElementInfo elementInfo) throws Exception {
    	if(CoderUtils.isSequenceSet(elementInfo))
    		throw new Exception ("SET is an excluded ASN.1 data type in MDER specialization.");

    	int resultSize = 0;
        Field[] fields = elementInfo.getFields(object.getClass());

        for ( int i = 0;i<fields.length; i++) {
             Field field  = fields [ fields.length - 1 - i];
             resultSize+= encodeSequenceField(object, fields.length - 1 - i, field,stream,elementInfo);
        }
        return resultSize;
    }

    public int encodeChoice(Object object, OutputStream stream,
                               ElementInfo elementInfo)  throws Exception {
    	int resultSize = 0, size=0;
        ElementInfo info = null;
        int elementIndex = 0;
        int fieldIdx = 0;
        for ( Field field : object.getClass().getDeclaredFields() ) {
            if(!field.isSynthetic()) {
                elementIndex++;
                info = new ElementInfo();
                info.setAnnotatedClass(field);
                if(elementInfo.hasPreparedInfo()) {
                    info.setPreparedInfo(elementInfo.getPreparedInfo().getFieldMetadata(fieldIdx));
                }
                else {
                    info.setASN1ElementInfoForClass(field);
                }
                if(isSelectedChoiceItem(field,object, info)) {
                    break;
                }
                else
                    info = null;
                fieldIdx++;
            }
        }
        if(info==null) {
            throw new  IllegalArgumentException ("The choice '" + object.toString() + "' does not have a selected item!");
        }

        int tag;
        if (info.getPreparedASN1ElementInfo()!=null && info.getPreparedASN1ElementInfo().hasTag())
        	tag = info.getPreparedASN1ElementInfo().getTag();
        else tag = elementIndex;


        Object invokeObjResult = invokeGetterMethodForField((Field)info.getAnnotatedClass(),object, info);
        size+=encodeClassType(invokeObjResult, stream, info);

        resultSize+=size;
        /* Number of octets */
        resultSize+=encodeIntegerValue(MDERCoderUtils.RestrictedInteger.INT_U16, size, stream);
        /* tag selected in choice type */
        resultSize+=encodeIntegerValue(MDERCoderUtils.RestrictedInteger.INT_U16, tag, stream);

        return resultSize;
    }

    public int encodeEnumItem(Object enumConstant, Class enumClass, OutputStream stream,
                                 ElementInfo elementInfo) throws Exception {

    	throw new Exception ("ENUMERATED is an excluded ASN.1 data type in MDER specialization.");
    }

    public int encodeBoolean(Object object, OutputStream stream,
                                ElementInfo elementInfo) throws Exception {
    	throw new Exception ("ENUMERATED is an excluded ASN.1 data type in MDER specialization.");
    }

    public int encodeAny(Object object, OutputStream stream,
                            ElementInfo elementInfo) throws Exception {
        int resultSize = 0, sizeOfString = 0;
        byte[] buffer = (byte[])object;
        stream.write( buffer );
        sizeOfString = buffer.length;
        CoderUtils.checkConstraints(sizeOfString,elementInfo);
        resultSize += sizeOfString;
        resultSize += encodeIntegerValue(MDERCoderUtils.RestrictedInteger.INT_U16, sizeOfString, stream);
        return resultSize;
    }

    public int encodeIntegerValue(long value, OutputStream stream) throws Exception {
        int resultSize = CoderUtils.getIntegerLength(value);
        for (int i = 0 ; i < resultSize ; i++) {
            stream.write((byte)value);
            value =  value >> 8 ;
        }
        return resultSize;
    }

    public int encodeIntegerValue(MDERCoderUtils.RestrictedInteger intType, long value, OutputStream stream) throws Exception {
        int resultSize = 0;
        if (!MDERCoderUtils.valueInRange(intType, value))
        	throw new Exception ("Integer value is not in restricted integer range.");

        if ((intType==MDERCoderUtils.RestrictedInteger.INT_U8)||(intType==MDERCoderUtils.RestrictedInteger.INT_I8)){
        	resultSize=1;
        }else if ((intType==MDERCoderUtils.RestrictedInteger.INT_U16)||(intType==MDERCoderUtils.RestrictedInteger.INT_I16))
        	resultSize=2;
        else resultSize=4;

        for (int i = 0 ; i < resultSize ; i++) {
            stream.write((byte)value);
            value =  value >> 8 ;
        }
        return resultSize;
    }

    public int encodeInteger(Object object, OutputStream stream,
                                ElementInfo elementInfo) throws Exception {
    	int resultSize = 0;
        int szOfInt = 0;
        long min = 0, max = 0;

        if(elementInfo.hasPreparedInfo()) {
            if(elementInfo.getPreparedInfo().hasConstraint()
                && elementInfo.getPreparedInfo().getConstraint() instanceof ASN1ValueRangeConstraintMetadata) {
                IASN1ConstraintMetadata constraint = elementInfo.getPreparedInfo().getConstraint();
                min = ((ASN1ValueRangeConstraintMetadata)constraint).getMin();
                max = ((ASN1ValueRangeConstraintMetadata)constraint).getMax();
            }
        } else if(elementInfo.getAnnotatedClass().isAnnotationPresent(ASN1ValueRangeConstraint.class)) {
            ASN1ValueRangeConstraint constraint = elementInfo.getAnnotatedClass().getAnnotation(ASN1ValueRangeConstraint.class);
            min = constraint.min();
            max = constraint.max();
        } else throw new Exception("Value range is not defined for MDER");

        if(object instanceof Integer) {
            Integer value = (Integer) object;
            CoderUtils.checkConstraints(value,elementInfo);
            szOfInt = encodeIntegerValue(MDERCoderUtils.getIntegerSubtype(min,max),value,stream);
        }
        else
        if(object instanceof Long) {
            Long value = (Long) object;
            CoderUtils.checkConstraints(value,elementInfo);
            szOfInt = encodeIntegerValue(MDERCoderUtils.getIntegerSubtype(min,max),value,stream);
        }
        resultSize += szOfInt;
        return resultSize;
    }

    public int encodeReal(Object object, OutputStream stream,
                             ElementInfo elementInfo) throws Exception {
    	throw new Exception ("REAL is an excluded ASN.1 data type in MDER specialization. Use FLOAT-Type.");
    }

    public int encodeOctetString(Object object, OutputStream stream,
                                    ElementInfo elementInfo) throws Exception {
        int resultSize = 0, sizeOfString = 0;
        byte[] buffer = (byte[])object;
        stream.write( buffer );
        sizeOfString = buffer.length;

        resultSize += sizeOfString;

        CoderUtils.checkConstraints(sizeOfString,elementInfo);

        if (!MDERCoderUtils.isFixedOctetString(elementInfo)){
        	resultSize += encodeIntegerValue(MDERCoderUtils.RestrictedInteger.INT_U16, sizeOfString, stream);
        	/*resultSize += encodeLength(sizeOfString, stream);*/
        }
        return resultSize;
    }

    public int encodeBitString(Object object, OutputStream stream,
                                  ElementInfo elementInfo) throws Exception {

    	int resultSize = 0, sizeOfString = 0;

        MDERCoderUtils.checkBitStringConstraints(elementInfo);
        BitString str = (BitString)object;
        CoderUtils.checkConstraints(str.getLengthInBits(), elementInfo);

        byte[] buffer = str.getValue();
        stream.write( buffer );
        sizeOfString = buffer.length;

        resultSize += sizeOfString;
        return resultSize;
    }

    public int encodeString(Object object, OutputStream stream,
                               ElementInfo elementInfo) throws Exception {
    	throw new Exception ("STRING is an excluded ASN.1 data type in MDER specialization.");
    }

    public int encodeSequenceOf(Object object, OutputStream stream,
                                   ElementInfo elementInfo) throws Exception {
        int resultSize = 0;
        Object[] collection = ((java.util.Collection<Object>)object).toArray();
        int sizeOfCollection = 0;
        for(int i=0;i<collection.length;i++) {
            Object obj = collection[collection.length - 1 - i];
            ElementInfo info = new ElementInfo();
            info.setAnnotatedClass(obj.getClass());
            info.setParentAnnotated(elementInfo.getAnnotatedClass());
            if(elementInfo.hasPreparedInfo()) {
                ASN1SequenceOfMetadata seqOfMeta = (ASN1SequenceOfMetadata)elementInfo.getPreparedInfo().getTypeMetadata();
                info.setPreparedInfo( seqOfMeta.getItemClassMetadata() );
            }
            sizeOfCollection+=encodeClassType(obj,stream,info);
        }
        resultSize += sizeOfCollection;
        CoderUtils.checkConstraints(collection.length,elementInfo);

        /* Number of octets */
        resultSize += encodeIntegerValue(MDERCoderUtils.RestrictedInteger.INT_U16, sizeOfCollection, stream);
        /* Number of elements in the sequence */
        resultSize += encodeIntegerValue(MDERCoderUtils.RestrictedInteger.INT_U16, collection.length, stream);
        return resultSize;
    }

    /*
    protected int encodeHeader(DecodedObject<Integer> tagValue, int contentLen, OutputStream stream) throws Exception {
        int resultSize = encodeLength(contentLen, stream);
        resultSize += encodeTag(tagValue, stream);
        return resultSize;
    }

    protected int encodeTag(DecodedObject<Integer> tagValue, OutputStream stream) throws Exception {
        int resultSize = tagValue.getSize();
        int value = tagValue.getValue();
        for (int i = 0 ; i < tagValue.getSize() ; i++) {
            stream.write((byte)value);
            value =  value >> 8 ;
        }
        return resultSize;
    }


    protected int encodeLength(int length, OutputStream stream) throws IOException {
        int resultSize = 0;

        if (length < 0) {
            throw new IllegalArgumentException() ;
        }
        else
        if (length < 128) {
            stream.write(length);
            resultSize++;
        }
        else
        if (length < 256) {
            stream.write(length);
            stream.write((byte)0x81);
            resultSize+=2;
        }
        else
        if (length < 65536) {
            stream.write((byte)(length)) ;
            stream.write((byte)(length >> 8));
            stream.write((byte)0x82);
            resultSize+=3;
        }
        else
        if (length < 16777126) {
            stream.write((byte)(length));
            stream.write((byte)(length >> 8)) ;
            stream.write((byte)(length >> 16)) ;
            stream.write((byte)0x83);
            resultSize+=4;
        }
        else {
            stream.write((byte)(length));
            stream.write((byte)(length >> 8));
            stream.write((byte)(length >> 16));
            stream.write((byte)(length >> 24));
            stream.write((byte)0x84);
            resultSize+=5;
        }
        return resultSize;
    }
    */
    public int encodeNull(Object object, OutputStream stream,
                             ElementInfo elementInfo) throws Exception  {
    	throw new Exception ("NULL is restricted type in MDER specialization.");
    }

    public int encodeObjectIdentifier(Object object, OutputStream stream,
                                      ElementInfo elementInfo) throws Exception {
    	 throw new Exception ("OBJECT IDENTIFIER is an excluded ASN.1 data type in MDER specialization.");
    }
}

