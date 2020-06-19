package io.quarkus.hibernate.orm.hibernate_runtime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.spi.ServiceRegistryImplementor;

@Path("/hibernate")
public class TestResource {

    @Inject
    EntityManager entityManager;

    @GET()
    @Path("/user")
    @Produces(MediaType.TEXT_PLAIN)
    public String user() throws SQLException {
        final Matcher matcher = Pattern.compile(".*url=(.+).*user=([a-zA-Z]+).+").matcher(getConnection().toString());
        if (matcher.matches()) {
            return matcher.group(2);
        }
        return "";
    }

    @GET()
    @Path("/schema")
    @Produces(MediaType.TEXT_PLAIN)
    public String schema() {
        return String.valueOf(getEMF().getProperties().get("hibernate.default_schema"));
    }

    @GET()
    @Path("/catalog")
    @Produces(MediaType.TEXT_PLAIN)
    public String catalog() {
        return String.valueOf(getEMF().getProperties().get("hibernate.default_catalog"));
    }

    private Connection getConnection() throws SQLException {
        final SessionFactoryImpl entityManagerFactory = getEMF();
        final ServiceRegistryImplementor serviceRegistry = entityManagerFactory.getServiceRegistry();
        return serviceRegistry.getService(ConnectionProvider.class).getConnection();
    }

    private SessionFactoryImpl getEMF() {
        return (SessionFactoryImpl) entityManager.getEntityManagerFactory();
    }

}
