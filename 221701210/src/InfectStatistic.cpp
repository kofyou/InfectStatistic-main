/***********************
FileName:InfectStatistic.cpp
Author:Cazenove
Version:1.0
Date:2020.02.09
Description:
Version Description:Basic funtion
Function List:
    void init();
    void ReadAllLog(string path);
    void ProcessLogFile(string filePath);
    void ShowProvince(string strProvince);
    void ShowAllProvince();
    void AddDiagnosis(string strProvince,int num);
    void AddSuspected(string strProvince,int num);
    void MoveDiagnosis(string strProvinceA,string strProvinceB,int num);
    void MoveSuspected(string strProvinceA,string strProvinceB,int num);
    void DeadDiagnosis(string strProvince,int num);
    void CureDiagnosis(string strProvince,int num);
    void SuspectedToDiagnosis(string strProvince,int num);
    void ExcludeSuspected(string strProvince,int num);
History:none

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
#pragma execution_character_set("gbk")//设定字符集，防止出现中文乱码
using namespace std;


/***********************
Description:省份信息结构体，用于存储单个省份的确诊、疑似、治愈、死亡数量
***********************/
struct SProvinceInformation
{
    unsigned int diagnosis;//确诊患者数量
    unsigned int suspected;//疑似患者数量
    unsigned int cure;//治愈患者数量
    unsigned int death;//死亡患者数量

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
Others:none
***********************/
void init();


/***********************
Description:读取所有的日志文件
Input:
Output:
Return:
Others:
***********************/
void ReadAllLog(string path);


/***********************
Description:处理传入的日志文件
Input:
Output:
Return:
Others:
***********************/
void ProcessLogFile(string filePath);


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
Input:strProvince:省份名称
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
void AddDiagnosis(string strProvince,int num);


/***********************
Description:<省> 新增 疑似患者 n人
Input:
    strProvince:省份名称
Output:none
Return:none
Others:
***********************/
void AddSuspected(string strProvince,int num);


/***********************
Description:<省1> 感染患者 流入 <省2> n人
Input:
    strProvince:省份名称
Output:none
Return:none
Others:
***********************/
void MoveDiagnosis(string strProvinceA,string strProvinceB,int num);


/***********************
Description:<省1> 疑似患者 流入 <省2> n人
Input:strProvince:省份名称
Output:none
Return:none
Others:
***********************/
void MoveSuspected(string strProvinceA,string strProvinceB,int num);


/***********************
Description:<省> 死亡 n人
Input:
Output:
Return:none
Others:
***********************/
void DeadDiagnosis(string strProvince,int num);


/***********************
Description:<省> 治愈 n人
Input:
Output:
Return:none
Others:
***********************/
void CureDiagnosis(string strProvince,int num);


/***********************
Description:<省> 疑似患者 确诊感染 n人
Input:
Output:
Return:none
Others:
***********************/
void SuspectedToDiagnosis(string strProvince,int num);


/***********************
Description:<省> 排除 疑似患者 n人
Input:
Output:
Return:none
Others:
***********************/
void ExcludeSuspected(string strProvince,int num);

/*主函数*/
int main(int args,char *argv[])
{
    init();
    ReadAllLog("..\\221701210\\log");
    ShowAllProvince();
    system("pause");
    return 0;
}


void init()//初始化
{
    ifstream ifProvince("..\\221701210\\log\\provincelist.txt");
    if(!ifProvince)
    {
        cout<<"省份配置文件打开失败！\n";
        exit(0);
    }
    
    string strProvince;
    for(int i=0;i<35;i++)
    {
        ifProvince>>strProvince;
        SProvinceInformation spiProvince = {0};
        mapProvince[strProvince] = spiProvince;//建立省份名称与信息存储结构的映射关系
    }
}

void ReadAllLog(string path)
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
            ProcessLogFile(filePath);//通过文件名，对文件进行对应处理
        } while (_findnext(hFile,&fileinfo) == 0);
        _findclose(hFile);
    }
}

