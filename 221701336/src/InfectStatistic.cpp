/**
 * InfectStatistic
 * TODO
 *
 * @author 221701336
 * @version 1.0
 * @since 2020.02.13
 */ 
 
#include <algorithm>
#include <iostream>
#include <fstream>
#include <cstring>
#include <io.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <malloc.h>
#include <cassert>
#include <windows.h>

//cd C:\Users\鸟蛋花机\Desktop\软件工程\寒假第二次作业\InfectStatistic-main\221701336\src
//InfectStatistic.exe list -date 2020-01-25 -type sp ip dead -out C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt 
//-log C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/log/ 
//InfectStatistic.exe list -date 2020-01-23 -province 福建 湖北 -type sp -log C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/log/ -out C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt 
using namespace std;

string province[32]={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江",
"湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西"
,"陕西","上海","四川","天津","西藏","新疆","云南","浙江","全国"}; 

string situation[8]={"新增 感染患者","新增 疑似患者","感染患者 流入","疑似患者 流入","死亡","治愈","疑似患者 确诊感染","排除 疑似患者"};

typedef struct ListNode
{
    char province[8];//省份 
	int numOfDead;//number of dead patients 死亡患者数量 
	int numOfIP;//number of infection patients 感染患者数量 
	int numOfSP;//number of suspected patients 疑似患者数量 
	int numOfCured;//number of cured patients  已痊愈患者数量
	int flag;//用于决定该节点是否需要输出 flag=1输出 flag=0不输出 
	struct ListNode *next;//指向下一个节点的指针	
}Node,*PNode;


PNode CreatList(void)//初始化链表 
{
	int len=31;
	PNode PHead =(PNode)malloc(sizeof(Node));
	if(PHead==NULL)
	{
		cout<<"空间分配失败"<<endl;
	}
	PHead->numOfCured=0;
	PHead->numOfDead=0;
	PHead->numOfIP=0;
	PHead->numOfSP=0;
	PHead->flag=0;
	strcpy(PHead->province,province[31].c_str());
		 
	PNode pNew=(PNode)malloc(sizeof(Node));
	if(pNew==NULL)
	{
		cout<<"分配新节点失败"<<endl;
	}
	pNew->numOfCured=0;
	pNew->numOfDead=0;
	pNew->numOfIP=0;
	pNew->numOfSP=0;
	pNew->flag=0;
	strcpy(pNew->province,province[0].c_str());
	pNew->next=NULL;
	
	PHead->next=pNew;
	PNode PTail=pNew;
	PTail->next=NULL;
	
	for(int i=1;i<len;i++)
	{
		PNode p=(PNode)malloc(sizeof(Node));
		if(p==NULL)
		{
			cout<<"分配新节点失败"<<endl;
		}
		p->numOfCured=0;
		p->numOfDead=0;
		p->numOfIP=0;
		p->numOfSP=0;
		p->flag=0;
		strcpy(p->province,province[i].c_str());
		PTail->next=p; 
		p->next=NULL;
		PTail=p;
	}
	cout<<"创建链表成功"<<endl;
	return PHead;
}

string getDate(int argc, char *argv[])//获得命令行参数中的日期 
{
	string s1,s2;
	s2="9999-12-31";
	for (int i = 0; i < argc; i++)
    {
		s1=argv[i]; 
        if(s1=="-date")
        {       
			s2=argv[i+1]; 	
		}
    } 
    return s2;
}

string getLog(int argc, char *argv[])//获得命令行参数中的目录 
{
	string s1;
    string s2="NULL";
	for (int i = 0; i < argc; i++)
    {
		s1=argv[i]; 
        if(s1=="-log")
        {        	
        	s2=argv[i+1];
		}
    }
	return s2; 
}

string getOutPath(int argc, char *argv[])//获得命令行参数中的输出路径 
{
	string s1;
    string s2="NULL";
	for (int i = 0; i < argc; i++)
    {
		s1=argv[i]; 
        if(s1=="-out")
        {        	
        	s2=argv[i+1];
		}
    }
	return s2; 
}

void getType(int argc, char *argv[],string type[])//获得命令行参数中的操作种类 
{
	string s1;
	string s2;
	for (int i = 0; i < argc; i++)
    {
		s1=argv[i]; 
        if (s1=="-type")
        {	
			for(int j=i+1;j<argc;j++)
			{
				s2=argv[j];
				if(s2=="ip"||s2=="sp"||s2=="cure"||s2=="dead")
				{
					type[j-i-1]=s2;
				}
			}	        	
		}
    }
}

