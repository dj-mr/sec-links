/**
 * Domain class for SEC Data.
 */
package com.probemore.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SECData {

    /**
     * Organization Name as registered with SEC.
     */
    private String      organizationName;

    /**
     * Name of document filed. Example: 10-Q, 10-K.
     */
    private String      formName;

    /**
     * CIK is unique identifier assigned to a company
     * filing financial data with SEC.
     */
    private String      cik;

    /**
     * Date form was prepared/filed with SEC.
     */
    private LocalDate   filingDate;

    /**
     * EDGAR URL needed to get form that was filed.
     */
    private String      secUrl;

}
