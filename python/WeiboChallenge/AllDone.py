# coding:utf8
__author__ = 'AC'

from numpy import *
from PreProcess import load_data
from FeatureExtracter import build_train_data_feature, build_predict_data_feature, build_test_data_feature
from Models import GBDT, RF

# 特征矩阵化
def matrixize_train_features(features):
    X, y = [], []
    for feature in features:
        x_temp = feature[:-1]
        X.append(x_temp)
        y.append(feature[-1])
        print x_temp, '|', feature[-1]
    return array(X), array(y)


def all_done(model, train_data_file, predict_data_file, result_file, dir=''):
    features_fc, features_cc, features_lc = build_train_data_feature(load_data(train_data_file), dir)
    X_fc, y_fc = matrixize_train_features(features_fc)
    X_cc, y_cc = matrixize_train_features(features_cc)
    X_lc, y_lc = matrixize_train_features(features_lc)

    model_fc = model(X_fc, y_fc)
    model_cc = model(X_cc, y_cc)
    model_lc = model(X_lc, y_lc)

    predict_data_source = load_data(predict_data_file)
    features_fc_predict, features_cc_predict, features_lc_predict = build_test_data_feature(predict_data_source, dir)

    y_fc_predict = model_fc.predict(array(features_fc_predict))
    y_cc_predict = model_cc.predict(array(features_cc_predict))
    y_lc_predict = model_lc.predict(array(features_lc_predict))

    fw = open(result_file, 'w')
    uids, mids = [], []
    for data in predict_data_source:
        uids.append(data[0])
        mids.append(data[1])
    for i in xrange(len(uids)):
        fw.write(uids[i]+'\t'+mids[i]+'\t'+str(int(y_fc_predict[i]))+','+str(int(y_cc_predict[i]))+','+str(int(y_lc_predict[i]))+'\n')
    fw.close()

if __name__ == '__main__':
    dir = 'E:\PycharmProjects\WeiboChallenge\code\Evaluation\\'
    # all_done(GBDT, dir+'train_data.txt', dir+'test_data.txt', dir+'result_15gbdt.txt', dir)
    all_done(RF, dir+'train_data.txt', dir+'test_data.txt', dir+'result_rf.txt', dir)
