# coding:utf8
__author__ = 'AC'
import time
from PreProcess import load_data
from FeatureExtracter import *
from Models import *
from Predict import *
from Result import save_uid_median_dict, load_uid_median_dict
'''
    train_data是2月1日到7月31日的数据， predict_data是8月份的数据
    可以采用train_data的2~6月份的数据作为训练集，7月份的数据作为测试集，评估算法的准确度
    转发偏差 deviation(f) = |count(fp)-count(fr)| / ( count(fr)+5 )
    评论偏差 deviation(c) = |count(cp)-count(cr)| / ( count(cr)+3 )
    点赞偏差 deviation(l) = |count(lp)-count(lr)| / ( count(lr)+3 )
    第i篇博文的准确度 precision(i) = 1-0.5*deviation(f)-0.25*deviation(c)-0.25*deviation(l)
    算法准确度 precision = ∑(count(i)+1)*sgn(precision(i)-0.8) / ∑(count(i)+1)
    sgn(x)为改进的符号函数,当x>0时,sgn(x)=1当x<=0时,sgn(x)=0
    count(i)为第i篇博文的总的转发、评论、赞之和,当count(i)>100时，取值为100
'''

def predict_data_feature(predict_data_source):
    features_fc, features_cc, features_lc = [], [], []
    uid_fcl_info_dict = load_uid_fcl_dict()
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

def save_feature_file():
    train_data_source = load_data(dir+'train_data.txt')
    feature_fc_train, feature_cc_train, feature_lc_train = build_train_data_feature(train_data_source)
    save_feature(feature_fc_train, dir+'feature_fc_train')
    save_feature(feature_cc_train, dir+'feature_cc_train')
    save_feature(feature_lc_train, dir+'feature_lc_train')

    predict_data_source = load_data(dir+'predict_data.txt')
    feature_fc_predict, feature_cc_predict, feature_lc_predict = predict_data_feature(predict_data_source)
    save_feature(feature_fc_predict, dir+'feature_fc_predict')
    save_feature(feature_cc_predict, dir+'feature_cc_predict')
    save_feature(feature_lc_predict, dir+'feature_lc_predict')

def save_gbdtmodel_file():
    print '开始训练转发GBDT模型:', time.asctime()
    begin_time = time.time()
    X_fc, y_fc = load_train_data_feature(dir+'feature_fc_train')
    gbdtmodel_fc = GBDT(X_fc, y_fc)
    joblib.dump(gbdtmodel_fc, dir+'gbdtmodel_fc.m')
    fc_model_finish_time = time.time()
    print '训练转发GBDT模型结束:', time.asctime()
    print '训练转发GBDT模型用时:', fc_model_finish_time-begin_time

    print '开始训练评论GBDT模型:', time.asctime()
    X_cc, y_cc = load_train_data_feature(dir+'feature_cc_train')
    gbdtmodel_cc = GBDT(X_cc, y_cc)
    joblib.dump(gbdtmodel_cc, dir+'gbdtmodel_cc.m')
    cc_model_finish_time = time.time()
    print '训练评论GBDT模型结束:', time.asctime()
    print '训练评论GBDT模型用时:', cc_model_finish_time-begin_time-fc_model_finish_time

    print '开始训练点赞GBDT模型:', time.asctime()
    X_lc, y_lc = load_train_data_feature(dir+'feature_lc_train')
    gbdtmodel_lc = GBDT(X_lc, y_lc)
    joblib.dump(gbdtmodel_lc, dir+'gbdtmodel_lc.m')
    lc_model_finish_time = time.time()
    print '训练点赞GBDT模型结束:', time.asctime()
    print '训练点赞GBDT模型用时:', lc_model_finish_time-cc_model_finish_time

'''
    ----------------------------
    采用预测文件和模型进行准确度计算
    ----------------------------
'''

# 实际的7月份博文mid-fcl映射关系
def mid_rfcl_dict():
    test_data_source = load_data('E:\PycharmProjects\WeiboChallenge\code\Evaluation\\test_data.txt')
    mid_rfcl_info_dict = {}
    for data in test_data_source:
        mid, fr, cr, lr = data[1], int(data[3]), int(data[4]), int(data[5])
        mid_rfcl_info_dict[mid] = [fr, cr, lr]
    return mid_rfcl_info_dict


