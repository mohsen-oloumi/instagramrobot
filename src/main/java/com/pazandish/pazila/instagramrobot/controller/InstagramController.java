package com.pazandish.pazila.instagramrobot.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/instagram")
public class InstagramController {

    @Autowired
    private WebDriver driver;

    @GetMapping("/comment/{postId}")
    @ResponseBody
    public List<String> getComment(@PathVariable String postId) {
        return getComments(postId);
    }

    @GetMapping("/follow/{profileId}")
    @ResponseBody
    public Map<String, Boolean> getFollower( @PathVariable String profileId) {
        return getFollowers(profileId);
    }

    Map<String, Boolean> getLikes(String postId) {
        Map<String, Boolean> likesMap = new HashMap<>();
        driver.get("https://www.instagram.com/p/" + postId + "/");
        try {
            Thread.sleep(5000);
            WebElement followersBtn = driver.findElement(By.xpath("//*[text()=' likes']"));
            followersBtn.click();
            Thread.sleep(500);
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                String documentHeight = "document.getElementsByClassName(\"Igw0E IwRSH eGOV_ vwCYk i0EQd\")[0].firstChild.scrollHeight";
                long lastHeight = (long) js.executeScript("return " + documentHeight);

                while (true) {
                    js.executeScript("document.getElementsByClassName(\"Igw0E IwRSH eGOV_ vwCYk i0EQd\")[0].firstChild.scrollTo(0, " + documentHeight + ")");
                    Thread.sleep(500);

                    long newHeight = (long) js.executeScript("return " + documentHeight);
                    if (newHeight == lastHeight) {
                        break;
                    }
                    lastHeight = newHeight;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Elements listItems = Jsoup.parse(driver.findElement(By.className("_1XyCr")).getAttribute("innerHTML")).child(0).child(1).child(1).child(0).child(0).select("div");
            for (Element element : listItems) {
                String userName = Jsoup.parse(element.html()).select("a.FPmhX.notranslate._0imsa").text();
                String followStatusTxt = Jsoup.parse(element.html()).select("button.sqdOP.L3NKy._8A5w5").text();
                boolean followStatus = false;
                if (followStatusTxt.toLowerCase(Locale.ROOT).equals("following"))
                    followStatus = true;
                likesMap.put(userName, followStatus);
            }
        } catch (Exception e) {

        }
        return likesMap;
    }
     List<String> getComments(String postId) {
        List<String> commentUser = new ArrayList<>();
        driver.get("https://www.instagram.com/p/" + postId + "/");
        try {
            Thread.sleep(5000);
            while(true) {
                try {
                    WebElement loadMoreCommentBtn = driver.findElement(By.cssSelector("span[class='glyphsSpriteCircle_add__outline__24__grey_9 u-__7']"));
                    loadMoreCommentBtn.click();
                    Thread.sleep(1000);
                }catch (Exception e){
                    break;
                }
            }
            Thread.sleep(500);

            WebElement followersElement = driver.findElement(By.className("XQXOT"));
            Document doc = Jsoup.parse(followersElement.getAttribute("innerHTML"));
            Elements listItems = doc.select("ul");
            for (Element element : listItems) {
                String userName = Jsoup.parse(element.html()).select("a.sqdOP.yWX7d._8A5w5.ZIAjV ").text();
                commentUser.add(userName);
            }

        } catch (Exception e) {

        }
        return commentUser;
    }


    Map<String, Boolean> getFollowers(String userAccount) {
        Map<String, Boolean> followersMap = new HashMap<>();
        driver.get("https://www.instagram.com/" + userAccount + "/");
        try {
            Thread.sleep(5000);
            WebElement followersBtn = driver.findElement(By.xpath("//*[text()=' followers']"));
            followersBtn.click();
            Thread.sleep(1000);
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                String documentHeight = "document.getElementsByClassName(\"isgrP\")[0].scrollHeight";
                long lastHeight = (long) js.executeScript("return " + documentHeight);

                while (true) {
                    js.executeScript("document.getElementsByClassName(\"isgrP\")[0].scrollTo(0, " + documentHeight + ")");
                    Thread.sleep(1500);

                    long newHeight = (long) js.executeScript("return " + documentHeight);
                    if (newHeight == lastHeight) {
                        break;
                    }
                    lastHeight = newHeight;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            WebElement followersElement = driver.findElement(By.className("PZuss"));
            Document doc = Jsoup.parse(followersElement.getAttribute("innerHTML"));
            Elements listItems = doc.select("li");
            for (Element element : listItems) {
                String userName = Jsoup.parse(element.html()).select("a.FPmhX.notranslate._0imsa").text();
                String followStatusTxt = Jsoup.parse(element.html()).select("button.sqdOP.L3NKy._8A5w5").text();
                boolean followStatus = false;
                if (followStatusTxt.toLowerCase(Locale.ROOT).equals("following"))
                    followStatus = true;
                followersMap.put(userName, followStatus);
            }
        } catch (Exception e) {

        }
        return followersMap;
    }

}