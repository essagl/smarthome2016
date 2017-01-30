package org.openhapi.smarthome2016.server.db;

import io.dropwizard.testing.junit.DAOTestRule;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openhapi.smarthome2016.server.core.BoardData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardDataDAOTest {

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
        .addEntityClass(BoardData.class)
        .build();

    private BoardDataDAO boardDataDAO;

    @Before
    public void setUp() throws Exception {
        boardDataDAO = new BoardDataDAO(daoTestRule.getSessionFactory());
    }

    @Test
    public void createData() {
        BoardData boardData = getBoardData();
        final BoardData storedData = daoTestRule.inTransaction(() -> boardDataDAO.createOrUpdate(boardData));
        assertThat(storedData.getId()).isGreaterThan(0);
        assertThat(storedData.getMessage()).isEqualTo("test");
    }

    private BoardData getBoardData() {
        double outDoorTemp = -3.45;
        double inDoorTemp = 19.68;
        double humidity = 45.98;
        boolean switchOpen = false;
        String message = "test";
        DateTime datetime = DateTime.now();

        return new BoardData(datetime,
                outDoorTemp,
                inDoorTemp,
                humidity,
                switchOpen,
                message);
    }

    @Test
    public void findAllAfter() {
        daoTestRule.inTransaction(() -> {
            boardDataDAO.createOrUpdate(getBoardData());
        });
        DateTime datetime = DateTime.now().plusHours(1);

        final List<BoardData>boardData = boardDataDAO.findAllNewerThan(datetime);
        assertThat(boardData.size()).isEqualTo(0);

        DateTime datetime1 = DateTime.now().minusHours(1);

        final List<BoardData>boardData1 = boardDataDAO.findAllNewerThan(datetime1);
        assertThat(boardData1.size()).isGreaterThan(0);
    }

}
