// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.intf.cisco.system;

import jsaf.intf.netconf.INetconf;
import jsaf.intf.ssh.system.IShell;

/**
 * A representation of an IOS command-line session.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public interface IIosSession extends INetconf {
    /**
     * Property indicating the number of milliseconds to wait for a command to begin to return data.
     */
    public static final String PROP_READ_TIMEOUT = "ios.read.timeout";

    /**
     * Retrieve "show tech-support" data from the device.
     */
    ITechSupport getTechSupport();

    /**
     * Obtain a shell connection to the device.
     */
    IShell getShell() throws Exception;
}
