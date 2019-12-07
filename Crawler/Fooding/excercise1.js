const axios = require("axios");
const cheerio = require("cheerio");
const log = console.log;

console.log('hello');

const getHtml = async () => {
  try {
    return await axios.get("https://store.naver.com/restaurants/list?entry=pll&filterId=r09680&query=강남구%20맛집&sessionid=5fNyMAquaU5mi4J6hvPFnQ%3D%3D");
  } catch (error) {
    console.error(error);
  }
};

getHtml()
  .then(html => {
    let ulList = [];
    const $ = cheerio.load(html.data);
    const $bodyList = $("div.list_area ul.list_place_col1").children("li.list_item.type_restaurant");



    $bodyList.each(function(i, elem) {
      ulList[i] = {
          title: $(this).find('div.list_item_inner a').text(),
          url: $(this).find('strong.news-tl a').attr('href'),
          image_url: $(this).find('p.poto a img').attr('src'),
          image_alt: $(this).find('p.poto a img').attr('alt'),
          summary: $(this).find('p.lead').text().slice(0, -11),
          date: $(this).find('span.p-time').text()
      };
    });
    
    console.log(ulList);
    const data = ulList.filter(n => n.title);
    return data;
  })
  .then(res => log(res));