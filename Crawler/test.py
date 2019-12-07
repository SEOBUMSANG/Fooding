from selenium import webdriver
from bs4 import BeautifulSoup
import requests
import time


print('start')
def test():
    driver = webdriver.Chrome()
    url = 'https://store.naver.com/restaurants/detail?entry=pll&id=35623097&query=%EB%9E%A8%EB%B8%8C%EB%9D%BC%ED%8A%BC'
    driver.get(url)

    resImageURL = driver.find_element_by_xpath('//*[@id="container"]/div[1]/div/div/div[3]/div/div[3]/div/a/img').get_attribute('src')
    print('Iamge url : '+resImageURL)

test()