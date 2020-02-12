import java.io.*;
import java.text.Collator;
import java.util.*;

public class InfectStatistic {

	private HashMap<String, Map<String, Integer>> infectMap;

	public InfectStatistic() {
		infectMap = new HashMap<>();
		HashMap<String, Integer> map = new HashMap<>();
		map.put("感染患者", 0);
		map.put("疑似患者", 0);
		map.put("治愈", 0);
		map.put("死亡", 0);
		infectMap.put("全国", map);
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
			int count, country;
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
			} else { //治愈 死亡
				//感染者人数减少
				count = infectMap.get(province).get("感染患者") - Integer.valueOf(params[2].replaceAll("人", ""));
				infectMap.get(province).replace("感染患者", count);
				count = infectMap.get(province).get(params[1]) + Integer.valueOf(params[2].replaceAll("人", ""));
				infectMap.get(province).replace(params[1], count);
			}
		}
		//修正全国人数变化
		int ip = 0, sp = 0, cure = 0, dead = 0;
		for (Map.Entry<String, Map<String, Integer>> e : infectMap.entrySet()) {
			if (!e.getKey().equals("全国")) {
				for (Map.Entry<String, Integer> entry: e.getValue().entrySet()) {
					switch (entry.getKey()) {
						case "感染患者": ip += entry.getValue(); break;
						case "疑似患者": sp += entry.getValue(); break;
						case "治愈": cure += entry.getValue(); break;
						case "死亡": dead += entry.getValue(); break;
					}
				}
			}
		}
		infectMap.get("全国").replace("感染患者", ip);
		infectMap.get("全国").replace("疑似患者", sp);
		infectMap.get("全国").replace("治愈", cure);
		infectMap.get("全国").replace("死亡", dead);

		bufferedReader.close();
	}

	public void parseDirectory(String path, String date) throws IOException {
		File file = new File(path);
		File[] fileList = file.listFiles();
		if (date != null) {
			for (File f:fileList) {
				if (f.getName().substring(0,f.getName().indexOf('.')).compareTo(date) <= 0) {
//					System.out.println(f.getName());
					readFile(f);
				}
			}
		} else {
			for (File f: fileList) {
//				System.out.println(f.getName());
				readFile(f);
			}
		}

	}

	public void output(String path, List<String> typeList, List<String> provinceList) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		List<String> result = parseType(typeList, provinceList);
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).contains("全国")) {
				System.out.println(result.get(i));
				result.remove(i);
				break;
			}
		}
		//排序
		Collator cmp = Collator.getInstance(Locale.CHINA);
		result.sort(cmp);
		for (String s: result
		     ) {
			System.out.println(s);
		}
	}


	public List<String> parseType(List<String> typeList, List<String> provinceList) {
		List<String> typeName = new LinkedList<>();
		List<String> result = new LinkedList<>();
		if (typeList != null) {
			for (String s:typeList) {
				switch (s) {
					case "ip": typeName.add("感染患者"); break;
					case "sp": typeName.add("疑似患者"); break;
					case "cure": typeName.add("治愈"); break;
					case "dead": typeName.add("死亡"); break;
				}
			}
		} else {
			typeName.add("感染患者");
			typeName.add("疑似患者");
			typeName.add("治愈");
			typeName.add("死亡");
		}
		if (provinceList != null) {
			provinceList.add("全国");
			for (String province : provinceList) {
				StringBuilder sb = new StringBuilder();
				sb.append(province+ " ");
				for (String type : typeName) {
					sb.append(type+infectMap.get(province).get(type)+"人 ");
				}
				result.add(sb.toString());
			}
		} else {
			for (Map.Entry<String, Map<String, Integer>> e : infectMap.entrySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(e.getKey()+" ");
				for (String type : typeName) {
					sb.append(type+e.getValue().get(type)+"人 ");
				}
				result.add(sb.toString());
			}
		}
		return result;
	}

	public void parseArgs(String[] args) {
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
			System.out.println(input + " " + output + " " + date);
			for (String type: typeList
			     ) {
				System.out.println(type);
			}

			for (String p:provinceList)
				System.out.println(p);
		}
	}

	public static void main(String[] args) {
//		InfectStatistic infectStatistic = new InfectStatistic();
//		infectStatistic.parseArgs(args);
	}

	public HashMap<String, Map<String, Integer>> getInfectMap() {
		return infectMap;
	}
}
