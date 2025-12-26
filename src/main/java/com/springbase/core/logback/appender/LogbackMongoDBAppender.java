package com.springbase.core.logback.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;





public class LogbackMongoDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    public static final String DEFAULT_DB_NAME = "logbackDB";
    public static final String DEFAULT_COLLECTION_NAME = "prjLogs";
    public static final int DEFAULT_SERVER_SELECTION_TIMEOUT = 100;


    /** mongoDB host */
    private String host;
    /** mongoDB port */
    private int port;
    /** mongoDB dbName */
    private String dbName;
    /** mongoDB collectionName */
    private String collectionName;
    /** mongoDB username */
    private String username;
    /** mongoDB password */
    private String password;

    @Override
    public void start() {
        try {
            connect();
            super.start(); // To change body of overridden methods use File |
                           // Settings | File Templates.
        } catch (Exception e) {
            addError("MongoDB에 연결하지 못했습니다. host=" + host, e);
        }
    }

    /**
     * mongoDB 연결 동작 수행
     *
     * @throws UnknownHostException
     */
    private void connect() throws UnknownHostException {
    //     mongoClient = new MongoClient(new ServerAddress(host, port),
    //             MongoClientOptions.builder()
    //                     // 서버가 사용 불가능한 경우 즉시 타임아웃 발생하도록 0으로 설정
    //                     // .serverSelectionTimeout(DEFAULT_SERVER_SELECTION_TIMEOUT)
    //                     .build());

    //     MongoCredential mongoCredential = (username != null && password != null)
    //                 ? MongoCredential.createCredential(username, dbName,password.toCharArray()) : null; 
    //        //MongoCredential.createCredential(username, dbName,password.toCharArray())
    //     // 접속 유저 정보
    //     //UserCredentials userCredentials = (username != null && password != null)
    //    //         ? new UserCredentials(username, password) : UserCredentials.NO_CREDENTIALS;

    //     if (dbName == null || dbName.isEmpty())
    //         dbName = DEFAULT_DB_NAME;
   
        

    //    ServerAddress address = new ServerAddress("127.0.0.1", 62797);
    //    MongoClient mongoClient = new MongoClient(address, Arrays.asList(mongoCredential));
    //    MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, dbName);

    //     if (collectionName == null || collectionName.isEmpty())
    //         collectionName = DEFAULT_COLLECTION_NAME;

    //     if (!mongoTemplate.collectionExists(collectionName))
    //         mongoTemplate.createCollection(collectionName);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        // if (eventObject == null)
        //     return;

        // LogbackMongoDBDocument doc = createLogDocument(eventObject);
        // mongoTemplate.save(doc, collectionName);
    }

    /**
     * 저장할 log를 mongoDB Document로 생성
     *
     * @param event
     * @return
     */
    private static LogbackMongoDBDocument createLogDocument(ILoggingEvent event) {
        LogbackMongoDBDocument doc = new LogbackMongoDBDocument();

        doc.setLogger(event.getLoggerName());
        doc.setLevel(event.getLevel().levelStr);
        doc.setThreadName(event.getThreadName());
        doc.setTimeStamp(new Date(event.getTimeStamp()));
        doc.setMessage(event.getFormattedMessage());

        if (event.getMarker() != null) {
            doc.setMarker(event.getMarker().getName());
        }

        IThrowableProxy tp = event.getThrowableProxy();
        if (tp != null) {
            String tpStr = ThrowableProxyUtil.asString(tp);
            List<String> stackTrace = Arrays.asList(tpStr.replace("\t", "").split(CoreConstants.LINE_SEPARATOR));
            if (stackTrace.size() > 0) {
                doc.setException(stackTrace.get(0));
            }
            if (stackTrace.size() > 1) {
                doc.setStackTrace(stackTrace.subList(1, stackTrace.size()));
            }
        }
        return doc;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
