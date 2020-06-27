package br.ufu.kaiosouza.javabenchmarks;

import org.openjdk.jmh.annotations.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
Benchmark                                     (name)  Mode  Cnt    Score   Error  Units
fromStringJDK9  1ad3b484-ed7e-4bbf-8880-a9d9d692d6a4  avgt    5  165,442 ± 2,184  ns/op
fromStringJDK8  1ad3b484-ed7e-4bbf-8880-a9d9d692d6a4  avgt    5  394,961 ± 1,799  ns/op

JDK9 = 0,000165442 seconds
JDK8 = 0,000394961 seconds

0,000229519 seconds difference, JDK9 method is 238,7% faster than JDK8
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class UUIDParser {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Param({"1ad3b484-ed7e-4bbf-8880-a9d9d692d6a4"})
    public String name;

    /*
        JDK 8 way to parse a string to UUID
     */
    @Benchmark
    public UUID fromStringJDK8() {
        String[] components = name.split("-");
        if (components.length != 5)
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        for (int i = 0; i < 5; i++)
            components[i] = "0x" + components[i];

        long mostSigBits = Long.decode(components[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]).longValue();

        long leastSigBits = Long.decode(components[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]).longValue();

        return new UUID(mostSigBits, leastSigBits);
    }

    @Benchmark
    public UUID fromStringJDK9() {
        int len = name.length();
        if (len > 36) {
            throw new IllegalArgumentException("UUID string too large");
        }

        int dash1 = name.indexOf('-');
        int dash2 = name.indexOf('-', dash1 + 1);
        int dash3 = name.indexOf('-', dash2 + 1);
        int dash4 = name.indexOf('-', dash3 + 1);
        int dash5 = name.indexOf('-', dash4 + 1);

        // For any valid input, dash1 through dash4 will be positive and dash5
        // negative, but it's enough to check dash4 and dash5:
        // - if dash1 is -1, dash4 will be -1
        // - if dash1 is positive but dash2 is -1, dash4 will be -1
        // - if dash1 and dash2 is positive, dash3 will be -1, dash4 will be
        //   positive, but so will dash5
        if (dash4 < 0 || dash5 >= 0) {
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        }

        long mostSigBits = Long.parseLong(name, 0, dash1, 16) & 0xffffffffL;
        mostSigBits <<= 16;
        mostSigBits |= Long.parseLong(name, dash1 + 1, dash2, 16) & 0xffffL;
        mostSigBits <<= 16;
        mostSigBits |= Long.parseLong(name, dash2 + 1, dash3, 16) & 0xffffL;
        long leastSigBits = Long.parseLong(name, dash3 + 1, dash4, 16) & 0xffffL;
        leastSigBits <<= 48;
        leastSigBits |= Long.parseLong(name, dash4 + 1, len, 16) & 0xffffffffffffL;

        return new UUID(mostSigBits, leastSigBits);
    }

}
