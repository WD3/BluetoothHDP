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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.SortedMap;

import org.bn.annotations.ASN1Any;
import org.bn.annotations.constraints.ASN1ValueRangeConstraint;
import org.bn.coders.*;
import org.bn.metadata.ASN1SequenceOfMetadata;
import org.bn.metadata.constraints.ASN1ValueRangeConstraintMetadata;
import org.bn.metadata.constraints.IASN1ConstraintMetadata;
import org.bn.types.*;
import org.bn.utils.BitArrayInputStream;

public class MDERDecoder extends Decoder {

    public DecodedObject decodeTag(InputStream stream) throws Exception {
    	/* no tagging for simple types are used in MDER */
    	return null;
    }

    protected boolean checkTagForObject(DecodedObject decodedTag, int tagClass, int elementType, int universalTag,
                                        ElementInfo elementInfo) {
        int definedTag = MDERCoderUtils.getTagValueForElement(elementInfo,tagClass,elementType,universalTag).getValue();
        return definedTag == (Integer)decodedTag.getValue();
    }

    public DecodedObject decodeSequence(DecodedObject decodedTag,Class objectClass,
                                           ElementInfo elementInfo, InputStream stream) throws Exception {
        if(CoderUtils.isSequenceSet(elementInfo))
    		throw new Exception ("SET is an excluded ASN.1 data type in MDER specialization.");


        Object sequence = createInstanceForElement(objectClass, elementInfo);
        initDefaultValues(sequence, elementInfo);

        Field[] fields = elementInfo.getFields(objectClass);

        DecodedObject dobj;
        int size=0;

        ElementInfo info = new ElementInfo();
        for ( int idx=0; idx<fields.length; idx++) {
        	Field field = fields[idx];
            if(!field.isSynthetic()) {
                if(elementInfo.hasPreparedInfo()) {
                    info.setPreparedInfo(elementInfo.getPreparedInfo().getFieldMetadata(idx));
                }
                if(CoderUtils.isOptionalField(field, info)) {
                	throw new Exception ("A component of a SEQUENCE may not be specified with OPTIONAL");
                }
                else {
                    dobj = decodeSequenceField(null,sequence,idx,field,stream,elementInfo,true);
                    size += dobj.getSize();
                }
            }
        }

       return new DecodedObject(sequence, size);
    }

    protected DecodedObject decodeSet(DecodedObject decodedTag,Class objectClass,
                                      ElementInfo elementInfo, Integer len,InputStream stream) throws Exception {
    	throw new Exception ("SET is an excluded ASN.1 data type in MDER specialization.");
    }


    public DecodedObject decodeEnumItem(DecodedObject decodedTag, Class objectClass, Class enumClass,
                                           ElementInfo elementInfo,
                                    InputStream stream) throws Exception {
    	throw new Exception ("ENUMERATED is an excluded ASN.1 data type in MDER specialization.");
    }

    public DecodedObject decodeBoolean(DecodedObject decodedTag, Class objectClass,
                                          ElementInfo elementInfo,
                                   InputStream stream) throws Exception {
    	throw new Exception ("BOOLEAN is an excluded ASN.1 data type in MDER specialization.");
    }

