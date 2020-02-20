/*********************************
FileName:InfectStatistic.cpp
Author:林羽希
Function:疫情状况统计 
**********************************/

#pragma warning(disable:4996)
#include <io.h>		
#include <iostream>
#include <fstream>
#include <cstring>
#include <windows.h>
#include<vector>
#include<algorithm>
#include<map>
#include<string>

using namespace std;

class Analysis
{
public:
	char outpath[1005];
	string province[40];
	map<string, int>mi;
	map<string, int>mi1;
	map<string, int>mi2;
	map<string, int>mi3;
	string province_of[40];
	string type[20];
	string Begin(string com);
	string Check(int date_,char *date);
	int province_num, type_num;
	int out[40][6];
	int Num(string tmp);
	void FindAllFile(const char* path, const char* format, const char* date);
	void Change(int province1,int province2,int type1,int type2,int num,int l);
	void Init();
	void Out();
	void CheckType(int num);
	void CheckAgain();
	void LineDetail(string tmp);
    int Detail(string p, string t) 
    {
        return out[mi[p]][mi2[t]];
    }
    
};

int Analysis::Num(string tmp)
{
    int num = 0;
    for (int i = tmp.size() - 1,c = 1;i >= 0;i--)
	{
		if (tmp[i] >= '0' && tmp[i] <= '9')num += (tmp[i] - '0') * c, c *= 10;
	}
	return num;
}

void Analysis::Change(int province1, int province2,int type1,int type2,int num,int l)
{
	if (province2 == 0 && type2 == 0)
	{
		if (l == 100)out[province1][type1] += num;
		else if (l == 101)out[province1][type1] -= num;
		else if (l == 102)
		{
			out[province1][mi2["感染患者"]] -= num;
			out[province1][mi2["治愈"]] += num;	
		}
		else 
		{
			out[province1][mi2["死亡"]] += num;
			out[province1][mi2["感染患者"]] -= num;
		}
	}
	else if (province2 == 0 && type2 != 0)
	{
		out[province1][mi2["疑似患者"]] -= num;
		out[province1][mi2["感染患者"]] += num;
	}
	else 
	{
		out[province1][type1] -= num;
		out[province2][type1] += num;
	}
}

void Analysis::LineDetail(string tmp)
{
	int province1 = 0, province2 = 0;
	string s = "";
	int l = 0;
	for (int j = 0;j < tmp.size();j++) 
	{
		if (tmp[j] == ' ')
		{
			if (!mi.count(s))
			{
				s = "";
				continue;
			}
			mi3[s] = 1;
			if (mi[s] >= 100)l = mi[s];
			else if (province1 == 0)province1 = mi[s];
			else province2 = mi[s];
			s = "";
		}
		else s = s + tmp[j];
	}
	int type1 = 0, type2 = 0;
	string ss = "";
	for (int j = 0;j < tmp.size();j++)
	{
		if (tmp[j] == ' ')
		{
			if (!mi2.count(ss))
			{
				ss = "";
				continue;
			}
			if (type1 == 0)type1 = mi2[ss];
			else type2 = mi2[ss];
			ss = "";
		}
		else 
		{
			ss = ss + tmp[j];
		}
	}
	int num = Num(tmp);
					//	cout<<fl1<<' '<<fl2<<' '<<f1<<' '<<f2<<' '<<num<<endl;
	Change(province1,province2,type1,type2,num,l);
}

