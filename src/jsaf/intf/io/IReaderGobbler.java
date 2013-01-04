// Copyright (C) 2012 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.intf.io;

import java.io.IOException;

/**
 * An interface for something that gobbles an IReader.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public interface IReaderGobbler {
    /**
     * Handle data from the reader. No effort should be made to catch any IOException.
     */
    void gobble(IReader reader) throws IOException;
} 
