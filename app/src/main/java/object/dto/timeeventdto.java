package object.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BenX on 18/03/2017.
 */
public class timeeventdto implements Serializable {

    public timeeventdto()
    {
    }

    public timeeventdto(timeeventdto dto)
    {
        this.userid = dto.userid;
        this.timestart = dto.timestart;
        this.timeend = dto.timeend;
        this.dayselect = dto.dayselect;
        this.note = dto.note;
        this.pe_id = dto.pe_id;
        this.bgcolor = dto.bgcolor;
        this.textcolor = dto.textcolor;
    }
    public int id;
    public int userid;
    public Date timestart;
    public Date timeend;
    public Date dayselect;
    public String note;
    public int pe_id;
    public String textcolor;
    public String bgcolor;
}