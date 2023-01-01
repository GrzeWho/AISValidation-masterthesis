package com.aisvalidator.base.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.EnumSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@Document("externalverificationresult")
public class ExternalVerificationResult {
    @Id
    private long mmsi;
    private boolean failure;
    private String failureString;
    private EnumSet<ExternalVerificationFailReason> failReasons;
    private Integer imo;
    private String callSign;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;
}