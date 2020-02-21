"""
 * InfectStatistic
 * TODO
 *
 * @author liulaoc
 * @version 1
 * @since 1
"""
from datetime import datetime, date
from datetime import timedelta
import re
import threading
import os
import sys
import os.path
from pathlib import Path
from sys import argv


class CmdCenter:
    LogPath = ""
    OutPath = ""
    Date = ""
    Types = []
    Provinces = []

    def __init__(self):
        pass

    def DealCmd(self, argvs):
        if len(argvs) < 6:
            return False
        if argvs[1] != "list":
            return False
        checkCount = 0
        for i in range(2, len(argvs)-1):
            if argvs[i] == "-log":
                checkCount += 1
                CmdCenter.LogPath = argvs[i+1]
            if argvs[i] == "-out":
                checkCount += 1
                CmdCenter.OutPath = argvs[i + 1]
            if argvs[i] == "-date":
                CmdCenter.Date = argvs[i+1]
            if argvs[i] == "-type":
                for k in range(i+1,len(argvs-1)):
                    if argvs[k][0] == '-':
                        break
                    else:
                        CmdCenter.Types.append(argvs[k])
            if argvs[i] == "-province":
                for k in range(i+1,len(argvs-1)):
                    if argvs[k][0] == '-':
                        break
                    else:
                        CmdCenter.Provinces.append(argvs[k])
        if checkCount < 2:
            return False
        else:
            return True


class DataCenter:
    __private_outPath=""
    __private_path=""
    __private_date=""
    __private_allData={}

    def __init__(self, dateTime, logPath, outPath):
        self.__private_date = datetime.date(datetime.strptime(dateTime, '%Y-%m-%d'))
        # self.__private_date = self.__private_date + date.timedelta(days=-1).strftime("%Y-%m-%d").date()
        self.__private_path = logPath
        self.__private_outPath = outPath

    def DealAllData(self):
        allFile = os.listdir(self.__private_path)
        for nowFile in allFile:
            nowDateStr = nowFile[:-8]
            nowDate = str2date(nowDateStr)
            diffDate = self.__private_date - nowDate
            if diffDate.total_seconds()>0:
                self.DealFileData(nowFile)

    # 用于处理一天的数据
    def DealFileData(self, fileName):
        filePath = self.__private_path + "/" + fileName
        obj = open(filePath, mode='r')
        fileContent = getFileContent(filePath)
        allStr = fileContent.split('\n')
        for str in allStr:
            self.DealOneLineData(str)
        obj.close()
        pass

    def DealOneLineData(self, lineContent):
        if isIgnoreLine(lineContent):
            return
        words = lineContent.split(" ")
        if len(words) > 4:
            province_1 = words[0]
            if province_1 not in self.__private_allData:
                obj = ProvinceData()
                self.__private_allData.setdefault(province_1, obj)
            province_2 = words[3]
            if province_2 not in self.__private_allData:
                obj = ProvinceData()
                self.__private_allData.setdefault(province_2, obj)
            count = int(content2Eng(words[len(words) - 1]))
            if words[1] == "感染患者":
                self.__private_allData[province_1].__dict__['InfectionCount'] -= count
                self.__private_allData[province_2].__dict__['InfectionCount'] += count
            if words[2] == "疑似患者":
                self.__private_allData[province_1].__dict__['UncertainCount'] -= count
                self.__private_allData[province_2].__dict__['UncertainCount'] += count
        else:
            province_1 = words[0]
            if province_1 not in self.__private_allData:
                obj = ProvinceData()
                self.__private_allData.setdefault(province_1, obj)
            count = int(content2Eng(words[len(words) - 1]))
            if words[1] == "死亡":
                self.__private_allData[province_1].__dict__['DieCount'] += count
            if words[1] == "治愈":
                self.__private_allData[province_1].__dict__['CureCount'] += count
                self.__private_allData[province_1].__dict__['InfectionCount'] -= count
            if words[1] == "新增":
                if words[2] == "感染患者":
                    self.__private_allData[province_1].__dict__['InfectionCount'] += count
                if words[2] == "疑似患者":
                    self.__private_allData[province_1].__dict__['UncertainCount'] += count
            if words[1] == "疑似患者":
                self.__private_allData[province_1].__dict__['UncertainCount'] -= count
                self.__private_allData[province_1].__dict__['InfectionCount'] += count
            if words[1] == "排除":
                self.__private_allData[province_1].UncertainCount -= count

    def OutputStatistic(self, types, provinces):
        output = ""
        if len(types) == 0:
            types.append("ip")
            types.append("sp")
            types.append("cure")
            types.append("dead")
        if "全国" in provinces:
            output += self.CountryInfo()
        if len(provinces) == 0:
            provinces = self.__private_allData.keys()
            output += self.CountryInfo()
        for province in provinces:
            if province in self.__private_allData:
                output += province + " "
                for nowType in types:
                    # 感染
                    if nowType == "ip":
                        output += "感染患者"
                        output += str(self.__private_allData[province].__dict__['InfectionCount'])
                        output += "人"
                    # 疑似
                    if nowType == "sp":
                        output += "疑似患者"
                        output += str(self.__private_allData[province].__dict__['UncertainCount'])
                        output += "人"
                    # 治愈
                    if nowType == "cure":
                        output += "治愈"
                        output += str(self.__private_allData[province].__dict__['CureCount'])
                        output += "人"
                    # 死亡
                    if nowType == "dead":
                        output += "死亡"
                        output += str(self.__private_allData[province].__dict__['DieCount'])
                        output += "人"
                    output += " "
                output += "\n"
        output += "// 该文档并非真实数据，仅供测试使用"
        return output

    def CountryInfo(self):
        output = ""
        ip = 0
        sp = 0
        cure = 0
        dead = 0
        for province in self.__private_allData.keys():
            ip += self.__private_allData[province].__dict__['InfectionCount']
            sp += self.__private_allData[province].__dict__['UncertainCount']
            cure += self.__private_allData[province].__dict__['CureCount']
            dead += self.__private_allData[province].__dict__['DieCount']
        output += "全国 "
        output += "感染患者{0}人".format(str(ip))
        output += "疑似患者{0}人".format(str(sp))
        output += "治愈{0}人".format(str(cure))
        output += "死亡{0}人".format(str(dead))
        output += "\n"
        return output


