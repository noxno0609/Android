package object;

import object.dto.periodeventdto;
import object.dto.timeeventdto;

/**
 * Created by BenX on 30/03/2017.
 */
public class util {
    public static void deleteDto(periodeventdto dto)
    {
        dto = new periodeventdto();
    }
    public static void deleteDto(timeeventdto dto)
    {
        dto = new timeeventdto();
    }
}
