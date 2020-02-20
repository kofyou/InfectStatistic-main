#  -*- coding:utf-8 -*-
#  !/usr/bin/python

# 
#  InfectStatistic
#  TODO 读取文件，列出全国和各省在某日的感染情况，输出到输出文件中
#  @author 091700410 傅少华
#  @version 1.5
#  @since 2020-2-18
#  

import os

import datetime

import sys

import time


def sort_by_pinyin(province):
    #  用于按拼音排序各省
    province_dict = {
        '全国': 0, '安徽': 1, '澳门': 2,
        '北京': 3, '重庆': 4, '福建': 5,
        '甘肃': 6, '广东': 7, '广西': 8,
        '贵州': 9, '海南': 10, '河北': 11,
        '河南': 12, '黑龙江': 13, '湖北': 14,
        '湖南': 15, '吉林': 16, '江苏': 17,
        '江西': 18, '辽宁': 19, '内蒙古': 20,
        '宁夏': 21, '青海': 22, '山东': 23,
        '山西': 24, '陕西': 25, '上海': 26,
        '四川': 27, '台湾': 28, '天津': 29,
        '香港': 30, '西藏': 31, '新疆': 32,
        '云南': 33, '浙江': 34
    }
    
    return province_dict[province]


def check_command(args):

    if '-log' not in args:
        log_error = '未指定日志目录的位置'
        print('未指定日志目录的位置')
        return log_error

    if '-out' not in args:
        out_error = '未指定输出文件的目录和文件名'
        print('未指定输出文件的目录和文件名')
        return out_error

    if '-log' in args and '-out' in args:
        cl = CommandHandler(args)
        cl.paras()
        fi = DataHandler(cl.input_path, cl.output_path, cl.input_date)
        fi.read_file()
        fi.write_file(cl.type_list, cl.province_list)

    return '运行成功'

