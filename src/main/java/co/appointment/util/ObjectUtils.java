package co.appointment.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjectUtils {
    public static Pageable getPageable(final int pageNumber, final int pageSize) {
        return PageRequest.of(pageNumber, pageSize);
    }
    public static Pageable getPageable(final int pageNumber, final int pageSize, Sort sort) {
        return PageRequest.of(pageNumber, pageSize, sort);
    }
    public static String dateToString(final Date date, final String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        return formatter.format(date);
    }
}
