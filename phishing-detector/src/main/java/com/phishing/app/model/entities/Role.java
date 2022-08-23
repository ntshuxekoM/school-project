package com.phishing.app.model.entities;


import com.phishing.app.model.common.AbstractEntity;
import com.phishing.app.model.enums.ERole;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {

    }

    public Role(ERole name) {
        this.name = name;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}