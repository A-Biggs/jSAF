// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf.util;

import java.util.Hashtable;
import java.util.Iterator;

import jsaf.intf.util.IObserver;
import jsaf.intf.util.IProducer;

/**
 * Utility class for an IProducer.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class Producer implements IProducer {
    Hashtable<IObserver, ObserverContext> observers;

    public Producer() {
	observers = new Hashtable<IObserver, ObserverContext>();
    }

    public void sendNotify(int msg, Object arg) {
	Iterator<ObserverContext> observerIter = observers.values().iterator();
	while(observerIter.hasNext()) {
	    observerIter.next().sendNotify(this, msg, arg);
	}
    }

    // Implement IProducer

    public void addObserver(IObserver observer, int min, int max) {
	if (!observers.containsKey(observer)) {
	    observers.put(observer, new ObserverContext(observer, min, max));
	}
    }

    public void removeObserver(IObserver observer) {
	if (observers.containsKey(observer)) {
	    observers.remove(observer);
	}
    }

    // Private

    private class ObserverContext {
	private int min, max;
	private IObserver observer;

	private ObserverContext(IObserver observer, int min, int max) {
	    this.observer = observer;
	    this.min = min;
	    this.max = max;
	}

	private boolean sendNotify(IProducer producer, int msg, Object arg) {
	    if (msg >= min && msg <= max) {
		observer.notify(producer, msg, arg);
		return true;
	    } else {
		return false;
	    }
	}
    }
}
