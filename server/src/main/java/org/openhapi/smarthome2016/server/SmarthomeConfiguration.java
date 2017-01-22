package org.openhapi.smarthome2016.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.NotEmpty;
import org.openhpi.smarthome2016.ServiceInterface;

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


    @Valid
    @NotNull
    private final SwaggerBundleConfiguration swagger = new SwaggerBundleConfiguration();

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration getSwagger() {
        return swagger;
    }


}
