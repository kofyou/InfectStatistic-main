
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
#include <io.h> 

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
    ///该省名
    string province;

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

    InfectInfo(string province) {
        this->province = province;
    }
private:

};

///工程数据类
class BaseData {
public:
    ///排好序的各省份元数据
    static vector<InfectInfo> meta_data;

    ///log文件夹下的全部文件名，届时将通过此数组进行读取
    static vector<string> files;
    
    ///需要展示的省份
    static vector<string> provinces;

    ///需要展示的数据类型
    static vector<Action> types;

    ///数据初始化元数据
    static void init() {
        meta_data.push_back(InfectInfo("安徽"));
        meta_data.push_back(InfectInfo("北京"));
        meta_data.push_back(InfectInfo("重庆"));
        meta_data.push_back(InfectInfo("福建"));
        meta_data.push_back(InfectInfo("甘肃"));
        meta_data.push_back(InfectInfo("广东"));
        meta_data.push_back(InfectInfo("广西"));
        meta_data.push_back(InfectInfo("贵州"));
        meta_data.push_back(InfectInfo("海南"));
        meta_data.push_back(InfectInfo("河北"));
        meta_data.push_back(InfectInfo("河南"));
        meta_data.push_back(InfectInfo("黑龙江"));
        meta_data.push_back(InfectInfo("湖北"));
        meta_data.push_back(InfectInfo("湖南"));
        meta_data.push_back(InfectInfo("江西"));
        meta_data.push_back(InfectInfo("吉林"));
        meta_data.push_back(InfectInfo("江苏"));
        meta_data.push_back(InfectInfo("辽宁"));
        meta_data.push_back(InfectInfo("内蒙古"));
        meta_data.push_back(InfectInfo("宁夏"));
        meta_data.push_back(InfectInfo("青海"));
        meta_data.push_back(InfectInfo("山西"));
        meta_data.push_back(InfectInfo("山东"));
        meta_data.push_back(InfectInfo("陕西"));
        meta_data.push_back(InfectInfo("上海"));
        meta_data.push_back(InfectInfo("四川"));
        meta_data.push_back(InfectInfo("天津"));
        meta_data.push_back(InfectInfo("西藏"));
        meta_data.push_back(InfectInfo("新疆"));
        meta_data.push_back(InfectInfo("云南"));
        meta_data.push_back(InfectInfo("浙江"));
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

    static void setExist(string province) {
        for(int i = 0 ; i < BaseData::meta_data.size() ; i++) {
            if(BaseData::meta_data[i].province == province) BaseData::meta_data[i].exist = true;
		}
    }
    

    static void setAction(string province, Action action, int count) {
        for(int i = 0 ; i < BaseData::meta_data.size() ; i++) {
            if(BaseData::meta_data[i].province == province) {
                switch (action) {
                    case dead:
                        BaseData::meta_data[i].dead_count += count;
                        BaseData::meta_data[i].sure_count -= count;
                        break;
                    case cure:
                        BaseData::meta_data[i].cure_count += count;
                        BaseData::meta_data[i].sure_count -= count;
                        break;
                    case decrease_sure:
                        BaseData::meta_data[i].sure_count -= count;
                        break;
                    case decrease_doubt:
                        BaseData::meta_data[i].doubt_count -= count;
                        break;
                    case increase_sure:
                        BaseData::meta_data[i].sure_count += count;
                        break;
                    case increase_doubt:
                        BaseData::meta_data[i].doubt_count += count;
                        break;
                }
            }
        }
    }
};

///静态数据成员的初始化 应在类外进行 需特别注意
vector<InfectInfo> BaseData::meta_data {};

vector<string> BaseData::files {};

vector<string> BaseData::provinces {};

vector<Action> BaseData::types {};

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
        setFilesWillBeRead(parameters[0]);
        
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
        
