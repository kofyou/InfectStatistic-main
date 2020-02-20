import os
from datetime import datetime

import argparse


def init():  # 初始化
    provinces = ["河南", "河北", "北京", "天津", "山东", "广东",
                 "山西", "黑龙江", "吉林", "辽宁", "浙江", "香港",
                 "江苏", "上海", "安徽", "江西", "湖南", "福建", "澳门",
                 "湖北", "新疆", "云南", "贵州", "台湾", "宁夏", "西藏",
                 "四川", "重庆", "内蒙古", "广西", "海南", "青海", "甘肃", "陕西"]  
    output_data = {}

    output_data["全国"] = {
        "死亡": 0,
        "治愈": 0,
        "疑似": 0,
        "感染": 0,
    }
    for province in provinces:
        output_data[province] = {
            "死亡": 0,
            "治愈": 0,
            "疑似": 0,
            "感染": 0,
        }
    return output_data


def getFiles(date, dir):  # 获取文件夹下的所有文件，并筛选出指定日期之前的文件
    todo_files = []
    files = os.listdir(dir)
    # print(files)

    for file in files:
        datetime_date = datetime.strptime(file.replace(".log.txt", ""), "%Y-%m-%d")

        if datetime_date <= datetime.strptime(date, "%Y-%m-%d"):
            todo_files.append(file)
    return todo_files


def processFiles(file, output_data, dir):  # 处理，汇总数据
    f = open(os.path.join(dir, file), "r", encoding="utf-8")
    raw_data = f.readlines()
    for d in raw_data:
        ld = str(d).replace("人", "").replace("\n", "").split(" ")
        print(ld)
        if ld[1] == "新增":
            if ld[2] == "感染患者":
                output_data[ld[0]]["感染"] += int(ld[-1])
                output_data["全国"]["感染"] += int(ld[-1])
            elif ld[2] == "疑似患者":
                output_data[ld[0]]["疑似"] += int(ld[-1])
                output_data["全国"]["疑似"] += int(ld[-1])
        elif ld[1] == "感染患者":
            output_data[ld[0]]["感染"] -= int(ld[-1])
            output_data[ld[3]]["感染"] += int(ld[-1])
        elif ld[1] == "疑似患者":
            if ld[2] == "确诊感染":
                output_data[ld[0]]["疑似"] -= int(ld[-1])
                output_data[ld[0]]["感染"] += int(ld[-1])
                output_data["全国"]["感染"] += int(ld[-1])
            elif ld[2] == "流入":
                output_data[ld[0]]["疑似"] -= int(ld[-1])
                output_data[ld[3]]["疑似"] += int(ld[-1])
        elif ld[1] == "死亡":
            output_data[ld[0]]["死亡"] += int(ld[-1])
            output_data["全国"]["死亡"] += int(ld[-1])
        elif ld[1] == "治愈":
            output_data[ld[0]]["治愈"] += int(ld[-1])
            output_data["全国"]["治愈"] += int(ld[-1])
        elif ld[1] == "排除":
            output_data[ld[0]]["疑似"] -= int(ld[-1])
            output_data["全国"]["疑似"] -= int(ld[-1])

def main(args):
    #print(args.province)
    if args.date == "Today":
        date = "2099-01-01"
    else:
        date = args.date
    todo_files = getFiles(date, args.log)
    #for file in todo_files:
    #    print(file)

    output_data = init()
    for file in todo_files:
        processFiles(file, output_data, args.log)

    f = open(os.path.join(args.out, args.date+".log.txt"), "w", encoding="utf-8")
    if args.province == "All" or "全国" in args.province:
        f.write("全国 ")
        if "ip" in args.type:
            f.write("感染患者{}人 ".format(max(0, output_data["全国"]["感染"])))
        if "sp" in args.type:
            f.write("疑似患者{}人 ".format(max(0, output_data["全国"]["疑似"])))
        if "cure" in args.type:
            f.write("治愈{}人 ".format(max(0, output_data["全国"]["治愈"])))
        if "dead" in args.type:
            f.write("死亡{}人 ".format(max(0, output_data["全国"]["死亡"])))
        f.write("\n")

    for k, v in output_data.items():
        if k == "全国":
            continue
        if args.province != "All" and k not in args.province:
            continue

        f.write("{} ".format(k))
        if "ip" in args.type:
            f.write("感染患者{}人 ".format(max(0, v["感染"])))
        if "sp" in args.type:
            f.write("疑似患者{}人 ".format(max(0, v["疑似"])))
        if "cure" in args.type:
            f.write("治愈{}人 ".format(max(0, v["治愈"])))
        if "dead" in args.type:
            f.write("死亡{}人 ".format(max(0, v["死亡"])))
        f.write("\n")

    f.close()


if __name__ == '__main__':
    parser = argparse.ArgumentParser(usage="缺乏必要参数", description="help info.")
    parser.add_argument("-date",  type=str, required=True)
    parser.add_argument("-log", type=str, required=True)
    parser.add_argument("-out", type=str, default="Today")
    parser.add_argument("-type", type=str, default=["ip", "sp", "cure", "dead"], nargs="*")
    parser.add_argument("-province", type=str, default="All", nargs="*")

    args = parser.parse_args()
    main(args)
