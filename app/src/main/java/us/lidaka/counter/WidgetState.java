package us.lidaka.counter;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class WidgetState implements Serializable {
    private static final long serialVersionUID = 1;

    public int id;
    public String label;
    public int step;
    public int count;

    public WidgetState(int id, String label, int step, int count) {
        this.id = id;
        this.label = label;
        this.step = step;
        this.count = count;
    }

    public static WidgetState load(Context context, int id) {
        try {
            FileInputStream fis = context.openFileInput(filenameForId(id));
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (WidgetState)(ois.readObject());
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public void persist(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(filenameForId(id), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
        } catch (IOException e) {
            return;
        }
    }

    private static String filenameForId(int id) {
        return "widget." + id;
    }
}
