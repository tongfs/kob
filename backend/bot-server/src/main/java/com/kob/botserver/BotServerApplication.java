<<<<<<<< HEAD:backend/main-server/src/main/java/com/kob/backend/mainserver/MainServerApplication.java
package com.kob.backend.mainserver;
========
package com.kob.botserver;
>>>>>>>> dev:backend/bot-server/src/main/java/com/kob/botserver/BotServerApplication.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
<<<<<<<< HEAD:backend/main-server/src/main/java/com/kob/backend/mainserver/MainServerApplication.java
public class MainServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServerApplication.class, args);
========
public class BotServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotServerApplication.class, args);
>>>>>>>> dev:backend/bot-server/src/main/java/com/kob/botserver/BotServerApplication.java
    }

}
