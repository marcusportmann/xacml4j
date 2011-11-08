package com.artagon.xacml.v30.types;

import java.net.InetAddress;
import java.util.Collection;

import com.artagon.xacml.v30.AttributeExp;
import com.artagon.xacml.v30.AttributeExpType;
import com.artagon.xacml.v30.BagOfAttributesExp;
import com.artagon.xacml.v30.BagOfAttributesExpType;
import com.artagon.xacml.v30.core.IPAddress;
import com.artagon.xacml.v30.core.PortRange;

/** 
 * XACML DataType:  <b>urn:oasis:names:tc:xacml:2.0:data-type:ipAddress</b>. 
 * <br>The "urn:oasis:names:tc:xacml:2.0:data-type:ipAddress" primitive 
 * type represents an IPv4 or IPv6 network address, with optional mask 
 * and optional port or port range. The syntax SHALL be:
 * <pre>
 *     ipAddress = address [ "/" mask ] [ ":" [ portrange ] ]
 * </pre>
 * For an IPv4 address, the address and mask are formatted in accordance 
 * with the syntax for a "host" in IETF RFC 2396 "Uniform Resource Identifiers 
 * (URI): Generic Syntax", section 3.2. 
 * <p>
 * For an IPv6 address, the address and mask are formatted in accordance with 
 * the syntax for an "ipv6reference" in IETF RFC 2732 "Format for Literal IPv6 
 * Addresses in URL's". (Note that an IPv6 address or mask, in this syntax, 
 * is enclosed in literal "[" "]" brackets.) 
 */
public enum IPAddressType implements AttributeExpType
{
	IPADDRESS("urn:oasis:names:tc:xacml:2.0:data-type:ipAddress");
	
	private String typeId;
	private BagOfAttributesExpType bagType;
	
	private IPAddressType(String typeId){
		this.typeId = typeId;
		this.bagType = new BagOfAttributesExpType(this);
	}
	
	public IPAddressValueExp create(InetAddress address, PortRange portRange) {
		return new IPAddressValueExp(this, new IPAddress(address, portRange));
	}

	public IPAddressValueExp create(InetAddress address, InetAddress mask) {
		return new IPAddressValueExp(this, new IPAddress(address, mask));
	}

	public IPAddressValueExp create(InetAddress address, InetAddress mask,
			PortRange portRange) {
		return new IPAddressValueExp(this, new IPAddress(address, mask, portRange));
	}
	
	public boolean isConvertableFrom(Object any) {
		return String.class.isInstance(any) 
		|| InetAddress.class.isInstance(any) || IPAddress.class.isInstance(any);
	}

	@Override
	public IPAddressValueExp create(Object any, Object ...params) {
		return new IPAddressValueExp(this, IPAddress.parse(any));
	}
	
	@Override
	public IPAddressValueExp fromXacmlString(String v, Object ...params) 
	{
		return new IPAddressValueExp(this, IPAddress.parse(v));
	}
	
	@Override
	public String getDataTypeId() {
		return typeId;
	}

	@Override
	public BagOfAttributesExpType bagType() {
		return bagType;
	}

	@Override
	public BagOfAttributesExp bagOf(AttributeExp... values) {
		return bagType.create(values);
	}

	@Override
	public BagOfAttributesExp bagOf(Collection<AttributeExp> values) {
		return bagType.create(values);
	}
	
	@Override
	public BagOfAttributesExp bagOf(Object... values) {
		return bagType.bagOf(values);
	}
	
	@Override
	public BagOfAttributesExp emptyBag() {
		return bagType.createEmpty();
	}
	
	@Override
	public String toString(){
		return typeId;
	}
}