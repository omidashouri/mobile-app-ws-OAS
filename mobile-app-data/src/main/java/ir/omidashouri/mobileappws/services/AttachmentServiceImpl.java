package ir.omidashouri.mobileappws.services;

import ir.omidashouri.mobileappws.domain.Attachment;
import ir.omidashouri.mobileappws.repositories.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("file name contains invalid path sequence" + fileName);
            }

            Attachment attachment = new Attachment(fileName, file.getContentType(), file.getBytes());

            return attachmentRepository.save(attachment);
        } catch (Exception exception) {
            throw new Exception("could not save file" + fileName);
        }
    }

    @Override
    public Attachment getAttachmentByTd(String fileId) throws Exception {
        return attachmentRepository.findById(Long.valueOf(fileId))
                .orElseThrow(()->new Exception("File not found with id: "+fileId));
    }
}
