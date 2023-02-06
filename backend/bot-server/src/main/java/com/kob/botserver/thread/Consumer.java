package com.kob.botserver.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.UUID;
import java.util.function.Supplier;

import org.joor.Reflect;

import com.kob.botserver.service.BotService;
import com.kob.common.model.dto.GameSituation;
import com.kob.common.util.GsonUtils;

import lombok.AllArgsConstructor;

/**
 * @author tongfs@stu.pku.edu.cn
 * @date 2023/2/4
 */
@AllArgsConstructor
public class Consumer implements Runnable {

    private static final String AFTER_CLASS_NAME = " implements java.util.function.Supplier<Integer>";
    private static final String CLASS_NAME = "com.kob.botserver.service.impl.SupplierImpl";

    private final GameSituation gameSituation;
    private final BotService botService;

    @Override
    public void run() {
        try {
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            String code = gameSituation.getBotCode();
            int i = code.indexOf(AFTER_CLASS_NAME);
            code = code.substring(0, i) + uuid + code.substring(i);

            Supplier<Integer> supplier = Reflect
                    .compile(CLASS_NAME + uuid, code)
                    .create()
                    .get();

            File file = new File(CLASS_NAME + uuid);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(GsonUtils.toJson(gameSituation));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            int nextStep = supplier.get();
            botService.sendNextStep(gameSituation.getUserId(), nextStep);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
