/***********************
FileName:InfectStatistic.cpp
Author:Cazenove
Version:v1.4
Date:2020.02.16
Description:
    本程序为疫情统计程序，通过读取list命令中给定了log路径下的日志文件，以及给定的具体参数，将统计结果输出到给定out路径的文件中
    list命令 支持以下命令行参数：
    -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
    -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
    -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
    -type 可选择[ip：感染患者，sp：疑似患者，cure：治愈 ，dead：死亡患者]，如 -type ip 表示只列出感染
    患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
    -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

    作业要求链接：https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287
    项目github链接：https://github.com/Cazenove/InfectStatistic-main
Version Description:
    修复了date为空时出错的bug；优化了代码顺序
History:
    V1.0:参照作业要求初步完成了所有基本命令
    v1.1:使用面向对象方法重新编排代码，将省份信息与操作进行了封装。
    v1.2:使用enum+map来将if else结构替换为switch结构、美化了部分代码，修正了部分对需求理解的偏差。
    v1.3:新增了文件名称验证功能
************************/

#include <cstdlib>
#include <cstdio>
#include <fstream>
#include <io.h>
#include <iostream>
#include <map>
#include <sstream>
#include <string>
#include <strstream>
#include <vector>
#include <windows.h>
using namespace std;

//存放传入的参数
int mainArgc;
char **mainArgv;

/***********************
Description:存放某省份的数据以及对数据的操作
***********************/
class CProvince
{
    public:
    unsigned int ip;//确诊患者数量
    unsigned int sp;//疑似患者数量
    unsigned int cure;//治愈患者数量
    unsigned int dead;//死亡患者数量

    bool isInLog;//是否在日志文件中出现过
    bool isPrint;//该省份是否要打印

    //初始化
    CProvince();

    //感染患者增加
    void AddIP(int num);
    
    //疑似患者增加
    void AddSP(int num);
    
    //感染患者流入其他省
    void MoveIP(string strProvince,int num);
    
    //疑似患者流入其他省
    void MoveSP(string strProvince,int num);
    
    //感染患者死亡
    void IPtoDead(int num);
    
    //感染患者治愈
    void IPtoCure(int num);
    
    //疑似患者确诊为感染患者
    void SPtoIP(int num);
    
    //排除疑似患者
    void SubSP(int num);
};

/***********************
Description:存放各省份名称
***********************/
static string PROVINCENAME[35] = 
{
    "全国","安徽","澳门","北京","重庆","福建","甘肃",
    "广东","广西","贵州","海南","河北","河南","黑龙江",
    "湖北","湖南","吉林","江苏","江西","辽宁","内蒙古",
    "宁夏","青海","山东","山西","陕西","上海","四川",
    "台湾","天津","西藏","香港","新疆","云南","浙江"
};

/***********************
Description:将list命令参数与枚举类型建立映射关系，用于简化if else结构
***********************/
enum EListValue
{
    logValue,
    outValue,
    dateValue,
    typeValue,
    provinceValue
};
map<string,EListValue> mapListValue;

/***********************
Description:将输出的命令类型与枚举类型建立映射关系，用于简化if else结构
***********************/
enum EDataValue
{
    ipValue,
    spValue,
    cureValue,
    deadValue
};
map<string,EDataValue> mapDataValue;

/***********************
Description:使用STL的map来建立省份名称和省份信息结构体之间的映射关系
***********************/
map<string,CProvince> mapProvince;


/***********************
Description:程序初始化
Input:none
Output:none
Return:none
Others:
    读取省份配置文件并清空mapProvince
***********************/
void init();

/***********************
Description:处理命令行参数，并交由list()处理
Input:
    int argc:从main函数读取的argc
    char *argv[]:从main函数读取的argv
Output:none
Return:none
Others:
    参数解析后调用list函数处理
***********************/
void ProcessOption(int argc,char *argv[]);

/***********************
Description:处理list命令
Input:
    string logPath:读取的日志文件目录
    string outPath:输出文件的存放目录
    string date:yyyy-mm-dd格式的日期
    vector<string> type:输出的格式
    vector<string> province:输出的省份
Output:none
Return:none
Others:
    使用init进行初始化
    调用ReadAllLog读取日志文件
    再使用OutLog输出日志文件
***********************/
void list(string logPath, string outPath, string date, vector<string> type, vector<string> province);

/***********************
Description:读取给定文件夹中，所有给定日期之前的日志文件
Input:
    string path:给定的文件夹路径
    string date:yyyy-mm-dd格式的日期
Output:none
Return:none
Others:
    会将读取到的子文件交由ReadLog函数处理
***********************/
void ReadAllLog(string path, string date);

