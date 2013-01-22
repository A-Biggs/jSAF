// Copyright (C) 2012 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.provider.unix.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.TimerTask;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.slf4j.cal10n.LocLogger;

import jsaf.Message;
import jsaf.JSAFSystem;
import jsaf.intf.io.IFile;
import jsaf.intf.io.IFilesystem;
import jsaf.intf.io.IReader;
import jsaf.intf.io.IReaderGobbler;
import jsaf.intf.system.IEnvironment;
import jsaf.intf.system.IProcess;
import jsaf.intf.system.ISession;
import jsaf.intf.unix.io.IUnixFileInfo;
import jsaf.intf.unix.io.IUnixFilesystem;
import jsaf.intf.unix.io.IUnixFilesystemDriver;
import jsaf.intf.unix.system.IUnixSession;
import jsaf.intf.util.ILoggable;
import jsaf.intf.util.ISearchable;
import jsaf.io.BufferedReader;
import jsaf.io.PerishableReader;
import jsaf.io.StreamTool;
import jsaf.io.fs.AbstractFilesystem;
import jsaf.util.SafeCLI;

/**
 * ISearchable implementation for files on Unix machines.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class UnixFileSearcher implements ISearchable<IFile>, ILoggable {
    private IUnixSession session;
    private IUnixFilesystemDriver driver;
    private AbstractFilesystem fs;
    private LocLogger logger;
    private Map<String, Collection<String>> searchMap;

    public UnixFileSearcher(IUnixSession session, IUnixFilesystemDriver driver, Map<String, Collection<String>> searchMap) {
	this.session = session;
	this.driver = driver;
	logger = session.getLogger();
	fs = (AbstractFilesystem)session.getFilesystem();
	this.searchMap = searchMap;
    }

    // Implement ILogger

    public void setLogger(LocLogger logger) {
	this.logger = logger;
    }

    public LocLogger getLogger() {
	return logger;
    }

    // Implement ISearchable<IFile>

    public ICondition condition(int field, int type, Object value) {
	return new GenericCondition(field, type, value);
    }

    public String[] guessParent(Pattern p, Object... args) {
	int index = 0;
	for (Object arg : args) {
	    if (index == 0) {
		if (arg instanceof Boolean) {
		    return fs.guessParent(p, ((Boolean)arg).booleanValue());
		}
	    }
	    index++;
	}
	return fs.guessParent(p, false);
    }

    public Collection<IFile> search(List<ISearchable.ICondition> conditions) throws Exception {
	String cmd = driver.getFindCommand(conditions);
	Collection<IFile> results = new ArrayList<IFile>();
	if (searchMap.containsKey(cmd)) {
	    for (String path : searchMap.get(cmd)) {
		results.add(fs.getFile(path));
	    }
	} else {
	    logger.debug(Message.STATUS_FS_SEARCH_START, cmd);
	    File localTemp = null;
	    IFile remoteTemp = null;
	    Collection<String> paths = new ArrayList<String>();
	    try {
		//
		// Run the command on the remote host, storing the results in a temporary file, then tranfer the file
		// locally and read it.
		//
		IReader reader = null;
		remoteTemp = execToFile(cmd);
		if (session.getWorkspace() == null || ISession.LOCALHOST.equals(session.getHostname())) {
		    reader = new BufferedReader(new GZIPInputStream(remoteTemp.getInputStream()));
		} else {
		    localTemp = File.createTempFile("search", null, session.getWorkspace());
		    StreamTool.copy(remoteTemp.getInputStream(), new FileOutputStream(localTemp), true);
		    reader = new BufferedReader(new GZIPInputStream(new FileInputStream(localTemp)));
		}

		IFile file = null;
		Iterator<String> iter = new ReaderIterator(reader);
		while ((file = createObject(iter)) != null) {
		    String path = file.getPath();
		    logger.debug(Message.STATUS_FS_SEARCH_MATCH, path);
		    results.add(file);
		    paths.add(path);
		}
		logger.debug(Message.STATUS_FS_SEARCH_DONE, results.size(), cmd);
	    } catch (Exception e) {
		logger.warn(Message.ERROR_FS_SEARCH);
		logger.warn(Message.getMessage(Message.ERROR_EXCEPTION), e);
	    } finally {
		if (localTemp != null) {
		    localTemp.delete();
		}
		if (remoteTemp != null) {
		    try {
			remoteTemp.delete();
			if (remoteTemp.exists()) {
			    SafeCLI.exec("rm -f " + remoteTemp.getPath(), session, ISession.Timeout.S);
			}
		    } catch (Exception e) {
			logger.warn(Message.getMessage(Message.ERROR_EXCEPTION), e);
		    }
		}
	    }
	    searchMap.put(cmd, paths);
	}
	return results;
    }

    // Private

    private IFile createObject(Iterator<String> input) {
	UnixFileInfo info = (UnixFileInfo)driver.nextFileInfo(input);
	if (info == null) {
	    return null;
	} else if (info.getPath() == null) {
	    //
	    // Skip a bad entry and try again
	    //
	    return createObject(input);
	} else {
	    return fs.createFileFromInfo(info);
	}
    }

    /**
     * Run the command, sending its output to a temporary file, and return the temporary file.
     */
    private IFile execToFile(String command) throws Exception {
	String unique = null;
	synchronized(this) {
	    unique = Long.toString(System.currentTimeMillis());
	    Thread.sleep(1);
	}
	IEnvironment env = session.getEnvironment();
	String tempPath = env.expand("%HOME%" + IUnixFilesystem.DELIM_STR + ".jOVAL.find" + unique + ".gz");
	logger.debug(Message.STATUS_FS_SEARCH_CACHE_TEMP, tempPath);
	String cmd = new StringBuffer(command).append(" | gzip > ").append(env.expand(tempPath)).toString();

	FileMonitor mon = new FileMonitor(tempPath);
	JSAFSystem.getTimer().schedule(mon, 15000, 15000);
	SafeCLI.exec(cmd, null, null, session, session.getTimeout(ISession.Timeout.XL), new ErrorReader(), new ErrorReader());
	mon.cancel();
	JSAFSystem.getTimer().purge();
	return fs.getFile(tempPath, IFile.Flags.READWRITE);
    }

    class ReaderIterator implements Iterator<String> {
	IReader reader;
	String next = null;

	ReaderIterator(IReader reader) {
	    this.reader = reader;
	}

	// Implement Iterator<String>

	public boolean hasNext() {
	    if (next == null) {
		try {
		    next = next();
		    return true;
		} catch (NoSuchElementException e) {
		    return false;
		}
	    } else {
		return true;
	    }
	}

	public String next() throws NoSuchElementException {
	    if (next == null) {
		try {
		    if ((next = reader.readLine()) == null) {
			try {
			    reader.close();
			} catch (IOException e) {
			}
			throw new NoSuchElementException();
		    }
		} catch (IOException e) {
		    throw new NoSuchElementException(e.getMessage());
		}
	    }
	    String temp = next;
	    next = null;
	    return temp;
	}

	public void remove() {
	    throw new UnsupportedOperationException();
	}
    }

    class FileMonitor extends TimerTask {
	private String path;

	FileMonitor(String path) {
	    this.path = path;
	}

	public void run() {
	    try {
		long len = fs.getFile(path, IFile.Flags.READVOLATILE).length();
		logger.info(Message.STATUS_FS_SEARCH_CACHE_PROGRESS, len);
	    } catch (IOException e) {
	    }
	}
    }

    class ErrorReader implements IReaderGobbler {
	ErrorReader() {}

	public void gobble(IReader err) throws IOException {
	    String line = null;
	    while((line = err.readLine()) != null) {
		if (line.trim().length() > 0) {
		    logger.debug(Message.ERROR_FS_SEARCH_LINE, line);
		}
	    }
	}
    }
}
