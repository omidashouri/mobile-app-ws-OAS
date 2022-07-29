package ir.omidashouri.mobileappws.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponse implements Serializable {

    private String fileName;
    private String downloadUrl;
    private String fileType;
    private long fileSize;
}
