package com.ef.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by fer on 04/06/19.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Access {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Basic
    @Column(nullable = false)
    private Timestamp date;
    @Column(nullable = false)
    private String ip;
    private String request;
    private String status;
    private String agent;

}
