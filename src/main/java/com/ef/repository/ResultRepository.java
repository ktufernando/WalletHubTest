package com.ef.repository;

import com.ef.model.IpLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by fer on 04/06/19.
 */
@Repository
public interface ResultRepository extends CrudRepository<IpLog, Long> {
}
