package com.build.qa.build.selenium.pageobjects.homepage;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.build.qa.build.selenium.pageobjects.BasePage;

public class HomePage extends BasePage {
	
	private By buildThemeBody;
	
	public HomePage(WebDriver driver, Wait<WebDriver> wait) {
		super(driver, wait);
		buildThemeBody = By.cssSelector("body.build-theme");
	}
	
	public boolean onBuildTheme() { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(buildThemeBody)) != null;
	}
	
	public boolean retryingFindClick(By by)
	{
        boolean result = false;
        
		for(int i=0; i<2; i++)
		{
			try {
				wait.until(ExpectedConditions.elementToBeClickable(by));
				driver.findElement(by).click();
				result = true;
				break;
			} catch(StaleElementReferenceException e) {
				driver.navigate().refresh();
			}
		}
        return result;
	}
	
	public boolean handleSplashModal()
	{
		boolean result = false;
		try {
			By newsplash = By.cssSelector("#email-subscribe-splash");
			WebDriverWait explicitwait = new WebDriverWait(driver, 3);
			explicitwait.until(ExpectedConditions.visibilityOfElementLocated(newsplash));
			driver.findElement(By.xpath("//*[@id='email-subscribe-splash']/div/div/div[1]/button")).click();
			result = true;
		} catch(TimeoutException e) {

		}
		
		return result;
	}
	
}
