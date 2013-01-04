// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.intf.unix.system;

import jsaf.Message;
import jsaf.intf.system.ISession;
import jsaf.util.SafeCLI;

/**
 * A representation of a Unix command-line session.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public interface IUnixSession extends ISession {
    /**
     * Property indicating the number of milliseconds to wait for a read before quiting.
     */
    String PROP_SUDO_READ_TIMEOUT = "read.timeout.sudo";

    Flavor getFlavor();

    /**
     * Enumeration of Unix flavors.
     */
    enum Flavor {
	UNKNOWN("unknown"),
	AIX("AIX"),
	LINUX("Linux"),
	MACOSX("Darwin"),
	SOLARIS("SunOS");
    
	private String value = null;
    
	private Flavor(String value) {
	    this.value = value;
	}

	public String value() {
	    return value;
	}

	public static Flavor flavorOf(String value) {
	    for (Flavor flavor : values()) {
		if (flavor.value().equals(value)) {
		    return flavor;
		}
	    }
	    return UNKNOWN;
	}
    
	public static Flavor flavorOf(IUnixSession session) {
	    Flavor flavor = UNKNOWN;
	    try {
		String osName = SafeCLI.exec("uname -s", session, Timeout.S);
		for (Flavor f : values()) {
		    if (f.value().equals(osName)) {
			flavor = f;
			break;
		    }
		}
	    } catch (Exception e) {
		session.getLogger().warn(Message.ERROR_UNIX_FLAVOR);
		session.getLogger().warn(Message.getMessage(Message.ERROR_EXCEPTION), e);
	    }
	    return flavor;
	}
    }
}
