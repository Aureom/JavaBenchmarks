package br.ufu.kaiosouza.javabenchmarks;

import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
Benchmark                                     Mode  Cnt     Score     Error  Units

StringFormatVsAppend.stringBlock              avgt    5     1,177    ±0,044   s/op
StringFormatVsAppend.stringBuffer             avgt    5     0,732    ±0,018   s/op
StringFormatVsAppend.stringBuilder            avgt    5     0,733    ±0,256   s/op
StringFormatVsAppend.stringConcat             avgt    5     0,204    ±0,027   s/op
StringFormatVsAppend.stringFormat             avgt    5     1,268    ±0,178   s/op

 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class StringFormatVsAppend {

    protected String AUTH_PRINCIPAL;
    protected String RESPONSE_URI;
    protected String RESPOSE_STATUS;
    protected String REQUEST_METHOD;
    protected String REQUEST_BODY;
    protected String RESPONDE_BODY;
    protected long TIME_TAKEN;

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Setup
    public void setup(){
        AUTH_PRINCIPAL = RandomStringUtils.randomAlphanumeric(19);
        RESPONSE_URI = RandomStringUtils.randomAlphanumeric(94);
        RESPOSE_STATUS = RandomStringUtils.randomNumeric(3);
        REQUEST_METHOD = RandomStringUtils.randomAlphanumeric(4);
        REQUEST_BODY = RandomStringUtils.randomAlphanumeric(100);
        RESPONDE_BODY = RandomStringUtils.randomAlphanumeric(600);
        TIME_TAKEN = RandomStringUtils.randomNumeric(5).hashCode();
    }

    @Benchmark
    public void stringFormat() {
        for (int i = 0; i < 1000000; i++) {
            var string = String.format("username: %s, status_code: %s, request_uri: %s, method: %s, request_payload: %s, response_payload: %s, time_taken: %s",
                    AUTH_PRINCIPAL, RESPOSE_STATUS, RESPONSE_URI, REQUEST_METHOD, REQUEST_BODY, RESPONDE_BODY, TIME_TAKEN);
        }
    }

    @Benchmark
    public void stringBuilder() {
        for (int i = 0; i < 1000000; i++) {
            var string = new StringBuilder()
                    .append("username: ").append(AUTH_PRINCIPAL)
                    .append(", status_code: ").append(RESPOSE_STATUS)
                    .append(", request_uri: ").append(RESPONSE_URI)
                    .append(", method: ").append(REQUEST_METHOD)
                    .append(", request_payload: ").append(REQUEST_BODY)
                    .append(", response_payload: ").append(RESPONDE_BODY)
                    .append(", time_taken: ").append(TIME_TAKEN)
                    .toString();
        }
    }

    @Benchmark
    public void stringConcat() {
        for (int i = 0; i < 1000000; i++) {
            var string = "username: " + AUTH_PRINCIPAL +
                    ", status_code: " + RESPOSE_STATUS +
                    ", request_uri: " + RESPONSE_URI +
                    ", method: " + REQUEST_METHOD +
                    ", request_payload: " + REQUEST_BODY +
                    ", response_payload: " + RESPONDE_BODY +
                    ", time_taken: " + TIME_TAKEN;
        }
    }

    @Benchmark
    public void stringBuffer() {
        for (int i = 0; i < 1000000; i++) {
            var string = new StringBuffer()
                    .append("username: ").append(AUTH_PRINCIPAL)
                    .append(", status_code: ").append(RESPOSE_STATUS)
                    .append(", request_uri: ").append(RESPONSE_URI)
                    .append(", method: ").append(REQUEST_METHOD)
                    .append(", request_payload: ").append(REQUEST_BODY)
                    .append(", response_payload: ").append(RESPONDE_BODY)
                    .append(", time_taken: ").append(TIME_TAKEN)
                    .toString();
        }
    }

    @Benchmark
    public void stringBlock() {
        for (int i = 0; i < 1000000; i++) {
            var string = """
                    username: %s, status_code: %s, request_uri: %s, method: %s, request_payload: %s, response_payload: %s, time_taken: %s
                    """.formatted(AUTH_PRINCIPAL, RESPOSE_STATUS, RESPONSE_URI, REQUEST_METHOD, REQUEST_BODY, RESPONDE_BODY, TIME_TAKEN);
        }
    }
}
