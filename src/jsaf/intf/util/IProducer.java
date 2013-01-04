// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.intf.util;

/**
 * The producer half of the Producer/Observer pattern.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public interface IProducer {
    /**
     * Add an observer.  When the producer generates a message in the range between min and max, it notifies the observer
     * using its notify method.
     */
    public void addObserver(IObserver observer, int min, int max);

    /**
     * Remove an observer.
     */
    public void removeObserver(IObserver observer);
}
