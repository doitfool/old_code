# coding:utf8
__author__ = 'AC'

from PreProcess import *
from FeatureExtracter import *
from pylab import *
import matplotlib.pyplot as plt

# 博文长度与转发数，评论数，点赞数的关系
def relation_fcl_and_blog_length(data_source, fcl):
    fclist, cclist, lclist, bllist = [], [], [], []
    for data in data_source:
        fc, cc, lc = int(data[3]), int(data[4]), int(data[5])
        bl = blog_length(data[-1])
        print fc, cc, lc, bl
        fclist.append(fc)
        cclist.append(cc)
        lclist.append(lc)
        bllist.append(bl)
    # 绘制转发数，评论数，点赞数柱状图

    if fcl == 1:
        plt.plot(bllist, fclist, 'r*')
        plt.ylabel('forward_count')
    elif fcl == 2:
        plt.plot(bllist, cclist, 'r*')
        plt.ylabel('comment_count')
    elif fcl == 3:
        plt.plot(bllist, lclist, 'r*')
        plt.ylabel('like_count')
    plt.xlabel('blog_length')
    plt.title('blog_length | fcl')
    plt.show()


if __name__ == '__main__':
    pass
    # train_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_train_data_new.txt')
    # relation_fcl_and_blog_length(sort_by_fc(train_data_source)[:5000], 1)
    # relation_fcl_and_blog_length(sort_by_cc(train_data_source)[:5000], 2)
    # relation_fcl_and_blog_length(sort_by_lc(train_data_source)[:5000], 3)