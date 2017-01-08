package org.openhapi.smarthome2016.server.resources;

import io.dropwizard.hibernate.UnitOfWork;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.db.UserDAO;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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


}
