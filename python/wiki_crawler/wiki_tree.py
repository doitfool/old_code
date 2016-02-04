# coding:utf8
import urllib
import urllib2
import httplib
import os
import random
import time
import sys
import xlwt
from BeautifulSoup import BeautifulSoup
from collections import defaultdict
reload(sys)
sys.setdefaultencoding('utf-8')

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
    print '\tget_page() start',
    page = ''
    # proxy = random.choice(proxy_servers())
    # proxys = ['222.34.3.9:8080', '131.255.172.169:8080', '123.56.124.91:3128', '190.0.43.186:8080',
    # '159.203.122.165:8080', '125.212.225.86:3128', '220.170.198.207:3128', '117.167.67.194:8123']
    proxys = ['117.147.220.230:8123', '193.0.152.59:3128', '151.80.135.147:3128', '222.34.3.12:8080',
              '118.123.113.4:80', '165.225.174.23:3128', '207.250.100.200:8080', '119.52.63.170:3128',
              '60.191.190.174:3128', '159.203.1.226:8080', '24.157.37.61:8080', '87.236.214.93:3128', '222.39.64.13:8118',
              '106.185.49.114:3128','181.188.194.180:80','74.208.184.125:3128','60.191.179.53:3128','117.169.203.253:8123',
              '117.169.203.253:8123','202.29.97.2:3128','118.123.113.4:80','118.123.113.4:80','118.123.113.4:80',
              '60.191.158.44:3128','120.206.223.11:8123','146.185.153.200:3128','146.185.153.200:3128',
              '60.191.165.28:3128','39.166.146.140:8123','39.166.146.140:8123','222.34.3.9:8080','222.34.3.9:8080',
              '190.0.43.186:8080','125.212.225.86:3128','220.170.198.207:3128','117.167.67.194:8123','220.170.198.207:3128',
              '117.147.220.230:8123','117.147.220.230:8123','117.147.220.230:8123','193.0.152.59:3128','193.0.152.59:3128',
              '220.170.198.207:3128','220.170.198.207:3128','220.181.143.14:9999','37.187.152.193:3128','37.187.152.193:3128',
              '77.93.202.118:80','220.181.143.14:9999','220.170.198.207:3128','77.93.202.118:80','77.93.202.118:80',
              '39.167.233.106:8123','39.167.233.106:8123','81.130.252.228:3128','220.170.198.207:3128','39.167.233.106:8123']
    proxy = random.choice(proxys)
    print 'proxy:', proxy,
    try:
        # 一无所有
        page = urllib2.urlopen(url).read()
        # 代理
        proxy_support = urllib2.ProxyHandler({'http': proxy})
        opener = urllib2.build_opener(proxy_support, urllib2.HTTPHandler)
        urllib2.install_opener(opener)
        page = urllib2.urlopen(url).read().decode('utf-8')
        # 浏览器header
        # user_agent = 'Mozilla/4.0(compatible; MSIE 5.5; Windows NT)'
        # headers = {'User-Agent': user_agent}
        # request = urllib2.Request(url, headers=headers)
        # response = urllib2.urlopen(request)
        # page = response.read().decode('utf-8')
    except urllib2.HTTPError as e:
        print "The server couldn't fulfill the request"
        print "Error code:",e.code
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

# 从获取的page中抽取所需内容
def get_content(page):
    print '\tget_content() start',
    category_links, page_links = [], []
    soup = BeautifulSoup(page, fromEncoding='utf-8')
    mw_subcategories = soup.find('div', {'id': 'mw-subcategories'})
    mw_pages = soup.find('div', {'id': 'mw-pages'})
    if mw_subcategories:
        category_links = ['https://zh.wikipedia.org'+one['href'] for one in soup.findAll('a', {'class': 'CategoryTreeLabel  CategoryTreeLabelNs14 CategoryTreeLabelCategory'})]
    if mw_pages:
        div_tag = soup.findAll('div', {'class': 'mw-content-ltr'})[-1]
        page_links = ['https://zh.wikipedia.org'+one['href'] for one in BeautifulSoup(str(div_tag)).findAll('a')]
    print '---> finish'
    return category_links, page_links

