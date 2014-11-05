package ing.unipi.it.activitymonitoringtools;

import android.hardware.SensorEvent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @brief Class that represents the buffer in which a sensor event listener saves the events he receives from the sensors
 * It allows a producer (the sensor event listener) and multiple consumers.
 */
public class SampleDataBuffer {

    private SampleData[] circularBuffer;
    private int head = 0;
    private int tail = 0;
    private int counter = 0;

    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();


    public SampleDataBuffer(int numSamples) {
        this.circularBuffer = new SampleData[numSamples];
    }

    public void putSample(SampleData sample)  {
        lock.lock();
        try {
            while (counter == circularBuffer.length)
                notFull.await();//il produttore aspetta ci sia almeno un posto in cui inserire
            circularBuffer[tail] = sample;
            counter++;
            tail = (tail + 1) % circularBuffer.length;
            notEmpty.signal();

        } catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }


    public SampleData getSample()  {

        SampleData sample = null;

        lock.lock();

        try {
            while (counter == 0)
                notEmpty.await();
            sample = circularBuffer[head];
            head = (head + 1) % circularBuffer.length;
            counter--;
            notFull.signal();

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

        return sample;
    }



    public SampleData[] getNSamples(int num) {
        SampleData[] samples = new SampleData[num];
        lock.lock();
        try {
            while (counter < num) notEmpty.await();
            System.arraycopy(circularBuffer, head, samples, 0, num);
            counter -=num;
            head = (head+num)%circularBuffer.length;
            for(int i = 0; i < num; i ++) {
                notFull.signal();
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }

        return samples;
    }

    public int getDimBuffer() {
        return circularBuffer.length;
    }



}




