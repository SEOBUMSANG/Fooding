from selenium import webdriver
from bs4 import BeautifulSoup
import requests
import time


print("start")

resName = []
resLocation = []

#페이지 번호 클릭(1->2 5->6)
def pageRequest():
    driver = webdriver.Chrome()
    url = 'https://store.naver.com/restaurants/list?filterId=r09680&page=1&query=%EA%B0%95%EB%82%A8%EA%B5%AC%20%EB%A7%9B%EC%A7%91&sessionid=z925KfI1B9YGCLi6T8WT9A%3D%3D'
    driver.get(url)

    for pageIndex in range(2, 7):
        driver.find_element_by_xpath('//*[@id="container"]/div[2]/div[1]/div/div[2]/div/div[2]/a['+str(pageIndex)+']').click()
        print("pageRequest1")
        crawlingPage(driver)
        print("pageRequest2")

#음식점 페이지 들어가기
def crawlingPage(driver):
    for resIndex in range(1,21):
        resLink = '/html/body/div/div/div[2]/div[1]/div/div[2]/ul/li['+str(resIndex)+']/div/div/div[1]/span/a'
        resUrl = driver.find_element_by_xpath(resLink).get_attribute('href')
        crawlingRestaurant(resUrl)

#음식점 페이지 들어가서 크롤링하기
#임시 드라이버(resDriver) 사용
def crawlingRestaurant(resUrl):
    resDriver = webdriver.Chrome()
    resDriver.get(resUrl)
    resName.append(resDriver.find_element_by_xpath('//*[@id="content"]/div[1]/div[1]/strong').text)
    tempLocation = resDriver.find_element_by_xpath('//*[@id="content"]/div[2]/*/div/div[2]/div/ul/li[1]/span').text
    r = requests.get('http://api.vworld.kr/new2coord.do?q='+tempLocation+'&a']
')
    resLocation.append(resDriver.find_element_by_xpath('//*[@id="content"]/div[2]/*/div/div[2]/div/ul/li[1]/span').text)



    print(resDriver.find_element_by_xpath('//*[@id="content"]/div[1]/div[1]/strong').text)
    print(resDriver.find_element_by_xpath('//*[@id="content"]/div[2]/*/div/div[2]/div/ul/li[1]/span').text)
    resDriver.close()


pageRequest()


for i in range(len(resName)):
    print(resName[i])
    print(resLocation[i])
