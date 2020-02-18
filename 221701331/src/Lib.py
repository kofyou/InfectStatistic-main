from datetime import date

TYPE_CN_MAP = {'infected': '感染患者', 'suspect': '疑似患者', 'cure': '治愈', 'death': '死亡'}


def parse_output_line(province, data):
    """
    处理输出的一行数据
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


def date_str_cmp_less(date1, date2, split='-', equal=True):
    d1 = date1.split(split)
    d2 = date2.spilt(split)
    map(lambda x: int(x), d1)
    map(lambda x: int(x), d2)
    dateObj1 = date(d1[0], d1[1], d1[2])
    dateObj2 = date(d2[0], d2[1], d2[2])
    if equal:
        return dateObj1 <= dateObj2
    return dateObj1 < dateObj2


def str_to_date(s, separator='-'):
    d = s.split(separator)
    d = list(map(lambda x: int(x), d))
    return date(d[0], d[1], d[2])