/***********************
Description:处理传入的日志文件
Input:
    string filePath:路径+文件名
Output:none
Return:none
Others:
    将读取到的数据存入mapProvince
***********************/
void ReadLog(string filePath);

/***********************
Description:生成日志文件
Input:
    string filePath:文件的生成路径及文件名
    vector<string> type:文件的输出顺序
    vector<string> province:输出的省份信息
Output:none
Return:none
Others:none
***********************/
void OutLog(string filePath, vector<string> type, vector<string> province);

/***********************
Description:比较两个yyyy-mm-dd格式的字符串日期的大小
Input:
    string date1:第一个日期
    string date2:第二个日期
Output:none
Return:
    int类型，为方便使用，参造strcmp函数的返回值。
    date1>date2 返回值大于0
    date1<date2 返回值小于0
    date1=date2 返回值为0
Others:none
***********************/
int datecmp(string date1, string date2);

/***********************
Description:判断文件是否是日志文件
Input:
    stirng fileName:文件名
Output:none
Return:
    true:是
    false:不是
Others:none
***********************/
bool isLogFile(string fileName);

/***********************
Description:将gbk转为utf-8
Input:
    const char *gbk:GBK编码的字符串
Output:none
Return:
    strTemp:utf-8格式的字符串
Others:none
***********************/
string GbkToUtf8(const char *gbk);

/*主函数*/
int main(int argc,char *argv[])
{
    mainArgc = argc;
    mainArgv = argv;
    init();//初始化
    ProcessOption(argc,argv);//处理命令行参数
    return 0;
}


void init()//初始化
{
    mapListValue["-log"] = logValue;
    mapListValue["-out"] = outValue;;
    mapListValue["-date"] = dateValue;
    mapListValue["-type"] = typeValue;
    mapListValue["-province"] = provinceValue;

    mapDataValue["ip"] = ipValue;
    mapDataValue["sp"] = spValue;
    mapDataValue["cure"] = cureValue;
    mapDataValue["dead"] = deadValue;

    mapProvince.clear();//清空map
    string strProvince;
    for(int i=0; i<35; i++)
    {
        CProvince cprovince;
        mapProvince[PROVINCENAME[i]] = cprovince;//建立省份名称与信息存储结构的映射关系
    }
}

void ProcessOption(int argc,char *argv[])//处理参数
{
    if((argc > 1) && !strcmp(argv[1], "list"))
    {
        string logPath = "";
        string outPath = "";
        string date = "";
        vector<string> type;
        vector<string> province;

        int index = 2;
        while(argv[index])
        {
            switch(mapListValue[argv[index]])
            {
                case logValue:
                    if(argv[index+1])
                    {
                        logPath = argv[index+1];
                        index++;
                    }
                    break;
                case outValue:
                    if(argv[index+1])
                    {
                        outPath = argv[index+1];
                        index++;
                    }
                    break;
                case dateValue:
                    if(argv[index+1][0] != '-')//如果下一位不是其他操作符，那么则是date的参数值
                    {
                        date = argv[index+1];
                        index++;
                    }
                    break;
                case typeValue:
                    while((argv[index+1]) && (argv[index+1][0] != '-'))//-type后面可能有0到多个参数值
                    {
                        type.push_back(argv[index+1]);
                        index++;
                    }
                    break;
                case provinceValue:
                    while((argv[index+1]) && (argv[index+1][0] != '-'))
                    {
                        province.push_back(GbkToUtf8(argv[index+1]));//将gbk转为utf-8，以免出现乱码
                        mapProvince[GbkToUtf8(argv[index+1])].isPrint = true;//要输出
                        index++;
                    }
                    break;
                default:
                    if(argv[index][0] == '-')
                    {
                        cout<<"Unknown command: -"<<argv[index]<<"\n";
                    }
                    break;
            }
            index++;
        }

        list(logPath, outPath, date, type, province);//交由list函数处理
    }
}

void list(string logPath, string outPath, string date, vector<string> type, vector<string> province)//list命令
{
    //文件读入和输出路径为空
    if(logPath == "" || outPath == "")
    {
        cout<<"Parameter value -log and -out cannot be empty\n";
        exit(0);
    }
    if(type.size() == 0)//如果没有设置type，则为默认顺序
    {
        //默认顺序为感染患者 疑似患者 治愈 死亡
        type.push_back("ip");
        type.push_back("sp");
        type.push_back("cure");
        type.push_back("dead");
    }
    ReadAllLog(logPath, date);//读取date之前所有的日志文件
    OutLog(outPath, type, province);//按照指定格式输出
}

