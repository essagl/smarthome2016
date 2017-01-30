package org.openhapi.smarthome2016.server.db;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.openhapi.smarthome2016.server.core.BoardData;

import java.util.List;
import java.util.Optional;

public class BoardDataDAO extends AbstractDAO<BoardData> {
    public BoardDataDAO(SessionFactory factory) {
        super(factory);
    }


    public Optional<BoardData> findById(Long id) {
        return Optional.ofNullable(get(id));
    }



    /**
     * Create or update a boardData. When creating a new boardData the id propertiy must be null.
     * @param boardData
     * @return the boardData or null.
     */
    public BoardData createOrUpdate(BoardData boardData) {
        return persist(boardData);
    }


    public List<BoardData> findAllNewerThan(DateTime timestamp) {
        return list(namedQuery("org.openhapi.smarthome2016.server.core.BoardData.findNewerThan").setParameter("instant",timestamp));
    }
}
