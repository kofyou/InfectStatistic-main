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

using namespace std;

//按一定顺序事先存储各省份，用于指令中省份的读取和判断 
string province[32]={"安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江",
"湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西"
,"陕西","上海","四川","天津","西藏","新疆","云南","浙江","全国"}; 

//事先存储各情况，用于后续情况的读取和判断 
string situation[8]={"新增 感染患者","新增 疑似患者","感染患者 流入","疑似患者 流入","死亡","治愈","疑似患者 确诊感染","排除 疑似患者"};

//存储各省份相关相关内容的结构体链表 
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

/*
功能：初始化链表 
输入参数：无
输出参数：无 
返回值：头结点指针 
*/ 
PNode CreatList(void) 
{
	int len=31;
	PNode PHead =(PNode)malloc(sizeof(Node));
	if(PHead==NULL)
	{
		cout<<"空间分配失败"<<endl;
	}
	//初始化各数值 
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
	//分配结点 
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
	return PHead;
}


/*
功能：获得命令行参数中的日期 
输入参数：命令行参数个数 argc(int) 命令函参数数组 argv[](char *) 
输出参数：无 
返回值：date值 
*/ 
string getDate(int argc, char *argv[]) 
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

/*
功能：获得命令行参数中的目录 
输入参数：命令行参数个数 argc(int) 命令函参数数组 argv[](char *) 
输出参数：无 
返回值：Log值 
*/
string getLog(int argc, char *argv[]) 
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

/*
功能：获得命令行参数中的路径 
输入参数：命令行参数个数 argc(int) 命令函参数数组 argv[](char *) 
输出参数：无 
返回值：outpath 
*/
string getOutPath(int argc, char *argv[]) 
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

/*
功能：获得命令行参数中的操作种类 
输入参数：命令行参数个数 argc(int) 命令函参数数组 argv[](char *) 字符串数组type[] 
输出参数：输出type值 
返回值：无 
*/
void getType(int argc, char *argv[],string type[]) 
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

/*
功能：获得命令行参数中的要求省份 
输入参数：命令行参数个数 argc(int) 命令函参数数组 argv[](char *) 字符串数组province[] 
输出参数：输出province值 
返回值：无 
*/
void getProvince(int argc,char *argv[],string province[]) 
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
 
/*
功能：统计新增感染患者的情况 
输入参数：省份province1(string) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void AddIP(string province1,int num,PNode head)
{
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

/*
功能：统计新增疑似患者的情况 
输入参数：省份province1(string) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void AddSP(string province1,int num,PNode head)
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

/*
功能：统计感染患者流入外省的情况 
输入参数：流出省份province1(string) 流入省份province2(string) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void MoveIP(string province1,string province2,int num,PNode head)
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

/*
功能：统计疑似患者流入外省的情况 
输入参数：流出省份province1(string) 流入省份province2(string) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void MoveSP(string province1,string province2,int num,PNode head)
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

/*
功能：统计感染患者死亡的情况 
输入参数：省份province1(string) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void Dead(string province1,int num,PNode head)
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

/*
功能：统计感染患者治愈的情况 
输入参数：省份province1(string) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void Cure(string province1,int num,PNode head)
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

/*
功能：统计疑似患者确诊的情况 
输入参数：省份province1(string) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void Diagnosis(string province1,int num,PNode head)
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

/*
功能：统计疑似患者被排除的情况 
输入参数：省份province1(string) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void Exclude(string province1,int num,PNode head) 
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

/*
功能：统计总函数，操作选择 
输入参数：省份province1(string) 省份province2(string) 操作数op(int) 变化人数num（int) 链表头指针head（PNode） 
输出参数：无 
返回值：无 
*/
void operate(string op_province1,string op_province2,int op,int number,PNode head) 
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

/*
功能：字符串编码方式：UTF-8 转 GBK 
输入参数：字符串(UTF-8) 
输出参数：无 
返回值：字符串(GBK)
备注：VC++11新增的功能库 
*/
static string Utf8ToGbk(const char *src_str)
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

/*
功能：字符串编码方式：GBK 转 UTF-8 
输入参数：字符串(GBK) 
输出参数：无 
返回值：字符串(UTF-8)
备注：VC++11新增的功能库 
*/
static string GBKToUTF8(const char* strGBK) 
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

/*
功能：读取每行文件中的操作
输入参数：存放各行内容的buffer[](char) 链表头指针head(PNode) 
输出参数：省份province1 province2(string)  人数num(int) 指令数op(int)  
返回值：无  
*/
void getOperation(char buffer[],PNode head) 
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

	op_province[0]=operation.substr(0,4);
    string::size_type idx; 
	
	for(int i=0;i<8;i++)//读取指令 
    {		
    	buf_situation=situation[i];
	    idx=operation.find(buf_situation);
	    if(idx != string::npos )
	    {	
			op=i;
			for(int j=0;j<32;j++)//读取城市 
		    {
		    	buf_province=province[j];
			    idx=operation.find(buf_province);
			    if(idx != string::npos&&province[j]!=op_province[0] )
			    {	
			    	op_province[1]=buf_province;			    	
			    }
			}
		} 							    
	}								
	for (int i = 0; buffer[i] != '\0'; ++i)
    {
        if (buffer[i] >= '0'&& buffer[i] <= '9') //如果是数字.
        {
            number*= 10;//先乘以10保证先检测到的数字存在高位，后检测的存在低位
            number+= buffer[i] - '0'; //数字字符的ascii-字符'0'的ascii码就等于该数字.         	
        }
    }
    string op_province1=op_province[0];
    string op_province2=op_province[1];
	operate(op_province1,op_province2,op,number,head);  
}

/*
功能：逐行读取文件的内容，并对'/'和空行进行排除 
输入参数：存放路径的fname[](char) 链表头指针head(PNode) 
输出参数：无 
返回值：无  
*/ 
void readTxt(char fname[],PNode head)
{
	char buffer[500];
	string strLine;
    ifstream inFile(fname);
	while(getline(inFile,strLine))
	{
	    strcpy(buffer,strLine.c_str());
	    if(buffer[0]=='/'||buffer[0]==' ')
		{
            break;
        }
        else
        {
        	getOperation(buffer,head);
		}	    		
	}
}

/*
功能：读取对应目录下的文件 
输入参数：要求日期date(string) 路径的path(string) 链表头指针head(PNode) 
输出参数：打开文件路径fname(char) 
返回值：无  
*/
void readLog(string date,string path,PNode head)
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

/*
功能：输出各目标结点的相应属性 
输入参数：数据流outFile(ostream) 链表头指针p (PNode) 输出种类type[](string) 
输出参数：输出结点的各指定数据到文本 
返回值：无  
*/
void outNode(ostream &outFile,PNode p,string type[]) 
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

/*
功能：将链表输出输出到对应文本 
输入参数：文件路径path(string) 链表指针p (PNode) 链表头指针head(PNode) 输出种类type[](string) 输出省份province[](string) 
输出参数：数据流outFile(ostream) 链表指针p(PNode) 输出种类tyep[](string) 
返回值：无  
*/
void output(string path,PNode p,PNode head,string type[],string province[]) 
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
	date=getDate(argc,argv);//读取要求日期		
	log=getLog(argc,argv);//读取打开目录 
	outPath=getOutPath(argc,argv);//读取输出目录	
	getType(argc,argv,type);//读取输出种类	
	getProvince(argc,argv,province);//读取输出省份 
		
	PNode head=CreatList();//初始化链表	
	PNode p=head;	
	readLog(date,log,head);//读取并执行 
	output(outPath,p,head,type,province);//输出 
	system("pause");
}



