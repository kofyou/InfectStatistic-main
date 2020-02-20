
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

class analysis
{
public:
	char outpath[1005];
	string province[40];
	map<string, int>mi;
	map<string, int>mi1;
	map<string, int>mi2;
	map<string, int>mi3;
	string p[40];
	string type[20];
	int num, num1, opp;
	int out[40][6];
	int Num(string tmp);

	void findAllFile(const char* path, const char* format, const char* date);
	string begin(string com);
	void init();
	void Out();
	int check;
	int Detail(string p, string t) {
		return out[mi[p]][mi2[t]];
	}
};

int analysis::Num(string tmp)
{
	int num = 0, c = 1;
	for (int i = tmp.size() - 1;i >= 0;i--)
	{
		if (tmp[i] >= '0' && tmp[i] <= '9')num += (tmp[i] - '0') * c, c *= 10;
	}
	return num;
}
void analysis::findAllFile(const char* path, const char* format, const char* date)
{
	check = 1;
	char newpath[200];
	char newpaths[200];
	strcpy(newpath, path);
	strcpy(newpaths, path);
	strcat(newpath, "\\*.*");    // 在目录后面加上"\\*.*"进行第一次搜索
	int handle;
	int b = 0;
	_finddata_t findData;
	check = 2;
	handle = _findfirst(newpath, &findData);
	if (handle == -1)
	{
		check = 3;
		// 检查是否成功
		return;
	}
	while (_findnext(handle, &findData) == 0) {
		if (findData.attrib & _A_SUBDIR) {
			if (strcmp(findData.name, ".") == 0 || strcmp(findData.name, "..") == 0)
				continue;
			strcpy(newpath, path);
			strcat(newpath, "\\");
			strcat(newpath, findData.name);
			findAllFile(newpath, format, date);
		}
		else {
			if (strstr(findData.name, format))
			{     //判断是不是txt文件 
				
				int size = strlen(date);
				int fl = 1;
				if (size != 0)
				{
					for (int i = 0;i < size;i++)
					{
						if (findData.name[i] != date[i])fl = 0;
						if (findData.name[i] > date[i])b = 1;
					}
				}
				if (b == 1)return ;
				if (fl == 1&&size != 0)b = 1;
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
					int fl1 = 0, fl2 = 0;
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
							else if (fl1 == 0)fl1 = mi[s];
							else fl2 = mi[s];
							s = "";
						}
						else s = s + tmp[j];
					}
					int f1 = 0, f2 = 0;
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
							if (f1 == 0)f1 = mi2[ss];
							else f2 = mi2[ss];
							ss = "";
						}
						else ss = ss + tmp[j];
					}
					check = 1;
					int num = Num(tmp);
					//	cout<<fl1<<' '<<fl2<<' '<<f1<<' '<<f2<<' '<<num<<endl;
					if (fl2 == 0 && f2 == 0)
					{
						if (l == 100)out[fl1][f1] += num;
						else if (l == 101)out[fl1][f1] -= num;
						else if (l == 102)out[fl1][mi2["感染患者"]] -= num, out[fl1][mi2["治愈"]] += num;
						else out[fl1][mi2["死亡"]] += num, out[fl1][mi2["感染患者"]] -= num;
					}
					else if (fl2 == 0 && f2 != 0)out[fl1][mi2["疑似患者"]] -= num, out[fl1][mi2["感染患者"]] += num;
					else out[fl1][f1] -= num, out[fl2][f1] += num;
					if (out[fl1][f1] != 0)check = out[fl1][f1];
				}
				if (b == 1)return;
			}
		}
	}
	_findclose(handle);    // 关闭搜索句柄
}
void analysis::init()
{
	memset(out, 0, sizeof(out));
	for (int i = 0;i <= 35;i++)province[i] = "";
	mi.clear();
	mi1.clear();
	mi2.clear();
	mi3.clear();
	num = 0, num1 = 0;
	mi["新增"] = 100;
	mi["排除"] = 101;
	mi["治愈"] = 102;
	mi["死亡"] = 103;
	mi["全国"] = 1;
	mi["安徽"] = 2, mi["北京"] = 3, mi["重庆"] = 4, mi["福建"] = 5, mi["甘肃"] = 6, mi["广东"] = 7;
	mi["广西"] = 8, mi["贵州"] = 9, mi["海南"] = 10, mi["河北"] = 11, mi["河南"] = 12, mi["黑龙江"] = 13;
	mi["湖北"] = 14, mi["湖南"] = 15, mi["吉林"] = 16, mi["江苏"] = 17, mi["江西"] = 18, mi["辽宁"] = 19;
	mi["内蒙古"] = 20, mi["宁夏"] = 21, mi["青海"] = 22, mi["山东"] = 23, mi["山西"] = 24, mi["陕西"] = 25;
	mi["上海"] = 26, mi[""] = 27, mi["四川"] = 28, mi["台湾"] = 29, mi["天津"] = 30, mi["西藏"] = 31;
	mi["香港"] = 32, mi["新疆"] = 33, mi["云南"] = 34,mi["浙江"];
	p[1] = "全国", p[2] = "安徽", p[3] = "北京", p[4] = "重庆", p[5] = "福建", p[6] = "甘肃";
	p[7] = "广东", p[8] = "广西", p[9] = "贵州", p[10] = "海南", p[11] = "河北", p[12] = "河南" ;
	p[13] = "黑龙江", p[14] = "湖北", p[15] = "湖南", p[16] = "吉林", p[17] = "江苏", p[18] = "江西";
	p[19] = "辽宁", p[20] = "内蒙古", p[21] = "宁夏", p[22] = "青海", p[23] = "山东", p[24] = "山西";
	p[25] = "陕西", p[26] = "上海", p[28] = "四川", p[29] = "台湾", p[30] = "天津", p[31] = "西藏";
	p[32] = "香港", p[33] = "新疆", p[34] = "云南",p[35]="浙江";
	p[27]="-";
}


