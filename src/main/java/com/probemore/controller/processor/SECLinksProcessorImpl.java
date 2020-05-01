/**
 * Contains methods that aid REST operations in SECLinks.
 */
package com.probemore.controller.processor;

import com.probemore.data.SECLinksRepository;
import com.probemore.model.SECData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.probemore.config.Constants.BATCH_SIZE;
import static com.probemore.config.Constants.FIRST_QUARTER;
import static com.probemore.config.Constants.FOURTH_QUARTER;
import static com.probemore.config.Constants.INDEX_OF_FILING_DATE;
import static com.probemore.config.Constants.INDEX_OF_EDGAR_URL;
import static com.probemore.config.Constants.INDEX_OF_CIK;
import static com.probemore.config.Constants.INDEX_OF_FORM_NAME;
import static com.probemore.config.Constants.SECOND_QUARTER;
import static com.probemore.config.Constants.THIRD_QUARTER;

@Slf4j
@Component
public class SECLinksProcessorImpl implements SECLinksProcessor {

    /**
     * EDGAR URL that is used to fetch data.
     */
    @Value("${edgar.url}")
    private String edgarUrl;

    /**
     * EDGAR URL domain for Archives.
     */
    @Value("${edgar.prefix}")
    private String edgarPrefix;

    /**
     * Default number of rows to fetch for GET operation on SEC links.
     */
    @Value("${seclinks.default.getSize}")
    private Integer defaultGetSize;

    /**
     * Default number of rows to skip for GET operation on SEC links.
     */
    @Value("${seclinks.default.getOffset}")
    private Integer defaultOffset;

    /**
     * Max Value of String for comparision.
     */
    @Value("${seclinks.comparision.stringMax}")
    private String stringMaxValue;

    /**
     * Min Value of Filing Date for comparision.
     */
    @Value("${seclinks.comparision.minFilingYear}")
    private Integer minFilingYear;

    /**
     * Interface that defines DB operations.
     */
    @Autowired
    private SECLinksRepository secLinksRepo;

    /**
     * Fetch all EDGAR URLs from database.
     * @param cik that needs to be looked up
     * @param formname that is used to filter data
     * @param filingdate that is used to filter data
     * @param offset that defines number of records to skip
     * @param length that defines number of records to fetch
     * @return Returns list of all SEC URLs
     */
    public List<SECData> getFilteredUrls(
            final Optional<String>     cik,
            final Optional<String>     formname,
            final Optional<LocalDate>  filingdate,
            final Optional<Integer>    offset,
            final Optional<Integer>    length
    ) {
        Pageable pageable = PageRequest.of(
                offset.orElse(Integer.valueOf(defaultOffset)),
                length.orElse(Integer.valueOf(defaultGetSize)));
        return secLinksRepo
                .findAllByCikBetweenAndFormNameBetweenAndFilingDateBetweenOrderByFilingDateDesc(
                        cik.orElse(""),
                        cik.orElse(stringMaxValue),
                        formname.orElse(""),
                        formname.orElse(stringMaxValue),
                        filingdate.orElse(
                                LocalDate.now().minusYears(minFilingYear)
                        ),
                        filingdate.orElse(LocalDate.now()),
                        pageable
                );
    }

    /**
     * EDGAR data is organized by year. Under year, data is
     * organized by quarters
     *
     * @param year - The "Year" folder that must be navigated
     * @return Returns list of QTR-n directories present in given folder
     */
    public long getDirectoryCountInURI(final String year)
            throws IOException {
        log.debug("Fetching EDGAR data from {}{}/", edgarUrl, year);

        try {
            URL url = new URL(edgarUrl + year + "/");
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openStream()))) {
                return bufferedReader
                        .lines()
                        .parallel()
                        .filter(x -> x.contains("QTR"))
                        .peek(log::debug)
                        .count();
            }
        } catch (IOException ioException) {
            log.debug("IOException encountered: {}", ioException);
            throw ioException;
        }

    }



    /**
     * Refresh database with EDGAR URLs for current and past financial quarters.
     * @throws IOException This exception is thrown when URL is malformed or
     *                     error is encountered when files are read
     */
    @Override
    public void downloadEdgarUrls() throws IOException {

        /*
         * Refresh data for prior and current quarters. Allows for late updates
         */

        switch (LocalDate.now().getMonth()) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear() - 1),
                        FOURTH_QUARTER
                );
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        FIRST_QUARTER
                );
                break;
            case APRIL:
            case MAY:
            case JUNE:
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        FIRST_QUARTER
                );
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SECOND_QUARTER
                );
                break;
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SECOND_QUARTER
                );
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        THIRD_QUARTER
                );
                break;
            default:
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        THIRD_QUARTER
                );
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        FOURTH_QUARTER
                );
                break;
        }
    }


    /**
     * Refresh database with EDGAR URLs for given calendar year.
     *
     * @param year     Calendar year for which data is refreshed
     * @param quarter Financial quarter for which data must be extracted
     * @throws IOException This exception is thrown when URL is malformed or
     *                     error is encountered when files are read
     */
    @Override
    public void downloadEdgarUrls(final String year, final long quarter)
            throws IOException {

        List<SECData> secDataList = new ArrayList<SECData>();

        /*
         * File structure of SEC Filing URL data
         * Organization Name, Form Name, CIK, Fiiing Date, Form URL
         */
        Function<String, SECData> splitEdgarRowIntoFields = row -> new SECData(
                row.substring(0, INDEX_OF_FORM_NAME).trim(),
                row.substring(INDEX_OF_FORM_NAME, INDEX_OF_CIK).trim(),
                row.substring(INDEX_OF_CIK, INDEX_OF_FILING_DATE).trim(),
                LocalDate.parse(row.substring(INDEX_OF_FILING_DATE, INDEX_OF_EDGAR_URL).trim()),
                edgarPrefix + row.substring(INDEX_OF_EDGAR_URL).trim()
        );

        /*
         * Read company.idx and filter out unnecessary records
         */
        try {
            URL url = new URL(edgarUrl
                                   + year
                                   + "/QTR"
                                   + quarter
                                   + "/company.idx");
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openStream()))) {
                secDataList.addAll(
                        bufferedReader
                                .lines()
                                .parallel()
                                .filter(x -> x.contains("10-Q")
                                        || x.contains("10-K")
                                        || x.contains("10-Q/A")
                                        || x.contains("10-K/A"))
                                .map(splitEdgarRowIntoFields)
                                .peek(x -> log.debug(x.toString()))
                                .collect(Collectors.toList())
                );
            }
        } catch (IOException ioException) {
            log.debug("IOException encountered: {}", ioException);
            throw ioException;
        }

        /*
         * Save data to database in batches of batchSize
         */
        IntStream.range(0, (secDataList.size() + BATCH_SIZE - 1) / BATCH_SIZE)
                .mapToObj(i -> secDataList
                        .subList(i * BATCH_SIZE,
                                 Math.min(secDataList.size(),
                                         (i + 1) * BATCH_SIZE)
                                )
                )
                .forEach(batch -> secLinksRepo.saveAll(batch));
    }

}
