/**
 * Constants used in this service.
 */
package com.probemore.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Constants {

    /**
     * Position in SEC company.idx where Form Name starts.
     */
    public static final int INDEX_OF_FORM_NAME = 62;
    /**
     * Position in SEC company.idx where CIK starts.
     */
    public static final int INDEX_OF_CIK = 74;
    /**
     * Position in SEC company.idx where Filing Date starts.
     */
    public static final int INDEX_OF_FILING_DATE = 86;
    /**
     * Position in SEC company.idx where Form URL starts.
     */
    public static final int INDEX_OF_EDGAR_URL = 96;

    /**
     * Batch size used for insert and update operations against DB.
     */
    public static final int BATCH_SIZE = 500;

}
