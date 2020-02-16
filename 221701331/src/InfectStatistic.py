# encoding=utf8
import re
import sys
import os
import argparse
from pypinyin import lazy_pinyin

PROVENCE_NAME = ["陕西", "甘肃", "青海", "宁夏", "新疆", "北京", "天津", "上海", "重庆", "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "台湾", "内蒙古", "广西", "西藏", "香港", "澳门"]
TYPE_CN_MAP = {'infected': '感染患者', 'suspect': '疑似患者', 'cure': '治愈', 'death': '死亡'}


class Statistic:
    def __init__(self):
        self.data = {}

    def change_infected(self, province, amount):
        self.data.setdefault(province, {'infected': 0, 'suspect': 0, 'death': 0, 'cure': 0})
        self.data[province]['infected'] += amount

    def change_suspect(self, province, amount):
        self.data.setdefault(province, {'infected': 0, 'suspect': 0, 'death': 0, 'cure': 0})
        self.data[province]['suspect'] += amount

    def add_death(self, province, amount):
        self.data.setdefault(province, {'infected': 0, 'suspect': 0, 'death': 0, 'cure': 0})
        self.data[province]['death'] += amount

    def add_cure(self, province, amount):
        self.data.setdefault(province, {'infected': 0, 'suspect': 0, 'death': 0, 'cure': 0})
        self.data[province]['cure'] += amount

    def parse_record_line(self, info_str: str):
        keywords = info_str.split(' ')
        count = int(re.findall(r'\d+', keywords[-1])[0])
        province = keywords[0]
        province2 = keywords[3] if len(keywords) >= 4 else None  # 若没有第二个省份则该变量无意义
        switch = {
            1: lambda: self.change_infected(province, count),  # 新增确诊
            2: lambda: self.change_suspect(province, count),  # 新增疑似
            3: lambda: (self.change_infected(province, -count), self.change_infected(province2, count)),  # 确诊流动
            4: lambda: (self.change_suspect(province, -count), self.change_suspect(province2, count)),  # 感染流动
            5: lambda: (self.add_death(province, count), self.change_infected(province, -count)),  # 死亡
            6: lambda: (self.add_cure(province, count), self.change_infected(province, -count)),  # 治愈
            7: lambda: (self.change_suspect(province, -count), self.change_infected(province, count)),  # 疑似->确诊
            8: lambda: self.change_suspect(province, -count)  # 排除疑似
        }
        switch[map_info_type(info_str)]()

    def get_total(self):
        def total(key): return sum(map(lambda x: x[key], self.data.values()))
        return {key: total(key) for key in ['infected', 'suspect', 'death', 'cure']}


def map_info_type(s: str) -> int:
    """
    根据s进行数据类型分类
    :param s: 从log中读取的字符串数据
    :return: 数据类型编号，与举例一致
    """
    keywords = s.split(' ')
    if keywords[1] == '新增':
        if keywords[2] == '感染患者':
            return 1
        else:
            return 2
    if keywords[1] == '感染患者':
        return 3
    if keywords[1] == '疑似患者':
        if keywords[2] == '流入':
            return 4
        else:
            return 7
    if keywords[1] == '死亡':
        return 5
    if keywords[1] == '治愈':
        return 6
    if keywords[1] == '排除':
        return 8


parser = argparse.ArgumentParser(prog='InfectStatistic', description='show statistic of epidemic data')
parser.add_argument('option', choices=['list'], type=str, help='only "list" support')
parser.add_argument('-log', type=str, required=True, help='*directory* that contain logs file')
parser.add_argument('-out', type=argparse.FileType('w'), required=True, help='*file* path that this script output')
parser.add_argument('-date', type=str, help='real time data until this param, format YYYY-mm-DD')
parser.add_argument('-type', type=str, nargs='*', choices=['ip', 'sp', 'cure', 'dead'], help='type(s) to output')
parser.add_argument('-province', type=str, nargs='*', help='which province(s) to display, input "全国" if needs sum')
option = parser.parse_args()

# ======= 检查命令行参数 =======
# check -date
if option.date and not re.match(r'\d{4}-\d{2}-\d{2}', option.date):
    sys.stderr.write('argument DATE is invalid: ' + option.date + '\nformat must be YYYY-mm-DD')
    exit(1)

# check -log
try:
    if option.log and len(os.listdir(option.log)) == 0:
        sys.stderr.write('log directory ({0}) is empty'.format(option.log))
        exit(1)
except FileNotFoundError as e:
    sys.stderr.write('cannot find log directory {0}'.format(option.log))
    exit(1)

# check -province
# if option.province and option.province not in PROVENCE_NAME + ['全国']:
# if option.province and len([x for x in (PROVENCE_NAME + ['全国']) if x in option.province]) == len(option.province):
#     sys.stderr.write('province name invalid: {0}'.format(option.province))
#     exit(1)

# ======= 日志文件处理 ========
stat = Statistic()
filesTuple = os.walk(option.log).__next__()
basePath = filesTuple[0] + '\\'
# 扫描目录下的所有文件
for filename in filesTuple[2]:
    with open(basePath + filename, encoding='utf8') as fr:
        # 处理文件的每一行
        for line in fr:
            if line[0] == line[1] == '/':
                continue
            if line[len(line) - 1] == '\n':
                line = line[:-1]
            stat.parse_record_line(line)
    # 若指定日期，则按参数中止循环，否则处理完所有文件
    if option.date and filename.find(option.date) > -1:
        break
stat.data['全国'] = stat.get_total()

# ======== 输出数据处理 ========
output = ''
data = {}
if option.province:
    # data = list(filter(lambda value, key: key in option.province, data.items()))
    for k, v in stat.data.items():
        if k in option.province:
            data[k] = v
if option.type:
    mapList = []
    for t in option.type:
        if t == 'ip':
            t = 'infected'
        elif t == 'sp':
            t = 'suspect'
        elif t == 'dead':
            t = 'death'
    for item in data:
        map(lambda x: {x[0]: x[1] for x[0] in option.type}, item.items())
        filter(lambda x: x in option.type, item.keys())

print(data)
