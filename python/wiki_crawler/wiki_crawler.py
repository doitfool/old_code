# coding:utf8
import urllib
import urllib2
import os
import time
import sys
import threadpool
from BeautifulSoup import BeautifulSoup
reload(sys)
sys.setdefaultencoding('utf-8')

# 获取网页全文
def get_page(url):
    try:
        # user_agent = 'Mozilla/4.0(compatible; MSIE 5.5; Windows NT)'
        # headers = {'User-Agent': user_agent}
        # values = {'username': 'XXX', 'password': 'XXX'}  #登录信息
        # data = urllib.urlencode(values)
        # request = urllib2.Request(url,data,headers)
        # response = urllib2.urlopen(request)
        response = urllib2.urlopen(url)
    except urllib2.HTTPError as e:
        print "The server couldn't fulfill the request"
        print "Error code:",e.code
        print "Return content:", e.read()
    except urllib2.URLError as e:
        print "Failed to reach the server"
        print "The reason:", e.reason
    # 重新启动爬虫
    #     time.sleep(200)
        get_page(url)
    # print response.info()
    page = response.read()  #read函数返回类型是str
    return page

# 从获取的page中抽取所需内容
def get_content(page):
    category_links, page_links = [], []
    soup = BeautifulSoup(page, fromEncoding='utf-8')
    title = str(soup.title.contents[0])
    _title = title[title.find(':')+1:title.find(' ')]
    mw_subcategories = soup.find('div',{'id':'mw-subcategories'})
    mw_pages = soup.find('div',{'id':'mw-pages'})
    if mw_subcategories:
        category_links = ['https://zh.wikipedia.org'+one['href'] for one in soup.findAll('a',{'class':'CategoryTreeLabel  CategoryTreeLabelNs14 CategoryTreeLabelCategory'})]
    if mw_pages:
        div_tag = soup.findAll('div', {'class':'mw-content-ltr'})[-1]
        page_links = ['https://zh.wikipedia.org'+one['href'] for one in BeautifulSoup(str(div_tag)).findAll('a')]
    return _title, category_links, page_links

def save2html(page, file_name):
    fw = open(root_path+'\\'+file_name, 'w')
    fw.write(page)
    fw.close()

# def save2html(page, dir_path, file_name):
#     if not os.path.exists(dir_path):
#         os.mkdir(dir_path)
#     fw = open(dir_path+'\\'+file_name, 'w')
#     fw.write(page)
#     fw.close()

# 线程池threadpool的使用
# pool = threadpool.ThreadPool(20)
# requests = threadpool.makeRequests(search, url_queue, timeCost)
# [pool.putRequest(req) for req in requests]
# pool.wait()

def string_process(s):
    t = '/\\:*?"<>|'
    ps = ''
    for i in xrange(len(s)):
        if s[i] in t:
            ps += ''
        else:
            ps += s[i]
    return unicode(ps)

def search(url):
    global url_queue
    page = get_page(url)
    title, category_links, page_links = get_content(page)

    print url, urllib.unquote(str(url)).encode('gbk', 'ignore'), string_process(title).encode('gbk', 'ignore')+'.html'

    save2html(page, string_process(title).encode('gbk', 'ignore')+'.html')
    url_queue.remove(url)
    url_queue += category_links+page_links

def timeCost(request, n):
    print "Elapsed time: %s" % (time.time()-start)

if __name__ == '__main__':
    root_path = 'C:\Users\AC\Desktop\wiki crawler\pages'  # 设置网页存放的当前目录，初始值为起始根目录
    url = u'https://zh.wikipedia.org/wiki/Category:%E4%BF%A1%E6%81%AF%E5%AE%89%E5%85%A8'   # 爬虫起始网页
    url_queue = []   # 只存储category子分类中的链接，即category_links
    url_queue += [url]
    start = time.time()
    while len(url_queue):
        print 'len:', len(url_queue)
        pool = threadpool.ThreadPool(200)
        requests = threadpool.makeRequests(search, url_queue, timeCost)
        [pool.putRequest(req) for req in requests]
        pool.wait()


