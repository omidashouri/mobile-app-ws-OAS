package ir.omidashouri.mobileappws.models.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRest extends RepresentationModel<UserRest>{

    @Schema(
            description = "user unique identifier id",
            required = true
    )
    private String userPublicId;
    @Schema(
            description = "user first name"
    )
    private String firstName;
    @Schema(
            description = "user last name"
    )
    private String lastName;
    @Schema(
            description = "user email address"
    )
    private String email;
    @Schema(
            description = "user address detail"
    )
    private List<AddressRest> addresses;

}
