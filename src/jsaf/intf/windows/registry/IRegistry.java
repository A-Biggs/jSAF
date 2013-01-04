// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.intf.windows.registry;

import java.util.NoSuchElementException;

import jsaf.intf.util.ILoggable;
import jsaf.intf.util.ISearchable;
import jsaf.provider.windows.registry.RegistryException;

/**
 * An interface for accessing a Windows registry.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public interface IRegistry extends ILoggable {
    /**
     * Search condition field for the hive.
     */
    int FIELD_HIVE = 100;

    /**
     * Search condition field for the key path or pattern.
     */
    int FIELD_KEY = 101;

    /**
     * Search condition field for the value name or pattern.
     */
    int FIELD_VALUE = 102;

    /**
     * Search condition field for a base-64 encoded value name.
     */
    int FIELD_VALUE_BASE64 = 103;

    String COMPUTERNAME_KEY	= "System\\CurrentControlSet\\Control\\ComputerName\\ComputerName";
    String COMPUTERNAME_VAL	= "ComputerName";

    /**
     * An enumeration of the registry hives.
     */
    enum Hive {
	HKCR ("HKEY_CLASSES_ROOT",	0x80000000L),
	HKCU ("HKEY_CURRENT_USER",	0x80000001L),
	HKLM ("HKEY_LOCAL_MACHINE",	0x80000002L),
	HKU  ("HKEY_USERS",		0x80000003L),
	HKCC ("HKEY_CURRENT_CONFIG",	0x80000005L),
	HKDD ("HKEY_DYN_DATA",		0x80000006L);

	private String name;
	private long id;

	private Hive(String name, long id) {
	    this.name = name;
	    this.id = id;
	}

	public String getName() {
	    return name;
	}

	public long getId() {
	    return id;
	}

	public static Hive fromName(String name) {
	    for (Hive hive : values()) {
		if (hive.getName().equals(name.toUpperCase())) {
		    return hive;
		}
	    }
	    return Hive.HKLM;
	}
    }

    String DELIM_STR		= "\\";
    char   DELIM_CH		= '\\';
    String ESCAPED_DELIM	= "\\\\";

    /**
     * Get Windows license data from the registry.
     *
     * @throws Exception if there was a problem retrieving the license information.
     */
    ILicenseData getLicenseData() throws Exception;

    /**
     * Get an ISearchable for the registry.
     */
    ISearchable<IKey> getSearcher();

    /**
     * Get a particular hive.
     */
    IKey getHive(Hive hive);

    /**
     * Return a key using its full path (including hive name).
     */
    IKey getKey(String fullPath) throws NoSuchElementException, RegistryException;

    /**
     * Return a key from a hive using the specified redirection mode.
     */
    IKey getKey(Hive hive, String path) throws NoSuchElementException, RegistryException;

    /**
     * Return the child subkeys of the specified key.
     */
    IKey[] enumSubkeys(IKey key) throws RegistryException;

    /**
     * Return a particular value of a key, given its name.
     *
     * @param name use null to retrieve the default value
     */
    IValue getValue(IKey key, String name) throws NoSuchElementException, RegistryException;

    /**
     * Return all the values of a key.
     */
    IValue[] enumValues(IKey key) throws RegistryException;
}
