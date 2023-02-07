package com.kob.mainserver.constant;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/6
 */
public class Constants {
    public static final int WINNER_SCORE = 5;
    public static final int LOSER_SCORE = -3;
    public static final int DRAW_SCORE = -2;

    public static final String BOT_DEFAULT_NAME = "默认机器人";
    public static final String BOT_DEFAULT_DESC = "虽然我看起来不太聪明的亚子，但我会慢慢成长哒~";
    public static final String BOT_DEFAULT_CODE =
            "/**\n" +
                    " * 这里给出代码样例，你可以修改 getNextStep() 方法中的内容\n" +
                    " */\n" +
                    "\n" +
                    "package com.kob.botserver.service.impl;\n" +
                    "\n" +
                    "import java.io.BufferedReader;\n" +
                    "import java.io.File;\n" +
                    "import java.io.FileReader;\n" +
                    "import java.io.IOException;\n" +
                    "import java.util.Random;\n" +
                    "\n" +
                    "\n" +
                    "/**\n" +
                    " * @author tongfs@stu.pku.edu.cn\n" +
                    " * @date 2023/2/4\n" +
                    " */\n" +
                    "public class SupplierImpl implements java.util.function.Supplier<Integer> {\n" +
                    "\n" +
                    "    private static final int[] dx = {-1, 0, 1, 0};\n" +
                    "    private static final int[] dy = {0, 1, 0, -1};\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public Integer get() {\n" +
                    "        File file = new File(this.getClass().getName());\n" +
                    "        int result;\n" +
                    "        try (BufferedReader br = new BufferedReader(new FileReader(file))) {\n" +
                    "            result = getNextStep(br.readLine());\n" +
                    "        } catch (IOException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "            result = 1;\n" +
                    "        }\n" +
                    "        file.delete();\n" +
                    "        return result;\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 获得下一步的具体方法\n" +
                    "     */\n" +
                    "    private int getNextStep(String serializedStr) {\n" +
                    "        String[] splits = serializedStr.split(\"#\");\n" +
                    "\n" +
                    "        String[] head = splits[0].split(\",\");\n" +
                    "        int headX = Integer.parseInt(head[0]);\n" +
                    "        int headY = Integer.parseInt(head[1]);\n" +
                    "\n" +
                    "        String[] scale = splits[1].split(\",\");\n" +
                    "        int rows = Integer.parseInt(scale[0]);\n" +
                    "        int cols = Integer.parseInt(scale[1]);\n" +
                    "\n" +
                    "        String gameMapStr = splits[2];\n" +
                    "        int[][] gameMap = new int[rows][cols];\n" +
                    "        for (int i = 0, k = 0; i < rows; i++) {\n" +
                    "            for (int j = 0; j < cols; j++, k++) {\n" +
                    "                if (gameMapStr.charAt(k) == '1') {\n" +
                    "                    gameMap[i][j] = 1;\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        int[] directions = new int[4];\n" +
                    "        int maxDirections = -1;\n" +
                    "        for (int i = 0; i < 4; i++) {\n" +
                    "            int a = headX + dx[i];\n" +
                    "            int b = headY + dy[i];\n" +
                    "            if (a >= 0 && a < rows && b >= 0 && b < cols && gameMap[a][b] == 0) {\n" +
                    "                directions[i] = explore(a, b, gameMap);\n" +
                    "            } else {\n" +
                    "                directions[i] = -1;\n" +
                    "            }\n" +
                    "            maxDirections = Math.max(maxDirections, directions[i]);\n" +
                    "        }\n" +
                    "\n" +
                    "        if (maxDirections == -1) return 0;\n" +
                    "\n" +
                    "        Random random = new Random();\n" +
                    "        while (true) {\n" +
                    "            int d = random.nextInt(4);\n" +
                    "            if (directions[d] == maxDirections) {\n" +
                    "                return d;\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 探索一下如果下一步走 (x, y)，则再下一步有多少个可选位置，将结果记录在\n" +
                    "     */\n" +
                    "    private int explore(int x, int y, int[][] gameMap) {\n" +
                    "        int rows = gameMap.length;\n" +
                    "        int cols = gameMap[0].length;\n" +
                    "        int res = 0;\n" +
                    "        for (int i = 0; i < 4; i++) {\n" +
                    "            int a = x + dx[i], b = y + dy[i];\n" +
                    "            if (a >= 0 && a < rows && b >= 0 && b < cols && gameMap[a][b] == 0) {\n" +
                    "                res++;\n" +
                    "            }\n" +
                    "        }\n" +
                    "        return res;\n" +
                    "    }\n" +
                    "}\n";

    public static final long BENBEN_ID = 0;
}