void getProvince(int argc,char *argv[],string province[])//获得命令行参数中的要求省份 
{
	string s1;
	string s2;
	for(int i=0;i<argc;i++)
	{
		s1=argv[i];
		if(s1=="-province")
		{
			for(int j=i+1;j<argc;j++)
			{
				s2=argv[j];
				if(s2!="-log"&&s2!="-out"&&s2!="-date"&&s2!="-type"&&s2!="province")
				{
					province[j-i-1]=s2;
				}
			}
		}
	}
}

void AddIP(string province1,int num,PNode head)//统计新增感染患者的情况 
{
	//ofstream outFile;
	//outFile.open(("C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt"),ios::app);
	PNode p=head;
	while(p!=NULL)
	{
		if(p->province==province1)
		{
			p->numOfIP=p->numOfIP+num;
			p->flag=1;
		}
		p=p->next;
	}
	head->numOfIP=head->numOfIP+num; 
	head->flag=1;
}

void AddSP(string province1,int num,PNode head)//统计新增疑似患者的情况
{
	//ofstream outFile;
	//outFile.open(("C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt"),ios::app);
	PNode p=head;
	while(p!=NULL)
	{
		if(p->province==province1)
		{
			p->numOfSP=p->numOfSP+num;
			p->flag=1;
		}
		p=p->next;
	}
	head->numOfSP=head->numOfSP+num; 
	head->flag=1;
}

void MoveIP(string province1,string province2,int num,PNode head)//统计感染患者流入外省的情况
{
	//ofstream outFile;
	//outFile.open(("C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt"),ios::app);
	PNode p=head;
	while(p!=NULL)
	{
		if(p->province==province1)
		{
			p->numOfIP=p->numOfIP-num;
			p->flag=1;
		}
		if(p->province==province2)
		{
			p->numOfIP=p->numOfIP+num;
			p->flag=1;
		}
		p=p->next;
	}
}

void MoveSP(string province1,string province2,int num,PNode head)//统计疑似患者流入外省的情况
{
	//ofstream outFile;
	//outFile.open(("C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt"),ios::app);
	PNode p=head;
	while(p!=NULL)
	{
		if(p->province==province1)
		{
			p->numOfSP=p->numOfSP-num;
			p->flag=1;
		}
		if(p->province==province2)
		{
			p->numOfSP=p->numOfSP+num;
			p->flag=1;
		}
		p=p->next;
	}
}

void Dead(string province1,int num,PNode head)//统计感染患者死亡的情况
{
	PNode p=head;
	while(p!=NULL)
	{
		if(p->province==province1)
		{
			p->numOfIP=p->numOfIP-num;
			p->numOfDead=p->numOfDead+num;
			p->flag=1;
		}
		p=p->next;
	}
	head->numOfIP=head->numOfIP-num;
	head->numOfDead=head->numOfDead+num; 
	head->flag=1;
}

void Cure(string province1,int num,PNode head)//统计感染患者治愈的情况
{
	PNode p=head;
	while(p!=NULL)
	{
		if(p->province==province1)
		{
			p->numOfIP=p->numOfIP-num;
			p->numOfCured=p->numOfCured+num;
			p->flag=1;
		}
		p=p->next;
	}
	head->numOfIP=head->numOfIP-num;
	head->numOfCured=head->numOfCured+num; 
	head->flag=1;
}

void Diagnosis(string province1,int num,PNode head)//统计疑似患者确诊的情况
{
	PNode p=head;
	while(p!=NULL)
	{
		if(p->province==province1)
		{
			p->numOfSP=p->numOfSP-num;
			p->numOfIP=p->numOfIP+num;
			p->flag=1;
		}
		p=p->next;
	}
	head->numOfSP=head->numOfSP-num;
	head->numOfIP=head->numOfIP+num; 
	head->flag=1;
}

void Exclude(string province1,int num,PNode head)//统计疑似患者被排除的情况 
{
	PNode p=head;
	while(p!=NULL)
	{
		if(p->province==province1)
		{
			p->numOfSP=p->numOfSP-num;
			p->flag=1;
		}
		p=p->next;
	}
	head->numOfSP=head->numOfSP-num;
	head->flag=1;
}

