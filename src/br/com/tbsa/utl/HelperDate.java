package br.com.tbsa.utl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javafx.scene.control.DatePicker;

public class HelperDate {

    private static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public static Date toDate(DatePicker datePicker) {
        if (datePicker.getValue() == null) {
            return null;
        }
        LocalDate ld = datePicker.getValue();
        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return date;
    }

    /**
     * Converte Date para LocalDate
     *
     * @param d
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date d) {
        Instant instant = Instant.ofEpochMilli(d.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    public static Date converterStringData(String srtData) throws ParseException {
        Date data = null;
        data = formato.parse(srtData);
        return data;
    }

    /**
     *
     * @param data
     * @return String formato 'BR'
     */
    public static String retornarDataString(Date data) {
        return formato.format(data);
    }
}
