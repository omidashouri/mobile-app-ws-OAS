package ir.omidashouri.mobileappws.models.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    @Schema(
            description = "error date",
            example = "400"
    )
    private Date timestamp;

    @Schema(
            description = "HTTP status error code"
    )
    private String errorCode;

    @Schema(
            description = "Error Message and detail",
            example = "User not exist in Data Base"
    )
    private String errorMessage;



}
