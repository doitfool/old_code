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
    # proxy = random.choice(proxy_servers())
    # 131.255.172.169:8080  123.56.124.91:3128 190.0.43.186:8080
    # proxy = '222.34.3.9:8080'  159.203.122.165:8080  125.212.225.86:3128 220.170.198.207:3128
    proxy = '192.99.20.92:3128'
    print 'proxy:', proxy,
    try:
        proxy_support = urllib2.ProxyHandler({'http': proxy})
        opener = urllib2.build_opener(proxy_support, urllib2.HTTPHandler)
        urllib2.install_opener(opener)
        page = urllib2.urlopen(url).read().decode('utf-8')
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
def search(url, depth):
    global urls_visited, my_tree
    print time.strftime('%H:%M:%S', time.localtime(time.time())), 'depth='+str(depth), url
    root = node(url)
    if not visited(url):
        urls_visited.append(url)
        if not is_leaf(url):
            print 'branch'
            nodes, c_links, p_links = [], [], []
            c_links, p_links = get_content(get_page(url))
            for c_link in c_links:
                print '\tc_link:', c_link
                c_node = node(c_link)
                nodes.append(c_node)
                search(c_link, depth+1)
            print 'leaf'
            for p_link in p_links:
                print '\tp_link', p_link
                p_node = node(p_link)
                nodes.append(p_node)
            my_tree[root] = nodes
    else:
        print 'visited'
    return root

def print_tree(my_tree, root):
    print urllib.unquote(root._title.encode('utf-8')).decode('utf-8'), '--->', [urllib.unquote(child._title.encode('utf-8')).decode('utf-8') for child in my_tree[root]]
    for child in my_tree[root]:
        print_tree(my_tree, child)


def save(root, column):
    # 深度遍历层次树，顺便写入Excel, row和column表示写入excel表格的行列坐标
    global row, my_tree
    url = root._url
    title = urllib.unquote(root._title.encode('utf-8'))
    print isinstance(title, unicode), isinstance(url, unicode)
    print title.decode('utf-8'), ' ---> ', [urllib.unquote(child._title.encode('utf-8')).decode('utf-8') for child in my_tree[root]]
    print row, column
    sheet.write(row, column, xlwt.Formula("HYPERLINK"+'("'+url+'";"'+title.decode('utf-8')+'")'))
    for child in my_tree[root]:
        save(child, column+1)
    row += 1


if __name__ == '__main__':
    depth = 1
    url = u'https://zh.wikipedia.org/wiki/Category:%E4%BF%A1%E6%81%AF%E5%AE%89%E5%85%A8'   # 爬虫起始网页
    my_tree = tree()
    start = time.time()
    urls_visited = []
    root = search(url, depth)
    print 'Build tree cost time:', time.time()-start
    print_tree(my_tree, root)
    excel = xlwt.Workbook()
    sheet = excel.add_sheet('wiki')
    row = 0
    save_start = time.time()
    save(root, 0)
    print 'Save tree cost time:', time.time()-save_start
    excel.save('wiki.xls')
    print 'Totally cost time:', time.time()-start




