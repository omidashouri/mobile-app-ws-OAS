package ir.omidashouri.mobileappws.models.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationStatusModel {

    @Schema(
            description = "Operation result"
    )
    private String operationResult;

    @Schema(
            description = "Operation name"
    )
    private String operationName;
}
