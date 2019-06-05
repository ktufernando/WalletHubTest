package com.ef.model;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;

/**
 * Created by fer on 04/06/19.
 */
public enum DurationTypes {

    HOURLY {
        @Override
        public Integer getDefaultThreshold() {
            return 200;
        }

        @Override
        public Timestamp getEndDate(Timestamp startDate) {
            return Timestamp.from(startDate.toInstant().plus(1, ChronoUnit.HOURS));
        }
    },
    DAILY {
        @Override
        public Integer getDefaultThreshold() {
            return 500;
        }

        @Override
        public Timestamp getEndDate(Timestamp startDate) {
            return Timestamp.from(startDate.toInstant().plus(1, ChronoUnit.DAYS));
        }
    };

    public abstract Integer getDefaultThreshold();
    public abstract Timestamp getEndDate(Timestamp startDate);



}
