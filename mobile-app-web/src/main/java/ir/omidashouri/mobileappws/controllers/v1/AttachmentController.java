package ir.omidashouri.mobileappws.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.omidashouri.mobileappws.domain.Attachment;
import ir.omidashouri.mobileappws.models.response.AttachmentResponse;
import ir.omidashouri.mobileappws.models.response.ErrorMessage;
import ir.omidashouri.mobileappws.services.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/attachments")
@RequiredArgsConstructor
@Tag(name = "attachment", description = "Attachment Sample API")
public class AttachmentController {

    private final AttachmentService attachmentService;


    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

        Attachment attachment = null;
        String downloadUrl = null;
        attachment = attachmentService.saveAttachment(file);
        downloadUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/download/")
                .path(String.valueOf(attachment.getId()))
                .toUriString();
        AttachmentResponse attachmentResponse = new AttachmentResponse();
        attachmentResponse.setFileName(attachment.getFileName());
        attachmentResponse.setFileSize(file.getSize());
        attachmentResponse.setFileType(file.getContentType());
        attachmentResponse.setDownloadUrl(downloadUrl);
        return ResponseEntity.ok(attachmentResponse);
    }



    @Operation(
            summary = "Download image by ID",
            description = "Search and download image by id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "successful operation",
                            content = @Content(mediaType = "image/jpeg",
                                    schema = @Schema(type = "string",
                                            format = "binary",
                                            implementation = ByteArrayResource.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping(path = "/download/{fileId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = null;
        attachment = attachmentService.getAttachmentByTd(fileId);


//        example of download_link
        /*        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+attachment.getFileName()+"\""
                        )
                .body(new ByteArrayResource(attachment.getData()));*/


//        example of display image
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .contentLength(attachment.getData().length)
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .headers(httpHeaders)
                .body((new ByteArrayResource(attachment.getData())));
    }
}
