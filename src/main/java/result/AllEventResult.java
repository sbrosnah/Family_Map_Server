package result;

import model.Event;
import java.util.ArrayList;

public class AllEventResult extends Result{
    ArrayList<Event> data;

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }
}
