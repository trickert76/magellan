/*
 *  Copyright (C) 2000-2003 Roger Butenuth, Andreas Gampe,
 *                          Stefan Goetz, Sebastian Pappert,
 *                          Klaas Prause, Enno Rehling,
 *                          Sebastian Tusk, Ulrich Kuester,
 *                          Ilja Pavkovic
 *
 * This file is part of the Eressea Java Code Base, see the
 * file LICENSING for the licensing information applying to
 * this file.
 *
 */

package com.eressea.event;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import com.eressea.util.CollectionFactory;
import com.eressea.util.logging.Logger;

/**
 * A class forwarding events from event sources to listeners.
 */
public class EventDispatcher {
	private static final Logger log					  = Logger.getInstance(EventDispatcher.class);
	private List			    gameDataListeners     = CollectionFactory.createLinkedList();
	private List			    tempUnitListeners     = CollectionFactory.createLinkedList();
	private List			    unitOrdersListeners   = CollectionFactory.createLinkedList();
	private List			    selectionListeners    = CollectionFactory.createLinkedList();
	private List			    orderConfirmListeners = CollectionFactory.createLinkedList();
	private boolean			    notifierIsAlive		  = false;
	private boolean			    stopNotification	  = false;
	private int				    eventsFired			  = 0;
	private int				    eventsDispatched	  = 0;
	private int				    lastPriority		  = Integer.MAX_VALUE;
	private static final int    GAMEDATA_INDEX		  = 0;
	private static final int    SELECTION_INDEX		  = 1;
	private static final int    UNITORDERS_INDEX	  = 2;
	private static final int    TEMPUNIT_INDEX		  = 3;
	private static final int    ORDERCONFIRM_INDEX    = 4;
	private static final int    PRIORITIES[]		  = { 0, 4, 1, 1, 1 };
	private EQueue			    queue;