void ProcessLogFile(string filePath)
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
                    AddDiagnosis(cBuffer[0],atoi(cBuffer[3]));
                }
                else//新增疑似患者
                {
                    AddSuspected(cBuffer[0],atoi(cBuffer[3]));
                }
            }
            else if(strcmp(cBuffer[1],"感染患者") == 0)//省1感染患者流入省2
            {
                MoveDiagnosis(cBuffer[0],cBuffer[3],atoi(cBuffer[4]));
            }
            else if(strcmp(cBuffer[1],"疑似患者") == 0)
            {
                if(strcmp(cBuffer[2],"流入") == 0)//省1疑似患者流入省2
                {
                    MoveSuspected(cBuffer[0],cBuffer[3],atoi(cBuffer[4]));
                }
                else//疑似患者确认感染
                {
                    SuspectedToDiagnosis(cBuffer[0],atoi(cBuffer[3]));
                }
            }
            else if(strcmp(cBuffer[1],"死亡") == 0)//感染患者死亡
            {
                DeadDiagnosis(cBuffer[0],atoi(cBuffer[2]));
            }
            else if(strcmp(cBuffer[1],"治愈") == 0)//感染患者治愈
            {
                CureDiagnosis(cBuffer[0],atoi(cBuffer[2]));
            }
            else if(strcmp(cBuffer[1],"排除") == 0)//排除疑似患者患者
            {
                SuspectedToDiagnosis(cBuffer[0],atoi(cBuffer[3]));
            }
        }
    }
    ifLog.close();
}

void ShowProvince(string strProvince)//按省份名称输出数据
{
    SProvinceInformation *pProvince=&mapProvince[strProvince];
    cout<<strProvince;
    printf(" 感染患者%d人 ",pProvince->diagnosis);
    printf("疑似患者%d人 ",pProvince->suspected);
    printf("治愈%d人 ",pProvince->cure);
    printf("死亡%d人\n",pProvince->death);
    delete(pProvince);
}

void ShowAllProvince()//输出所有省份的数据信息
{
    ifstream ifProvince("..\\221701210\\log\\provincelist.txt");
    if(!ifProvince)
    {
        cout<<"省份配置文件打开失败！\n";
        exit(0);
    }
    string strProvince;
    for(int i=0;i<35;i++)//按照省份列表的顺序输出每个省份的数据信息
    {
        ifProvince>>strProvince;
        cout<<strProvince<<" 感染患者"<<mapProvince[strProvince].diagnosis<<"人 ";
        cout<<"疑似患者"<<mapProvince[strProvince].suspected<<"人 ";
        cout<<"治愈"<<mapProvince[strProvince].cure<<"人 ";
        cout<<"死亡"<<mapProvince[strProvince].death<<"人\n";
    }
}

void AddDiagnosis(string strProvince,int num)
{
    mapProvince[strProvince].diagnosis += num;
    mapProvince["全国"].diagnosis += num;
}
void AddSuspected(string strProvince,int num)
{
    mapProvince[strProvince].suspected += num;
    mapProvince["全国"].suspected += num;
}
void MoveDiagnosis(string strProvinceA,string strProvinceB,int num)
{
    mapProvince[strProvinceA].diagnosis -= num;
    mapProvince[strProvinceB].diagnosis += num;
}
void MoveSuspected(string strProvinceA,string strProvinceB,int num)
{
    mapProvince[strProvinceA].suspected -= num;
    mapProvince[strProvinceB].suspected += num;
}
void DeadDiagnosis(string strProvince,int num)
{
    mapProvince[strProvince].diagnosis -= num;
    mapProvince[strProvince].death += num;
    mapProvince["全国"].death += num;
}
void CureDiagnosis(string strProvince,int num)
{
    mapProvince[strProvince].diagnosis -= num;
    mapProvince[strProvince].cure += num;
    mapProvince["全国"].cure += num;
}
void SuspectedToDiagnosis(string strProvince,int num)
{
    mapProvince[strProvince].suspected -= num;
    mapProvince[strProvince].diagnosis += num;
    mapProvince["全国"].suspected -= num;
    mapProvince["全国"].diagnosis += num;
}
void ExcludeSuspected(string strProvince,int num)
{
    mapProvince[strProvince].suspected -= num;
    mapProvince["全国"].suspected -= num;
}