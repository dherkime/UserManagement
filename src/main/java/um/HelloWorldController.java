package um;

import java.util.List;

import domain.User;
import domain.UserResource;
import exceptions.ErrorInfo;
import exceptions.LoginRefusedException;
import exceptions.UserEmailAddressInUseException;
import exceptions.UserNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by don on 9/28/15.
 */
@Controller
@RequestMapping("/api")
@Api(value="/api", description="Operations for User Management")
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "List all the users", httpMethod = "GET")
    @RequestMapping(value = "/users")
    @ResponseBody
    public HttpEntity<List<User>> list() {
        List<User> users = userService.findAll();
        final List<UserResource> userResources = UserResource.from(users);
//        for (UserResource userResource : userResources) {
//            //Note the trailing slash...  I believe this is necessary so that Spring MVC can properly
//            //extract the email address as the {id} and not just extract the portion before the dot
//            userResource.add(linkTo(methodOn(HelloWorldController.class).user(userResource.getEmailAddress() + "/")).withSelfRel());
//        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @ApiOperation(value = "Find a user with a specified ID", httpMethod = "GET")
    @RequestMapping(value = "/users/{id}")
    @ResponseBody
    public HttpEntity<User> user(@ApiParam(name="id", value="The ID (email address **with a trailing forward slash**) of the user to be viewed", required=true)
                                         @PathVariable String id) {
        User user = userService.findById(id);
        if (null == user) {
            throw new UserNotFoundException("The user with id \"" + id + "\" was not found.");
        }
        final UserResource userResource = UserResource.from(user);

        //Note the trailing slash...  I believe this is necessary so that Spring MVC can properly
        //extract the email address as the {id} and not just extract the portion before the dot
        Link link = new Link(linkTo(methodOn(HelloWorldController.class).user(id)).withSelfRel().getHref() + "/");
        userResource.add(link);

        return new ResponseEntity<User>(user, HttpStatus.OK);
        //return new ResponseEntity<UserResource>(userResource, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new user using a JSON request body", httpMethod = "POST")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<User> create(@Valid @RequestBody UserResource userResource) {
        final User user = userService.create(userResource.toUser());
        final UserResource createdUserResource = UserResource.from(user);
        Link link = linkTo(methodOn(HelloWorldController.class).user(user.getId() + "/")).withSelfRel();
        createdUserResource.add(link);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
        //return new ResponseEntity<UserResource>(createdUserResource, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the user using a JSON request body", httpMethod = "PUT")
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ResponseBody
    public HttpEntity<User> update(@Valid @RequestBody UserResource userResource) {
        final User user = userService.update(userResource.toUser());
        final UserResource createdUserResource = UserResource.from(user);
        Link link = linkTo(methodOn(HelloWorldController.class).user(user.getId() + "/")).withSelfRel();
        createdUserResource.add(link);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
        //return new ResponseEntity<UserResource>(createdUserResource, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete the user with the given ID", httpMethod = "DELETE")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@ApiParam(name="id", value="The ID (email address **with a trailing forward slash**) of the user to be viewed", required=true)
                                         @PathVariable String id) {
        userService.delete(id);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @ApiOperation(value = "Login the user with the given ID and password", httpMethod = "GET")
    @RequestMapping(value = "/users/login", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<String> user(@ApiParam(name="id", value="The ID (email address) of the user to be viewed", required=true)
                                   @RequestParam("emailAddress") String emailAddress,
                                   @ApiParam(name="password", value="The plaintext (!!!) login password for the user", required=true)
                                   @RequestParam("password") String password) {
        userService.login(emailAddress, password);
        return new ResponseEntity<String>(HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserEmailAddressInUseException.class)
    @ResponseBody
    ErrorInfo handleUserEmailAddressInUseException(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    ErrorInfo handleUserNotFoundException(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginRefusedException.class)
    @ResponseBody
    ErrorInfo handleLoginRefusedException(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

}
