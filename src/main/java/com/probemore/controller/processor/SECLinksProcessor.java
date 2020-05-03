/**
 * Interface that defines contract for SECLinksProcessor methods.
 */
package com.probemore.controller.processor;

import com.probemore.model.SECLinks;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SECLinksProcessor {

    /**
     * Fetches URL content given a CIK.
     * @param cik that needs to be looked up
     * @param formname that is used to filter data
     * @param filingdate that is used to filter data
     * @param offset that defines number of records to skip
     * @param length that defines number of records to fetch
     * @return Lis of URLs
     */
    List<SECLinks> getFilteredUrls(
            Optional<String>     cik,
            Optional<String>     formname,
            Optional<LocalDate>  filingdate,
            Optional<Integer>    offset,
            Optional<Integer>    length
    );

    /**
     * Download company.idx from each of the folders inside EDGAR "year" folder.
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
     * @param year Year for which data is downloaded
     * @param quarter Financial quarter for which data must be extracted
     * @throws IOException
     */
    void downloadEdgarUrls(Optional<String> year, Optional<Long> quarter)
            throws IOException;

}