class DataHandler:
    #  从日志读取数据，处理后输出到目标文件

    data_dict = {}
    #  用于存放数据的嵌套字典，字典的键为省份名，值为记录各类型对应人数的字典，其键为类型，其值为对应的人数
    
    def __init__(self, input_path, output_path, file_date):
        
        self.input_path = input_path
        self.output_path = output_path
        if len(file_date) == 0:
            self.file_date = '1970-01-01'
            #  将input_date设为不可能大于任何日志日期的日期，这样若没有日期输入，则日期会在DataHandler类的read_file函数中被设为日志最新的一天
        else:
            self.file_date = file_date
        self.data_dict = {'全国': {'感染患者': 0, '疑似患者': 0, '治愈': 0, '死亡': 0}}
    
    def dict_add_items(self, province):
        self.data_dict.update({province: {'感染患者': 0, '疑似患者': 0, '治愈': 0, '死亡': 0}})
    
    def file_data(self, input_list):
        #  处理文档中的数据
        
        province = input_list[0]
        
        if province not in self.data_dict:
            self.dict_add_items(province)
            #  若省份不在字典中，则以省份名为键新建元素并添加到字典中
            
        if input_list[1] == '新增' or input_list[1] == '排除':
            type = input_list[2]
            num = int(str(input_list[3]).replace('人', ''))
            if input_list[1] == '新增':
                self.data_dict[province][type] += num
                self.data_dict['全国'][type] += num
            else:
                self.data_dict[province][type] -= num
                self.data_dict['全国'][type] -= num
        
        if input_list[1] == '死亡' or input_list[1] == '治愈':
            num = int(str(input_list[2]).replace('人', ''))
            self.data_dict[province][input_list[1]] += num
            self.data_dict[province]['感染患者'] -= num
            self.data_dict['全国'][input_list[1]] += num
            self.data_dict['全国']['感染患者'] -= num
            
        if input_list[2] == '流入':
            num = int(str(input_list[4]).replace('人', ''))
            type = input_list[1]
            self.data_dict[province][type] -= num
            self.data_dict[input_list[3]][type] += num
        
        if input_list[2] == '确诊感染':
            num = int(str(input_list[3]).replace('人', ''))
            self.data_dict[province]['感染患者'] += num
            self.data_dict[province]['疑似患者'] -= num
            self.data_dict['全国']['感染患者'] += num
            self.data_dict['全国']['疑似患者'] -= num
            
    def read_file(self):
        #  读取日志
        
        try:
            date_of_file = datetime.datetime.strptime(self.file_date, '%Y-%m-%d')
        except ValueError as identifier:
            print('错误：日期输入错误!')
            return '错误：日期输入错误!'
        
        if not os.path.exists(self.input_path):
            print('错误：日志路径不存在!')
            return '错误：日志路径不存在!'
        
        for root, dirs, files in os.walk(self.input_path):
            #  读取指定日期及之前的日志的数据，若无指定日期，则将指定日期设为日志最新的一天
            if self.file_date == ('1970-01-01'):
                for file in files:
                    if date_of_file <= datetime.datetime.strptime(file.replace('.log.txt', ''), '%Y-%m-%d'):
                        date_of_file = datetime.datetime.strptime(file.replace('.log.txt', ''), '%Y-%m-%d')
                self.file_date = datetime.datetime.strftime(date_of_file,'%Y-%m-%d')
                
            for file in files:
                if date_of_file >= datetime.datetime.strptime(file.replace('.log.txt', ''), '%Y-%m-%d'):
                    f = open(self.input_path + file, 'r', encoding='utf-8')
                    for line in f:
                        if line[0:2] == '//':
                            pass
                        else:
                            content = line.split()
                            self.file_data(content)
                    f.close()
    
    def write_file(self, type_list=[], province_list=[]):
        #  输出到文件
        
        f = open(self.output_path, 'w+', encoding='utf-8')
        
        return_str = ''
        
        if len(type_list) == 0:
            type_list = ['感染患者', '疑似患者', '治愈', '死亡']
        
        for i in range(len(type_list)):
            if type_list[i] == 'ip':
                type_list[i] = '感染患者'
            elif type_list[i] == 'sp':
                type_list[i] = '疑似患者'
            elif type_list[i] == 'cure':
                type_list[i] = '治愈'
            elif type_list[i] == 'dead':
                type_list[i] = '死亡'
        
        if len(province_list) == 0:
            for i in range(35):
                for m in self.data_dict:
                    if sort_by_pinyin(m) == i:
                        output_str = str(m)
                        for n in type_list:
                            output_str = output_str + ' ' + n + str(self.data_dict[m][n]) + '人'
                        output_str += '\n'
                        return_str += output_str
                        f.writelines(output_str)
        else:
            for province in province_list:
                if province not in self.data_dict:
                    self.dict_add_items(province)
            for i in range(35):
                for m in self.data_dict:
                    if sort_by_pinyin(m) == i and m in province_list:
                        output_str = str(m)
                        for n in type_list:
                            output_str = output_str + ' ' + n + str(self.data_dict[m][n]) + '人'
                        output_str += '\n'
                        return_str += output_str
                        f.writelines(output_str)
        
        f.writelines('// 该文档并非真实数据，仅供测试使用')

        f.close()

        print(return_str)

        return return_str
                        

class CommandHandler:
    #  处理命令行参数
    
    input_date = ''
    
    input_path = ''
    
    output_path = ''
    
    type_list = []
    
    province_list = []
    
    def __init__(self, args):
        
        self.args = args

    def paras(self):
        #  处理命令行参数
      
        index = 0
        
        for arg in self.args:
            if arg == '-log':
                self.input_path = self.args[index + 1]
            if arg == '-out':
                self.output_path = self.args[index + 1]
            if arg == '-date':
                self.input_date = self.args[index + 1]
            if arg == '-type':
                self.set_type_list(index)
            if arg == '-province':
                self.set_province_list(index)
            index += 1
                
    def set_type_list(self, index):
        
        for i in range(index + 1, len(self.args)):
            if self.args[i][0] != '-':
                if self.args[i] not in ['ip', 'sp', 'cure', 'dead']:
                    print('错误：输入的类型不存在!')
                    return '错误：输入的类型不存在!'
                    
                self.type_list.append(self.args[i])
            else:
                break
            
    def set_province_list(self, index):
        
        for i in range(index + 1, len(self.args)):
            if self.args[i][0] != '-':
                try:
                    sort_by_pinyin(self.args[i])
                except KeyError as identifier:
                    print('错误：输入的省份不存在!')
                    return '错误：输入的省份不存在!'
                self.province_list.append(self.args[i])
            else:
                break


if __name__ == "__main__":
    if len(sys.argv) >= 2 and sys.argv[1] == 'list':
        check_command(sys.argv[2:])