    public DecodedObject decodeAny(DecodedObject decodedTag, Class objectClass,
                                      ElementInfo elementInfo,
                               InputStream stream) throws Exception {

    	DecodedObject dobj = new DecodedObject<Integer>(
    			MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16),
    			MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16));
        DecodedObject<Long> lVal = decodeLongValue(stream, dobj, false);
        DecodedObject<Integer> length = new DecodedObject<Integer>( (int)((long)lVal.getValue()),
        		MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16) );

        int bufSize = length.getValue();
        ByteArrayOutputStream anyStream;

        if(bufSize==0){
        	//Length can be 0, wich means the any_data field doesn not exist
        	anyStream = new ByteArrayOutputStream(0);
            return new DecodedObject(anyStream.toByteArray(),MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16));
        }

        anyStream = new ByteArrayOutputStream(1024);
        int len = 0;

        byte[] buffer = new byte[bufSize];

        int readed=0;
        do{
        	readed = stream.read(buffer);
        	if (readed>0) {
	        	anyStream.write(buffer,0,readed);
	        	len+=readed;
        	}
        }while(len < bufSize);

        CoderUtils.checkConstraints(len,elementInfo);
        return new DecodedObject(anyStream.toByteArray(),len + MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16));
    }

    public DecodedObject decodeNull(DecodedObject decodedTag, Class objectClass,
                                       ElementInfo elementInfo,
                                InputStream stream) throws Exception {
    	throw new Exception ("NULL is restricted type in MDER specialization.");
    }

    public DecodedObject decodeInteger(DecodedObject decodedTag, Class objectClass,
                                          ElementInfo elementInfo,
                                   InputStream stream) throws Exception {
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
        }else return null;

        return decodeIntegerValue(MDERCoderUtils.getIntegerSubtype(min,max),
        						  stream,
        						  objectClass,
        						  elementInfo);
    }

    public DecodedObject decodeReal(DecodedObject decodedTag, Class objectClass,
                                       ElementInfo elementInfo,
                                   InputStream stream) throws Exception {
    	throw new Exception ("OREAL is an excluded ASN.1 data type in MDER specialization. Use FLOAT-Type");
    }

    public DecodedObject decodeChoice(DecodedObject decodedTag, Class objectClass,
                                         ElementInfo elementInfo,
                                   InputStream stream) throws Exception {
    	DecodedObject dobj = new DecodedObject<Integer>(
    			MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16),
    			MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16));

        DecodedObject<Long> lVal = decodeLongValue(stream, dobj, false);
        DecodedObject<Integer> childDecodedTag = new DecodedObject<Integer>( (int)((long)lVal.getValue()),
        		MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16) );

        lVal = decodeLongValue(stream, dobj, false);
        DecodedObject<Integer> lenOfChild = new DecodedObject<Integer>( (int)((long)lVal.getValue()),
        		MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16) );

        Object choice = createInstanceForElement(objectClass,elementInfo);
        DecodedObject value = null;

        Field[] fields = elementInfo.getFields(objectClass);

        int size = 0;
        int fieldIdx = 0;
        for ( Field field : fields ) {
            if(!field.isSynthetic()) {
                ElementInfo info = new ElementInfo();
                info.setAnnotatedClass(field);
                if(elementInfo.hasPreparedInfo()) {
                    info.setPreparedInfo(elementInfo.getPreparedInfo().getFieldMetadata(fieldIdx));
                }
                else
                    info.setASN1ElementInfoForClass(field);
                if(CoderUtils.isMemberClass(field.getType(),info)) {
                //if(field.getType().isMemberClass()) {
                    info.setParentObject(choice);
                }
                info.setGenericInfo(field.getGenericType());

                if (info.getPreparedASN1ElementInfo()!=null && info.getPreparedASN1ElementInfo().hasTag()){
                	if (info.getPreparedASN1ElementInfo().getTag() == childDecodedTag.getValue()){
                		value = decodeClassType(decodedTag, field.getType(),info,stream);
                		size += value.getSize();
                	}
                }else if (fieldIdx==childDecodedTag.getValue()){
                	value = decodeClassType(decodedTag, field.getType(),info,stream);
                	size += value.getSize();
                }

                fieldIdx++;
                if(value!=null) {
                    invokeSelectMethodForField(field, choice, value.getValue(),info);
                    break;
                };

            }
        }
        if(value == null && !CoderUtils.isOptional(elementInfo)) {
            throw new  IllegalArgumentException ("The choice '" + objectClass.toString() + "' does not have a selected item!");
        }
        if (lenOfChild.getValue()!=size)
        	throw new  IllegalArgumentException ("The choice '" + objectClass.toString() +
        			"' has length of " + lenOfChild.getValue() + " bytes, readed " + size + " bytes!");

        DecodedObject<Object> result = new DecodedObject(choice, value!=null ? value.getSize(): 0);
        result.setSize(result.getSize() + childDecodedTag.getSize() + lenOfChild.getSize());

        return result;
    }

    protected DecodedObject decodeIntegerValue(MDERCoderUtils.RestrictedInteger intType,
    		InputStream stream, Class objectClass, ElementInfo elementInfo) throws Exception {
    	/*No tags are sent in MDER form simple types*/
    	DecodedObject dobj = new DecodedObject<Integer>(MDERCoderUtils.getIntegerSubtypeLength(intType),
    			MDERCoderUtils.getIntegerSubtypeLength(intType));

    	boolean signed= (intType==MDERCoderUtils.RestrictedInteger.INT_I8 ||
    					intType==MDERCoderUtils.RestrictedInteger.INT_I16 ||
    					intType==MDERCoderUtils.RestrictedInteger.INT_I32 );
        DecodedObject<Long> lVal = decodeLongValue(stream, dobj, signed);
        if(objectClass.equals(Integer.class)) {
        	DecodedObject<Integer> result = new DecodedObject<Integer>( (int)((long)lVal.getValue()), MDERCoderUtils.getIntegerSubtypeLength(intType) );
        	CoderUtils.checkConstraints(result.getValue(),elementInfo);
        	return result;
        }else{
        	CoderUtils.checkConstraints(lVal.getValue(),elementInfo);
        	return lVal;
        }
    }

    public DecodedObject<Long> decodeLongValue(InputStream stream,
            DecodedObject<Integer> len, boolean signed) throws Exception {
		DecodedObject<Long> result = new DecodedObject<Long>();
		long value =0;
		for(int i=0;i<len.getValue();i++) {
			int bt = stream.read();
			if (bt == -1 ) {
				throw new IllegalArgumentException("Unexpected EOF when decoding!");
			}

			if( signed && i == 0 && (bt & (byte)0x80)!=0) {
				bt = bt - 256;
			}

			value = (value << 8) | bt ;
		}

		result.setValue(value);
		result.setSize(len.getSize());
		return result;
	}

    public DecodedObject<Long> decodeLongValue(InputStream stream,
                                               DecodedObject<Integer> len) throws Exception {
        DecodedObject<Long> result = new DecodedObject<Long>();
        long value =0;
        for(int i=0;i<len.getValue();i++) {
            int bt = stream.read();
            if (bt == -1 ) {
                throw new IllegalArgumentException("Unexpected EOF when decoding!");
            }

            if( i == 0 && (bt & (byte)0x80)!=0) {
                bt = bt - 256;
            }

            value = (value << 8) | bt ;
        }
        result.setValue(value);
        result.setSize(len.getValue() +  len.getSize());
        return result;
    }

    public DecodedObject decodeOctetString(DecodedObject decodedTag, Class objectClass,
                                              ElementInfo elementInfo,
                                       InputStream stream) throws Exception {
    	DecodedObject<Integer> len = null;
    	int size = 0;

    	if (MDERCoderUtils.isFixedOctetString(elementInfo)){
    		len = new DecodedObject<Integer>((int)MDERCoderUtils.getLengthFixedOctetString(elementInfo),
        			MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16) );
        	CoderUtils.checkConstraints(len.getValue(),elementInfo);
        	size = len.getValue();
    	}else{
    		DecodedObject dobj = new DecodedObject<Integer>(
					MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16),
					MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16));
			DecodedObject<Long> lVal = decodeLongValue(stream, dobj, false);
			len = new DecodedObject<Integer>( (int)((long)lVal.getValue()),
			MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16) );
			size = len.getValue() + MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16);
    	}


        byte[] byteBuf = new byte[ len.getValue()];
        stream.read(byteBuf);
        return new DecodedObject(byteBuf, size);
    }

    public DecodedObject decodeBitString(DecodedObject decodedTag, Class objectClass,
                                            ElementInfo elementInfo,
                                       InputStream stream) throws Exception {
        MDERCoderUtils.RestrictedBitString bsType = MDERCoderUtils.checkBitStringConstraints(elementInfo);
        int sizeOfString = MDERCoderUtils.getBitStringSubtypeLength(bsType);
        byte[] byteBuf = new byte[ sizeOfString ];
        CoderUtils.checkConstraints(sizeOfString*8,elementInfo);

        stream.read(byteBuf);

        DecodedObject<BitString> result = new DecodedObject<BitString>();
        result.setValue(new BitString(byteBuf));
        result.setSize(sizeOfString);
        return result;
    }

    public DecodedObject decodeString(DecodedObject decodedTag, Class objectClass,
                                         ElementInfo elementInfo,
                                  InputStream stream) throws Exception {
    	throw new Exception ("String is an excluded ASN.1 data type in MDER specialization.");
    }

    public DecodedObject decodeSequenceOf(DecodedObject decodedTag, Class objectClass,
                                             ElementInfo elementInfo,
                                      InputStream stream) throws Exception {

        Collection result = new LinkedList();

    	DecodedObject dobj = new DecodedObject<Integer>(
    			MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16),
    			MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16));
        DecodedObject<Long> lVal = decodeLongValue(stream, dobj, false);
        DecodedObject<Integer> count = new DecodedObject<Integer>( (int)((long)lVal.getValue()),
        		MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16) );

        lVal = decodeLongValue(stream, dobj, false);
        DecodedObject<Integer> len = new DecodedObject<Integer>( (int)((long)lVal.getValue()),
        		MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16) );

        if(len.getValue()!=0) {
            int lenOfItems = 0;
            int cntOfItems = 0;
            Class paramType = CoderUtils.getCollectionType(elementInfo);

            do {
                ElementInfo info = new ElementInfo();
                info.setAnnotatedClass(paramType);
                info.setParentAnnotated(elementInfo.getAnnotatedClass());
                if(elementInfo.hasPreparedInfo()) {
                    ASN1SequenceOfMetadata seqOfMeta = (ASN1SequenceOfMetadata)elementInfo.getPreparedInfo().getTypeMetadata();
                    info.setPreparedInfo( seqOfMeta.getItemClassMetadata() );
                }

                DecodedObject item=decodeClassType(null,paramType,info,stream);
                if(item!=null) {
                    lenOfItems+=item.getSize();
                    result.add(item.getValue());
                    cntOfItems++;
                }
            }
            while(lenOfItems < len.getValue());
            CoderUtils.checkConstraints ( cntOfItems ,elementInfo );
        }
        return new DecodedObject(result, MDERCoderUtils.getIntegerSubtypeLength(MDERCoderUtils.RestrictedInteger.INT_U16)*2 +len.getValue());
    }


    public DecodedObject decodeObjectIdentifier(DecodedObject decodedTag,
                                                Class objectClass,
                                                ElementInfo elementInfo,
                                                InputStream stream) throws Exception {
    	throw new Exception ("OBJECT IDENTIFIER is an excluded ASN.1 data type in MDER specialization.");
    }
}
