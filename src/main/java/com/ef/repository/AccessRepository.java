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

    @Query(value = "SELECT ip AS ip, COUNT(ip) AS count " +
            "              FROM access " +
            "              WHERE date BETWEEN ?1 AND ?2 " +
            "              GROUP BY ip " +
            "              HAVING count >= ?3",
            nativeQuery = true)
    Stream<MatchedIp> findIpsFor(Timestamp start, Timestamp end, Integer threshold);

    interface MatchedIp {

        String getIp();

        Integer getCount();
    }

}
