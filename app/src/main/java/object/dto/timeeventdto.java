package object.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BenX on 18/03/2017.
 */
public class timeeventdto implements Serializable {

    public int id;
    public int userid;
    public Date timestart;
    public Date timeend;
    public Date dayselect;
    public String note;
    public int pe_id;
}