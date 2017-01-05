package org.openhapi.smarthome2016.server.resources;


import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jackson.Jackson;
import org.apache.commons.codec.digest.DigestUtils;
import org.openhapi.smarthome2016.server.Authorized;
import org.openhapi.smarthome2016.server.core.User;
import org.openhapi.smarthome2016.server.core.UserRole;
import org.openhapi.smarthome2016.server.db.UserDao;
import org.openhapi.smarthome2016.server.exception.ResponseException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.util.Random;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserDao userDao;


    public UserResource(UserDao userDao) {
        this.userDao = userDao;

    }

    @POST
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public JsonNode post(@Valid User user) {
        User existingUser = userDao.retrieve(user.getEmail());
        if (existingUser == null) {
            Random r = new SecureRandom();
            byte[] saltBytes = new byte[32];
            r.nextBytes(saltBytes);
            String salt = org.apache.commons.codec.digest.DigestUtils.sha256Hex(saltBytes);
            user.setSalt(salt);
            String password = user.getPassword();
            String hashedPassword = DigestUtils.sha256Hex(salt+password);
            user.setPassword(hashedPassword);

            user.setRole(UserRole.VIEWER);

            userDao.create(user.getEmail(), user.getPassword(), user.getSalt(), user.getRole().toString());
        } else {
            String sentPasswordHashed = DigestUtils.sha256Hex(existingUser.getSalt()+user.getPassword());
            if (!sentPasswordHashed.equals(existingUser.getPassword())) {
                ResponseException.formatAndThrow(Response.Status.BAD_REQUEST, "Invalid email / password");
            }
            user = existingUser;
        }

       // Session session = sessionDao.createSession(user.getEmail());
        ObjectNode object = (ObjectNode) Jackson.newObjectMapper().convertValue(user, JsonNode.class);
        object.put("user", user.getEmail());
        return object;
    }

    @PUT
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public User put(@Authorized User existingUser, User user) {
        if (user.getPassword() != null) {
            Random r = new SecureRandom();
            byte[] saltBytes = new byte[32];
            r.nextBytes(saltBytes);
            String salt = DigestUtils.sha256Hex(saltBytes);
            existingUser.setSalt(salt);
            String password = user.getPassword();
            String hashedPassword = DigestUtils.sha256Hex(salt+password);
            existingUser.setPassword(hashedPassword);
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }
        if (user.getEmail() != null) {
            ResponseException.formatAndThrow(Response.Status.BAD_REQUEST, "You cannot update a user's email");
        }
        userDao.update(existingUser.getEmail(), existingUser.getPassword(), existingUser.getSalt(), existingUser.getRole().toString());

        return existingUser;
    }

    @GET
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public User get(@Authorized User existingUser) {
        return existingUser;
    }

    @DELETE
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public String delete(@Authorized User existingUser, @HeaderParam("Authorization") String sessionToken) {
        if (existingUser.getEmail().equals("admin")) {
            ResponseException.formatAndThrow(Response.Status.BAD_REQUEST, "You cannot delete the admin user");
        }

        userDao.delete(existingUser);


        return "{}";
    }

    @OPTIONS
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public void options() { }
}
