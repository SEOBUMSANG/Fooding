from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from bs4 import BeautifulSoup
import requests
import time

#Firebase
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
cred = credentials.Certificate("fooding-bb036-firebase-adminsdk-cgso1-47bbee9fdc.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

resName = []
resDescription = []
resLocationX = []
resLocationY = []
resImageURL = []
resAddress = []


print("start")

#페이지 번호 클릭(1->2 5->6)
def pageRequest():
    driver = webdriver.Chrome()
    url = 'https://store.naver.com/restaurants/list?filterId=r09110&page=1&query=%EC%A2%85%EB%A1%9C%EA%B5%AC%20%EB%A7%9B%EC%A7%91&sessionid=6CURWGdFQbla0OyA7L7kYg%3D%3D'
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

    #식당 이름
    try:
        resName.append(resDriver.find_element_by_xpath('//*[@id="content"]/div[1]/div[1]/strong').text)
    except NoSuchElementException:
        resName.append('No such restaurent Name')
    
    #식당 정보
    try:
        resDescription.append(resDriver.find_element_by_xpath('//*[@id="content"]/div[1]/div[1]/div/div/span').text)
    except NoSuchElementException:
        resDescription.append('No such description')

    #식당 이미지 URL
    tempResImageURL = []
    for i in range(3, 7):
        try:
            tempResImageURL.append(resDriver.find_element_by_xpath('//*[@id="container"]/div[1]/div/div/div[3]/div/div['+str(i)+']/div/a/img').get_attribute('src'))

        except NoSuchElementException:
            tempResImageURL.append('No such Image URL')
    resImageURL.append(tempResImageURL)

    #식당 위치
    try:
        tempLocation = resDriver.find_element_by_xpath('//*[@id="content"]/div[2]/*/div/div[2]/div/ul/li[1]/span').text
        resAddress.append(tempLocation.split(' ', maxsplit = 2))
        #지오코더 api
        r = requests.get('http://api.vworld.kr/req/address?service=address&request=getCoord&key=E3F9CD97-08F2-38F3-B906-A245EE11181B&simple=true&address='
        +tempLocation+'&type=ROAD')
        tempLocation = r.text
        #받은 정보 정제
        tempLocationX = tempLocation[tempLocation.find('"x" : ')+7 : tempLocation.find('"x" : ')+19]
        tempLocationY = tempLocation[tempLocation.find('"y" : ')+7 : tempLocation.find('"y" : ')+19]
        tempLocationX = tempLocationX.split('"')
        tempLocationY = tempLocationY.split('"')
        resLocationX.append(tempLocationX[0])
        resLocationY.append(tempLocationY[0])
    except NoSuchElementException:
        resAddress.append(['No', 'such', 'Address'])
        resLocationX.append('No such lng')
        resLocationY.append('No such lat')
    '''
    print(resDriver.find_element_by_xpath('//*[@id="content"]/div[1]/div[1]/strong').text)
    print(resDriver.find_element_by_xpath('//*[@id="content"]/div[2]/*/div/div[2]/div/ul/li[1]/span').text)
    '''
    resDriver.close()



def putFiresBase():

    for i in range(0, 100):
        data = {
        u'name': resName[i],
        u'description' : resDescription[i],
        u'lng': resLocationX[i],
        u'lat': resLocationY[i],
        u'resImageURL' : resImageURL[i],
        u'resAddress' : resAddress[i]
        }
        if i < 10 :
            dataIndex = 'data_0'+str(i)
        else :
            dataIndex = 'data_'+str(i)
        db.collection(u'jongro').document(dataIndex).set(data)


def main():
    pageRequest()
    putFiresBase()
    
    #for i in range(len(resName)):
     #   print("음식점 이름 : "+resName[i])
    #    print("음식점 설명 : "+resDescription[i])
     #   print("음식점 X 좌표 : "+resLocationX[i])
     #   print("음식점 Y 좌표 : "+resLocationY[i])
     #   for j in range(0,4):
      #      print(i+'번째 음식점 이미지 URL : '+resImageURL[i][j])
      #  for j in range(0,3):
       #     print(i+'번째 음식점 주소 : '+resAddress[i][j])


main()


