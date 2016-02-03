# coding:utf8
__author__ = 'AC'

from sklearn.externals import joblib
from FeatureExtracter import load_predict_data_feature
def predict(model_file, predict_feature_file):
    model = joblib.load(model_file)
    X = load_predict_data_feature(predict_feature_file)
    y = model.predict(X)
    return y

if __name__ == '__main__':
    y_fc = predict('E:\PycharmProjects\WeiboChallenge\code\gbdtmodel_fc.m', 'E:\PycharmProjects\WeiboChallenge\code\gbdtfeature_fc_predict')
    y_cc = predict('E:\PycharmProjects\WeiboChallenge\code\gbdtmodel_cc.m', 'E:\PycharmProjects\WeiboChallenge\code\gbdtfeature_cc_predict')
    y_lc = predict('E:\PycharmProjects\WeiboChallenge\code\gbdtmodel_lc.m', 'E:\PycharmProjects\WeiboChallenge\code\gbdtfeature_lc_predict')
    print y_fc
    print y_cc
    print y_lc
