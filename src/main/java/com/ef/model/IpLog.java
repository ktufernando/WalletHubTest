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
@ToString
public class IpLog {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(length = 15, nullable = false)
    private String ip;
    @Column(nullable = false)
    private Integer requests;
    @Basic
    @Column(nullable = false)
    private Timestamp start;
    @Basic
    @Column(nullable = false)
    private Timestamp finish;
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private DurationTypes duration;
    @Column(nullable = false)
    private Integer threshold;
    private String comment;
}