	private EventDispatcher() {
		queue = new EQueue();

		Thread t = new Thread(new EManager());

		//t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	private static final EventDispatcher INSTANCE = new EventDispatcher();

	/**
	 * Returns the shared instance of the event dispatcher. This will create a
	 * new one if there's no current one. This is the Singleton pattern.
	 *
	 * @return TODO: DOCUMENT ME!
	 */
	public static EventDispatcher getDispatcher() {
		return INSTANCE;
	}

	/**
	 * Adds a listener for selection events.
	 *
	 * @param l the listener to add.
	 *
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener l) {
		selectionListeners.add(l);
	}

	/**
	 * Adds the given selection listener to the front of all registered
	 * listeners. Warning: The order will change if another listener is added
	 * with priority.
	 *
	 * @param l TODO: DOCUMENT ME!
	 */
	public void addPrioritySelectionListener(SelectionListener l) {
		selectionListeners.add(0, l);
	}

	/**
	 * Removes the specified listener for selection events.
	 *
	 * @param l the listener to remove.
	 *
	 * @return true if this list contained the specified element.
	 *
	 * @see SelectionEvent
	 */
	public boolean removeSelectionListener(SelectionListener l) {
		return selectionListeners.remove(l);
	}

	/**
	 * Adds a listener for game data events.
	 *
	 * @param l the listener to add.
	 *
	 * @see GameDataEvent
	 */
	public void addGameDataListener(GameDataListener l) {
		gameDataListeners.add(l);
	}

	/**
	 * Adds the given game-data listener to the front of all registered
	 * listeners. Warning: The order will change if another listener is added
	 * with priority.
	 *
	 * @param l TODO: DOCUMENT ME!
	 */
	public void addPriorityGameDataListener(GameDataListener l) {
		gameDataListeners.add(0, l);
	}

	/**
	 * Removes the specified listener for game data events.
	 *
	 * @param l the listener to remove.
	 *
	 * @return true if this list contained the specified element.
	 *
	 * @see GameDataEvent
	 */
	public boolean removeGameDataListener(GameDataListener l) {
		return gameDataListeners.remove(l);
	}

	/**
	 * Adds a listener for temp unit events.
	 *
	 * @param l the listener to add.
	 *
	 * @see TempUnitEvent
	 */
	public void addTempUnitListener(TempUnitListener l) {
		tempUnitListeners.add(l);
	}

	/**
	 * Adds the given temp-unit listener to the front of all registered
	 * listeners. Warning: The order will change if another listener is added
	 * with priority.
	 *
	 * @param l TODO: DOCUMENT ME!
	 */
	public void addPriorityTempUnitListener(TempUnitListener l) {
		tempUnitListeners.add(0, l);
	}

	/**
	 * Removes the specified listener for temp unit events.
	 *
	 * @param l the listener to remove.
	 *
	 * @return true if this list contained the specified element.
	 *
	 * @see TempUnitEvent
	 */
	public boolean removeTempUnitListener(TempUnitListener l) {
		return tempUnitListeners.remove(l);
	}

	/**
	 * Adds a listener for unit orders events.
	 *
	 * @param l the listener to add.
	 *
	 * @see UnitOrdersEvent
	 */
	public void addUnitOrdersListener(UnitOrdersListener l) {
		unitOrdersListeners.add(l);
	}

	/**
	 * Adds the given unit-orders listener to the front of all registered
	 * listeners. Warning: The order will change if another listener is added
	 * with priority.
	 *
	 * @param l TODO: DOCUMENT ME!
	 */
	public void addPriorityUnitOrdersListener(UnitOrdersListener l) {
		unitOrdersListeners.add(0, l);
	}

	/**
	 * Removes the specified listener for unit order events.
	 *
	 * @param l the listener to remove.
	 *
	 * @return true if this list contained the specified element.
	 *
	 * @see UnitOrdersEvent
	 */
	public boolean removeUnitOrdersListener(UnitOrdersListener l) {
		return unitOrdersListeners.remove(l);
	}

	/**
	 * Removes the specified listener from all event queues
	 *
	 * @param o the listener to remove.
	 *
	 * @return true if one of the list contained the specified element.
	 */
	public boolean removeAllListeners(Object o) {
		boolean result = false;

		if(o instanceof GameDataListener) {
			if(removeGameDataListener((GameDataListener) o)) {
				if(log.isDebugEnabled()) {
					log.debug("EventDispatcher.removeAllListeners: stale GameDataListener entry for " +
							  o.getClass());
				}

				result = true;
			}
		}

		if(o instanceof TempUnitListener) {
			if(removeTempUnitListener((TempUnitListener) o)) {
				if(log.isDebugEnabled()) {
					log.debug("EventDispatcher.removeAllListeners: stale TempUnitListener entry for " +
							  o.getClass());
				}

				result = true;
			}
		}

		if(o instanceof UnitOrdersListener) {
			if(removeUnitOrdersListener((UnitOrdersListener) o)) {
				if(log.isDebugEnabled()) {
					log.debug("EventDispatcher.removeAllListeners: stale UnitOrdersListener entry for " +
							  o.getClass());
				}

				result = true;
			}
		}

		if(o instanceof SelectionListener) {
			if(removeSelectionListener((SelectionListener) o)) {
				if(log.isDebugEnabled()) {
					log.debug("EventDispatcher.removeAllListeners: stale SelectionListener entry for " +
							  o.getClass());
				}

				result = true;
			}
		}

		if(o instanceof OrderConfirmListener) {
			if(removeOrderConfirmListener((OrderConfirmListener) o)) {
				if(log.isDebugEnabled()) {
					log.debug("EventDispatcher.removeAllListeners: stale OrderConfirmListener entry for " +
							  o.getClass());
				}

				result = true;
			}
		}

		return result;
	}

	/**
	 * Adds a listener for order confirm events.
	 *
	 * @param l the listener to add.
	 *
	 * @see OrderConfirmEvent
	 */
	public void addOrderConfirmListener(OrderConfirmListener l) {
		orderConfirmListeners.add(l);
	}

	/**
	 * Adds the given order-confirm listener to the front of all registered
	 * listeners. Warning: The order will change if another listener is added
	 * with priority.
	 *
	 * @param l TODO: DOCUMENT ME!
	 */
	public void addPriorityOrderConfirmListener(OrderConfirmListener l) {
		orderConfirmListeners.add(0, l);
	}

	/**
	 * Removes the specified listener for order confirm events.
	 *
	 * @param l the listener to remove.
	 *
	 * @return true if this list contained the specified element.
	 *
	 * @see OrderConfirmEvent
	 */
	public boolean removeOrderConfirmListener(OrderConfirmListener l) {
		return orderConfirmListeners.remove(l);
	}

	/**
	 * Forwards an event to all registered listeners for this event type.
	 * 
	 * <p>
	 * If synchronous is false, the forwarding is done asynchronously in a
	 * separate dispatcher thread. If the fire method is called before the
	 * dispatcher thread has finished the previous request, it is stopped and
	 * starts forwarding the new event.
	 * </p>
	 *
	 * @param e TODO: DOCUMENT ME!
	 * @param synchronous TODO: DOCUMENT ME!
	 */
	public void fire(EventObject e, boolean synchronous) {
		if(synchronous) {
			new Notifier(e).run();
		} else {
			queue.push(e);
		}
	}

	/**
	 * Asynchronously forwards an event to all registered listeners for this
	 * event type.
	 *
	 * @param e TODO: DOCUMENT ME!
	 */
	public void fire(EventObject e) {
		fire(e, false);
	}

	/**
	 * Returns the number of events that were passed to this dispatcher for
	 * forwarding.
	 *
	 * @return TODO: DOCUMENT ME!
	 */
	public int getEventsFired() {
		return eventsFired;
	}

	/**
	 * Returns the number of events that were actually forwarded to event
	 * listeners.
	 *
	 * @return TODO: DOCUMENT ME!
	 */
	public int getEventsDispatched() {
		return eventsDispatched;
	}

	private class EManager implements Runnable {
		/**
		 * TODO: DOCUMENT ME!
		 */
		public void run() {
			while(true) {
				try {
					EventObject o = queue.waitFor();

					long	    start = 0;

					int		    prio = getPriority(o);

					eventsFired++;

					// if some notifier has already been started and it should stop we have to wait for it to die
					// of course the waiting time should be bounded

					/* Note: I think it's better to use a priority system. Some events should not be
					 *       interrupted like GameDataEvents since data integrity would be lost.
					 *          Andreas
					 */
					if(notifierIsAlive) {
						if(prio < lastPriority) { // interrupt the old notifier
							stopNotification = true;
							start			 = System.currentTimeMillis();

							while(stopNotification &&
									  ((System.currentTimeMillis() - start) < 2000)) {
								Thread.yield();
							}
						} else { // wait for the notifier

							do {
								Thread.yield();
							} while(notifierIsAlive);
						}
					}

					stopNotification = false;

					lastPriority = prio;
					SwingUtilities.invokeLater(new Notifier(o));
				} catch(InterruptedException ie) {
				}
			}
		}
	}

	private class EQueue {
		private List    objects = CollectionFactory.createLinkedList();
		private boolean block = false;

		/**
		 * TODO: DOCUMENT ME!
		 *
		 * @return TODO: DOCUMENT ME!
		 */
		public synchronized EventObject poll() {
			if(block) {
				try {
					this.wait();
				} catch(InterruptedException ie) {
				}
			}

			return (EventObject) objects.remove(0);
		}

		/**
		 * TODO: DOCUMENT ME!
		 *
		 * @return TODO: DOCUMENT ME!
		 *
		 * @throws InterruptedException TODO: DOCUMENT ME!
		 */
		public synchronized EventObject waitFor() throws InterruptedException {
			if(block) {
				this.wait();
			}

			if(objects.size() == 0) {
				this.wait();
			}

			return poll();
		}

		/**
		 * TODO: DOCUMENT ME!
		 *
		 * @param o TODO: DOCUMENT ME!
		 */
		public synchronized void push(EventObject o) {
			int index = 0;

			if(objects.size() > 0) {
				int prioNew = getPriority(o);
				block = true;

				while(index < objects.size()) {
					EventObject obj     = (EventObject) objects.get(index);
					int		    prioOld = getPriority(obj);

					if(prioOld > prioNew) {
						do {
							objects.remove(index);
						} while(index < objects.size());
					} else {
						index++;
					}
				}

				block = false;
			}

			objects.add(index, o);
			this.notifyAll();
		}
	}

	protected int getPriority(EventObject e) {
		int prio = -1;

		if(e instanceof GameDataEvent) {
			prio = PRIORITIES[GAMEDATA_INDEX];
		} else if(e instanceof SelectionEvent) {
			prio = PRIORITIES[SELECTION_INDEX];
		} else if(e instanceof UnitOrdersEvent) {
			prio = PRIORITIES[UNITORDERS_INDEX];
		} else if(e instanceof TempUnitEvent) {
			prio = PRIORITIES[TEMPUNIT_INDEX];
		} else if(e instanceof OrderConfirmEvent) {
			prio = PRIORITIES[ORDERCONFIRM_INDEX];
		}

		return prio;
	}

	private class Notifier implements Runnable {
		private EventObject event = null;

		/**
		 * Creates a new Notifier object.
		 *
		 * @param e TODO: DOCUMENT ME!
		 */
		public Notifier(EventObject e) {
			this.event = e;
		}

		/**
		 * TODO: DOCUMENT ME!
		 */
		public void run() {
			notifierIsAlive = true;

			// the for loops are duplicated for each event type to
			// avoid a lot of expensive class casts and instanceof
			// operations
			if(event instanceof SelectionEvent) {
				SelectionEvent e = (SelectionEvent) event;

				for(Iterator iter = selectionListeners.iterator();
						iter.hasNext() &&
						!EventDispatcher.this.stopNotification;) {
					eventsDispatched++;
					((SelectionListener) iter.next()).selectionChanged(e);

					if(EventDispatcher.this.stopNotification) {
						EventDispatcher.this.stopNotification = false;
					}
				}
			} else if(event instanceof OrderConfirmEvent) {
				OrderConfirmEvent e = (OrderConfirmEvent) event;

				for(Iterator iter = orderConfirmListeners.iterator();
						iter.hasNext() &&
						!EventDispatcher.this.stopNotification;) {
					eventsDispatched++;
					((OrderConfirmListener) iter.next()).orderConfirmationChanged(e);

					if(EventDispatcher.this.stopNotification) {
						EventDispatcher.this.stopNotification = false;
					}
				}
			} else if(event instanceof UnitOrdersEvent) {
				UnitOrdersEvent e = (UnitOrdersEvent) event;

				for(Iterator iter = unitOrdersListeners.iterator();
						iter.hasNext() &&
						!EventDispatcher.this.stopNotification;) {
					eventsDispatched++;
					((UnitOrdersListener) iter.next()).unitOrdersChanged(e);

					if(EventDispatcher.this.stopNotification) {
						EventDispatcher.this.stopNotification = false;
					}
				}
			} else if(event instanceof TempUnitEvent) {
				TempUnitEvent e = (TempUnitEvent) event;

				for(Iterator iter = tempUnitListeners.iterator();
						iter.hasNext() &&
						!EventDispatcher.this.stopNotification;) {
					TempUnitListener l = (TempUnitListener) iter.next();
					eventsDispatched++;

					if(e.getType() == TempUnitEvent.CREATED) {
						l.tempUnitCreated(e);
					} else if(e.getType() == TempUnitEvent.DELETED) {
						l.tempUnitDeleted(e);
					}

					if(EventDispatcher.this.stopNotification) {
						EventDispatcher.this.stopNotification = false;
					}
				}
			} else if(event instanceof GameDataEvent) {
				GameDataEvent e = (GameDataEvent) event;

				for(Iterator iter = gameDataListeners.iterator();
						iter.hasNext() &&
						!EventDispatcher.this.stopNotification;) {
					eventsDispatched++;
					((GameDataListener) iter.next()).gameDataChanged(e);

					if(EventDispatcher.this.stopNotification) {
						EventDispatcher.this.stopNotification = false;
					}
				}
			}

			// 2002.03.04 pavkovic: get rid of evil Event, seems that Notifier will not
			// be removed correctly
			event		    = null;
			notifierIsAlive = false;
			lastPriority    = Integer.MAX_VALUE;
		}
	}
}
