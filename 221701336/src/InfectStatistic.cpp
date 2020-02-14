/**
 * InfectStatistic
 * TODO
 *
 * @author 221701336
 * @version 1.0
 * @since 2020.02.13
 */ 
#include<algorithm>
#include<iostream>
#include<fstream>
#include<cstring>
#include<io.h>

using namespace std;

struct pNode
{
    string province;//省份 
	int numOfDead;//number of dead patients 死亡患者数量 
	int numOfIP;//number of infection patients 感染患者数量 
	int numOfSP;//number of suspected patients 疑似患者数量 
	int numOfCured;//number of cured patients  已痊愈患者数量 
	struct pNode *next;
};

void input(string path,pNode p);
void output(string path,pNode p);
void readDir(string date,string path);

void readDir(string date,string path)
{
	string s="*.txt";
	path=path+s;
	char dirPath[100];
	strcpy(dirPath,path.c_str());
	long Handle;
    struct _finddata_t FileInfo;
    
    if((Handle=_findfirst(dirPath,&FileInfo))==-1L)
    {
        printf("没有找到相应目录\n");
    }
    else
    {   	
    	if(FileInfo.name<=date)
        {
            printf("%s\n", FileInfo.name);
        }
        while(_findnext(Handle,&FileInfo)==0)//成功返回0，失败返回-1
        {
            if(FileInfo.name<=date)
            {
                printf("%s\n", FileInfo.name);
                //读取函数 
            }
            else
            {
				_findclose(Handle);
			} 
        }      
    }
}


/*void input(string path,string date,pNode p)
{
	
	
} */


void output(string path,pNode p)
{
	FILE *fp;
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
	outFile<<p.province; 
	outFile<<" 感染患者"<<p.numOfIP <<"人";
	outFile<<" 疑似患者"<<p.numOfSP <<"人";
	outFile<<" 治愈"<<p.numOfCured<<"人";
	outFile<<" 死亡"<<p.numOfDead<<"人"<<endl; 
	outFile.close(); //关闭文本文件
}

int main()
{
	string date="2020-01-25"; //所要求的的日期 
	char inPath[100]="F:/新建文件夹/InfectStatistic-main/example/log/";//输入地址 
	string dirPath;//目录地址 
	dirPath=inPath; 
	readDir(date,dirPath);
	
	
	char outPath[100]="C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/out.txt";//输出地址 
	string path;
	path=outPath;
	pNode p={"福建",6,3,15,20};
	//output(path,p);
	system("pause");
}



