package com.ef.service;

import com.ef.model.Access;
import com.ef.model.ProcessRequest;
import com.ef.model.IpLog;
import com.ef.repository.AccessRepository;
import com.ef.repository.ResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by fer on 04/06/19.
 */
@Slf4j
@Service
@Transactional
public class ParserService {

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Value("${timestamp.format}")
    private String format;


    /**
     * Test logic
     * @param accessLog
     * @param startDate
     * @param duration
     * @param threshold
     */
    public void accessProcess(String accessLog, String startDate, String duration, String threshold) {
        try {
            persisAccessData(accessLog);
            determineAttack(new ProcessRequest(startDate, duration, threshold));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 1- Query for find ips with startDat, endDate and threshold
     * 2- Load them to MySQL table with comments
     * 2- Print them to console
     * @param request
     */
    private void determineAttack(ProcessRequest request) {
        log.info("Matching IPs with input parameters: {}", request);
        Stream<AccessRepository.MatchedIp> ips = accessRepository.findIpsFor(request.getStartDate(), request.getEndDate(), request.getThreshold());
        ips.forEach((i) -> {
            log.info("Loggind IP: {} with {} request", i.getIp(), i.getCount());
            IpLog ipLog = IpLog.builder()
                    .duration(request.getDurationType())
                    .finish(request.getEndDate())
                    .ip(i.getIp())
                    .requests(i.getCount())
                    .start(request.getStartDate())
                    .threshold(request.getThreshold())
                    .comment(String.format("The IP %s has %s request from %s to %s. Issue saved.", i.getIp(), i.getCount(), request.getStartDate(), request.getEndDate()))
                    .build();
            resultRepository.save(ipLog);
            log.info("Saved: {}", ipLog);
        });

    }

    /**
     * 1- Delete all data
     * 2- Load file log
     * 3- Parse lines
     * 4- Persist log
     * @param accessLog
     * @throws Exception
     */
    private void persisAccessData(String accessLog) throws Exception {
        log.info("Deleting old data...");
        accessRepository.deleteAll();
        Stream<String> lines = getStreamFile(accessLog);
        log.info("Saving request access log...");
        lines.forEach((l) -> {
            try {
                List<String> e = Arrays.asList(l.split("\\|"));
                Access access = Access.builder().date(getTimeStamp(e.get(0)))
                        .ip(e.get(1)).request(e.get(2)).status(e.get(3)).agent(e.get(4))
                        .build();
                accessRepository.save(access);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    /**
     * Get file from path
     * @param accessLog
     * @return
     * @throws IOException
     */
    private Stream<String> getStreamFile(String accessLog) throws IOException {
        log.info("Getting the stream from the file...");
        Path path = Paths.get(accessLog);
        return Files.lines(path);
    }

    /**
     * Get Timestamp
     * @param date
     * @return
     * @throws ParseException
     */
    private Timestamp getTimeStamp(String date) throws ParseException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date parsedDate = dateFormat.parse(date);
            return new java.sql.Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            log.error(String.format("Error parsing file: invalid format. Value: %s - Format: %s", date, format));
            throw e;
        }
    }

}
