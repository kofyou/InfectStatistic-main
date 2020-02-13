/**
 * InfectStatistic
 * TODO
 *
 * @author 221701336
 * @version 1.0
 * @since 2020.02.13
 */ 
#include <algorithm>
#include<iostream>
#include<fstream>
#include<cstring>
using namespace std;

struct pNode
{
    string province;//省份 
	int numOfDead;//number of dead patients 死亡患者数量 
	int numOfIP;//number of infection patients 感染患者数量 
	int numOfSP;//number of suspected patients 疑似患者数量 
	int numOfCured;//number of cured patients  已痊愈患者数量 
	//struct pNode *next;
};

void output(string path,pNode p);

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
	string date; //所要求的的日期 
	string inPath;//目录地址 
	char outPath[100]="C:/Users/鸟蛋花机/Desktop/软件工程/寒假第二次作业/out.txt";//输出地址 
	string path;
	path=outPath;
	pNode p={"福建",6,3,15,20};
	output(path,p);
	system("pause");
}



