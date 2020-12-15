package dj.mr.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants used in the application.
 */
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

    /**
     * Default number of rows to fetch for GET operation on SEC links.
     */
    public static final int SEC_LINKS_DEFAULT_GET_SIZE = 500;

    /**
     * Default number of rows to skip for GET operation on SEC links.
     */
    public static final int SEC_LINKS_DEFAULT_OFFSET = 0;

    /**
     * Max Value of String for comparision.
     */
    public static final String SEC_LINKS_COMPARISION_STRING_MAX_VALUE = "zzzzzzzzzzzzzz";

    /**
     * Min Value of Filing Date for comparison.
     */
    public static final int SEC_LINKS_MINIMUM_FILING_YEAR = 20;

    /**
     * First Quarter.
     */
    public static final int SEC_LINKS_FIRST_QUARTER = 1;

    /**
     * Second Quarter.
     */
    public static final int SEC_LINKS_SECOND_QUARTER = 2;

    /**
     * Third Quarter.
     */
    public static final int SEC_LINKS_THIRD_QUARTER = 3;

    /**
     * Fourth Quarter.
     */
    public static final int SEC_LINKS_FOURTH_QUARTER = 4;

    /**
     * Length of CIK that is stored (if needed by prefixing) in database.
     */
    public static final int LENGTH_OF_CIK = 10;

    /**
     * Prefix used for CIK if its length is less than expected value.
     */
    public static final String PREFIX_FOR_CIK = "0";
}
