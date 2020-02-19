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
        BaseData::province_map.insert(make_pair("浙江", InfectInfo()));
        BaseData::province_map.insert(make_pair("云南", InfectInfo()));
        BaseData::province_map.insert(make_pair("新疆", InfectInfo()));
        BaseData::province_map.insert(make_pair("天津", InfectInfo()));
        BaseData::province_map.insert(make_pair("四川", InfectInfo()));
        BaseData::province_map.insert(make_pair("上海", InfectInfo()));
        BaseData::province_map.insert(make_pair("陕西", InfectInfo()));
        BaseData::province_map.insert(make_pair("山东", InfectInfo()));
        BaseData::province_map.insert(make_pair("山西", InfectInfo()));
        BaseData::province_map.insert(make_pair("宁夏", InfectInfo()));
        BaseData::province_map.insert(make_pair("内蒙古", InfectInfo()));
        BaseData::province_map.insert(make_pair("辽宁", InfectInfo()));
        BaseData::province_map.insert(make_pair("江苏", InfectInfo()));
        BaseData::province_map.insert(make_pair("吉林", InfectInfo()));
        BaseData::province_map.insert(make_pair("江西", InfectInfo()));
        BaseData::province_map.insert(make_pair("湖南", InfectInfo()));
        BaseData::province_map.insert(make_pair("湖北", InfectInfo()));
        BaseData::province_map.insert(make_pair("黑龙江", InfectInfo()));
        BaseData::province_map.insert(make_pair("河南", InfectInfo()));
        BaseData::province_map.insert(make_pair("河北", InfectInfo()));
        BaseData::province_map.insert(make_pair("海南", InfectInfo()));
        BaseData::province_map.insert(make_pair("贵州", InfectInfo()));
        BaseData::province_map.insert(make_pair("广西", InfectInfo()));
        BaseData::province_map.insert(make_pair("广东", InfectInfo()));
        BaseData::province_map.insert(make_pair("甘肃", InfectInfo()));
        BaseData::province_map.insert(make_pair("福建", InfectInfo()));
        BaseData::province_map.insert(make_pair("北京", InfectInfo()));
        BaseData::province_map.insert(make_pair("安徽", InfectInfo()));
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
    ///数据、参数预处理 根据返回结果判断是否合法
    static bool getParameters(int argc, char* argv[]) {
        vector<string> strings;

        vector<string> parameters;

        ///将除了第一个默认参数以外的参数填充到字符串数组
        for(int i = 1 ; i < argc ; i++) {
            strings.push_back(argv[i]);
        }

        ///读取省份参数
        parameters = attributeParameters("-province", strings);
        BaseData::provinces = parameters;

        ///读取日期参数
        parameters = attributeParameters("-date", strings);
        BaseData::files = parameters;

        ///读取类型参数
        parameters = attributeParameters("-type", strings);
        BaseData::types = transferStringToAction(parameters);

        ///全部合法 成功返回
        return true;
    }

    ///逐行根据-date 参数读入数据，并写入键值映射表
    static void readDataFromFiles() {
        ifstream input_stream;

        string data;

        input_stream.open("/Users/vegetablefriend/Desktop/InfectStatistic-main/example/log/2020-01-22.log.txt");

        while(getline(input_stream, data)) {
            if (data.find("//") != -1) break;
            getInfoFromString(data);
        }
    }

    ///根据读入并处理完的数据，结合-type -province参数输出结果
    static void outputResult() {
        unordered_map<string, InfectInfo>::iterator it;

        string output_res;

        ///如果未指定省份 或者 省份参数里有全国 先输出全国的数据
        if (BaseData::provinces.empty() || BaseData::contains("全国")) {
            cout << "全国" << endl;
        }

        for (it = BaseData::province_map.begin(); it != BaseData::province_map.end(); it++) {

            ///决定需要输出的省份
            if (it->second.exist && BaseData::contains(it->first)) {

                ///如果类型未被指定
                if (BaseData::types.empty()) {
                    cout << it->first;
                    printf(" 感染患者 %d人 疑似患者%d人 治愈%d人 死亡%d人\n", it->second.sure_count, it->second.doubt_count,
                           it->second.cure_count, it->second.dead_count);
                } else {
                    cout << it->first;
                    ///根据类型按顺序输出
                    for (int i = 0; i < BaseData::types.size(); i++) {
                        switch (BaseData::types[i]) {
                            case dead:
                                printf(" 死亡%d人", it->second.dead_count);
                                break;
                            case increase_doubt:
                                printf(" 疑似患者%d人", it->second.doubt_count);
                                break;
                            case increase_sure:
                                printf(" 确诊患者%d人", it->second.sure_count);
                                break;
                            case cure:
                                printf(" 治愈%d人", it->second.cure_count);
                                break;
                        }
                    }

                    printf("\n");
                }
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
    }

    ///获取文件夹下的全部文件 并根据 -date 参数来返回需要读的文件集的字符串数组
    static vector<string> readFilesByDate() {
        vector<string> result;
        cout << result[3] << endl;
    }

    ///判断某种属性是否被指定 并返回这些属性参数构成的数组
    static vector<string> attributeParameters(const string attribute, const vector<string> strings) {

        vector<string> res;

        for(int i = 0 ; i < strings.size() ; i++) {
            if(attribute == strings[i]) {

                for(int j = i + 1 ; j < strings.size() ; j++) {
                    if(strings[j].find('-') == -1 || strings[j].find('.') != -1) {

                        res.push_back(strings[j]);
                    }
                }

                return res;
            }
        }

        return {};
    }

    ///转换字符串数组 为 枚举数组
    static vector<Action> transferStringToAction(const vector<string> strings) {
        vector<Action> res;
        for(int i = 0 ; i < strings.size() ; i++) {
            if (strings[i].find("ip") != -1) res.push_back(increase_sure);
            else if (strings[i].find("sp") != -1) res.push_back(increase_doubt);
            else if (strings[i].find("cure") != -1) res.push_back(cure);
            else if (strings[i].find("dead") != -1) res.push_back(dead);
        }

        return res;
    }
};

//MARK: 程序运行主类
class Application {
public:
    static void run(int argc, char* argv[]) {

        if(!Tool::getParameters(argc, argv)) return;

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
