package com.phishing.app.model.lookups;
import static javax.persistence.GenerationType.IDENTITY;

import com.phishing.app.model.common.AbstractLookup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_config")
public class AppConfig{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "value", length = 500)
    private String value;
}
