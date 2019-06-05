package com.ef.repository;

import com.ef.model.Access;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.stream.Stream;

/**
 * Created by fer on 04/06/19.
 */
@Repository
public interface AccessRepository extends CrudRepository<Access, Long> {

    @Query(value = "SELECT c.ip AS ip, c.count AS count FROM ( " +
            "                SELECT " +
            "                  a.ip AS ip, " +
            "                  COUNT(a.ip) AS count " +
            "                FROM access AS a " +
            "                WHERE (a.date >= ?1 AND a.date <= ?2) " +
            "                GROUP BY a.ip " +
            "              ) AS c " +
            "WHERE c.count >= ?3",
            nativeQuery = true)
    Stream<MatchedIp> findIpsFor(Timestamp start, Timestamp end, Integer threshold);

    interface MatchedIp {

        String getIp();

        Integer getCount();
    }

}
