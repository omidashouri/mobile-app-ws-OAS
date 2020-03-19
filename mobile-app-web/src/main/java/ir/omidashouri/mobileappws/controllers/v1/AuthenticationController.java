package ir.omidashouri.mobileappws.controllers.v1;

/*import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;*/

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.omidashouri.mobileappws.models.request.UserLoginRequestModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "login", description = "Authentication Sample API")
public class AuthenticationController {

    @Operation(description = "User Login",
            responses = {
           @ApiResponse(
                   headers = {
                           @Header(
                                   name = "authorization",
                                   description = "Bearer <JWT value here>",
                                   schema = @Schema(
                                           type = "string",
                                           accessMode = Schema.AccessMode.READ_ONLY,
                                           format = "uuid"
                                   ),
                                   required = true),
                           @Header(name = "userid",description = "user Id description")
                   },
                   responseCode = "200",
                   description = "",
                   content = @Content(schema = @Schema(implementation = UserLoginRequestModel.class))),
/*            @ApiResponse(responseCode = "200",
                    content = @Content(
                            mediaType = "*//*",
                            schema = @Schema(
                                    type = "string",
                                    format = "uuid",
                                    description = "the generated UUID",
                                    accessMode = Schema.AccessMode.READ_ONLY,
                                    example = "Schema example"
                            ),
                            examples = {
                                    @ExampleObject(name = "Default Response", value = "SubscriptionResponse",
                                            summary = "Subscription Response Example", externalValue = "Subscription Response value 1")
                            }
                    )),*/
            @ApiResponse(
                    responseCode = "404",
                    description = "Person with such e-mail doesn't exists")
                    })
/*    @ApiResponses({
            @ApiResponse(code = 200,
            message = "Response Headers",
            responseHeaders = {
                    @ResponseHeader(name = "authorization",
                    description = "Bearer <JWT value here>",
                    response = String.class),
                    @ResponseHeader(name = "userID",
                    description = "<Public User ID value here>",
                    response = String.class)
            })
    })*/
    @PostMapping("/users/login")
    public void theFakeLogin(@RequestBody UserLoginRequestModel userLoginRequestModel){

//        the reason to throw this exception is that this method should no really execute when calling this controller
//        because spring security override this login
        throw new IllegalStateException("This method should no be called. This method is implemented by spring security.");
    }
}
