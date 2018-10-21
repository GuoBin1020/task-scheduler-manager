package com.example.tsm.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sender")
@Data
public class SenderEntity {

    @Id
    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "host")
    private String host;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "auth")
    private boolean auth;

}
