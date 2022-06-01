package com.hunglp.iambackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tenant extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenantId;

    private String name;

    private boolean isDeleted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<Users> users = new ArrayList<>();


}
