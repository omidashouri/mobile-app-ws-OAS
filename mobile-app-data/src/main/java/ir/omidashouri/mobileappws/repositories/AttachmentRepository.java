package ir.omidashouri.mobileappws.repositories;

import ir.omidashouri.mobileappws.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {


}