void operate(string op_province1,string op_province2,int op,int number,PNode head)//统计函数 
{
	PNode p=head;
	string province1=op_province1;
	string province2=op_province2;
	int num=number;
	switch (op)
	{
		case 0:
			AddIP(province1,num,p); 
			break;
		case 1:
			AddSP(province1,num,p);
			break;
		case 2:
			MoveIP(province1,province2,num,p);
			break;
		case 3: 
			MoveSP(province1,province2,num,p);
			break;
		case 4:
			Dead(province1,num,p);
			break;
		case 5:
			Cure(province1,num,p);
			break;
		case 6:
			Diagnosis(province1,num,p);
			break;
		case 7:
			Exclude(province1,num,p);
			break;
		default :
			break;
	}		
}

static string Utf8ToGbk(const char *src_str)//编码方式：UTF-8 转 GBK
{
	int len = MultiByteToWideChar(CP_UTF8, 0, src_str, -1, NULL, 0);
	wchar_t* wszGBK = new wchar_t[len + 1];
	memset(wszGBK, 0, len * 2 + 2);
	MultiByteToWideChar(CP_UTF8, 0, src_str, -1, wszGBK, len);
	len = WideCharToMultiByte(CP_ACP, 0, wszGBK, -1, NULL, 0, NULL, NULL);
	char* szGBK = new char[len + 1];
	memset(szGBK, 0, len + 1);
	WideCharToMultiByte(CP_ACP, 0, wszGBK, -1, szGBK, len, NULL, NULL);
	string strTemp(szGBK);
	if (wszGBK) delete[] wszGBK;
	if (szGBK) delete[] szGBK;
	return strTemp;
}

static string GBKToUTF8(const char* strGBK)//编码方式：GBK 转 UTF-8 
{
	int len = MultiByteToWideChar(CP_ACP, 0, strGBK, -1, NULL, 0);
	wchar_t* wstr = new wchar_t[len + 1];
	memset(wstr, 0, len + 1);
	MultiByteToWideChar(CP_ACP, 0, strGBK, -1, wstr, len);
	len = WideCharToMultiByte(CP_UTF8, 0, wstr, -1, NULL, 0, NULL, NULL);
	char* str = new char[len + 1];
	memset(str, 0, len + 1);
	WideCharToMultiByte(CP_UTF8, 0, wstr, -1, str, len, NULL, NULL);
	std::string strTemp = str;
	
	if (wstr) delete[] wstr;
	if (str) delete[] str;
	
	return strTemp;
}

void getOperation(char buffer[],PNode head)//读取每行文件中的操作 
{
    string op_province[2];//存放操作省份 
    op_province[0]="0";
    op_province[1]="0";
    int op=10;//存放操作数     
    int number=0;//存放人数    
    int count=0;//记录操作中省份个数 
   
    string buf_situation;
    string buf_province;
    
	string operation=buffer;
 	operation=Utf8ToGbk(buffer);
	
	//ofstream outFile;
	//outFile.open(("C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt"),ios::app); 
	op_province[0]=operation.substr(0,4);
	//outFile<<op_province[0]<<endl;
    string::size_type idx; 
	
	for(int i=0;i<8;i++)//读取指令 
    {

		
    	buf_situation=situation[i];
	    idx=operation.find(buf_situation);//在a中查找b.
	    if(idx != string::npos )
	    {	
	    	//ofstream outFile;
			//outFile.open(("C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt"),ios::app); 
			//outFile<<buf_situation<<endl;
			op=i;
			for(int j=0;j<32;j++)//读取城市 
		    {
		    	buf_province=province[j];
			    idx=operation.find(buf_province);//在a中查找b.
			    if(idx != string::npos&&province[j]!=op_province[0] )
			    {	
			    	op_province[1]=buf_province;			    	
			    }
			}
		} 							    
	}								
	for (int i = 0; buffer[i] != '\0'; ++i) //当a数组元素不为结束符时.遍历字符串a.
    {
        if (buffer[i] >= '0'&& buffer[i] <= '9') //如果是数字.
        {
            number*= 10;//先乘以10保证先检测到的数字存在高位，后检测的存在低位
            number+= buffer[i] - '0'; //数字字符的ascii-字符'0'的ascii码就等于该数字.         	
        }
    }
	//outFile<<"人数:"<<number<<endl;
    string op_province1=op_province[0];
    string op_province2=op_province[1];
	operate(op_province1,op_province2,op,number,head);  
}

void readTxt(char fname[],PNode head)//逐行读取文件的内容 
{
	char buffer[500];
	string strLine;
    ifstream inFile(fname);
	//ofstream outFile("C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt"); 
	while(getline(inFile,strLine))
	{
	    strcpy(buffer,strLine.c_str());
	    if(buffer[0]=='/'||buffer[0]==' ')
		{
            break;
        }
        else
        {
        	//ofstream outFile;
			//outFile.open(("C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/InfectStatistic-main/221701336/src/out.txt"),ios::app); 
        	//outFile<<buffer<<endl;
        	getOperation(buffer,head);
		}	    		
	}
}