# 预测的博文mid-fcl映射关系以及mid-count映射关系
def mid_pfcl_count_dict(model_files, predict_feature_files):
    test_data_source = load_data('E:\PycharmProjects\WeiboChallenge\code\Evaluation\\test_data.txt')
    mid_pfcl_info_dict = {}
    mid_count_info_dict = {}
    y_fc = predict(model_files[0], predict_feature_files[0])
    y_cc = predict(model_files[1], predict_feature_files[1])
    y_lc = predict(model_files[2], predict_feature_files[2])
    for i in xrange(len(test_data_source)):
        mid = test_data_source[i][1]
        fp, cp, lp = int(y_fc[i]), int(y_cc[i]), int(y_lc[i])
        mid_pfcl_info_dict[mid] = [fp, cp, lp]
        mid_count_info_dict[mid] = fp+cp+lp
    return mid_pfcl_info_dict, mid_count_info_dict

def precision(model_files, predict_feature_files):
    mid_rfcl = mid_rfcl_dict()
    mid_pfcl, mid_count = mid_pfcl_count_dict(model_files, predict_feature_files)
    mid_precision = {}
    for mid in mid_rfcl:
        fr, cr, lr = mid_rfcl[mid]
        fp, cp, lp = mid_pfcl[mid]
        fd = abs(fp-fr)/(fr+5)
        cd = abs(cp-cr)/(cr+3)
        ld = abs(lp-lr)/(lr+3)
        mid_precision[mid] = 1-0.5*fd-0.25*cd-0.25*ld
    sum = 0.0
    num = 0
    for mid in mid_count:
        counti = mid_count[mid]
        if counti > 100:
            counti = 100
        if mid_precision[mid]-0.8 > 0:
            sgn = 1
        else:
            sgn = 0
        sum += (counti+1)*sgn
        num += (counti+1)
    return sum/num


'''
    --------------------------------
    采用预测文件和结果文件进行准确度计算
    --------------------------------
'''
def mid_pfcl_count_dict2(result_data_source):
    mid_pfcl_info_dict = {}
    mid_count_dict = {}
    for data in result_data_source:
        mid = data[1]
        fr, cr, lr = [int(count) for count in data[2].split(',')]
        mid_count_dict[mid] = fr+cr+lr
        mid_pfcl_info_dict[mid] = [fr, cr, lr]
        print mid, ':', mid_pfcl_info_dict[mid], '|', mid_count_dict[mid]
    return mid_pfcl_info_dict, mid_count_dict

def precision2(result_data_source):
    mid_rfcl = mid_rfcl_dict()
    mid_pfcl, mid_count = mid_pfcl_count_dict2(result_data_source)
    mid_precision = {}
    fw = open(dir+'mid_of_sgn0_result1', 'w')
    for mid in mid_rfcl:
        fr, cr, lr = mid_rfcl[mid]
        fp, cp, lp = mid_pfcl[mid]
        fd = abs(fp-fr)/(fr+5)
        cd = abs(cp-cr)/(cr+3)
        ld = abs(lp-lr)/(lr+3)
        mid_precision[mid] = 1-0.5*fd-0.25*cd-0.25*ld
    sum = 0.0
    num = 0
    for mid in mid_count:
        counti = mid_count[mid]
        if counti > 100:
            counti = 100
        if mid_precision[mid]-0.8 > 0:
            sgn = 1
        else:
            sgn = 0
        print 'counti=', counti, '\tsgn=', sgn
        sum += (counti+1)*sgn
        num += (counti+1)
        print 'sum=', sum, '\tnum=', num
        if sgn==0:
            fw.write(mid+'\n')
    fw.close()
    return sum/num