        for(int i = 0 ; i < BaseData::files.size() ; i++) {
        	input_stream.open(BaseData::files[i]);

       	 	while(getline(input_stream, data)) {
            	if (data.find("//") != -1) break;
            	getInfoFromString(data);
        	}
        	
        	input_stream.close();
        	
		}
    }

    ///根据读入并处理完的数据，结合-type -province参数输出结果
    static void outputResult() {
        vector<InfectInfo>::iterator it;

        string output_res;

        ///如果未指定省份 先把所有存在的省份加入到带展示省份中
        if (BaseData::provinces.empty()) {
            printf("%s", "全国");
            for(int i = 0 ; i < BaseData::meta_data.size() ; i++) {
            	if(BaseData::meta_data[i].exist) {
            		BaseData::provinces.push_back(BaseData::meta_data[i].province);
            	}
            }
        } else if (BaseData::contains("全国")) {
            printf("%s", "全国");
        }

        for (it = BaseData::meta_data.begin(); it != BaseData::meta_data.end(); it++) {

            ///决定需要输出的省份
            if (it->exist && BaseData::contains(it->province)) {

                ///如果类型未被指定
                if (BaseData::types.empty()) {
                    cout << it->province;
                    printf(" 感染患者 %d人 疑似患者%d人 治愈%d人 死亡%d人\n", it->sure_count, it->doubt_count,
                           it->cure_count, it->dead_count);
                } else {
                    cout << it->province;
                    ///根据类型按顺序输出
                    for (int i = 0; i < BaseData::types.size(); i++) {
                        switch (BaseData::types[i]) {
                            case dead:
                                printf(" 死亡%d人", it->dead_count);
                                break;
                            case increase_doubt:
                                printf(" 疑似患者%d人", it->doubt_count);
                                break;
                            case increase_sure:
                                printf(" 确诊患者%d人", it->sure_count);
                                break;
                            case cure:
                                printf(" 治愈%d人", it->cure_count);
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
    	cout << data << endl;
    	
        int first_index = 0;
        int space_index = data.find_first_of(' ');

        ///此处获取第一个省份
        string province = data.substr(first_index, space_index);
        data = data.substr(space_index + 1, data.size());

        ///注册该省信息
        BaseData::setExist(province);

        ///此处获取该行信息的人数
        int last_space = data.find_last_of(' ');
        int person_index = data.find_first_of("人");
        string count_string = data.substr(last_space + 1, person_index - last_space);
        
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
            BaseData::setAction(province, action, count);

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
            BaseData::setAction(province, action, count);

            last_space = data.find_last_of(' ');
            string str = data.substr(last_space + 1);
            action = increase_sure;
            province = str;
        }

        BaseData::setAction(province, action, count);
    }

    ///获取文件夹下的全部文件 并根据 -date 参数来返回需要读的文件集的字符串数组
    static void getAllFiles(string path, vector<string>& files) {
    	  //文件句柄
 		 long hFile = 0;
  		//文件信息
  		struct _finddata_t fileinfo; 
  		string p; 
  		if ((hFile = _findfirst(p.assign(path).append("\\*").c_str(), &fileinfo)) != -1) {
    		do {
      			if ((fileinfo.attrib & _A_SUBDIR)) { //比较文件类型是否是文件夹
        			if (strcmp(fileinfo.name, ".") != 0 && strcmp(fileinfo.name, "..") != 0) {
          				files.push_back(p.assign(path).append("\\").append(fileinfo.name));
          				//递归搜索
          				getAllFiles(p.assign(path).append("\\").append(fileinfo.name), files);
        		}
      		} else {
        		files.push_back(p.assign(path).append("\\").append(fileinfo.name));
          	}
    	} 
		while (_findnext(hFile, &fileinfo) == 0); //寻找下一个，成功返回0，否则-1
    		_findclose(hFile);
  		}
    }

    ///判断某种属性是否被指定 并返回这些属性参数构成的数组
    static vector<string> attributeParameters(const string attribute, const vector<string> strings) {

        vector<string> res;

        for(int i = 0 ; i < strings.size() ; i++) {
            if(attribute == strings[i]) {
                for(int j = i + 1 ; j < strings.size() ; j++) {
                    if(strings[j].find('-') == -1 || strings[j].find('.') != -1) {

                        res.push_back(strings[j]);
                    } else if (attribute == "-date") {
                    	
                    	res.push_back(strings[j]);
                    	break;
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

	///根据日期设置需要被读取的文件 date为传入的参数日期 
	static void setFilesWillBeRead(string date) {
		
		string year_string, month_string, day_string;
		int year, month, day;
		
		int first_index = 0;
		int last_index = date.find_first_of('-');
		
		year_string = date.substr(first_index, last_index);
		year = stoi(year_string);
		date = date.substr(last_index + 1);

		last_index = date.find_first_of('-');
		month_string = date.substr(first_index , last_index);
		month = stoi(month_string);
		date = date.substr(last_index + 1);
		
		last_index = date.find_first_of('-');
		day_string = date.substr(first_index, last_index);
		day = stoi(day_string);
		
		 ///首先获取文件夹下全部文件  
        getAllFiles("D:\\log", BaseData::files);
        vector<string> new_files;
        
        for(int i = 0 ; i < BaseData::files.size() ; i++) {
        	
        	if (isFileShow(BaseData::files[i], year, month, day)) {
        		new_files.push_back(BaseData::files[i]);
			}
		}
        
        ///再根据日期获取需要读取的文件 并设置
		BaseData::files = new_files; 
	}
	
	static bool isFileShow(string date, int year, int month, int day) {
		string year_string, month_string, day_string;
		int file_year, file_month, file_day;
		
		int first_index = date.find_last_of('\\');
		int last_index;
		
		year_string = date.substr(first_index + 1, 4);
		
		file_year = stoi(year_string);
		date = date.substr(first_index + 5);

		month_string = date.substr(1, 2);
		file_month = stoi(month_string);
		date = date.substr(3);
		
		day_string = date.substr(1, 2);
		file_day = stoi(day_string);
		
		///数据提取完毕后 进行比较 如果日期在date参数后面 返回false
		if(file_year > year) return false;
		else if(file_year < year) return true;
		else {
			if(file_month > month) return false;
			else if (file_month < month) return true;
			else {
				if(file_day > day) return false;
				else return true; 
			}
		}
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
