package io.quarkiverse.quteserverpages.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.vertx.core.Context;

public class BlockingHandlerTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest().withApplicationRoot(root -> {
        root
                .addAsResource(new StringAsset(
                        "blocking={cdi:bean.isOnWorkerThread}"),
                        "templates/blocking.txt")
                .addAsResource(new StringAsset(
                        "quarkus.qsp.use-blocking-handler=true"),
                        "application.properties");
    });

    @Test
    public void testHandler() {
        given()
                .when().get("/qsp/blocking")
                .then()
                .statusCode(200)
                .body(containsString("blocking=true"));

    }

    @Named
    @Singleton
    public static class Bean {

        public boolean isOnWorkerThread() {
            return Context.isOnWorkerThread();
        }

    }

}
