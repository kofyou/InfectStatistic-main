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

    ///减少确诊
    decrease_sure,

    ///减少疑似
    decrease_doubt,

    ///死亡
    dead,

    ///治愈
    cure,
};

///某省疫情信息
class InfectInfo {
public:
    ///该省是否用到
    bool exist = false;

    ///疑似人数
    int doubt_count = 0;

    ///确诊人数
    int sure_count = 0;

    ///治愈人数
    int cure_count = 0;

    ///死亡人数
    int dead_count = 0;

private:

};

///工程数据类
class BaseData {
public:
    ///省份键值映射
    static unordered_map<string, InfectInfo> province_map;

    ///log文件夹下的全部文件名，届时将通过此数组进行读取
    static vector<string> files;
    
    ///需要展示的省份
    static vector<string> provinces;

    ///需要展示的数据类型
    static vector<Action> types;

    ///数据初始化
    static void init() {
        BaseData::province_map.insert(make_pair("安徽", InfectInfo()));
        BaseData::province_map.insert(make_pair("北京", InfectInfo()));
        BaseData::province_map.insert(make_pair("福建", InfectInfo()));
        BaseData::province_map.insert(make_pair("甘肃", InfectInfo()));
        BaseData::province_map.insert(make_pair("广东", InfectInfo()));
        BaseData::province_map.insert(make_pair("广西", InfectInfo()));
        BaseData::province_map.insert(make_pair("贵州", InfectInfo()));
        BaseData::province_map.insert(make_pair("海南", InfectInfo()));
        BaseData::province_map.insert(make_pair("河北", InfectInfo()));
        BaseData::province_map.insert(make_pair("河南", InfectInfo()));
        BaseData::province_map.insert(make_pair("黑龙江", InfectInfo()));
        BaseData::province_map.insert(make_pair("湖北", InfectInfo()));
        BaseData::province_map.insert(make_pair("湖南", InfectInfo()));
        BaseData::province_map.insert(make_pair("江西", InfectInfo()));
        BaseData::province_map.insert(make_pair("吉林", InfectInfo()));
        BaseData::province_map.insert(make_pair("江苏", InfectInfo()));
        BaseData::province_map.insert(make_pair("辽宁", InfectInfo()));
        BaseData::province_map.insert(make_pair("内蒙古", InfectInfo()));
        BaseData::province_map.insert(make_pair("宁夏", InfectInfo()));
        BaseData::province_map.insert(make_pair("山西", InfectInfo()));
        BaseData::province_map.insert(make_pair("山东", InfectInfo()));
        BaseData::province_map.insert(make_pair("陕西", InfectInfo()));
        BaseData::province_map.insert(make_pair("上海", InfectInfo()));
        BaseData::province_map.insert(make_pair("四川", InfectInfo()));
        BaseData::province_map.insert(make_pair("天津", InfectInfo()));
        BaseData::province_map.insert(make_pair("新疆", InfectInfo()));
        BaseData::province_map.insert(make_pair("云南", InfectInfo()));
        BaseData::province_map.insert(make_pair("浙江", InfectInfo()));
    }

    static bool contains(string province) {
        for(int i = 0 ; i < BaseData::provinces.size() ; i++) {
            if (BaseData::provinces[i] == province) return true;
        }

        return false;
    }

    static bool contains(Action type) {
        for(int i = 0 ; i < BaseData::types.size() ; i++) {
            if (BaseData::types[i] == type) return true;
        }

        return false;
    }
};

///静态数据成员的初始化 应在类外进行 需特别注意
unordered_map<string, InfectInfo> BaseData::province_map {};

vector<string> BaseData::files {};

vector<string> BaseData::provinces {};

vector<Action> BaseData::types {};

