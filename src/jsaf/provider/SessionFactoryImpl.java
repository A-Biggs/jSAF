// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.provider;

import java.io.File;

import jsaf.intf.io.IFilesystem;
import jsaf.intf.system.IRemote;
import jsaf.intf.system.ISession;
import jsaf.provider.unix.system.UnixSession;
import jsaf.provider.windows.system.WindowsSession;

/**
 * Local provider SessionFactory implementation class.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class SessionFactoryImpl extends SessionFactory {
    private File workspace;

    public SessionFactoryImpl (File workspace) {
	this.workspace = workspace;
    }

    // Implement abstract methods of SessionFactory

    /**
     * This is a SessionFactory implementation for a local provider, which does not support remote sessions.
     */
    public IRemote getRemote() {
	throw new UnsupportedOperationException("getRemote");
    }

    public ISession createSession() {
	return createSession(ISession.LOCALHOST);
    }

    public ISession createSession(String target) {
	if (!ISession.LOCALHOST.equals(target)) {
	    throw new IllegalArgumentException(target);
	}
	File wsdir = null;
	if (workspace != null) {
	    wsdir = new File(workspace, target);
	    if (!wsdir.exists()) {
		wsdir.mkdirs();
	    }
	}
	ISession session = null;
	if (System.getProperty("os.name").startsWith("Windows")) {
	    session = new WindowsSession(wsdir);
	} else {
	    session = new UnixSession(wsdir);
	}
	if (workspace == null) {
	    session.getProperties().setProperty(IFilesystem.PROP_CACHE_JDBM, null);
	}
	return session;
    }
}
