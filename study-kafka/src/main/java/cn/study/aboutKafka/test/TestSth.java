package cn.study.aboutKafka.test;

import lombok.Data;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jixuelei
 * @date 2021/1/18
 */
public class TestSth {

    @Test
    public void t1() {
        Set<Double> sets = new HashSet<>(1000);
        for (int i = 0; i < 1000; i++) {
            double random = Math.random();
            sets.add(random);
        }
        System.out.println("sets size: " + sets.size());
        sets.forEach(System.out::println);
    }

    @Test
    public void t2() {
        SecureRandom secRandom = new SecureRandom();
        SecureRandom secRandom2 = new SecureRandom();

        System.out.println(secRandom.nextLong());
        System.out.println(secRandom2.nextLong());
    }

    @Test
    public void t3() {
        tt(0);
    }

    void tt(int type) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        switch (type){
            case 0:
                try {
                    Date sss = format.parse("sss");
                    System.out.println(sss);
                } catch (ParseException e) {
                    e.printStackTrace();
                    break;
                }
            case 1:
                System.out.println(type);
                break;
            default:
                System.out.println("default");
        }
    }


    @Test
    public void t4() {
        System.out.println(KamsAuditStatusEnum.A.label);
    }

    enum KamsAuditStatusEnum {

        W("W", "待提交"),
        P("P", "待审核"),
        A("A", "已通过"),
        R("R", "已驳回");

        public final String value;
        private final String label;

        KamsAuditStatusEnum(String value, String label) {
            this.value = value;
            this.label = label;
        }
    }

}


