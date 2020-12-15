package dj.mr.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class SecLinksCompositeKey implements Serializable {

    /**
     * Name of document filed. Example: 10-Q, 10-K.
     */
    @Id
    @NotNull
    private final String formName;

    /**
     * CIK is unique identifier assigned to a company
     * filing financial data with SEC.
     */
    @Id
    @NotNull
    private final String cik;

    /**
     * Date form was prepared/filed with SEC.
     */
    @Id
    @NotNull
    private final LocalDate filingDate;

}
