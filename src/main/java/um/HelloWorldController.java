package um;

import java.util.List;

import domain.User;
import domain.UserResource;
import exceptions.ErrorInfo;
import exceptions.LoginRefusedException;
import exceptions.UserEmailAddressInUseException;
import exceptions.UserNotFoundException;
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

@Controller
@RequestMapping("/api")
public class HelloWorldController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users")
    @ResponseBody
    public HttpEntity<List<UserResource>> list() {
        List<User> users = userService.findAll();
        final List<UserResource> userResources = UserResource.from(users);
        for (UserResource userResource : userResources) {
            //Note the trailing slash...  I believe this is necessary so that Spring MVC can properly
            //extract the email address as the {id} and not just extract the portion before the dot
            userResource.add(linkTo(methodOn(HelloWorldController.class).user(userResource.getEmailAddress() + "/")).withSelfRel());
        }
        return new ResponseEntity<List<UserResource>>(userResources, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}")
    @ResponseBody
    public HttpEntity<UserResource> user(@PathVariable String id) {
        User user = userService.findById(id);
        if (null == user) {
            throw new UserNotFoundException("The user with id \"" + id + "\" was not found.");
        }
        final UserResource userResource = UserResource.from(user);

        //Note the trailing slash...  I believe this is necessary so that Spring MVC can properly
        //extract the email address as the {id} and not just extract the portion before the dot
        Link link = new Link(linkTo(methodOn(HelloWorldController.class).user(id)).withSelfRel().getHref() + "/");
        userResource.add(link);

        return new ResponseEntity<UserResource>(userResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<UserResource> create(@Valid @RequestBody UserResource userResource) {
        final User user = userService.create(userResource.toUser());
        final UserResource createdUserResource = UserResource.from(user);
        Link link = linkTo(methodOn(HelloWorldController.class).user(user.getId() + "/")).withSelfRel();
        createdUserResource.add(link);
        return new ResponseEntity<UserResource>(createdUserResource, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ResponseBody
    public HttpEntity<UserResource> update(@Valid @RequestBody UserResource userResource) {
        final User user = userService.update(userResource.toUser());
        final UserResource createdUserResource = UserResource.from(user);
        Link link = linkTo(methodOn(HelloWorldController.class).user(user.getId() + "/")).withSelfRel();
        createdUserResource.add(link);
        return new ResponseEntity<UserResource>(createdUserResource, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable String id) {
        userService.delete(id);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<String> user(@RequestParam("emailAddress") String emailAddress,
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