///工程操作类
class Command {
public:
    static void ActionWith(Action action, int count, string province) {
        switch (action) {
            case dead:
                BaseData::province_map[province].dead_count += count;
                BaseData::province_map[province].sure_count -= count;
                break;
            case cure:
                BaseData::province_map[province].cure_count += count;
                BaseData::province_map[province].sure_count -= count;
                break;
            case decrease_sure:
                BaseData::province_map[province].sure_count -= count;
                break;
            case decrease_doubt:
                BaseData::province_map[province].doubt_count -= count;
                break;
            case increase_sure:
                BaseData::province_map[province].sure_count += count;
                break;
            case increase_doubt:
                BaseData::province_map[province].doubt_count += count;
                break;
        }
    }
};

///工程工具类
class Tool {
public:
    ///逐行根据-date 参数读入数据，并写入键值映射表
    static void readDataFromFiles() {
        ifstream input_stream;

        string data;

        input_stream.open("/Users/vegetablefriend/Desktop/InfectStatistic-main/example/log/2020-01-22.log.txt");

        //readFilesByDate();

        while(getline(input_stream, data)) {
            if (data.find("//") != -1) break;
            getInfoFromString(data);
        }
    }

    ///根据读入并处理完的数据，结合-type -province参数输出结果
    static void outputResult() {
        unordered_map<string, InfectInfo>::iterator it;

        string output_res;

        for(it = BaseData::province_map.begin(); it != BaseData::province_map.end() ; it++) {
            if (it->second.exist && BaseData::contains(it->first)) {

            }
        }

    }

private:
    ///根据传入的字符串 读取信息 并将其添加到映射表
    static void getInfoFromString(string data) {
        int first_index = 0;
        int space_index = data.find_first_of(' ');

        ///此处获取第一个省份
        string province = data.substr(first_index, space_index);
        data = data.substr(space_index + 1, data.size());

        ///注册该省信息
        BaseData::province_map[province].exist = true;

        ///此处获取该行信息的人数
        int last_space = data.find_last_of(' ');
        int person_index = data.find_last_of("人");
        string count_string = data.substr(last_space, person_index - last_space - 2);
        int count = stoi(count_string);
        data = data.substr(0, last_space);

        ///此处获取该省对应的动作 action
        Action action;

        space_index = data.find_first_of(' ');
        string op = data.substr(0, space_index);

        ///根据获得的情况来作出对应的操作
        if(op == "死亡") {
            action = dead;
        } else if (op == "治愈") {
            action = cure;
        } else if (op == "疑似患者") {

            ///首先执行一次疑似患者减少的操作
            action = decrease_doubt;
            Command::ActionWith(action, count, province);

            ///再判断是确诊了 还是迁出了
            last_space = data.find_last_of(' ');
            string str = data.substr(last_space + 1);
            if(str == "确诊感染") {
                action = increase_sure;
            } else {
                action = increase_doubt;
                province = str;
            }

        } else if (op == "排除") {
            action = decrease_doubt;
        } else if (op == "新增") {
            last_space = data.find_last_of(' ');
            string str = data.substr(last_space + 1);

            if(str == "感染患者") {
                action = increase_sure;
            } else {
                action = increase_doubt;
            }

        } else if (op == "感染患者") {

            action = decrease_sure;
            Command::ActionWith(action, count, province);

            last_space = data.find_last_of(' ');
            string str = data.substr(last_space + 1);
            action = increase_sure;
            province = str;
        }

        Command::ActionWith(action, count, province);

        cout << "--------------------------------------" << endl;
        outputResult();
    }

    ///根据 -type 参数 读取需要展示的数据类型
    static Action* readParameterOfType() {

        Action actions[5];

        return actions;
    }

    ///根据 -province 参数 读取出需要展示的省份字符串数组
    static vector<string> readParameterOfProvince() {
        
    }

    ///获取文件夹下的全部文件 并根据 -date 参数来返回需要读的文件集的字符串数组
    static vector<string> readFilesByDate() {
        vector<string> result;
        cout << result[3] << endl;
    }
};

//MARK: 程序运行主类
class Application {
public:
    static void run(int argc, char* argv[]) {

        BaseData::init();

        Tool::readDataFromFiles();

        Tool::outputResult();
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
