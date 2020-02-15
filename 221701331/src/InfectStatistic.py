# encoding=utf8
import re
import os
import argparse
# fw = open('test.output.txt', 'w', encoding='utf8')


class Statistic:
    def __init__(self):
        self.data = {}

    def changeInfected(self, province, amount):
        self.data.setdefault(province, {'infected': 0, 'suspect': 0, 'death': 0, 'cure': 0})
        self.data[province]['infected'] += amount

    def changeSuspect(self, province, amount):
        self.data.setdefault(province, {'infected': 0, 'suspect': 0, 'death': 0, 'cure': 0})
        self.data[province]['suspect'] += amount

    def addDeath(self, province, amount):
        self.data.setdefault(province, {'infected': 0, 'suspect': 0, 'death': 0, 'cure': 0})
        self.data[province]['suspect'] += amount

    def addCure(self, province, amount):
        self.data.setdefault(province, {'infected': 0, 'suspect': 0, 'death': 0, 'cure': 0})
        self.data[province]['suspect'] += amount

    def parseRecordLine(self, infoStr: str):
        keywords = infoStr.split(' ')
        count = int(re.findall(r'\d+', keywords[-1])[0])
        province = keywords[0]
        province2 = keywords[3] if len(keywords) >= 4 else None  # 若没有第二个省份则该变量无意义
        switch = {
            1: lambda: self.changeInfected(province, count),  # 新增确诊
            2: lambda: self.changeSuspect(province, count),  # 新增疑似
            3: lambda: (self.changeInfected(province, -count), self.changeInfected(province2, count)),  # 确诊流动
            4: lambda: (self.changeSuspect(province, -count), self.changeSuspect(province2, count)),  # 感染流动
            5: lambda: (self.addDeath(province, count), self.changeInfected(province, -count)),  # 死亡
            6: lambda: (self.addCure(province, count), self.changeInfected(province, -count)),  # 治愈
            7: lambda: (self.changeSuspect(province, -count), self.changeInfected(province, count)),  # 疑似->确诊
            8: lambda: self.changeSuspect(province, -count)  # 排除疑似
        }
        switch[getInfoType(infoStr)]()

    def getTotal(self):
        def total(key): return sum(map(lambda x: x[key], self.data.values()))
        return {key: total(key) for key in ['infected', 'suspect', 'death', 'cure']}

    def getByType(self, type):
        pass

    def getByProvince(self, province):
        return self.data[province]


def getInfoType(s: str) -> int:
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


# stat = Statistic()
#
# filesTuple = os.walk(r'..\log').__next__()
# basePath = filesTuple[0] + '\\'
# for filename in filesTuple[2]:
#     with open(basePath + filename, encoding='utf8') as fr:
#         for line in fr:
#             if line[0] == line[1] == '/':
#                 continue
#             if line[len(line) - 1] == '\n':
#                 line = line[:-1]
#             stat.parseRecordLine(line)
#
# print(stat.data)
parser = argparse.ArgumentParser(prog='InfectStatistic', description='show statistic of epidemic data')
parser.add_argument('option', choices=['list'], type=str, help='only "list" support')
parser.add_argument('-log', type=str, required=True, help='*directory* that contain logs file')
parser.add_argument('-out', type=argparse.FileType('w'), required=True, help='*file* path that this script output')
parser.add_argument('-date', type=str, help='real time data until this param, format YYYY-mm-DD')
parser.add_argument('-type', type=str, nargs='*', choices=['ip', 'sp', 'cure', 'dead'], help='type to output')
parser.add_argument('-province', type=str, nargs='*', help='which province(s) to display, input "全国" if needs sum')

option = parser.parse_args()

print(option)
