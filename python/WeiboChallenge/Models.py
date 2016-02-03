#coding:utf8
__author__ = 'AC'

import time
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import GradientBoostingRegressor, RandomForestRegressor
from sklearn.externals import joblib
from FeatureExtracter import *

def GBDT(X, y):
    gbdt = GradientBoostingRegressor()
    gbdt.fit(X, y)
    return gbdt

def RF(X, y):
    rf = RandomForestRegressor()
    rf.fit(X, y)
    return rf

if __name__ == '__main__':
    feature_dir = 'E:\PycharmProjects\WeiboChallenge\\feature1\\'
    model_dir = 'E:\PycharmProjects\WeiboChallenge\\model1\\'

    print '开始训练转发GBDT模型:', time.asctime()
    begin_time = time.time()
    X_fc, y_fc = load_train_data_feature(feature_dir+'gbdtfeature_fc')
    gbdtmodel_fc = GBDT(X_fc, y_fc)
    joblib.dump(gbdtmodel_fc, model_dir+'gbdtmodel_fc.m')
    fc_model_finish_time = time.time()
    print '训练转发GBDT模型结束:', time.asctime()
    print '训练转发GBDT模型用时:', fc_model_finish_time-begin_time

    print '开始训练评论GBDT模型:', time.asctime()
    X_cc, y_cc = load_train_data_feature(feature_dir+'gbdtfeature_cc')
    gbdtmodel_cc = GBDT(X_cc, y_cc)
    joblib.dump(gbdtmodel_cc, model_dir+'gbdtmodel_cc.m')
    cc_model_finish_time = time.time()
    print '训练评论GBDT模型结束:', time.asctime()
    print '训练评论GBDT模型用时:', cc_model_finish_time-begin_time-fc_model_finish_time

    print '开始训练点赞GBDT模型:', time.asctime()
    X_lc, y_lc = load_train_data_feature(feature_dir+'gbdtfeature_lc')
    gbdtmodel_lc = GBDT(X_lc, y_lc)
    joblib.dump(gbdtmodel_lc, model_dir+'gbdtmodel_lc.m')
    lc_model_finish_time = time.time()
    print '训练点赞GBDT模型结束:', time.asctime()
    print '训练点赞GBDT模型用时:', lc_model_finish_time-cc_model_finish_time




