#coding:utf8
__author__ = 'AC'
import time
import jieba
import jieba.analyse
import pynlpir
import polyglot
from polyglot.text import Text
from numpy import *
from PreProcess import *

'''
train_data_feature文件格式为二维列表，列属性如下
转发模型: time_unix，avg_fc, max_fc, min_fc, blog_length, exist_link, exist_at_symbol, is_topic, fc_count
评论模型: time_unix，avg_cc, max_cc, min_cc, blog_length, exist_link, exist_at_symbol, is_topic, cc_count
点赞模型: time_unix，avg_lc, max_lc, min_lc, blog_length, exist_link, exist_at_symbol, is_topic, lc_count
predict_data_feature文件不包括最后一列
'''
'''
现有特征: time_unix，avg_fc, max_fc, min_fc, blog_length, exist_link
         num_of_at_symbol(@字符个数), num_of_well_symbol(#字符个数), num_of_num(数字字符的个数),
         exist_chat_symbol(含有“”字符), exist_book_symbol(含有《》字符)
         exist_bigbracket_symbol(含有{}字符)， exist_smallbracket_symbol(含有（）()字符)
         exist_midbracket_symbol(含有【】[]字符), exist_exclamation_mark(含有！!)

现有特征
        time_unix，avg_fc, max_fc, min_fc, blog_length, exist_link, exist_chat_symbol, exist_book_symbol,
        exist_bigbracket_symbol, exist_smallbracket_symbol, exist_midbracket_symbol, exist_exclamation_mark,
        num_of_at_symbol, num_of_well_symbol, num_of_num
'''

def blog_features(blog):
    words = jieba.cut(blog)
    blog_length = len(blog)
    exist_link = 0
    if 'http' in blog:
        exist_link = 1
    exist_chat_symbol = 0
    if '"' in blog:
        exist_chat_symbol = 1
    exist_book_symbol = 0
    if '《' in blog:
        exist_book_symbol = 1
    exist_bigbracket_symbol = 0
    if '{' in blog:
        exist_bigbracket_symbol = 1
    exist_smallbracket_symbol = 0
    if '(' in blog or '（' in blog:
        exist_smallbracket_symbol = 1
    exist_midbracket_symbol = 0
    if '[' in blog or '【' in blog:
        exist_midbracket_symbol = 1
    exist_exclamation_mark = 0
    if '!' in blog or '！' in blog:
        exist_exclamation_mark = 1

    num_of_at_symbol, num_of_well_symbol, num_of_num = 0, 0, 0
    for word in words:
        if word == '@':
            num_of_at_symbol += 1
        elif word == '#':
            num_of_well_symbol += 1
        elif word.isdigit():
            num_of_num += 1
    features = [blog_length, exist_link, exist_chat_symbol, exist_book_symbol, exist_bigbracket_symbol,
                exist_smallbracket_symbol, exist_midbracket_symbol, exist_exclamation_mark,
                num_of_at_symbol, num_of_well_symbol, num_of_num]
    return features

def keywords(blog):
    kwords = jieba.analyse.textrank(blog)
    return kwords

# 博文主题
# 博文情感分析
def sentiment(blog):
    text = Text(blog)
    print text.detect_language()
    print text.polarity


# 博文发送时间(转换为UNIX时间戳)
def blog_send_time(time_str):
    return int(time.mktime(time.strptime(time_str, '%Y-%m-%d %H:%M:%S')))

# 用户历史博文数目(time_unix前的博文)
def blog_nums_of_uid(train_data_source, uid, time_unix):
    blogs_of_uid = get_blogs_of_uid(train_data_source, uid, time_unix)
    return len(blogs_of_uid)

# 用户历史博文转发评论点赞平均值，最大值，最小值
def fcl_info_of_uid(train_data_source, uid, time_unix=0):
    blogs_of_uid = get_blogs_of_uid(train_data_source, uid, time_unix)
    forward_counts = get_forward_counts(blogs_of_uid)
    comment_counts = get_comment_counts(blogs_of_uid)
    like_counts = get_like_counts(blogs_of_uid)
    if len(forward_counts) == 0:
        avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc = 0,0,0,0,0,0,0,0,0
    else:
        avg_fc = int(sum(forward_counts)/len(forward_counts))
        max_fc = max(forward_counts)
        min_fc = min(forward_counts)
        avg_cc = int(sum(comment_counts)/len(comment_counts))
        max_cc = max(comment_counts)
        min_cc = min(comment_counts)
        avg_lc = int(sum(like_counts)/len(like_counts))
        max_lc = max(like_counts)
        min_lc = min(like_counts)
    return avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc

