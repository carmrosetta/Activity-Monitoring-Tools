package ing.unipi.it.activitymonitoringtools;

import java.io.Serializable;

/**
 * Created by carmen on 03/11/14.
 */
public class SampleData implements Serializable {
    String timestamp;
    float[] values;

    public SampleData (String timestamp, float[] values) {
        this.timestamp = timestamp;
        this.values = values;
    }

    public String toString() {
        String sensedValues = "";

        for(int j = 0; j < values.length; j++) {
            sensedValues += ", "+values[j];
        }

        return timestamp+sensedValues+"\n";
    }


}
