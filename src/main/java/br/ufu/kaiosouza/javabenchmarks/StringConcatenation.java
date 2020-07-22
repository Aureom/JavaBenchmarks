package br.ufu.kaiosouza.javabenchmarks;

import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/*
======================== 100 strings ========================
Benchmark      Mode  Cnt     Score     Error  Units
stringBuilder  avgt    5  1287,515 ±  31,738  ns/op
stringConcat   avgt    5  5103,256 ± 327,179  ns/op

stringBuilder 0,000001287515 seconds
stringConcat 0,000005103256 seconds

0,0038 seconds difference, stringBuilder is 296,3% faster
=============================================================

======================= 10000 strings =======================
Benchmark      Mode  Cnt         Score       Error  Units
stringBuilder  avgt    5    133594,747 ±  2915,532  ns/op
stringConcat   avgt    5  44270142,382 ± 99648,120  ns/op

stringBuilder 0,000133594747 seconds
stringConcat 0,044270142382 seconds

0,0441 seconds difference, stringBuilder is 33.037,6% faster
=============================================================
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class StringConcatenation {

    public ArrayList<String> strings = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Setup
    public void setup(){
        for (int i = 0; i < 10000; i++){
            strings.add(RandomStringUtils.randomAlphanumeric(5));
        }
    }

    @Benchmark
    public String stringConcat() {
        String string = "";

        for(String str : strings){
            string = string + str;
        }

        return string;
    }

    @Benchmark
    public String stringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();

        for(String str : strings){
            stringBuilder.append(str);
        }

        return stringBuilder.toString();
    }
}
