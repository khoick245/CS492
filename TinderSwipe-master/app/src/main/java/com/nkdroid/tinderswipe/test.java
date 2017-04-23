package com.nkdroid.tinderswipe;

/**
 * Created by khoinguyen on 4/23/17.
 */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class test {

    public static void main(String[] args) throws Exception {
        WebDriver driver=new FirefoxDriver();
        driver.get("http://qaautomated.blogspot.in");
        Thread.sleep(3000);
        driver.quit();
    }
}
