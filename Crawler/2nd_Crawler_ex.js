var webdriver = require('selenium-webdriver');
var By = require('selenium-webdriver').By;
var cheerio = require('cheerio');
var request = require('request');

var driver = new webdriver.Builder()
    .withCapabilities(webdriver.Capabilities.chrome())
    .build();

var url = "https://store.naver.com/restaurants/list?entry=pll&filterId=r09680&query=%EA%B0%95%EB%82%A8%EA%B5%AC%20%EB%A7%9B%EC%A7%91&sessionid=8ke9PmGWNiobQ91TiwzyWA%3D%3D"
driver.get(url);

var resName = new Array();
var resInfo = new Array();
var endOfPage;

console.log("start");

driver.findElements(By.xpath(
    '//*[@id="container"]/div[2]/div[1]/div/div[2]/ul/li')).then(function(resSet){
        resName=[]; 
        console.log(resName);
        for(var i=0; i < resSet.length; i++){
            resSet[i].findElement(By.xpath('div/div/div[1]/span/a')).then(function(resSet){
                resSet.getAttribute('title').then(function(title){
                    console.log('음식점 이름 :', title)
                    resName.push(title);
                })
            })
        }
    })

