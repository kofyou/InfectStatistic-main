/*
该代码功能:实现全国肺炎疫情情况的统计
作者:何瑞晨 学号:221701239
*/

#include<stdio.h>
#include<iostream>
#include<fstream>
#include<filesystem>
#include <string>  
#include <io.h>  
#include <vector>  

using namespace std;

int main(int argc, char* argv[])
{
	cout << argc;
	for (int i = 0; i < argc; i++)
	{
		cout << argv[i] << "\n";      //命令行测试
	}
	system("pause");
	
}