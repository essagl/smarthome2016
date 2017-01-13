package org.openhapi.smarthome2016.server.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.db.UserDAO;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "user", description = "Endpoint for user management")
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @UnitOfWork
    @RolesAllowed("ADMIN")
    @ApiOperation(value = "Create or update a user", notes = "ADMIN role required! Creating a user requires to omit the id,<br> " +
            "roles are a comma separated string with USER or ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User creates", response = User.class),
            @ApiResponse(code = 401, message = "Not allowed - The auth header must contain admin credentials"),
            @ApiResponse(code = 500, message = "Internal error") })
    public User createUser(User user) {
        return userDAO.createOrUpdate(user);
    }

    @GET
    @UnitOfWork
    @Path("list")
    @RolesAllowed("ADMIN")
    @ApiOperation(value = "Create a list of all users", notes = "ADMIN role required!")
    public List<User> listUsers() {
        return userDAO.findAll();
    }



    @GET
    @UnitOfWork
    @RolesAllowed("ADMIN")
    @Path("{userName}")
    @ApiOperation(value = "Find user by username", notes = "ADMIN role required!")
    public User getUser(@PathParam("userName") String userName) {
        return userDAO.findByName(userName).orElseThrow(() -> new NotFoundException("No such user."));
    }

    @GET
    @UnitOfWork
    @RolesAllowed("USER")
    @Path("user")
    @ApiOperation(value = "Find user by username and password", notes = "USER role required!")
    public User getUser(@ApiParam(name = "name", value = "Alphanumeric username", required = true)
                            @QueryParam("name") String userName,
                        @ApiParam(name = "password", value = "Alphanumeric password", required = true)
                        @QueryParam("password") String password) {
        return userDAO.findByNameAndPassword(userName,password).orElseThrow(() -> new NotFoundException("No such user."));
    }

}
