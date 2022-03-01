package result;

import java.util.ArrayList;
import model.Person;

public class AllPersonResult extends Result{
    ArrayList<Person> data;

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }
}
