// Copyright (C) 2012 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.intf.util;

import java.util.Properties;

/**
 * An interface representing something that can have properties. Iterates on the property keys.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public interface IProperty extends Iterable<String> {
    /**
     * Returns the value associates with the key.  Returns null if there is no corresponding value defined for the key.
     */
    String getProperty(String key);

    /**
     * Returns the value of key as an int.  Returns 0 if there is no corresponding value defined for the key.
     */
    int getIntProperty(String key);

    /**
     * Returns the value of key as an long.  Returns 0 if there is no corresponding value defined for the key.
     */
    long getLongProperty(String key);

    /**
     * Returns the value of key as a boolean.  Returns false if there is no corresponding value defined for the key.
     */
    boolean getBooleanProperty(String key);

    /**
     * Set the value for the specified key.  Set the value to null to remove the property.
     */
    void setProperty(String key, String value);

    Properties toProperties();
}