# 用于存储各个省份的数据
class ProvinceData:
    def __init__(self):
        self.InfectionCount = 0
        self.UncertainCount = 0
        self.CureCount = 0
        self.DieCount = 0
        pass


# 文件操作-判断一行字符串是否被忽略
def isIgnoreLine(str):
    if len(str) <= 2:
        return True
    else:
        if str[0] == '/' and str[1] == '/':
            return True
    return False


# 时间与str互转
def str2date(current_str, date_format="%Y-%m-%d"):
    current_str.replace(" ", "")
    current_date = datetime.strptime(current_str, date_format).date()
    return current_date


# 文件操作-获取文件全部内容
def getFileContent(path):
    with open(path, 'r', encoding='utf-8') as file_object:
        contends = file_object.read()
    return contends


def content2Eng(file):
    pattern = re.compile(r'[\u4e00-\u9fa5]')
    english = re.sub(pattern, '', file)
    return english


cmdCenter = CmdCenter()
couldRun = CmdCenter.DealCmd(cmdCenter, argv)
# print(CmdCenter.LogPath)
if couldRun:
    dataCenter = DataCenter(CmdCenter.Date, CmdCenter.LogPath, CmdCenter.OutPath)
    DataCenter.DealAllData(dataCenter)
    resultStr = DataCenter.OutputStatistic(dataCenter, CmdCenter.Types, CmdCenter.Provinces)
    f = open(CmdCenter.OutPath, 'w')
    f.write(resultStr)
    print("成功")
else:
    print("输入格式错误")