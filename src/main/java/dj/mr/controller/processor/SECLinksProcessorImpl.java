package dj.mr.controller.processor;

import dj.mr.config.Constants;
import dj.mr.data.SECLinksRepository;
import dj.mr.model.SecLinks;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import static dj.mr.config.Constants.BATCH_SIZE;
import static dj.mr.config.Constants.INDEX_OF_CIK;
import static dj.mr.config.Constants.INDEX_OF_EDGAR_URL;
import static dj.mr.config.Constants.INDEX_OF_FILING_DATE;
import static dj.mr.config.Constants.INDEX_OF_FORM_NAME;
import static dj.mr.config.Constants.LENGTH_OF_CIK;
import static dj.mr.config.Constants.PREFIX_FOR_CIK;
import static dj.mr.config.Constants.SEC_LINKS_FIRST_QUARTER;
import static dj.mr.config.Constants.SEC_LINKS_FOURTH_QUARTER;
import static dj.mr.config.Constants.SEC_LINKS_SECOND_QUARTER;
import static dj.mr.config.Constants.SEC_LINKS_THIRD_QUARTER;

/**
 * Contains methods that aid REST operations in SecLinks.
 */
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
     * Interface that defines DB operations.
     */
    @Autowired
    private SECLinksRepository secLinksRepo;

    /**
     * Fetch EDGAR URLs with data filtered by parameters passed.
     *
     * @param cik             that needs to be looked up
     * @param formname        that is used to filter data
     * @param startfilingdate that is used to filter data
     * @param endfilingdate   that is used to filter data
     * @param offset          that defines number of records to skip
     * @param length          that defines number of records to fetch
     * @return Returns list of all SEC URLs
     */
    public List<SecLinks> getFilteredUrls(
            final Optional<String> cik,
            final Optional<String> formname,
            final Optional<LocalDate> startfilingdate,
            final Optional<LocalDate> endfilingdate,
            final Optional<Integer> offset,
            final Optional<Integer> length
    ) {
        Pageable pageable = PageRequest.of(
                offset.orElse(Constants.SEC_LINKS_DEFAULT_OFFSET),
                length.orElse(Constants.SEC_LINKS_DEFAULT_GET_SIZE));
        return secLinksRepo
                .findAllByCikBetweenAndFormNameBetweenAndFilingDateBetweenOrderByFilingDateDesc(
                        cik.orElse(""),
                        cik.orElse(Constants.SEC_LINKS_COMPARISION_STRING_MAX_VALUE),
                        formname.orElse(""),
                        formname.orElse(Constants.SEC_LINKS_COMPARISION_STRING_MAX_VALUE),
                        startfilingdate.orElse(
                                LocalDate.now().minusYears(Constants.SEC_LINKS_MINIMUM_FILING_YEAR)
                        ),
                        endfilingdate.orElse(LocalDate.now()),
                        pageable
                );
    }

    /**
     * Refresh database with EDGAR URLs for given year, quarter.
     *
     * @param year    Calendar year for which data is refreshed
     * @param quarter Financial quarter for which data must be extracted
     * @throws IOException This exception is thrown when URL is malformed or
     *                     error is encountered when files are read
     */
    @Override
    public void downloadEdgarUrls(
            final Optional<String> year,
            final Optional<Long> quarter)
            throws IOException {

        //refresh data for current and past quarter
        if (!year.isPresent() && !quarter.isPresent()) {
            downloadEdgarUrls();
            //refresh data for all quarters in given year
        } else if (!quarter.isPresent()) {
            long numDirs = getDirectoryCountInURI(year.get());
            for (long i = 1L; i <= numDirs; i++) {
                downloadEdgarUrls(year.get(), i);
            }
            //refresh data for given year and quarter
        } else {
            downloadEdgarUrls(year.get(), quarter.get());
        }

    }

    /**
     * EDGAR data is organized by year. Under year, data is
     * organized by quarters. This method returns those quarters
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
            log.debug("IOException encountered: {}", ioException.getMessage());
            throw ioException;
        }

    }

    /**
     * Refresh database with EDGAR URLs for current and past financial quarters.
     *
     * @throws IOException This exception is thrown when URL is malformed or
     *                     error is encountered when files are read
     */
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
                        SEC_LINKS_FOURTH_QUARTER
                );
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SEC_LINKS_FIRST_QUARTER
                );
                break;
            case APRIL:
            case MAY:
            case JUNE:
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SEC_LINKS_FIRST_QUARTER
                );
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SEC_LINKS_SECOND_QUARTER
                );
                break;
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SEC_LINKS_SECOND_QUARTER
                );
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SEC_LINKS_THIRD_QUARTER
                );
                break;
            default:
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SEC_LINKS_THIRD_QUARTER
                );
                downloadEdgarUrls(
                        String.valueOf(LocalDate.now().getYear()),
                        SEC_LINKS_FOURTH_QUARTER
                );
                break;
        }
    }


    /**
     * Refresh database with EDGAR URLs for given year, quarter.
     *
     * @param year    Calendar year for which data is refreshed
     * @param quarter Financial quarter for which data must be extracted
     * @throws IOException This exception is thrown when URL is malformed or
     *                     error is encountered when files are read
     */
    private void downloadEdgarUrls(final String year,
                                   final long quarter)
            throws IOException {

        List<SecLinks> secDataList = new ArrayList<>();

        /*
         * File structure of SEC Filing URL data
         * Organization Name, Form Name, CIK, Fiiing Date, Form URL
         */
        Function<String, SecLinks> splitEdgarRowIntoFields
                = row -> new SecLinks(
                row.substring(0, INDEX_OF_FORM_NAME).trim(),
                row.substring(INDEX_OF_FORM_NAME, INDEX_OF_CIK).trim(),
                StringUtils.leftPad(
                        row.substring(INDEX_OF_CIK, INDEX_OF_FILING_DATE).trim(),
                        LENGTH_OF_CIK,
                        PREFIX_FOR_CIK
                ),
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
                                        || x.contains("10-K"))
                                .filter(x -> !x.contains("NT 10-K")
                                        && !x.contains("NT 10-Q"))
                                .map(splitEdgarRowIntoFields)
                                .peek(x -> log.debug(x.toString()))
                                .collect(Collectors.toList())
                );
            }
        } catch (IOException ioException) {
            log.debug("IOException encountered: {}", ioException.getMessage());
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
