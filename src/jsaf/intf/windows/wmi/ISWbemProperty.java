// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.intf.windows.wmi;

import java.math.BigInteger;

import jsaf.provider.windows.wmi.WmiException;

/**
 * An SWbemProperty interface.
 * 
 * @see "http://msdn.microsoft.com/en-us/library/aa393804(VS.85).aspx"
 */
public interface ISWbemProperty {
    /**
     * Get the name of the property.
     */
    public String getName() throws WmiException;

    /**
     * Get the value of the property, wrapped by an object provided by the underlying implementation of the WMI provider.
     */
    public Object getValue() throws WmiException;

    /**
     * Get the value of the property as an Integer.
     */
    public Integer getValueAsInteger() throws WmiException;
    
    /**
     * Get the value of the property as a Long.
     */
    public Long getValueAsLong() throws WmiException;

    /**
     * Get the value of the proeprty as a Windows Timestamp, which is the number of 100-nanosecond 'clicks' since 1601AD.
     *
     * @see org.joval.os.windows.Timestamp
     */
    public BigInteger getValueAsTimestamp() throws WmiException;

    /**
     * Get the value of the property as a Boolean.
     */
    public Boolean getValueAsBoolean() throws WmiException;

    /**
     * Get the value of the property as a String.
     */
    public String getValueAsString() throws WmiException;

    /**
     * Get the value of the property as a an array of Strings.
     */
    public String[] getValueAsArray() throws WmiException;
}
