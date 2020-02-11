/***********************
FileName:InfectStatistic.cpp
Author:Cazenove
Version:v1.1
Date:2020.02.10
Description:
    该程序为疫情统计程序，从log目录中读取.log.txt文件，统计并生成当日疫情信息的日志文件。
Version Description:Basic funtion
Function List:
    void init();
    void ReadAllLog(string path);
    void ReadLog(string filePath);
    void ShowProvince(string strProvince);
    void ShowAllProvince();
    void AddIP(string strProvince,int num);
    void AddSP(string strProvince,int num);
    void MoveIP(string strProvinceA,string strProvinceB,int num);
    void MoveSP(string strProvinceA,string strProvinceB,int num);
    void IPtoDead(string strProvince,int num);
    void IPtoCure(string strProvince,int num);
    void SPtoIP(string strProvince,int num);
    void SubSP(string strProvince,int num);
History:
    v1.0:Basic function
************************/
#include <iostream>
#include <string>
#include <cstdio>
#include <io.h>
#include <vector>
#include <map>
#include <windows.h>
#include <cstdlib>
#include <fstream>
#include <sstream>
#include <strstream>
#include <getopt.h>
#pragma execution_character_set("gbk")//设定字符集，防止出现中文乱码
using namespace std;


/***********************
Description:省份信息结构体，用于存储单个省份的确诊、疑似、治愈、死亡数量
***********************/
struct SProvinceInformation
{
    unsigned int ip;//确诊患者数量
    unsigned int sp;//疑似患者数量
    unsigned int cure;//治愈患者数量
    unsigned int dead;//死亡患者数量
};

string PROVINCE[35] = 
{
    "全国","安徽","澳门","北京","重庆","福建","甘肃",
    "广东","广西","贵州","海南","河北","河南","黑龙江",
    "湖北","湖南","吉林","江苏","江西","辽宁","内蒙古",
    "宁夏","青海","山东","山西","陕西","上海","四川",
    "台湾","天津","西藏","香港","新疆","云南","浙江"
};

/***********************
Description:使用STL的map来建立省份名称和省份信息结构体之间的映射关系
***********************/
map<string,SProvinceInformation> mapProvince;


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
Description:生成日志文件
Input:
Output:
Return:
Others:
***********************/
void OutLog(string filePath, vector<string> type, vector<string> province);


/***********************
Description:输出指定省份的人员信息
Input:strProvince:省份名称
Output:省份 感染患者a人 疑似患者b人 治愈c人 死亡d人
Return:none
Others:
***********************/
void ShowProvince(string strProvince);


/***********************
Description:输出所有省份的人员信息
Input:
Output:省份 感染患者a人 疑似患者b人 治愈c人 死亡d人
Return:none
Others:
***********************/
void ShowAllProvince();


/***********************
Description:<省> 新增 感染患者 n人
Input:
    strProvince:省名称
Output:none
Return:none
Others:
***********************/
void AddIP(string strProvince,int num);


/***********************
Description:<省> 新增 疑似患者 n人
Input:
    strProvince:省份名称
Output:none
Return:none
Others:
***********************/
void AddSP(string strProvince,int num);


/***********************
Description:<省1> 感染患者 流入 <省2> n人
Input:
    strProvince:省份名称
Output:none
Return:none
Others:
***********************/
void MoveIP(string strProvinceA,string strProvinceB,int num);


/***********************
Description:<省1> 疑似患者 流入 <省2> n人
Input:strProvince:省份名称
Output:none
Return:none
Others:
***********************/
void MoveSP(string strProvinceA,string strProvinceB,int num);


/***********************
Description:<省> 死亡 n人
Input:
Output:
Return:none
Others:
***********************/
void IPtoDead(string strProvince,int num);


/***********************
Description:<省> 治愈 n人
Input:
Output:
Return:none
Others:
***********************/
void IPtoCure(string strProvince,int num);


/***********************
Description:<省> 疑似患者 确诊感染 n人
Input:
Output:
Return:none
Others:
***********************/
void SPtoIP(string strProvince,int num);


/***********************
Description:<省> 排除 疑似患者 n人
Input:
Output:
Return:none
Others:
***********************/
void SubSP(string strProvince,int num);


/*主函数*/
int main(int argc,char *argv[])
{
    ProcessOption(argc,argv);//处理命令行参数
    system("pause");
    return 0;
}


