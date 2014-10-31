package ing.unipi.it.activitymonitoringtools;

import android.hardware.SensorEvent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @brief Class that represents the buffer in which a sensor event listener saves the events he receives from the sensors
 * It allows a producer (the sensor event listener) and multiple consumers.
 */
public class SensorEventBuffer {

    private SensorEvent[] circularBuffer;
    private int dimBuffer;
    private int head;
    private int tail;
    private int counter = 0;

    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();


    public SensorEventBuffer(int numEvents) {
        this.dimBuffer = numEvents;
        this.circularBuffer = new SensorEvent[numEvents];
        this.head = 0;
        this.tail = 0;
    }

    public void putEvent(SensorEvent event) throws InterruptedException {
        lock.lock();
        try {
            while (counter == dimBuffer)
                notFull.await();//il produttore aspetta ci sia almeno un posto in cui inserire
            circularBuffer[tail] = event;
            counter++;
            tail = (tail + 1) % dimBuffer;
            notEmpty.signal();

        } finally {
            lock.unlock();
        }

    }


    public SensorEvent getEvent() throws InterruptedException {

        SensorEvent event;

        lock.lock();
        boolean removeElement = true;
        try {
            while (counter == 0)
                notEmpty.await();
            event = circularBuffer[head];
            head = (head + 1) % dimBuffer;
            counter--;
            notFull.signal();

        } finally {
            lock.unlock();
        }

        return event;
    }




}




