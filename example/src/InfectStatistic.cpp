/*
 * InfectStatistic
 * TODO
 *
 * @author 221701335 袁锦辉 
 * @version V1.0 
 * @since 2020.2.10 
 */

#include <iostream>
#include <fstream>
#include <string>
#include <io.h>
#include <vector> 
#include <cstdlib>
#include <windows.h>
#include <map> 
#include <vector> 

using namespace std;

//utf-8转为ansi函数 
string UnicodeToANSI(const std::wstring& strUnicode){
	int nAnsiLen = WideCharToMultiByte(CP_ACP,
		0,
		strUnicode.c_str(),
		-1,
		NULL,
		0,
		NULL,
		NULL);
	char *pAnsi = new char[nAnsiLen + 1];
	memset((void*)pAnsi, 0, (nAnsiLen + 1) * sizeof(char));
 
	::WideCharToMultiByte(CP_ACP,
		0,
		strUnicode.c_str(),
		-1,
		pAnsi,
		nAnsiLen,
		NULL,
		NULL);
 
	std::string strAnsi;
	strAnsi = pAnsi;
	delete[]pAnsi;
 
	return strAnsi;
}


wstring UTF8ToUnicode(const std::string& str){
	int nUnicodeLen = ::MultiByteToWideChar(CP_UTF8,
		0,
		str.c_str(),
		-1,
		NULL,
		0);
 
	wchar_t*  pUnicode;
	pUnicode = new wchar_t[nUnicodeLen + 1];
	memset((void*)pUnicode, 0, (nUnicodeLen + 1) * sizeof(wchar_t));
 
	::MultiByteToWideChar(CP_UTF8,
		0,
		str.c_str(),
		-1,
		(LPWSTR)pUnicode,
		nUnicodeLen);
 
	std::wstring  strUnicode;
	strUnicode = (wchar_t*)pUnicode;
	delete []pUnicode;
 
	return strUnicode;
}
string Utf8ToAnsi(const std::string &strUtf8){
	std::wstring strUnicode = UTF8ToUnicode(strUtf8);
	
	return UnicodeToANSI(strUnicode);
}


//获取特定格式的文件名    
void getAllFiles(string path, vector<string>& files,string format){	
	intptr_t  hFile = 0;//文件句柄  64位下long 改为 intptr_t
	struct _finddata_t fileinfo;//文件信息 
	string p;
	if ((hFile = _findfirst(p.assign(path).append("\\*" + format).c_str(), &fileinfo)) != -1) {
		do{
			if ((fileinfo.attrib & _A_SUBDIR)){//判断是否为文件夹
				if (strcmp(fileinfo.name, ".") != 0 && strcmp(fileinfo.name, "..") != 0){//文件夹名中不含"."和".."
					files.push_back(p.assign(path).append(fileinfo.name)); //保存文件夹名
					getAllFiles(p.assign(path).append(fileinfo.name), files,format); //递归遍历文件夹
				}
			}
			else{
				files.push_back(p.assign(path).append(fileinfo.name));//如果不是文件夹，储存文件名
				//files.push_back(p.assign(path).append("\\").append(fileinfo.name));
			}
		} while (_findnext(hFile, &fileinfo) == 0);
		_findclose(hFile);
	}
}