void init()//初始化
{
    //ifstream ifProvince("..\\221701210\\log\\provincelist.txt");//打开省份配置文件
    /*ifstream ifProvince("D:\\Users\\qaz70\\Documents\\GitHub\\InfectStatistic-main\\221701210\\log\\provincelist.txt");//打开省份配置文件
    if(!ifProvince)
    {
        cout<<"省份配置文件打开失败！\n";
        exit(0);
    }*/
    
    mapProvince.clear();//清空map
    string strProvince;
    for(int i=0; i<35; i++)
    {
        //ifProvince>>strProvince;//从文件中读取省份名称
        SProvinceInformation spiProvince = {0};
        mapProvince[PROVINCE[i]] = spiProvince;//建立省份名称与信息存储结构的映射关系
    }


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

void ReadAllLog(string path, string date)//读取到date之前的给定路径中所有日志文件
{
    long hFile = 0;//文件句柄

    struct _finddata_t fileinfo;//文件信息

    string p;
    if((hFile = _findfirst(p.assign(path).append("\\*.log.txt").c_str(),&fileinfo)) != -1)
    {
        do
        {
            string fileName = fileinfo.name;
            string filePath = "\\";
            filePath = path + filePath + fileName;//构造完整的文件路径及名称
            ReadLog(filePath);//通过文件名，对文件进行对应处理
        } while (_findnext(hFile,&fileinfo) == 0);
        _findclose(hFile);
    }
}

void ReadLog(string filePath)
{
    ifstream ifLog(filePath.c_str());//打开日志文件
    if(!ifLog)
    {
        cout<<filePath<<"文件打开失败\n";
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
            
        }

        if(cBuffer[0][0]!='/')//跳过注释
        {
            if(strcmp(cBuffer[1],"新增") == 0)
            {
                if(strcmp(cBuffer[2],"感染患者") == 0)//新增感染患者
                {
                    AddIP(cBuffer[0],atoi(cBuffer[3]));
                }
                else//新增疑似患者
                {
                    AddSP(cBuffer[0],atoi(cBuffer[3]));
                }
            }
            else if(strcmp(cBuffer[1],"感染患者") == 0)//省1感染患者流入省2
            {
                MoveIP(cBuffer[0],cBuffer[3],atoi(cBuffer[4]));
            }
            else if(strcmp(cBuffer[1],"疑似患者") == 0)
            {
                if(strcmp(cBuffer[2],"流入") == 0)//省1疑似患者流入省2
                {
                    MoveSP(cBuffer[0],cBuffer[3],atoi(cBuffer[4]));
                }
                else//疑似患者确认感染
                {
                    SPtoIP(cBuffer[0],atoi(cBuffer[3]));
                }
            }
            else if(strcmp(cBuffer[1],"死亡") == 0)//感染患者死亡
            {
                IPtoDead(cBuffer[0],atoi(cBuffer[2]));
            }
            else if(strcmp(cBuffer[1],"治愈") == 0)//感染患者治愈
            {
                IPtoCure(cBuffer[0],atoi(cBuffer[2]));
            }
            else if(strcmp(cBuffer[1],"排除") == 0)//排除疑似患者患者
            {
                SubSP(cBuffer[0],atoi(cBuffer[3]));
            }
        }
    }
    ifLog.close();
}

void ProcessOption(int argc,char *argv[])//处理参数
{
    if(argc > 1)//有参数
    {
        if(strcmp(argv[1], "list") == 0)
        {
            string logPath = "";
            string outPath = "";
            string date ="";
            vector<string> type;
            vector<string> province;

            int index = 2;
            while(argv[index])
            {
                if(strcmp(argv[index], "-log") == 0)
                {
                    if(argv[index+1])
                    {
                        logPath = argv[index+1];
                        index++;
                    }
                }
                else if(strcmp(argv[index], "-out") == 0)
                {
                    if(argv[index+1])
                    {
                        outPath = argv[index+1];
                        index++;
                    }
                }
                else if(strcmp(argv[index], "-date") == 0)
                {
                    if(argv[index+1][0] != '-')//如果下一位不是其他操作符，那么则是date的参数值
                    {
                        date = argv[index+1];
                        index++;
                    }
                }
                else if(strcmp(argv[index], "-type") == 0)
                {
                    while((argv[index+1]) && (argv[index+1][0] != '-'))//-type后面可能有0到多个参数值
                    {
                        type.push_back(argv[index+1]);
                        index++;
                    }
                }
                else if(strcmp(argv[index], "-province") == 0)//-province后面可能有0到多个参数
                {
                    while((argv[index+1]) && (argv[index+1][0] != '-'))
                    {
                        province.push_back(argv[index+1]);
                        index++;
                    }
                }
                else
                {
                    if(argv[index][0] == '-')
                    {
                        cout<<"未知的操作符"<<argv[index]<<endl;
                    }
                }
                index++;
            }

            if(logPath == "" || outPath == "")
            {
                cout<<"-log和-out指令参数不能为空！\n";
                exit(0);
            }
            list(logPath, outPath, date, type, province);//交由list函数处理
        }
    }
}

