/**
 * InfectStatistic
 * TODO
 *
 * @author VegetableFriend
 * @version 1.0
 * @since 2020.02.12
 */


#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <unordered_map>

using namespace std;

///某省行为
enum Action {
    ///增加确诊
    increase_sure,

    ///增加疑似
    increase_doubt,

    ///确诊流入
    sure_transfer,

    ///疑似流入
    doubt_transfer,

    ///死亡
    dead,

    ///治愈
    cure,

    ///疑似变确诊
    doubt_to_sure,

    ///排除疑似
    doubt_remove
};


///某省疫情信息
class InfectInfo {
public:
    ///该省是否用到
    bool exist = false;

    ///疑似人数
    int doubt_count;

    ///确诊人数
    int sure_count;

    ///治愈人数
    int cure_count;

    ///死亡人数
    int dead_count;

private:

};

///工程数据类
class BaseData {
public:
    ///省份键值映射
    static unordered_map<string, InfectInfo> province_keys;

    ///数据初始化
    static void init() {
        BaseData::province_keys.insert(make_pair("安徽", InfectInfo()));
        BaseData::province_keys.insert(make_pair("北京", InfectInfo()));
        BaseData::province_keys.insert(make_pair("福建", InfectInfo()));
        BaseData::province_keys.insert(make_pair("甘肃", InfectInfo()));
        BaseData::province_keys.insert(make_pair("广东", InfectInfo()));
        BaseData::province_keys.insert(make_pair("广西", InfectInfo()));
        BaseData::province_keys.insert(make_pair("贵州", InfectInfo()));
        BaseData::province_keys.insert(make_pair("海南", InfectInfo()));
        BaseData::province_keys.insert(make_pair("河北", InfectInfo()));
        BaseData::province_keys.insert(make_pair("河南", InfectInfo()));
        BaseData::province_keys.insert(make_pair("黑龙江", InfectInfo()));
        BaseData::province_keys.insert(make_pair("湖北", InfectInfo()));
        BaseData::province_keys.insert(make_pair("湖南", InfectInfo()));
        BaseData::province_keys.insert(make_pair("江西", InfectInfo()));
        BaseData::province_keys.insert(make_pair("吉林", InfectInfo()));
        BaseData::province_keys.insert(make_pair("江苏", InfectInfo()));
        BaseData::province_keys.insert(make_pair("辽宁", InfectInfo()));
        BaseData::province_keys.insert(make_pair("内蒙古", InfectInfo()));
        BaseData::province_keys.insert(make_pair("宁夏", InfectInfo()));
        BaseData::province_keys.insert(make_pair("山西", InfectInfo()));
        BaseData::province_keys.insert(make_pair("山东", InfectInfo()));
        BaseData::province_keys.insert(make_pair("陕西", InfectInfo()));
        BaseData::province_keys.insert(make_pair("上海", InfectInfo()));
        BaseData::province_keys.insert(make_pair("四川", InfectInfo()));
        BaseData::province_keys.insert(make_pair("天津", InfectInfo()));
        BaseData::province_keys.insert(make_pair("新疆", InfectInfo()));
        BaseData::province_keys.insert(make_pair("云南", InfectInfo()));
        BaseData::province_keys.insert(make_pair("浙江", InfectInfo()));
    }
};

///静态数据成员的初始化 应在类外进行 需特别注意 
unordered_map<string, InfectInfo> BaseData::province_keys {};

///工程工具类
class Tool {
public:
    ///逐行根据-date 参数读入数据，并写入键值映射表
    static void readDataFromFiles() {
        ifstream input_stream;

        string data;

        input_stream.open("/Users/vegetablefriend/Desktop/InfectStatistic-main/example/log/2020-01-22.log.txt");

        while(getline(input_stream, data)) {
            getInfoFromString(data);
        }
    }

    ///根据读入并处理完的数据，结合-type -province参数输出结果
    static void outputResult() {
        unordered_map<string, InfectInfo>::iterator it;

        for(it = BaseData::province_keys.begin(); it != BaseData::province_keys.end() ; it++) {
            cout << it->second.cure_count << endl;
        }

    }

private:
    ///根据传入的字符串 读取信息 并将其添加到映射表
    static void getInfoFromString(string data) {
        int first_index = 0;
        int space_index = data.find_first_of(' ');

        ///此处获取第一个省份
        string province = data.substr(first_index, space_index);

        ///注册该省信息
        BaseData::province_keys[province].exist = true;
        BaseData::province_keys[province].cure_count = 100;

        ///此处获取操作
    }
};

//MARK: 程序运行主类
class Application {
public:
    static void run(int argc, char* argv[]) {

        //BaseData::init();

        //Tool::readDataFromFiles();

        //Tool::outputResult();
    }
};


//MARK: 程序入口
/** params:
 @argc: 参数数量，默认为1
 @argv: 参数字符串数组，默认有一个运行目录的参数
 */
int main(int argc, char* argv[]) {
    Application::run(argc, argv);
}

