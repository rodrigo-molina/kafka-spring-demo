package com.example.spring.kafka.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;

public class KafkaWindowsUtils {
    private static final Logger LOGGER = LogManager.getLogger(KafkaWindowsUtils.class);

    public static final String OS_NAME_PROPERTY = "os.name";
    public static final String TMP_KAFKA_STREAMS_WINDOWS_DIRECTORY = "/tmp/kafka-streams/";

    private KafkaWindowsUtils() {
    }

    public static void deleteTemporalFolderIfWindows(final String applicationId) {
        if (System.getProperty(OS_NAME_PROPERTY).toLowerCase().contains("win")) {
            try {
                FileUtils.deleteDirectory(new File(TMP_KAFKA_STREAMS_WINDOWS_DIRECTORY + applicationId));
                FileUtils.forceMkdir(new File(TMP_KAFKA_STREAMS_WINDOWS_DIRECTORY + applicationId));
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }
}
