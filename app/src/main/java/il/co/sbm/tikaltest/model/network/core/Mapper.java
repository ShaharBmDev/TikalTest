package il.co.sbm.tikaltest.model.network.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import il.co.sbm.tikaltest.utils.DateTimeUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public final class Mapper {
    private static ObjectMapper MAPPER;

    public static ObjectMapper get() {
        if (MAPPER == null) {
            MAPPER = new ObjectMapper();

            // This is useful for me in case I add new object properties on the server side which are not yet available on the client.
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            DateFormat defaultDateFormat = new SimpleDateFormat(DateTimeUtils.DATE_FORMAT_dd_MM_yyyy_T_HH_mm_ss, Locale.getDefault());
            MAPPER.setDateFormat(defaultDateFormat);
        }

        return MAPPER;
    }

    public static String string(Object data) {
        try {
            return get().writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectOrThrow(String data,
                                      Class<T> type) throws IOException {
        return get().readValue(data, type);
    }

    public static <T> T object(String data, Class<T> type) {
        try {
            return objectOrThrow(data, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectOrThrow(String data,
                                      TypeReference<T> type) throws IOException {
        return get().readValue(data, type);
    }

    public static <T> T object(String data, TypeReference<T> type) {
        try {
            return objectOrThrow(data, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
