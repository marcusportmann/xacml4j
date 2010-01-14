package com.artagon.xacml.policy.type;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.artagon.xacml.DataTypes;
import com.artagon.xacml.policy.BaseAttributeDataType;
import com.artagon.xacml.policy.type.DateTimeType.DateTimeValue;
import com.artagon.xacml.util.Preconditions;

final class DateTimeTypeImpl extends BaseAttributeDataType<DateTimeValue> implements DateTimeType
{
	private DatatypeFactory xmlDataTypesFactory;
	
	public DateTimeTypeImpl() throws DatatypeConfigurationException{
		super(DataTypes.DATETIME, XMLGregorianCalendar.class);
		this.xmlDataTypesFactory = DatatypeFactory.newInstance();
	}

	@Override
	public DateTimeValue fromXacmlString(String v) {
		Preconditions.checkNotNull(v);
		XMLGregorianCalendar dateTime = xmlDataTypesFactory.newXMLGregorianCalendar(v);
		return new DateTimeValue(this, validateXmlDateTime(dateTime));
	}
	
	@Override
	public DateTimeValue create(Object any){
		Preconditions.checkNotNull(any);
		Preconditions.checkArgument(isConvertableFrom(any), String.format(
				"Value=\"%s\" of class=\"%s\" " +
				"can't ne converted to XACML \"dateTime\" type", 
				any, any.getClass()));
		if(String.class.isInstance(any)){
			return fromXacmlString((String)any);
		}
		return new DateTimeValue(this, validateXmlDateTime((XMLGregorianCalendar)any));
	}
	
	private XMLGregorianCalendar validateXmlDateTime(XMLGregorianCalendar dateTime){
		if(!dateTime.getXMLSchemaType().equals(DatatypeConstants.DATETIME)){
			throw new IllegalArgumentException(String.format("Given value=\"%s\" does " +
					"not represent type=\"%s\"", dateTime.toXMLFormat(), DatatypeConstants.DATETIME));
		}
		return dateTime;
	}
}
