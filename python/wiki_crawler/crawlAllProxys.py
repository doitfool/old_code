#coding:utf8
__author__ = 'AC'

'''
 获取proxy-list网站10页中所有的proxy地址
'''
import pprint
import urllib2
from BeautifulSoup import BeautifulSoup
def proxy_servers(url):
    try:
        response = urllib2.urlopen(url)
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

if __name__ == '__main__':
    urls = []
    for p in xrange(1, 11):
        url = 'http://proxy-list.org/chinese/index.php?p=' + str(p)
        urls.append(url)
    proxys = []
    for url in urls:
        print 'crawling\t'+url
        proxys.append(proxy_servers(url))
    for proxy in proxys:
        print proxy
    with open('proxys.txt', 'w') as fw:
        for proxy in proxys:
            for socket in proxy:
                fw.write(str(socket)+'\n')
    fw.close()

