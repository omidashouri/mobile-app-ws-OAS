package ir.omidashouri.mobileappws.services;

import ir.omidashouri.mobileappws.domain.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachmentByTd(String fileId) throws Exception;
}
