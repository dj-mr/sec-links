/**
 * Domain class for SEC Data.
 */
package com.probemore.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDate;

@Data
//@Entity
//@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class SECData {

    /**
     * Organization Name as registered with SEC.
     */
    private final String      organizationName;

    /**
     * Name of document filed. Example: 10-Q, 10-K.
     */
    private final String      formName;

    /**
     * CIK is unique identifier assigned to a company
     * filing financial data with SEC.
     */
    private final String      cik;

    /**
     * Date form was prepared/filed with SEC.
     */
    private final LocalDate   filingDate;

    /**
     * EDGAR URL needed to get form that was filed.
     */
    private final String      secUrl;

}