void  analysis::Out()
{
	int o = 0;
	for (int i = 2;i < 36;i++)
	{
		for (int j = 1;j < num1;j++)
		{
			out[1][j] += out[i][j];
		}
	}
	for (int i = 1;i < 36;i++)
	{
		if (mi1.count(p[i]) || (num == 0 &&(mi3.count(p[i]) || i == 1))) 
		{
			cout << p[i] << ' ';
			for (int j = 1;j < num1;j++)
			{
				cout << type[j] << out[i][j] << "人 ";
				if (out[i][j]!=0)check = out[i][j];
			}
			if (mi1[p[i]] == 1 || num == 0)
			{
				cout << "\n";
			}
		}
	}
	cout << "// 该文档并非真实数据，仅供测试使用" << "\n";
}
string analysis::begin(string com)
{
	init();
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
			num1 = c;
			if (!mi2.count("感染患者"))mi2["感染患者"] = c, type[c++] = "感染患者";
			if (!mi2.count("疑似患者"))mi2["疑似患者"] = c, type[c++] = "疑似患者";
			if (!mi2.count("治愈"))mi2["治愈"] = c, type[c++] = "治愈";
			if (!mi2.count("死亡"))mi2["死亡"] = c, type[c++] = "死亡";
			mi2["确诊感染"] = 11;
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
			num = c;
		}
		else if(com[i]==' ');
		else i--;
	}
	if (num1 == 0)
	{
		int c = 1;
		if (!mi2.count("感染患者"))mi2["感染患者"] = c, type[c++] = "感染患者";
		if (!mi2.count("疑似患者"))mi2["疑似患者"] = c, type[c++] = "疑似患者";
		if (!mi2.count("治愈"))mi2["治愈"] = c, type[c++] = "治愈";
		if (!mi2.count("死亡"))mi2["死亡"] = c, type[c++] = "死亡";
		mi2["确诊感染"] = 11;
		num1 = c;
	}
	
	findAllFile(inpath, ".log.txt", date);
	string o = "";
	if (cc > 0)
	{
		for (int i = 0;i < cc;i++)o = o + date[i];
		o = o + " ";
	}
	if (num > 0)
	{
		for (int i = 0;i < num;i++)o = o + province[i];
		o = o + " ";
	}
	if (num1 > 1)
	{
		for (int i = 1;i < num1;i++)o = o + type[i];
		o = o + " ";
	}
	return o;

}


int main(int argc, char* argv[])
{
	analysis A;
	string s = "";
	for(int i=0;i<argc;i++)
	{
		int size=strlen(argv[i]);
		for(int j=0;j<size;j++)s=s+argv[i][j];
		s=s+" ";
	}
	//cout<<s;
	A.begin(s);
	A.Out();
	
	return 0;
}