'''
    uid_fcl_dict_file.txt格式
    列属性包括uid, avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc
'''

def save_uid_fcl_dict_to_file(train_data_source, dir=''):
    fw = open(dir+'uid_fcl_dict_file.txt', 'w')
    uid_fcl_info_dict = {}
    for line in train_data_source:
        if uid_fcl_info_dict.has_key(line[0]):
            continue
        else:
            uid_fcl_info_dict[line[0]] = list(fcl_info_of_uid(train_data_source, line[0]))
            fw.write(line[0])
            for num in uid_fcl_info_dict[line[0]]:
                fw.write('\t'+str(num))
            fw.write('\n')
        print '已存储', len(uid_fcl_info_dict), '位用户的fcl信息'
    fw.close()
#
# train_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_train_data_new.txt')
# save_uid_fcl_dict_to_file(train_data_source)

def load_uid_fcl_dict(dir=''):
    uid_fcl_info_dict = {}
    fr = open(dir+'uid_fcl_dict_file.txt', 'r')
    for line in fr:
        mapping = line.split('\t')
        key, value = mapping[0], mapping[1:]
        uid_fcl_info_dict[key] = [int(x.strip()) for x in value]
    fr.close()
    return uid_fcl_info_dict


'''
    train_data_feature文件格式为二维列表，列属性如下
    转发模型: time_unix，avg_fc, max_fc, min_fc, blog_length, exist_link, exist_symbol, is_topic, fc_count
    评论模型: time_unix，avg_cc, max_cc, min_cc, blog_length, exist_link, exist_symbol, is_topic, cc_count
    点赞模型: time_unix，avg_lc, max_lc, min_lc, blog_length, exist_link, exist_symbol, is_topic, lc_count
    predict_data_feature文件不包括最后一列
'''
'''
现有特征(15个)
    time_unix，avg_fcl, max_fcl, min_fcl, blog_length, exist_link, exist_chat_symbol, exist_book_symbol,
    exist_bigbracket_symbol, exist_smallbracket_symbol, exist_midbracket_symbol, exist_exclamation_mark,
    num_of_at_symbol, num_of_well_symbol, num_of_num
'''

# 生成train_data特征文件
def build_train_data_feature(train_data_source, dir=''):
    features_fc, features_cc, features_lc = [], [], []
    uid_fcl_info_dict = load_uid_fcl_dict(dir)
    print 'uid_fcl_info_dict目录:', dir
    for i in xrange(len(train_data_source)):
        print '训练第'+str(i+1)+'条博文数据'
        uid, mid, time, fc, cc, lc, blog = train_data_source[i]
        print uid, mid, time, fc, cc, lc, blog
        time_unix = blog_send_time(time)
        avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc = uid_fcl_info_dict[uid]
        feature_fc = [time_unix, avg_fc, max_fc, min_fc] + blog_features(blog) + [int(fc)]
        feature_cc = [time_unix, avg_cc, max_cc, min_cc] + blog_features(blog) + [int(cc)]
        feature_lc = [time_unix, avg_lc, max_lc, min_lc] + blog_features(blog) + [int(lc)]
        features_fc.append(feature_fc)
        features_cc.append(feature_cc)
        features_lc.append(feature_lc)
    return features_fc, features_cc, features_lc

