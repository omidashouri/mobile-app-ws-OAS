package ir.omidashouri.mobileappws.controllers.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.omidashouri.mobileappws.exceptions.UserServiceException;
import ir.omidashouri.mobileappws.mapper.AddressRestMapper;
import ir.omidashouri.mobileappws.mapper.UserDtoUserReqMapper;
import ir.omidashouri.mobileappws.models.dto.AddressDto;
import ir.omidashouri.mobileappws.models.dto.UserDto;
import ir.omidashouri.mobileappws.models.request.*;
import ir.omidashouri.mobileappws.models.response.*;
import ir.omidashouri.mobileappws.services.AddressService;
import ir.omidashouri.mobileappws.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "users", description = "The user API")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final UserDtoUserReqMapper userDtoUserReqMapper;
    private final AddressRestMapper addressRestMapper;

//    response to All ajax from any origin and any domain or specific by {"http://localhost:8082","http://localhost:8083"}
//    we can use it on class, we define it in WebConfig class
//    @CrossOrigin(origins = "*")

    // http://localhost:8080/v1/users/aLIRVt88hdQ858q5AMURm1QI6DC3Je
    // in header add Accept : application/xml or application/json
    @GetMapping(path = "/{userPublicId}",
//            make response as XML or JSON
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String userPublicId) {

        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserPublicId(userPublicId);

        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

/*    @ApiOperation(value = "THe Get User Details Web Service Endpoint ",
                  notes = "This web service end points return user details. use public user id in url path")*/
//  use RequestParam because want to retrieve query parameter from url
//  page start from zero
/*    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "${userController.authorization.header.description}", paramType = "header")
    })*/

    @Operation(
            summary = "find All users",
            description = "Find person by e-mail",
            tags = "users",
            security = @SecurityRequirement(name = "bearer-jwt"),
            responses = {
                    @ApiResponse(content = @Content(schema = @Schema(implementation = UserDto.class)), responseCode = "200"),
                    @ApiResponse(responseCode = "403", description = "Access Denied"),
                    @ApiResponse(responseCode = "404", description = "Person with such e-mail doesn't exists")})
    @ApiResponses(
            value = {
            @ApiResponse(
                    headers = {@Header(name = "authorization",description = "authorization description"),@Header(name = "userid")},
                    responseCode = "200",
                    description = "successful operation",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserRest.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getUsers(@RequestParam(value = "page", defaultValue = "1") int pageValue
            , @RequestParam(value = "limit", defaultValue = "25") int limitValue) {

        try {
            List<UserDto> userDtoList = userService.getUserDtosByPageAndLimit(pageValue, limitValue);

            List<UserRest> userRestList = new ArrayList<>();

            for (UserDto userDto : userDtoList) {
                UserRest userRest = new UserRest();
                BeanUtils.copyProperties(userDto, userRest);
                userRestList.add(userRest);
            }
            return ResponseEntity.ok(userRestList);

        } catch (Exception ex) {
            return badRequest(ex);
        }

    }

    @PostMapping(
//            consume for accepting XML or jason in RequestBody
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
            , produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        UserRest returnValue = new UserRest();
        try{

//        this must be username but here is email address
        if (userDetails.getFirstName().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto userDto = new UserDto();

//        BeanUtils.copyProperties(userDetails,userDto);
        userDto = userDtoUserReqMapper.UserDetailsReqToUserDto(userDetails);

        //omiddo: check username is not duplicate
/*       if(username is duplicate){
             return conflict();
            }
 */
        UserDto createdUserDto = userService.createUserDto(userDto);

//omiddo: change it later with mapper
//        BeanUtils.copyProperties(createdUserDto,returnValue);

        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(createdUserDto, UserRest.class);
        return ResponseEntity.ok(returnValue);

        }catch (Exception ex){
            return badRequest(ex);
        }
    }

    @PutMapping(path = "/{userPublicId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
            , produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    PathVariable is in url and RequestBody is in the body(raw) part of request
    public ResponseEntity<? extends Object> updateUser(@PathVariable String userPublicId, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        try{
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userDetails, userDto);

            //omiddo: check if(user do not exist in database){
            /*
            return notFound();
             */
            UserDto updatedUserDto = userService.updateUserDto(userPublicId, userDto);
            BeanUtils.copyProperties(updatedUserDto, returnValue);

            return ResponseEntity.ok(returnValue);
        } catch (Exception ex){
            return badRequest(ex);
        }
    }

    @DeleteMapping(path = "/{userPublicId}"
            , produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> deleteUser(@PathVariable String userPublicId) {

        try{

            OperationStatusModel operationStatusModel = new OperationStatusModel();
            operationStatusModel.setOperationName(RequestOperationName.DELETE.name());

            userService.deleteUserDto(userPublicId);

            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
            return ResponseEntity.ok(operationStatusModel);

        } catch (Exception ex){
            return badRequest(ex);
        }
    }

    //    http://localhost:8080/v1/users/SvWcmm8yptgOAS7Cw5QtDpdDjVVXfd/addresses
//    first create omidashouri3 then replace new public userId and addressId
//    use CollectionModel for HAL, so embedded collection in response are also json (which here is address)
    @GetMapping(path = "/{userPublicId}/addresses",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
                    "application/hal+json"})
    public CollectionModel<AddressRest> getUserAddresses(@PathVariable String userPublicId) {

        List<AddressDto> addressDtoes = addressService.getAddressDtosByUserPublicId(userPublicId);

        List<AddressRest> addressRestList = new ArrayList<>();

        if (addressDtoes != null && !addressDtoes.isEmpty()) {
            addressRestList = addressRestMapper.AddressDtoesToAddressRests(addressDtoes);
        }

        for (AddressRest addressRest : addressRestList) {
            Link addressLink = ControllerLinkBuilder
                    .linkTo(ControllerLinkBuilder
                            .methodOn(UserController.class)
                            .getUserAddress(userPublicId, addressRest.getAddressPublicId()))
                    .withSelfRel();

            addressRest.add(addressLink);

            Link userLink = ControllerLinkBuilder
                    .linkTo(ControllerLinkBuilder
                            .methodOn(UserController.class).getUser(userPublicId)).withRel("user");

            addressRest.add(userLink);
        }


        return new CollectionModel<>(addressRestList);
    }

    //    http://localhost:8080/v1/users/SvWcmm8yptgOAS7Cw5QtDpdDjVVXfd/addresses/qpiYxzrBGE73250AwK1ui1sAkpkXFw
//    first create omidashouri3 then replace new public userId and addressId
//    use EntityModel for HAL, so embedded collection in response are also json
    @GetMapping(path = "/{userPublicId}/addresses/{addressPublicId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
                    "application/hal+json"})
    public EntityModel<AddressRest> getUserAddress(@PathVariable String addressPublicId,
                                                   @PathVariable String userPublicId) {

        AddressDto addressDto = addressService.getAddressDtoByAddressPublicId(addressPublicId);

        AddressRest addressRest = new AddressRest();

        if (addressDto != null) {
            addressRest = addressRestMapper.AddressDtoToAddressRest(addressDto);
        }

        ModelMapper modelMapper = new ModelMapper();

//        old
/*        Link addressLink = ControllerLinkBuilder.linkTo(UserController.class)
                                .slash(userPublicId)
                                .slash("addresses")
                                .slash(addressPublicId)
                                .withSelfRel();    */

        Link addressLink = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder
                        .methodOn(UserController.class)
                        .getUserAddress(userPublicId, addressPublicId))
                .withSelfRel();

        Link userLink = ControllerLinkBuilder.linkTo(UserController.class)
                .slash(userPublicId)
                .withRel("user");
//        old
/*        Link addressesLink = ControllerLinkBuilder.linkTo(UserController.class)
                .slash(userPublicId)
                .slash("addresses")
                .withRel("addresses");  */

        Link addressesLink = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder
                        .methodOn(UserController.class).getUserAddresses(userPublicId))
                .withRel("addresses");

        addressRest.add(addressLink);
        addressRest.add(userLink);
        addressRest.add(addressesLink);

        return new EntityModel<>(addressRest);
    }

    /*
     * method for verifying email and when call it, do not need login
     * http://localhost:8080/v1/users/email-verification?token=sdfsdf
     */
    @GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

    //    http://localhost:8080/v1/users/password-reset-request
    @PostMapping(path = "/password-reset-request",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if (operationResult) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

    //    http://localhost:8080/v1/users/password-reset
    @PostMapping(path = "/password-reset",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.resetPassword(passwordResetModel.getToken()
                , passwordResetModel.getPassword());

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if (operationResult) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }


    private ResponseEntity<?> notFound() {
        return new ResponseEntity<>(
                new ErrorMessage(new Date(),HttpStatus.NOT_FOUND.toString(), "User not Found"),
                HttpStatus.NOT_FOUND
        );
    }

    private ResponseEntity<?> badRequest(Throwable throwable) {
        return new ResponseEntity<>(
                new ErrorMessage(new Date(), HttpStatus.BAD_REQUEST.toString(), "Bad request"),
                HttpStatus.BAD_REQUEST
        );
    }

    private ResponseEntity<?> conflict(){
        return new ResponseEntity<>(
                new ErrorMessage(new Date(),HttpStatus.CONFLICT.toString(),"Member already Exist"),
                HttpStatus.CONFLICT
        );
    }


}