void Analysis::FindAllFile(const char* path, const char* format, const char* date)
{
    char newpath[200];
    char newpaths[200];
    strcpy(newpath, path);
    strcpy(newpaths, path);
	strcat(newpath, "\\*.*");    // 在目录后面加上"\\*.*"进行第一次搜索
	int handle;
	int returnflag = 0;
	_finddata_t findData;
	handle = _findfirst(newpath, &findData);
	if (handle == -1)
	{
		// 检查是否成功
		return;
	}
    while (_findnext(handle, &findData) == 0) 
    {
        if (findData.attrib & _A_SUBDIR) {
			if (strcmp(findData.name, ".") == 0 || strcmp(findData.name, "..") == 0)
				continue;
			strcpy(newpath, path);
			strcat(newpath, "\\");
			strcat(newpath, findData.name);
			FindAllFile(newpath, format, date);
		}
        else 
        {
            if (strstr(findData.name, format))
			{     //判断是不是txt文件 
				
				int size = strlen(date);
				int flg = 1;
				if (size != 0)
				{
					for (int i = 0;i < size;i++)
					{
						if (findData.name[i] != date[i])flg = 0;
						if (findData.name[i] > date[i])returnflag = 1;
					}
				}
				if (returnflag == 1)return ;
				if (flg == 1&&size != 0)returnflag = 1;
				freopen(outpath, "w", stdout);
				char nowpath[200];
				strcpy(nowpath, newpaths);
				strcat(nowpath, "\\");
				strcat(nowpath, findData.name);
				ifstream fin(nowpath);
				if (!fin.is_open())cout << "error";
				string tmp;
				while (getline(fin, tmp))
				{
					if (tmp[0] == '/' && tmp[1] == '/')continue;
					LineDetail(tmp);
				}
				if (returnflag == 1)return;
			}
		}
	}
	_findclose(handle);    // 关闭搜索句柄
}

void Analysis::Init()
{
	memset(out, 0, sizeof(out));
	for (int i = 0;i <= 35;i++)province[i] = "";
	mi.clear();
	mi1.clear();
	mi2.clear();
	mi3.clear();
	province_num = 0, type_num = 0;
	mi["新增"] = 100;
	mi["排除"] = 101;
	mi["治愈"] = 102;
	mi["死亡"] = 103;
	mi["全国"] = 1;
	mi["安徽"] = 2, mi["北京"] = 3, mi["重庆"] = 4;
	 mi["福建"] = 5, mi["甘肃"] = 6, mi["广东"] = 7;
	mi["广西"] = 8, mi["贵州"] = 9, mi["海南"] = 10;
	mi["河北"] = 11, mi["河南"] = 12, mi["黑龙江"] = 13;
	mi["湖北"] = 14, mi["湖南"] = 15, mi["吉林"] = 16;
	mi["江苏"] = 17, mi["江西"] = 18, mi["辽宁"] = 19;
	mi["内蒙古"] = 20, mi["宁夏"] = 21, mi["青海"] = 22;
	 mi["山东"] = 23, mi["山西"] = 24, mi["陕西"] = 25;
	mi["上海"] = 26, mi[""] = 27, mi["四川"] = 28;
	mi["台湾"] = 29, mi["天津"] = 30, mi["西藏"] = 31;
	mi["香港"] = 32, mi["新疆"] = 33, mi["云南"] = 34,mi["浙江"];
	province_of[1] = "全国", province_of[2] = "安徽", province_of[3] = "北京";
	province_of[4] = "重庆", province_of[5] = "福建", province_of[6] = "甘肃";
	province_of[7] = "广东", province_of[8] = "广西", province_of[9] = "贵州";
	province_of[10] = "海南", province_of[11] = "河北", province_of[12] = "河南" ;
	province_of[13] = "黑龙江", province_of[14] = "湖北", province_of[15] = "湖南";
	province_of[16] = "吉林", province_of[17] = "江苏", province_of[18] = "江西";
	province_of[19] = "辽宁", province_of[20] = "内蒙古", province_of[21] = "宁夏";
	province_of[22] = "青海", province_of[23] = "山东", province_of[24] = "山西";
	province_of[25] = "陕西", province_of[26] = "上海", province_of[28] = "四川";
	province_of[29] = "台湾", province_of[30] = "天津", province_of[31] = "西藏";
	province_of[32] = "香港", province_of[33] = "新疆", province_of[34] = "云南";
	province_of[35] = "浙江"; province_of[27]="-";
}


void Analysis::Out()
{
	for (int i = 2;i < 36;i++)
	{
		for (int j = 1;j < type_num;j++)
		{
			out[1][j] += out[i][j];
		}
	}
	for (int i = 1;i < 36;i++)
	{
		bool flg= province_num == 0 &&(mi3.count(province_of[i]) || i == 1);
		if (mi1.count(province_of[i]) || flg ) 
		{
			cout << province_of[i] << ' ';
			for (int j = 1;j < type_num;j++)
			{
				cout << type[j] << out[i][j] << "人 ";
			}
			if (mi1[province_of[i]] == 1 || province_num == 0)
			{
				cout << "\n";
			}
		}
	}
	cout << "// 该文档并非真实数据，仅供测试使用" << "\n";
}

