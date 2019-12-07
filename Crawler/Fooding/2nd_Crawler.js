var webdriver = require('selenium-webdriver');
var By = require('selenium-webdriver');
var cheerio = require('cheerio');
var request = require('request');

var driver = new webdriver.Builder()
    .withCapabilities(webdriver.Capabilities.chrome())
    .build();

var url = "http://www.google.com"
driver.get(url);
driver.quit();