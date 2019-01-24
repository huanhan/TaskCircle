package com.tc;

import com.tc.db.entity.User;
import com.tc.dto.enums.DateType;
import com.tc.dto.finance.QueryIE;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.task.QueryTaskInterflow;
import com.tc.dto.user.DateCondition;
import com.tc.dto.user.ModifyPassword;
import com.tc.dto.user.ModifyUserHeader;
import com.tc.dto.user.contact.AddContact;
import com.tc.until.IdGenerator;
import com.tc.until.ListUtils;
import com.tc.until.TimestampHelper;
import com.tc.until.TranstionHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextRuntime {
    public static void main(String[] object){
        System.out.println(IdGenerator.INSTANCE.nextId());

        Float f1 = 2f;
        Float f2 = 1.2f;

        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());

        System.out.println(b1.subtract(b2).floatValue());
        System.out.println(b1.add(b2).floatValue());
        System.out.println(b1.multiply(b2).floatValue());
        System.out.println(b1.remainder(b2).floatValue());
        System.out.println(b1.divide(b2,3,0).floatValue());


        Timestamp now = new Timestamp(System.currentTimeMillis());


        LocalDateTime localDateTime = now.toLocalDateTime();
        LocalDate localDate = LocalDate.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth());
        int year = localDateTime.getYear();
        int monty = localDateTime.getMonth().getValue();
        int day = localDateTime.getDayOfMonth();

        System.out.println(year + "/" + monty + "/" + day);
        Timestamp dayBegin = new Timestamp(Date.from(LocalDate.of(year,11,day).atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        System.out.println(dayBegin.getTime());

        if(TimestampHelper.isToday(dayBegin)){
            System.out.println("是当天");
        }else {
            System.out.println("不是当天");
        }

        System.out.println(TimestampHelper.toLocalDate(now).toString());

        System.out.println(TimestampHelper.todayEndByTimestamp());
        System.out.println(localDate.lengthOfMonth());

        System.out.println(TimestampHelper.differByMinute(new Timestamp(System.currentTimeMillis()),
                TimestampHelper.toTimestamp(LocalDateTime.of(2019,1,18,13,50))));

        user();


        List<String> ss = new ArrayList<>();
        ss.add("1");
        ss.add("2");
        ss.add("3");
        ss.add("4");
        ss.add("wo");
        List<Long> ls = ListUtils.to(ss);

        ls.forEach(System.out::println);



        long minue = TimestampHelper.differByMonth(now,
                TimestampHelper.toTimestamp(LocalDateTime.of(2019,2,28,13,50)));
        System.out.println(minue);


        dateCondition(
                now,
                TimestampHelper.toTimestamp(LocalDateTime.of(2019,2,28,13,50)),
                DateType.HOURS
        );

        printGson(new QueryHunterTask());
    }


    public static void user(){
        Field[] fields = User.class.getFields();
        for (Field field:
             fields) {
            try {
                System.out.println(field.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void dateCondition(Timestamp begin, Timestamp end, DateType type){
        DateCondition dateCondition = new DateCondition();
        dateCondition.setBegin(begin);
        dateCondition.setEnd(end);
        dateCondition.setType(type);
        DateCondition.reset(dateCondition);
        System.out.println("开始时间：" + dateCondition.getBegin());
        System.out.println("结束时间：" + dateCondition.getEnd());
        System.out.println("类型：" + dateCondition.getType().name());

    }

    public static void printGson(Object o){
        String json = TranstionHelper.toGson(o);
        System.out.println(json);
    }
}
