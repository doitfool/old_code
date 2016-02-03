#coding:utf8
__author__ = 'AC'

from PreProcess import *
from FeatureExtracter import *
from Predict import predict
# 全为0提交
def result1():
    predict_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_predict_data_new.txt')
    fw = open('result1.txt', 'w')
    for data in predict_data_source:
        fw.write(data[0]+'\t'+data[1]+'\t0,0,0\n')
    fw.close()

# 按照平均值提交
def result2():
    print '开始时间:', time.asctime()
    begin_time = time.time()
    train_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_train_data_new.txt')
    predict_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_predict_data_new.txt')
    predict_data_uids = get_uids(predict_data_source)
    uid_avg_dict = {}
    # for uid in predict_data_uids:
    #     uid_fc = get_forward_counts_of_uid(train_data_source, uid)
    #     uid_cc = get_comment_counts_of_uid(train_data_source, uid)
    #     uid_lc = get_like_counts_of_uid(train_data_source, uid)
    #     if len(uid_fc) == 0:  # train_data中不含有该UID
    #         avg_fc, avg_cc, avg_lc = 0,0,0
    #     else:
    #         avg_fc = int(sum(uid_fc)/len(uid_fc))
    #         avg_cc = int(sum(uid_cc)/len(uid_cc))
    #         avg_lc = int(sum(uid_lc)/len(uid_lc))
    #     uid_avg_dict[uid] = str(avg_fc)+','+str(avg_cc)+','+str(avg_lc)
    #     print uid, avg_fc, avg_cc, avg_lc
    for uid in predict_data_uids:
        avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc = fcl_info_of_uid(train_data_source, uid)
        uid_avg_dict[uid] = str(avg_fc)+','+str(avg_cc)+','+str(avg_lc)
        print uid, avg_fc, avg_cc, avg_lc
    fw = open('result2.txt', 'w')
    for data in predict_data_source:
        fw.write(data[0]+'\t'+data[1]+'\t'+uid_avg_dict[data[0]]+'\n')
    fw.close()
    print '结束时间:', time.asctime()
    end_time = time.time()
    print '共用时:', str(end_time-begin_time)+'s.'

# gbdt模型
def result3():
    print '开始时间:', time.asctime()
    begin_time = time.time()
    y_fc = predict('E:\PycharmProjects\WeiboChallenge\code\gbdtmodel_fc.m', 'E:\PycharmProjects\WeiboChallenge\code\gbdtfeature_fc_predict')
    y_cc = predict('E:\PycharmProjects\WeiboChallenge\code\gbdtmodel_cc.m', 'E:\PycharmProjects\WeiboChallenge\code\gbdtfeature_cc_predict')
    y_lc = predict('E:\PycharmProjects\WeiboChallenge\code\gbdtmodel_lc.m', 'E:\PycharmProjects\WeiboChallenge\code\gbdtfeature_lc_predict')
    predict_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_predict_data_new.txt')
    fw = open('result3.txt', 'w')
    uids, mids = [], []
    for data in predict_data_source:
        uids.append(data[0])
        mids.append(data[1])
    print len(uids), len(mids), len(y_fc), len(y_cc), len(y_lc)
    for i in xrange(len(uids)):
        fw.write(uids[i]+'\t'+mids[i]+'\t'+str(int(y_fc[i]))+','+str(int(y_cc[i]))+','+str(int(y_lc[i]))+'\n')
    fw.close()
    print '结束时间:', time.asctime()
    end_time = time.time()
    print '共用时:', str(end_time-begin_time)+'s.'

# 添加特征后的gbdt模型
def result4():
    print '开始时间:', time.asctime()
    begin_time = time.time()
    y_fc = predict(model_dir+'gbdtmodel_fc.m', feature_dir+'gbdtfeature_fc_predict')
    y_cc = predict(model_dir+'gbdtmodel_cc.m', feature_dir+'gbdtfeature_cc_predict')
    y_lc = predict(model_dir+'gbdtmodel_lc.m', feature_dir+'gbdtfeature_lc_predict')
    predict_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_predict_data_new.txt')
    fw = open('result4.txt', 'w')
    uids, mids = [], []
    for data in predict_data_source:
        uids.append(data[0])
        mids.append(data[1])
    print len(uids), len(mids), len(y_fc), len(y_cc), len(y_lc)
    for i in xrange(len(uids)):
        fw.write(uids[i]+'\t'+mids[i]+'\t'+str(int(y_fc[i]))+','+str(int(y_cc[i]))+','+str(int(y_lc[i]))+'\n')
    fw.close()
    print '结束时间:', time.asctime()
    end_time = time.time()
    print '共用时:', str(end_time-begin_time)+'s.'

def save_uid_median_dict(data_source, dir=''):
    uid_median_dict = {}
    fw = open(dir+'uid_median_dict.txt', 'w')
    uids = get_uids(data_source)
    for uid in uids:
        fc_list, cc_list, lc_list = get_fcl_counts_of_uid(data_source, uid)
        fc_median = int(round(median(fc_list)))
        cc_median = int(round(median(cc_list)))
        lc_median = int(round(median(lc_list)))
        uid_median_dict[uid] = [fc_median, cc_median, lc_median]
        print uid, fc_median, cc_median, lc_median
        fw.write(uid+'\t'+str(fc_median)+'\t'+str(cc_median)+'\t'+str(lc_median)+'\n')
    fw.close()
    return uid_median_dict
# save_uid_median_dict(load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_train_data_new.txt'))

def load_uid_median_dict(dir=''):
    uid_median_dict = {}
    fr = open(dir+'uid_median_dict.txt', 'r')
    for line in fr:
        uid, fc_median, cc_median, lc_median = line.strip().split('\t')
        print uid, fc_median, cc_median, lc_median
        uid_median_dict[uid] = [int(fc_median), int(cc_median), int(lc_median)]
    fr.close()
    return uid_median_dict

# 按照中位数提交
def result5():
    train_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_train_data_new.txt')
    predict_data_source = load_data('E:\PycharmProjects\WeiboChallenge\data\weibo_predict_data_new.txt')
    print 'load data finished.'
    train_data_uids = get_uids(train_data_source)
    print 'get train data uids finished.'
    uid_median_dict = load_uid_median_dict()
    print 'get train data uid-median dict finished.'
    fw = open('result5.txt', 'w')
    for data in predict_data_source:
        uid, mid = data[0], data[1]
        fc_median, cc_median, lc_median = 0, 0, 0
        if uid in train_data_uids:
            fc_median, cc_median, lc_median = uid_median_dict[uid]
        print uid, mid, fc_median, cc_median, lc_median
        fw.write(uid+'\t'+mid+'\t'+str(fc_median)+','+str(cc_median)+','+str(lc_median)+'\n')
    fw.close()

if __name__ == '__main__':
    feature_dir = 'E:\PycharmProjects\WeiboChallenge\\feature1\\'
    model_dir = 'E:\PycharmProjects\WeiboChallenge\\model1\\'
    # result1()
    # result2()
    # result3()
    # result4()
    result5()
