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
    public static final String BOT_DEFAULT_DESC = "我虽然看起来不太聪明的亚子，但我会慢慢成长哒~";
    public static final String BOT_DEFAULT_CODE =
            "/**\n" +
                    " * 这里给出代码样例，你可以修改 getNextStep() 方法中的内容\n" +
                    " * <p>\n" +
                    " *  class GameSituation {\n" +
                    " *      private long userId;\n" +
                    " *      private Cell head;\n" +
                    " *      private String botCode;\n" +
                    " *      private String botCode;\n" +
                    " *      private int[][] gameMap;\n" +
                    " *      private int round;\n" +
                    " *      private LinkedList<Cell> body1, body2;\n" +
                    " *  }\n" +
                    " * <p>\n" +
                    " *  class Cell {\n" +
                    " *      private int x, y;\n" +
                    " *  }\n" +
                    " */\n" +
                    "\n" +
                    "package com.kob.botserver.service.impl;\n" +
                    "\n" +
                    "import static com.kob.common.constant.Constants.COLS;\n" +
                    "import static com.kob.common.constant.Constants.ROWS;\n" +
                    "import static com.kob.common.constant.Constants.UP;\n" +
                    "import static com.kob.common.constant.Constants.dx;\n" +
                    "import static com.kob.common.constant.Constants.dy;\n" +
                    "\n" +
                    "import java.io.BufferedReader;\n" +
                    "import java.io.File;\n" +
                    "import java.io.FileReader;\n" +
                    "import java.io.IOException;\n" +
                    "import java.util.LinkedList;\n" +
                    "import java.util.List;\n" +
                    "import java.util.Random;\n" +
                    "\n" +
                    "import com.kob.common.model.dto.Cell;\n" +
                    "import com.kob.common.model.dto.GameSituation;\n" +
                    "import com.kob.common.util.GsonUtils;\n" +
                    "\n" +
                    "/**\n" +
                    " * @author tongfs@stu.pku.edu.cn\n" +
                    " * @date 2023/2/4\n" +
                    " */\n" +
                    "public class SupplierImpl implements java.util.function.Supplier<Integer> {\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public Integer get() {\n" +
                    "        File file = new File(this.getClass().getName());\n" +
                    "        int result;\n" +
                    "        try (BufferedReader br = new BufferedReader(new FileReader(file))) {\n" +
                    "            result = getNextStep(GsonUtils.fromJson(br.readLine(), GameSituation.class));\n" +
                    "        } catch (IOException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "            result = UP;\n" +
                    "        }\n" +
                    "        file.delete();\n" +
                    "        return result;\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 获得下一步的具体方法\n" +
                    "     */\n" +
                    "    private int getNextStep(GameSituation gameSituation) {\n" +
                    "        Cell head = gameSituation.getHead();\n" +
                    "        int[][] gameMap = gameSituation.getGameMap();\n" +
                    "        LinkedList<Cell> body1 = gameSituation.getBody1();\n" +
                    "        LinkedList<Cell> body2 = gameSituation.getBody2();\n" +
                    "\n" +
                    "        if (!isIncreasing(gameSituation.getRound())) {\n" +
                    "            body1.poll();\n" +
                    "            body2.poll();\n" +
                    "        }\n" +
                    "\n" +
                    "        updateMap(gameMap, body1, body2);\n" +
                    "\n" +
                    "        int[] directions = new int[4];  // -1表示该方向不可走，非负表示选择该方向后下一步还有几个位置可走(0~3)\n" +
                    "        int maxDirections = -1;  // 下下一步最多有几个位置可走\n" +
                    "        for (int i = 0; i < 4; i++) {\n" +
                    "            int a = head.getX() + dx[i];\n" +
                    "            int b = head.getY() + dy[i];\n" +
                    "            if (a >= 0 && a < ROWS && b >= 0 && b < COLS && gameMap[a][b] == 0) {\n" +
                    "                directions[i] = explore(a, b, gameMap);\n" +
                    "            } else {\n" +
                    "                directions[i] = -1;\n" +
                    "            }\n" +
                    "            maxDirections = Math.max(maxDirections, directions[i]);\n" +
                    "        }\n" +
                    "\n" +
                    "        if (maxDirections == -1) return UP;\n" +
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
                    "     * 当前回合蛇的身体是否要增长\n" +
                    "     */\n" +
                    "    private boolean isIncreasing(int round) {\n" +
                    "        if (round <= 5) return true;\n" +
                    "        if (round <= 11) return (round & 1) == 1;\n" +
                    "        return round % 3 == 2;\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 更新地图\n" +
                    "     */\n" +
                    "    private void updateMap(int[][] gameMap, List<Cell> body1, List<Cell> body2) {\n" +
                    "        for (Cell cell : body1) {\n" +
                    "            gameMap[cell.getX()][cell.getY()] = 1;\n" +
                    "        }\n" +
                    "        for (Cell cell : body2) {\n" +
                    "            gameMap[cell.getX()][cell.getY()] = 1;\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 探索一下如果下一步走 (x, y)，则再下一步有多少个可选位置，将结果记录在\n" +
                    "     */\n" +
                    "    private int explore(int x, int y, int[][] gameMap) {\n" +
                    "        int res = 0;\n" +
                    "        for (int i = 0; i < 4; i++) {\n" +
                    "            int a = x + dx[i], b = y + dy[i];\n" +
                    "            if (a >= 0 && a < ROWS && b >= 0 && b < COLS && gameMap[a][b] == 0) {\n" +
                    "                res++;\n" +
                    "            }\n" +
                    "        }\n" +
                    "        return res;\n" +
                    "    }\n" +
                    "}\n";

    public static final long BENBEN_ID = 0;
}
