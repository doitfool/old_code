# coding:utf8
__author__ = 'AC'
import time
'''
    data path:
    1.train_data path:     E:\PycharmProjects\WeiboChallenge\data\weibo_train_data.txt
    2.predict_data path:   E:\PycharmProjects\WeiboChallenge\data\weibo_predict_data.txt
    3.result_data path:    E:\PycharmProjects\WeiboChallenge\data\weibo_result_data.txt

    data format:
    1.train_data:   uid, mid, time, forward_count, comment_count, like_count, content
    2.predict_data: uid, mid, time, content
    3.result_data:  uid, mid, forward_count, comment_count, like_count

    task: predict the forward_count, comment_count and like_count of a blog after being sent in one week
'''

# 从文件中读入数据
def load_data(filename):
    file_data = []
    with open(filename, 'r') as fr:
        for line in fr:
            data = line.split('\t')
            file_data.append(data)
    fr.close()
    return file_data

# 打印前num行数据
def print_first_num_line_data(data_source, num):
    col_num = len(data_source[0])
    for i in range(num):
        print i+1, '\t',
        for j in range(col_num-1):
            print data_source[i][j], '\t',
        print data_source[i][col_num-1].decode('utf-8', 'ignore').encode('gbk', 'ignore'),

# 打印从start到end行的数据,当start和end相同时打印第start行数据
def print_start2end_data(data_source, start, end):
    col_num = len(data_source[0])
    for i in range(start-1, end):
        print i+1, '\t',
        for j in range(col_num-1):
            print data_source[i][j], '\t',
        print data_source[i][col_num-1].decode('utf-8', 'ignore').encode('gbk', 'ignore'),

# 打印第row行第col列数据
def print_point_data(data_source, row, col):
    col_num = len(data_source[0])
    if col == col_num:
        print data_source[row-1][col-1].decode('utf-8', 'ignore').encode('gbk', 'ignore')
    else:
        print data_source[row-1][col-1]

# 得到uid用户time_unix前的所有博文,time_unix=0时返回uid用户的所有博文
def get_blogs_of_uid(data_source, uid, time_unix=0):
    if time_unix == 0:
        blogs_of_uid = []
        for i in xrange(len(data_source)):
            if data_source[i][0] == uid:
                blogs_of_uid.append(data_source[i])
        return blogs_of_uid
    else:
        blogs_of_uid = []
        for i in xrange(len(data_source)):
            if data_source[i][0] == uid:
                temp_time = int(time.mktime(time.strptime(data_source[i][2], '%Y-%m-%d')))
                if temp_time < time_unix:
                    blogs_of_uid.append(data_source[i])
        return blogs_of_uid

def get_uids(data_source):
    uids_set = set()
    for i in xrange(len(data_source)):
        uids_set.add(data_source[i][0])
    return uids_set

def get_mids(data_source):
    mids_set = set()
    for i in xrange(len(data_source)):
        mids_set.add(data_source[i][1])
    return mids_set

def get_forward_counts(data_source):
    forward_counts = []
    for i in xrange(len(data_source)):
        forward_counts.append(int(data_source[i][3]))
    return forward_counts

def get_forward_counts_of_uid(data_source, uid):
    forward_counts = []
    for i in xrange(len(data_source)):
        if data_source[i][0] == uid:
            forward_counts.append(int(data_source[i][3]))
    return forward_counts


def get_comment_counts(data_source):
    comment_counts = []
    for i in xrange(len(data_source)):
        comment_counts.append(int(data_source[i][4]))
    return comment_counts

def get_comment_counts_of_uid(data_source, uid):
    comment_counts = []
    for i in xrange(len(data_source)):
        if data_source[i][0] == uid:
            comment_counts.append(int(data_source[i][4]))
    return comment_counts

def get_like_counts(data_source):
    like_counts = []
    for i in xrange(len(data_source)):
        like_counts.append(int(data_source[i][5]))
    return like_counts

def get_like_counts_of_uid(data_source, uid):
    like_counts = []
    for i in xrange(len(data_source)):
        if data_source[i][0] == uid:
            like_counts.append(int(data_source[i][5]))
    return like_counts

def get_fcl_counts(data_source):
    forward_counts, comment_counts, like_counts = [], [], []
    for i in xrange(len(data_source)):
        forward_counts.append(int(data_source[i][3]))
        comment_counts.append(int(data_source[i][4]))
        like_counts.append(int(data_source[i][5]))
    return forward_counts, comment_counts, like_counts

def get_fcl_counts_of_uid(data_source, uid):
    forward_counts, comment_counts, like_counts = [], [], []
    for i in xrange(len(data_source)):
        if data_source[i][0] == uid:
            forward_counts.append(int(data_source[i][3]))
            comment_counts.append(int(data_source[i][4]))
            like_counts.append(int(data_source[i][5]))
    return forward_counts, comment_counts, like_counts

def sort_by_time(data_source, reverse=False):
    return sorted(data_source, key=lambda x: int(time.mktime(time.strptime(x[2], '%Y-%m-%d %H:%M:%S'))), reverse=reverse)

# 存储UID集合求同求异结果到文件中
def store_uids(uid_set, filename):
    dir_path = 'E:\PycharmProjects\WeiboChallenge\data\\'
    fw = open(dir_path+filename, 'w')
    for uid in uid_set:
        fw.write(uid+'\n')
    fw.close()

# 按照forward_count排序,reverse为true是降序，否则升序
def sort_by_fc(data_source, reverse=True):
    return sorted(data_source, key=lambda x: int(x[3]), reverse=reverse)

# 按照comment_count排序
def sort_by_cc(data_source, reverse=True):
    return sorted(data_source, key=lambda x: int(x[4]), reverse=reverse)

# 按照like_count排序
def sort_by_lc(data_source, reverse=True):
    return sorted(data_source, key=lambda x: int(x[5]), reverse=reverse)

# 按照三者之和排序
def sort_by_sum(data_source, reverse=True):
    return sorted(data_source, key=lambda x: (int(x[3])+int(x[4])+int(x[5])), reverse=reverse)

# 存储从start到end行数据到文件中，便于人工分析
def store_start2end_data(data_source, start, end, filename):
    dir_path = 'E:\PycharmProjects\WeiboChallenge\data\\'
    fw = open(dir_path+filename, 'w')
    col_num = len(data_source[0])
    for i in xrange(start-1, end):
        for j in xrange(col_num-1):
            fw.write(data_source[i][j]+'\t')
        fw.write(data_source[i][col_num-1])
    fw.close()

def store_data(data_source, filename):
    dir_path = 'E:\PycharmProjects\WeiboChallenge\data\\'
    fw = open(dir_path+filename, 'w')
    col_num = len(data_source[0])
    for i in xrange(len(data_source)):
        for j in xrange(col_num-1):
            fw.write(data_source[i][j]+'\t')
        fw.write(data_source[i][col_num-1])
    fw.close()


#
# train_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_train_data_new.txt')
# predict_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_predict_data_new.txt')
#
# train_data_uids = get_uids(train_data_source)
# predict_data_uids = get_uids(predict_data_source)
#
# print 'train_data共有UID', len(get_uids(train_data_source))
# print 'train_data共有MID', len(get_mids(train_data_source))
#
# print 'predict_data共有UID', len(get_uids(predict_data_source))
# print 'predict_data共有MID', len(get_mids(predict_data_source))



# print len(get_uids(load_data('E:\PycharmProjects\WeiboChallenge\code\Evaluation\\train_data.txt')))
# 36426
# print len(get_uids(load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_train_data_new.txt')))
# 37263