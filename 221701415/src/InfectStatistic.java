import java.util.LinkedList;
import java.util.List;

public class InfectStatistic {

	public static void main(String[] args) {
		int optNumber = 0;
		if (args[0].equals("list")) {
			String input = null, output = null, date = null;
			List<String> typeList = new LinkedList<>(), provinceList = new LinkedList<>();
			while (optNumber < args.length) {
				if (args[optNumber].equals("-l") || args[optNumber].equals("--log")) {
					input = args[++optNumber];
				} else if (args[optNumber].equals("-o") || args[optNumber].equals("--out")) {
					output = args[++optNumber];
				} else if (args[optNumber].equals("-d") || args[optNumber].equals("--date")) {
					date = args[++optNumber];
				} else if (args[optNumber].equals("-t") || args[optNumber].equals("--type")) {
					String type = args[++optNumber];
					while (!type.contains("-")) {
						typeList.add(type);
						if (optNumber == args.length-1)
							break;
						type = args[++optNumber];

					}
				} else if (args[optNumber].equals("-p") || args[optNumber].equals("--province")) {
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
}
