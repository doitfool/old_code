# coding:utf8

import urllib2
import httplib
from BeautifulSoup import BeautifulSoup
import sys
reload(sys)
sys.setdefaultencoding('utf-8')


# 解析代理网站获得多个代理IP，建立IP池
def proxy_servers():
    try:
        response = urllib2.urlopen('http://proxy-list.org/chinese/index.php?setlang=chinese')
    except urllib2.HTTPError as e:
        print "The server couldn't fulfill the request"
        print "Error code:", e.code
        print "Return content:", e.read()
    except urllib2.URLError as e:
        print "Failed to reach the server"
        print "The reason:", e.reason
    page = response.read()
    soup = BeautifulSoup(page, fromEncoding='utf-8')
    proxys = [proxy.contents for proxy in soup.findAll('li', {'class': 'proxy'})]
    return proxys[1:]

# 获取网页全文
def get_page(url):
    print 'get_page('+url+') start',
    # page = ''
    # proxy = random.choice(proxy_servers())
    # proxy = random.choice(proxy_servers())
    # 131.255.172.169:8080  123.56.124.91:3128 190.0.43.186:8080
    # proxy = '222.34.3.9:8080'  159.203.122.165:8080  125.212.225.86:3128 220.170.198.207:3128, 117.167.67.194:8123
    # proxy = '138.36.27.2:3128'
    # print 'proxy:', proxy,
    try:
        # 一无所有
        # page = urllib2.urlopen(url).read()
        # 代理
        # proxy_support = urllib2.ProxyHandler({'http': proxy})
        # opener = urllib2.build_opener(proxy_support, urllib2.HTTPHandler)
        # urllib2.install_opener(opener)
        # page = urllib2.urlopen(url).read().decode('utf-8')
        # 浏览器header
        user_agent = 'Mozilla/4.0(compatible; MSIE 5.5; Windows NT)'
        headers = {'User-Agent': user_agent}
        request = urllib2.Request(url, headers=headers)
        response = urllib2.urlopen(request)
        page = response.read().decode('utf-8')
    except urllib2.HTTPError as e:
        print "The server couldn't fulfill the request"
        print "Error code:", e.code
        print "Return content:", e.read()
    except urllib2.URLError as e:
        print "Failed to reach the server"
        print "The reason:", e.reason
    except httplib.IncompleteRead as e:
        page = e.partial
    else:
        pass
    print '---> finish', len(page)
    return page

# 解析网页（从获取的page中抽取所需内容）
def get_content(page):
    print '\tget_content() start',
    soup = BeautifulSoup(page, fromEncoding='utf-8')
    question_links = soup.findAll('a', {'class': 'question_link'})
    question_titles = [question_link.text for question_link in question_links]
    print '---> finish'
    return question_titles

'''
    爬取知乎话题广场http://www.zhihu.com/topics下多个话题里的精华问题（每个话题选前20页）
'''
if __name__ == '__main__':
    urls_all = []
    with open('resource\link.txt', 'r') as fr:
        urls = fr.readlines()
        for url in urls:
            for i in xrange(1, 21):
                # print i, url.strip()+'?page='+str(i)
                urls_all.append(url.strip()+'?page='+str(i))
    fw = open('resource\zhihu_questions.txt', 'a')
    for url in urls_all:
        page = get_page(url)
        question_titles = get_content(page)
        for question_title in question_titles:
            fw.write(question_title+'\n')
    fw.close()



