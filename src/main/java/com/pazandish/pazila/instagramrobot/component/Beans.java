package com.pazandish.pazila.instagramrobot.component;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Component
public class Beans {
    @Autowired
    private WebDriver driver;

    @Bean
    public WebDriver webDriver() {
        ChromeOptions options = new ChromeOptions()
                .addArguments("--no-sandbox")
                .addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                .addArguments("--window-size=1920,1080")
                .setHeadless(true);

        ChromeDriver driver = new ChromeDriver(options);
//        driver.setLogLevel(Level.WARNING);
        try {
            driver.get("https://www.instagram.com/");
            Thread.sleep(5000);

            WebElement username = driver.findElement(By.name("username"));
            WebElement password = driver.findElement(By.name("password"));
            username.sendKeys("username");
            password.sendKeys("password");
            password.sendKeys(Keys.ENTER);
            Thread.sleep(3000);

        } catch (InterruptedException e) {
//            driver.quit();
        } finally {
//            driver.quit();
        }
        Set<Cookie> cookieSet = driver.manage().getCookies();
        cookieSet.forEach(cookie -> {
            // TODO: 6/1/2021
        });
        return driver;
    }

}