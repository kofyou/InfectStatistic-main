/*
 * InfectStatistic
 * TODO
 *
 * @author 221701335 Ԭ���� 
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

//utf-8תΪansi���� 
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


//��ȡ�ض���ʽ���ļ���    
void getAllFiles(string path, vector<string>& files,string format){	
	intptr_t  hFile = 0;//�ļ����  64λ��long ��Ϊ intptr_t
	struct _finddata_t fileinfo;//�ļ���Ϣ 
	string p;
	if ((hFile = _findfirst(p.assign(path).append("\\*" + format).c_str(), &fileinfo)) != -1) {
		do{
			if ((fileinfo.attrib & _A_SUBDIR)){//�ж��Ƿ�Ϊ�ļ���
				if (strcmp(fileinfo.name, ".") != 0 && strcmp(fileinfo.name, "..") != 0){//�ļ������в���"."��".."
					files.push_back(p.assign(path).append(fileinfo.name)); //�����ļ�����
					getAllFiles(p.assign(path).append(fileinfo.name), files,format); //�ݹ�����ļ���
				}
			}
			else{
				files.push_back(p.assign(path).append(fileinfo.name));//��������ļ��У������ļ���
				//files.push_back(p.assign(path).append("\\").append(fileinfo.name));
			}
		} while (_findnext(hFile, &fileinfo) == 0);
		_findclose(hFile);
	}
}


int main(int argc, char** argv) {
	//���������ʽ��¼�ṹ 
	int outTypeSet = 0;
	vector<string> outType;
	int outProvincesSet = 0;
	string provincesList[] = {"ȫ��","����","����","����","����","����","�㶫","����","����","����",
	"�ӱ�","����","������","����","����","����","����","����","����","���ɹ�","����","�ຣ","ɽ��",
	"ɽ��","����","�Ϻ�","�Ĵ�","���","����","�½�","����","�㽭"}; 
	map<string,int> provincesOut;
	provincesOut["ȫ��"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["�㶫"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["�ӱ�"] = 0;
	provincesOut["����"] = 0;
	provincesOut["������"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["����"] = 0;
	provincesOut["���ɹ�"] = 0;
	provincesOut["����"] = 0;
	provincesOut["�ຣ"] = 0;
	provincesOut["ɽ��"] = 0;
	provincesOut["ɽ��"] = 0;
	provincesOut["����"] = 0; 
	provincesOut["�Ϻ�"] = 0;
	provincesOut["�Ĵ�"] = 0;
	provincesOut["���"] = 0;
	provincesOut["����"] = 0;
	provincesOut["�½�"] = 0;
	provincesOut["����"] = 0;
	provincesOut["�㽭"] = 0;
	//���ø�ʡ����ͳ��map�ṹ 
	map<string,int> provincesUndef;
	map<string,int> provincesDef;
	map<string,int> provincesCure;
	map<string,int> provincesDie;
	provincesUndef["ȫ��"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["�㶫"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["�ӱ�"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["������"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["���ɹ�"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["�ຣ"] = 0;
	provincesUndef["ɽ��"] = 0;
	provincesUndef["ɽ��"] = 0;
	provincesUndef["����"] = 0; 
	provincesUndef["�Ϻ�"] = 0;
	provincesUndef["�Ĵ�"] = 0;
	provincesUndef["���"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["�½�"] = 0;
	provincesUndef["����"] = 0;
	provincesUndef["�㽭"] = 0;
	provincesDef["ȫ��"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["�㶫"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["�ӱ�"] = 0;
	provincesDef["����"] = 0;
	provincesDef["������"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["����"] = 0;
	provincesDef["���ɹ�"] = 0;
	provincesDef["����"] = 0;
	provincesDef["�ຣ"] = 0;
	provincesDef["ɽ��"] = 0;
	provincesDef["ɽ��"] = 0;
	provincesDef["����"] = 0; 
	provincesDef["�Ϻ�"] = 0;
	provincesDef["�Ĵ�"] = 0;
	provincesDef["���"] = 0;
	provincesDef["����"] = 0;
	provincesDef["�½�"] = 0;
	provincesDef["����"] = 0;
	provincesDef["�㽭"] = 0;
	provincesCure["ȫ��"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["�㶫"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["�ӱ�"] = 0;
	provincesCure["����"] = 0;
	provincesCure["������"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["����"] = 0;
	provincesCure["���ɹ�"] = 0;
	provincesCure["����"] = 0;
	provincesCure["�ຣ"] = 0;
	provincesCure["ɽ��"] = 0;
	provincesCure["ɽ��"] = 0;
	provincesCure["����"] = 0; 
	provincesCure["�Ϻ�"] = 0;
	provincesCure["�Ĵ�"] = 0;
	provincesCure["���"] = 0;
	provincesCure["����"] = 0;
	provincesCure["�½�"] = 0;
	provincesCure["����"] = 0;
	provincesCure["�㽭"] = 0;
	provincesDie["ȫ��"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["�㶫"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["�ӱ�"] = 0;
	provincesDie["����"] = 0;
	provincesDie["������"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["����"] = 0;
	provincesDie["���ɹ�"] = 0;
	provincesDie["����"] = 0;
	provincesDie["�ຣ"] = 0;
	provincesDie["ɽ��"] = 0;
	provincesDie["ɽ��"] = 0;
	provincesDie["����"] = 0; 
	provincesDie["�Ϻ�"] = 0;
	provincesDie["�Ĵ�"] = 0;
	provincesDie["���"] = 0;
	provincesDie["����"] = 0;
	provincesDie["�½�"] = 0;
	provincesDie["����"] = 0;
	provincesDie["�㽭"] = 0;
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
			cout << "�����������" << endl;
			return -1;
		}
	}
	
	if(inPath.length() == 0){
		cout << "-log����δָ��" << endl;
		return -1;	
	} 
	
	if(outPath.length() == 0){
		cout << "-out����δָ��" << endl;
		return -1;	
	} 
	
	if(date.length() != 0){
		string dateTemp = date;
		date = inPath;
		date.append(dateTemp);
		date.append(".log.txt");
	}
	//��ȡ��־�ļ��� 
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
		//�����ĵ�����
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
			
			if(info == "�ų�"){
				fin >> temp;
				fin >> numTemp;
				fin >> temp;
				provincesUndef.at(targetProvince) -= numTemp;
				provincesUndef.at("ȫ��") -= numTemp;
			}
			
			else if(info == "����"){
				fin >> numTemp;
				fin >> temp;
				provincesDie.at(targetProvince) += numTemp;
				provincesDie.at("ȫ��") += numTemp;
				provincesDef.at(targetProvince) -= numTemp;
				provincesDef.at("ȫ��") -= numTemp;
			}
			
			else if(info == "����"){
				fin >> numTemp;
				fin >> temp;
				provincesCure.at(targetProvince) += numTemp;
				provincesCure.at("ȫ��") += numTemp;
				provincesDef.at(targetProvince) -= numTemp;
				provincesDef.at("ȫ��") -= numTemp;
			}
			
			else if(info == "����"){
				fin >> temp;
				info = Utf8ToAnsi(temp);
				if(info == "��Ⱦ����") {
					fin >> numTemp;
					fin >> temp;
					provincesDef.at(targetProvince) += numTemp;
					provincesDef.at("ȫ��") += numTemp;
				} 
				else if(info == "���ƻ���"){
					fin >> numTemp;
					fin >> temp;
					provincesUndef.at(targetProvince) += numTemp;
					provincesUndef.at("ȫ��") += numTemp;
				} 
				else{
					cout << targetProvince << "����ĵ����ݴ���" << endl;
					fin.getline(temp,255);
					continue;
				} 
			}
			
			else if(info == "��Ⱦ����"){
				fin >> temp;
				fin >> temp; 
				string tempProvince = Utf8ToAnsi(temp);
				fin >> numTemp;
				fin >> temp;
				provincesDef.at(targetProvince) -= numTemp;
				provincesDef.at(tempProvince) += numTemp;
			}
			
			else if(info == "���ƻ���"){
				fin >> temp;
				info = Utf8ToAnsi(temp);
				if(info == "ȷ���Ⱦ"){
					fin >> numTemp;
					fin >> temp;
					provincesDef.at(targetProvince) += numTemp;
					provincesDef.at("ȫ��") += numTemp;
					provincesUndef.at(targetProvince) -= numTemp;
					provincesUndef.at("ȫ��") -= numTemp;
				}
				else if(info == "����"){
					fin >> temp; 
					string tempProvince = Utf8ToAnsi(temp);
					fin >> numTemp;
					fin >> temp;
					provincesUndef.at(targetProvince) -= numTemp;
					provincesUndef.at(tempProvince) += numTemp;
				}
				else{
					cout << targetProvince << "����ĵ����ݴ���" << endl;
					fin.getline(temp,255);
					continue; 
				} 	
			}
			
			else{
				cout << targetProvince << "����ĵ����ݴ���" << endl;
				fin.getline(temp,255);
				continue; 
			} 
		}
		//�����ĵ�������� 
		fin.close();
	}
	//����ָ���ĵ��������
	const char *FILEPATH = const_cast<char *>(outPath.c_str()) ;
	ofstream fout(FILEPATH);
	if(outProvincesSet == 0 && outTypeSet == 0){
		for(int i = 0; i < 32; i++){
			fout << provincesList[i] << "\t��Ⱦ����" << provincesDef.at(provincesList[i]) << "\t���ƻ���";
			fout << provincesUndef.at(provincesList[i]) << "\t����" << provincesCure.at(provincesList[i]) << "\t����";
			fout << provincesDie.at(provincesList[i]) << endl;
		}
	}
	else{
		if(outProvincesSet != 0 && outTypeSet == 0){
			for(int i = 0; i < 32; i++){
				if(provincesOut.at(provincesList[i]) == 1)
					fout << provincesList[i] << "\t��Ⱦ����" << provincesDef.at(provincesList[i]) << "\t���ƻ���";
					fout << provincesUndef.at(provincesList[i]) << "\t����" << provincesCure.at(provincesList[i]) << "\t����";
					fout << provincesDie.at(provincesList[i]) << endl;
			}
		}
		else if(outProvincesSet == 0 && outTypeSet != 0){
			for(int i = 0; i < 32; i++){
				fout << provincesList[i];
				for (vector<string>::iterator iter = outType.begin(); iter != outType.end(); iter++){
					if(*iter == "ip"){
						fout << "\t��Ⱦ����" << provincesDef.at(provincesList[i]);
					}
					else if(*iter == "sp"){
						fout << "\t���ƻ���" << provincesUndef.at(provincesList[i]);
					}
					else if(*iter == "cure"){
						fout << "\t����" << provincesCure.at(provincesList[i]);
					}
					else if(*iter == "dead"){
						fout << "\t����" << provincesDie.at(provincesList[i]);
					}
					else{
						cout << "-type��������" << endl;
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
							fout << "\t��Ⱦ����" << provincesDef.at(provincesList[i]);
						}
						else if(*iter == "sp"){
							fout << "\t���ƻ���" << provincesUndef.at(provincesList[i]);
						}
						else if(*iter == "cure"){
							fout << "\t����" << provincesCure.at(provincesList[i]);
						}
						else if(*iter == "dead"){
							fout << "\t����" << provincesDie.at(provincesList[i]);
						}
						else{
							cout << "-type��������" << endl;
							return -1; 
						}
					}
					fout << endl;
				}
			}
			
		}
	}
	fout << "// ���ĵ�������ʵ���ݣ���������ʹ��" << endl;
	fout.close();
	cout << "ͳ�����" << endl;
	
	return 0;
}
