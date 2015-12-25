# coding:utf8
__author__ = 'AC'

import random
import sys

# 随机生成word
def generate_rand_word(method='1'):
    if method == '1':  # 随机生成word方式一
        word = ''
        character_set = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
        rand_len = random.randint(1, 30)  # word长度随机定义
        for i in xrange(rand_len):
            char = random.choice(character_set)
            word += char
    else:     # 随机生成word方式二
        words = ['blue', 'red', 'yellow', 'pink', 'black', 'white']
        word = random.choice(words)
    # print len(word), word
    return word


# 创建txt文件放入C:\Users\AC\Desktop\test目录下
def create_txt_file(argv):
    # 命令行定义生成文件名，随机生成word总数目，word生成方式
    file_name = argv[1]
    word_num = int(argv[2])
    method = None
    if len(argv) > 3:
        method = argv[3]
    word_dict = {}
    for i in xrange(word_num):
        word = generate_rand_word(method)
        if word_dict.has_key(word):
            word_dict[word] += 1
        else:
            word_dict[word] = 1
    fw = open(file_name, 'w')
    for key in word_dict:
        print key, ':', word_dict[key]
        for i in xrange(word_dict[key]):
            fw.write(key+'\n')
    fw.close()

if __name__ == '__main__':
    create_txt_file(sys.argv)
    # generate_rand_word(False)