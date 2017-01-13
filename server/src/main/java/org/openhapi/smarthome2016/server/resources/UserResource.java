package org.openhapi.smarthome2016.server.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.db.UserDAO;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api("/user")
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
    @ApiOperation(value = "Create a user", notes = "Creating a user with roles USER / ADMIN")
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
    public List<User> listUsers() {
        return userDAO.findAll();
    }



    @GET
    @UnitOfWork
    @RolesAllowed("ADMIN")
    @Path("{userName}")
    public User getUser(@PathParam("userName") String userName) {
        return userDAO.findByName(userName).orElseThrow(() -> new NotFoundException("No such user."));
    }

    @GET
    @UnitOfWork
    @RolesAllowed("USER")
    @Path("user")
    public User getUser(@QueryParam("name") String userName,@QueryParam("password") String password) {
        return userDAO.findByNameAndPassword(userName,password).orElseThrow(() -> new NotFoundException("No such user."));
    }

}
