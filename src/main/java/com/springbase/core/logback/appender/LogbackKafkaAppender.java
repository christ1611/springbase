package com.springbase.core.logback.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.github.danielwegener.logback.kafka.KafkaAppender;
import com.github.danielwegener.logback.kafka.delivery.AsynchronousDeliveryStrategy;
import com.github.danielwegener.logback.kafka.keying.NoKeyKeyingStrategy;
import com.springbase.core.logback.properties.LogRuleProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import static com.springbase.core.logback.LogbackDefine.*;




@Slf4j
public class LogbackKafkaAppender {

    private final LogRuleProperties logRuleProperties;

    public LogbackKafkaAppender(LogRuleProperties logRuleProperties) {
        this.logRuleProperties = logRuleProperties;
    }

    public void setAppender(Logger logger) {
        log.info("[Logback] Logstash Appender Configuration Start, logger = [{}]", logger.getName());

        String appender = logger.getName() + DEFAULT_APPENDER_NAME_LOGSTASH;

        LoggerContext loggerContext = ((ch.qos.logback.classic.Logger) logger).getLoggerContext();

        KafkaAppender<ILoggingEvent> kafkaAppender = new KafkaAppender<>();
        kafkaAppender.setName(appender);
        kafkaAppender.setContext(loggerContext);



        kafkaAppender.setKeyingStrategy  ( new NoKeyKeyingStrategy          () ) ;
        kafkaAppender.setDeliveryStrategy( new AsynchronousDeliveryStrategy () );

        LogRuleProperties.Kafka kafkaConf = logRuleProperties.getKafka();
        // JsonLayout 설정
        LogbackLayout logbackLayout = new LogbackLayout();
        if( kafkaConf.isJsonLayout() )
        {
            kafkaAppender.setEncoder(logbackLayout.setJsonLayout());
        }
        else
        {
            kafkaAppender.setEncoder(logbackLayout.setPatternLayout(loggerContext, ENCODER_PATTERN_CONSOLE));
        }

        kafkaAppender.addProducerConfig("bootstrap.servers=" + kafkaConf.getBootstrapServers() );
        kafkaAppender.setTopic( kafkaConf.getTopic() );


        // 기존에 add 되어있다면 중복되는 문제. 일단 detach 시킴.
        ((ch.qos.logback.classic.Logger) logger).detachAppender(appender);
        ((ch.qos.logback.classic.Logger) logger).addAppender(kafkaAppender);

        log.debug("[Logback] Kafka Appender BootStrapServers[{}], Topic[{}]", kafkaConf.getBootstrapServers(), kafkaConf.getTopic());
        log.info("[Logback] Logstash Appender Configuration End, Set Appender = [{}]", kafkaAppender.getName());
    }
}
