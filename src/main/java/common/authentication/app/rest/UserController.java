package common.authentication.app.rest;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import common.authentication.app.api.ApiError;
import common.authentication.app.api.UserApi;
import common.authentication.app.rest.request.CreateUserRequest;
import common.authentication.domain.exception.GenericException;
import common.authentication.domain.exception.UserException;
import common.authentication.domain.service.user.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@RestController
@RequestMapping(path = "v1/users/")
public class UserController extends CommonController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Create user.")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "User created."),
            @ApiResponse(code = 400, message = "Input data error.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ApiError.class) })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserApi> create(@Valid @RequestBody final CreateUserRequest request) throws UserException {
        return new ResponseEntity<>(userService.create(request), CREATED);
    }

    @ApiOperation(value = "Get user by username.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "User data."),
            @ApiResponse(code = 400, message = "Input data error.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ApiError.class) })
    @GetMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserApi> getByUsername(@PathVariable(value = "username") final String username)
            throws UserException {
        return ResponseEntity.ok(userService.get(username));
    }

    @ApiOperation(value = "Get user using filters.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "User data."),
            @ApiResponse(code = 400, message = "Input data error.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ApiError.class) })
    @GetMapping(path = "/findByFilters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserApi> get(@RequestParam(value = "documentNumber") final String documentNumber,
                                       @RequestParam(value = "phoneNumber") final String phoneNumber,
                                       @RequestParam(value = "username") final String username,
                                       @RequestParam(value = "email") final String email) throws UserException {
        return ResponseEntity.ok(userService.get(username, documentNumber, email, phoneNumber));
    }

    @ApiOperation(value = "Verify email.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "User data."),
            @ApiResponse(code = 400, message = "Input data error.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ApiError.class) })
    @PostMapping(path = "/{username}/verifyEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> verifyEmail(@PathVariable(value = "username") final String username)
            throws GenericException {
        userService.enableAccount(username);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete user.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "User deleted."),
            @ApiResponse(code = 400, message = "Input data error.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ApiError.class) })
    @DeleteMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable(value = "username") final String username) throws UserException {
        userService.delete(username);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get all users.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Users data."),
            @ApiResponse(code = 400, message = "Input data error.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = ApiError.class) })
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserApi>> get() {
        return ResponseEntity.ok(userService.getAll());
    }

}