void list(string logPath, string outPath, string date, vector<string> type, vector<string> province)//list命令
{
    cout<<logPath<<endl<<outPath<<endl<<date<<endl;
    init();//初始化
    ReadAllLog(logPath, date);//读取date之前所有的日志文件
    OutLog(outPath, type, province);//按照指定格式输出
    ShowAllProvince();
}

void OutLog(string filePath, vector<string> type, vector<string> province)
{   
    ofstream ofLog(filePath.c_str(),ios::out);//创建并写入新的日志文件
    if(!ofLog)
    {
        cout<<"输出目录打开失败！";
        exit(0);
    }

    for(int i=0;i<35;i++)//按照省份列表的顺序输出每个省份的数据信息
    {
        ofLog<<PROVINCE[i]<<" 感染患者"<<mapProvince[PROVINCE[i]].ip<<"人 ";
        ofLog<<"疑似患者"<<mapProvince[PROVINCE[i]].sp<<"人 ";
        ofLog<<"治愈"<<mapProvince[PROVINCE[i]].cure<<"人 ";
        ofLog<<"死亡"<<mapProvince[PROVINCE[i]].dead<<"人\n";
    }
    ofLog<<"// 该文档并非真实数据，仅供测试使用";

    ofLog.close();
}

void ShowProvince(string strProvince)//按省份名称输出数据
{
    SProvinceInformation *pProvince=&mapProvince[strProvince];
    cout<<strProvince;
    printf(" 感染患者%d人 ",pProvince->ip);
    printf("疑似患者%d人 ",pProvince->sp);
    printf("治愈%d人 ",pProvince->cure);
    printf("死亡%d人\n",pProvince->dead);
    delete(pProvince);
}

void ShowAllProvince()//输出所有省份的数据信息
{
    for(int i=0;i<35;i++)//按照省份列表的顺序输出每个省份的数据信息
    {
        cout<<PROVINCE[i]<<" 感染患者"<<mapProvince[PROVINCE[i]].ip<<"人 ";
        cout<<"疑似患者"<<mapProvince[PROVINCE[i]].sp<<"人 ";
        cout<<"治愈"<<mapProvince[PROVINCE[i]].cure<<"人 ";
        cout<<"死亡"<<mapProvince[PROVINCE[i]].dead<<"人\n";
    }
}

void AddIP(string strProvince,int num)
{
    mapProvince[strProvince].ip += num;
    mapProvince["全国"].ip += num;
}
void AddSP(string strProvince,int num)
{
    mapProvince[strProvince].sp += num;
    mapProvince["全国"].sp += num;
}
void MoveIP(string strProvinceA,string strProvinceB,int num)
{
    mapProvince[strProvinceA].ip -= num;
    mapProvince[strProvinceB].ip += num;
}
void MoveSP(string strProvinceA,string strProvinceB,int num)
{
    mapProvince[strProvinceA].sp -= num;
    mapProvince[strProvinceB].sp += num;
}
void IPtoDead(string strProvince,int num)
{
    mapProvince[strProvince].ip -= num;
    mapProvince[strProvince].dead += num;
    mapProvince["全国"].ip -= num;
    mapProvince["全国"].dead += num;
}
void IPtoCure(string strProvince,int num)
{
    mapProvince[strProvince].ip -= num;
    mapProvince[strProvince].cure += num;
    mapProvince["全国"].ip -= num;
    mapProvince["全国"].cure += num;
}
void SPtoIP(string strProvince,int num)
{
    mapProvince[strProvince].sp -= num;
    mapProvince[strProvince].ip += num;
    mapProvince["全国"].sp -= num;
    mapProvince["全国"].ip += num;
}
void SubSP(string strProvince,int num)
{
    mapProvince[strProvince].sp -= num;
    mapProvince["全国"].sp -= num;
}