void readLog(string date,string path,PNode head)//读取对应目录下的文件 
{
	string s1="*.txt";
	string s2=".log.txt";
	string bufLogPath=path;
	string logPath=path;
	path=path+s1;
	date=date+s2;
	char dirPath[100];
	strcpy(dirPath,path.c_str());
	string latestLog;
	long Handle;
    struct _finddata_t FileInfo;    
    if((Handle=_findfirst(dirPath,&FileInfo))==-1L)
    {
        printf("没有找到相应目录\n");
    }
    else
    {   	
        logPath=bufLogPath+FileInfo.name;
        char fname[100];
		strcpy(fname,logPath.c_str());
        readTxt(fname,head);          
        while(_findnext(Handle,&FileInfo)==0)//成功返回0，失败返回-1
        {
            if(FileInfo.name<date||FileInfo.name==date)
            {
                logPath=bufLogPath+FileInfo.name;
            	char fname[100];
				strcpy(fname,logPath.c_str());
            	readTxt(fname,head);
            }
        }      
    }
}

void outNode(ostream &outFile,PNode p,string type[])//输出各目标结点的相应属性 
{
	outFile<<GBKToUTF8(p->province); 
	if(type[0]=="0")
	{
		outFile<<GBKToUTF8(" 感染患者")<<p->numOfIP<<GBKToUTF8("人");
		outFile<<GBKToUTF8(" 疑似患者")<<p->numOfSP<<GBKToUTF8("人");
		outFile<<GBKToUTF8(" 治愈")<<p->numOfCured<<GBKToUTF8("人");
		outFile<<GBKToUTF8(" 死亡")<<p->numOfDead<<GBKToUTF8("人")<<endl; 
	}
	else
	{
		for(int i=0;i<4;i++)
		{
			if(type[i]=="ip")
			{
				outFile<<GBKToUTF8(" 感染患者")<<p->numOfIP <<GBKToUTF8("人");
			}
			else if(type[i]=="sp")
			{
				outFile<<GBKToUTF8(" 疑似患者")<<p->numOfSP <<GBKToUTF8("人");
			}
			else if(type[i]=="cure")
			{
				outFile<<GBKToUTF8(" 疑似患者")<<p->numOfCured<<GBKToUTF8("人");	
			}
			else if(type[i]=="dead")
			{
				outFile<<GBKToUTF8(" 疑似患者")<<p->numOfDead<<GBKToUTF8("人"); 	
			}
			else
			{
				break;
			}
		}
		outFile<<endl;
	}
}

void output(string path,PNode p,PNode head,string type[],string province[])//输出到文本 
{
	FILE *fp;
	int i=0;
	char fname[100];
	strcpy(fname,path.c_str());
	fp=fopen(fname,"a+");
	if(fp==NULL)
	{
		cout<<"打开失败"<<endl; 
	}
	fclose(fp);	
	ofstream outFile; 
	outFile.open(fname);
	if(outFile.is_open()!=1)
	{
		cout<<"打开失败!"<<endl; 
	}
	if(province[i]=="0")
	{
		outNode(outFile,head,type);
		p=p->next;
		while(p!=NULL)
		{
			if(p->flag==1)
			{
				outNode(outFile,p,type);
			}
			p=p->next;
	    }
	}
	else
	{
		while(province[i]!="0")
		{
			while(p!=NULL)
			{
				if(p->flag==1&&p->province==province[i])
				{
					outNode(outFile,p,type);
				}
				p=p->next;
		    }
		    p=head->next;
			i++;	
		}
	}
	outFile.close(); //关闭文本文件
}

int main(int argc,char *argv[]) 
{		
	string date="9999-12-31"; 
	string type[4]={"0","0","0","0"};
	string outPath;
	string log;
	string province[33];	
	for(int i=0;i<33;i++)
	{
		province[i]="0";
	}	
	date=getDate(argc,argv);		
	log=getLog(argc,argv);	
	outPath=getOutPath(argc,argv);	
	getType(argc,argv,type);	
	getProvince(argc,argv,province);
		
	PNode head=CreatList();	
	PNode p=head;	
	readLog(date,log,head);//读取目录 
	output(outPath,p,head,type,province);//输出 
	system("pause");
}



