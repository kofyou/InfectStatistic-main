"""
 * Lib
 * TODO
 *
 * @author liulaoc
 * @version 1
 * @since 1
"""
import datetime
import threading
import os
import sys


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


# 时间与str互转
def str2date(str, date_format="%Y%m%d"):
    date = datetime.datetime.strptime(str, date_format)
    return date


# 文件操作-获取文件全部内容
def getFileContent(path):
    with open(path) as file_object:
        contends = file_object.read()
    return contends
