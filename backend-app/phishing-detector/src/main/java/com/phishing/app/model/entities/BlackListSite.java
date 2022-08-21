package com.phishing.app.model.entities;

import com.phishing.app.model.common.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "black_list_site", uniqueConstraints = {@UniqueConstraint(columnNames = "url")})
public class BlackListSite extends AbstractEntity {

    @NotBlank
    @Size(max = 250)
    private String url;

    @NotBlank
    @Size(max = 120)
    @Column(name = "site_name", length = 250)
    private String siteName;

}