# 生成predict_data特征文件
def build_predict_data_feature(predict_data_source, dir=''):
    features_fc, features_cc, features_lc = [], [], []
    uid_fcl_info_dict = load_uid_fcl_dict(dir)
    print 'uid_fcl_info_dict目录:', dir
    for i in xrange(len(predict_data_source)):
        print '训练第'+str(i+1)+'条博文数据'
        uid, mid, time, blog = predict_data_source[i]
        print uid, mid, time, blog
        time_unix = blog_send_time(time)
        if uid_fcl_info_dict.has_key(uid):
            avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc = uid_fcl_info_dict[uid]
        else:
            avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc = 0, 0, 0, 0, 0, 0, 0, 0, 0
        feature_fc = [time_unix, avg_fc, max_fc, min_fc] + blog_features(blog)
        feature_cc = [time_unix, avg_cc, max_cc, min_cc] + blog_features(blog)
        feature_lc = [time_unix, avg_lc, max_lc, min_lc] + blog_features(blog)
        features_fc.append(feature_fc)
        features_cc.append(feature_cc)
        features_lc.append(feature_lc)
    return features_fc, features_cc, features_lc

def build_test_data_feature(test_data_source, dir=''):
    features_fc, features_cc, features_lc = [], [], []
    uid_fcl_info_dict = load_uid_fcl_dict(dir)
    print 'uid_fcl_info_dict目录:', dir
    for i in xrange(len(test_data_source)):
        print '训练第'+str(i+1)+'条博文数据'
        uid, mid, time, fc, cc, lc, blog = test_data_source[i]
        print uid, mid, time, fc, cc, lc, blog
        time_unix = blog_send_time(time)
        if uid_fcl_info_dict.has_key(uid):
            avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc = uid_fcl_info_dict[uid]
        else:
            avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc = 0, 0, 0, 0, 0, 0, 0, 0, 0
        feature_fc = [time_unix, avg_fc, max_fc, min_fc] + blog_features(blog)
        feature_cc = [time_unix, avg_cc, max_cc, min_cc] + blog_features(blog)
        feature_lc = [time_unix, avg_lc, max_lc, min_lc] + blog_features(blog)
        features_fc.append(feature_fc)
        features_cc.append(feature_cc)
        features_lc.append(feature_lc)
    return features_fc, features_cc, features_lc

# 将特征存入文件
def save_feature(feature, filename):
    fw = open(filename, 'w')
    for line in feature:
        for info in line[:-1]:
            fw.write(str(info)+'\t')
        fw.write(str(line[-1])+'\n')
    fw.close()

def load_train_data_feature(feature_file):
    fr = open(feature_file, 'r')
    X, y = [], []
    for line in fr:
        features = line.split('\t')
        x_temp = [int(feature.strip()) for feature in features[:-1]]
        print x_temp
        X.append(x_temp)
        y.append(int(features[-1].strip()))
    fr.close()
    return array(X), array(y)

def load_predict_data_feature(feature_file):
    fr = open(feature_file, 'r')
    X = []
    for line in fr:
        features = line.split('\t')
        x_temp = [int(feature.strip()) for feature in features]
        print x_temp
        X.append(x_temp)
    fr.close()
    return array(X)


if __name__ == '__main__':
    # 生成train_data对应的特征文件
    # ------------------------------------
    feature_dir = 'E:\PycharmProjects\WeiboChallenge\\feature1\\'
    print '开始抽取特征:', time.asctime()
    begin_time = time.time()
    features_fc, features_cc, features_lc = build_train_data_feature(load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_train_data_new.txt'))
    feature_build_time = time.time()
    print '特征抽取结束:', time.asctime()
    print '抽取特征用时:', feature_build_time-begin_time, 's.'
    # ------------------------------------
    print '开始存储特征:', time.asctime()
    save_feature(features_fc, feature_dir+'gbdtfeature_fc')
    save_feature(features_cc, feature_dir+'gbdtfeature_cc')
    save_feature(features_lc, feature_dir+'gbdtfeature_lc')
    feature_save_time = time.time()
    print '存储特征结束:', time.asctime()
    print '存储特征用时:', feature_save_time-feature_build_time, 's.'

    # 生成predict_data对应的特征文件
    predict_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_predict_data_new.txt')
    features_fc, features_cc, features_lc = build_predict_data_feature(predict_data_source)
    save_feature(features_fc, feature_dir+'gbdtfeature_fc_predict')
    save_feature(features_cc, feature_dir+'gbdtfeature_cc_predict')
    save_feature(features_lc, feature_dir+'gbdtfeature_lc_predict')