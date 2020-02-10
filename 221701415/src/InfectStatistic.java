import java.io.*;
import java.util.*;

public class InfectStatistic {

	private HashMap<String, Map<String, Integer>> infectMap;

	public InfectStatistic() {
		infectMap = new HashMap<>();
	}

	public void readFile(File file) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String textLine = null;
		while ((textLine = bufferedReader.readLine())!=null) {
			if (textLine.contains("//"))
				continue;
			String[] params = textLine.split(" ");
			String province = params[0];
			if (!infectMap.containsKey(province)) {
				HashMap<String, Integer> map = new HashMap<>();
				map.put("感染患者", 0);
				map.put("疑似患者", 0);
				map.put("治愈", 0);
				map.put("死亡", 0);
				infectMap.put(province, map);
			}
			int count;
			//解析当前行
			if (textLine.contains("新增")) {
				//新增人数
				count = infectMap.get(province).get(params[2]) + Integer.valueOf(params[3].replaceAll("人", ""));
				infectMap.get(province).replace(params[2], count);
			} else if (textLine.contains("流入")) {
				//流出省份减少人数
				count = infectMap.get(province).get(params[1]) - Integer.valueOf(params[4].replaceAll("人", ""));
				infectMap.get(province).replace(params[1], count);

				//流入省份增加人数
				count = infectMap.get(params[3]).get(params[1]) + Integer.valueOf(params[4].replaceAll("人", ""));
				infectMap.get(params[3]).replace(params[1], count);
			} else if (textLine.contains("确诊感染")) {
				//感染者人数增加
				count = infectMap.get(province).get("感染患者") + Integer.valueOf(params[3].replaceAll("人", ""));
				infectMap.get(province).replace("感染患者", count);

				//疑似患者人数减少
				count = infectMap.get(province).get("疑似患者") - Integer.valueOf(params[3].replaceAll("人", ""));
				infectMap.get(province).replace("疑似患者", count);
			} else if (textLine.contains("排除")) {
				//疑似患者人数减少
				count = infectMap.get(province).get("疑似患者") - Integer.valueOf(params[3].replaceAll("人", ""));
				infectMap.get(province).replace("疑似患者", count);
			} else {
				//感染者人数减少
				count = infectMap.get(province).get("感染患者") - Integer.valueOf(params[2].replaceAll("人", ""));
				infectMap.get(province).replace("感染患者", count);
				infectMap.get(province).replace(params[1], Integer.valueOf(params[2].replaceAll("人", "")));
			}

		}

		bufferedReader.close();
	}


	public static void main(String[] args) {
		int optNumber = 0;
		if (args[0].equals("list")) {
			String input = null, output = null, date = null;
			List<String> typeList = new LinkedList<>(), provinceList = new LinkedList<>();
			while (optNumber < args.length) {
				if (args[optNumber].equals("-log")) {
					input = args[++optNumber];
				} else if (args[optNumber].equals("-out")) {
					output = args[++optNumber];
				} else if (args[optNumber].equals("-date")) {
					date = args[++optNumber];
				} else if (args[optNumber].equals("-type")) {
					String type = args[++optNumber];
					while (!type.contains("-")) {
						typeList.add(type);
						if (optNumber == args.length-1)
							break;
						type = args[++optNumber];

					}
				} else if (args[optNumber].equals("-province")) {
					String province = args[++optNumber];
					while (!province.contains("-")) {
						provinceList.add(province);
						if (optNumber == args.length-1)
							break;
						province = args[++optNumber];
					}
				} else {
					optNumber++;
				}
			}
			System.out.println(input+output+date);
			for (int i = 0; i < typeList.size(); i++) {
				System.out.println(typeList.get(i));
			}
			for (int i = 0; i < provinceList.size(); i++) {
				System.out.println(provinceList.get(i));
			}
		}
	}

	public HashMap<String, Map<String, Integer>> getInfectMap() {
		return infectMap;
	}
}
