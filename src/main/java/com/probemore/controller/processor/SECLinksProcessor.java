/**
 * Interface that defines contracts for SECLinksProcessor methods.
 */
package com.probemore.controller.processor;

import java.util.List;

public interface SECLinksProcessor {

    /**
     * EDGAR data is organized by year. Under year, data is
     * organized by quarters
     * @param year - The "Year" folder that must be navigated
     * @return Returns list of QTR-n directories present in given folder
     */
    public List<String> getDirectoriesInURI(String year) ;

}
