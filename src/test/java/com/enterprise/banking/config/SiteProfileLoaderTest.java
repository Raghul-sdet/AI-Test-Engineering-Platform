package com.enterprise.banking.config;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;

public class SiteProfileLoaderTest {

    @Test
    public void testLoadParaBankProfileSuccessfully() {
        SiteProfileLoader loader = new SiteProfileLoader("paraBank");
        
        Assert.assertEquals(loader.getBaseUrl(), "https://parabank.parasoft.com/parabank");
        Assert.assertEquals(loader.getApiBaseUrl(), "https://parabank.parasoft.com/parabank/services/bank");
        
        String locator = loader.getLocator("login", "usernameField");
        Assert.assertEquals(locator, "name=username");
        
        Map<String, String> endpoint = loader.getApiEndpoint("transferFunds");
        Assert.assertEquals(endpoint.get("method"), "POST");
        Assert.assertEquals(endpoint.get("path"), "/transfer");
        
        String testData = loader.getTestData("defaultCustomerId");
        Assert.assertEquals(testData, "12212");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*does not exist.*")
    public void testLoadNonexistentProfileThrowsError() {
        new SiteProfileLoader("nonexistent-profile");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*missing in profile.*")
    public void testGetNonexistentLocatorThrowsError() {
        SiteProfileLoader loader = new SiteProfileLoader("paraBank");
        loader.getLocator("login", "nonexistentElement");
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = ".*missing in profile.*")
    public void testGetNonexistentTestDataThrowsError() {
        SiteProfileLoader loader = new SiteProfileLoader("paraBank");
        loader.getTestData("nonexistentFixture");
    }
}
