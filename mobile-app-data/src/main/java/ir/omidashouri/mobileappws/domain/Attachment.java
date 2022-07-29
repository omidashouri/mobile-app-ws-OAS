
package ir.omidashouri.mobileappws.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

//@JsonPropertyOrder({"addressPublicId"}) //specify order by our will
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TBL_ATTACHMENT",schema = "photo_app")
public class Attachment extends BaseEntity {

    private static final long serialVersionUID = -2935476350760223385L;

    @Column(name= "FILE_NAME")
    private String fileName;

    @Column(name= "FILE_TYPE")
    private String fileType;

    @Lob
    @Column(name= "DATA")
    private byte[] data;



}