# 全为0提交
def result1():
    test_data_source = load_data('E:\PycharmProjects\WeiboChallenge\code\Evaluation\\test_data.txt')
    fw = open(dir+'result1.txt', 'w')
    for data in test_data_source:
        fw.write(data[0]+'\t'+data[1]+'\t0,0,0\n')
    fw.close()

# 按照平均值提交
def result2():
    print '开始时间:', time.asctime()
    begin_time = time.time()
    train_data_source = load_data(dir+'train_data.txt')
    predict_data_source = load_data(dir+'test_data.txt')
    predict_data_uids = get_uids(predict_data_source)
    uid_avg_dict = {}
    for uid in predict_data_uids:
        avg_fc, max_fc, min_fc, avg_cc, max_cc, min_cc, avg_lc, max_lc, min_lc = fcl_info_of_uid(train_data_source, uid)
        uid_avg_dict[uid] = str(avg_fc)+','+str(avg_cc)+','+str(avg_lc)
        print uid, avg_fc, avg_cc, avg_lc
    fw = open(dir+'result2.txt', 'w')
    for data in predict_data_source:
        fw.write(data[0]+'\t'+data[1]+'\t'+uid_avg_dict[data[0]]+'\n')
    fw.close()
    print '结束时间:', time.asctime()
    end_time = time.time()
    print '共用时:', str(end_time-begin_time)+'s.'

# 按照中位数提交
def result3():
    print '开始时间:', time.asctime()
    begin_time = time.time()
    train_data_source = load_data(dir+'train_data.txt')
    predict_data_source = load_data(dir+'test_data.txt')
    print 'get train data uids finished.'
    train_data_uids = get_uids(train_data_source)
    uid_median_dict = load_uid_median_dict(dir)
    print 'get train data uid-median dict finished.'
    fw = open(dir+'result_median.txt', 'w')
    for data in predict_data_source:
        uid, mid = data[0], data[1]
        fc_median, cc_median, lc_median = 0, 0, 0
        if uid in train_data_uids:
            fc_median, cc_median, lc_median = uid_median_dict[uid]
        print uid, mid, fc_median, cc_median, lc_median
        fw.write(uid+'\t'+mid+'\t'+str(fc_median)+','+str(cc_median)+','+str(lc_median)+'\n')
    fw.close()
    print '结束时间:', time.asctime()
    end_time = time.time()
    print '共用时:', str(end_time-begin_time)+'s.'

if __name__ == '__main__':
    # 不更改特征，下列方法不需执行
    # save_feature_file()
    # save_gbdtmodel_file()
    # -------------------------

    dir = 'E:\PycharmProjects\WeiboChallenge\code\Evaluation\\'
    '''
        根据模型计算准确率:
    '''
    # predict_data_source = load_data(dir+'test_data.txt')
    # model_file = [dir+'gbdtmodel_fc.m',
    #               dir+'gbdtmodel_cc.m',
    #               dir+'gbdtmodel_lc.m']
    # predict_feature_file = [dir+'feature_fc_predict',
    #                         dir+'feature_cc_predict',
    #                         dir+'feature_lc_predict']
    # print precision(predict_data_source, model_file, predict_feature_file)
    # 8特征gbdt模型准确率 0.57093882199

    '''
        根据文件计算准确率:
    '''
    # result1()
    # result2()
    # result3()

    # result_all0_data_source = load_data(dir+'result_all0.txt')
    # print precision2(result_all0_data_source)
    # result_avg_data_source = load_data(dir+'result_avg.txt')
    # print precision2(result_avg_data_source)
    result_median_data_source = load_data(dir+'result_median.txt')
    print precision2(result_median_data_source)

    # result_15gbdt_data_source = load_data(dir+'result_15gbdt.txt')
    # print precision2(result_15gbdt_data_source)

    # result_rf_data_source = load_data(dir+'result_rf.txt')
    # print precision2(result_rf_data_source)

    # 全0提交准确率 1.0 ???????
    # 平均值提交准确率 0.543233827834
    # 中位数提交准确率 0.759496086748
    # 8特征gbdt模型准确率 0.57093882199
    # 15特征gbdt模型准确率 0.550606652926
    # 15特征rf模型准确率 0.336594357707