package com.hunglp.iambackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Role extends  DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;


    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Users user;


}