string Analysis::Check(int date_,char *date)
{
	string data="";
	if (date_ > 0)
	{
		for (int i = 0;i < date_;i++)data = data + date[i];
		data = data + " ";
	}
	if (province_num > 0)
	{
		for (int i = 0;i < province_num;i++)data = data + province[i];
		data = data + " ";
	}
	if (type_num > 1)
	{
		for (int i = 1;i < type_num;i++)data = data + type[i];
		data = data + " ";
	}
	return data;
}

void Analysis::CheckType(int num)
{
	int c = num;
	if (!mi2.count("感染患者"))mi2["感染患者"] = c, type[c++] = "感染患者";
	if (!mi2.count("疑似患者"))mi2["疑似患者"] = c, type[c++] = "疑似患者";
	if (!mi2.count("治愈"))mi2["治愈"] = c, type[c++] = "治愈";
	if (!mi2.count("死亡"))mi2["死亡"] = c, type[c++] = "死亡";
	mi2["确诊感染"] = 11;
}

void Analysis::CheckAgain()
{
	if (type_num == 0)
	{
		int c = 1;
		if (!mi2.count("感染患者"))mi2["感染患者"] = c, type[c++] = "感染患者";
		if (!mi2.count("疑似患者"))mi2["疑似患者"] = c, type[c++] = "疑似患者";
		if (!mi2.count("治愈"))mi2["治愈"] = c, type[c++] = "治愈";
		if (!mi2.count("死亡"))mi2["死亡"] = c, type[c++] = "死亡";
		mi2["确诊感染"] = 11;
		type_num = c;
	}
}

string Analysis::Begin(string com)
{
	Init();
	char inpath[1005], date[105];
	
	int cc = 0;
	int size = com.size();
	for (int i = 0;i < size;i++)
	{
		string s = "";
		while (i < size && com[i] != ' ')
		{
			s = s + com[i];
			i++;
		}
		i++;
		if (s == "-log")
		{
			int c = 0;
			while (i < size && com[i] != ' ')
			{
				inpath[c] = com[i];
				i++, c++;
			}
			inpath[c] = '\0';
		}
		else if (s == "-out")
		{
			int c = 0;
			while (i < size && com[i] != ' ')
			{
				outpath[c] = com[i];
				i++, c++;
			}
			outpath[c] = '\0';
		}
		else if (s == "-date")
		{
			int c = 0;
			while (i < size && com[i] != ' ')
			{
				date[c] = com[i];
				i++, c++;
			}
			cc = c;
			date[c] = '\0';
		}
		else if (s == "-type")
		{
			int c = 1;
			string ss = "";
			while (i < size)
			{
				ss = ss + com[i];
				i++;
				if (com[i] == ' ' || i == size)
				{
					if (ss == "ip")mi2["感染患者"] = c, type[c++] = "感染患者";
					else if (ss == "sp")mi2["疑似患者"] = c, type[c++] = "疑似患者";
					else if (ss == "cure")mi2["治愈"] = c, type[c++] = "治愈";
					else if (ss == "dead")mi2["死亡"] = c, type[c++] = "死亡";
					i++;
					ss = "";
				}
			}
			type_num = c;
			CheckType(c);
		}
		else if (s == "-province")
		{
			int c = 0;
			while (i < size && (com[i] >= 'z' || com[i] <= 'a') && com[i] != '-')
			{
				if (com[i] == ' ')mi1[province[c]] = 1, c++;
				else province[c] = province[c] + com[i];
				i++;
			}
			if (i == size)mi1[province[c]] = 1, c++;
			if (com[i] >= 'z' || com[i] <= 'a')i--;
			province_num = c;
		}
		else if(com[i]==' ');
		else i--;
	}
	CheckAgain();
	FindAllFile(inpath, ".log.txt", date);
	return Check(cc,date);
	
}


int main(int argc, char* argv[])
{
	Analysis A;
	string s = "";
	for(int i=0;i<argc;i++)
	{
		int size=strlen(argv[i]);
		for(int j=0;j<size;j++)s=s+argv[i][j];
		s=s+" ";
	}
	//cout<<s;
	A.Begin(s);
	A.Out();
	
	return 0;
}


