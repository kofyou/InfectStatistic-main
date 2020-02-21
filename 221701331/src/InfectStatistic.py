import re
import sys
import os
import argparse
# from pypinyin import lazy_pinyin
from Lib import parse_output_line, str_to_date

SORTED_PROVENCE_NAME = ["安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"]
EMPTY_DATA = {'infected': 0, 'suspect': 0, 'cure': 0, 'death': 0}


class Statistic:
    def __init__(self):
        self.data = {}

    def change_infected(self, province, amount):
        self.data.setdefault(province, {**EMPTY_DATA})
        self.data[province]['infected'] += amount

    def change_suspect(self, province, amount):
        self.data.setdefault(province, {**EMPTY_DATA})
        self.data[province]['suspect'] += amount

    def add_death(self, province, amount):
        self.data.setdefault(province, {**EMPTY_DATA})
        self.data[province]['death'] += amount

    def add_cure(self, province, amount):
        self.data.setdefault(province, {**EMPTY_DATA})
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
        return {key: total(key) for key in ['infected', 'suspect', 'cure', 'death']}


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


if __name__ == '__main__':
    parser = argparse.ArgumentParser(prog='InfectStatistic', description='show statistic of epidemic data')
    parser.add_argument('option', choices=['list'], type=str, help='only "list" support')
    parser.add_argument('-log', type=str, required=True, help='*directory* that contain logs file')
    parser.add_argument('-out', type=argparse.FileType('w', encoding='utf8'), required=True,
                        help='*file* path that this script output')
    parser.add_argument('-date', type=str, help='real time data until this param, format YYYY-mm-DD')
    parser.add_argument('-type', type=str, nargs='*', choices=['ip', 'sp', 'cure', 'dead'], help='type(s) to output')
    parser.add_argument('-province', type=str, nargs='*', help='which province(s) to display, input "全国" if needs sum')
    option = parser.parse_args()

    # ======= 检查命令行参数 =======
    # check -log
    try:
        if option.log and len(os.listdir(option.log)) == 0:
            sys.stderr.write('log directory ({0}) is empty'.format(option.log))
            exit(1)
    except FileNotFoundError as e:
        sys.stderr.write('cannot find log directory {0}'.format(option.log))
        exit(1)
    # check -date
    if option.date and not re.match(r'^\d{4}-\d{2}-\d{2}$', option.date):
        sys.stderr.write('argument -date is invalid: ' + option.date + '\nformat must be YYYY-mm-DD')
        exit(1)
    # check -province
    # if option.province and len(set(SORTED_PROVENCE_NAME + ['全国'] - set(option.province)):  # 集合方法求差集
    wrongInputProvinces = [x for x in (option.province or []) if x not in SORTED_PROVENCE_NAME + ['全国']]
    if option.province and len(wrongInputProvinces):
        sys.stderr.write('province name invalid: {0}'.format(wrongInputProvinces))
        exit(1)
    # check -type
    if option.type and len(option.type) != len(set(option.type)):
        sys.stderr.write('type repeat!')
        exit(1)

    # ======= 日志文件处理 ========
    stat = Statistic()
    filesTuple = os.walk(option.log).__next__()
    basePath = filesTuple[0] + '\\'
    # 扫描目录下的所有文件
    for filename in filesTuple[2]:
        # 若指定日期，则按参数中止循环，否则处理完所有文件
        # if option.date and filename.find(option.date) > -1:
        if option.date and str_to_date(filename[:10]) > str_to_date(option.date):
            break
        with open(basePath + filename, encoding='utf8') as fr:
            # 处理文件的每一行
            for line in fr:
                if line[0] == line[1] == '/':
                    continue
                if line[len(line) - 1] == '\n':
                    line = line[:-1]
                stat.parse_record_line(line)
    if option.date and str_to_date(filesTuple[2][-1][:10]) < str_to_date(option.date):
        sys.stderr.write('warning: 日期超出范围\n')
    stat.data['全国'] = stat.get_total()

    # ======== 输出数据处理 ========
    output = ''
    data = {}
    if option.province:
        for p in option.province:
            data[p] = stat.data[p] if p in stat.data.keys() else {**EMPTY_DATA}
    else:
        data = {**stat.data}  # todo:这里应该不是深拷贝
    # sortedProvince = sorted(list(data.keys()), key=lambda x: lazy_pinyin(x[0]))
    # 排除type参数不包含的类型
    if option.type:
        mapList = []
        new_data = {}
        for t in option.type:
            if t == 'ip':
                mapList.append('infected')
            elif t == 'sp':
                mapList.append('suspect')
            elif t == 'dead':
                mapList.append('death')
            else:
                mapList.append('cure')
        for k, prov in data.items():
            new_data[k] = {}
            for t in mapList:
                new_data[k][t] = prov[t]
        data = new_data
    # 生成输出文本
    if not option.province or '全国' in option.province:
        output += parse_output_line('全国', data)
    for prov in SORTED_PROVENCE_NAME:
        output += parse_output_line(prov, data)
    output += '// 该文档并非真实数据，仅供测试使用\n' \
              '// 命令：' + ' '.join(sys.argv[1:]) + '\n'
    # 写入out参数指定的文件
    option.out.write(output)
    print(output)
    option.out.close()
