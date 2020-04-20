package com.probemore.data;

import com.probemore.model.SECData;

public interface SECLinksRepository {

    Iterable<SECData> finaAll();

    SECData findOne(String cik);

    SECData save(SECData secData);

}