void ReadAllLog(string path, string date)//读取到date之前的给定路径中所有日志文件
{
    long hFile = 0;//文件句柄
    struct _finddata_t fileinfo;//文件信息
    char fileName[10];
    //priority_queue <string, vector<string>, less<string> > queueLog;

    string p;
    if((hFile = _findfirst(p.assign(path).append("\\*.log.txt").c_str(), &fileinfo)) != -1)
    {
        do
        {
            if(isLogFile(fileinfo.name))//判断文件的名称格式是否是日志文件
            {
                //当date为默认时，输出最新的情况。否则输出date之前的最新情况。
                if(date.empty() || datecmp(date, fileinfo.name) >= 0)
                {
                    string fileName = fileinfo.name;
                    string filePath = "\\";
                    filePath = path + filePath + fileName;//构造完整的文件路径及名称
                    ReadLog(filePath);//通过文件名，对文件进行对应处理
                }
            }
        } while (_findnext(hFile,&fileinfo) == 0);
        
        //date比最新日志文件的日期还大，提示超出范围并关闭程序。
        if(!date.empty() && datecmp(date, fileinfo.name) > 0)
        {
            cout<<"out of range\n";
            exit(0);
        }
        _findclose(hFile);
    }
}

void ReadLog(string filePath)
{
    ifstream ifLog(filePath.c_str());//打开日志文件
    if(!ifLog)
    {
        cout<<filePath<<" open failure.\n";
        return ;
    }

    string buffer;//读取行缓冲区
    while(getline(ifLog,buffer))//逐行读取文件
    {
        char cBuffer[10][20];
        int i=0;
        if(buffer.size() == 0)//跳过空行
        {
            break;
        }
        istringstream ist(buffer);
        while(ist>>cBuffer[i++])//从缓冲区逐个读入词语
        {
            //none
        }

        if(cBuffer[0][0]!='/')//跳过注释
        {
            char *province = cBuffer[0];
            if(strcmp(cBuffer[1],"新增") == 0)
            {
                if(strcmp(cBuffer[2],"感染患者") == 0)//新增感染患者
                {
                    mapProvince[province].AddIP(atoi(cBuffer[3]));
                }
                else//新增疑似患者
                {
                    mapProvince[province].AddSP(atoi(cBuffer[3]));
                }
            }
            else if(strcmp(cBuffer[1],"感染患者") == 0)//省1感染患者流入省2
            {
                mapProvince[province].MoveIP(cBuffer[3],atoi(cBuffer[4]));
            }
            else if(strcmp(cBuffer[1],"疑似患者") == 0)
            {
                if(strcmp(cBuffer[2],"流入") == 0)//省1疑似患者流入省2
                {
                    mapProvince[province].MoveSP(cBuffer[3],atoi(cBuffer[4]));
                }
                else//疑似患者确认感染
                {
                    mapProvince[province].SPtoIP(atoi(cBuffer[3]));
                }
            }
            else if(strcmp(cBuffer[1],"死亡") == 0)//感染患者死亡
            {
                mapProvince[province].IPtoDead(atoi(cBuffer[2]));
            }
            else if(strcmp(cBuffer[1],"治愈") == 0)//感染患者治愈
            {
                mapProvince[province].IPtoCure(atoi(cBuffer[2]));
            }
            else if(strcmp(cBuffer[1],"排除") == 0)//排除疑似患者患者
            {
                mapProvince[province].SubSP(atoi(cBuffer[3]));
            }
        }
    }
    ifLog.close();
}

void OutLog(string filePath, vector<string> type, vector<string> province)
{
    ofstream ofLog(filePath.c_str(),ios::out);//创建并写入新的日志文件
    if(!ofLog)
    {
        cout<<filePath<<" open failure.\n";
        exit(0);
    }
    for(int i=0; i<35; i++)//按照省份列表的顺序输出每个省份的数据信息
    {
        //指定打印参数中出现过的省份，若参数为空，输出日志文件中出现过的省份
        if((mapProvince[PROVINCENAME[i]].isPrint) || (!province.size() && mapProvince[PROVINCENAME[i]].isInLog))
        {
            ofLog<<PROVINCENAME[i]<<" ";
            for(int j=0; j<type.size(); j++)
            {
                switch(mapDataValue[type[j]])
                {
                    case ipValue:
                        ofLog<<"感染患者"<<mapProvince[PROVINCENAME[i]].ip<<"人 ";
                        break;
                    case spValue:
                        ofLog<<"疑似患者"<<mapProvince[PROVINCENAME[i]].sp<<"人 ";
                        break;
                    case cureValue:
                        ofLog<<"治愈"<<mapProvince[PROVINCENAME[i]].cure<<"人 ";
                        break;
                    case deadValue:
                        ofLog<<"死亡"<<mapProvince[PROVINCENAME[i]].dead<<"人 ";
                        break;
                    default:
                        break;
                }
            }
            ofLog<<"\n";
        }
    }
    ofLog<<"// 该文档并非真实数据，仅供测试使用\n";
    ofLog<<"// 命令 InfectStatistic.exe";
    for(int i=1; i<mainArgc; i++)
    {
        ofLog<<mainArgv[i]<<" ";
    }

    ofLog.close();
}

