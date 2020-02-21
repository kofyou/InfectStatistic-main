from datetime import date
import pytest

TYPE_CN_MAP = {'infected': '感染患者', 'suspect': '疑似患者', 'cure': '治愈', 'death': '死亡'}


def parse_output_line(province, data):
    """
    处理输出的一行数据到题目指定格式
    :param province: 省份名称
    :param data: 存储数据的字典，符合Statistic.data的结构
    :return: 数据行
    """
    if province in data.keys():
        oneLine = province
        for k2, v2 in data[province].items():
            oneLine += ' ' + TYPE_CN_MAP[k2] + str(v2) + '人'
        return oneLine + '\n'
    return ''


def str_to_date(s, separator='-'):
    """
    字符串转date对象
    :param s: 日期字符串，分隔符由separator指定
    :param separator: 日期的分隔符
    :return: date对象
    """
    d = s.split(separator)
    d = list(map(lambda x: int(x), d))
    return date(d[0], d[1], d[2])
