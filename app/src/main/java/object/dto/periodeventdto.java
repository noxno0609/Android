package object.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BenX on 27/03/2017.
 */
public class periodeventdto implements Serializable {

    public int id;
    public int userid;
    public Date timestart;
    public Date timeend;
    public Date datestart;
    public Date dateend;
    public String dayselect;
    public String note;
    public String textcolor;
    public String bgcolor;
}
