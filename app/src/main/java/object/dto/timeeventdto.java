package dao1;

import java.text.SimpleDateFormat;

/**
 * Created by BenX on 18/03/2017.
 */
public class timeeventdto {
    public timeeventdto()
    {
        this.id =0;
        this.userid = 0;
        this.timestart = new SimpleDateFormat();
        this.timeend = new SimpleDateFormat();
        this.note = "";
        this.pe_id = 0;
    }

    public int id;
    public int userid;
    public SimpleDateFormat timestart;
    public SimpleDateFormat timeend;
    public SimpleDateFormat dayselect;
    public String note;
    public int pe_id;
}
