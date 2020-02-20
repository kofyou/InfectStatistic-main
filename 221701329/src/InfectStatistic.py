"""
 * InfectStatistic
 * TODO
 *
 * @author liulaoc
 * @version 1
 * @since 1
"""
import Lib.py
import os.path
from pathlib import Path
from sys import argv

couldRun = false
if len(argv) >=6:
    couldRun = true
if argv[1] == "list":
    couldRun = true
else:
    couldRun = false
checkCount=0
if couldRun:
    for i in len(argv-1):
        if argv[i] == "-log":
        if argv[i] == "-out":
        if argv[i] == "-date":
        if argv[i] == "-type":
        if argv[i] == "-province":

    file = Path(argv[4])
    file = file[:-1]
    couldRun = false
    if file.is_dir():
        if file.exists():
            couldRun = true

    couldRun = false
    if dicFile.is_dir():
        if dicFile.exists():
            couldRun = true
if couldRun:
    dataCenter = DataCenter.__init__(argv[2],argv[4],argv[6])
    dataCenter.DealAllData()
    resultStr = OutputStatistic()
else:
    print("输入格式错误")


class CmdCenter:
    LogPath=""
    OutPath=""
    Date=""
    Type=""
    Province=""
    def __init__(self):
        pass
    def DealCmd(self,argv):




class DataCenter:
    def __init__(self, dateTime, logPath, outPath):
        self.__private_date = datetime.datetime.strptime(dateTime, '%Y-%m-%d').date()
        self.__private_date -= 1
        self.__private_path = logPath
        self.__private_outPath = outPath
        self.__private_allData.setdefault("1", 1)
        self.__private_allData.clear()
        pass
    __private_outPath
    __private_path
    __private_date
    __private_allData

    def DealAllData(self):
        fileName = self.__private_date.strftime("%Y%m%d") + ".log.txt"
        allFile = os.listdir(path)
        fileCount = len(allFile)
        for nowFile in allFile:
            nowDateStr = nowFile[:-8]
            nowDate = str2date(nowDateStr)
            diffDate = self.__private_date - nowDate
            if diffDate>0:
                self.DealFileData(nowFile)
            pass
        pass

    # 用于处理一天的数据
    def DealFileData(self, fileName):
        filePath = self.__private_path + "/" + fileName
        obj = open(filePath, mode='r')
        fileContent = getFileContent(filep)
        allStr = fileContent.split('\n')
        for str in allStr:
            self.DealOneLineData(str)
        obj.close()
        pass

    def DealOneLineData(self,str):
        if isIgnoreLine(str):
            return
        words = str.split(" ")
        if len(words) >4:
            province_1 = words[0]
            if not self.__private_allData.hasKey(province_1):
                obj = ProvinceData()
                self.__private_allData.setdefault(province_1, obj)
            province_2 = words[3]
            if not self.__private_allData.hasKey(province_2):
                obj = ProvinceData()
                self.__private_allData.setdefault(province_2,obj)
            count = words[len(words) - 1]
            if words[1] == "感染患者":
                self.private_allData[province_1].InfectionCount -= count
                self.private_allData[province_2].InfectionCount += count
            if words[2] == "疑似患者":
                self.private_allData[province_1].UncertainCount -= count
                self.private_allData[province_2].UncertainCount += count
        else:
            province_1 = words[0]
            if not self.__private_allData.hasKey(province_1):
                obj = ProvinceData()
                self.__private_allData.setdefault(province_1, obj)
            count = words[len(words) - 1]
            if words[1] == "死亡":
                self.private_allData[province_1].DieCount += count
            if words[1] == "治愈":
                self.private_allData[province_1].CureCount += count
                self.private_allData[province_1].InfectionCount -= count
            if words[1] == "新增":
                if words[2] == "感染患者":
                    self.private_allData[province_1].InfectionCount += count
                if words[2] == "疑似患者":
                    self.private_allData[province_1].UncertainCount += count
            if words[1] == "疑似患者":
                self.private_allData[province_1].UncertainCount -= count
                self.private_allData[province_1].InfectionCount += count
            if words[1] == "排除":
                self.private_allData[province_1].UncertainCount -= count

    def OutputStatistic(self):
        pass

# 枚举类，用来表明数据类型
class InfluenceType:
    def __init__(self):
        pass

    # 感染、疑似、治愈、死亡
    Infection = 0
    Uncertain = 1
    Cure = 2
    Die = 3


# 用于存储各个省份的数据
class ProvinceData:
    def __init__(self):
        pass

    InfectionCount = 0
    UncertainCount = 0
    CureCount = 0
    DieCount = 0

# 文件操作-判断一行字符串是否被忽略
def isIgnoreLine(str):
    if str.Count <=2:
        return true
    else:
        if str[0] == '/' and str[1]=='/':
            return true
    return false

verbDic_Level1 = { "排除":2, "死亡":3, "治愈":4}
verbDic_Level2 = {"新增":1, "流入":2, "排除":3}
def isVerbWithLevel(str):
    if str in verbDic_Level1.keys():
        return 1
    if str in verbDic_Level2.keys():
        return 2
    pass

def isProvince(str):
    pass

def isPeopleType(str):
    pass

def isNum(str):
    pass