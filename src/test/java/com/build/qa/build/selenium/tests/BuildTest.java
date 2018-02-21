package com.build.qa.build.selenium.tests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.build.qa.build.selenium.framework.BaseFramework;
import com.build.qa.build.selenium.pageobjects.homepage.HomePage;

public class BuildTest extends BaseFramework {

	/**
	 * Extremely basic test that outlines some basic functionality and page objects
	 * as well as assertJ
	 */
	@Test
	public void navigateToHomePage() {
		driver.get(getConfiguration("HOMEPAGE"));
		HomePage homePage = new HomePage(driver, wait);

		softly.assertThat(homePage.onBuildTheme())
		.as("The website should load up with the Build.com desktop theme.")
		.isTrue();
	}
	
	/**
	 * Search for the Quoizel MY1613 from the search bar
	 * 
	 * @assert: That the product page we land on is what is expected by checking the
	 *          product title
	 * @difficulty Easy
	 */
	@Test
	public void searchForProductLandsOnCorrectProduct() {
		boolean modalview = false;
		HomePage homePage = new HomePage(driver, wait);
		
		driver.get(getConfiguration("HOMEPAGE"));
		modalview = homePage.handleSplashModal();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_txt")));
		
		driver.findElement(By.id("search_txt")).sendKeys("Quoizel MY1613");
		
		driver.findElement(By.className("search-site-search")).click();
		
		if(!modalview) {			
			modalview = homePage.handleSplashModal();
		}
		
		softly.assertThat(driver.findElement(By.cssSelector("h2[class*='js-sub-heading']")).getText())
		.isEqualTo("Malaga Monterey Mosaic 3 Light 16\" Wide Flush Mount Ceiling Fixture with Pen Shell Mosaic Shade");		
	}

	/**
	 * Go to the Bathroom Sinks category directly
	 * (https://www.build.com/bathroom-sinks/c108504) and add the second product on
	 * the search results (Category Drop) page to the cart.
	 * 
	 * @assert: the product that is added to the cart is what is expected
	 * @difficulty Easy-Medium
	 */
	@Test
	public void addProductToCartFromCategoryDrop() {	
		boolean modalview = false;
		String itemName = "";
		HomePage homePage = new HomePage(driver, wait);
		driver.get("https://www.build.com/bathroom-sinks/c108504");
		
		modalview = homePage.handleSplashModal();
		
		By installationType = By.cssSelector("a[href*='ts1508517364_c108504_fi19797_fvdrop in']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(installationType));
		driver.findElement(installationType).click();
		
		By secondItem = By.xpath("//li[@id='product-composite-561522']/div[2]/a/div[1]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(secondItem));
		driver.findElement(secondItem).click();
		
		if(!modalview) {			
			modalview = homePage.handleSplashModal();
		}
		
		By addToCart = By.xpath("//*[@id=\"configure-product-wrap\"]/button");
		
		if(homePage.retryingFindClick(addToCart))
		{
			By itemTitle = By.xpath("//*[@id=\"recommended-options\"]/div[1]/div/div[3]/a/p");
	
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(itemTitle));
			} catch(TimeoutException e) {
				homePage.retryingFindClick(addToCart);
			}
			
			itemName = driver.findElement(itemTitle).getText();
		}
		
		softly.assertThat(itemName)
				.isEqualTo("Kohler K-2241-8 Memoirs Classic 17\" Drop In Bathroom Sink with 3 Holes Drilled and Overflow");
	}

