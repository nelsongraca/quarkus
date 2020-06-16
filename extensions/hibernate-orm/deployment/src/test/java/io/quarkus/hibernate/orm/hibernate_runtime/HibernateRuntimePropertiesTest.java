package io.quarkus.hibernate.orm.hibernate_runtime;

import io.quarkus.bootstrap.model.AppArtifact;
import io.quarkus.builder.Version;
import io.quarkus.test.QuarkusProdModeTest;
import io.restassured.RestAssured;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.Matchers.is;

/**
 * @author Nelson Graça graca.nelson@gmail.com
 */
public class HibernateRuntimePropertiesTest {

    private static final HashMap<String, String> runtimeProperties = new HashMap<>();

    @RegisterExtension
    static QuarkusProdModeTest runner = new QuarkusProdModeTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(BasicEntity.class, TestResource.class))
            .withConfigurationResource("application-hibernate-runtime.properties")
            .setRun(true)
            .setApplicationName("hibernate-runtime")
            .setApplicationVersion(Version.getVersion())
            .setForcedDependencies(Arrays.asList(
                    new AppArtifact("io.quarkus", "quarkus-jdbc-h2", Version.getVersion()),
                    new AppArtifact("io.quarkus", "quarkus-resteasy", Version.getVersion()),
                    new AppArtifact("io.quarkus", "quarkus-hibernate-validator", Version.getVersion()),
                    new AppArtifact("org.hibernate", "hibernate-envers", "5.4.17.Final")
            ))
            .setRuntimeProperties(runtimeProperties);

    static {
        runtimeProperties.put("quarkus.datasource.username", "SOMEUSER");
        runtimeProperties.put("quarkus.hibernate-orm.database.default-schema", "other-schema");
        runtimeProperties.put("quarkus.hibernate-orm.database.default-catalog", "other-catalog");
    }

    @Test
    @Transactional
    public void checkUser() {
        RestAssured.when().get("/hibernate/user").then().body(is("SOMEUSER"));
    }

    @Test
    @Transactional
    public void checkSchema() {
        RestAssured.when().get("/hibernate/schema").then().body(is("other-schema"));
    }

    @Test
    @Transactional
    public void checkCatalog() {
        RestAssured.when().get("/hibernate/catalog").then().body(is("other-catalog"));
    }

}
