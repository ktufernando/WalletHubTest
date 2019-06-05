package com.ef.model;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by fer on 04/06/19.
 */
@Slf4j
@Getter
@ToString
public class ProcessRequest {

    private Timestamp startDate;
    private DurationTypes durationType;
    private Integer threshold;
    private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");

    public ProcessRequest(String startDate, String duration, String threshold) {
        this.durationType = DurationTypes.valueOf(duration.toUpperCase());
        if (Strings.isNullOrEmpty(threshold)) {
            this.threshold = this.durationType.getDefaultThreshold();
        } else {
            this.threshold = Integer.parseInt(threshold);
        }
        try {
            this.startDate = new Timestamp(FORMAT.parse(startDate).getTime());
        } catch (ParseException e) {
            log.error("Date format exception.", e);
            e.printStackTrace();
        }
    }

    public Timestamp getEndDate() {
        return durationType.getEndDate(startDate);
    }


}
