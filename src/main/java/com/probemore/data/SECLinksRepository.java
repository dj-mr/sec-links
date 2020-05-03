/**
 * DAO for SEC Urls Content.
 */
package com.probemore.data;

import com.probemore.model.SECLinks;
import com.probemore.model.SECLinksCompositeKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SECLinksRepository
        extends JpaRepository<SECLinks, SECLinksCompositeKey> {

    /**
     * Fetch all CIK related content in Descending order of Filing Date.
     * @param minCik is minimum value of CIK being searched
     * @param maxCik is maximum value of CIK being searched
     * @param minFormName is minimum value of form being searched. Eg 10-K
     * @param maxFormName is minimum value of form being searched. Eg NT 10-Q
     * @param minFilingDate is minimum value of filing year being searched
     * @param maxFilingDate is maximum value of filing year being searched
     * @param pageable defines offset and length values for extraction
     * @return returns list of all SEC URLs
     */
    List<SECLinks> findAllByCikBetweenAndFormNameBetweenAndFilingDateBetweenOrderByFilingDateDesc(
            String minCik,
            String maxCik,
            String minFormName,
            String maxFormName,
            LocalDate minFilingDate,
            LocalDate maxFilingDate,
            Pageable pageable
    );

}
