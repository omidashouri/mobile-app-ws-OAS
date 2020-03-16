package ir.omidashouri.mobileappws.models.response;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    private Date timestamp;
    private String errorCode;
    private String errorMessage;



}
