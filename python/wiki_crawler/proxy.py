# coding:utf8
__author__ = 'AC'

import urllib2
from BeautifulSoup import BeautifulSoup

'''
    读取proxy-list网站的proxy列表，作为代理池
    http://proxy-list.org/chinese/index.php
'''
class proxy():
    _proxy = ''
    _https = ''



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
    proxys_tag = soup.findAll('div', {'class': 'table'})
    print type(proxys_tag), proxys_tag
    print type(proxys_tag.children)
    best_proxy = None
    for proxy in proxys_tag.children:
        print proxy

    return best_proxy.contents