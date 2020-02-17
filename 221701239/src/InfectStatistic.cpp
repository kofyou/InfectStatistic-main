/*
该代码功能:实现全国肺炎疫情情况的统计
作者:何瑞晨 学号:221701239
*/

#include<stdio.h>
#include<iostream>
#include<fstream>
#include<filesystem>
#include <string>
#include<string.h>
#include <io.h>  
#include <vector>  
#include<map>
#include <windows.h>

using namespace std;
map<string, string> m_single;      //存储命令行只有一个值的元素的信息
map<string, vector<string>> m_many;   //存储命令行可能有多个值的元素的信息

//将字符转utf-8
string UTF8ToGB(const char* str)
{
	string result;
	WCHAR *strSrc;
	LPSTR szRes;

	//获得临时变量的大小
	int i = MultiByteToWideChar(CP_UTF8, 0, str, -1, NULL, 0);
	strSrc = new WCHAR[i + 1];
	MultiByteToWideChar(CP_UTF8, 0, str, -1, strSrc, i);

	//获得临时变量的大小
	i = WideCharToMultiByte(CP_ACP, 0, strSrc, -1, NULL, 0, NULL, NULL);
	szRes = new CHAR[i + 1];
	WideCharToMultiByte(CP_ACP, 0, strSrc, -1, szRes, i, NULL, NULL);

	result = szRes;
	delete[]strSrc;
	delete[]szRes;

	return result;
}
//按字符串读取文件
void process_file(vector<string> file_list)
{
	fstream fei_yan_log(file_list[0]);
	string temp;
	fei_yan_log >> temp;
	temp = UTF8ToGB(temp.c_str()).c_str();
	cout << temp;
}


 //该函数实现了对命令行信息的处理
void process_cmd(int num, char* cmd_i[])
{
	int i;
	string temp;   //中转字符串
	for (i = 3; i < num; i++)
	{
		if (strcmp(cmd_i[i], "-province")==0 || strcmp(cmd_i[i], "-type")==0)
		{
			vector<string> list_temp;
			string m_name = cmd_i[i];//记录命令行参数名称
			temp = cmd_i[i];
			while (temp.compare("-") != 0)
			{
				temp = cmd_i[++i];
				list_temp.push_back(temp);
				cout << cmd_i[i] << "\n";
				if (i >= num - 1)
					break;
				temp=temp.substr(0, 1);
			}
			m_many.insert(make_pair(m_name, list_temp));
		}
		else
		{
			string temp1, temp2;
			temp1 = cmd_i[i];
			temp2 = cmd_i[++i];
			m_single.insert(make_pair(temp1, temp2));
		}
	}
}

//该函数实现了将path路径文件夹下的所有文件读取到files里面
void getFiles(const std::string & path, std::vector<std::string> & files)
{
	//文件句柄  
	long long hFile = 0;
	//文件信息，_finddata_t需要io.h头文件  
	struct _finddata_t fileinfo;
	std::string p;
	int i = 0;
	if ((hFile = _findfirst(p.assign(path).append("\\*").c_str(), &fileinfo)) != -1)
	{
		do
		{
			//如果是目录,迭代之  
			//如果不是,加入列表  
			if ((fileinfo.attrib & _A_SUBDIR))
			{
				//if (strcmp(fileinfo.name, ".") != 0 && strcmp(fileinfo.name, "..") != 0)
				//getFiles(p.assign(path).append("\\").append(fileinfo.name), files);
			}
			else
			{
				files.push_back(p.assign(path).append("\\").append(fileinfo.name));
			}
		} while (_findnext(hFile, &fileinfo) == 0);
		_findclose(hFile);
	}
}

int main(int argc, char* argv[])
{
	process_cmd(argc, argv);
	int i;
	cout << argc;
	for (i = 0; i < argc; i++)
	{
		cout << argv[i] << "\n";      //命令行测试
	}
	vector<string> file_list;
	getFiles("D:\\肺炎代码\\InfectStatistic-main\\221701239\\log", file_list);
	for (i = 0; i < file_list.size(); i++)
	{
		//cout << file_list[i] << "\n" ;
	}
	process_file(file_list);
	system("pause");

}