/**
 * Interface that defines contract for SECLinksProcessor methods.
 */
package com.probemore.controller.processor;

import java.io.IOException;

public interface SECLinksProcessor {

    /**
     * EDGAR data is organized by year. Under year, data is
     * organized by quarters as shown below for
     * https://www.sec.gov/Archives/edgar/full-index/2019/
     *
     * ========================================
     * Name      Size      Last Modified
     * ========================================
     * QTR1                09/28/2019 03:15:44 AM
     * QTR2                09/28/2019 03:17:45 AM
     * QTR3                09/30/2019 10:26:12 PM
     * QTR4                10/01/2019 12:20:13 AM
     * ========================================
     *
     * @param year - The "Year" folder that must be navigated. 2019 in above URL
     * @return Returns count of lines with occurence of string "QTR".
     *         Each occurrence represents data available for a quarter
     *         starting with QTR1
     */
    long getDirectoryCountInURI(String year) throws IOException;

    /**
     * Download company.zip from each of the folders inside EDGAR "year" folder.
     * Sample structure for each QTR with relevant files is shown below using
     * https://www.sec.gov/Archives/edgar/full-index/2019/QTR1/
     *
     * =================================================
     * Name           Size       Last Modified
     * =================================================
     * company.gz     4136 KB    03/29/2019 10:10:01 PM
     * company.idx    43113 KB   03/29/2019 10:08:34 PM
     * company.Z      7464 KB    03/29/2019 10:09:53 PM
     * company.zip    3767 KB    03/29/2019 10:10:39 PM
     * crawler.idx    52821 KB   03/29/2019 10:08:31 PM
     * form.gz        4633 KB    03/29/2019 10:10:57 PM
     * form.idx       43113 KB   03/29/2019 10:08:37 PM
     * form.Z         7441 KB    03/29/2019 10:10:48 PM
     * form.zip       4291 KB    03/29/2019 10:11:26 PM
     * master.gz      3636 KB    03/29/2019 10:11:35 PM
     * master.idx     25506 KB   03/29/2019 10:08:27 PM
     * master.Z       6582 KB    03/29/2019 10:11:31 PM
     * master.zip     3473 KB    03/29/2019 10:11:52 PM
     * =================================================
     *
     *
     * @param quarters
     * @throws IOException
     */
    void downloadEdgarUrls(String year, long quarters) throws IOException;

}
