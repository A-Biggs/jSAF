// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.provider.unix.io.driver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.slf4j.cal10n.LocLogger;

import jsaf.Message;
import jsaf.intf.io.IFile;
import jsaf.intf.io.IFileMetadata;
import jsaf.intf.io.IFilesystem;
import jsaf.intf.io.IReader;
import jsaf.intf.unix.io.IUnixFileInfo;
import jsaf.intf.unix.io.IUnixFilesystem;
import jsaf.intf.unix.io.IUnixFilesystemDriver;
import jsaf.intf.unix.system.IUnixSession;
import jsaf.intf.util.ISearchable;
import jsaf.io.PerishableReader;
import jsaf.provider.unix.io.UnixFileInfo;
import jsaf.util.SafeCLI;
import jsaf.util.StringTools;

/**
 * IUnixFilesystemDriver implementation for Mac OS X.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class MacOSXDriver extends AbstractDriver {
    public MacOSXDriver(IUnixSession session) {
	super(session);
    }

    // Implement IUnixFilesystemDriver

    static final String OPEN = "(";
    static final String CLOSE = ")";

    public Collection<IFilesystem.IMount> getMounts(Pattern typeFilter) throws Exception {
	Collection<IFilesystem.IMount> mounts = new ArrayList<IFilesystem.IMount>();
	for (String line : SafeCLI.multiLine("mount", session, IUnixSession.Timeout.S)) {
	    int ptr = line.indexOf(" on ");
	    String device = line.substring(0, ptr);
	    int begin = ptr + 4;
	    ptr = line.indexOf(OPEN);
	    String path = line.substring(begin, ptr).trim();
	    begin = ptr;
	    ptr = line.indexOf(CLOSE, begin);
	    StringTokenizer tok = new StringTokenizer(line.substring(begin, ptr), ",");
	    String fsType = null;
	    while(tok.hasMoreTokens()) {
		String attr = tok.nextToken().trim();
		if (attr.endsWith("fs")) {
		    fsType = attr;
		    break;
		}
	    }
	    if (fsType != null) {
		if (typeFilter != null && typeFilter.matcher(fsType).find()) {
		    logger.info(Message.STATUS_FS_MOUNT_SKIP, path, fsType);
		} else if (path.startsWith(IUnixFilesystem.DELIM_STR)) {
		    logger.info(Message.STATUS_FS_MOUNT_ADD, path, fsType);
		    mounts.add(new Mount(path, fsType));
		}
	    }
	}
	return mounts;
    }

    public String getFindCommand(List<ISearchable.ICondition> conditions) {
	String from = null;
	boolean dirOnly = false;
	boolean followLinks = false;
	boolean xdev = false;
	Pattern path = null, dirname = null, basename = null;
	String literalBasename = null, antiBasename = null;
	int depth = ISearchable.DEPTH_UNLIMITED;

	for (ISearchable.ICondition condition : conditions) {
	    switch(condition.getField()) {
	      case IUnixFilesystem.FIELD_FOLLOW_LINKS:
		followLinks = true;
		break;
	      case IUnixFilesystem.FIELD_XDEV:
		xdev = true;
		break;
	      case IFilesystem.FIELD_FILETYPE:
		if (IFilesystem.FILETYPE_DIR.equals(condition.getValue())) {
		    dirOnly = true;
		}
		break;
	      case IFilesystem.FIELD_PATH:
		path = (Pattern)condition.getValue();
		break;
	      case IFilesystem.FIELD_DIRNAME:
		dirname = (Pattern)condition.getValue();
		break;
	      case IFilesystem.FIELD_BASENAME:
		switch(condition.getType()) {
		  case ISearchable.TYPE_INEQUALITY:
		    antiBasename = (String)condition.getValue();
		    break;
		  case ISearchable.TYPE_EQUALITY:
		    literalBasename = (String)condition.getValue();
		    break;
		  case ISearchable.TYPE_PATTERN:
		    basename = (Pattern)condition.getValue();
		    break;
		}
		break;
	      case ISearchable.FIELD_DEPTH:
		depth = ((Integer)condition.getValue()).intValue();
		break;
	      case ISearchable.FIELD_FROM:
		from = (String)condition.getValue();
		break;
	    }
	}

	StringBuffer sb = new StringBuffer("find");
	if (followLinks) {
	    sb.append(" -L");
	}
	String FIND = sb.toString();
	StringBuffer cmd = new StringBuffer(FIND).append(" ").append(from);
	if (xdev) {
	    cmd.append(" -mount");
	}
	if (depth != ISearchable.DEPTH_UNLIMITED) {
	    cmd.append(" -maxdepth ").append(Integer.toString(depth));
	}
	if (dirOnly) {
	    cmd.append(" -type d");
	    if (dirname != null) {
		cmd.append(" | grep -E \"").append(dirname.pattern()).append("\"");
	    }
	} else {
	    if (path != null) {
		cmd.append(" | grep -E \"").append(path.pattern()).append("\"");
	    } else {
		if (dirname != null) {
		    cmd.append(" -type d");
		    cmd.append(" | grep -E \"").append(dirname.pattern()).append("\"");
		    cmd.append(" | xargs -I{} ").append(FIND).append(" '{}' -maxdepth 1");
		}
		cmd.append(" -type f");
		if (basename != null) {
		    cmd.append(" | awk -F/ '$NF ~ /");
		    cmd.append(basename.pattern());
		    cmd.append("/'");
		} else if (antiBasename != null) {
		    cmd.append(" ! -name \"").append(antiBasename).append("\"");
		} else if (literalBasename != null) {
		    cmd.append(" -name \"").append(literalBasename).append("\"");
		}
	    }
	}
	cmd.append(" | xargs -I{} ").append(getStatCommand()).append(" '{}'");
	return cmd.toString();
    }

    public String getStatCommand() {
	return "ls -ldnT";
    }

    public UnixFileInfo nextFileInfo(Iterator<String> lines) {
	String line = null;
	if (lines.hasNext()) {
	    line = lines.next();
	} else {
	    return null;
	}

	char unixType = line.charAt(0);
	String permissions = line.substring(1, 10);
	boolean hasExtendedAcl = false;
	if (line.charAt(10) == '+') {
	    hasExtendedAcl = true;
	}

	StringTokenizer tok = new StringTokenizer(line.substring(11));
	String linkCount = tok.nextToken();
	int uid = -1;
	try {
	    uid = Integer.parseInt(tok.nextToken());
	} catch (NumberFormatException e) {
	    //DAS -- could be, e.g., 4294967294 (illegal "nobody" value)
	}
	int gid = -1;
	try {
	    gid = Integer.parseInt(tok.nextToken());
	} catch (NumberFormatException e) {
	    //DAS -- could be, e.g., 4294967294 (illegal "nobody" value)
	}

	IFileMetadata.Type type = IFileMetadata.Type.FILE;
	switch(unixType) {
	  case IUnixFileInfo.DIR_TYPE:
	    type = IFileMetadata.Type.DIRECTORY;
	    break;

	  case IUnixFileInfo.LINK_TYPE:
	    type = IFileMetadata.Type.LINK;
	    break;

	  case IUnixFileInfo.CHAR_TYPE:
	  case IUnixFileInfo.BLOCK_TYPE:
	    int ptr = -1;
	    if ((ptr = line.indexOf(",")) > 11) {
		tok = new StringTokenizer(line.substring(ptr+1));
	    }
	    break;
	}

	long length = 0;
	try {
	    length = Long.parseLong(tok.nextToken());
	} catch (NumberFormatException e) {
	}

	long mtime = IFile.UNKNOWN_TIME;
	String dateStr = tok.nextToken("/").trim();
	try {
	    mtime = new SimpleDateFormat("MMM dd HH:mm:ss yyyy").parse(dateStr).getTime();
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	String path = null, linkPath = null;
	int begin = line.indexOf(session.getFilesystem().getDelimiter());
	if (begin > 0) {
	    int end = line.indexOf("->");
	    if (end == -1) {
		path = line.substring(begin).trim();
	    } else if (end > begin) {
		path = line.substring(begin, end).trim();
		linkPath = line.substring(end+2).trim();
	    }
	}

	return new UnixFileInfo(type, path, linkPath, IFile.UNKNOWN_TIME, mtime, IFile.UNKNOWN_TIME, length,
				unixType, permissions, uid, gid, hasExtendedAcl, null);
    }
}
