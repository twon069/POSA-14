package edu.vuum.mocca;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore implementation using
 *        Java a ReentrantLock and a ConditionObject (which is accessed via a
 *        Condition). It must implement both "Fair" and "NonFair" semaphore
 *        semantics, just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
	private final ReentrantLock mRLock;

    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
	private final Condition mEmptyPermit;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here. Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
	private volatile int mPermitCount;

    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
    	mRLock = new ReentrantLock(fair);
    	mEmptyPermit = mRLock.newCondition();
    	mPermitCount = permits;
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
    	mRLock.lockInterruptibly();
    	try
    	{
    		while (mPermitCount == 0)
    			mEmptyPermit.await();
    		--mPermitCount;
    	}
    	finally
    	{
    		mRLock.unlock();
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.
    	mRLock.lock();
    	while (mPermitCount == 0)
    		mEmptyPermit.awaitUninterruptibly(); // No try{} needed, because nothing thrown.
    	--mPermitCount;
    	mRLock.unlock();
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here.
    	mRLock.lock();
    	++mPermitCount;
    	mEmptyPermit.signal();
    	mRLock.unlock();
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here to return the correct result
    	mRLock.lock();
    	int curPermitCount = mPermitCount;
    	mRLock.unlock();
        return curPermitCount;
    }
}
