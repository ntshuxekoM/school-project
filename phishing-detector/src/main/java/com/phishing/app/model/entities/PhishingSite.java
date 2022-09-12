package com.phishing.app.model.entities;

import com.phishing.app.model.common.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "phishing_site", uniqueConstraints = {@UniqueConstraint(columnNames = "url")})
public class PhishingSite extends AbstractEntity {

    @NotBlank
    @Size(max = 250)
    private String url;

}