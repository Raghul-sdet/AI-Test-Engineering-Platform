package com.enterprise.banking.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import java.net.HttpURLConnection;
import java.net.URL;

public class WireMockSetup {
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && "stop".equals(args[0])) {
            System.out.println("Stopping WireMock...");
            try {
                HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8089/__admin/shutdown").openConnection();
                con.setRequestMethod("POST");
                con.getResponseCode();
                System.out.println("WireMock shutdown command sent.");
            } catch (Exception e) {
                System.out.println("WireMock not running or could not be reached on 8089.");
            }
            return;
        }

        // Start mode
        System.out.println("Checking if port 8089 is already in use...");
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8089/__admin/mappings").openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == 200) {
                System.out.println("WireMock is already running on port 8089. Reusing existing instance.");
                HttpURLConnection resetCon = (HttpURLConnection) new URL("http://localhost:8089/__admin/reset").openConnection();
                resetCon.setRequestMethod("POST");
                resetCon.getResponseCode();
            } else {
                System.err.println("ERROR: Port 8089 is in use by another application! Cannot start WireMock.");
                System.exit(1);
            }
        } catch (Exception e) {
            // Port is free or not WireMock. We can safely start.
            System.out.println("Port 8089 is free. Starting WireMock server...");
            WireMockServer wireMockServer = new WireMockServer(options().port(8089));
            wireMockServer.start();
        }

        configureFor("localhost", 8089);

        stubFor(get(urlEqualTo("/parabank/services/bank/accounts/12345/transactions"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"id\":14143,\"accountId\":12345,\"type\":\"Credit\",\"date\":1693958400000,\"amount\":100.00,\"description\":\"Deposit\"}]")
                .withUniformRandomDelay(50, 200)));

        stubFor(get(urlEqualTo("/parabank/services/bank/login/john/demo"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"id\":12212,\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":{\"street\":\"123 Main St\",\"city\":\"Anytown\",\"state\":\"CA\",\"zipCode\":\"12345\"},\"phoneNumber\":\"555-1234\",\"ssn\":\"999-99-9999\"}")
                .withUniformRandomDelay(50, 200)));

        stubFor(post(urlEqualTo("/parabank/services/bank/transfer"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/plain")
                .withBody("Successfully transferred $1.00 from account 12345 to account 12456")
                .withUniformRandomDelay(50, 200)));

        System.out.println("WireMock initialized with performance testing stubs on port 8089.");

        // Loop forever to keep JVM alive until shut down
        while (true) {
            try {
                HttpURLConnection checkCon = (HttpURLConnection) new URL("http://localhost:8089/__admin/").openConnection();
                checkCon.setRequestMethod("GET");
                checkCon.getResponseCode();
                Thread.sleep(2000);
            } catch (Exception ex) {
                // Server stopped
                System.out.println("WireMock server stopped. Exiting JVM.");
                break;
            }
        }
    }
}