int main(int argc, char** argv) {
	//设置输出格式记录结构 
	int outTypeSet = 0;
	vector<string> outType;
	int outProvincesSet = 0;
	string provincesList[] = {"全国","安徽","北京","重庆","福建","甘肃","广东","广西","贵州","海南",
	"河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东",
	"山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江"}; 
	map<string,int> provincesOut;
	provincesOut["全国"] = 0;
	provincesOut["安徽"] = 0;
	provincesOut["北京"] = 0;
	provincesOut["重庆"] = 0;
	provincesOut["福建"] = 0;
	provincesOut["甘肃"] = 0;
	provincesOut["广东"] = 0;
	provincesOut["广西"] = 0;
	provincesOut["贵州"] = 0;
	provincesOut["海南"] = 0;
	provincesOut["河北"] = 0;
	provincesOut["河南"] = 0;
	provincesOut["黑龙江"] = 0;
	provincesOut["湖北"] = 0;
	provincesOut["湖南"] = 0;
	provincesOut["吉林"] = 0;
	provincesOut["江苏"] = 0;
	provincesOut["江西"] = 0;
	provincesOut["辽宁"] = 0;
	provincesOut["内蒙古"] = 0;
	provincesOut["宁夏"] = 0;
	provincesOut["青海"] = 0;
	provincesOut["山东"] = 0;
	provincesOut["山西"] = 0;
	provincesOut["陕西"] = 0; 
	provincesOut["上海"] = 0;
	provincesOut["四川"] = 0;
	provincesOut["天津"] = 0;
	provincesOut["西藏"] = 0;
	provincesOut["新疆"] = 0;
	provincesOut["云南"] = 0;
	provincesOut["浙江"] = 0;
	//设置各省人数统计map结构 
	map<string,int> provincesUndef;
	map<string,int> provincesDef;
	map<string,int> provincesCure;
	map<string,int> provincesDie;
	provincesUndef["全国"] = 0;
	provincesUndef["安徽"] = 0;
	provincesUndef["北京"] = 0;
	provincesUndef["重庆"] = 0;
	provincesUndef["福建"] = 0;
	provincesUndef["甘肃"] = 0;
	provincesUndef["广东"] = 0;
	provincesUndef["广西"] = 0;
	provincesUndef["贵州"] = 0;
	provincesUndef["海南"] = 0;
	provincesUndef["河北"] = 0;
	provincesUndef["河南"] = 0;
	provincesUndef["黑龙江"] = 0;
	provincesUndef["湖北"] = 0;
	provincesUndef["湖南"] = 0;
	provincesUndef["吉林"] = 0;
	provincesUndef["江苏"] = 0;
	provincesUndef["江西"] = 0;
	provincesUndef["辽宁"] = 0;
	provincesUndef["内蒙古"] = 0;
	provincesUndef["宁夏"] = 0;
	provincesUndef["青海"] = 0;
	provincesUndef["山东"] = 0;
	provincesUndef["山西"] = 0;
	provincesUndef["陕西"] = 0; 
	provincesUndef["上海"] = 0;
	provincesUndef["四川"] = 0;
	provincesUndef["天津"] = 0;
	provincesUndef["西藏"] = 0;
	provincesUndef["新疆"] = 0;
	provincesUndef["云南"] = 0;
	provincesUndef["浙江"] = 0;
	provincesDef["全国"] = 0;
	provincesDef["安徽"] = 0;
	provincesDef["北京"] = 0;
	provincesDef["重庆"] = 0;
	provincesDef["福建"] = 0;
	provincesDef["甘肃"] = 0;
	provincesDef["广东"] = 0;
	provincesDef["广西"] = 0;
	provincesDef["贵州"] = 0;
	provincesDef["海南"] = 0;
	provincesDef["河北"] = 0;
	provincesDef["河南"] = 0;
	provincesDef["黑龙江"] = 0;
	provincesDef["湖北"] = 0;
	provincesDef["湖南"] = 0;
	provincesDef["吉林"] = 0;
	provincesDef["江苏"] = 0;
	provincesDef["江西"] = 0;
	provincesDef["辽宁"] = 0;
	provincesDef["内蒙古"] = 0;
	provincesDef["宁夏"] = 0;
	provincesDef["青海"] = 0;
	provincesDef["山东"] = 0;
	provincesDef["山西"] = 0;
	provincesDef["陕西"] = 0; 
	provincesDef["上海"] = 0;
	provincesDef["四川"] = 0;
	provincesDef["天津"] = 0;
	provincesDef["西藏"] = 0;
	provincesDef["新疆"] = 0;
	provincesDef["云南"] = 0;
	provincesDef["浙江"] = 0;
	provincesCure["全国"] = 0;
	provincesCure["安徽"] = 0;
	provincesCure["北京"] = 0;
	provincesCure["重庆"] = 0;
	provincesCure["福建"] = 0;
	provincesCure["甘肃"] = 0;
	provincesCure["广东"] = 0;
	provincesCure["广西"] = 0;
	provincesCure["贵州"] = 0;
	provincesCure["海南"] = 0;
	provincesCure["河北"] = 0;
	provincesCure["河南"] = 0;
	provincesCure["黑龙江"] = 0;
	provincesCure["湖北"] = 0;
	provincesCure["湖南"] = 0;
	provincesCure["吉林"] = 0;
	provincesCure["江苏"] = 0;
	provincesCure["江西"] = 0;
	provincesCure["辽宁"] = 0;
	provincesCure["内蒙古"] = 0;
	provincesCure["宁夏"] = 0;
	provincesCure["青海"] = 0;
	provincesCure["山东"] = 0;
	provincesCure["山西"] = 0;
	provincesCure["陕西"] = 0; 
	provincesCure["上海"] = 0;
	provincesCure["四川"] = 0;
	provincesCure["天津"] = 0;
	provincesCure["西藏"] = 0;
	provincesCure["新疆"] = 0;
	provincesCure["云南"] = 0;
	provincesCure["浙江"] = 0;
	provincesDie["全国"] = 0;
	provincesDie["安徽"] = 0;
	provincesDie["北京"] = 0;
	provincesDie["重庆"] = 0;
	provincesDie["福建"] = 0;
	provincesDie["甘肃"] = 0;
	provincesDie["广东"] = 0;
	provincesDie["广西"] = 0;
	provincesDie["贵州"] = 0;
	provincesDie["海南"] = 0;
	provincesDie["河北"] = 0;
	provincesDie["河南"] = 0;
	provincesDie["黑龙江"] = 0;
	provincesDie["湖北"] = 0;
	provincesDie["湖南"] = 0;
	provincesDie["吉林"] = 0;
	provincesDie["江苏"] = 0;
	provincesDie["江西"] = 0;
	provincesDie["辽宁"] = 0;
	provincesDie["内蒙古"] = 0;
	provincesDie["宁夏"] = 0;
	provincesDie["青海"] = 0;
	provincesDie["山东"] = 0;
	provincesDie["山西"] = 0;
	provincesDie["陕西"] = 0; 
	provincesDie["上海"] = 0;
	provincesDie["四川"] = 0;
	provincesDie["天津"] = 0;
	provincesDie["西藏"] = 0;
	provincesDie["新疆"] = 0;
	provincesDie["云南"] = 0;
	provincesDie["浙江"] = 0;
	string targetProvince; 
	string inPath("");
	string outPath("");
	string date("");
	string file;
	string option,val;
	char temp[256];
	int numTemp;
	
	for(int i = 1; i < argc; i++){
		option = argv[i];
		if(option == "-log"){
			inPath = argv[++i];
		}
		else if(option == "-out"){
			outPath = argv[++i];
		}
		else if(option == "-date"){
			date = argv[++i];
		}
		else if(option == "-type"){
			outTypeSet = 1;
			while((i+1) < argc) {
				val = argv[i+1];
				if((val == "ip"||val == "sp"||val == "cure"||val == "dead")){
					outType.push_back(val);
					i++;
				}
				else
					break;
			}
		}
		else if(option == "-province"){
			outProvincesSet = 1;
			map<string,int>::iterator it;
			while((i+1) < argc){
				val = argv[i+1];
				it = provincesOut.find(val);
				if(it != provincesOut.end()){
					provincesOut.at(val) = 1;
					i++;
				}
				else
					break;
			}
			
		}
		else if(option == "list")
			continue;
		else{
			cout << endl << endl << option << endl << endl;
			cout << "输出参数错误" << endl;
			return -1;
		}
	}
	
	if(inPath.length() == 0){
		cout << "-log参数未指定" << endl;
		return -1;	
	} 
	
	if(outPath.length() == 0){
		cout << "-out参数未指定" << endl;
		return -1;	
	} 
	
	if(date.length() != 0){
		string dateTemp = date;
		date = inPath;
		date.append(dateTemp);
		date.append(".log.txt");
	}
	//读取日志文件夹 
	vector<string> files;
	string format = "";	//.txt
	getAllFiles(inPath, files, format);
	int size = files.size();
	for (int i = 0; i < size; i++){	
		if(date.length() != 0){
			if(files[i] > date)
				continue;		
		}
		const char *FILEPATH = const_cast<char *>(files[i].c_str()) ;
		ifstream fin(FILEPATH);
		//处理文档内容
		while(!fin.eof()){		 
			fin >> temp;
			string info = Utf8ToAnsi(temp);
			if(info == "//"){
				fin.getline(temp,255);
				continue;
			}
			targetProvince = info;
			fin >> temp;
			info = Utf8ToAnsi(temp);
			
			if(info == "排除"){
				fin >> temp;
				fin >> numTemp;
				fin >> temp;
				provincesUndef.at(targetProvince) -= numTemp;
				provincesUndef.at("全国") -= numTemp;
			}
			
			else if(info == "死亡"){
				fin >> numTemp;
				fin >> temp;
				provincesDie.at(targetProvince) += numTemp;
				provincesDie.at("全国") += numTemp;
				provincesDef.at(targetProvince) -= numTemp;
				provincesDef.at("全国") -= numTemp;
			}
			
			else if(info == "治愈"){
				fin >> numTemp;
				fin >> temp;
				provincesCure.at(targetProvince) += numTemp;
				provincesCure.at("全国") += numTemp;
				provincesDef.at(targetProvince) -= numTemp;
				provincesDef.at("全国") -= numTemp;
			}
			
			else if(info == "新增"){
				fin >> temp;
				info = Utf8ToAnsi(temp);
				if(info == "感染患者") {
					fin >> numTemp;
					fin >> temp;
					provincesDef.at(targetProvince) += numTemp;
					provincesDef.at("全国") += numTemp;
				} 
				else if(info == "疑似患者"){
					fin >> numTemp;
					fin >> temp;
					provincesUndef.at(targetProvince) += numTemp;
					provincesUndef.at("全国") += numTemp;
				} 
				else{
					cout << targetProvince << "相关文档数据错误" << endl;
					fin.getline(temp,255);
					continue;
				} 
			}
			
			else if(info == "感染患者"){
				fin >> temp;
				fin >> temp; 
				string tempProvince = Utf8ToAnsi(temp);
				fin >> numTemp;
				fin >> temp;
				provincesDef.at(targetProvince) -= numTemp;
				provincesDef.at(tempProvince) += numTemp;
			}
			
			else if(info == "疑似患者"){
				fin >> temp;
				info = Utf8ToAnsi(temp);
				if(info == "确诊感染"){
					fin >> numTemp;
					fin >> temp;
					provincesDef.at(targetProvince) += numTemp;
					provincesDef.at("全国") += numTemp;
					provincesUndef.at(targetProvince) -= numTemp;
					provincesUndef.at("全国") -= numTemp;
				}
				else if(info == "流入"){
					fin >> temp; 
					string tempProvince = Utf8ToAnsi(temp);
					fin >> numTemp;
					fin >> temp;
					provincesUndef.at(targetProvince) -= numTemp;
					provincesUndef.at(tempProvince) += numTemp;
				}
				else{
					cout << targetProvince << "相关文档数据错误" << endl;
					fin.getline(temp,255);
					continue; 
				} 	
			}
			
			else{
				cout << targetProvince << "相关文档数据错误" << endl;
				fin.getline(temp,255);
				continue; 
			} 
		}
		//当个文档处理完毕 
		fin.close();
	}
	//所有指定文档处理完毕
	const char *FILEPATH = const_cast<char *>(outPath.c_str()) ;
	ofstream fout(FILEPATH);
	if(outProvincesSet == 0 && outTypeSet == 0){
		for(int i = 0; i < 32; i++){
			fout << provincesList[i] << "\t感染患者" << provincesDef.at(provincesList[i]) << "\t疑似患者";
			fout << provincesUndef.at(provincesList[i]) << "\t治愈" << provincesCure.at(provincesList[i]) << "\t死亡";
			fout << provincesDie.at(provincesList[i]) << endl;
		}
	}
	else{
		if(outProvincesSet != 0 && outTypeSet == 0){
			for(int i = 0; i < 32; i++){
				if(provincesOut.at(provincesList[i]) == 1)
					fout << provincesList[i] << "\t感染患者" << provincesDef.at(provincesList[i]) << "\t疑似患者";
					fout << provincesUndef.at(provincesList[i]) << "\t治愈" << provincesCure.at(provincesList[i]) << "\t死亡";
					fout << provincesDie.at(provincesList[i]) << endl;
			}
		}
		else if(outProvincesSet == 0 && outTypeSet != 0){
			for(int i = 0; i < 32; i++){
				fout << provincesList[i];
				for (vector<string>::iterator iter = outType.begin(); iter != outType.end(); iter++){
					if(*iter == "ip"){
						fout << "\t感染患者" << provincesDef.at(provincesList[i]);
					}
					else if(*iter == "sp"){
						fout << "\t疑似患者" << provincesUndef.at(provincesList[i]);
					}
					else if(*iter == "cure"){
						fout << "\t治愈" << provincesCure.at(provincesList[i]);
					}
					else if(*iter == "dead"){
						fout << "\t死亡" << provincesDie.at(provincesList[i]);
					}
					else{
						cout << "-type参数错误" << endl;
						return -1; 
					}
				}
				fout << endl;
			}
		}
		else{
			for(int i = 0; i < 32; i++){
				if(provincesOut.at(provincesList[i]) == 1){
					fout << provincesList[i];
					for (vector<string>::iterator iter = outType.begin(); iter != outType.end(); iter++){
						if(*iter == "ip"){
							fout << "\t感染患者" << provincesDef.at(provincesList[i]);
						}
						else if(*iter == "sp"){
							fout << "\t疑似患者" << provincesUndef.at(provincesList[i]);
						}
						else if(*iter == "cure"){
							fout << "\t治愈" << provincesCure.at(provincesList[i]);
						}
						else if(*iter == "dead"){
							fout << "\t死亡" << provincesDie.at(provincesList[i]);
						}
						else{
							cout << "-type参数错误" << endl;
							return -1; 
						}
					}
					fout << endl;
				}
			}
			
		}
	}
	fout << "// 该文档并非真实数据，仅供测试使用" << endl;
	fout.close();
	cout << "统计完毕" << endl;
	
	return 0;
}