string GbkToUtf8(const char *gbk)//将gbk转为utf-8
{
	int len = MultiByteToWideChar(CP_ACP, 0, gbk, -1, NULL, 0);
	wchar_t* wstr = new wchar_t[len + 1];
	memset(wstr, 0, len + 1);
	MultiByteToWideChar(CP_ACP, 0, gbk, -1, wstr, len);
	len = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
	char* str = new char[len + 1];
	memset(str, 0, len + 1);
	WideCharToMultiByte(CP_UTF8, 0, wstr, -1, str, len, NULL, NULL);
	string strTemp = str;
	if (wstr) delete[] wstr;
	if (str) delete[] str;
	return strTemp;
}

int datecmp(string date1, string date2)
{
    /*把yyyy-mm-dd格式的字符串分离开*/
    int year1,month1,day1;
    int year2,month2,day2;

    year1 = atoi(date1.substr(0,4).c_str());
    month1 = atoi(date1.substr(5,2).c_str());
    day1 = atoi(date1.substr(8,2).c_str());

    year2 = atoi(date2.substr(0,4).c_str());
    month2 = atoi(date2.substr(5,2).c_str());
    day2 = atoi(date2.substr(8,2).c_str());

    if(year1 > year2)
    {
        return 1;
    }
    else if(year1 == year2)
    {
        if(month1 > month2)
        {
            return 1;
        }
        else if(month1 == month2)
        {
            if(day1 > day2)
            {
                return 1;
            }
            else if(day1 == day2)
            {
                return 0;
            }
            else
            {
                return -1;
            }
            
        }
        else//month1 < month2
        {
            return -1;
        }
    }
    else//year1 < year2
    {
        return -1;
    }
}

bool isLogFile(string fileName)
{
    //文件名格式为yyyy-mm-dd.log.txt 为18
    if(fileName.length() != 18)
    {
        return false;
    }
    else
    {
        char file[20];
        strcpy(file, fileName.c_str());
        if(file[4] != '-' || file[7] != '-')
        {
            return false;
        }
        for(int i=0; i<10; i++)
        {
            if(i != 4 && i !=7)
            {
                if((file[i] < '0') || (file[i] > '9'))
                {
                    return false;
                }
            }
        }
    }
    return true;
}


/*CProvince类方法*/
CProvince::CProvince()//类初始化
{
    ip = 0;
    sp = 0;
    cure = 0;
    dead = 0;
    isInLog = false;
    isPrint = false;
}
void CProvince::AddIP(int num)//感染患者增加
{
    this->isInLog = true;
    this->ip += num;
    mapProvince["全国"].isInLog = true;
    mapProvince["全国"].ip += num;
}
void CProvince::AddSP(int num)//疑似患者增加
{
    this->isInLog = true;
    this->sp += num;
    mapProvince["全国"].isInLog = true;
    mapProvince["全国"].sp += num;
}
void CProvince::MoveIP(string strProvince,int num)//感染患者流动
{
    this->isInLog = true;
    this->ip -= num;
    mapProvince[strProvince].isInLog = true;
    mapProvince[strProvince].ip += num;
}
void CProvince::MoveSP(string strProvince,int num)//疑似患者流动
{
    this->isInLog = true;
    this->sp -= num;
    mapProvince[strProvince].isInLog = true;
    mapProvince[strProvince].sp += num;
}
void CProvince::IPtoDead(int num)//感染患者死亡
{
    this->isInLog = true;
    this->ip -= num;
    this->dead += num;
    mapProvince["全国"].isInLog = true;
    mapProvince["全国"].ip -= num;
    mapProvince["全国"].dead += num;
}
void CProvince::IPtoCure(int num)//感染患者治愈
{
    this->isInLog = true;
    this->ip -= num;
    this->cure += num;
    mapProvince["全国"].isInLog = true;
    mapProvince["全国"].ip -= num;
    mapProvince["全国"].cure += num;
}
void CProvince::SPtoIP(int num)//疑似患者确诊
{
    this->isInLog = true;
    this->sp -= num;
    this->ip += num;
    mapProvince["全国"].isInLog = true;
    mapProvince["全国"].sp -= num;
    mapProvince["全国"].ip += num;
}
void CProvince::SubSP(int num)//疑似患者排除
{
    this->isInLog = true;
    this->sp -= num;
    mapProvince["全国"].isInLog = true;
    mapProvince["全国"].sp -= num;
}