class node():
    _title, _url = '', ''
    def __init__(self, url):
        self._url = url.encode('utf-8')
        if url.find('Category') != -1:
            self._title = url[(url.rfind(':')+1):]
        else:
            self._title = url[(url.rfind('/')+1):]

# 一行python表示树
def tree():
    return defaultdict(tree)

def is_leaf(url):
    if url.find('Category') != -1:
        return False
    else:
        return True

# global url_visited
def visited(url):
    if url in urls_visited:
        return True
    else:
        return False

# build the tree
# def search(root, depth):
#     global my_tree
#     url = root._url
#     print time.strftime('%H:%M:%S', time.localtime(time.time())), 'depth='+str(depth), url
#     if not is_leaf(url):
#         print 'branch'
#         c_links, p_links = get_content(get_page(url))
#         # for link in c_links+p_links:
#         #     children.append(node(link))
#         my_tree[root] = []
#         for c_link in c_links:
#             c_child = node(c_link)
#             my_tree[root] += [c_child]
#             search(c_child, depth+1)
#         print 'leaf'
#         for p_link in p_links:
#             p_child = node(p_link)
#             my_tree[root] += [p_child]
#             print '\tp_link', p_link
#     return root

# 暴力URL去重法
def search(root, depth):
    global my_tree
    global urls_visited
    url = root._url
    print time.strftime('%H:%M:%S', time.localtime(time.time())), 'depth='+str(depth), url
    if url not in urls_visited:
        urls_visited.append(url)
        if not is_leaf(url):
            print 'branch'
            c_links, p_links = get_content(get_page(url))
            # for link in c_links+p_links:
            #     children.append(node(link))
            my_tree[root] = []
            for c_link in c_links:
                c_child = node(c_link)
                my_tree[root] += [c_child]
                search(c_child, depth+1)
            print 'leaf'
            for p_link in p_links:
                p_child = node(p_link)
                my_tree[root] += [p_child]
                print '\tp_link', p_link
    else:
        print 'visited'
    return root


def print_tree(my_tree, root):
    print urllib.unquote(root._title.encode('utf-8')).decode('utf-8'), ' ---> [',
    for child in my_tree[root]:
        print urllib.unquote(child._title.encode('utf-8')).decode('utf-8'), ',',
    print ']'
    for child in my_tree[root]:
        print_tree(my_tree, child)


def save(root, column):
    # 深度遍历层次树，顺便写入Excel, row和column表示写入excel表格的行列坐标
    global row, my_tree
    url = urllib.unquote(root._url.encode('utf-8', 'ignore')).decode('utf-8', 'ignore')
    if url.find('Category') != -1:
        title = url[(url.rfind(':')+1):]
    else:
        title = url[(url.rfind('/')+1):]
    sheet.write(row, column, xlwt.Formula("HYPERLINK"+'("'+url+'";"'+title+'")'))
    for child in my_tree[root]:
        save(child, column+1)
    row += 1


if __name__ == '__main__':
    depth = 1
    # url = u'https://zh.wikipedia.org/wiki/Category:%E4%BF%A1%E6%81%AF%E5%AE%89%E5%85%A8'   # 爬虫起始网页 信息安全
    url = u'https://zh.wikipedia.org/wiki/Category:%E9%9A%B1%E7%A7%81%E6%AC%8A'  # 隐私权
    my_tree = tree()
    start = time.time()
    urls_visited = [u'https://zh.wikipedia.org/wiki/Category:%E8%AD%98%E5%88%A5']
    root = search(node(url), depth)
    print 'Build tree cost time:', time.time()-start
    print_tree(my_tree, root)
    excel = xlwt.Workbook()
    sheet = excel.add_sheet('wiki')
    row = 0
    save_start = time.time()
    save(root, 0)
    print 'Save tree cost time:', time.time()-save_start
    excel.save('隐私权.xls')       # 以Unicode编码中文文件名  u'测试.xls'
    print 'Totally cost time:', time.time()-start




