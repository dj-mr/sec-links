/**
 * Domain class for SEC Data.
 */
package com.probemore.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@IdClass(SECLinksCompositeKey.class)
public class SECLinks implements Serializable {

    /**
     * Organization Name as registered with SEC.
     */
    private final String      organizationName;

    /**
     * Name of document filed. Example: 10-Q, 10-K.
     */
    @Id
    @NotNull
    private final String      formName;

    /**
     * CIK is unique identifier assigned to a company
     * filing financial data with SEC.
     */
    @Id
    @NotNull
    private final String      cik;

    /**
     * Date form was prepared/filed with SEC.
     */
    @Id
    @NotNull
    private final LocalDate   filingDate;

    /**
     * EDGAR URL needed to get form that was filed.
     */
    private final String      secUrl;

}