	/**
	 * Add a product to the cart and email the cart to yourself, also to my email
	 * address: jgilmore+SeleniumTest@build.com Include this message in the "message
	 * field" of the email form: "This is {yourName}, sending you a cart from my
	 * automation!"
	 * 
	 * @assert that the "Cart Sent" success message is displayed after emailing the
	 *         cart
	 * @difficulty Medium-Hard
	 */
	@Test
	public void addProductToCartAndEmailIt() {
		boolean modalview = false;
		String msg = "";
		HomePage homePage = new HomePage(driver, wait);
		driver.get("https://www.build.com/bathroom-sinks/c108504");
		
		modalview = homePage.handleSplashModal();
		
		By installationType = By.cssSelector("a[href*='ts1508517364_c108504_fi19797_fvdrop in']");
		wait.until(ExpectedConditions.visibilityOfElementLocated(installationType));
		driver.findElement(installationType).click();
		
		By secondItem = By.xpath("//li[@id='product-composite-561522']/div[2]/a/div[1]");
		wait.until(ExpectedConditions.visibilityOfElementLocated(secondItem));
		driver.findElement(secondItem).click();
		
		if(!modalview) {
			
			modalview = homePage.handleSplashModal();
		}
		
		By addToCart = By.xpath("//*[@id=\"configure-product-wrap\"]/button");
		
		if(homePage.retryingFindClick(addToCart))
		{
			By itemTitle = By.xpath("//*[@id=\"recommended-options\"]/div[1]/div/div[3]/a/p");
	
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(itemTitle));
			} catch(TimeoutException e) {
				homePage.retryingFindClick(addToCart);
			}
			driver.findElement(By.cssSelector("button[href='/index.cfm?page=cart:cart']")).click();
		}
		
		By emailButton = By.cssSelector("button[class*='js-email-cart-button']");
		
		if(homePage.retryingFindClick(emailButton))
		{
			By emailCartModal = By.cssSelector("#cart-email");
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(emailCartModal));
			} catch(TimeoutException e) {
				homePage.retryingFindClick(emailButton);
			}
			if(driver.findElement(emailCartModal).isDisplayed()) {
				
				driver.findElement(By.cssSelector("#yourName")).sendKeys("Pinak Das");
				driver.findElement(By.cssSelector("#yourEmail")).sendKeys("pinakdas163@gmail.com");
				driver.findElement(By.cssSelector("#recipientName")).sendKeys("Pinak Das");
				driver.findElement(By.cssSelector("#recipientEmail")).sendKeys("pinakdas163@gmail.com, pinak.spidey@gmail.com");
				driver.findElement(By.cssSelector("#quoteMessage"))
				.sendKeys("This is Pinak Das sending you a cart from my automation!");
	
				driver.findElement(By.cssSelector("button[class*='js-email-cart-submit-button']")).click();
	
				By msgnotification = By.xpath("//div[contains(@class, 'js-notifications')]/ul/li");
				wait.until(ExpectedConditions.visibilityOfElementLocated(msgnotification));
				
				msg = driver.findElement(msgnotification).getText();
			}
		}
		
		softly.assertThat(msg).
		isEqualTo("Cart Sent! The cart has been submitted to the recipient via email.");
	}
	
	/**
	 * Go to a category drop page (such as Bathroom Faucets) and narrow by at least
	 * two filters (facets), e.g: Finish=Chromes and Theme=Modern
	 * @throws Exception 
	 * 
	 * @assert that the correct filters are being narrowed, and the result count is
	 *         correct, such that each facet selection is narrowing the product
	 *         count.
	 * @difficulty Hard
	 */
	@Test
	public void facetNarrowBysResultInCorrectProductCounts() {
		boolean modalview = false;
		HomePage homePage = new HomePage(driver, wait);
		
		driver.get(getConfiguration("HOMEPAGE"));
		
		modalview = homePage.handleSplashModal();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@data-category-id='108412']/a[@data-category-id='108412']")));
		driver.findElement(By.xpath("//li[@data-category-id='108412']/a[@data-category-id='108412']")).click();
				
		if(!modalview) {
			
			modalview = homePage.handleSplashModal();
		}
		By bathroomCategory = By.cssSelector("a[href*='/bathroom-sink-faucets/c108503?intcmp=category-108503']");
		wait.until(ExpectedConditions.elementToBeClickable(bathroomCategory));
		driver.findElement(bathroomCategory).click();
		
		By results = By.className("js-num-results");
		wait.until(ExpectedConditions.visibilityOfElementLocated(results));
		int totalProdResult = Integer.parseInt(driver.findElement(results).getText().replace(",", ""));
		
		By firstFilter = By.xpath("//label[@data-facet-value='Chromes']/input");
		
		if(homePage.retryingFindClick(firstFilter))
		{
			By filteronetag = By.xpath("//span[text()='Colors: ']/following-sibling::span[1]");
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(filteronetag));
			} catch(TimeoutException e) {
				homePage.retryingFindClick(firstFilter);
			}
			String filteronename = driver.findElement(filteronetag).getText().replaceFirst(" x", "");
			softly.assertThat(filteronename).isEqualTo("Chromes");
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(results));
		int chromeResult = Integer.parseInt(driver.findElement(results).getText().replace(",", ""));
		
		softly.assertThat(totalProdResult).isGreaterThanOrEqualTo(chromeResult);	

		By secondFilter = By.xpath("//label[@data-facet-value='Modern']/input");
		
		if(homePage.retryingFindClick(secondFilter))
		{
			By filtertwotag = By.xpath("//span[text()='Theme: ']/following-sibling::span[1]");
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(filtertwotag));
			} catch(TimeoutException e) {
				homePage.retryingFindClick(secondFilter);
			}
			String filtertwoname = driver.findElement(filtertwotag).getText().replaceFirst(" x", "");
			softly.assertThat(filtertwoname).isEqualTo("Modern");
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(results));
		int modernResult = Integer.parseInt(driver.findElement(results).getText().replace(",", ""));
		
		softly.assertThat(totalProdResult).isGreaterThanOrEqualTo(modernResult);
		softly.assertThat(chromeResult).isGreaterThanOrEqualTo(modernResult);
	}
}
