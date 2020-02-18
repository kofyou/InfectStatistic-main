#-*- coding:utf8 -*-
#!/usr/bin/python

# 
#  InfectStatistic
#  TODO
#  读取文件，列出全国和各省在某日的感染情况，输出到输出文件中
#  @author 091700410 傅少华
#  @version 1.0
#  @since 2020-2-18
#  

def sort_by_pinyin(str):
    
    province_dict = {
        '全国':0,'澳门':1,'安徽':2,
        '北京':3,'重庆':4,'福建':5,
        '甘肃':6,'广东':7,'广西':8,
        '贵州':9,'海南':10,'河北':11,
        '河南':12,'黑龙江':13,'湖北':14,
        '湖南':15,'吉林':16,'江苏':17,
        '江西':18,'辽宁':19,'内蒙古':20,
        '宁夏':21,'青海':22,'山东':23,
        '山西':24,'陕西':25,'上海':26,
        '四川':27,'台湾':28,'天津':29,
        '香港':30,'西藏':31,'新疆':32,
        '云南':33,'浙江':34
    }
    
    return province_dict[str]


class InfectStatistic:
    
    data_dict = {'全国':{'感染患者':0,'疑似患者':0,'治愈':0,'死亡':0}}
    
    def __init__(self,input_path,output_path,file_date):
        
        self.input_path = input_path
        self.output_path = output_path
        self.file_date = file_date
    
    def file_data(self,input_list):
        
        province = input_list[0]
        
        if province in InfectStatistic.data_dict:
            pass
        else:
            InfectStatistic.data_dict.update({province:{'感染患者':0,'疑似患者':0,'治愈':0,'死亡':0}})
            
        if input_list[1] == '新增' or input_list[1] == '排除':
            type = input_list[2]
            num = int(str(input_list[3]).replace('人', ''))
            if input_list[1] == '新增':
                InfectStatistic.data_dict[province][type] += num
                InfectStatistic.data_dict['全国'][type] += num
            else:
                InfectStatistic.data_dict[province][type] -= num
                InfectStatistic.data_dict['全国'][type] -= num
        
        if input_list[1] == '死亡' or input_list[1] == '治愈':
            num = int(str(input_list[2]).replace('人', ''))
            InfectStatistic.data_dict[province][input_list[1]] += num
            InfectStatistic.data_dict[province]['感染患者'] -= num
            InfectStatistic.data_dict['全国'][input_list[1]] += num
            InfectStatistic.data_dict['全国']['感染患者'] -= num
            
        if input_list[2] == '流入':
            num = int(str(input_list[4]).replace('人', ''))
            type = input_list[1]
            InfectStatistic.data_dict[province][type] -= num
            InfectStatistic.data_dict[input_list[3]][type] += num
        
        if input_list[2] == '确诊感染':
            num = int(str(input_list[3]).replace('人', ''))
            InfectStatistic.data_dict[province]['感染患者'] += num
            InfectStatistic.data_dict[province]['疑似患者'] -= num
            InfectStatistic.data_dict['全国']['感染患者'] += num
            InfectStatistic.data_dict['全国']['疑似患者'] -= num
            
        
    def read_file(self):
        
        f = open(self.input_path + self.file_date + '.log.txt','r',encoding='utf-8')
        for line in f:
            if line[0:2] == '//':
                pass
            else:
                content = line.split()
                print(str(content))
                InfectStatistic.file_data(self,content)
                
        print(InfectStatistic.data_dict)
        
fi = InfectStatistic('..\\log\\','','2020-01-22')
fi.read_file()
        
