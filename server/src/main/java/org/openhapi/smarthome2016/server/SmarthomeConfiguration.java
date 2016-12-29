package org.openhapi.smarthome2016.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;
import org.openhapi.smarthome2016.server.board.ServiceImpl;
import org.openhapi.smarthome2016.server.board.ServiceInterface;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

public class SmarthomeConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    private Map<String, Map<String, String>> viewRendererConfiguration = Collections.emptyMap();

    @NotEmpty
    private String boardServiceClass;

    @JsonProperty
    public void setBoardServiceClass(String boardServiceClass) {
        this.boardServiceClass = boardServiceClass;
    }


    public ServiceInterface getBoardService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ServiceInterface boardService = (ServiceInterface) Class.forName(boardServiceClass).newInstance();
        return boardService;
    }



    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }
//
//    @JsonProperty("viewRendererConfiguration")
//    public Map<String, Map<String, String>> getViewRendererConfiguration() {
//        return viewRendererConfiguration;
//    }
//
//    @JsonProperty("viewRendererConfiguration")
//    public void setViewRendererConfiguration(Map<String, Map<String, String>> viewRendererConfiguration) {
//        final ImmutableMap.Builder<String, Map<String, String>> builder = ImmutableMap.builder();
//        for (Map.Entry<String, Map<String, String>> entry : viewRendererConfiguration.entrySet()) {
//            builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
//        }
//        this.viewRendererConfiguration = builder.build();
//    }
}
