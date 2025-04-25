import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.spi.JsonbProvider;
import test.api.ParameterSummary;
import test.api.RestServiceApi;

@RunWith(Arquillian.class)
public class ResteasyEncodedTest {

    private static final String APP_BASE_URL = "http://localhost:8080/resteasy-encoded-test/app";
    private static RestServiceApi resteasyClient;

    @BeforeClass
    public static void init() {
        resteasyClient = ((ResteasyClient) ResteasyClientBuilder.newBuilder().build())
                .target(APP_BASE_URL).proxy(RestServiceApi.class);
    }

    @Deployment
    public static Archive<?> createTestDeployment() throws Exception {

        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class,
                "resteasy-encoded-test.war");
        webArchive.addPackage("test.api");
        webArchive.addPackage("test.server");
        return webArchive;

    }

    @Test(expected = IOException.class) // invalid URL
    @RunAsClient
    public void testPlainWhitespaceInPathWithoutEncodedPath() throws Exception {
        ParameterSummary ps = readContentWithoutRestEasy("method/a b?qp=a%2Fb&eqp=a%2Fb");
        Assert.assertEquals("a b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedWhitespaceInPathWithoutEncodedPath() throws Exception {
        ParameterSummary ps = readContentWithoutRestEasy("method/a%20b?qp=a%2Fb&eqp=a%2Fb");
        Assert.assertEquals("a b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedWhitespaceInPathWithEncodedPath() throws Exception {
        ParameterSummary ps = readContentWithoutRestEasy("method-encoded/a%20b?qp=a%2Fb&eqp=a%2Fb");
        Assert.assertEquals("a%20b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test(expected = FileNotFoundException.class)
    @RunAsClient
    public void testPlainSlashInPathWithoutEncodedPath() throws Exception {
        ParameterSummary ps = readContentWithoutRestEasy("method/a/b?qp=a%2Fb&eqp=a%2Fb");
        Assert.assertEquals("a/b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test(expected = FileNotFoundException.class)
    @RunAsClient
    public void testPlainSlashInPathWithEncodedPath() throws Exception {
        ParameterSummary ps = readContentWithoutRestEasy("method-encoded/a/b?qp=a%2Fb&eqp=a%2Fb");
        Assert.assertEquals("a/b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedSlashInPathWithoutEncodedPath() throws Exception {
        ParameterSummary ps = readContentWithoutRestEasy("method/a%2Fb?qp=a%2Fb&eqp=a%2Fb");
        Assert.assertEquals("a/b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedSlashInPathWithEncodedPath() throws Exception {
        ParameterSummary ps = readContentWithoutRestEasy("method-encoded/a%2Fb?qp=a%2Fb&eqp=a%2Fb");
        Assert.assertEquals("a%2Fb", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedSlashInPathWithRestEasyClientMethod() throws Exception {
        ParameterSummary ps = resteasyClient.method("a%2Fb", "a/b", "a/b");
        Assert.assertEquals("a%2Fb", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a/b", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedSlashInPathAndInQueryParamsWithRestEasyClientMethod() throws Exception {
        ParameterSummary ps = resteasyClient.method("a%2Fb", "a%2Fb", "a%2Fb");
        Assert.assertEquals("a%2Fb", ps.getPathParam());
        Assert.assertEquals("a%2Fb", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedSlashInPathWithRestEasyClientMethodEncoded() throws Exception {
        ParameterSummary ps = resteasyClient.methodEncoded("a%2Fb", "a/b", "a/b");
        Assert.assertEquals("a%2Fb", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a/b", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedSlashInPathAndInQueryParamsWithRestEasyClientMethodEncoded()
            throws Exception {
        ParameterSummary ps = resteasyClient.methodEncoded("a%2Fb", "a%2Fb", "a%2Fb");
        Assert.assertEquals("a%2Fb", ps.getPathParam());
        Assert.assertEquals("a%2Fb", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testPlainSlashInPathWithRestEasyClientMethod() throws Exception {
        ParameterSummary ps = resteasyClient.method("a/b", "a/b", "a/b");
        Assert.assertEquals("a/b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a/b", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testPlainSlashInPathWithRestEasyClientMethodEncoded() throws Exception {
        ParameterSummary ps = resteasyClient.methodEncoded("a/b", "a/b", "a/b");
        Assert.assertEquals("a/b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a/b", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedWhitespaceInPathWithRestEasyClientMethod() throws Exception {
        ParameterSummary ps = resteasyClient.method("a%20b", "a/b", "a/b");
        Assert.assertEquals("a%20b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a/b", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedWhitespaceInPathAndInQueryParamsWithRestEasyClientMethod()
            throws Exception {
        ParameterSummary ps = resteasyClient.method("a%20b", "a%2Fb", "a%2Fb");
        Assert.assertEquals("a%20b", ps.getPathParam());
        Assert.assertEquals("a%2Fb", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedWhitespaceInPathWithRestEasyClientMethodEncoded() throws Exception {
        ParameterSummary ps = resteasyClient.methodEncoded("a%20b", "a/b", "a/b");
        Assert.assertEquals("a%20b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a/b", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testEncodedWhitespaceInPathAndInQueryParamsWithRestEasyClientMethodEncoded()
            throws Exception {
        ParameterSummary ps = resteasyClient.methodEncoded("a%20b", "a%2Fb", "a%2Fb");
        Assert.assertEquals("a%20b", ps.getPathParam());
        Assert.assertEquals("a%2Fb", ps.getQueryParam());
        Assert.assertEquals("a%2Fb", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testPlainWhitespaceInPathWithRestEasyClientMethod() throws Exception {
        ParameterSummary ps = resteasyClient.method("a b", "a/b", "a/b");
        Assert.assertEquals("a b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a/b", ps.getEncodedQueryParam());
    }

    @Test
    @RunAsClient
    public void testPlainWhitespaceInPathWithRestEasyClientMethodEncoded() throws Exception {
        // this should be an illegal request:
        ParameterSummary ps = resteasyClient.methodEncoded("a b", "a/b", "a/b");
        Assert.assertEquals("a b", ps.getPathParam());
        Assert.assertEquals("a/b", ps.getQueryParam());
        Assert.assertEquals("a/b", ps.getEncodedQueryParam());
    }

    private ParameterSummary readContentWithoutRestEasy(String methodPath) throws IOException {
        URL url = new URL(APP_BASE_URL + "/service/" + methodPath);

        try (InputStream is = url.openStream()) {
            Jsonb jsonb = JsonbProvider.provider().create().build();
            return jsonb.fromJson(is, ParameterSummary.class);
        }
    }

}
