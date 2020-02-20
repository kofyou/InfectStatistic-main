"""
 * Lib
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
"""
import datetime
import threading


# 单例基类
class Singleton(object):
    def __init__(self):
        pass
    _instance_lock = threading.Lock()

    def __new__(cls, *args, **kwargs):
        if not hasattr(Singleton, "_instance"):
            with Singleton._instance_lock:
                if not hasattr(Singleton, "_instance"):
                    Singleton._instance = object.__new__(cls)
        return Singleton._instance


class DataCenter(Singleton):
    def __init__(self):
        self.__private_dateTime = 0
        pass
    __private_date

    def DataInit(self, dateTime):
        self.__private_dateTime = datetime.datetime.strptime(dateTime, '%Y-%m-%d').date()
        self.__private_dateTime -= 1


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
