package cz.cvut.fel.pjv.schoolis;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class responsible for saving generated login in new thread.
 * 
 * @author Vojcek
 */
public class SaveThread implements Runnable {
    private final String filename;
    private final String dataToSave;

    /**
     * SaveThread constructor.
     * 
     * @param filename      filename for saved login 
     * @param dataToSave    data to be saved
     */
    public SaveThread(String filename, String dataToSave) {
        this.filename = filename;
        this.dataToSave = dataToSave;
    }
    
    /**
     * Thread entry point.
     * 
     * The thread saves the given data and end itself.
     */
    @Override
    public void run() {
        BufferedWriter writer = null;
        try {
            // Get current timestamp for using in filename
            String timeLog = new SimpleDateFormat("_dd_mm_yyyy").format(Calendar.getInstance().getTime());
            
            writer = new BufferedWriter(new OutputStreamWriter(
                 new FileOutputStream(Config.LOGINFOLDER + filename + timeLog + ".txt"), "UTF-8"));
            writer.write(dataToSave);
            
            try {
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }   
}
